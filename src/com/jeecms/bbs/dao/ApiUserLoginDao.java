package com.jeecms.bbs.dao;

import com.jeecms.bbs.entity.ApiUserLogin;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface ApiUserLoginDao {
	public Pagination getPage(int pageNo, int pageSize);

	public ApiUserLogin findById(Long id);
	
	public ApiUserLogin findUserLogin(String username,String sessionKey);

	public ApiUserLogin save(ApiUserLogin bean);

	public ApiUserLogin updateByUpdater(Updater<ApiUserLogin> updater);

	public ApiUserLogin deleteById(Long id);
}