package com.jeecms.plug.live.dao;

import java.util.List;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLiveUserAccount;

public interface BbsLiveUserAccountDao {
	public Pagination getPage(Integer userId,Integer orderBy,
			int pageNo, int pageSize);
	
	public List<BbsLiveUserAccount> getList(Integer userId,
			Integer orderBy,int count);

	public BbsLiveUserAccount findById(Integer id);

	public BbsLiveUserAccount save(BbsLiveUserAccount bean);

	public BbsLiveUserAccount updateByUpdater(Updater<BbsLiveUserAccount> updater);

	public BbsLiveUserAccount deleteById(Integer id);
}