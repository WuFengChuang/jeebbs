package com.jeecms.plug.live.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveRateDao;
import com.jeecms.plug.live.entity.BbsLiveRate;

@Repository
public class BbsLiveRateDaoImpl extends HibernateBaseDao<BbsLiveRate, Integer> implements BbsLiveRateDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	public BbsLiveRate findByNearLimitNum(Integer userNum){
		String hql="from BbsLiveRate bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(userNum!=null){
			f.append(" and bean.userNum>=:userNum").setParam("userNum", userNum);
		}
		f.append(" order by bean.userNum asc");
		List<BbsLiveRate>list=find(f);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public BbsLiveRate findById(Integer id) {
		BbsLiveRate entity = get(id);
		return entity;
	}

	public BbsLiveRate save(BbsLiveRate bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsLiveRate deleteById(Integer id) {
		BbsLiveRate entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsLiveRate> getEntityClass() {
		return BbsLiveRate.class;
	}
}