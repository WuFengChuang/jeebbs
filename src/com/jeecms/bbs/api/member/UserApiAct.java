package com.jeecms.bbs.api.member;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.cache.BbsConfigEhCache;
import com.jeecms.bbs.entity.ApiAccount;
import com.jeecms.bbs.entity.BbsConfig;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.entity.BbsWebservice;
import com.jeecms.bbs.manager.ApiAccountMng;
import com.jeecms.bbs.manager.ApiRecordMng;
import com.jeecms.bbs.manager.ApiUserLoginMng;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsUserExtMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.manager.BbsWebserviceMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.LoginUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;
@Controller
public class UserApiAct {
	public static final short REGISTER_CLOSE=0;
	
	/**
	 * 禁言/解除禁言
	 * @param forumId 板块ID 必填
	 * @param userId 被禁言用户id 必填
	 * @param disabled 禁言状态
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/user/forbidden")
	public void userForbidden(Integer forumId,Integer userId,Boolean disabled,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		if (disabled==null) {
			disabled=true;
		}
		errors = ApiValidate.validateRequiredParams(errors, forumId,userId);
		if (!errors.hasErrors()) {
			BbsUser user = CmsUtils.getUser(request);
			BbsUser forbiddenUser = bbsUserMng.findById(userId);
			BbsForum forum = forumMng.findById(forumId);
			CmsSite site = CmsUtils.getSite(request);
			errors = validateForbidden(errors, user, forbiddenUser, forum);
			if (errors.hasErrors()) {
				code = ResponseCode.API_CODE_FORBIDDEN_ERROR;
				message = errors.getErrors().get(0);
			}else{
				if (disabled) {//禁言
					if(!forbiddenUser.getDisabled()){//被禁言用户是否处于禁言状态
						bbsUserMng.forbidUser(site.getId(), forbiddenUser, true);//true禁言 false不禁言
					}
				}else{//解除禁言
					if(forbiddenUser.getDisabled()){//被禁言用户是否处于禁言状态
						bbsUserMng.forbidUser(site.getId(), forbiddenUser, false);
					}
				}
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				status = Constants.API_STATUS_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 添加会员用户
	 * @param username 用户名   必选
	 * @param email 邮箱 非必选
	 * @param loginPassword 密码  必选
	 * @param realname 真实姓名 非必选
	 * @param gender 性别 非必选
	 * @param birthdayStr 生日 格式"yyyy-MM-dd" 例如"1980-01-01" 非必选
	 * @param phone  电话 非必选
	 * @param mobile 手机 非必选
	 * @param qq qq号  非必选
	 * @param userImg 用户头像  非必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@SignValidate
	@RequestMapping(value = "/user/save")
	public void userAdd(String username, String email, String loginPassword,
			String realname,Boolean gender,String birthdayStr,
			String phone,String mobile,String qq,String userImg,
			String appId,String sign,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = null;
		errors = ApiValidate.validateRequiredParams(errors,username,loginPassword);
		if (!errors.hasErrors()) {
			BbsConfig bbsConfig = bbsConfigMng.findById(site.getId());
			if (bbsConfig.getRegisterStatus().equals(REGISTER_CLOSE)) {
				errors.addErrorString("\"member register Close\"");
				code = ResponseCode.API_CODE_MEMBER_CLOSE;
			}
			if (!errors.hasErrors()) {
				user = bbsUserMng.findByUsername(username);
				if (user==null) {
					String ip = RequestUtils.getIpAddr(request);
					Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
					Integer groupId = null;
					BbsUserGroup group = bbsConfigMng.findById(site.getId())
							.getRegisterGroup();
					if (group != null) {
						groupId = group.getId();
					}
					BbsUserExt userExt=new BbsUserExt();
					if(StringUtils.isNotBlank(birthdayStr)){
						userExt.setBirthday(DateUtils.parseDayStrToDate(birthdayStr));
					}
					userExt.setGender(gender);
					userExt.setMoble(mobile);
					userExt.setPhone(phone);
					userExt.setQq(qq);
					userExt.setRealname(realname);
					try {
						user = bbsUserMng.registerMember(username, email, false,loginPassword,
								  ip, groupId, userExt,attrs);
					} catch (Exception e) {
						//可能发送邮件异常
					} 
					bbsUserMng.updateMember(user.getId(), null, null, null, null,
							userImg, userExt, null,null);
					bbsWebserviceMng.callWebService(BbsWebservice.SERVICE_TYPE_ADD_USER,
							username, loginPassword, email, userExt);
					bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
					apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
							appId, "/user/add",sign);
					body="{\"id\":"+"\""+user.getId()+"\"}";
					message=Constants.API_MESSAGE_SUCCESS;
					status=Constants.API_STATUS_SUCCESS;
					code =ResponseCode.API_CODE_CALL_SUCCESS;
				}else{
					//用户名已存在
					message=Constants.API_MESSAGE_USERNAME_EXIST;
					code=ResponseCode.API_CODE_USERNAME_EXIST;
				}
			}else{
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 修改会员资料
	 * @param username 用户名   必选
	 * @param realname 真实姓名 非必选
	 * @param gender 性别 非必选
	 * @param birthdayStr 生日 格式"yyyy-MM-dd" 例如"1980-01-01" 非必选
	 * @param phone  电话 非必选
	 * @param mobile 手机 非必选
	 * @param qq qq号  非必选
	 * @param userImg 用户头像 非必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@SignValidate
	@RequestMapping(value = "/user/update")
	public void userEdit(String username, String realname,Boolean gender,
			String birthdayStr,String phone,String mobile,String qq,
			String userImg,String appId,String nonce_str,String sign,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, username);
		if (!errors.hasErrors()) {
			BbsUser user = bbsUserMng.findByUsername(username);
			if (user!=null) {
				BbsUserExt userExt = user.getUserExt();
				if(StringUtils.isNotBlank(birthdayStr)){
					userExt.setBirthday(DateUtils.parseDayStrToDate(birthdayStr));
				}
				userExt.setGender(gender);
				if(StringUtils.isNotBlank(mobile)){
					userExt.setMoble(mobile);
				}
				if(StringUtils.isNotBlank(phone)){
					userExt.setPhone(phone);
				}
				if(StringUtils.isNotBlank(qq)){
					userExt.setQq(qq);
				}
				if(StringUtils.isNotBlank(realname)){
					userExt.setRealname(realname);
				}
				bbsUserExtMng.update(userExt, user);
				if(StringUtils.isNotBlank(userImg)){
					bbsUserMng.updateMember(user.getId(), null, null, null, null,
							userImg, userExt, null,null);
				}
				bbsWebserviceMng.callWebService(BbsWebservice.SERVICE_TYPE_UPDATE_USER,
						username, null, null, userExt);
				message = Constants.API_MESSAGE_SUCCESS;
				status = Constants.API_STATUS_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_USER_NOT_FOUND;
				code = ResponseCode.API_CODE_USER_NOT_FOUND;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 修改会员密码
	 * @param username 用户名   必选
	 * @param email 邮箱 非必选
	 * @param origPwd 原密码  必选
	 * @param newPwd 新密码 必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value = "/user/pwd")
	public void pwdEdit(String username, String oldPwd,String newPwd,String email,
			String appId,String sign,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, username,oldPwd,newPwd);
		if (!errors.hasErrors()) {
			BbsUser user = bbsUserMng.findByUsername(username);
			if (user!=null) {
				//原密码错误
				if (!bbsUserMng.isPasswordValid(user.getId(), oldPwd)) {
					message=Constants.API_MESSAGE_ORIGIN_PWD_ERROR;
					code=ResponseCode.API_CODE_ORIGIN_PWD_ERROR;
				}else{
					bbsUserMng.updatePwdEmail(user.getId(), newPwd, email);
					bbsWebserviceMng.callWebService(BbsWebservice.SERVICE_TYPE_UPDATE_USER,
							username, newPwd, email, null);
					apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
							appId, "/user/pwd",sign);
					message=Constants.API_MESSAGE_SUCCESS;
					status=Constants.API_STATUS_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}
			}else{
				//用户不存在
				message=Constants.API_MESSAGE_USER_NOT_FOUND;
				code=ResponseCode.API_CODE_USER_NOT_FOUND;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 获取用户信息
	 * @param username 用户名必选
	 * @param https 用户头像返回地址格式    1 http格式   0 https格式 非必选 默认1
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value= "/user/get")
	public void getUserInfo(Integer https,String username,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if (https==null) {
			https=Constants.URL_HTTP;
		}
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, username);
		if (!errors.hasErrors()) {
			BbsUser user = bbsUserMng.findByUsername(username);
			if(user!=null){
				//加密返回
				try {
					/*加密有中文乱码问题
					String aesKey=apiAccount.getAesKey();
					body=AES128Util.encrypt(
							user.convertToJson(site).toString(), aesKey);
							*/
					body=user.convertToJson(site,https).toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				message=Constants.API_MESSAGE_SUCCESS;
				status=Constants.API_STATUS_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				//用户不存在
				message=Constants.API_MESSAGE_USER_NOT_FOUND;
				code=ResponseCode.API_CODE_USER_NOT_FOUND;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 用户退出API
	 * @param username 用户名 必选
	 * @param sessionKey 会话标识 必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value = "/user/logout")
	public void userLogout(String appId,String sessionKey,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		BbsUser user = CmsUtils.getUser(request);
		if (user!=null) {
			//已登录则退出
			String decryptSessionKey="";
			if(StringUtils.isNotBlank(appId)){
				ApiAccount apiAccount=apiAccountMng.findByAppId(appId);
				if(apiAccount!=null){
					String aesKey=apiAccount.getAesKey();
					try {
						decryptSessionKey = AES128Util.decrypt(sessionKey, aesKey,apiAccount.getIvKey());
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
			}
			if(StringUtils.isNotBlank(decryptSessionKey)){
				apiUserLoginMng.userLogout(user.getUsername(),decryptSessionKey);
				LoginUtils.logout();
			}
			message = Constants.API_MESSAGE_SUCCESS;
			status = Constants.API_STATUS_SUCCESS;
		}else{
			//用户不存在
			message=Constants.API_MESSAGE_USER_NOT_FOUND;
			code=ResponseCode.API_CODE_USER_NOT_FOUND;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/user/getPerms")
	public void getUserPerms(Integer https,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		CmsSite site = CmsUtils.getSite(request);
		ApiAccount apiAccount = apiAccountMng.getApiAccount(request);
		if (apiAccount!=null) {
			if (apiAccount.getDisabled()) {
				message=Constants.API_MESSAGE_ACCOUNT_DISABLED;
				code = ResponseCode.API_CODE_ACCOUNT_DISABLED;
			}else{
				BbsUser user = apiUserLoginMng.getUser(apiAccount, request);
				JSONObject json = user.convertToJson(site, https);
				if (user.getAdmin()) {
					json.put("perms", user.getPermStr());
				}else{
					json.put("perms", "");
				}
				body=json.toString();
				message=Constants.API_MESSAGE_SUCCESS;
				status=Constants.API_STATUS_SUCCESS;
			}
		}else{
			//用户不存在
			message=Constants.API_MESSAGE_USER_NOT_LOGIN;
			code=ResponseCode.API_CODE_USER_NOT_LOGIN;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateForbidden(WebErrors errors,BbsUser user, BbsUser forbiddenUser,BbsForum forum) {
		// 用户未登录
		if (user == null) {
			errors.addErrorString("\"user not login\"");
			return errors;
		}
		if (forum == null) {
			errors.addErrorString("\"forum not exit\"");
			return errors;
		}
		//禁言者不是版主
		if (!user.getModerator()) {
			errors.addError(Constants.API_MESSAGE_NOT_MODERATOR);
			return errors;
		}
		// 被禁言者不存在
		if (forbiddenUser == null) {
			errors.addErrorString("\"forbidden user not exit\"");
			return errors;
		}
		if(!user.getGroup().hasRight(forum, user)){
			errors.addErrorString("\"user not moderator\"");
			return errors;
		}
		// 不允许禁言自己
		if (user.equals(forbiddenUser)) {
			errors.addErrorString("\"can not forbidden self\"");
			return errors;
		}
		//不允许禁言版主
		if (forbiddenUser.getGroup().hasRight(forum, forbiddenUser)) {
			errors.addErrorString("\"can not forbidden moderator\"");
			return errors;
		}
		//不允许禁用管理员
		if(forbiddenUser.getAdmin()){
			errors.addErrorString("\"can not forbidden Administrators\"");
			return errors;
		}
		return errors;
	}
	
	private String weiXinJsCode2SessionUrl;
	
	public String getWeiXinJsCode2SessionUrl() {
		return weiXinJsCode2SessionUrl;
	}

	public void setWeiXinJsCode2SessionUrl(String weiXinJsCode2SessionUrl) {
		this.weiXinJsCode2SessionUrl = weiXinJsCode2SessionUrl;
	}
	
	@Autowired
	private BbsForumMng forumMng;
	@Autowired
	private ApiRecordMng apiRecordMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsUserExtMng bbsUserExtMng;
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
	@Autowired
	private BbsWebserviceMng bbsWebserviceMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsConfigEhCache bbsConfigEhCache;
	@Autowired
	private ApiAccountMng apiAccountMng;
}
