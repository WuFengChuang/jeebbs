package com.jeecms.bbs.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsGiftDao;
import com.jeecms.bbs.entity.BbsGift;

@Repository
public class BbsGiftDaoImpl extends HibernateBaseDao<BbsGift, Integer> implements BbsGiftDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	public List<BbsGift> getList(Boolean disabled,Integer first, Integer count){
		Finder f=Finder.create(" from BbsGift bean where 1=1 ");
		if(disabled!=null){
			f.append(" and bean.disabled=:disabled").setParam("disabled", disabled);
		}
		f.append(" order by bean.priority asc");
		if(first!=null){
			f.setFirstResult(first);
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		f.setCacheable(true);
		return find(f);
	}

	public BbsGift findById(Integer id) {
		BbsGift entity = get(id);
		return entity;
	}

	public BbsGift save(BbsGift bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsGift deleteById(Integer id) {
		BbsGift entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsGift> getEntityClass() {
		return BbsGift.class;
	}
}