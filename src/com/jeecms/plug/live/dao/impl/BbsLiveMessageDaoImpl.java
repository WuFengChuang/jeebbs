package com.jeecms.plug.live.dao.impl;

import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveMessageDao;
import com.jeecms.plug.live.entity.BbsLiveMessage;

@Repository
public class BbsLiveMessageDaoImpl extends HibernateBaseDao<BbsLiveMessage, Long> implements BbsLiveMessageDao {
	public Pagination getPage(Integer liveId,int pageNo, int pageSize) {
		String hql="select bean from BbsLiveMessage bean where 1=1 ";
		Finder finder=Finder.create(hql);
		if(liveId!=null){
			finder.append(" and bean.live.id=:liveId").setParam("liveId", liveId);
		}
		return find(finder, pageNo, pageSize);
	}

	public BbsLiveMessage findById(Long id) {
		BbsLiveMessage entity = get(id);
		return entity;
	}

	public BbsLiveMessage save(BbsLiveMessage bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsLiveMessage deleteById(Long id) {
		BbsLiveMessage entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsLiveMessage> getEntityClass() {
		return BbsLiveMessage.class;
	}
}