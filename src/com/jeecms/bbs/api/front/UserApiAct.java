package com.jeecms.bbs.api.front;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
import com.jeecms.bbs.entity.ApiRecord;
import com.jeecms.bbs.entity.ApiUserLogin;
import com.jeecms.bbs.entity.BbsThirdAccount;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.entity.BbsWebservice;
import com.jeecms.bbs.manager.ApiAccountMng;
import com.jeecms.bbs.manager.ApiRecordMng;
import com.jeecms.bbs.manager.ApiUserLoginMng;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsThirdAccountMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.manager.BbsWebserviceMng;
import com.jeecms.bbs.service.ImageSvc;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.email.EmailSender;
import com.jeecms.common.email.MessageTemplate;
import com.jeecms.common.security.encoder.PwdEncoder;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.HttpClientUtil;
import com.jeecms.common.web.LoginUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.entity.UnifiedUser;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.manager.ConfigMng;
import com.jeecms.core.manager.UnifiedUserMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class UserApiAct {
	
	private final String WEIXIN_JSCODE_2_SESSION_URL="weixin.jscode2sessionUrl";
	public static final short REGISTER_CLOSE=0;
	
	/**
	 * 找回密码
	 * @param username 用户名 必填
	 * @param email 邮箱 必填
	 * @param captcha 验证码 必填
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/user/forgot_password")
	public void ForgotPassword(String username,String email,String captcha
			,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors =WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, username,email,captcha);
		if (!errors.hasErrors()) {
			UnifiedUser user = unifiedUserMng.getByUsername(username);
			EmailSender sender = conMng.getEmailSender();
			MessageTemplate msgTpl = conMng.getForgotPasswordMessageTemplate();
			if (user==null) {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}else if (StringUtils.isBlank(user.getEmail())) {
				message = Constants.API_MESSAGE_EMAIL_NOT_SET;
				code = ResponseCode.API_CODE_EMAIL_NOT_SET;
			} else if (!user.getEmail().equals(email)) {
				message = Constants.API_MESSAGE_INPUT_ERROR;
				code = ResponseCode.API_CODE_INPUT_ERROR;
			} else if (sender == null) {
				message = Constants.API_MESSAGE_EMAIL_SETTING_ERROR;
				code = ResponseCode.API_CODE_EMAIL_SETTING_ERROR;
			} else if (msgTpl == null) {
				message = Constants.API_MESSAGE_EMAIL_TEMPLATE_ERROR;
				code = ResponseCode.API_CODE_EMAIL_TEMPLATE_ERROR;
			} else {
				try {
					unifiedUserMng.passwordForgotten(user.getId(),sender, msgTpl);
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
					status = Constants.API_STATUS_SUCCESS;
				} catch (Exception e) {
					message = Constants.API_MESSAGE_EMAIL_SEND_EXCEPTION;
					code = ResponseCode.API_CODE_EMAIL_SEND_EXCEPTION;
				}
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 用户登录API
	 * @param username 用户名 必选
	 * @param aesPassword 加密密码 必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value = "/user/login")
	public void userLogin(
			String username,String aesPassword,
			String appId,String nonce_str,String sign,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		WebErrors errors=WebErrors.create(request);
		ApiAccount apiAccount = null;
		BbsUser user = null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, appId,
				nonce_str,sign,username,aesPassword);
		if(!errors.hasErrors()){
			apiAccount=apiAccountMng.findByAppId(appId);
			errors=ApiValidate.validateApiAccount(request, errors, apiAccount);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_APP_PARAM_ERROR;
			}else{
				//验证签名
				errors=ApiValidate.validateSign(request, errors,apiAccount, sign);
				if(errors.hasErrors()){
					code=ResponseCode.API_CODE_SIGN_ERROR;
				}
			}
		}else{
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}
		if(errors.hasErrors()){
			message=errors.getErrors().get(0);
		}else{
			//签名数据不可重复利用
			ApiRecord record=apiRecordMng.findBySign(sign, appId);
			if(record!=null){
				message=Constants.API_MESSAGE_REQUEST_REPEAT;
				code=ResponseCode.API_CODE_REQUEST_REPEAT;
			}else{
				user=bbsUserMng.findByUsername(username);
				if(user!=null){
					String aesKey=apiAccount.getAesKey();
					//解密用户输入的密码
					String encryptPass="";
					try {
						encryptPass = AES128Util.decrypt(aesPassword, aesKey,apiAccount.getIvKey());
					} catch (Exception e) {
						//e.printStackTrace();
					}
					//验证用户密码
					if(bbsUserMng.isPasswordValid(user.getId(), encryptPass)){
						//解决会话固定漏洞
						LoginUtils.logout();
						//sessionID加密后返回 ,该值作为用户数据交互识别的关键值
						//调用接口端将该值保存，调用用户数据相关接口传递加密sessionID后的值，服务器端解密后查找用户
						String sessionKey=session.getSessionId(request, response);
						apiUserLoginMng.userLogin(username, sessionKey);
						LoginUtils.loginShiro(request, response, username);
						CmsUtils.setUser(request, user);
						try {
							//加密返回
							body="\""+AES128Util.encrypt(sessionKey, aesKey,apiAccount.getIvKey())+"\"";
						} catch (Exception e) {
							e.printStackTrace();
						}
						apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
								appId, "/user/login",sign);
						message=Constants.API_MESSAGE_SUCCESS;
						status=Constants.API_STATUS_SUCCESS;
					}else{
						//密码错误
						message=Constants.API_MESSAGE_PASSWORD_ERROR;
						code=ResponseCode.API_CODE_PASSWORD_ERROR;
					}
				}else{
					//用户不存在
					message=Constants.API_MESSAGE_USER_NOT_FOUND;
					code=ResponseCode.API_CODE_USER_NOT_FOUND;
				}
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 微信小程序-微信用户登录获取sessionKey和openid API
	 * @param js_code 微信小程序登录code 必选
	 * @param grant_type 非必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value = "/user/weixinLogin")
	public void weixinAppLogin(
			String js_code,String grant_type,
			String appId,String nonce_str,String sign,
			HttpServletRequest request,HttpServletResponse response) 
					{
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		WebErrors errors=WebErrors.create(request);
		if(StringUtils.isNotBlank(grant_type)){
			grant_type="authorization_code";
		}
		ApiAccount apiAccount = null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, appId,
				nonce_str,sign,js_code);
		if(!errors.hasErrors()){
			apiAccount=apiAccountMng.findByAppId(appId);
			errors=ApiValidate.validateApiAccount(request, errors, apiAccount);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_APP_PARAM_ERROR;
			}else{
				//验证签名
				errors=ApiValidate.validateSign(request, errors,apiAccount, sign);
				if(errors.hasErrors()){
					code=ResponseCode.API_CODE_SIGN_ERROR;
				}
			}
		}else{
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}
		if(errors.hasErrors()){
			message=errors.getErrors().get(0);
		}else{
			//签名数据不可重复利用
			ApiRecord record=apiRecordMng.findBySign(sign, appId);
			if(record!=null){
				message=Constants.API_MESSAGE_REQUEST_REPEAT;
				code=ResponseCode.API_CODE_REQUEST_REPEAT;
			}else{
				initWeiXinJsCode2SessionUrl();
				Map<String,String>params=new HashMap<String, String>();
				CmsConfig config=configMng.get();
				params.put("appid", config.getWeixinAppId());
				params.put("secret", config.getWeixinAppSecret());
				params.put("js_code",js_code);
				params.put("grant_type",grant_type);
				String result=HttpClientUtil.postParams(getWeiXinJsCode2SessionUrl(),
						params);
				JSONObject json;
				Object openId = null;
				try {
					json = new JSONObject(result);
					openId=json.get("openid");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String openid=null;
				if(openId!=null){
					openid=(String)openId;
				}
				if(StringUtils.isNotBlank(openid)){
					body=thirdLoginGetSessionKey(apiAccount, openid,null, 
							Constants.THIRD_SOURCE_WEIXIN_APP, request, response);
				}
				message=Constants.API_MESSAGE_SUCCESS;
				status=Constants.API_STATUS_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 第三方登录API
	 * @param thirdKey 第三方key 必选
	 * @param source 第三方来源 非必选 默认微信小程序
	 * @param username 为第三方用户指定创建的用户名
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value = "/user/thirdLogin")
	public void thirdLoginApi(
			String thirdKey,String source,String username,
			String appId,String nonce_str,String sign,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if(StringUtils.isNotBlank(source)){
			source=Constants.THIRD_SOURCE_WEIXIN_APP;
		}
		WebErrors errors=WebErrors.create(request);
		ApiAccount apiAccount = null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, appId,
				nonce_str,sign,thirdKey);
		if(!errors.hasErrors()){
			apiAccount=apiAccountMng.findByAppId(appId);
			errors=ApiValidate.validateApiAccount(request, errors, apiAccount);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_APP_PARAM_ERROR;
			}else{
				//验证签名
				errors=ApiValidate.validateSign(request, errors,apiAccount, sign);
				if(errors.hasErrors()){
					code=ResponseCode.API_CODE_SIGN_ERROR;
				}
			}
		}else{
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}
		if(errors.hasErrors()){
			message=errors.getErrors().get(0);
		}else{
			//签名数据不可重复利用
			ApiRecord record=apiRecordMng.findBySign(sign, appId);
			if(record!=null){
				message=Constants.API_MESSAGE_REQUEST_REPEAT;
				code=ResponseCode.API_CODE_REQUEST_REPEAT;
			}else{
				body=thirdLoginGetSessionKey(apiAccount, thirdKey,
						username, source, request, response);
				apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
						appId, "/user/thirdLogin",sign);
				message=Constants.API_MESSAGE_SUCCESS;
				status=Constants.API_STATUS_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private String thirdLoginGetSessionKey(ApiAccount apiAccount,
			String thirdKey,String username,String source,
			HttpServletRequest request,HttpServletResponse response){
		String aesKey=apiAccount.getAesKey();
		thirdKey=pwdEncoder.encodePassword(thirdKey);
		BbsThirdAccount thirdAccount=thirdAccountMng.findByKey(thirdKey);
		if(thirdAccount!=null){
			username=thirdAccount.getUsername();
		}else{
			//用户不存在,则新建用户
			//若是没有传递用户名则随机用户
			if(StringUtils.isBlank(username)){
				username=getRandomUsername();
			}else{
				//若是传递的用户名存在则随机
				if(userExist(username)){
					username=getRandomUsername();
				}
			}
			BbsUserExt userExt=new BbsUserExt();
			String imageUrl="";
			//第三方授权来自微信小程序
			if(source.equals(Constants.THIRD_SOURCE_WEIXIN_APP)){
				String nickName =request.getParameter("nickName");
				String avatarUrl =request.getParameter("avatarUrl");
				String gender =request.getParameter("gender");
				String province =request.getParameter("province");
				String city =request.getParameter("city");
				String country =request.getParameter("country");
				if(StringUtils.isNotBlank(gender)){
					if(gender.equals(2)){
						userExt.setGender(false);
					}else if(gender.equals(1)){
						userExt.setGender(true);
					}else{
						userExt.setGender(null);
					}
				}
				if(StringUtils.isNotBlank(nickName)){
					userExt.setRealname(nickName);
				}
				String comefrom="";
				if(StringUtils.isNotBlank(country)){
					comefrom+=country;
				}
				if(StringUtils.isNotBlank(province)){
					comefrom+=province;
				}
				if(StringUtils.isNotBlank(city)){
					comefrom+=city;
				}
				userExt.setComefrom(comefrom);
				if(StringUtils.isNotBlank(avatarUrl)){
					CmsConfig config=configMng.get();
					CmsSite site=CmsUtils.getSite(request);
					Ftp ftp=site.getUploadFtp();
					imageUrl=imgSvc.crawlImg(avatarUrl, config.getContextPath(), config.getUploadToDb(), config.getDbFileUri(), ftp, site.getUploadPath());
				}
			}
			String ip = RequestUtils.getIpAddr(request);
			CmsSite site = CmsUtils.getSite(request);
			Integer groupId = null;
			BbsUserGroup group = bbsConfigMng.findById(site.getId())
					.getRegisterGroup();
			if (group != null) {
				groupId = group.getId();
			}
			BbsUser user = null;
			try {
				user = bbsUserMng.registerMember(username, null, false,null,
						  ip, groupId, userExt,null);
			} catch (Exception e) {
				//可能发送邮件异常
			} 
			if(user!=null){
				//解决会话固定漏洞
				LoginUtils.logout();
				bbsUserMng.updateMember(user.getId(), null, null, null, null,
						imageUrl, userExt, null,null);
				bbsWebserviceMng.callWebService(BbsWebservice.SERVICE_TYPE_ADD_USER,
						username,null, null, userExt);
				bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
				//绑定新建的用户
				thirdAccount=new BbsThirdAccount();
				thirdAccount.setUsername(username);
				thirdAccount.setAccountKey(thirdKey);
				thirdAccount.setSource(source);
				thirdAccount.setUser(user);
				thirdAccountMng.save(thirdAccount);
				LoginUtils.loginShiro(request, response, username);
				CmsUtils.setUser(request, user);
			}
		}
		String sessionKey=session.getSessionId(request, response);
		apiUserLoginMng.userLogin(username, sessionKey);
		JSONObject json=new JSONObject();
		try {
			//加密返回
			json.put("sessionKey", AES128Util.encrypt(sessionKey, aesKey,apiAccount.getIvKey()));
			json.put("username",username);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return json.toString();
	}
	
	/**
	 * 获取用户状态API
	 * @param username 用户名 必选
	 * @param sessionKey 会话标识 必选
	 * @param appId appID 必选
	 * @param nonce_str 随机字符串 必选
	 * @param sign 签名必选
	 */
	@RequestMapping(value = "/user/getStatus")
	public void getUserStatus(
			String username,String sessionKey,
			String appId,String nonce_str,String sign,
			HttpServletRequest request,HttpServletResponse response) throws JSONException {
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		WebErrors errors=WebErrors.create(request);
		ApiAccount apiAccount = null;
		BbsUser user = null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, appId,
				nonce_str,sign,username,sessionKey);
		if(!errors.hasErrors()){
			apiAccount=apiAccountMng.findByAppId(appId);
			errors=ApiValidate.validateApiAccount(request, errors, apiAccount);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_APP_PARAM_ERROR;
			}else{
				//验证签名
				errors=ApiValidate.validateSign(request, errors,apiAccount, sign);
				if(errors.hasErrors()){
					code=ResponseCode.API_CODE_SIGN_ERROR;
				}
			}
		}else{
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}
		if(errors.hasErrors()){
			message=errors.getErrors().get(0);
		}else{
			//签名数据不可重复利用
			ApiRecord record=apiRecordMng.findBySign(sign, appId);
			if(record!=null){
				message=Constants.API_MESSAGE_REQUEST_REPEAT;
				code=ResponseCode.API_CODE_REQUEST_REPEAT;
			}else{
				user=bbsUserMng.findByUsername(username);
				if(user!=null){
					String aesKey=apiAccount.getAesKey();
					String decryptSessionKey = null;
					try {
						decryptSessionKey = AES128Util.decrypt(sessionKey, aesKey,apiAccount.getIvKey());
					} catch (Exception e) {
						//e.printStackTrace();
					}
					if(StringUtils.isNotBlank(decryptSessionKey)){
						ApiUserLogin userLogin=apiUserLoginMng.findUserLogin(username, decryptSessionKey);
						if(userLogin!=null){
							message="\"over time\"";
							code=ResponseCode.API_CODE_USER_STATUS_OVER_TIME;
							if(userLogin.getActiveTime()!=null){
								Date now=Calendar.getInstance().getTime();
								Double timeOver=DateUtils.getDiffMinuteTwoDate(userLogin.getActiveTime(), now);
								if(timeOver<=Constants.USER_OVER_TIME){
									message="\"login\"";
									code=ResponseCode.API_CODE_USER_STATUS_LOGIN;
									LoginUtils.loginShiro(request, response, username);
								}else{
									BbsUser currUser=CmsUtils.getUser(request);
									if(currUser!=null){
										//用户已经登陆前台（目前v5版本前台认证登陆机制不同）
										apiUserLoginMng.userActive(request,response);
									}else{
										//如果记住登录的
										Subject subject = SecurityUtils.getSubject();
										if(subject.isRemembered()){
											String rememberUser =  (String) subject.getPrincipal();
											LoginUtils.loginShiro(request, response, rememberUser);
										}else{
											LoginUtils.logout();
										}
									}
								}
							}
						}else{
							message="\"no login\"";
							code=ResponseCode.API_CODE_USER_STATUS_LOGOUT;
							LoginUtils.logout();
						}
						status=Constants.API_STATUS_SUCCESS;
					}else{
						message=Constants.API_MESSAGE_PARAM_ERROR;
						code=ResponseCode.API_CODE_PARAM_ERROR;
					}
				}else{
					//用户不存在
					message=Constants.API_MESSAGE_USER_NOT_FOUND;
					code=ResponseCode.API_CODE_USER_NOT_FOUND;
				}
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private  String getRandomUsername(){
		SimpleDateFormat fomat=new SimpleDateFormat("yyyyMMddHHmmss");
		String username=fomat.format(Calendar.getInstance().getTime())+RandomStringUtils.random(5,Num62.N10_CHARS);;
		if (userExist(username)) {
			return getRandomUsername();
		}else{
			return username;
		}
	}
	
	private  boolean userExist(String username){
		if (unifiedUserMng.usernameExist(username)) {
			return true;
		}else{
			return false;
		}
	}
	
	private void initWeiXinJsCode2SessionUrl(){
		if(getWeiXinJsCode2SessionUrl()==null){
			setWeiXinJsCode2SessionUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),WEIXIN_JSCODE_2_SESSION_URL));
		}
	}
	
	
	
	private String weiXinJsCode2SessionUrl;
	
	public String getWeiXinJsCode2SessionUrl() {
		return weiXinJsCode2SessionUrl;
	}

	public void setWeiXinJsCode2SessionUrl(String weiXinJsCode2SessionUrl) {
		this.weiXinJsCode2SessionUrl = weiXinJsCode2SessionUrl;
	}

	@Autowired
	private ApiRecordMng apiRecordMng;
	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
	@Autowired
	private BbsWebserviceMng bbsWebserviceMng;
	@Autowired
	private BbsThirdAccountMng thirdAccountMng;
	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private ImageSvc imgSvc;
	@Autowired
	private CmsConfigMng configMng;
	@Autowired
	private PwdEncoder pwdEncoder;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsConfigEhCache bbsConfigEhCache;
	@Autowired
	private ConfigMng conMng;
	
}

