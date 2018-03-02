package com.jeecms.plug.live.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveApplyDao;
import com.jeecms.plug.live.entity.BbsLiveApply;

@Repository
public class BbsLiveApplyDaoImpl extends HibernateBaseDao<BbsLiveApply, Integer> implements BbsLiveApplyDao {
	public Pagination getPage(String mobile,Short status,
			Integer applyUserId,int pageNo, int pageSize) {
		Finder f=createFinder(mobile, status, applyUserId);
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}
	
	public List<BbsLiveApply> getList(String mobile,Short status,
			Integer applyUserId,Integer first, Integer count){
		Finder f=createFinder(mobile, status, applyUserId);
		f.setCacheable(true);
		return find(f);
	}
	
	public Long findLiveApplyCount(Short status,Integer applyUserId){
		Finder f=Finder.create("select count(bean.id) from BbsLiveApply bean where 1=1 ");
		if(status!=null){
			f.append(" and bean.status=:status").setParam("status", status);
		}
		if(applyUserId!=null){
			f.append(" and bean.applyUser.id=:applyUserId").setParam("applyUserId", applyUserId);
		}
		Query query = f.createQuery(getSession());
		query.setCacheable(true).setMaxResults(1);
		return (Long) query.uniqueResult(); 
	}
	
	private Finder createFinder(String mobile,Short status,Integer applyUserId){
		Finder f=Finder.create(" from BbsLiveApply bean where 1=1 ");
		if(StringUtils.isNotBlank(mobile)){
			f.append(" and bean.mobile like:mobile").setParam("mobile", "%"+mobile+"%");
		}
		if(status!=null){
			f.append(" and bean.status=:status").setParam("status", status);
		}
		if(applyUserId!=null){
			f.append(" and bean.applyUser.id=:applyUserId").setParam("applyUserId", applyUserId);
		}
		return f;
	}

	public BbsLiveApply findById(Integer id) {
		BbsLiveApply entity = get(id);
		return entity;
	}

	public BbsLiveApply save(BbsLiveApply bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsLiveApply deleteById(Integer id) {
		BbsLiveApply entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsLiveApply> getEntityClass() {
		return BbsLiveApply.class;
	}
}