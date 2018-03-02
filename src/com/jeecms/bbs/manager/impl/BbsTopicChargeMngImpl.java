package com.jeecms.bbs.manager.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsTopicChargeDao;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicCharge;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsTopicChargeMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsTopicChargeMngImpl implements BbsTopicChargeMng {
	
	@Transactional(readOnly=true)
	public List<BbsTopicCharge> getList(String title,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,int orderBy,Integer first,Integer count){
		return dao.getList(title,authorUserId,
				buyTimeBegin,buyTimeEnd, orderBy,first, count);
	}
	
	@Transactional(readOnly=true)
	public Pagination getPage(String title,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,
			int orderBy,int pageNo,int pageSize){
		return dao.getPage(title,authorUserId,
				buyTimeBegin,buyTimeEnd,orderBy,pageNo,pageSize);
	}
	
	public BbsTopicCharge save(Double chargeAmount, Short charge,
			Boolean rewardPattern,Double rewardRandomMin,
			Double rewardRandomMax,BbsTopic topic){
		BbsTopicCharge topicCharge=new BbsTopicCharge();
		if(charge!=null){
			topicCharge.setChargeAmount(chargeAmount);
			topicCharge.setChargeReward(charge);
			topicCharge.setRewardPattern(rewardPattern);
			topicCharge.setRewardRandomMax(rewardRandomMax);
			topicCharge.setRewardRandomMin(rewardRandomMin);
			topicCharge=save(topicCharge, topic);
		}
		return topicCharge;
	}
	
	public void afterContentUpdate(BbsTopic bean,Short charge,Double chargeAmount
			,Boolean rewardPattern,Double rewardRandomMin,
			Double rewardRandomMax) {
		if(charge!=null&&!charge.equals(BbsTopicCharge.MODEL_FREE)){
			BbsTopicCharge c=bean.getTopicCharge();
			//收费金额变更
			if(c!=null){
				c.setChargeAmount(chargeAmount);
				c.setChargeReward(charge);
				c.setRewardPattern(rewardPattern);
				c.setRewardRandomMax(rewardRandomMax);
				c.setRewardRandomMin(rewardRandomMin);
				update(c);
			}else{
				//从免费变更收费
				save(chargeAmount, charge,rewardPattern,rewardRandomMin,rewardRandomMax,bean);
			}
		}else{
			BbsTopicCharge c=bean.getTopicCharge();
			//从收费变免费
			if(c!=null){
				c.setChargeAmount(0d);
				c.setChargeReward(BbsTopicCharge.MODEL_FREE);
				c.setRewardPattern(rewardPattern);
				c.setRewardRandomMax(rewardRandomMax);
				c.setRewardRandomMin(rewardRandomMin);
				update(c);
			}
		}
	}

	public BbsTopicCharge update(BbsTopicCharge bean) {
		Updater<BbsTopicCharge> updater = new Updater<BbsTopicCharge>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}
	
	public BbsTopicCharge afterUserPay(Double payAmout, BbsTopic topic){
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		BbsTopicCharge charge=topic.getTopicCharge();
		if(charge==null){
			BbsConfigCharge config=configChargeMng.getDefault();
			charge=new BbsTopicCharge();
			charge.setTotalAmount(payAmout);
			charge.setYearAmount(payAmout);
			charge.setMonthAmount(payAmout);
			charge.setDayAmount(payAmout);
			charge.setLastBuyTime(curr.getTime());
			charge.setChargeAmount(0d);
			charge.setChargeReward(BbsTopicCharge.MODEL_FREE);
			charge.setRewardPattern(BbsTopicCharge.REWARD_RANDOM);
			charge.setRewardRandomMin(config.getRewardMin());
			charge.setRewardRandomMax(config.getRewardMax());
			charge.setTopic(topic);
			save(charge, topic);
		}else{
			if(charge.getYearAmount()!=null){
				charge.setYearAmount(charge.getYearAmount()+payAmout);
			}else{
				charge.setYearAmount(payAmout);
			}
			if(charge.getMonthAmount()!=null){
				charge.setMonthAmount(charge.getMonthAmount()+payAmout);
			}else{
				charge.setMonthAmount(payAmout);
			}
			if(charge.getDayAmount()!=null){
				charge.setDayAmount(charge.getDayAmount()+payAmout);
			}else{
				charge.setDayAmount(payAmout);
			}
			if(topic.getLastBuyTime()!=null){
				last.setTime(topic.getLastBuyTime());
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
			if(charge.getTotalAmount()!=null){
				charge.setTotalAmount(charge.getTotalAmount()+payAmout);
			}else{
				charge.setTotalAmount(payAmout);
			}
			charge.setLastBuyTime(curr.getTime());
			update(charge);
		}
		return charge;
	}
	

	private BbsTopicCharge save(BbsTopicCharge charge, BbsTopic topic) {
		topic.setTopicCharge(charge);
		charge.setTopic(topic);
		charge.init();
		dao.save(charge);
		return charge;
	}

	private BbsTopicChargeDao dao;
	@Autowired
	private BbsConfigChargeMng configChargeMng;

	@Autowired
	public void setDao(BbsTopicChargeDao dao) {
		this.dao = dao;
	}
}