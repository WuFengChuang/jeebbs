package com.jeecms.bbs.dao;

import com.jeecms.bbs.entity.BbsAccountPay;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

import java.util.Date;
import java.util.List;


public interface BbsAccountPayDao {
	public Pagination getPage(String drawNum,Integer payUserId,Integer drawUserId,
			Date payTimeBegin,Date payTimeEnd,int pageNo, int pageSize);

	public BbsAccountPay findById(Long id);

	public BbsAccountPay save(BbsAccountPay bean);

	public BbsAccountPay updateByUpdater(Updater<BbsAccountPay> updater);

	public BbsAccountPay deleteById(Long id);

	public List<BbsAccountPay> getList(String drawNum, Integer payUserId, Integer drawUserId, Date payTimeBegin,
			Date payTimeEnd, Integer first, Integer count);
}