package com.jeecms.plug.live.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveUserDao;
import com.jeecms.plug.live.entity.BbsLiveUser;

@Repository
public class BbsLiveUserDaoImpl extends HibernateBaseDao<BbsLiveUser, Integer> implements BbsLiveUserDao {
	public Pagination getPage(Long orderId,Integer userId,Integer liveId,int pageNo, int pageSize) {
		Finder f=Finder.create("select bean from BbsLiveUser bean where 1=1 ");
		if(orderId!=null){
			f.append(" and bean.order.id=:orderId").setParam("orderId", orderId);
		}
		if(userId!=null){
			f.append(" and bean.joinUser.id=:userId").setParam("userId", userId);
		}
		if(liveId!=null){
			f.append(" and bean.live.id=:liveId").setParam("liveId", liveId);
		}
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}
	
	public Pagination getLivePage(Integer userId,int pageNo, int pageSize){
		Finder f=Finder.create("select bean.live from BbsLiveUser bean where 1=1 ");
		if(userId!=null){
			f.append(" and bean.joinUser.id=:userId").setParam("userId", userId);
		}
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}
	
	public List<BbsLiveUser> getList(Integer userId,Integer liveId,
			Integer first,Integer count){
		Finder f=Finder.create("select bean from BbsLiveUser bean where 1=1 ");
		if(userId!=null){
			f.append(" and bean.joinUser.id=:userId").setParam("userId", userId);
		}
		if(liveId!=null){
			f.append(" and bean.live.id=:liveId").setParam("liveId", liveId);
		}
		if(first!=null){
			f.setFirstResult(first);
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		f.setCacheable(true);
		return find(f);
	}

	public BbsLiveUser findById(Integer id) {
		BbsLiveUser entity = get(id);
		return entity;
	}

	public BbsLiveUser save(BbsLiveUser bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsLiveUser deleteById(Integer id) {
		BbsLiveUser entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsLiveUser> getEntityClass() {
		return BbsLiveUser.class;
	}
}