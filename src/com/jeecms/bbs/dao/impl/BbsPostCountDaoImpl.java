package com.jeecms.bbs.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsPostCountDao;
import com.jeecms.bbs.entity.BbsPostCount;

@Repository
public class BbsPostCountDaoImpl extends HibernateBaseDao<BbsPostCount, Integer> implements BbsPostCountDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public BbsPostCount findById(Integer id) {
		BbsPostCount entity = get(id);
		return entity;
	}

	public BbsPostCount save(BbsPostCount bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsPostCount deleteById(Integer id) {
		BbsPostCount entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsPostCount> getEntityClass() {
		return BbsPostCount.class;
	}
}