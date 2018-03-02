package com.jeecms.bbs.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsLimitDao;
import com.jeecms.bbs.entity.BbsLimit;

@Repository
public class BbsLimitDaoImpl extends HibernateBaseDao<BbsLimit, Integer> implements BbsLimitDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	public List<BbsLimit> getList(String ip,Integer userId){
		String hql=" from BbsLimit bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(StringUtils.isNotBlank(ip)){
			f.append(" and bean.ip=:ip").setParam("ip", ip);
		}
		if(userId!=null){
			f.append(" and bean.userId=:userId").setParam("userId", userId);
		}
		return find(f);
	}

	public BbsLimit findById(Integer id) {
		BbsLimit entity = get(id);
		return entity;
	}

	public BbsLimit save(BbsLimit bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsLimit deleteById(Integer id) {
		BbsLimit entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsLimit> getEntityClass() {
		return BbsLimit.class;
	}
}