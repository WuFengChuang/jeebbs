package com.jeecms.common.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * Cookie 辅助类
 * 
 * @author tom
 * 
 */
public class CookieUtils {
	/**
	 * 每页条数cookie名称
	 */
	public static final String COOKIE_PAGE_SIZE = "_cookie_page_size";
	/**
	 * 排序
	 */
	public static final String COOKIE_ODERBY = "_cookie_orderBy_";
	/**
	 * 默认每页条数
	 */
	public static final int DEFAULT_SIZE = 20;
	/**
	 * 最大每页条数
	 */
	public static final int MAX_SIZE = 200;

	/**
	 * 获得cookie的每页条数
	 * 
	 * 使用_cookie_page_size作为cookie name
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return default:20 max:200
	 */
	public static int getPageSize(HttpServletRequest request) {
		Assert.notNull(request);
		Cookie cookie = getCookie(request, COOKIE_PAGE_SIZE);
		int count = 0;
		if (cookie != null) {
			try {
				count = Integer.parseInt(cookie.getValue());
			} catch (Exception e) {
			}
		}
		if (count <= 0) {
			count = DEFAULT_SIZE;
		} else if (count > MAX_SIZE) {
			count = MAX_SIZE;
		}
		return count;
	}
	
	public static int getOrderBy(Integer forumId,HttpServletRequest request) {
		Assert.notNull(request);
		Cookie cookie = getCookie(request, COOKIE_ODERBY+forumId);
		int orderBy = 0;
		if (cookie != null) {
			try {
				orderBy = Integer.parseInt(cookie.getValue());
			} catch (Exception e) {
			}
		}
		if (orderBy <= 0) {
			orderBy = 1;
		}
		return orderBy;
	}
	
	public static Cookie addOrderByCookie(HttpServletRequest request,
			HttpServletResponse response, Integer forumId, Integer value,
			Integer expiry, String domain,String path) {
		return addCookie(request, response, COOKIE_ODERBY+forumId, 
				value.toString(), expiry, domain,path);
	}

	/**
	 * 获得cookie
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param name
	 *            cookie name
	 * @return if exist return cookie, else return null.
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Assert.notNull(request);
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie c : cookies) {
				if (c.getName().equals(name)) {
					return c;
				}
			}
		}
		return null;
	}

	/**
	 * 根据部署路径，将cookie保存在根目录。
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 * @param expiry
	 * @return
	 */
	public static Cookie addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer expiry, String domain) {
		Cookie cookie = new Cookie(name, value);
		if (expiry != null) {
			cookie.setMaxAge(expiry);
		}
		if (StringUtils.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}
		String ctx = request.getContextPath();
		cookie.setPath(StringUtils.isBlank(ctx) ? "/" : ctx);
		response.addCookie(cookie);
		return cookie;
	}
	
	public static Cookie addCookie(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			Integer expiry, String domain,String path) {
		Cookie cookie = new Cookie(name, value);
		if (expiry != null) {
			cookie.setMaxAge(expiry);
		}
		if (StringUtils.isNotBlank(domain)) {
			cookie.setDomain(domain);
		}
		cookie.setPath(path);
		//,boolean secure,boolean httpOnly
		//cookie.setSecure(secure);
		//cookie.setHttpOnly(httpOnly);
		response.addCookie(cookie);
		return cookie;
	}

	/**
	 * 取消cookie
	 * 
	 * @param response
	 * @param name
	 * @param domain
	 */
	public static void cancleCookie(HttpServletResponse response, String name,
			String domain) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		if (!StringUtils.isBlank(domain)) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}
}
