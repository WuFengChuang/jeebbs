package com.jeecms.common.web;

/**
 * @author Tom
 */
import java.io.IOException;

import javax.servlet.Filter;

import javax.servlet.FilterChain;

import javax.servlet.FilterConfig;

import javax.servlet.ServletException;

import javax.servlet.ServletRequest;

import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jeecms.core.web.front.URLHelper;


public class XssFilter implements Filter {
	private String excludeUrls;
	FilterConfig filterConfig = null;
	public void init(FilterConfig filterConfig) throws ServletException {
		this.excludeUrls=filterConfig.getInitParameter("excludeUrls");
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		if(isExcludeUrl(request)){
			chain.doFilter(request, response);
		}else{
			HttpServletRequest req=(HttpServletRequest)request;
			chain.doFilter(new XssHttpServletRequestWrapper(req), response);
		}
	}
	
	private boolean isExcludeUrl(ServletRequest request){
		boolean exclude=false;
		if(StringUtils.isNotBlank(excludeUrls)){
			 String[]excludeUrl=excludeUrls.split("@");
			 String reqUrl=URLHelper.getURI((HttpServletRequest)request);
			 if(excludeUrl!=null&&excludeUrl.length>0){
				 for(String url:excludeUrl){
					 if(StringUtils.isNotBlank(url)&&reqUrl.startsWith(url)){
						 exclude=true;
						 break;
					 }
				 }
			 }
		}
		return exclude;
	}

}
