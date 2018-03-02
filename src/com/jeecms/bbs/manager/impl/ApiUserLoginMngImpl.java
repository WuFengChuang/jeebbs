package com.jeecms.bbs.manager.impl;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.dao.ApiUserLoginDao;
import com.jeecms.bbs.entity.ApiAccount;
import com.jeecms.bbs.entity.ApiUserLogin;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.ApiAccountMng;
import com.jeecms.bbs.manager.ApiUserLoginMng;
import com.jeecms.bbs.manager.BbsUserMng;

@Service
@Transactional
public class ApiUserLoginMngImpl implements ApiUserLoginMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public ApiUserLogin findById(Long id) {
		ApiUserLogin entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public ApiUserLogin findUserLogin(String username,String sessionKey){
		return dao.findUserLogin(username, sessionKey);
	}
	
	@Transactional(readOnly = true)
	public BbsUser findUser(String sessionKey,String aesKey,String ivKey){
		String decryptSessionKey="";
		BbsUser user=null;
		if(StringUtils.isNotBlank(sessionKey)){
			try {
				//sessionKey用户会话标志加密串
				decryptSessionKey=AES128Util.decrypt(sessionKey, aesKey,ivKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ApiUserLogin apiUserLogin=findUserLogin(null, decryptSessionKey);
			if(apiUserLogin!=null&&StringUtils.isNotBlank(decryptSessionKey)){
				String username=apiUserLogin.getUsername();
				if(StringUtils.isNotBlank(username)){
					user=userMng.findByUsername(username);
				}
			}
		}
		return user;
	}
	
	public BbsUser getUser(ApiAccount apiAccount,HttpServletRequest request){
		BbsUser user=null;
		String sessionKey=RequestUtils.getQueryParam(request,Constants.COMMON_PARAM_SESSIONKEY);
		String aesKey=apiAccount.getAesKey();
		user=findUser(sessionKey, aesKey, apiAccount.getIvKey());
		return user;
	}
	
	public ApiUserLogin userLogin(String username,String sessionKey){
		ApiUserLogin login=findUserLogin(null,sessionKey);
		if(login==null){
			login=new ApiUserLogin();
			login.setLoginTime(Calendar.getInstance().getTime());
			login.setActiveTime(Calendar.getInstance().getTime());
			login.setLoginCount(1);
			login.setSessionKey(sessionKey);
			login.setUsername(username);
			login=save(login);
		}else{
			login.setLoginTime(Calendar.getInstance().getTime());
			login.setActiveTime(Calendar.getInstance().getTime());
			login.setLoginCount(1+login.getLoginCount());
			login.setSessionKey(sessionKey);
			update(login);
		}
		return login;
	}
	
	public ApiUserLogin userLogout(String username,String sessionKey){
		ApiUserLogin login=findUserLogin(username,sessionKey);
		if(login!=null){
			/* 一个账户只允许一个地方同时登陆则修改
			login.setSessionKey("");
			login.setActiveTime(null);
			update(login);
			*/
			deleteById(login.getId());
		}
		return login;
	}
	
	public void userActive(HttpServletRequest request,HttpServletResponse response){
		String sessionKey=RequestUtils.getQueryParam(request,Constants.COMMON_PARAM_SESSIONKEY);
		ApiAccount apiAccount = apiAccountMng.getApiAccount(request);
		Short status=getStatus(apiAccount,request,response);
		if(apiAccount!=null){
			String decryptSessionKey="";
			String aesKey=apiAccount.getAesKey();
			try {
				decryptSessionKey=AES128Util.decrypt(sessionKey, aesKey,apiAccount.getIvKey());
			} catch (Exception e) {
				//e.printStackTrace();
			}
			if(StringUtils.isNotBlank(decryptSessionKey)){
				userActive(decryptSessionKey);
			}
		}
		if(apiAccount!=null&&status.equals(ApiUserLogin.USER_STATUS_LOGIN)){
			userActive(sessionKey);
		}
	}
	
	public Short getStatus(ApiAccount apiAccount,
			HttpServletRequest request,HttpServletResponse response){
		String sessionKey=RequestUtils.getQueryParam(request,Constants.COMMON_PARAM_SESSIONKEY);
		Short loginStatus=ApiUserLogin.USER_STATUS_LOGOUT;
		if(apiAccount!=null){
			String decryptSessionKey="";
			String aesKey=apiAccount.getAesKey();
			try {
				decryptSessionKey=AES128Util.decrypt(sessionKey, aesKey,apiAccount.getIvKey());
			} catch (Exception e) {
				//e.printStackTrace();
			}
			if(StringUtils.isNotBlank(decryptSessionKey)){
				loginStatus=getUserStatus(decryptSessionKey);
			}
		}
		return loginStatus;
	}
	
	@Transactional(readOnly = true)
	public Short getUserStatus(String sessionKey){
		ApiUserLogin login=findUserLogin(null, sessionKey);
		if(login!=null&&login.getActiveTime()!=null&&login.getSessionKey().equals(sessionKey)){
			Date activeTime=login.getActiveTime();
			Date now=Calendar.getInstance().getTime();
			if(DateUtils.getDiffMinuteTwoDate(activeTime, now)<=Constants.USER_OVER_TIME){
				return ApiUserLogin.USER_STATUS_LOGIN;
			}else{
				return ApiUserLogin.USER_STATUS_LOGOVERTIME;
			}
		}else{
			return ApiUserLogin.USER_STATUS_LOGOUT;
		}
	}
	
	private ApiUserLogin userActive(String sessionKey){
		ApiUserLogin login=findUserLogin(null, sessionKey);
		if(login!=null){
			login.setActiveTime(Calendar.getInstance().getTime());
		}
		return login;
	}

	public ApiUserLogin save(ApiUserLogin bean) {
		dao.save(bean);
		return bean;
	}

	public ApiUserLogin update(ApiUserLogin bean) {
		Updater<ApiUserLogin> updater = new Updater<ApiUserLogin>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public ApiUserLogin deleteById(Long id) {
		ApiUserLogin bean = dao.deleteById(id);
		return bean;
	}
	
	public ApiUserLogin[] deleteByIds(Long[] ids) {
		ApiUserLogin[] beans = new ApiUserLogin[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private ApiUserLoginDao dao;
	@Autowired
	private BbsUserMng userMng;
	@Autowired
	private ApiAccountMng apiAccountMng;

	@Autowired
	public void setDao(ApiUserLoginDao dao) {
		this.dao = dao;
	}
}