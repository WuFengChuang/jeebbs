package com.jeecms.bbs.manager.impl;

import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsConfigChargeDao;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.security.encoder.Md5PwdEncoder;
import com.jeecms.core.manager.CmsConfigMng;

@Service
@Transactional
public class BbsConfigChargeMngImpl implements BbsConfigChargeMng {
	
	@Transactional(readOnly = true)
	public BbsConfigCharge findById(Integer id) {
		BbsConfigCharge entity = dao.findById(id);
		return entity;
	}
	
	public BbsConfigCharge getDefault() {
		return findById(1);
	}

	public BbsConfigCharge update(BbsConfigCharge bean,
			String payTransferPassword,Map<String,String> keys,Map<String,String>fixVal) {
		Updater<BbsConfigCharge> updater = new Updater<BbsConfigCharge>(bean);
		for(Entry<String, String> att:keys.entrySet()){
			if(StringUtils.isBlank(att.getValue())){
				updater.exclude(att.getKey());
			}
		}
		if(StringUtils.isBlank(payTransferPassword)){
			updater.exclude("payTransferPassword");
		}else{
			bean.setPayTransferPassword(pwdEncoder.encodePassword(payTransferPassword));;
		}
		if(fixVal!=null){
			cmsConfigMng.updateRewardFixAttr(fixVal);
		}
		bean = dao.updateByUpdater(updater);
		return bean;
	}
	
	public BbsConfigCharge afterUserPay(Double payAmout,BbsIncomeType incomeType){
		BbsConfigCharge config=getDefault();
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		if(incomeType.equals(BbsIncomeType.ad)){
			if(config.getProfitAdTotal()!=null){
				config.setProfitAdTotal(config.getProfitAdTotal()+payAmout);
			}else{
				config.setProfitAdTotal(payAmout);
			}
		}else if(incomeType.equals(BbsIncomeType.gift)){
			if(config.getProfitGiftTotal()!=null){
				config.setProfitGiftTotal(config.getProfitGiftTotal()+payAmout);
			}else{
				config.setProfitGiftTotal(payAmout);
			}
		}else if(incomeType.equals(BbsIncomeType.magic)){
			if(config.getProfitMagicTotal()!=null){
				config.setProfitMagicTotal(config.getProfitMagicTotal()+payAmout);
			}else{
				config.setProfitMagicTotal(payAmout);
			}
		}else if(incomeType.equals(BbsIncomeType.post)){
			if(config.getProfitPostTotal()!=null){
				config.setProfitPostTotal(config.getProfitPostTotal()+payAmout);
			}else{
				config.setProfitPostTotal(payAmout);
			}
			if(config.getLastBuyTime()!=null){
				last.setTime(config.getLastBuyTime());
				int currDay = curr.get(Calendar.DAY_OF_YEAR);
				int lastDay = last.get(Calendar.DAY_OF_YEAR);
				int currYear=curr.get(Calendar.YEAR);
				int lastYear=last.get(Calendar.YEAR);
				int currMonth = curr.get(Calendar.MONTH);
				int lastMonth = last.get(Calendar.MONTH);
				if(lastYear!=currYear){
					config.setCommissionYear(0d);
					config.setCommissionMonth(0d);
					config.setCommissionDay(0d);
				}else{
					if(currMonth!=lastMonth){
						config.setCommissionMonth(0d);
						config.setCommissionDay(0d);
					}else{
						if (currDay != lastDay) {
							config.setCommissionDay(0d);
						}
					}
				}
			}
			config.setCommissionDay(config.getCommissionDay()+payAmout);
			config.setCommissionMonth(config.getCommissionMonth()+payAmout);
			config.setCommissionYear(config.getCommissionYear()+payAmout);
			config.setCommissionTotal(config.getCommissionTotal()+payAmout);
		}else if(incomeType.equals(BbsIncomeType.ticket)){
			if(config.getProfitLiveTotal()!=null){
				config.setProfitLiveTotal(config.getProfitLiveTotal()+payAmout);
			}else{
				config.setProfitLiveTotal(payAmout);
			}
		}
		config.setLastBuyTime(curr.getTime());
		return config;
	}

	private BbsConfigChargeDao dao;
	@Autowired
	private Md5PwdEncoder pwdEncoder;
	@Autowired
	private CmsConfigMng cmsConfigMng;

	@Autowired
	public void setDao(BbsConfigChargeDao dao) {
		this.dao = dao;
	}
}