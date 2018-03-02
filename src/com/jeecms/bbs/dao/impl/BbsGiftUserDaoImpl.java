package com.jeecms.bbs.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsGiftUserDao;
import com.jeecms.bbs.entity.BbsGiftUser;

@Repository
public class BbsGiftUserDaoImpl extends HibernateBaseDao<BbsGiftUser, Integer> implements BbsGiftUserDao {
	public Pagination getPage(Integer giftId,Integer userId,int pageNo, int pageSize) {
		Finder f=getFinder(giftId, userId);
		return find(f, pageNo, pageSize);
	}
	
	public List<BbsGiftUser> getList(Integer giftId,Integer userId,
			Integer first, Integer count){
		Finder f=getFinder(giftId, userId);
		if(first!=null){
			f.setFirstResult(first);
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		return find(f);
	}
	
	private Finder getFinder(Integer giftId,Integer userId){
		Finder f=Finder.create(" from BbsGiftUser bean where 1=1");
		if(giftId!=null){
			f.append(" and bean.gift.id=:giftId").setParam("giftId", giftId);
		}
		if(userId!=null&&userId!=0){
			f.append(" and bean.user.id=:userId").setParam("userId", userId);
		}
		return f;
	}

	public BbsGiftUser findById(Integer id) {
		BbsGiftUser entity = get(id);
		return entity;
	}

	public BbsGiftUser save(BbsGiftUser bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsGiftUser deleteById(Integer id) {
		BbsGiftUser entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsGiftUser> getEntityClass() {
		return BbsGiftUser.class;
	}
}