package com.jeecms.bbs.manager.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsUserOnlineDao;
import com.jeecms.bbs.entity.BbsSession;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserOnline;
import com.jeecms.bbs.manager.BbsSessionMng;
import com.jeecms.bbs.manager.BbsUserOnlineMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.manager.CmsConfigMng;

@Service
@Transactional
public class BbsUserOnlineMngImpl implements BbsUserOnlineMng {

	@Transactional(readOnly = true)
	public List<BbsUserOnline> getList() {
		return dao.getList();
	}
	
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsUserOnline findById(Integer id) {
		BbsUserOnline entity = dao.findById(id);
		return entity;
	}

	public BbsUserOnline save(BbsUserOnline bean) {
		dao.save(bean);
		return bean;
	}
	
	public BbsUserOnline saveByUser(BbsUser user){
		BbsUserOnline online = new BbsUserOnline();
		online.setUser(user);
		online.initial();
		online=save(online);
		return online;
	}

	public BbsUserOnline update(BbsUserOnline bean) {
		Updater<BbsUserOnline> updater = new Updater<BbsUserOnline>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}
	
	public void clearCount(CmsConfig config){
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		last.setTime(config.getCountClearTime());
		int currYear = curr.get(Calendar.YEAR);
		int lastYear = last.get(Calendar.YEAR);
		int currWeek = curr.get(Calendar.WEEK_OF_YEAR);
		int lastWeek = last.get(Calendar.WEEK_OF_YEAR);
		int currMonth = curr.get(Calendar.MONTH);
		int lastMonth = last.get(Calendar.MONTH);
		int currDay = curr.get(Calendar.DAY_OF_YEAR);
		int lastDay = last.get(Calendar.DAY_OF_YEAR);
		if(currDay!=lastDay){
			cmsConfigMng.updateCountClearTime(curr.getTime());
			dao.clearCount(currWeek != lastWeek, currMonth != lastMonth, currYear!=lastYear);
		}
	}
	
	public void updateUserOnlineTime(BbsSession userSession,Date clearDate){
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		last.setTime(clearDate);
		Double timegap=(curr.getTime().getTime()-userSession.getLastActivetime().getTime())/(1000.0*60*60);
		int currDay = curr.get(Calendar.DAY_OF_YEAR);
		int lastDay = last.get(Calendar.DAY_OF_YEAR);
		int currYear = curr.get(Calendar.YEAR);
		int lastYear = last.get(Calendar.YEAR);
		int currWeek = curr.get(Calendar.WEEK_OF_YEAR);
		int lastWeek = last.get(Calendar.WEEK_OF_YEAR);
		int currMonth = curr.get(Calendar.MONTH);
		int lastMonth = last.get(Calendar.MONTH);
		BbsUser user=userSession.getUser();
		BbsUserOnline useronline;
		if(user!=null){
			useronline=user.getUserOnline();
			if(useronline!=null){
				useronline.setOnlineLatest(timegap);
				if(currDay!=lastDay){
					useronline.setOnlineDay(timegap);
				}else{
					useronline.setOnlineDay(useronline.getOnlineDay()+timegap);
				}
				if(currWeek!=lastWeek){
					useronline.setOnlineWeek(timegap);
				}else{
					useronline.setOnlineWeek(useronline.getOnlineWeek()+timegap);
				}
				if(currMonth!=lastMonth){
					useronline.setOnlineMonth(timegap);
				}else{
					useronline.setOnlineMonth(useronline.getOnlineMonth()+timegap);
				}
				if(currYear!=lastYear){
					useronline.setOnlineYear(timegap);
				}else{
					useronline.setOnlineYear(useronline.getOnlineYear()+timegap);
				}
				useronline.setOnlineTotal(useronline.getOnlineTotal()+timegap);
				update(useronline);
			}
		}
	}

	public BbsUserOnline deleteById(Integer id) {
		BbsUserOnline bean = dao.deleteById(id);
		return bean;
	}

	public BbsUserOnline[] deleteByIds(Integer[] ids) {
		BbsUserOnline[] beans = new BbsUserOnline[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsUserOnlineDao dao;
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private BbsSessionMng bbsSessionMng;

	@Autowired
	public void setDao(BbsUserOnlineDao dao) {
		this.dao = dao;
	}

}
