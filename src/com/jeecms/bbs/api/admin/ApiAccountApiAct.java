package com.jeecms.bbs.api.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeecms.bbs.manager.ApiAccountMng;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.security.encoder.PwdEncoder;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.web.WebErrors;


@Controller
public class ApiAccountApiAct {
	private static final Logger log = LoggerFactory.getLogger(ApiAccountApiAct.class);
	
	@Autowired
	private ApiAccountMng manager;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/apiAccount/list")
	public void list(Integer pageNo,Integer pageSize,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		Pagination page = manager.getPage(pageNo, pageSize);
		List<ApiAccount> list = (List<ApiAccount>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size() ; i++) {
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"toatalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/apiAccount/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		ApiAccount bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new ApiAccount();
				bean.init();
			}else{
				bean = manager.findById(id);
			}
			if (bean !=null) {
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
	@RequestMapping(value = "/apiAccount/save")
	public void save(ApiAccount bean ,String apiPwd,String setAppId,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getAppId(),setAppId,
				bean.getAppKey(),bean.getAesKey(),bean.getDisabled(),bean.getIvKey());
		if (!errors.hasErrors()) {
			try {
				CmsConfig config = cmsConfigMng.get();
				Map<String, String> map = config.getAttr();
				ApiAccount apiAccount=apiAccountMng.getApiAccount(request);
				//独立密码解密
				String decrypt = AES128Util.decrypt(apiPwd, apiAccount.getAesKey(), apiAccount.getIvKey());
				//独立密码MD5加密
				String encodePassword = pwdEncoder.encodePassword(decrypt);
				String oldPWD = map.get("apiAccountMngPassword");
				//判断输入密码是否正确
				if (encodePassword.equals(oldPWD)) {
					bean.setAppId(setAppId);
					bean = manager.save(bean);
					log.info("save ApiAccount id={}",bean.getId());
					status = Constants.API_STATUS_SUCCESS;
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
					body = "{\"id\":"+"\""+bean.getId()+"\"}";
				}else{
					message = Constants.API_MESSAGE_PASSWORD_ERROR;
					code = ResponseCode.API_CODE_PASSWORD_ERROR;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/*
	@RequestMapping(value = "/apiAccount/update")
	public void update(ApiAccount bean ,String setAppId,String apiPwd, String appKey,
			String aesKey,String ivKey,String apiAccountMngPassword,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors,bean.getId());
		if (!errors.hasErrors()) {
			CmsConfig config = cmsConfigMng.get();
			Map<String, String> map = config.getAttr();
			//独立密码解密
			String decrypt = AES128Util.decrypt(apiPwd, aesKey, ivKey);
			//独立密码MD5加密
			String encodePassword = pwdEncoder.encodePassword(decrypt);
			String oldPWD = map.get("apiAccountMngPassword");
			//判断输入密码是否正确
			if (encodePassword.equals(oldPWD)) {
				bean.setAppId(setAppId);
				bean = manager.update(bean, appKey, aesKey, ivKey);
				log.info("update ApiAccount id={}",bean.getId());
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
			}else{
				message = Constants.API_MESSAGE_PASSWORD_ERROR;
				code = ResponseCode.API_CODE_PASSWORD_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/apiAccount/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				ApiAccount[] deleteByIds = manager.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete ApiAccount id={}",deleteByIds[i]);
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
	*/
	
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private PwdEncoder pwdEncoder;
	@Autowired
	private ApiAccountMng apiAccountMng;
}
