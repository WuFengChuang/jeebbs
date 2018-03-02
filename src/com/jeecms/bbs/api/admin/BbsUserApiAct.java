package com.jeecms.bbs.api.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.entity.BbsWebservice;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.manager.BbsWebserviceMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsUserApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsUserApiAct.class);
	@Autowired
	private BbsUserMng manager;
	@Autowired
	private BbsWebserviceMng bbsWebserviceMng;
	
	/**
	 * 会员列表
	 * @param https
	 * @param username 按用户名查询
	 * @param groupId 按会员组编号查询
	 * @param lastLoginDay 距离最后一次登录的天数
	 * @param orderBy 排序规则
	 * @param pageNo 第pageNo页
	 * @param pageSize 每页总数
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/user/list")
	public void userList(Integer https,String username,Integer groupId,Integer lastLoginDay
			,Integer orderBy,Integer pageNo,Integer pageSize,Boolean all,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if(https==null){
			https=Constants.URL_HTTP;
		}
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		Pagination page;
		if (all!=null&&all) {
			page = manager.getPage(username, null, groupId, false,
					null,false,null,user.getRank(),null, pageNo,
					pageSize);
		}else{
			page = manager.getPage(username, null, groupId, null, false, false, lastLoginDay, 
					user.getRank(), orderBy, pageNo, pageSize);
		}
		List<BbsUser> list = (List<BbsUser>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson(site, https));
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 会员、管理员详情
	 * @param id 会员、管理员编号
	 * @param https
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value="/user/get")
	public void userGet(Integer id,Integer https,
			HttpServletRequest request, HttpServletResponse response) throws JSONException{
		BbsUser user = null;
		CmsSite site = CmsUtils.getSite(request);
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if(https==null){
			https=Constants.URL_HTTP;
		}
		if (id!=null) {
			if (id.equals(0)) {
				user = new BbsUser();
			}else{
				user = manager.findById(id);
			}
			if (user!=null) {
				JSONObject json = user.convertToJson(site, https);
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
	 * 会员添加
	 * @param bean
	 * @param ext
	 * @param username 用户名
	 * @param email 电子邮箱
	 * @param password 密码
	 * @param groupId 会员组编号
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/user/save")
	public void save(BbsUser bean,BbsUserExt ext,String username,String email,
			String password,Integer groupId,Boolean official,
			HttpServletRequest request,HttpServletResponse response){
		String ip = RequestUtils.getIpAddr(request);
		Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		bean.init();
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				username,password,groupId,ip);
		if (!errors.hasErrors()) {
			try {
				bean = manager.registerMember(username, email, official, password, ip, groupId, ext, attrs);
				callWebService(username, password, email, ext,BbsWebservice.SERVICE_TYPE_ADD_USER);
				log.info("save BbsUser id={}",bean.getId());
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 会员修改
	 * @param bean
	 * @param ext
	 * @param email 电子邮箱
	 * @param password 密码
	 * @param groupId 会员组编号
	 * @param disabled 禁用标识
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping("/user/update")
	public void update(BbsUser bean,BbsUserExt ext,String email,
			String password,Integer groupId,Boolean disabled,
			HttpServletRequest request,HttpServletResponse response){
		Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		bean.init();
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				groupId,disabled);
		if (!errors.hasErrors()) {
			try {
				bean = manager.updateMember(bean.getId(), email, password, disabled, null, null, ext, attrs, groupId);
				callWebService(bean.getUsername(), password, email, ext,BbsWebservice.SERVICE_TYPE_UPDATE_USER);
				log.info("update BbsUser id={}",bean.getId());
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 会员、管理员删除
	 * @param ids 会员、管理员编号字符串(多个编号用逗号隔开)
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/user/delete")
	public void delete(String ids,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				BbsUser[] deleteByIds = manager.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					Map<String,String>paramsValues=new HashMap<String, String>();
					paramsValues.put("username", deleteByIds[i].getUsername());
					bbsWebserviceMng.callWebService(BbsWebservice.SERVICE_TYPE_DELETE_USER, paramsValues);
					log.info("delete BbsUser id={}",deleteByIds[i]);
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
	 * 官网团队账户列表
	 * @param https
	 * @param pageNo 第pageNo页
	 * @param pageSize 每页总数
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/user/official_list")
	public void officialList(Integer https,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsUser user = CmsUtils.getUser(request);
		CmsSite site = CmsUtils.getSite(request);
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		Pagination page = manager.getPage(null, null, null, null, false, true, null, user.getRank(), null, pageNo, pageSize);
		List<BbsUser> list = (List<BbsUser>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson(site, https));
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 根据用户名查询用户是否存在
	 * @param username
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/user/comparison_username")
	public void findByUsername(String username,
			HttpServletRequest request,HttpServletResponse response){
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsUser user = null;
		if (username!=null) {
			user = manager.findByUsername(username);
			if (user!=null) {
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body += ",\"result\":"+true;
			}else{
				code = ResponseCode.API_CODE_NOT_FOUND;
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				body += ",\"result\":"+false;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private void callWebService(String username,String password,String email,BbsUserExt userExt,String operate){
		if(bbsWebserviceMng.hasWebservice(operate)){
			Map<String,String>paramsValues=new HashMap<String, String>();
			paramsValues.put("username", username);
			paramsValues.put("password", password);
			if(StringUtils.isNotBlank(email)){
				paramsValues.put("email", email);
			}
			if(StringUtils.isNotBlank(userExt.getRealname())){
				paramsValues.put("realname", userExt.getRealname());
			}
			if(userExt.getGender()!=null){
				paramsValues.put("sex", userExt.getGender().toString());
			}
			if(StringUtils.isNotBlank(userExt.getMoble())){
				paramsValues.put("tel",userExt.getMoble());
			}
			bbsWebserviceMng.callWebService(operate, paramsValues);
		}
	}
	
}
