package com.jeecms.bbs.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsWebserviceAuth;

public interface BbsWebserviceAuthDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public BbsWebserviceAuth findByUsername(String username);

	public BbsWebserviceAuth findById(Integer id);

	public BbsWebserviceAuth save(BbsWebserviceAuth bean);

	public BbsWebserviceAuth updateByUpdater(Updater<BbsWebserviceAuth> updater);

	public BbsWebserviceAuth deleteById(Integer id);
}