package com.jeecms.bbs.manager.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsAccountDrawDao;
import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsAccountDrawMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsAccountDrawMngImpl implements BbsAccountDrawMng {
	
	public BbsAccountDraw draw(BbsUser user,Double amount,
			String applyAccount,Short applyType){
		BbsAccountDraw apply=new BbsAccountDraw();
		apply.setApplyAccount(applyAccount);
		apply.setApplyAmount(amount);
		apply.setApplyStatus(BbsAccountDraw.CHECKING);
		apply.setApplyTime(Calendar.getInstance().getTime());
		apply.setApplyType(applyType);
		apply.setDrawUser(user);
		return save(apply);
	}
	
	public Double getAppliedSum(Integer userId,Short applyType){
		Short[]status={0,1};
		Double applyAmoutTotal=0d;
		List<BbsAccountDraw>list=dao.getList(userId, status,applyType, 1000);
		for(BbsAccountDraw d:list){
			applyAmoutTotal+=d.getApplyAmount();
		}
		return applyAmoutTotal;
	}
	
	@Transactional(readOnly = true)
	public Pagination getPage(Integer userId,Short applyStatus,
			Date applyTimeBegin,Date applyTimeEnd
			,Short applyType,int pageNo, int pageSize) {
		Pagination page = dao.getPage(userId,applyStatus
				,applyTimeBegin,applyTimeEnd,applyType,pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsAccountDraw> getList(Integer userId,Short applyStatus,
			Date applyTimeBegin,Date applyTimeEnd
			,Short applyType,Integer first,Integer count){
		return dao.getList(userId, applyStatus, applyTimeBegin, 
				applyTimeEnd,applyType, first, count);
	}
	
	@Transactional(readOnly = true)
	public Long findAccountDrawCount(Integer userId,Short applyStatus,Short applyType){
		return dao.findAccountDrawCount(userId,applyStatus,applyType);
	}

	@Transactional(readOnly = true)
	public BbsAccountDraw findById(Integer id) {
		BbsAccountDraw entity = dao.findById(id);
		return entity;
	}

	public BbsAccountDraw save(BbsAccountDraw bean) {
		dao.save(bean);
		return bean;
	}

	public BbsAccountDraw update(BbsAccountDraw bean) {
		Updater<BbsAccountDraw> updater = new Updater<BbsAccountDraw>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public BbsAccountDraw deleteById(Integer id) {
		BbsAccountDraw bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsAccountDraw[] deleteByIds(Integer[] ids) {
		BbsAccountDraw[] beans = new BbsAccountDraw[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsAccountDrawDao dao;

	@Autowired
	public void setDao(BbsAccountDrawDao dao) {
		this.dao = dao;
	}
}