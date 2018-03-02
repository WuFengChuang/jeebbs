package com.jeecms.bbs.api.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsCategoryApiAct {
	
	private static final Logger log = LoggerFactory
			.getLogger(BbsCategoryApiAct.class);
	
	/**
	 * 分区列表API
	 */
	@RequestMapping(value = "/category/list")
	public void categoryList(HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		Integer siteId=CmsUtils.getSiteId(request);
		List<BbsCategory> list = bbsCategoryMng.getList(siteId);
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson());
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
	 * 获取分区信息
	 * @param id 版块id
	 */
	@RequestMapping(value = "/category/get")
	public void categoryGet(Integer id,HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		BbsCategory category = null;
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		String body="\"\"";
		if (id != null) {
			if(id.equals(0)){
				category=new BbsCategory();
			}else{
				category = bbsCategoryMng.findById(id);
			}
			if (category != null) {
				JSONObject json=category.convertToJson();
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
	@RequestMapping("/category/save")
	public void save(BbsCategory category, 
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors,
				category.getTitle(),category.getPath());
		if(!errors.hasErrors()){
			category.setSite(site);
			category.init();
			bbsCategoryMng.save(category);
			log.info("save BbsCategory id={}", category.getId());
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
			body="{\"id\":"+"\""+category.getId()+"\"}";
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/category/update")
	public void update(BbsCategory category,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors,category.getId());
		if(!errors.hasErrors()){
			bbsCategoryMng.update(category);
			log.info("update BbsForum id={}", category.getId());
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
			body="{\"id\":"+"\""+category.getId()+"\"}";
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/category/delete")
	public void delete(String ids,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, ids);
		Short topLevel=0;
		if(!errors.hasErrors()){
			Integer[]idArray=StrUtils.getInts(ids);
			try {
				for (int i = 0; i < idArray.length; i++) {
					List<BbsForum> listForum = forumManager.getList(site.getId(), idArray[i],null);
					Integer[] forumIds = new Integer[listForum.size()];
					for (int ii = 0; ii < listForum.size(); ii++) {
						forumIds[ii]=listForum.get(ii).getId();
						List<BbsTopic> listTopic = bbsTopicMng.getList(listForum.get(ii).getId(),
								null,null,topLevel,0,Integer.MAX_VALUE);
						for (int j = 0; j < listTopic.size(); j++) {
							BbsTopic topic = bbsTopicMng.deleteById(listTopic.get(j).getId());
							log.info("delete BbsTopic id={}", topic.getId());
						}
					}
					BbsForum[] beans = forumManager.deleteByIds(forumIds);
					for (BbsForum bean : beans) {
						log.info("delete BbsForum id={}", bean.getId());
					}
				}
				BbsCategory[] beans = bbsCategoryMng.deleteByIds(idArray);
				for (BbsCategory bean : beans) {
					log.info("delete BbsCategory id={}", bean.getId());
				}
				status=Constants.API_STATUS_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			}  catch (Exception e) {
				status=Constants.API_MESSAGE_DELETE_ERROR;
				message=Constants.API_MESSAGE_DELETE_ERROR;
				code=ResponseCode.API_CODE_DELETE_ERROR;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/category/o_priority_update")
	public void priorityMoveUpdate(String ids,
			HttpServletRequest request,	
			HttpServletResponse response,  ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsCategory t;
		Integer id;
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, ids);
		if(!errors.hasErrors()){
			String[]cids=ids.split(",");
			for (int i = 0; i < cids.length; i++) {
				id = Integer.parseInt(cids[i]);
				if (id != null) {
					t = bbsCategoryMng.findById(id);
					if (t != null && t.getSite().getId().equals(site.getId())) {
						t.setPriority(i);
						bbsCategoryMng.update(t);
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
	
	@Autowired
	private BbsForumMng forumManager;
	@Autowired
	private BbsTopicMng bbsTopicMng;
	@Autowired
	private BbsCategoryMng bbsCategoryMng;
}

