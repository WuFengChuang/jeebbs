package com.jeecms.bbs.api.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsUserGroupApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsUserGroupApiAct.class);
	
	@Autowired
	private BbsUserGroupMng groupManager;
	@Autowired
	private BbsForumMng forumManager;
	
	/**
	 * 会员组列表显示
	 * @param groupType 会员组类型
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/group/list")
	public void groupList(Short groupType,Integer https,
			HttpServletRequest request,HttpServletResponse response)
				throws JSONException{
		CmsSite site = CmsUtils.getSite(request);
		if (groupType==null) {//默认会员组类型为1
			groupType=1;
		}
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		List<BbsUserGroup> list = new ArrayList<>();
		if (groupType!=-1) {
			list = groupManager.getList(site.getId(),groupType);
		}else{
			list = groupManager.getList(site.getId());
		}
		JSONArray jsonArray = new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i = 0 ; i < list.size() ; i++){
				jsonArray.put(i,list.get(i).convertToJson(site,https));
				
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 会员组个体详情显示
	 * @param id 会员组id
	 * @param response
	 * @param request
	 * @throws JSONException
	 */
	@RequestMapping(value = "/group/get")
	public void groupGet(Integer id,Short type,Integer https,
			HttpServletResponse response,HttpServletRequest request) throws JSONException{
		BbsUserGroup group = null;
		CmsSite site = CmsUtils.getSite(request);
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (https==null) {
			https=Constants.URL_HTTP;
		}
		if(id!=null){
			if (id.equals(0)) {
				group = new BbsUserGroup();
				group.setType(type);
				group.init();
			}else{
				group = groupManager.findById(id);
			}
			if (group!=null) {
				JSONObject json = group.convertToJson(site,https);
				//查询指定会员组的权限
				List<BbsForum> list = forumManager.getList(CmsUtils.getSiteId(request));
				String forumViewIds="",forumTopicIds="",forumReplyIds="";
				Integer[]forumGroupViewIds,forumGroupTopicIds,forumGroupReplyIds;
				for (BbsForum forum : list) {
					if (StringUtils.isNotBlank(forum.getGroupViews())) {
						forumGroupViewIds=splitString(forum.getGroupViews(), ",");
					}else{
						forumGroupViewIds=null;
					}
					if(forumGroupViewIds!=null){
						for(int i=0;i<forumGroupViewIds.length;i++){
							if(forumGroupViewIds[i].equals(id)){
								forumViewIds+=forum.getId()+",";
							}
						}
					}
					if (StringUtils.isNotBlank(forum.getGroupTopics())) {
						forumGroupTopicIds=splitString(forum.getGroupTopics(), ",");
					}else{
						forumGroupTopicIds=null;
					}
					if(forumGroupTopicIds!=null){
						for(int i=0;i<forumGroupTopicIds.length;i++){
							if(forumGroupTopicIds[i].equals(id)){
								forumTopicIds+=forum.getId()+",";
							}
						}
					}
					if (StringUtils.isNotBlank(forum.getGroupReplies())) {
						forumGroupReplyIds=splitString(forum.getGroupReplies(), ",");
					}else{
						forumGroupReplyIds = null;
					}
					if(forumGroupReplyIds!=null){
						for(int i=0;i<forumGroupReplyIds.length;i++){
							if(forumGroupReplyIds[i].equals(id)){
								forumReplyIds+=forum.getId()+",";
							}
						}
					}
				}
				//判断查询到的会员组权限字符串长度是否为0，根据不同情况截取字符串
				if (forumViewIds.length()==0) {
					json.put("views", forumViewIds);
				}else{
					json.put("views", forumViewIds.substring(0,forumViewIds.length()-1));
				}
				
				if (forumTopicIds.length()==0) {
					json.put("topics", forumTopicIds);
				}else{
					json.put("topics", forumTopicIds.substring(0,forumTopicIds.length()-1));
				}
				if (forumReplyIds.length()==0) {
					json.put("replies", forumReplyIds);
				}else{
					json.put("replies", forumReplyIds.substring(0,forumReplyIds.length()-1));
				}
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = json.toString();
			}else{
				code = ResponseCode.API_CODE_NOT_FOUND;
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 添加会员组
	 * @param bean 会员组实体
	 * @param groupType 会员组类型
	 * @param forumIds 板块id
	 * @param views 浏览板块权限
	 * @param topics 发新话题权限
	 * @param replies 发表回复权限
	 * @param request
	 * @param response
	 * @param model
	 */
	@SignValidate
	@RequestMapping(value = "/group/save")
	public void save(BbsUserGroup bean,Short groupType,String forumIds,String views,
			String topics,String replies,
			HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		//验证公共非空参数
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				groupType,bean.getName(),bean.getPoint(),bean.getImgPath(),forumIds);
		if (!errors.hasErrors()) {
			Integer[] forumArray = StrUtils.getInts(forumIds);
			Integer[] viewArray = StrUtils.getInts(views);
			Integer[] topicArray = StrUtils.getInts(topics);
			Integer[] replieArray = StrUtils.getInts(replies);
			bean.setSite(site);
			bean.setType(groupType);
			if (groupType==2) {
				//当groupType为2时，初始化特定的属性
				bean = systemPerms(bean);
				Map<String, String> requestMap = RequestUtils.getRequestMap(request, "perm_");
				Set<Entry<String,String>> entrySet = bean.getPerms().entrySet();
				for (Entry<String, String> entry : entrySet) {//接受并覆盖接口所传参数数据
					if (requestMap.containsKey(entry.getKey())) {
						bean.getPerms().put(entry.getKey(), requestMap.get(entry.getKey()));
					}
				}
			}
			bean = groupManager.save(bean);
			forumManager.update(bean.getId(), forumArray, viewArray, topicArray, replieArray);
			log.info("save BbsUserGroup id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 会员组删除
	 * @param ids  ,逗号分隔ID字符串
	 * @param request 
	 * @param response
	 * @param model
	 */
	@SignValidate
	@RequestMapping(value = "/group/delete")
	public void delete(String ids,
			HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = WebErrors.create(request);
		//验证公共非空参数
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				forumManager.update(site.getId(),idArray);
				BbsUserGroup[] deleteByIds = groupManager.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsUserGroup id={}",deleteByIds[i]);
				}
				status=Constants.API_STATUS_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			} catch (Exception e) {
				status = Constants.API_MESSAGE_DELETE_ERROR;
				message = Constants.API_MESSAGE_DELETE_ERROR;
				code = ResponseCode.API_CODE_DELETE_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 会员组更新
	 * @param bean 会员组对象
	 * @param groupType 会员组类型
	 * @param forumIds 权限设置-板块编号
	 * @param views 浏览板块
	 * @param topics 发新话题
	 * @param replies 发表回复
	 * @param request
	 * @param response
	 * @param model
	 */
	@SignValidate
	@RequestMapping(value = "/group/update")
	public void update(BbsUserGroup bean,Short groupType,String forumIds,String views,
			String topics,String replies,
			HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				groupType,bean.getId(),bean.getName(),bean.getPoint(),bean.getImgPath(),forumIds);
		if (!errors.hasErrors()) {
			Integer[] forumArray = StrUtils.getInts(forumIds);
			Integer[] viewArray = StrUtils.getInts(views);
			Integer[] topicArray = StrUtils.getInts(topics);
			Integer[] replieArray = StrUtils.getInts(replies);
			bean.setPerms(RequestUtils.getRequestMap(request, "perm_"));
			bean = groupManager.update(bean);
			forumManager.update(bean.getId(), forumArray, viewArray, topicArray, replieArray);
			log.info("update BbsUserGroup id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 初始化系统组独有属性值
	 * @param bean
	 * @return
	 */
	private BbsUserGroup systemPerms(BbsUserGroup bean){
		if (!bean.getPerms().containsKey("super_moderator")) {
			bean.getPerms().put("super_moderator", "false");
		}
		if (!bean.getPerms().containsKey("post_limit")) {
			bean.getPerms().put("post_limit", "true");
		}
		if (!bean.getPerms().containsKey("topic_top")) {
			bean.getPerms().put("topic_top", "true");
		}
		if (!bean.getPerms().containsKey("topic_manage")) {
			bean.getPerms().put("topic_manage", "true");
		}
		if (!bean.getPerms().containsKey("topic_edit")) {
			bean.getPerms().put("topic_edit", "true");
		}
		if (!bean.getPerms().containsKey("topic_shield")) {
			bean.getPerms().put("topic_shield", "true");
		}
		if (!bean.getPerms().containsKey("topic_delete")) {
			bean.getPerms().put("topic_delete", "true");
		}
		if (!bean.getPerms().containsKey("view_ip")) {
			bean.getPerms().put("view_ip", "true");
		}
		if (!bean.getPerms().containsKey("member_prohibit")) {
			bean.getPerms().put("member_prohibit", "true");
		}
		return bean;
	}
	
	private Integer[] splitString(String str,String split){
		if(StringUtils.isNotBlank(str)&&StringUtils.isNotBlank(split)){
			String[]ids=str.split(split);
			List<String>idList=new ArrayList<String>();
			for(String id:ids){
				if(StringUtils.isNotBlank(id)&&StringUtils.isNumeric(id)){
					idList.add(id);
				}
			}
			Integer[]results=new Integer[idList.size()];
			for(int i=0;i<idList.size();i++){
					results[i]=Integer.parseInt(idList.get(i));
			}
			return results;
		}else{
			return null;
		}
	}
}
