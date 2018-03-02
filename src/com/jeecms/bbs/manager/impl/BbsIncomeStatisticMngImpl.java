package com.jeecms.bbs.manager.impl;

import static com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType.ad;
import static com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType.gift;
import static com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType.magic;
import static com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType.post;
import static com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType.ticket;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.bbs.dao.BbsIncomeStatisticDao;
import com.jeecms.bbs.entity.BbsIncomeStatistic;
import com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType;
import com.jeecms.bbs.manager.BbsIncomeStatisticMng;

@Service
@Transactional
public class BbsIncomeStatisticMngImpl implements BbsIncomeStatisticMng {
	@Transactional(readOnly = true)
	public Pagination getPage(Date begin,Date end,int pageNo, int pageSize) {
		Pagination page = dao.getPage(begin,end,pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsIncomeStatistic> getList(Date begin,Date end,Integer first,Integer count){
		return dao.getList(begin,end,first,count);
	}
	
	@Transactional(readOnly = true)
	public List<Object[]> findIncomeStatisticCount(Date begin,Date end,int groupBy){
		return dao.findIncomeStatisticCount(begin,end,groupBy);
	}

	@Transactional(readOnly = true)
	public BbsIncomeStatistic findById(Integer id) {
		BbsIncomeStatistic entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsIncomeStatistic findByDate(Date date) {
		BbsIncomeStatistic entity = dao.findByDate(date);
		return entity;
	}

	public BbsIncomeStatistic save(BbsIncomeStatistic bean) {
		dao.save(bean);
		return bean;
	}
	
	public BbsIncomeStatistic afterPay(Double amount,
			BbsIncomeType incomeType){
		Date now=Calendar.getInstance().getTime();
		now=DateUtils.parseDateTimeToDay(now);
		BbsIncomeStatistic sta=findByDate(now);
		boolean isUpdate=false;
		if(sta!=null){
			sta.setTotalIncomeAmount(sta.getTotalIncomeAmount()+amount);
			isUpdate=true;
		}else{
			sta=new BbsIncomeStatistic();
			sta.setTotalIncomeAmount(amount);
			sta.setIncomeDate(now);
			sta.init();
		}
		if (incomeType.equals(ticket)){
			sta.setLiveIncomeAmount(amount+sta.getLiveIncomeAmount());
		}else if(incomeType.equals(ad)){
			sta.setAdIncomeAmount(amount+sta.getAdIncomeAmount());
		}else if(incomeType.equals(gift)){
			sta.setGiftIncomeAmount(amount+sta.getGiftIncomeAmount());
		}else if(incomeType.equals(magic)){
			sta.setMagicIncomeAmount(amount+sta.getMagicIncomeAmount());
		}else if(incomeType.equals(post)){
			sta.setPostIncomeAmount(amount+sta.getPostIncomeAmount());
		}
		if(isUpdate){
			sta=update(sta);
		}else{
			sta=save(sta);
		}
		return sta;
	}

	public BbsIncomeStatistic update(BbsIncomeStatistic bean) {
		Updater<BbsIncomeStatistic> updater = new Updater<BbsIncomeStatistic>(bean);
		BbsIncomeStatistic entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsIncomeStatistic deleteById(Integer id) {
		BbsIncomeStatistic bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsIncomeStatistic[] deleteByIds(Integer[] ids) {
		BbsIncomeStatistic[] beans = new BbsIncomeStatistic[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsIncomeStatisticDao dao;

	@Autowired
	public void setDao(BbsIncomeStatisticDao dao) {
		this.dao = dao;
	}
}