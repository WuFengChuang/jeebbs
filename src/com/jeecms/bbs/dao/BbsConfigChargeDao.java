package com.jeecms.bbs.dao;

import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface BbsConfigChargeDao {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsConfigCharge findById(Integer id);

	public BbsConfigCharge save(BbsConfigCharge bean);

	public BbsConfigCharge updateByUpdater(Updater<BbsConfigCharge> updater);

	public BbsConfigCharge deleteById(Integer id);
}