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
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.BbsConfigAttr;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsLiveConfigApiAct {
	private static final Logger log = LoggerFactory.getLogger(BbsLiveConfigApiAct.class);
	
	@Autowired
	private CmsConfigMng cmsConfigMng;
	
	@RequestMapping(value = "/live/config_get")
	public void get(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		CmsConfig cmsConfig = cmsConfigMng.get();
		if (cmsConfig!=null) {
			JSONObject json = convertToJson(cmsConfig);
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = json.toString();
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping(value = "/live/config_update")
	public void update(BbsConfigAttr attr,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, attr.getLiveCheck(),attr.getLivePlat());
		if (!errors.hasErrors()) {
			cmsConfigMng.updateConfigAttr(attr);
			log.info("update BbsConfigAttr");
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private JSONObject convertToJson(CmsConfig cmsConfig) throws JSONException{
		JSONObject json = new JSONObject();
		if (cmsConfig.getLiveCheck()!=null) {
			json.put("liveCheck", cmsConfig.getLiveCheck());
		}else{
			json.put("liveCheck", false);
		}
		if (StringUtils.isNotBlank(cmsConfig.getLivePlat())) {
			json.put("livePlat", cmsConfig.getLivePlat());
		}else{
			json.put("livePlat", "");
		}
		if (StringUtils.isNotBlank(cmsConfig.getTencentPushFlowKey())) {
			json.put("tencentPushFlowKey", cmsConfig.getTencentPushFlowKey());
		}else{
			json.put("tencentPushFlowKey", "");
		}
		if (StringUtils.isNotBlank(cmsConfig.getTencentBizId())) {
			json.put("tencentBizId", cmsConfig.getTencentBizId());
		}else{
			json.put("tencentBizId", "");
		}
		if (StringUtils.isNotBlank(cmsConfig.getTencentApiAuthKey())) {
			json.put("tencentApiAuthKey", cmsConfig.getTencentApiAuthKey());
		}else{
			json.put("tencentApiAuthKey", "");
		}
		if (StringUtils.isNotBlank(cmsConfig.getTencentAppId())) {
			json.put("tencentAppId", cmsConfig.getTencentAppId());
		}else{
			json.put("tencentAppId", "");
		}
		if (StringUtils.isNotBlank(cmsConfig.getBaiduPushDomain())) {
			json.put("baiduPushDomain", cmsConfig.getBaiduPushDomain());
		}else{
			json.put("baiduPushDomain", "");
		}
		if (StringUtils.isNotBlank(cmsConfig.getBaiduPlayDomain())) {
			json.put("baiduPlayDomain", cmsConfig.getBaiduPlayDomain());
		}else{
			json.put("baiduPlayDomain", "");
		}
		json.put("accessKeyId", "");
		json.put("accessKey", "");
		json.put("streamSafeKey", "");
		return json;
	}
}
