package com.jeecms.plug.live.manager.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveChargeDao;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.entity.BbsLiveCharge;
import com.jeecms.plug.live.manager.BbsLiveChargeMng;

@Service
@Transactional
public class BbsLiveChargeMngImpl implements BbsLiveChargeMng {
	
	public BbsLiveCharge afterUserPay(Double payAmout,Integer ticketNum, BbsLive live){
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		BbsLiveCharge charge=live.getLiveCharge();
		if(charge==null){
			charge=new BbsLiveCharge();
			charge.setTotalAmount(payAmout);
			charge.setYearAmount(payAmout);
			charge.setMonthAmount(payAmout);
			charge.setDayAmount(payAmout);
			charge.setLastBuyTime(curr.getTime());
			charge.setTicketNum(ticketNum);
			charge.setGiftNum(0);
			charge=save(charge, live);
		}else{
			if(live.getLastBuyTime()!=null){
				last.setTime(live.getLastBuyTime());
				int currDay = curr.get(Calendar.DAY_OF_YEAR);
				int lastDay = last.get(Calendar.DAY_OF_YEAR);
				int currYear=curr.get(Calendar.YEAR);
				int lastYear=last.get(Calendar.YEAR);
				int currMonth = curr.get(Calendar.MONTH);
				int lastMonth = last.get(Calendar.MONTH);
				if(lastYear!=currYear){
					charge.setYearAmount(0d);
					charge.setMonthAmount(0d);
					charge.setDayAmount(0d);
				}else{
					if(currMonth!=lastMonth){
						charge.setMonthAmount(0d);
						charge.setDayAmount(0d);
					}else{
						if (currDay != lastDay) {
							charge.setDayAmount(0d);
						}
					}
				}
			}
			charge.setTotalAmount(charge.getTotalAmount()+payAmout);
			charge.setYearAmount(charge.getYearAmount()+payAmout);
			charge.setMonthAmount(charge.getMonthAmount()+payAmout);
			charge.setDayAmount(charge.getDayAmount()+payAmout);
			charge.setLastBuyTime(curr.getTime());
			if(charge.getTicketNum()!=null){
				charge.setTicketNum(charge.getTicketNum()+ticketNum);
			}else{
				charge.setTicketNum(ticketNum);
			}
			update(charge);
		}
		return charge;
	}
	
	/**
	 * live接收礼物
	 * @param live 活动live
	 */
	public BbsLiveCharge afterReceiveGift(BbsLive live,Integer giftNum){
		BbsLiveCharge charge=null;
		if(live!=null){
			charge=live.getLiveCharge();
			if(charge==null){
				charge=new BbsLiveCharge();
				if(giftNum!=null){
					charge.setGiftNum(giftNum);
				}else{
					charge.setGiftNum(0);
				}
				charge=save(charge, live);
			}else{
				if(giftNum!=null){
					if(charge.getGiftNum()!=null){
						charge.setGiftNum(charge.getGiftNum()+giftNum);
					}else{
						charge.setGiftNum(giftNum);
					}
				}
				update(charge);
			}
		}
		return charge;
	}
	
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsLiveCharge findById(Integer id) {
		BbsLiveCharge entity = dao.findById(id);
		return entity;
	}

	public BbsLiveCharge save(BbsLiveCharge bean,BbsLive live) {
		bean.setLive(live);
		dao.save(bean);
		return bean;
	}

	public BbsLiveCharge update(BbsLiveCharge bean) {
		Updater<BbsLiveCharge> updater = new Updater<BbsLiveCharge>(bean);
		BbsLiveCharge entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsLiveCharge deleteById(Integer id) {
		BbsLiveCharge bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsLiveCharge[] deleteByIds(Integer[] ids) {
		BbsLiveCharge[] beans = new BbsLiveCharge[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsLiveChargeDao dao;

	@Autowired
	public void setDao(BbsLiveChargeDao dao) {
		this.dao = dao;
	}
}