package com.jeecms.bbs.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsAdvertisingDao;
import com.jeecms.bbs.entity.BbsAdvertising;
import com.jeecms.bbs.entity.BbsAdvertisingSpace;
import com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsAdvertisingMng;
import com.jeecms.bbs.manager.BbsAdvertisingSpaceMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsIncomeStatisticMng;
import com.jeecms.bbs.manager.BbsUserAccountMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.core.manager.CmsConfigMng;

@Service
@Transactional
public class BbsAdvertisingMngImpl implements BbsAdvertisingMng {
	@Transactional(readOnly = true)
	public Pagination getPage(Integer siteId, Integer adspaceId,
			Boolean enabled, Integer queryChargeMode,String queryTitle,
			Integer ownerId,int pageNo, int pageSize) {
		Pagination page = dao.getPage(siteId, adspaceId, enabled, 
				queryChargeMode,queryTitle,ownerId,pageNo,pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public List<BbsAdvertising> getList(Integer adspaceId, Boolean enabled,
			Integer ownerId,Integer count) {
		return dao.getList(adspaceId, enabled,ownerId,count);
	}
	
	@Transactional(readOnly = true)
	public List<BbsAdvertising> getApiList(String queryTitle, Integer orderBy, Integer queryChargeMode, Integer first,
			Integer count) {
		return dao.getApiList(queryTitle, orderBy, queryChargeMode, first, count);
	}
	
	@Transactional(readOnly = true)
	public Long findAdvertiseCount(Integer adspaceId, Boolean enabled,
			Integer ownerId,Date queryTimeBegin,Date queryTimeEnd) {
		return dao.findAdvertiseCount(adspaceId, enabled,ownerId,queryTimeBegin,queryTimeEnd);
	}

	@Transactional(readOnly = true)
	public BbsAdvertising findById(Integer id) {
		BbsAdvertising entity = dao.findById(id);
		return entity;
	}

	public BbsAdvertising save(BbsAdvertising bean, Integer adspaceId,
			Map<String, String> attr) {
		bean.setAdspace(bbsAdvertisingSpaceMng.findById(adspaceId));
		bean.setAttr(attr);
		bean.init();
		dao.save(bean);
		return bean;
	}
	
	public BbsAdvertising save(BbsAdvertising bean, Integer adspaceId,
			Integer chargeDay,Date startTime,Map<String, String> attr){
		bean.setAdspace(bbsAdvertisingSpaceMng.findById(adspaceId));
		bean.setAttr(attr);
		bean.init();
		if(startTime!=null&&chargeDay>=0&&
				bean.getChargeMode().equals(BbsAdvertising.CHARGE_MODE_DAY)){
			bean.setEndTime(DateUtils.afterDate(startTime, chargeDay));
		}
		bean=dao.save(bean);
		//广告主的广告费用扣除
		if(bean.getChargeAmount()!=null&&bean.getChargeAmount()>0){
			BbsUser owner=bean.getOwner();
			if(owner!=null){
				userAccountMng.adPay(bean.getChargeAmount(), owner);
			}
		}
		return bean;
	}

	public BbsAdvertising update(BbsAdvertising bean, Integer adspaceId,
			Integer chargeDay,
			Date startTime,Map<String, String> attr) {
		BbsAdvertisingSpace adSpace=bbsAdvertisingSpaceMng.findById(adspaceId);
		Updater<BbsAdvertising> updater = new Updater<BbsAdvertising>(bean);
		updater.include(BbsAdvertising.PROP_CODE);
		updater.include(BbsAdvertising.PROP_START_TIME);
		updater.include(BbsAdvertising.PROP_END_TIME);
		bean = dao.updateByUpdater(updater);
		bean.setAdspace(adSpace);
		bean.getAttr().clear();
		bean.getAttr().putAll(attr);
		return bean;
	}
	
	public BbsAdvertising update(BbsAdvertising bean) {
		Updater<BbsAdvertising> updater = new Updater<BbsAdvertising>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}
	
	public BbsAdvertising deleteById(Integer id) {
		BbsAdvertising bean = dao.deleteById(id);
		return bean;
	}

	public BbsAdvertising[] deleteByIds(Integer[] ids) {
		BbsAdvertising[] beans = new BbsAdvertising[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	public boolean getCanDisplay(Integer id){
		BbsAdvertising ad = findById(id);
		boolean canDisplay=true;
		if (ad != null) {
			//展现收费模式，若广告是按展现量收费，广告主费用不足则禁用广告
			if(ad.getChargeMode().equals(BbsAdvertising.CHARGE_MODE_DISPLAY)){
				Double adDisplayCharge=configMng.get().getAdDisplayCharge();
				if(adDisplayCharge>0&&ad.getOwner()!=null&&
						ad.getOwner().getAdAccountMount()<adDisplayCharge){
					canDisplay=false;
				}
			}
		}else{
			canDisplay=false;
		}
		return canDisplay;
	}
	
	public boolean getCanClick(Integer id){
		BbsAdvertising ad = findById(id);
		boolean canClick=true;
		if (ad != null) {
			//点击收费模式，若广告是按点击量收费，广告主费用不足则禁用广告
			if(ad.getChargeMode().equals(BbsAdvertising.CHARGE_MODE_CLICK)){
				Double adClickCharge=configMng.get().getAdClickCharge();
				if(adClickCharge>0&&ad.getOwner()!=null&&
						ad.getOwner().getAdAccountMount()<adClickCharge){
					canClick=false;
				}
			}
		}else{
			canClick=false;
		}
		return canClick;
	}
	
	
	public void display(Integer id) {
		BbsAdvertising ad = findById(id);
		if (ad != null) {
			boolean canDisplay=getCanDisplay(id);
			if(canDisplay){
				ad.setDisplayCount(ad.getDisplayCount() + 1);
				Double adDisplayCharge =configMng.get().getAdDisplayCharge();
				if(ad.getOwner()!=null&&adDisplayCharge>0&&
						ad.getChargeMode().equals(BbsAdvertising.CHARGE_MODE_DISPLAY)){
					userAccountMng.adPay(adDisplayCharge,ad.getOwner());
				}
			}else{
				ad.setEnabled(false);
				update(ad);
			}
		}
	}

	public void click(Integer id) {
		BbsAdvertising ad = findById(id);
		if (ad != null) {
			boolean canClick=getCanClick(id);
			if(canClick){
				ad.setClickCount(ad.getClickCount() + 1);
				Double adClickCharge =configMng.get().getAdClickCharge();
				if(ad.getOwner()!=null&&adClickCharge>0&&
						ad.getChargeMode().equals(BbsAdvertising.CHARGE_MODE_CLICK)){
					userAccountMng.adPay(adClickCharge,ad.getOwner());
				}
			}else{
				ad.setEnabled(false);
				update(ad);
			}
		}
	}

	private BbsAdvertisingSpaceMng bbsAdvertisingSpaceMng;
	private BbsAdvertisingDao dao;
	@Autowired
	private BbsIncomeStatisticMng incomeStatisticMng;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private CmsConfigMng configMng;
	@Autowired
	private BbsUserAccountMng userAccountMng;

	@Autowired
	public void setBbsAdvertisingSpaceMng(
			BbsAdvertisingSpaceMng cmsAdvertisingSpaceMng) {
		this.bbsAdvertisingSpaceMng = cmsAdvertisingSpaceMng;
	}

	@Autowired
	public void setDao(BbsAdvertisingDao dao) {
		this.dao = dao;
	}

}