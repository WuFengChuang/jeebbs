package com.jeecms.bbs.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsTopicCountDao;
import com.jeecms.bbs.entity.BbsTopicCount;

@Repository
public class BbsTopicCountDaoImpl extends HibernateBaseDao<BbsTopicCount, Integer> implements BbsTopicCountDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public BbsTopicCount findById(Integer id) {
		BbsTopicCount entity = get(id);
		return entity;
	}

	public BbsTopicCount save(BbsTopicCount bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsTopicCount deleteById(Integer id) {
		BbsTopicCount entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsTopicCount> getEntityClass() {
		return BbsTopicCount.class;
	}
}