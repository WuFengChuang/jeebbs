package com.jeecms.bbs.action.login;


import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.bbs.Constants.TPLDIR_INDEX;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.bbs.cache.BbsConfigEhCache;
import com.jeecms.bbs.entity.BbsThirdAccount;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.entity.BbsWebservice;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsThirdAccountMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.manager.BbsWebserviceMng;
import com.jeecms.bbs.service.ImageSvc;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.security.encoder.PwdEncoder;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.HttpClientUtil;
import com.jeecms.common.web.HttpRequestUtil;
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
import com.jeecms.core.manager.UnifiedUserMng;
import com.jeecms.core.web.WebErrors;


/**
 * 第三方登录Action
 * 腾讯qq、新浪微博、微信登陆
 */
@Controller
public class ThirdLoginAct {
	public static final String TPL_BIND = "tpl.member.bind";
	public static final String TPL_AUTH = "tpl.member.auth";
	public static final String TPL_INDEX = "tpl.index";
	
	public static final String USER_LOG_OUT_FLAG = "logout";
	public static final String WEIXIN_AUTH_CODE_URL ="weixin.auth.getQrCodeUrl";
	public static final String WEIXIN_AUTH_TOKEN_URL ="weixin.auth.getAccessTokenUrl";
	public static final String WEIXIN_AUTH_USER_URL ="weixin.auth.getUserInfoUrl";
	
	
	@RequestMapping(value = "/public_auth.jspx")
	public String auth(String openId,HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, TPL_AUTH);
	}
	
	@RequestMapping(value = "/public_auth_login.jspx")
	public void authLogin(String key,String source,HttpServletRequest request,HttpServletResponse response, ModelMap model) throws JSONException {
		if(StringUtils.isNotBlank(source)){
			if(source.equals(BbsThirdAccount.QQ_PLAT)){
				session.setAttribute(request,response,BbsThirdAccount.QQ_KEY, key);
			}else if(source.equals(BbsThirdAccount.QQ_WEBO_PLAT)){
				session.setAttribute(request,response,BbsThirdAccount.QQ_WEBO_KEY, key);
			}else if(source.equals(BbsThirdAccount.SINA_PLAT)){
				session.setAttribute(request,response,BbsThirdAccount.SINA_KEY, key);
			}
		}
		JSONObject json=new JSONObject();
		//库中存放的是加密后的key
		if(StringUtils.isNotBlank(key)){
			key=pwdEncoder.encodePassword(key);
		}
		BbsThirdAccount account=accountMng.findByKey(key);
		if(account!=null){
			json.put("succ", true);
			//已绑定直接登陆
			loginByKey(key, request, response, model);
		}else{
			json.put("succ", false);
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@RequestMapping(value = "/public_bind.jspx",method = RequestMethod.GET)
	public String bind_get(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, TPL_BIND);
	}
	
	@RequestMapping(value = "/public_bind.jspx",method = RequestMethod.POST)
	public String bind_post(String username,String password,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		boolean usernameExist=unifiedUserMng.usernameExist(username);
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		String source="";
		if(!usernameExist){
			//用户名不存在
			errors.addErrorCode("error.usernameNotExist");
		}else{
			UnifiedUser u=unifiedUserMng.getByUsername(username);
			boolean passwordValid=unifiedUserMng.isPasswordValid(u.getId(), password);
			if(!passwordValid){
				errors.addErrorCode("error.passwordInvalid");
			}else{
				//获取用户来源
				String openId=(String) session.getAttribute(request, BbsThirdAccount.QQ_KEY);
				String uid=(String) session.getAttribute(request, BbsThirdAccount.SINA_KEY);
				String weboOpenId=(String) session.getAttribute(request, BbsThirdAccount.QQ_WEBO_KEY);
				String weixinOpenId=(String) session.getAttribute(request, BbsThirdAccount.WEIXIN_KEY);
				if(StringUtils.isNotBlank(openId)){
					source=BbsThirdAccount.QQ_PLAT;
				}else if(StringUtils.isNotBlank(uid)){
					source=BbsThirdAccount.SINA_PLAT;
				}else if(StringUtils.isNotBlank(weboOpenId)){
					source=BbsThirdAccount.QQ_WEBO_PLAT;
				}else if(StringUtils.isNotBlank(weixinOpenId)){
					source=BbsThirdAccount.WEIXIN_PLAT;
				}
				//提交登录并绑定账号
				loginByUsername(username, request, response, model);
			}
		}
		if(errors.hasErrors()){
			errors.toModel(model);
			model.addAttribute("success",false);
		}else{
			model.addAttribute("success",true);
		}
		model.addAttribute("source", source);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,TPLDIR_MEMBER, TPL_BIND);
	}
	
	@RequestMapping(value = "/public_bind_username.jspx")
	public String bind_username_post(String username,
			String nickname,Integer sex,String province,
			String city,String headimgurl,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		String source="";
		if(StringUtils.isBlank(username)){
			//用户名为空
			errors.addErrorCode("error.usernameRequired");
		}else{
			boolean usernameExist=unifiedUserMng.usernameExist(username);
			if(usernameExist){
				//用户名存在
				errors.addErrorCode("error.usernameExist");
			}else{
				//获取用户来源
				String openId=(String) session.getAttribute(request, BbsThirdAccount.QQ_KEY);
				String uid=(String) session.getAttribute(request, BbsThirdAccount.SINA_KEY);
				String weboOpenId=(String) session.getAttribute(request, BbsThirdAccount.QQ_WEBO_KEY);
				String weixinOpenId=(String) session.getAttribute(request, BbsThirdAccount.WEIXIN_KEY);
				BbsUserExt ext=new BbsUserExt();
				String imageUrl="";
				if(StringUtils.isNotBlank(weixinOpenId)){
					String comefrom="";
					if(StringUtils.isNotBlank(province)){
						comefrom+=province;
					}
					if(StringUtils.isNotBlank(city)){
						comefrom+=city;
					}
					ext.setComefrom(comefrom);
					if(StringUtils.isNotBlank(nickname)){
						ext.setRealname(nickname);
					}
					if(sex!=null){
						if(sex.equals(1)){
							ext.setGender(true);
						}else if(sex.equals(2)){
							ext.setGender(false);
						}
					}
					if(StringUtils.isNotBlank(headimgurl)){
						CmsConfig config=cmsConfigMng.get();
						Ftp ftp=site.getUploadFtp();
					    imageUrl=imgSvc.crawlImg(headimgurl, 
								config.getContextPath(), config.getUploadToDb(), 
								config.getDbFileUri(), ftp, site.getUploadPath());
					}
				}
				//(获取到登录授权key后可以注册用户)
				if(StringUtils.isNotBlank(openId)
						||StringUtils.isNotBlank(uid)||
						StringUtils.isNotBlank(weboOpenId)||
						StringUtils.isNotBlank(weixinOpenId)){
					//初始设置密码同用户名
					Integer groupId = null;
					BbsUserGroup group = bbsConfigMng.findById(site.getId())
							.getRegisterGroup();
					if (group != null) {
						groupId = group.getId();
					}
					try {
						BbsUser user =bbsUserMng.registerMember(username, null,false,
								username,RequestUtils.getIpAddr(request), groupId,
								ext,null);
						bbsUserMng.updateMember(user.getId(), null, null, null, null,
								imageUrl, ext, null,null);
						bbsConfigEhCache.setBbsConfigCache(0, 0, 0, 1, user, site.getId());
						callWebService(username, username, null, new BbsUserExt(), BbsWebservice.SERVICE_TYPE_ADD_USER);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
				if(StringUtils.isNotBlank(openId)){
					source=BbsThirdAccount.QQ_PLAT;
				}else if(StringUtils.isNotBlank(uid)){
					source=BbsThirdAccount.SINA_PLAT;
				}else if(StringUtils.isNotBlank(weboOpenId)){
					source=BbsThirdAccount.QQ_WEBO_PLAT;
				}else if(StringUtils.isNotBlank(weixinOpenId)){
					source=BbsThirdAccount.WEIXIN_PLAT;
				}
				//提交登录并绑定账号
				loginByUsername(username, request, response, model);
			}
		}
		if(errors.hasErrors()){
			errors.toModel(model);
			model.addAttribute("success",false);
		}else{
			model.addAttribute("success",true);
		}
		model.addAttribute("source", source);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,TPLDIR_MEMBER, TPL_BIND);
	}
	
	@RequestMapping(value = "/weixin_login.jspx")
	public String weixinLogin(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String codeUrl="";
		if(getWeixinAuthCodeUrl()==null){
			codeUrl=PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),WEIXIN_AUTH_CODE_URL);
			setWeixinAuthCodeUrl(codeUrl);
		}
		CmsConfig config=cmsConfigMng.get();
		String auth_url="/weixin_auth.jspx";
		String redirect_uri=site.getUrlPrefixWithNoDefaultPort();
		if(StringUtils.isNotBlank(site.getContextPath())){
			redirect_uri+=site.getContextPath();
		}
		redirect_uri+=auth_url;
		codeUrl=getWeixinAuthCodeUrl()+"&appid="+config.getWeixinLoginId()+"&redirect_uri="+redirect_uri
				+"&state="+RandomStringUtils.random(10,Num62.N36_CHARS)+"#wechat_redirect";
		return "redirect:"+codeUrl;
	}
	
	@RequestMapping(value = "/weixin_auth.jspx")
	public String weixinAuth(String code,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		if(getWeixinAuthTokenUrl()==null){
			setWeixinAuthTokenUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),WEIXIN_AUTH_TOKEN_URL));
		}
		if(getWeixinAuthUserUrl()==null){
			setWeixinAuthUserUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),WEIXIN_AUTH_USER_URL));
		}
		CmsConfig config=cmsConfigMng.get();
		String tokenUrl=getWeixinAuthTokenUrl()+"&appid="+config.getWeixinLoginId()+"&secret="+config.getWeixinLoginSecret()+"&code="+code;
		JSONObject json=null;
		try {
			//获取openid和access_token
			json = new JSONObject(HttpClientUtil.getInstance().get(tokenUrl));
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		if(json!=null){
			try {
				String openid = json.getString("openid");
				String access_token = json.getString("access_token");
				if(StringUtils.isNotBlank(openid)&&StringUtils.isNotBlank(access_token)){
					//库中存储的是加密后的
					String md5OpenId=pwdEncoder.encodePassword(openid);
					BbsThirdAccount account=accountMng.findByKey(md5OpenId);
					if(account!=null){
						//已绑定直接登陆
						loginByKey(md5OpenId, request, response, model);
						return "redirect:index.jhtml";
					}else{
						String userUrl=getWeixinAuthUserUrl()+"&access_token="+access_token+"&openid="+openid;
						try {
							//获取用户信息
							json = new JSONObject(HttpClientUtil.getInstance().get(userUrl));
							String nickname=(String) json.get("nickname");
							Integer sex=(Integer) json.get("sex");
							String province=(String)json.get("province");
							String city=(String)json.get("city");
							String headimgurl=(String)json.get("headimgurl");
							model.addAttribute("nickname", nickname);
							model.addAttribute("sex", sex);
							model.addAttribute("province", province);
							model.addAttribute("city", city);
							model.addAttribute("headimgurl", headimgurl);
							session.setAttribute(request, response, BbsThirdAccount.WEIXIN_KEY, openid);
							return FrontUtils.getTplPath(request, site.getSolutionPath(),
									TPLDIR_MEMBER, TPL_BIND);
						} catch (JSONException e3) {
							e3.printStackTrace();
						}
					}
				}
			} catch (JSONException e) {
				WebErrors errors=WebErrors.create(request);
				String errcode = null;
				try {
					errcode = json.getString("errcode");
				} catch (JSONException e1) {
					//e1.printStackTrace();
				}
				if(StringUtils.isNotBlank(errcode)){
					errors.addErrorCode("weixin.auth.fail");
				}
				return FrontUtils.showError(request, response, model, errors);
			}
		}
		return FrontUtils.showMessage(request, model,"weixin.auth.succ");
	}
	
	//判断用户是否登录
	@RequestMapping(value = "/sso/authenticate.jspx")
	public void authenticate(String username,String sessionId,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		BbsUser user= bbsUserMng.findByUsername(username);
		if(user!=null&&sessionId!=null){
			String userSessionId=user.getSessionId();
			if(StringUtils.isNotBlank(userSessionId)){
				if(userSessionId.equals(sessionId)){
					ResponseUtils.renderJson(response, "true");
				}
			}else{
				ResponseUtils.renderJson(response, "false");
			}
		}
	}
	
	@RequestMapping(value = "/sso/login.jspx")
	public void loginSso(String username,String sessionId,String ssoLogout,HttpServletRequest request,HttpServletResponse response) {
		BbsUser user =CmsUtils.getUser(request);
		if(StringUtils.isNotBlank(username)){
			//未登录过其他系统
			JSONObject object =new JSONObject();
			try {
				if(user==null){
					//未登录，其他地方已经登录，则登录自身
					CmsConfig config=cmsConfigMng.get();
					List<String>authenticateUrls=config.getSsoAuthenticateUrls();
					String success=authenticate(username, sessionId, authenticateUrls);
					if(success.equals("true")){
						//解决会话固定漏洞
						LoginUtils.logout();
						LoginUtils.loginShiro(request, response, username);
						user = bbsUserMng.findByUsername(username);
						if(user!=null){
							bbsUserMng.updateLoginInfo(user.getId(), null,null,sessionId);
						}
						object.put("result", "login");
					}
				}else if(StringUtils.isNotBlank(ssoLogout)&&ssoLogout.equals("true")){
					LoginUtils.logout();
					object.put("result", "logout");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ResponseUtils.renderJson(response, object.toString());
		}
	}
	
	private String authenticate(String username,String sessionId,List<String>authenticateUrls){
		String result="false";
		for(String url:authenticateUrls){
			result=authenticate(username, sessionId, url);
			if(result.equals("true")){
				break;
			}
		}
		return result;
	}
	
	private String authenticate(String username,String sessionId,String authenticateUrl){
		Map<String,String>params=new HashMap<String, String>();
		params.put("username", username);
		params.put("sessionId", sessionId);
		String result="false";
		try {
			result=HttpRequestUtil.request(authenticateUrl, params, "post", "utf-8");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	/**
	 * 用户名登陆,绑定用户名和第三方账户key
	 * @param username
	 * @param request
	 * @param response
	 * @param model
	 */
	private void loginByUsername(String username, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String openId=(String) session.getAttribute(request, BbsThirdAccount.QQ_KEY);
		String uid=(String) session.getAttribute(request, BbsThirdAccount.SINA_KEY);
		String weboOpenId=(String) session.getAttribute(request, BbsThirdAccount.QQ_WEBO_KEY);
		String weixinOpenId=(String) session.getAttribute(request, BbsThirdAccount.WEIXIN_KEY);
		//解决会话固定漏洞
		LoginUtils.logout();
		if(StringUtils.isNotBlank(openId)){
			LoginUtils.loginShiro(request, response, username);
			//绑定账户
			bind(username, openId,  BbsThirdAccount.QQ_PLAT);
		}
		if(StringUtils.isNotBlank(uid)){
			LoginUtils.loginShiro(request, response, username);
			//绑定账户
			bind(username, uid,  BbsThirdAccount.SINA_PLAT);
		}
		if(StringUtils.isNotBlank(weboOpenId)){
			LoginUtils.loginShiro(request, response, username);
			//绑定账户
			bind(username, weboOpenId,  BbsThirdAccount.QQ_WEBO_PLAT);
		}
		if(StringUtils.isNotBlank(weixinOpenId)){
			LoginUtils.loginShiro(request, response, username);
			//绑定账户
			bind(username, weixinOpenId,  BbsThirdAccount.WEIXIN_PLAT);
		}
	}
	
	/**
	 * 已绑定用户key登录
	 * @param key
	 * @param request
	 * @param response
	 * @param model
	 */
	private void loginByKey(String key,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		BbsThirdAccount account=accountMng.findByKey(key);
		if(StringUtils.isNotBlank(key)&&account!=null){
			//解决会话固定漏洞
			LoginUtils.logout();
			String username=account.getUsername();
			LoginUtils.loginShiro(request, response, username);
		}
	}
	
	
	
	private void bind(String username,String openId,String source){
		BbsThirdAccount account=accountMng.findByKey(openId);
		if(account==null){
			account=new BbsThirdAccount();
			account.setUsername(username);
			//第三方账号唯一码加密存储 防冒名登录
			openId=pwdEncoder.encodePassword(openId);
			account.setAccountKey(openId);
			account.setSource(source);
			account.setUser(bbsUserMng.findByUsername(username));
			accountMng.save(account);
		}
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
	
	private String weixinAuthCodeUrl;
	private String weixinAuthTokenUrl;
	private String weixinAuthUserUrl;
	

	public String getWeixinAuthCodeUrl() {
		return weixinAuthCodeUrl;
	}

	public void setWeixinAuthCodeUrl(String weixinAuthCodeUrl) {
		this.weixinAuthCodeUrl = weixinAuthCodeUrl;
	}

	public String getWeixinAuthTokenUrl() {
		return weixinAuthTokenUrl;
	}

	public void setWeixinAuthTokenUrl(String weixinAuthTokenUrl) {
		this.weixinAuthTokenUrl = weixinAuthTokenUrl;
	}
	
	public String getWeixinAuthUserUrl() {
		return weixinAuthUserUrl;
	}

	public void setWeixinAuthUserUrl(String weixinAuthUserUrl) {
		this.weixinAuthUserUrl = weixinAuthUserUrl;
	}
	
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsThirdAccountMng accountMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private PwdEncoder pwdEncoder;
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private BbsWebserviceMng bbsWebserviceMng;
	@Autowired
	private BbsConfigEhCache bbsConfigEhCache;
	@Autowired
	private ImageSvc imgSvc;
}
