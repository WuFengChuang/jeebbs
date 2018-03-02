package com.jeecms.bbs.dao;

import java.util.List;

import com.jeecms.bbs.entity.BbsUserOnline;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface BbsUserOnlineDao {

	public Pagination getPage(int pageNo, int pageSize);

	public BbsUserOnline findById(Integer id);

	public BbsUserOnline save(BbsUserOnline bean);
	
	public int clearCount(boolean week, boolean month,boolean year);

	public BbsUserOnline updateByUpdater(Updater<BbsUserOnline> bean);

	public BbsUserOnline deleteById(Integer id);

	public List<BbsUserOnline> getList();

}