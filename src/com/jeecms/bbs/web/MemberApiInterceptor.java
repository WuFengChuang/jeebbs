package com.jeecms.bbs.web;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.ApiAccount;
import com.jeecms.bbs.entity.ApiUserLogin;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.ApiAccountMng;
import com.jeecms.bbs.manager.ApiUserLoginMng;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.web.WebErrors;

/**
 * 会员中心API拦截器
 * 
 *判断用户
 * @author tom
 * 
 */
public class MemberApiInterceptor extends HandlerInterceptorAdapter {
	private static final Logger log = Logger
			.getLogger(MemberApiInterceptor.class);
	public static final String SITE_PARAM = "_site_id_param";
	public static final String SITE_COOKIE = "_site_id_cookie";
	public static final String PERMISSION_MODEL = "_permission_key";

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// 获得站点
		CmsSite site = getSite(request, response);
		CmsUtils.setSite(request, site);
		// Site加入线程变量
		CmsThreadVariable.setSite(site);
		// 正常状态
		String uri = getURI(request);
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		WebErrors errors=WebErrors.create(request);
		//验证appId是否有效
		String code=ResponseCode.API_CODE_USER_NOT_LOGIN;
		ApiAccount apiAccount=apiAccountMng.getApiAccount(request);
		if(apiAccount!=null&&!apiAccount.getDisabled()){
			//获取用户并且刷新用户活跃时间
			BbsUser user=apiUserLoginMng.getUser(apiAccount,request);
			//获取用户状态
			Short userStatus=apiUserLoginMng.getStatus(apiAccount,request,response);
			CmsUtils.setUser(request, user);
			// User加入线程变量
			CmsThreadVariable.setUser(user);
			if(user==null){
				errors.addErrorString(Constants.API_MESSAGE_USER_NOT_LOGIN);
			}else{
				//判断用户活跃状态
				if(userStatus.equals(ApiUserLogin.USER_STATUS_LOGOVERTIME)){
					errors.addErrorString(Constants.API_MESSAGE_USER_OVER_TIME);
					code=ResponseCode.API_CODE_USER_STATUS_OVER_TIME;
				}else{
					boolean needValidateSign=false;
					HandlerMethod handlerMethod = (HandlerMethod) handler;
		            Method method = handlerMethod.getMethod();
		            SignValidate annotation = method.getAnnotation(SignValidate.class);
		            if (annotation != null) {
		            	needValidateSign=annotation.need();
		            }
		            /*
					//是否添加、修改、删除操作，是需要校验签名
		            if(uri.endsWith("save")||uri.endsWith("update")||uri.endsWith("delete")){
		            	needValidateSign=true;
		            }
		            */
					//是否添加、修改、删除操作，是需要校验签名
					if(needValidateSign){
						Object[]result=validateSign(request,errors);
						boolean succ=(boolean) result[0];
						if(!succ){
							code=(String) result[1];
						}
					}
				}
			}
		}else{
			code=ResponseCode.API_CODE_APP_PARAM_ERROR;
			errors.addErrorString(Constants.API_MESSAGE_APP_PARAM_ERROR);
		}
		if(errors.hasErrors()){
			status=Constants.API_STATUS_FAIL;
			message=errors.getErrors().get(0);
			ApiResponse apiResponse=new ApiResponse(body, message, status,code);
			ResponseUtils.renderApiJson(response, request, apiResponse);
			return false;
		}
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView mav)
			throws Exception {
		//刷新用户活跃时间
		apiUserLoginMng.userActive(request,response);
	}

	/**
	 * 按参数、cookie、域名、默认。
	 * 
	 * @param request
	 * @return 不会返回null，如果站点不存在，则抛出异常。
	 */
	private CmsSite getSite(HttpServletRequest request,
			HttpServletResponse response) {
		CmsSite site = getByParams(request, response);
		if (site == null) {
			site = getByCookie(request);
		}
		if (site == null) {
			site = getByDomain(request);
		}
		if (site == null) {
			site = getByDefault();
		}
		if (site == null) {
			throw new RuntimeException("cannot get site!");
		} else {
			return site;
		}
	}

	private CmsSite getByParams(HttpServletRequest request,
			HttpServletResponse response) {
		String p = RequestUtils.getQueryParam(request,SITE_PARAM);
		if (!StringUtils.isBlank(p)) {
			try {
				Integer siteId = Integer.parseInt(p);
				CmsSite site = cmsSiteMng.findById(siteId);
				if (site != null) {
					// 若使用参数选择站点，则应该把站点保存至cookie中才好。
					CookieUtils.addCookie(request, response, SITE_COOKIE, site
							.getId().toString(), null,null);
					return site;
				}
			} catch (NumberFormatException e) {
				log.warn("param site id format exception", e);
			}
		}
		return null;
	}

	private CmsSite getByCookie(HttpServletRequest request) {
		Cookie cookie = CookieUtils.getCookie(request, SITE_COOKIE);
		if (cookie != null) {
			String v = cookie.getValue();
			if (!StringUtils.isBlank(v)) {
				try {
					Integer siteId = Integer.parseInt(v);
					return cmsSiteMng.findById(siteId);
				} catch (NumberFormatException e) {
					log.warn("cookie site id format exception", e);
				}
			}
		}
		return null;
	}

	private CmsSite getByDomain(HttpServletRequest request) {
		String domain = request.getServerName();
		if (!StringUtils.isBlank(domain)) {
			return cmsSiteMng.findByDomain(domain, true);
		}
		return null;
	}

	private CmsSite getByDefault() {
		List<CmsSite> list = cmsSiteMng.getListFromCache();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 获得第三个路径分隔符的位置
	 * 
	 * @param request
	 * @throws IllegalStateException
	 *             访问路径错误，没有三(四)个'/'
	 */
	private static String getURI(HttpServletRequest request)
			throws IllegalStateException {
		UrlPathHelper helper = new UrlPathHelper();
		String uri = helper.getOriginatingRequestUri(request);
		String ctxPath = helper.getOriginatingContextPath(request);
		int start = 0, i = 0, count = 2;
		if (!StringUtils.isBlank(ctxPath)) {
			count++;
		}
		while (i < count && start != -1) {
			start = uri.indexOf('/', start + 1);
			i++;
		}
		if (start <= 0) {
			throw new IllegalStateException(
					"admin access path not like '/jeeadmin/jeecms/...' pattern: "
							+ uri);
		}
		return uri.substring(start);
	}
	
	private Object[] validateSign(HttpServletRequest request,WebErrors errors){
		boolean vali=true;
		String sign=RequestUtils.getQueryParam(request,Constants.COMMON_PARAM_SIGN);
		String appId=RequestUtils.getQueryParam(request,Constants.COMMON_PARAM_APPID);
		ApiAccount apiAccount=apiAccountMng.findByAppId(appId);
		errors=ApiValidate.validateApiAccount(request, errors, apiAccount);
		String code="";
		Object[]result=new Object[2];
		if(errors.hasErrors()){
			code=ResponseCode.API_CODE_APP_PARAM_ERROR;
			vali=false;
		}else{
			//验证签名
			errors=ApiValidate.validateSign(request, errors,apiAccount, sign);
			//Account可能获取不到，需要再次判断
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_SIGN_ERROR;
				vali=false;
			}
		}
		result[0]=vali;
		result[1]=code;
		return result;
	}
	

	private CmsSiteMng cmsSiteMng;

	@Autowired
	private ApiAccountMng apiAccountMng;
	@Autowired
	private ApiUserLoginMng apiUserLoginMng;
	
	@Autowired
	public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
		this.cmsSiteMng = cmsSiteMng;
	}

}