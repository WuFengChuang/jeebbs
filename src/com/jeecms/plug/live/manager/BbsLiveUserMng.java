package com.jeecms.plug.live.manager;

import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLiveUser;

public interface BbsLiveUserMng {
	public Pagination getPage(Long orderId,Integer userId,Integer liveId,int pageNo, int pageSize);
	
	public Pagination getLivePage(Integer userId,int pageNo, int pageSize);
	
	public boolean hasBuyLive(Integer userId,Integer liveId);
	
	public boolean saveUserLiveTicket(BbsOrder order,Integer userId,Integer liveId);

	public BbsLiveUser findById(Integer id);

	public BbsLiveUser save(BbsLiveUser bean);

	public BbsLiveUser update(BbsLiveUser bean);

	public BbsLiveUser deleteById(Integer id);
	
	public BbsLiveUser[] deleteByIds(Integer[] ids);
}