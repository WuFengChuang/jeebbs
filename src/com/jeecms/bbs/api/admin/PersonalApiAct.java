package com.jeecms.bbs.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.jeecms.bbs.manager.BbsUserExtMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class PersonalApiAct {
	private static final Logger log = LoggerFactory.getLogger(PersonalApiAct.class);
	
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsUserExtMng bbsUserExtMng;
	
	@RequestMapping(value="/personal/get")
	public void get(HttpServletRequest request, HttpServletResponse response) throws JSONException{
		BbsUser user = CmsUtils.getUser(request);
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (user!=null) {
			JSONObject json = convertToJson(user);
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = json.toString();
		}else{
			code = ResponseCode.API_CODE_NOT_FOUND;
			message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SignValidate
	@RequestMapping(value = "/personal/update")
	public void update(String origPwd,String newPwd,String email,String realname,
			HttpServletRequest request,HttpServletResponse response){
		BbsUser user = CmsUtils.getUser(request);
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, user.getId(),
				origPwd);
		if (!errors.hasErrors()&&bbsUserMng.isPasswordValid(user.getId(), origPwd)) {
			BbsUserExt ext = user.getUserExt();
			ext.setRealname(realname);
			bbsUserExtMng.update(ext, user);
			log.info("update BbsUser id={}",user.getId());
			bbsUserMng.updatePwdEmail(user.getId(), newPwd, email);
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+user.getId()+"\"}";
		}else{
			code = ResponseCode.API_CODE_NOT_FOUND;
			message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/personal/check_pwd")
	public void checkPwd(String origPwd,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsUser user = CmsUtils.getUser(request);
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors =WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, user.getId(), origPwd);
		if (!errors.hasErrors()) {
			boolean pass = bbsUserMng.isPasswordValid(user.getId(), origPwd);
			JSONObject json = new JSONObject();
			json.put("pass", pass);
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = json.toString();
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private JSONObject convertToJson(BbsUser user) throws JSONException {
		JSONObject json = new JSONObject();
		if (StringUtils.isNotBlank(user.getUsername())) {
			json.put("username", user.getUsername());
		}else{
			json.put("username", "");
		}
		if (StringUtils.isNotBlank(user.getRealname())) {
			json.put("realname", user.getRealname());
		}else{
			json.put("realname", "");
		}
		if (StringUtils.isNotBlank(user.getEmail())) {
			json.put("email", user.getEmail());
		}else{
			json.put("email", "");
		}
		return json;
	}
}
