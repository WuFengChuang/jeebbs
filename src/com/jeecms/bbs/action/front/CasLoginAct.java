package com.jeecms.bbs.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.UnifiedUser;
import com.jeecms.core.manager.UnifiedUserMng;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.jeecms.bbs.entity.ApiAccount;
import com.jeecms.bbs.entity.ApiUserLogin;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.ApiAccountMng;
import com.jeecms.bbs.manager.ApiUserLoginMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.security.CaptchaErrorException;
import com.jeecms.common.security.CaptchaRequiredException;
import com.jeecms.common.security.DisabledException;
import com.jeecms.common.security.InactiveException;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.web.LoginUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;

@Controller
public class CasLoginAct {

	public static final String LOGIN_INPUT = "tpl.loginInput";
	public static final String LOGIN_STATUS = "tpl.loginStatus";
	public static final String REGISTER_ACTIVE_SUCCESS = "tpl.registerActiveSuccess";
	public static final String COOKIE_ERROR_REMAINING = "_error_remaining";
	/**
	 * 验证码名称
	 */
	public static final String CAPTCHA_PARAM = "captcha";
	
	@RequestMapping(value = "/login.jspx")
	public String login(String returnUrl,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		if(StringUtils.isBlank(returnUrl)){
			session.setAttribute(request, response, "loginSource", null);
		}
		Object source=session.getAttribute(request, "loginSource");
		if(source!=null){
			String loginSource=(String) source;
			model.addAttribute("loginSource",loginSource);
		}
		model.addAttribute("site", site);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site, TPLDIR_MEMBER, LOGIN_INPUT);
	}


	@RequestMapping(value = "/login.jspx", method = RequestMethod.POST)
	public String submit(String username,HttpServletRequest request, 
			HttpServletResponse response,ModelMap model)  {
		CmsSite site = CmsUtils.getSite(request);
		Object error = request.getAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if (error != null) {
			model.addAttribute("error",error);
			model.addAttribute("errorRemaining", unifiedUserMng.errorRemaining(username));
		}
		session.setAttribute(request, response, "loginSource", null);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site, TPLDIR_MEMBER, LOGIN_INPUT);
	}
	
	@RequestMapping(value = "/loginAjax.jspx", method = RequestMethod.POST)
	public void submitAjax(String username,String password,Boolean rememberMe,
			HttpServletRequest request, 
			HttpServletResponse response,ModelMap model)  {
		//验证码校验
		if (isCaptchaRequired(username,request, response)) {
			String captcha = request.getParameter(CAPTCHA_PARAM);
			if (captcha != null) {
				if (!imageCaptchaService.validateResponseForID(session.getSessionId(request, response), captcha)) {
					onLoginFailure(username, request);
				}
			} else {
				onLoginFailure(username, request);
			}
		}
		BbsUser user=bbsUserMng.findByUsername(username);
		if(user!=null){
			if(isDisabled(user)){
				onLoginFailure(username, request);
			}
			if(!isActive(user)){
				onLoginFailure(username, request);
			}
			boolean passwordVaid=unifiedUserMng.isPasswordValid(user.getId(), password);
			if(passwordVaid){
				if(rememberMe==null){
					rememberMe=false;
				}
				LoginUtils.loginShiroRemember(request, response, username,password,rememberMe);
				session.setAttribute(request, response, "userId", user.getId());
				CmsUtils.setUser(request, user);
			}else{
				//密码错误
				onLoginFailure(username, request);
			}
		}
		if (CmsUtils.getUser(request)!=null) {
			ResponseUtils.renderJson(response, "true");
		}else{
			ResponseUtils.renderJson(response, "false");
		}
	}
	
	@RequestMapping(value = "/adminLogin.jspx", method = RequestMethod.POST)
	public void adminLogin(HttpServletRequest request, 
			HttpServletResponse response,ModelMap model)  {
		BbsUser user=CmsUtils.getUser(request);
		ApiAccount apiAccount=apiAccountMng.findByDefault();
		JSONObject json=new JSONObject();
		String encryptSessionKey="";
		//登陆API后台
		if(user!=null&&apiAccount!=null){
			String aesKey=apiAccount.getAesKey();
			String sessionKey=session.getSessionId(request, response);
			apiUserLoginMng.userLogin(user.getUsername(), sessionKey);
			try {
				encryptSessionKey=
						AES128Util.encrypt(sessionKey, aesKey,apiAccount.getIvKey());
				json.put("sessionKey", encryptSessionKey);
				json.put("userName", user.getUsername());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@RequestMapping(value = "/adminLogout.jspx", method = RequestMethod.POST)
	public void adminLogout(String userName,
			String sessionKey,HttpServletRequest request, 
			HttpServletResponse response,ModelMap model)  {
		ApiAccount apiAccount=apiAccountMng.findByDefault();
		JSONObject json=new JSONObject();
		//登出API后台
		if(StringUtils.isNotBlank(userName)&&
				StringUtils.isNotBlank(sessionKey)&&apiAccount!=null){
			String decryptSessionKey="";
			try {
				decryptSessionKey=AES128Util.decrypt(sessionKey, 
						apiAccount.getAesKey(), apiAccount.getIvKey());
			} catch (Exception e) {
			}
			//检查是否登陆
			if(StringUtils.isNotBlank(decryptSessionKey)){
				ApiUserLogin userLogin=apiUserLoginMng.findUserLogin(userName, decryptSessionKey);
				if(userLogin!=null){
					apiUserLoginMng.userLogout(userName,decryptSessionKey);
				}
			}
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	private boolean isCaptchaRequired(String username,HttpServletRequest request,
			HttpServletResponse response) {
		Integer errorRemaining = unifiedUserMng.errorRemaining(username);
		String captcha=RequestUtils.getQueryParam(request, CAPTCHA_PARAM);
		// 如果输入了验证码，那么必须验证；如果没有输入验证码，则根据当前用户判断是否需要验证码。
		if (!StringUtils.isBlank(captcha)|| (errorRemaining != null && errorRemaining < 0)) {
			return true;
		}
		return false;
	}
	
	private void onLoginFailure(String username,HttpServletRequest request) {
		String ip = RequestUtils.getIpAddr(request);
		BbsUser user = bbsUserMng.findByUsername(username);
		if(user!=null){
			unifiedUserMng.updateLoginError(user.getId(), ip);
		}
	}
	
	//用户禁用返回true 未查找到用户或者非禁用返回false
	private boolean isDisabled(BbsUser user){
		if(user!=null){
			if(user.getDisabled()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
		
	//用户激活了返回true 未查找到用户或者非禁用返回false
	private boolean isActive(BbsUser user){
		UnifiedUser unifiedUser=unifiedUserMng.findById(user.getId());
		if(unifiedUser!=null){
			if(unifiedUser.getActivation()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private ImageCaptchaService imageCaptchaService;
	@Autowired
	private BbsUserMng bbsUserMng;
}
