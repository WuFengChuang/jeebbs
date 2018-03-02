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
import com.jeecms.bbs.entity.ApiAccount;
import com.jeecms.bbs.entity.BbsConfig;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsCreditExchange;
import com.jeecms.bbs.manager.ApiAccountMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsCreditExchangeMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.common.security.encoder.PwdEncoder;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.BbsConfigAttr;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsConfigItem;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Config.ConfigEmailSender;
import com.jeecms.core.entity.Config.ConfigLogin;
import com.jeecms.core.entity.Config.ConfigMessageTemplate;
import com.jeecms.core.manager.CmsConfigItemMng;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.manager.ConfigMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class CmsSiteConfigApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsSiteConfigApiAct.class);
	
	@RequestMapping(value = "/config_api_pwd/validate")
	public void valAPIpwd(String pwd,HttpServletResponse response,HttpServletRequest request) throws Exception{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, pwd);
		if (!errors.hasErrors()) {
			CmsConfig config = cmsConfigMng.get();
			ApiAccount account = accountMng.getApiAccount(request);
			//密码解密
			String decryptOld = AES128Util.decrypt(pwd, account.getAesKey(), account.getIvKey());
			//旧密码加密
			String encodePassword = pwdEncoder.encodePassword(decryptOld);
			Map<String, String> map = config.getAttr();
			String apiPwd = map.get("apiAccountMngPassword");
			if (encodePassword.equals(apiPwd)) {//判断旧密码与原密码是否相同
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				status = Constants.API_STATUS_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_PASSWORD_ERROR;
				code = ResponseCode.API_CODE_PASSWORD_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SignValidate
	@RequestMapping(value = "/config_api_pwd/update")
	public void updateAPIpwd(String oldPwd,String newPwd,
			HttpServletResponse response,HttpServletRequest request) throws Exception{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, oldPwd,newPwd);
		if (!errors.hasErrors()) {
			CmsConfig config = cmsConfigMng.get();
			ApiAccount account = accountMng.getApiAccount(request);
			//密码解密
			String decryptOld = AES128Util.decrypt(oldPwd, account.getAesKey(), account.getIvKey());
			String decryptNew = AES128Util.decrypt(newPwd, account.getAesKey(), account.getIvKey());
			//旧密码加密
			String encodePassword = pwdEncoder.encodePassword(decryptOld);
			Map<String, String> map = config.getAttr();
			String apiPwd = map.get("apiAccountMngPassword");
			if (encodePassword.equals(apiPwd)) {//判断旧密码与原密码是否相同
				//新密码加密
				map.put("apiAccountMngPassword", pwdEncoder.encodePassword(decryptNew));
				config.setAttr(map);
				CmsConfig update = cmsConfigMng.update(config);
				log.info("update api account password id={}",update.getId());
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				status = Constants.API_STATUS_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_PASSWORD_ERROR;
				code = ResponseCode.API_CODE_PASSWORD_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	
	@RequestMapping(value = "/site_config/system_get")
	public void systemEdit(HttpServletResponse response,HttpServletRequest request) throws JSONException{
		CmsConfig config = null;
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		config = cmsConfigMng.get();
		if (config!=null) {
			JSONObject json = config.convertToJson();
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
	@RequestMapping(value = "/site_config/system_update")
	public void systemUpdate(CmsConfig bean , HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getId(),
				bean.getDefImg(),bean.getUploadToDb(),bean.getDbFileUri());
		if (errors!=null) {
			bean = cmsConfigMng.update(bean);
			log.info("update CmsConfig id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/site_config/base_get")
	public void baseGet(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		CmsSite site = null;
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		site = CmsUtils.getSite(request);
		if (site!=null) {
			JSONObject json = site.convertToJson();
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
	@RequestMapping(value = "/site_config/base_update")
	public void baseUpdate(CmsSite bean,Integer uploadFtpId,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getId(),
				bean.getDomain(),bean.getPath(),bean.getCorsUrl(),bean.getRelativePath(),
				bean.getProtocol(),bean.getDynamicSuffix(),bean.getStaticSuffix(),bean.getLocaleAdmin(),bean.getLocaleFront());
		if (errors!=null) {
			bean = manager.update(bean, uploadFtpId);
			log.info("update CmsSite id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/bbs_config/get")
	public void bbsConfigGet(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		CmsSite site = CmsUtils.getSite(request);
		BbsConfig config = null;
		CmsConfig cmsConfig = null;
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		config = bbsConfigMng.findById(site.getId());
		cmsConfig = cmsConfigMng.get();
		if (config!=null&&cmsConfig!=null) {
			JSONObject json = bbsConfigToJson(config,cmsConfig);
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
	@RequestMapping(value = "/bbs_config/update")
	public void bbsConfigUpd(BbsConfig bean, BbsConfigAttr bbsConfigAttr,Integer registerGroupId,Integer defaultGroupId,
			HttpServletRequest request,HttpServletResponse response){
		CmsSite site = CmsUtils.getSite(request);
		bean.setSite(site);
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getId(),bean.getDefAvatar(),bean.getPicZoomDefWidth(),
				bean.getAvatarHeight(),bean.getAvatarWidth(),bean.getTopicCountPerPage(),bean.getPostCountPerPage(),bean.getTopicHotCount(),
				bean.getKeywords(),bean.getDescription(),bean.getRegisterStatus(),bean.getEmailValidate(),
				registerGroupId,defaultGroupId,bbsConfigAttr.getKeepMinute(),bbsConfigAttr.getDefaultActiveLevel(),bbsConfigAttr.getSensitivityInputOn());
		if (!errors.hasErrors()) {
			bean.setRegisterGroup(bbsUserGroupMng.findById(registerGroupId));
			bean.setDefaultGroup(bbsUserGroupMng.findById(defaultGroupId));
			bbsConfigMng.update(bean);
			cmsConfigMng.updateConfigAttr(bbsConfigAttr);
			log.info("update CmsSite id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/bbs_config/login_get")
	public void loginConfigGet(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		ConfigLogin configLogin = configMng.getConfigLogin();
		EmailSender emailSender = configMng.getEmailSender();
		MessageTemplate forgotTemplate = configMng.getForgotPasswordMessageTemplate();
		MessageTemplate registerTemplate = configMng.getRegisterMessageTemplate();
		if (configLogin!=null&&emailSender!=null&&forgotTemplate!=null&&registerTemplate!=null) {
			JSONObject json = loginConfigToJson(configLogin,emailSender,forgotTemplate,registerTemplate);
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
	@RequestMapping(value = "/bbs_config/login_update")
	public void loginConfigUpd(ConfigLogin configLogin,ConfigEmailSender emailSender,ConfigMessageTemplate messageTemplate,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, configLogin.getErrorTimes(),
				configLogin.getErrorInterval(),emailSender.getHost(),emailSender.getUsername(),
				messageTemplate.getForgotPasswordSubject(),messageTemplate.getForgotPasswordText(),
				messageTemplate.getRegisterSubject(),messageTemplate.getRegisterText());
		if (!errors.hasErrors()) {
			if (StringUtils.isBlank(emailSender.getPassword())) {
				emailSender.setPassword(configMng.getEmailSender().getPassword());
			}
			configMng.updateOrSave(configLogin.getAttr());
			configMng.updateOrSave(emailSender.getAttr());
			configMng.updateOrSave(messageTemplate.getAttr());
			log.info("update loginConfig of Config");
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/bbs_config/creditExchange_get")
	public void bbsCreditExchangeGet(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		CmsSite site = CmsUtils.getSite(request);
		BbsCreditExchange exchange = creditExchangeMng.findById(site.getId());
		if (exchange!=null) {
			JSONObject json = exchange.convertToJson();
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
	@RequestMapping(value = "/bbs_config/creditExchange_update")
	public void bbsCreditExchangeUpd(BbsCreditExchange bean,HttpServletRequest request,
			HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getId(),
				bean.getPointinavailable(),bean.getPointoutavailable(),bean.getPrestigeinavailable(),
				bean.getPrestigeoutavailable(),bean.getExchangetax(),bean.getMiniBalance());
		if (!errors.hasErrors()) {
			bean = creditExchangeMng.update(bean);
			log.info("update BbsCreditExchange id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+bean.getId()+"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value="/bbs_config/charge_get")
	public void chargeGet(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		CmsConfig config = cmsConfigMng.get();
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsConfigCharge charge = chargeMng.getDefault();
		if (config!=null) {
			Map<String, String> attr = config.getRewardFixAttr();
			JSONObject json = new JSONObject();
			json.put("configCharge", charge.convertToJson(true));
			json.put("fixMap", attrToJson(attr));
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
	@RequestMapping(value = "/bbs_config/charge_update")
	public void chargeUpd(BbsConfigCharge bean , String wexinPassword,String wexinSecret,
			String alipayKey,String alipayPublicKey,String alipayPrivateKey,
			String transferApiPassword,String payTransferPassword,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getId(),bean.getWeixinAppId(),
				bean.getWeixinAccount(),bean.getRewardPattern(),bean.getRewardMin(),bean.getRewardMax(),bean.getAlipayAppId(),
				bean.getGiftChargeRatio(),bean.getMinDrawAmount(),bean.getChargeRatio());
		if (!errors.hasErrors()) {
			Map<String, String> attrs = new HashMap<String,String>();
			attrs.put("weixinPassword", wexinPassword);
			attrs.put("wexinSecret", wexinSecret);
			attrs.put("alipayKey", alipayKey);
			attrs.put("alipayPublicKey", alipayPublicKey);
			attrs.put("alipayPrivateKey", alipayPrivateKey);
			attrs.put("transferApiPassword", transferApiPassword);
			Map<String, String> fixMap = RequestUtils.getRequestMap(request, "attr_");
			bean = chargeMng.update(bean, payTransferPassword, attrs, fixMap);
			log.info("update BbsConfigCharge id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+bean.getId()+"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/bbs_config/message_get")
	public void messageGet(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsConfigAttr configAttr = cmsConfigMng.get().getConfigAttr();
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (configAttr!=null) {
			JSONObject json = new JSONObject();
			if (configAttr.getReportMsgAuto()!=null) {
				json.put("reportMsgAuto", configAttr.getReportMsgAuto());
			}else{
				json.put("reportMsgAuto", "");
			}
			if (StringUtils.isNotBlank(configAttr.getReportMsgTxt())) {
				json.put("reportMsgTxt", configAttr.getReportMsgTxt());
			}else{
				json.put("reportMsgTxt", "");
			}
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
	@RequestMapping(value = "/bbs_config/message_update")
	public void messageUpd(BbsConfigAttr bean ,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getReportMsgAuto(),
				bean.getReportMsgTxt());
		if (!errors.hasErrors()) {
			cmsConfigMng.updateConfigAttr(bean);
			log.info("update BbsConfigAttr");
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/bbs_config/api_get")
	public void apiGet(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsConfigAttr configAttr = cmsConfigMng.get().getConfigAttr();
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (configAttr!=null) {
			JSONObject json = apiToJson(configAttr);
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
	@RequestMapping(value = "/bbs_config/api_update")
	public void apiUpdate(BbsConfigAttr bean,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getQqEnable(),
				bean.getSinaEnable(),bean.getWeixinEnable());
		if (!errors.hasErrors()) {
			cmsConfigMng.updateConfigAttr(bean);
			log.info("update BbsConfigAttr");
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/bbs_config/sso_get")
	public void ssoAuthenticateGet(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		CmsConfig config = cmsConfigMng.get();
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (config!=null) {
			JSONObject json = ssoToJson(config);
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
	@RequestMapping(value="/bbs_config/sso_update")
	public void ssoAuthenticateUpd(HttpServletRequest request, HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		Map<String, String> ssoMap = RequestUtils.getRequestMap(request, "attr_");
		errors = ApiValidate.validateRequiredParams(errors, ssoMap.get("ssoEnable"));
		if (!errors.hasErrors()) {
			cmsConfigMng.updateSsoAttr(ssoMap);
			log.info("update attrs of CmsConfig");;
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/bbs_config/item_list")
	public void itemList(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		List<CmsConfigItem> list = cmsConfigItemMng.getList(cmsConfigMng.get().getId(), CmsConfigItem.CATEGORY_REGISTER);
		JSONArray jsonArray = new JSONArray();
		if (list!=null) {
			for(int i = 0 ; i<list.size(); i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/bbs_config/item_get")
	public void itemGet(Integer id,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		CmsConfigItem bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsConfigItem();
			}else{
				bean = cmsConfigItemMng.findById(id);
			}
			if (bean!=null) {
				JSONObject json = bean.convertToJson();
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

	@SignValidate
	@RequestMapping(value = "/bbs_config/item_save")
	public void itemSave(CmsConfigItem bean,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		bean.init();
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getPriority(),
				bean.getField(),bean.getLabel(),bean.getDataType(),bean.getRequired());
		if (!errors.hasErrors()) {
			bean.setConfig(cmsConfigMng.get());
			bean.setCategory(CmsConfigItem.CATEGORY_REGISTER);
			bean = cmsConfigItemMng.save(bean);
			log.info("save CmsConfigItem id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SignValidate
	@RequestMapping(value = "/bbs_config/item_update")
	public void itemUpdate(CmsConfigItem bean,HttpServletRequest request, HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getId(),
				bean.getField(),bean.getLabel(),bean.getPriority(),bean.getRequired());
		if (!errors.hasErrors()) {
			bean = cmsConfigItemMng.update(bean);
			log.info("update CmsConfigItem id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SignValidate
	@RequestMapping(value = "/bbs_config/item_delete")
	public void itemDelete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				CmsConfigItem[] deleteByIds = cmsConfigItemMng.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete CmsConfigItem id={}",deleteByIds[i].getId());
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
	
	@RequestMapping(value = "/bbs_config/item_priority")
	public void itemPriority(String ids,String priorities,String labels,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArray = StrUtils.getInts(ids);
			Integer[] priorityArray = StrUtils.getInts(priorities);
			String[] labelArray = labels.split(",");
			if (idArray.length==priorityArray.length&&priorityArray.length==labelArray.length) {
				cmsConfigItemMng.updatePriority(idArray, priorityArray, labelArray);
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/bbs_config/ad_get")
	public void adGet(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsConfigAttr configAttr = cmsConfigMng.get().getConfigAttr();
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (configAttr!=null) {
			JSONObject json = adToJson(configAttr);
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
	@RequestMapping(value = "/bbs_config/ad_update")
	public void adUpdate(BbsConfigAttr bean,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getAdDayCharge(),
				bean.getAdClickCharge(),bean.getAdDisplayCharge());
		if (!errors.hasErrors()) {
			cmsConfigMng.updateConfigAttr(bean);
			log.info("update BbsConfigAttr");
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 广告设置Json对象
	 * @param attr
	 * @return
	 * @throws JSONException
	 */
	private JSONObject adToJson(BbsConfigAttr attr) throws JSONException{
		JSONObject json = new JSONObject();
		if (attr.getAdDayCharge()!=null) {
			json.put("adDayCharge", attr.getAdDayCharge());
		}else{
			json.put("adDayCharge", "");
		}
		if (attr.getAdClickCharge()!=null) {
			json.put("adClickCharge", attr.getAdClickCharge());
		}else{
			json.put("adClickCharge", "");
		}
		if (attr.getAdDisplayCharge()!=null) {
			json.put("adDisplayCharge", attr.getAdDisplayCharge());
		}else{
			json.put("adDisplayCharge", "");
		}
		return json;
	}
	
	/**
	 * 单点登录认证json对象
	 * @param config
	 * @return
	 * @throws JSONException
	 */
	private JSONObject ssoToJson(CmsConfig config) throws JSONException {
		JSONObject json = new JSONObject();
		if (config.getConfigAttr()!=null&&config.getConfigAttr().getSsoEnable()!=null) {
			json.put("attr_ssoEnable", config.getConfigAttr().getSsoEnable());
		}else{
			json.put("attr_ssoEnable", "");
		}
		JSONArray jsonArray = new JSONArray();
		if (config.getSsoAttr()!=null) {
			Map<String, String> ssoAttr = config.getSsoAttr();
			int index = 0;
			for(String key : ssoAttr.keySet()){
				JSONObject object = new JSONObject();
				if (StringUtils.isNotBlank(ssoAttr.get(key))) {
					object.put("key", "attr_"+key);
					object.put("value", ssoAttr.get(key));
				}else{
					object.put("key", "attr_"+key);
					object.put("value", "");
				}
				jsonArray.put(index,object);
				index++;
			}
		}
		json.put("attr", jsonArray);
		return json;
	}

	/**
	 * 第三方登录设置json对象
	 * @param configAttr
	 * @return
	 * @throws JSONException
	 */
	private JSONObject apiToJson(BbsConfigAttr configAttr) throws JSONException{
		JSONObject json = new JSONObject();
		if (configAttr.getQqEnable()!=null) {
			json.put("qqEnable", configAttr.getQqEnable());
		}else{
			json.put("qqEnable", "");
		}
		if (StringUtils.isNotBlank(configAttr.getQqID())) {
			json.put("qqID", configAttr.getQqID());
		}else{
			json.put("qqID", "");
		}
		if (StringUtils.isNotBlank(configAttr.getQqKey())) {
			json.put("qqKey", configAttr.getQqKey());
		}else{
			json.put("qqKey", "");
		}
		if (configAttr.getSinaEnable()!=null) {
			json.put("sinaEnable", configAttr.getSinaEnable());
		}else{
			json.put("sinaEnable", "");
		}
		if (StringUtils.isNotBlank(configAttr.getSinaID())) {
			json.put("sinaID", configAttr.getSinaID());
		}else{
			json.put("sinaID", "");
		}
		if (StringUtils.isNotBlank(configAttr.getSinaKey())) {
			json.put("sinaKey", configAttr.getSinaKey());
		}else{
			json.put("sinaKey", "");
		}
		if (configAttr.getQqWeboEnable()!=null) {
			json.put("qqWeboEnable", configAttr.getQqWeboEnable());
		}else{
			json.put("qqWeboEnable", "");
		}
		if (StringUtils.isNotBlank(configAttr.getQqWeboID())) {
			json.put("qqWeboID", configAttr.getQqWeboID());
		}else{
			json.put("qqWeboID", "");
		}
		if (StringUtils.isNotBlank(configAttr.getQqWeboKey())) {
			json.put("qqWeboKey", configAttr.getQqWeboKey());
		}else{
			json.put("qqWeboKey", "");
		}
		if (configAttr.getWeixinEnable()!=null) {
			json.put("weixinEnable", configAttr.getWeixinEnable());
		}else{
			json.put("weixinEnable", "");
		}
		if (StringUtils.isNotBlank(configAttr.getWeixinLoginId())) {
			json.put("weixinLoginId", configAttr.getWeixinLoginId());
		}else{
			json.put("weixinLoginId", "");
		}
		if (StringUtils.isNotBlank(configAttr.getWeixinLoginSecret())) {
			json.put("weixinLoginSecret", configAttr.getWeixinLoginSecret());
		}else{
			json.put("weixinLoginSecret", "");
		}
		if (StringUtils.isNotBlank(configAttr.getWeixinAppId())) {
			json.put("weixinAppId", configAttr.getWeixinAppId());
		}else{
			json.put("weixinAppId", "");
		}
		if (StringUtils.isNotBlank(configAttr.getWeixinAppSecret())) {
			json.put("weixinAppSecret", configAttr.getWeixinAppSecret());
		}else{
			json.put("weixinAppSecret", "");
		}
		return json;
	}
	
	/**
	 * map转json
	 * @param attr
	 * @return
	 * @throws JSONException
	 */
	private JSONArray attrToJson(Map<String, String> attr) throws JSONException{
		JSONArray jsonArray = new JSONArray();
		int index = 0;
		for(String key : attr.keySet()){
			JSONObject json = new JSONObject();
			if (StringUtils.isNotBlank(attr.get(key))) {
				json.put("key", "attr_"+key);
				json.put("value", attr.get(key));
			}else{
				json.put("key", "attr_"+key);
				json.put("value", "");
			}
			jsonArray.put(index,json);
			index++;
		}
		return jsonArray;
	}
	
	/**
	 * 登录设置Json对象
	 * @param configLogin
	 * @param emailSender
	 * @param forgotTemplate
	 * @param registerTemplate
	 * @return
	 * @throws JSONException
	 */
	private JSONObject loginConfigToJson(ConfigLogin configLogin, EmailSender emailSender,
			MessageTemplate forgotTemplate, MessageTemplate registerTemplate) throws JSONException {
		JSONObject json = new JSONObject();
		if (configLogin.getErrorTimes()!=null) {
			json.put("errorTimes", configLogin.getErrorTimes());
		}else{
			json.put("errorTimes", "");
		}
		if (configLogin.getErrorInterval()!=null) {
			json.put("errorInterval", configLogin.getErrorInterval());
		}else{
			json.put("errorInterval", "");
		}
		if (StringUtils.isNotBlank(emailSender.getHost())) {
			json.put("host", emailSender.getHost());
		}else{
			json.put("host", "");
		}
		if (emailSender.getPort()!=null) {
			json.put("port", emailSender.getPort());
		}else{
			json.put("port", "");
		}
		if (StringUtils.isNotBlank(emailSender.getUsername())) {
			json.put("username", emailSender.getUsername());
		}else{
			json.put("username", "");
		}
		if (StringUtils.isNotBlank(emailSender.getPassword())) {
			json.put("password", emailSender.getPassword());
		}else{
			json.put("password", "");
		}
		if (StringUtils.isNotBlank(emailSender.getEncoding())) {
			json.put("encoding", emailSender.getEncoding());
		}else{
			json.put("encoding", "");
		}
		if (StringUtils.isNotBlank(emailSender.getPersonal())) {
			json.put("personal", emailSender.getPersonal());
		}else{
			json.put("personal", "");
		}
		if (StringUtils.isNotBlank(forgotTemplate.getForgotPasswordSubject())) {
			json.put("forgotPasswordSubject", forgotTemplate.getForgotPasswordSubject());
		}else{
			json.put("forgotPasswordSubject", "");
		}
		if (StringUtils.isNotBlank(forgotTemplate.getForgotPasswordText())) {
			json.put("forgotPasswordText", forgotTemplate.getForgotPasswordText());
		}else{
			json.put("forgotPasswordText", "");
		}
		if (StringUtils.isNotBlank(registerTemplate.getRegisterSubject())) {
			json.put("registerSubject", registerTemplate.getRegisterSubject());
		}else{
			json.put("registerSubject", "");
		}
		if (StringUtils.isNotBlank(registerTemplate.getRegisterText())) {
			json.put("registerText", registerTemplate.getRegisterText());
		}else{
			json.put("registerText", "");
		}
		return json;
	}

	/**
	 * 论坛配置json对象
	 * @param config
	 * @param cmsConfig
	 * @return
	 * @throws JSONException
	 */
	private JSONObject bbsConfigToJson(BbsConfig config,CmsConfig cmsConfig) throws JSONException{
		JSONObject json = new JSONObject();
		if (config.getId()!=null) {
			json.put("id", config.getId());
		}else{
			json.put("id", "");
		}
		json.put("keepMinute", cmsConfig.getKeepMinute());
		if (StringUtils.isNotBlank(config.getDefAvatar())) {
			json.put("defAvatar", config.getDefAvatar());
		}else{
			json.put("defAvatar", "");
		}
		if (config.getPicZoomDefWidth()!=null) {
			json.put("picZoomDefWidth", config.getPicZoomDefWidth());
		}else{
			json.put("picZoomDefWidth", "");
		}
		if (config.getAvatarWidth()!=null) {
			json.put("avatarWidth", config.getAvatarWidth());
		}else{
			json.put("avatarWidth", "");
		}
		if (config.getAvatarHeight()!=null) {
			json.put("avatarHeight", config.getAvatarHeight());
		}else{
			json.put("avatarHeight", "");
		}
		if (config.getTopicCountPerPage()!=null) {
			json.put("topicCountPerPage", config.getTopicCountPerPage());
		}else{
			json.put("topicCountPerPage", "");
		}
		if (config.getPostCountPerPage()!=null) {
			json.put("postCountPerPage", config.getPostCountPerPage());
		}else{
			json.put("postCountPerPage", "");
		}
		if (config.getTopicHotCount()!=null) {
			json.put("topicHotCount", config.getTopicHotCount());
		}else{
			json.put("topicHotCount", "");
		}
		if (StringUtils.isNotBlank(config.getKeywords())) {
			json.put("keywords", config.getKeywords());
		}else{
			json.put("keywords", "");
		}
		if (StringUtils.isNotBlank(config.getDescription())) {
			json.put("description", config.getDescription());
		}else{
			json.put("description", "");
		}
		if (config.getRegisterStatus()!=null) {
			json.put("registerStatus", config.getRegisterStatus());
		}else{
			json.put("registerStatus", "");
		}
		if (config.getRegisterGroup()!=null&&config.getRegisterGroup().getId()!=null) {
			json.put("registerGroupId", config.getRegisterGroup().getId());
		}else{
			json.put("registerGroupId", "");
		}
		if (config.getEmailValidate()!=null) {
			json.put("emailValidate", config.getEmailValidate());
		}else{
			json.put("emailValidate", "");
		}
		if (StringUtils.isNotBlank(config.getRegisterRule())) {
			json.put("registerRule", config.getRegisterRule());
		}else{
			json.put("registerRule", "");
		}
		if (config.getDefaultGroup()!=null&&config.getDefaultGroup().getId()!=null) {
			json.put("defaultGroupId", config.getDefaultGroup().getId());
		}else{
			json.put("defaultGroupId", "");
		}
		if (cmsConfig.getDefaultActiveLevel()!=null) {
			json.put("defaultActiveLevel", cmsConfig.getDefaultActiveLevel());
		}else{
			json.put("defaultActiveLevel", "");
		}
		if (cmsConfig.getSensitivityInputOn()!=null) {
			json.put("sensitivityInputOn", cmsConfig.getSensitivityInputOn());
		}else{
			json.put("sensitivityInputOn", "");
		}
		return json;
	}
	@Autowired
	private CmsSiteMng manager;
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private BbsCreditExchangeMng creditExchangeMng;
	@Autowired
	private BbsConfigChargeMng chargeMng;
	@Autowired
	private CmsConfigItemMng cmsConfigItemMng;
	@Autowired
	private PwdEncoder pwdEncoder;
	@Autowired
	private ApiAccountMng accountMng;
	
}
