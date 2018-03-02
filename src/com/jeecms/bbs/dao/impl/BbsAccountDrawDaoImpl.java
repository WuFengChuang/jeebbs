package com.jeecms.bbs.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsAccountDrawDao;
import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;

@Repository
public class BbsAccountDrawDaoImpl extends HibernateBaseDao<BbsAccountDraw, Integer> implements BbsAccountDrawDao {
	public Pagination getPage(Integer userId,Short applyStatus,
			Date applyTimeBegin,Date applyTimeEnd,
			Short applyType,int pageNo, int pageSize) {
		Finder f=createFinder(userId, applyStatus, 
				applyTimeBegin, applyTimeEnd,applyType);
		return find(f, pageNo, pageSize);
	}
	
	public List<BbsAccountDraw> getList(Integer userId,Short applyStatus,
			Date applyTimeBegin,Date applyTimeEnd,
			Short applyType,Integer first,Integer count){
		Finder f=createFinder(userId, applyStatus, 
				applyTimeBegin, applyTimeEnd,applyType);
		if(first!=null){
			f.setFirstResult(first);
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		return find(f);
	}
	
	public List<BbsAccountDraw> getList(Integer userId,Short[] status,
			Short applyType,Integer count){
		String hql="select bean  from BbsAccountDraw bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(userId!=null){
			f.append(" and bean.drawUser.id=:userId").setParam("userId", userId);
		}
		if(status!=null){
			f.append(" and bean.applyStatus in(:status)").setParamList("status", status);
		}
		if(applyType!=null){
			f.append(" and bean.applyType =:applyType").setParam("applyType", applyType);
		}
		f.setCacheable(true);
		f.setMaxResults(count);
		return find(f);
	}
	
	public Long findAccountDrawCount(Integer userId,Short applyStatus,Short applyType){
		String hql="select count(bean.id)  from BbsAccountDraw bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(userId!=null){
			f.append(" and bean.drawUser.id=:userId").setParam("userId", userId);
		}
		if(applyStatus!=null){
			f.append(" and bean.applyStatus =:status)").setParam("status", applyStatus);
		}
		if(applyType!=null){
			f.append(" and bean.applyType =:applyType").setParam("applyType", applyType);
		}
		Query query = f.createQuery(getSession());
		query.setCacheable(true).setMaxResults(1);
		return (Long) query.uniqueResult(); 
	}

	public BbsAccountDraw findById(Integer id) {
		BbsAccountDraw entity = get(id);
		return entity;
	}

	public BbsAccountDraw save(BbsAccountDraw bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsAccountDraw deleteById(Integer id) {
		BbsAccountDraw entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	private Finder createFinder(Integer userId,Short applyStatus,
			Date applyTimeBegin,Date applyTimeEnd,Short applyType){
		String hql="select bean  from BbsAccountDraw bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(userId!=null){
			if(userId!=0){
				f.append(" and bean.drawUser.id=:userId").setParam("userId", userId);
			}else{
				f.append(" and 1!=1");
			}
		}
		if(applyStatus!=null&&applyStatus!=-1){
			f.append(" and bean.applyStatus=:applyStatus").setParam("applyStatus", applyStatus);
		}
		if(applyTimeBegin!=null){
			f.append(" and bean.applyTime>=:applyTimeBegin")
			.setParam("applyTimeBegin", DateUtils.getStartDate(applyTimeBegin));
		}
		if(applyTimeEnd!=null){
			f.append(" and bean.applyTime<=:applyTimeEnd")
			.setParam("applyTimeEnd", DateUtils.getFinallyDate(applyTimeEnd));
		}
		if(applyType!=null){
			f.append(" and bean.applyType =:applyType").setParam("applyType", applyType);
		}
		f.setCacheable(true);
		return f;
	}
	
	@Override
	protected Class<BbsAccountDraw> getEntityClass() {
		return BbsAccountDraw.class;
	}
}