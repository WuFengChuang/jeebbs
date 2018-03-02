package com.jeecms.bbs.manager.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;


import com.jeecms.bbs.dao.BbsGiftDao;
import com.jeecms.bbs.entity.BbsGift;
import com.jeecms.bbs.manager.BbsGiftMng;

@Service
@Transactional
public class BbsGiftMngImpl implements BbsGiftMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsGift> getList(Boolean disabled,Integer first, Integer count){
		return dao.getList(disabled,first, count);
	}

	@Transactional(readOnly = true)
	public BbsGift findById(Integer id) {
		BbsGift entity = dao.findById(id);
		return entity;
	}
	
	public BbsGift afterUserPay(Double payAmout, BbsGift gift){
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		if(gift.getLastBuyTime()!=null){
			last.setTime(gift.getLastBuyTime());
			int currDay = curr.get(Calendar.DAY_OF_YEAR);
			int lastDay = last.get(Calendar.DAY_OF_YEAR);
			int currYear=curr.get(Calendar.YEAR);
			int lastYear=last.get(Calendar.YEAR);
			int currMonth = curr.get(Calendar.MONTH);
			int lastMonth = last.get(Calendar.MONTH);
			if(lastYear!=currYear){
				gift.setYearAmount(0d);
				gift.setMonthAmount(0d);
				gift.setDayAmount(0d);
			}else{
				if(currMonth!=lastMonth){
					gift.setMonthAmount(0d);
					gift.setDayAmount(0d);
				}else{
					if (currDay != lastDay) {
						gift.setDayAmount(0d);
					}
				}
			}
		}
		gift.setTotalAmount(gift.getTotalAmount()+payAmout);
		gift.setYearAmount(gift.getYearAmount()+payAmout);
		gift.setMonthAmount(gift.getMonthAmount()+payAmout);
		gift.setDayAmount(gift.getDayAmount()+payAmout);
		gift.setLastBuyTime(curr.getTime());
		return gift;
	}

	public BbsGift save(BbsGift bean) {
		bean.init();
		dao.save(bean);
		return bean;
	}

	public BbsGift update(BbsGift bean) {
		Updater<BbsGift> updater = new Updater<BbsGift>(bean);
		BbsGift entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsGift deleteById(Integer id) {
		BbsGift bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsGift[] deleteByIds(Integer[] ids) {
		BbsGift[] beans = new BbsGift[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsGiftDao dao;

	@Autowired
	public void setDao(BbsGiftDao dao) {
		this.dao = dao;
	}

	@Override
	public void updatePriority(Integer[] idArray, Integer[] prioritiyArray, Boolean[] disabledArray) {
		if (idArray==null||prioritiyArray==null||disabledArray==null
				||idArray.length!=prioritiyArray.length
				||prioritiyArray.length!=disabledArray.length) {
			return;
		}
		BbsGift gift;
		for(int i = 0 ; i<idArray.length;i++){
			gift = findById(idArray[i]);
			gift.setPriority(prioritiyArray[i]);
			gift.setDisabled(disabledArray[i]);
		}
	}
}