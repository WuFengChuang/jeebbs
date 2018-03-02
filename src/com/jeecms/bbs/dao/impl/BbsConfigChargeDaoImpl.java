package com.jeecms.bbs.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsConfigChargeDao;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
@Repository
public class BbsConfigChargeDaoImpl extends HibernateBaseDao<BbsConfigCharge, Integer> implements BbsConfigChargeDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public BbsConfigCharge findById(Integer id) {
		BbsConfigCharge entity = get(id);
		return entity;
	}

	public BbsConfigCharge save(BbsConfigCharge bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsConfigCharge deleteById(Integer id) {
		BbsConfigCharge entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsConfigCharge> getEntityClass() {
		return BbsConfigCharge.class;
	}
}