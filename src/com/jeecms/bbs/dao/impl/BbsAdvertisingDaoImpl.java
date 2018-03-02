package com.jeecms.bbs.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsAdvertisingDao;
import com.jeecms.bbs.entity.BbsAdvertising;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsAdvertisingDaoImpl extends
		HibernateBaseDao<BbsAdvertising, Integer> implements BbsAdvertisingDao {
	public Pagination getPage(Integer siteId, Integer adspaceId,
			Boolean enabled, Integer queryChargeMode,String queryTitle,
			Integer ownerId,int pageNo, int pageSize) {
		Finder f = Finder.create("from BbsAdvertising bean where 1=1");
		if (siteId != null && adspaceId == null) {
			f.append(" and bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		} else if (adspaceId != null) {
			f.append(" and bean.adspace.id=:adspaceId");
			f.setParam("adspaceId", adspaceId);
		}
		if (enabled != null) {
			f.append(" and bean.enabled=:enabled");
			f.setParam("enabled", enabled);
		}
		if (queryChargeMode != null) {
			f.append(" and bean.chargeMode=:chargeMode");
			f.setParam("chargeMode", queryChargeMode);
		}
		if (StringUtils.isNotBlank(queryTitle)) {
			f.append(" and bean.name=:name");
			f.setParam("name", queryTitle);
		}
		if(ownerId!=null){
			f.append(" and bean.owner.id=:ownerId");
			f.setParam("ownerId", ownerId);
		}
		f.append(" order by bean.id desc");
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}
	

	@Override
	public List<BbsAdvertising> getApiList(String queryTitle, Integer orderBy, Integer queryChargeMode, Integer first,
			Integer count) {
		Finder f = Finder.create("from BbsAdvertising bean where 1=1");
		if (StringUtils.isNotBlank(queryTitle)) {
			f.append(" and bean.name=:name");
			f.setParam("name", queryTitle);
		}
		if (queryChargeMode!=null) {
			f.append(" and bean.chargeMode=:chargeMode");
			f.setParam("chargeMode", queryChargeMode);
		}
		if (first!=null) {
			f.setFirstResult(first);
		}
		if (count!=null) {
			f.setMaxResults(count);
		}
		f.setCacheable(true);
		return find(f);
	}

	@SuppressWarnings("unchecked")
	public List<BbsAdvertising> getList(Integer adspaceId, Boolean enabled
			,Integer ownerId,Integer count) {
		Finder f = Finder.create("from BbsAdvertising bean where 1=1");
		if (adspaceId != null) {
			f.append(" and bean.adspace.id=:adspaceId");
			f.setParam("adspaceId", adspaceId);
		}
		if (enabled != null) {
			f.append(" and bean.enabled=:enabled");
			f.setParam("enabled", enabled);
		}
		if(ownerId!=null){
			f.append(" and bean.owner.id=:ownerId");
			f.setParam("ownerId", ownerId);
		}
		f.setMaxResults(count);
		return find(f);
	}
	
	public Long findAdvertiseCount(Integer adspaceId, Boolean enabled,
			Integer ownerId,Date queryTimeBegin,Date queryTimeEnd){
		Finder f = Finder.create("select count(bean.id) from BbsAdvertising bean where 1=1");
		if (adspaceId != null) {
			f.append(" and bean.adspace.id=:adspaceId");
			f.setParam("adspaceId", adspaceId);
		}
		if (enabled != null) {
			f.append(" and bean.enabled=:enabled");
			f.setParam("enabled", enabled);
		}
		if(ownerId!=null){
			f.append(" and bean.owner.id=:ownerId");
			f.setParam("ownerId", ownerId);
		}
		if(queryTimeBegin!=null){
			f.append(" and bean.endTime>=:queryTimeBegin");
			f.setParam("queryTimeBegin", queryTimeBegin);
		}
		if(queryTimeEnd!=null){
			f.append(" and bean.endTime<=:queryTimeEnd");
			f.setParam("queryTimeEnd", queryTimeEnd);
		}
		Query query = f.createQuery(getSession());
		query.setCacheable(true).setMaxResults(1);
		return (Long) query.uniqueResult(); 
	}

	public BbsAdvertising findById(Integer id) {
		BbsAdvertising entity = get(id);
		return entity;
	}

	public BbsAdvertising save(BbsAdvertising bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsAdvertising deleteById(Integer id) {
		BbsAdvertising entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<BbsAdvertising> getEntityClass() {
		return BbsAdvertising.class;
	}
}