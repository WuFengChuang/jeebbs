package com.jeecms.bbs.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

import java.util.Date;
import java.util.List;

import com.jeecms.bbs.entity.BbsIncomeStatistic;

public interface BbsIncomeStatisticDao {
	public Pagination getPage(Date begin,Date end,int pageNo, int pageSize);
	
	public List<BbsIncomeStatistic> getList(Date begin,Date end,Integer first,Integer count);
	
	public List<Object[]> findIncomeStatisticCount(Date begin,Date end,int groupBy);

	public BbsIncomeStatistic findById(Integer id);
	
	public BbsIncomeStatistic findByDate(Date date);

	public BbsIncomeStatistic save(BbsIncomeStatistic bean);

	public BbsIncomeStatistic updateByUpdater(Updater<BbsIncomeStatistic> updater);

	public BbsIncomeStatistic deleteById(Integer id);
}