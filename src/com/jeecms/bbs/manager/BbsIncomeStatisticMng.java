package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;

import java.util.Date;
import java.util.List;

import com.jeecms.bbs.entity.BbsIncomeStatistic;
import com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType;

public interface BbsIncomeStatisticMng {
	public Pagination getPage(Date begin,Date end,int pageNo, int pageSize);
	
	public List<BbsIncomeStatistic> getList(Date begin,Date end,Integer first,Integer count);
	
	public List<Object[]> findIncomeStatisticCount(Date begin,Date end,int groupBy);

	public BbsIncomeStatistic findById(Integer id);
	
	public BbsIncomeStatistic findByDate(Date date);

	public BbsIncomeStatistic save(BbsIncomeStatistic bean);
	
	public BbsIncomeStatistic afterPay(Double amount,
			BbsIncomeType incomeType);

	public BbsIncomeStatistic update(BbsIncomeStatistic bean);

	public BbsIncomeStatistic deleteById(Integer id);
	
	public BbsIncomeStatistic[] deleteByIds(Integer[] ids);
}