package com.jeecms.plug.live.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLiveRate;

public interface BbsLiveRateDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public BbsLiveRate findByNearLimitNum(Integer userNum);

	public BbsLiveRate findById(Integer id);

	public BbsLiveRate save(BbsLiveRate bean);

	public BbsLiveRate updateByUpdater(Updater<BbsLiveRate> updater);

	public BbsLiveRate deleteById(Integer id);
}