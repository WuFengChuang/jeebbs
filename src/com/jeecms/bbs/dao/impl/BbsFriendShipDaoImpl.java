package com.jeecms.bbs.dao.impl;

import static com.jeecms.bbs.entity.BbsFriendShip.ACCEPT;
import static com.jeecms.bbs.entity.BbsFriendShip.APPLYING;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.bbs.dao.BbsFriendShipDao;
import com.jeecms.bbs.entity.BbsFriendShip;
import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;

@Repository
public class BbsFriendShipDaoImpl extends
		HibernateBaseDao<BbsFriendShip, Integer> implements BbsFriendShipDao {
	public BbsFriendShip findById(Integer id) {
		BbsFriendShip entity = get(id);
		return entity;
	}

	public BbsFriendShip save(BbsFriendShip bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsFriendShip deleteById(Integer id) {
		BbsFriendShip entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	public BbsFriendShip getFriendShip(Integer userId, Integer friendId) {
		String hql = "from BbsFriendShip bean where (bean.user.id=:userId and bean.friend.id=:friendId) "
											  + "or (bean.user.id=:friendId and bean.friend.id=:userId)";
		Finder f = Finder.create(hql).setParam("userId", userId).setParam("friendId", friendId);
		f.setMaxResults(1);
		Query query = f.createQuery(getSession());
		return (BbsFriendShip) query.uniqueResult();
	}
	
	public List<BbsFriendShip> getListByUserId(Integer userId,Integer first,Integer count) {
		String hql = "from BbsFriendShip bean where bean.user.id=:userId and bean.status="+ACCEPT;
		Finder f = Finder.create(hql).setParam("userId", userId);
		if(first!=null){
			f.setFirstResult(first);
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		return find(f);
	}
	
	public Pagination getPageByUserId(Integer userId, Integer pageNo, Integer pageSize) {
		String hql = "from BbsFriendShip bean where bean.user.id=:userId and bean.status="+ACCEPT;
		Finder f = Finder.create(hql).setParam("userId", userId);
		return find(f, pageNo, pageSize);
	}
	
	public Pagination getApplyByUserId(Integer userId, Integer pageNo, Integer pageSize) {
		String hql = "from BbsFriendShip bean where bean.friend.id=:userId and bean.status="+APPLYING;
		Finder f = Finder.create(hql).setParam("userId", userId);
		return find(f, pageNo, pageSize);
	}

	
	public List<BbsFriendShip> getApplyList(Integer userId,Integer first,Integer count) {
		String hql = "from BbsFriendShip bean where bean.friend.id=:userId and bean.status="+APPLYING;
		Finder f = Finder.create(hql).setParam("userId", userId);
		if(first!=null){
			f.setFirstResult(first);
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		return find(f);
	}

	
	
	@Override
	protected Class<BbsFriendShip> getEntityClass() {
		return BbsFriendShip.class;
	}
}