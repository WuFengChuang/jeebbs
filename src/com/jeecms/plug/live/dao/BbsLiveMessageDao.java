package com.jeecms.plug.live.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLiveMessage;

public interface BbsLiveMessageDao {
	public Pagination getPage(Integer liveId,int pageNo, int pageSize);

	public BbsLiveMessage findById(Long id);

	public BbsLiveMessage save(BbsLiveMessage bean);

	public BbsLiveMessage updateByUpdater(Updater<BbsLiveMessage> updater);

	public BbsLiveMessage deleteById(Long id);
}