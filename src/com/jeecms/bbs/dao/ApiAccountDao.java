package com.jeecms.bbs.dao;

import java.util.List;

import com.jeecms.bbs.entity.ApiAccount;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface ApiAccountDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<ApiAccount> getList(int first, int count);
	
	public ApiAccount findByAppId(String appId);

	public ApiAccount findById(Integer id);

	public ApiAccount save(ApiAccount bean);

	public ApiAccount updateByUpdater(Updater<ApiAccount> updater);

	public ApiAccount deleteById(Integer id);
}