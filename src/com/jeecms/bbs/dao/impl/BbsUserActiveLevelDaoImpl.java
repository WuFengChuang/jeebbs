package com.jeecms.bbs.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsUserActiveLevelDao;
import com.jeecms.bbs.entity.BbsUserActiveLevel;

@Repository
public class BbsUserActiveLevelDaoImpl extends HibernateBaseDao<BbsUserActiveLevel, Integer> implements BbsUserActiveLevelDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	@Override
	public List<BbsUserActiveLevel> getApiList(Integer first, Integer count) {
		Finder f=Finder.create("from BbsUserActiveLevel bean order by bean.requiredHour asc");
		if (first!=null) {
			f.setFirstResult(first);
		}
		if (count!=null) {
			f.setMaxResults(count);
		}
		f.setCacheable(true);
		return find(f);
	}
	
	public List<BbsUserActiveLevel> getList(Integer count){
		Finder f=Finder.create("from BbsUserActiveLevel bean order by bean.requiredHour asc");
		if(count==null){
			count=1000;
		}
		f.setMaxResults(count);
		f.setCacheable(true);
		return find(f);
	}
	
	

	public BbsUserActiveLevel findById(Integer id) {
		BbsUserActiveLevel entity = get(id);
		return entity;
	}

	public BbsUserActiveLevel save(BbsUserActiveLevel bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsUserActiveLevel deleteById(Integer id) {
		BbsUserActiveLevel entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsUserActiveLevel> getEntityClass() {
		return BbsUserActiveLevel.class;
	}

}