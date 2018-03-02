package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeecms.bbs.entity.ApiAccount;
import com.jeecms.bbs.entity.ApiUserLogin;
import com.jeecms.bbs.entity.BbsUser;

public interface ApiUserLoginMng {
	public Pagination getPage(int pageNo, int pageSize);

	public ApiUserLogin findById(Long id);
	
	public BbsUser getUser(ApiAccount apiAccount,HttpServletRequest request);
	
	public ApiUserLogin findUserLogin(String username,String sessionKey);
	
	public BbsUser findUser(String sessionKey,String aesKey,String ivKey);
	
	public ApiUserLogin userLogin(String username,String sessionKey);
	
	public Short getUserStatus(String sessionKey);
	
	public Short getStatus(ApiAccount apiAccount,
			HttpServletRequest request,HttpServletResponse response);
	
	public ApiUserLogin userLogout(String username,String sessionKey);
	
	public void userActive(HttpServletRequest request,HttpServletResponse response);

	public ApiUserLogin save(ApiUserLogin bean);

	public ApiUserLogin update(ApiUserLogin bean);

	public ApiUserLogin deleteById(Long id);
	
	public ApiUserLogin[] deleteByIds(Long[] ids);
}