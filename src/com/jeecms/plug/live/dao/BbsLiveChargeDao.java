package com.jeecms.plug.live.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLiveCharge;

public interface BbsLiveChargeDao {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsLiveCharge findById(Integer id);

	public BbsLiveCharge save(BbsLiveCharge bean);

	public BbsLiveCharge updateByUpdater(Updater<BbsLiveCharge> updater);

	public BbsLiveCharge deleteById(Integer id);
}