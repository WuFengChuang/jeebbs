package com.jeecms.plug.live.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveChargeDao;
import com.jeecms.plug.live.entity.BbsLiveCharge;

@Repository
public class BbsLiveChargeDaoImpl extends HibernateBaseDao<BbsLiveCharge, Integer> implements BbsLiveChargeDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public BbsLiveCharge findById(Integer id) {
		BbsLiveCharge entity = get(id);
		return entity;
	}

	public BbsLiveCharge save(BbsLiveCharge bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsLiveCharge deleteById(Integer id) {
		BbsLiveCharge entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsLiveCharge> getEntityClass() {
		return BbsLiveCharge.class;
	}
}