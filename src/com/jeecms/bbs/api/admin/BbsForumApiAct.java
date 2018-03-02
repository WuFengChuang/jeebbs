package com.jeecms.bbs.api.admin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.manager.BbsCategoryMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsCategory;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsForumExt;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.tpl.TplManager;
import com.jeecms.core.web.CoreUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsForumApiAct {
	
	private static final Logger log = LoggerFactory
			.getLogger(BbsForumApiAct.class);
	
	/**
	 * 分区版块列表API
	 * @param https  url返回格式    1 http格式   0 https格式
	 * @param categoryId  分区ID  必选
	 */
	@RequestMapping(value = "/forum/list")
	public void forumList(
			Integer https,Integer categoryId,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		Integer siteId=CmsUtils.getSiteId(request);
		if(https==null){
			https=Constants.URL_HTTP;
		}
		List<BbsForum> list = forumManager.getList(siteId, categoryId,null);
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson(https));
			}
		}
		String status=Constants.API_STATUS_SUCCESS;
		String message=Constants.API_MESSAGE_SUCCESS;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		String body=jsonArray.toString();
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 分区版块列表API
	 */
	@RequestMapping(value = "/forum/listGroupByCategory")
	public void forumListGroupByCategory(
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		Integer siteId=CmsUtils.getSiteId(request);
		List<BbsCategory> list = bbsCategoryMng.getList(siteId);
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				//jsonArray.put(i, list.get(i).convertToJson(https));
				BbsCategory c=list.get(i);
				JSONObject cateJson=new JSONObject();
				cateJson.put("id",c.getId());
				cateJson.put("path", c.getPath());
				cateJson.put("title", c.getTitle());
				cateJson.put("priority", c.getPriority());
				JSONArray forumArray=new JSONArray();
				Set<BbsForum>forums=c.getForums();
				Iterator<BbsForum>it=forums.iterator();
				int j=0;
				while(it.hasNext()){
					BbsForum f=it.next();
					JSONObject forumJson=new JSONObject();
					forumJson.put("id", f.getId());
					forumJson.put("title", f.getTitle());
					forumJson.put("path", f.getPath());
					forumJson.put("priority", f.getPriority());
					forumArray.put(j++, forumJson);
				}
				cateJson.put("forums", forumArray);
				jsonArray.put(i, cateJson);
			}
		}
		String status=Constants.API_STATUS_SUCCESS;
		String message=Constants.API_MESSAGE_SUCCESS;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		String body=jsonArray.toString();
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 获取版块信息
	 * @param https  url返回格式    1 http格式   0 https格式
	 * @param id 版块id
	 */
	@RequestMapping(value = "/forum/get")
	public void forumGet(Integer id,Integer https,
			HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		BbsForum forum = null;
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		String body="\"\"";
		if(https==null){
			https=Constants.URL_HTTP;
		}
		if (id != null) {
			if (id.equals(0)) {
				forum = new BbsForum();
			}else{
				forum = forumManager.findById(id);
			}
			if (forum != null) {
				JSONObject json=forum.convertToJson(https);
				status=Constants.API_STATUS_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
				body=json.toString();
			} else {
				code=ResponseCode.API_CODE_PARAM_ERROR;
				message=Constants.API_MESSAGE_PARAM_ERROR;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/forum/save")
	public void save(BbsForum bean,BbsForumExt ext,
			Integer categoryId,String views,
			String topics,String replies, 
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		// 加上模板前缀
		String tplPath = site.getTplPath();
		if (!StringUtils.isBlank(ext.getTplForum())) {
			ext.setTplForum(tplPath + ext.getTplForum());
		}
		if (!StringUtils.isBlank(ext.getTplTopic())) {
			ext.setTplTopic(tplPath + ext.getTplTopic());
		}
		if (!StringUtils.isBlank(ext.getTplMobileForum())) {
			ext.setTplMobileForum(tplPath + ext.getTplMobileForum());
		}
		if (!StringUtils.isBlank(ext.getTplMobileTopic())) {
			ext.setTplMobileTopic(tplPath + ext.getTplMobileTopic());
		}
		bean.init();
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, categoryId,
				bean.getTitle(),bean.getPath());
		if(!errors.hasErrors()){
			//验证板块路径是否已存在
			BbsForum path = forumManager.getByPath(site.getId(), bean.getPath());
			if (path==null) {
				Integer[]viewArray=StrUtils.getInts(views);
				Integer[]topicArray=StrUtils.getInts(topics);
				Integer[]replieArray=StrUtils.getInts(replies);
				bean = forumManager.save(bean,ext, categoryId, site, viewArray, topicArray, replieArray);
				log.info("save BbsForum id={}", bean.getId());
				status=Constants.API_STATUS_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
				body="{\"id\":"+"\""+bean.getId()+"\"}";
			}else{
				message=Constants.API_MESSAGE_FORUM_PATH_EXIST;
				code = ResponseCode.API_CODE_FORUM_PATH_EXIST;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/forum/update")
	public void update(BbsForum bean,BbsForumExt ext,
			Integer categoryId, 
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		// 加上模板前缀
		String tplPath = site.getTplPath();
		if (!StringUtils.isBlank(ext.getTplForum())) {
			ext.setTplForum(tplPath + ext.getTplForum());
		}
		if (!StringUtils.isBlank(ext.getTplTopic())) {
			ext.setTplTopic(tplPath + ext.getTplTopic());
		}
		if (!StringUtils.isBlank(ext.getTplMobileForum())) {
			ext.setTplMobileForum(tplPath + ext.getTplMobileForum());
		}
		if (!StringUtils.isBlank(ext.getTplMobileTopic())) {
			ext.setTplMobileTopic(tplPath + ext.getTplMobileTopic());
		}
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors,bean.getId(),categoryId);
		if(!errors.hasErrors()){
			try {
				bean = forumManager.update(bean,ext,categoryId);
				log.info("update BbsForum id={}", bean.getId());
				status=Constants.API_STATUS_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
				body="{\"id\":"+"\""+bean.getId()+"\"}";
			} catch (Exception e) {
				code=ResponseCode.API_CODE_PARAM_ERROR;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	@SignValidate
	@RequestMapping("/forum/batchupdate")
	public void batchUpdate(String categorys,
			String forums,
			HttpServletRequest request, HttpServletResponse response,ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site=CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors,categorys,forums);
		if(!errors.hasErrors()){
			boolean cateParamSucc=true;
			boolean forumParamSucc=true;
			String[]categorysArray=categorys.split(";");
			String[]forumsArray=forums.split(";");
			Map<String,Integer>saveCateIdMap=new HashMap<>();
			if(categorysArray!=null&&categorysArray.length>0){
				cateParamSucc=validateCateParam(categorysArray);
				if(cateParamSucc){
					for(String c:categorysArray){
						String[]category=c.split(",");
							String cid=category[0];
							if(StringUtils.isNumeric(cid)){
								BbsCategory cate=bbsCategoryMng.findById(Integer.parseInt(cid));
								if(cate!=null){
									cate.setTitle(category[1]);
									cate.setPath(category[2]);
									bbsCategoryMng.update(cate);
								}else{
									//添加
									cate=new BbsCategory();
									cate.setTitle(category[1]);
									cate.setPath(category[2]);
									cate.setSite(site);
									cate.init();
									cate=bbsCategoryMng.save(cate);
								}
								saveCateIdMap.put(cid, cate.getId());
							}
					}
				}
			}
			if(forumsArray!=null&&forumsArray.length>0){
				forumParamSucc=validateForumParam(forumsArray);
				if(cateParamSucc&&forumParamSucc){
					for(String f:forumsArray){
						String[]forum=f.split(",");
							if(forum.length==4){
								//修改
								String fid=forum[3];
								if(StringUtils.isNumeric(fid)&&StringUtils.isNumeric(forum[2])){
									BbsForum forumBean=forumManager.findById(Integer.parseInt(fid));
									forumBean.setTitle(forum[0]);
									forumBean.setPath(forum[1]);
									forumBean.setCategory(bbsCategoryMng.findById(Integer.parseInt(forum[2])));
									forumManager.update(forumBean);
								}
							}else if(forum.length==3){
								//添加
								Integer dbCateId=saveCateIdMap.get(forum[2]);
								if(dbCateId!=null){
									BbsForum forumBean=new BbsForum();
									forumBean.setTitle(forum[0]);
									forumBean.setPath(forum[1]);
									forumBean.setCategory(bbsCategoryMng.findById(dbCateId));
									forumBean.setSite(site);
									forumBean.init();
									BbsForumExt ext=new BbsForumExt();
									forumManager.save(forumBean,ext, dbCateId, site, null, null, null);
								}
							}
					}
				}
			}
			if(forumParamSucc&&cateParamSucc){
				status=Constants.API_STATUS_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				 message=Constants.API_MESSAGE_PARAM_ERROR;
				 code=ResponseCode.API_CODE_PARAM_ERROR;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/forum/delete")
	public void delete(String ids,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, ids);
		Short topLevel=0;
		if(!errors.hasErrors()){
			Integer[]idArray=StrUtils.getInts(ids);
			try {
				for (int i = 0; i < idArray.length; i++) {
					List<BbsTopic> list = bbsTopicMng.getList(idArray[i],null,null,topLevel,0,Integer.MAX_VALUE);
					for (int j = 0; j < list.size(); j++) {
						BbsTopic topic = bbsTopicMng.deleteById(list.get(j).getId());
						log.info("delete BbsTopic id={}", topic.getId());
					}
				}
				BbsForum[] beans = forumManager.deleteByIds(idArray);
				for (BbsForum bean : beans) {
					log.info("delete BbsForum id={}", bean.getId());
				}
				status=Constants.API_STATUS_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				status=Constants.API_MESSAGE_DELETE_ERROR;
				message=Constants.API_MESSAGE_DELETE_ERROR;
				code=ResponseCode.API_CODE_DELETE_ERROR;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/forum/o_priority_update")
	public void priorityMoveUpdate(String ids,
			HttpServletRequest request,	
			HttpServletResponse response,  ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsForum t;
		Integer id;
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, ids);
		if(!errors.hasErrors()){
			String[]fid=ids.split(",");
			for (int i = 0; i < fid.length; i++) {
				id = Integer.parseInt(fid[i]);
				if (id != null) {
					t = forumManager.findById(id);
					if (t != null && t.getSite().getId().equals(site.getId())) {
						t.setPriority(i);
						forumManager.update(t);
					}
				}
			}
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/forum/getTpls")
	public void getTplList(HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		CmsSite site=CmsUtils.getSite(request);
		// 主题列表模板列表
		List<String> forumTplList = getTplForum(request, site, null);
		// 主题详细页模板列表
		List<String> topicTplList = getTplTopic(request, site, null);
		//手机主题列表模板列表
		List<String> mobileForumTplList = getMobileTplForum(request, site, null);
		//手机主题详细页模板列表
		List<String> mobileTopicTplList = getMobileTplTopic(request, site, null);
		JSONObject json=new JSONObject();
		JSONArray forumTplJson=new JSONArray();
		if(forumTplList!=null&&forumTplList.size()>0){
			for(int i=0;i<forumTplList.size();i++){
				forumTplJson.put(i, forumTplList.get(i));
			}
		}
		json.put("forumTpl", forumTplJson.toString());
		JSONArray topicTplJson=new JSONArray();
		if(topicTplList!=null&&topicTplList.size()>0){
			for(int i=0;i<topicTplList.size();i++){
				topicTplJson.put(i, topicTplList.get(i));
			}
		}
		json.put("topicTpl", topicTplJson.toString());
		JSONArray mobileForumTplJson=new JSONArray();
		if(mobileForumTplList!=null&&mobileForumTplList.size()>0){
			for(int i=0;i<mobileForumTplList.size();i++){
				mobileForumTplJson.put(i, mobileForumTplList.get(i));
			}
		}
		json.put("mobileForumTpl", mobileForumTplJson.toString());
		JSONArray mobileTopicTplJson=new JSONArray();
		if(mobileTopicTplList!=null&&mobileTopicTplList.size()>0){
			for(int i=0;i<mobileTopicTplList.size();i++){
				mobileTopicTplJson.put(i, mobileTopicTplList.get(i));
			}
		}
		json.put("mobileTopicTpl", mobileTopicTplJson.toString());
		String status=Constants.API_STATUS_SUCCESS;
		String message=Constants.API_MESSAGE_SUCCESS;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		String body=json.toString();
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private List<String> getTplForum(HttpServletRequest request,CmsSite site,
			String tpl) {
		List<String> tplList = tplManager.getNameListByPrefix(BbsForum.getTplForumDef(site.getSolutionPath(),false,request));
		return CoreUtils.tplTrim(tplList, site.getTplPath(), tpl);
	}
	
	private List<String> getMobileTplForum(HttpServletRequest request,
			CmsSite site,String tpl) {
		List<String> tplList = tplManager.getNameListByPrefix(BbsForum.getTplForumDef(site.getMobileSolutionPath(),false,request));
		return CoreUtils.tplTrim(tplList, site.getTplPath(), tpl);
	}

	private List<String> getTplTopic(HttpServletRequest request,CmsSite site,
			String tpl) {
		List<String> tplList = tplManager.getNameListByPrefix(BbsForum.getTplTopicDef(site.getSolutionPath(),false,request));
		return CoreUtils.tplTrim(tplList, site.getTplPath(), tpl);
	}
	
	private List<String> getMobileTplTopic(HttpServletRequest request,
			CmsSite site,String tpl) {
		List<String> tplList = tplManager.getNameListByPrefix(BbsForum.getTplTopicDef(site.getMobileSolutionPath(),false,request));
		return CoreUtils.tplTrim(tplList, site.getTplPath(), tpl);
	}
	
	private boolean validateCateParam(String[]categorysArray){
		boolean cateParamSucc=true;
		for(String c:categorysArray){
			String[]category=c.split(",");
			if(category!=null&&category.length>0){
				if(category.length==3){
					String cid=category[0];
					if(!StringUtils.isNumeric(cid)||StringUtils.isBlank(category[1])
							||StringUtils.isBlank(category[2])){
						cateParamSucc=false;
						break;
					}
				}else{
					cateParamSucc=false;
				}
			}else{
				cateParamSucc=false;
			}
		}
		return cateParamSucc;
	}
	
	private boolean validateForumParam(String forumsArray[]){
		boolean forumParamSucc=true;
		for(String f:forumsArray){
			String[]forum=f.split(",");
			if(forum==null||(forum.length!=3&&forum.length!=4)){
				forumParamSucc=false;
			}
			if(forum!=null){
				if(forum.length==4){
					if(StringUtils.isBlank(forum[0])||StringUtils.isBlank(forum[1])
							||!StringUtils.isNumeric(forum[3])
							||!StringUtils.isNumeric(forum[2])){
						forumParamSucc=false;
					}
				}else if(forum.length==3){
					if(StringUtils.isBlank(forum[0])||StringUtils.isBlank(forum[1])
							||!StringUtils.isNumeric(forum[2])){
						forumParamSucc=false;
					}
				}
			}
		}
		return forumParamSucc;
	}
	
	@Autowired
	private BbsForumMng forumManager;
	@Autowired
	private BbsTopicMng bbsTopicMng;
	@Autowired
	private BbsCategoryMng bbsCategoryMng;
	@Autowired
	private TplManager tplManager;
}

