package com.jeecms.bbs.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsTopicTypeSubscribeDao;
import com.jeecms.bbs.entity.BbsTopicTypeSubscribe;

@Repository
public class BbsTopicTypeSubscribeDaoImpl extends HibernateBaseDao<BbsTopicTypeSubscribe, Integer> implements BbsTopicTypeSubscribeDao {
	public Pagination getPage(Integer userId,int pageNo, int pageSize) {
		String hql="from BbsTopicTypeSubscribe bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(userId!=null){
			f.append(" and bean.user.id=:userId").setParam("userId", userId);
		}
		Pagination page = find(f, pageNo, pageSize);
		return page;
	}

	public BbsTopicTypeSubscribe findById(Integer id) {
		BbsTopicTypeSubscribe entity = get(id);
		return entity;
	}
	
	public BbsTopicTypeSubscribe find(Integer typeId,Integer userId){
		List<BbsTopicTypeSubscribe> li=getList(typeId, userId);
		if(li!=null&&li.size()>0){
			return li.get(0);
		}else{
			return null;
		}
	}
	
	public List<BbsTopicTypeSubscribe> getList(Integer typeId,Integer userId){
		String hql="from BbsTopicTypeSubscribe bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(typeId!=null){
			f.append(" and bean.type.id=:typeId").setParam("typeId", typeId);
		}
		if(userId!=null){
			f.append(" and bean.user.id=:userId").setParam("userId", userId);
		}
		return find(f);
	}

	public BbsTopicTypeSubscribe save(BbsTopicTypeSubscribe bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsTopicTypeSubscribe deleteById(Integer id) {
		BbsTopicTypeSubscribe entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsTopicTypeSubscribe> getEntityClass() {
		return BbsTopicTypeSubscribe.class;
	}
}