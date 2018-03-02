package com.jeecms.bbs.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsAdvertisingSpaceDao;
import com.jeecms.bbs.entity.BbsAdvertisingSpace;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;

@Repository
public class BbsAdvertisingSpaceDaoImpl extends
		HibernateBaseDao<BbsAdvertisingSpace, Integer> implements
		BbsAdvertisingSpaceDao {
	@SuppressWarnings("unchecked")
	public List<BbsAdvertisingSpace> getList(Integer siteId) {
		Finder f = Finder.create("from BbsAdvertisingSpace bean");
		if (siteId != null) {
			f.append(" where bean.site.id=:siteId");
			f.setParam("siteId", siteId);
		}
		return find(f);
	}

	public BbsAdvertisingSpace findById(Integer id) {
		BbsAdvertisingSpace entity = get(id);
		return entity;
	}

	public BbsAdvertisingSpace save(BbsAdvertisingSpace bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsAdvertisingSpace deleteById(Integer id) {
		BbsAdvertisingSpace entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}

	@Override
	protected Class<BbsAdvertisingSpace> getEntityClass() {
		return BbsAdvertisingSpace.class;
	}
}