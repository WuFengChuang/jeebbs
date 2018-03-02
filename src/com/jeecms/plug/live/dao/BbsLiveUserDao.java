package com.jeecms.plug.live.dao;

import java.util.List;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLiveUser;

public interface BbsLiveUserDao {
	public Pagination getPage(Long orderId,Integer userId,Integer liveId,int pageNo, int pageSize);
	
	public Pagination getLivePage(Integer userId,int pageNo, int pageSize);
	
	public List<BbsLiveUser> getList(Integer userId,Integer liveId,
			Integer first,Integer count);

	public BbsLiveUser findById(Integer id);

	public BbsLiveUser save(BbsLiveUser bean);

	public BbsLiveUser updateByUpdater(Updater<BbsLiveUser> updater);

	public BbsLiveUser deleteById(Integer id);
}