package com.jeecms.bbs.web;

import static com.jeecms.common.web.Constants.MESSAGE;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.CmsSensitivity;
import com.jeecms.bbs.manager.BbsSessionMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.manager.CmsSensitivityMng;
import com.jeecms.common.util.CheckMobile;
import com.jeecms.common.web.LoginUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;

/**
 * BBS上下文信息拦截器
 * 
 * 包括登录信息、权限信息、站点信息
 * 
 * @author tom
 * 
 */
public class FrontContextInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws Exception {
		CmsSite site = null;
		List<CmsSite> list = cmsSiteMng.getListFromCache();
		int size = list.size();
		if (size == 0) {
			throw new RuntimeException("no site record in database!");
		} else if (size == 1) {
			site = list.get(0);
		} else {
			String server = request.getServerName();
			String alias, redirect;
			for (CmsSite s : list) {
				// 检查域名
				if (s.getDomain().equals(server)) {
					site = s;
					break;
				}
				// 检查域名别名
				alias = s.getDomainAlias();
				if (!StringUtils.isBlank(alias)) {
					for (String a : StringUtils.split(alias, ',')) {
						if (a.equals(server)) {
							site = s;
							break;
						}
					}
				}
				// 检查重定向
				redirect = s.getDomainRedirect();
				if (!StringUtils.isBlank(redirect)) {
					for (String r : StringUtils.split(redirect, ',')) {
						if (r.equals(server)) {
							try {
								response.sendRedirect(s.getUrl());
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
							return false;
						}
					}
				}
			}
			if (site == null) {
				throw new SiteNotFoundException(server);
			}
		}
		List<CmsSensitivity> sensitivityList =cmsSensitivityMng.getList(site.getId(), true);
		CmsUtils.setSite(request, site);
		// 敏感词加入线程变量
		CmsThreadVariable.setSensitivityList(sensitivityList);

		BbsUser user = null;
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()||subject.isRemembered()) {
			String username =  (String) subject.getPrincipal();
			user = bbsUserMng.findByUsername(username);
			CmsUtils.setUser(request, user);
			// Site加入线程变量
			CmsThreadVariable.setUser(user);
		}
		if(subject.isRemembered()){
			String username =  (String) subject.getPrincipal();
			LoginUtils.loginShiro(request, response, username);
		}
		if (user != null) {
			// 用户被禁用，提示无权限。
			if (user.getDisabled()) {
				request.setAttribute(MESSAGE, MessageResolver.getMessage(request,"member.gag"));
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}
		}
		checkEquipment(request, response);
		bbsSessionMng.recordUserSession(request, response);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//清空敏感词
		CmsThreadVariable.removeSensitivityList();
	}
	
	/**
	 * 检查访问方式
	 */
	public void checkEquipment(HttpServletRequest request,HttpServletResponse response){
		String ua=(String) session.getAttribute(request,"ua");
		String userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();
		if(null==ua){
			try{
				if(null == userAgent){  
				    userAgent = "";  
				}
				if(CheckMobile.check(userAgent)){
					ua="mobile";
				} else {
					ua="pc";
				}
				session.setAttribute(request, response, "ua",ua);
			}catch(Exception e){}
		}
		if(StringUtils.isNotBlank((ua) )){
			request.setAttribute("ua", ua);
		}
	}
	
	
	private CmsSiteMng cmsSiteMng;
	private BbsUserMng bbsUserMng;
	private CmsSensitivityMng cmsSensitivityMng;
	@Autowired
	private BbsSessionMng bbsSessionMng;
	@Autowired
	private SessionProvider session;

	@Autowired
	public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
		this.cmsSiteMng = cmsSiteMng;
	}
	
	@Autowired
	public void setBbsUserMng(BbsUserMng bbsUserMng) {
		this.bbsUserMng = bbsUserMng;
	}
	
	@Autowired
	public void setCmsSensitivityMng(CmsSensitivityMng cmsSensitivityMng) {
		this.cmsSensitivityMng = cmsSensitivityMng;
	}
}