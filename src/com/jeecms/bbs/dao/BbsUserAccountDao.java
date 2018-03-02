package com.jeecms.bbs.dao;

import java.util.Date;
import java.util.List;

import com.jeecms.bbs.entity.BbsUserAccount;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface BbsUserAccountDao {
	
	public Pagination getPage(String username,Date drawTimeBegin,Date drawTimeEnd,
			int orderBy,int pageNo,int pageSize);
	
	public BbsUserAccount findById(Integer id);

	public BbsUserAccount save(BbsUserAccount bean);

	public BbsUserAccount updateByUpdater(Updater<BbsUserAccount> updater);

	public List<BbsUserAccount> getList(String username, Date drawTimeBegin, Date drawTimeEnd, Integer orderBy,
			Integer first, Integer count);
}