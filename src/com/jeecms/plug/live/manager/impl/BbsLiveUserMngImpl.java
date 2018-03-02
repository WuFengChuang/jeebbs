package com.jeecms.plug.live.manager.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveUserDao;
import com.jeecms.plug.live.entity.BbsLiveUser;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.manager.BbsLiveUserMng;

@Service
@Transactional
public class BbsLiveUserMngImpl implements BbsLiveUserMng {
	@Transactional(readOnly = true)
	public Pagination getPage(Long orderId,Integer userId,Integer liveId,int pageNo, int pageSize) {
		Pagination page = dao.getPage(orderId,userId,liveId,pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public Pagination getLivePage(Integer userId,int pageNo, int pageSize){
		Pagination page = dao.getLivePage(userId,pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public boolean hasBuyLive(Integer userId,Integer liveId){
		if(userId!=null&&liveId!=null){
			List<BbsLiveUser>list=dao.getList(userId, liveId, 0, 1);
			if(list!=null&&list.size()>0){
				return true;
			}
		}
		return false;
	}
	
	public boolean saveUserLiveTicket(BbsOrder order,Integer userId,Integer liveId){
		if(userId!=null&&liveId!=null){
			boolean hasBuyTicket=hasBuyLive(userId, liveId);
			if(hasBuyTicket){
				return false;
			}else{
				BbsLiveUser ticket=new BbsLiveUser();
				ticket.setBuyTime(Calendar.getInstance().getTime());
				ticket.setJoinUser(userMng.findById(userId));
				ticket.setLive(liveMng.findById(liveId));
				ticket.setOrder(order);
				save(ticket);
				if(order.getLiveUsedNum()==null){
					order.setLiveUsedNum(1);
				}else{
					order.setLiveUsedNum(order.getLiveUsedNum()+1);
				}
				orderMng.update(order);
				return true;
			}
		}
		return  false;
	}

	@Transactional(readOnly = true)
	public BbsLiveUser findById(Integer id) {
		BbsLiveUser entity = dao.findById(id);
		return entity;
	}

	public BbsLiveUser save(BbsLiveUser bean) {
		dao.save(bean);
		return bean;
	}

	public BbsLiveUser update(BbsLiveUser bean) {
		Updater<BbsLiveUser> updater = new Updater<BbsLiveUser>(bean);
		BbsLiveUser entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsLiveUser deleteById(Integer id) {
		BbsLiveUser bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsLiveUser[] deleteByIds(Integer[] ids) {
		BbsLiveUser[] beans = new BbsLiveUser[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsLiveUserDao dao;
	@Autowired
	private BbsUserMng userMng;
	@Autowired
	private BbsLiveMng liveMng;
	@Autowired
	private BbsOrderMng orderMng;

	@Autowired
	public void setDao(BbsLiveUserDao dao) {
		this.dao = dao;
	}
}