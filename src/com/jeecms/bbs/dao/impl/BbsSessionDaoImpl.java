package com.jeecms.bbs.dao.impl;

import java.util.Date;
import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsSessionDao;
import com.jeecms.bbs.entity.BbsSession;

@Repository
public class BbsSessionDaoImpl extends HibernateBaseDao<BbsSession, Long> implements BbsSessionDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}
	
	public List<BbsSession> getList(Integer userId,Integer count){
		String hql=" from BbsSession bean ";
		Finder f=Finder.create(hql);
		if(userId!=null){
			f.append(" where bean.user.id=:userId").setParam("userId", userId);
		}
		f.append(" order by bean.lastActivetime desc ");
		f.setMaxResults(count);
		f.setCacheable(true);
		return find(f);
	}
	
	public Integer total(boolean member){
		String hql="select count(*) from BbsSession bean ";
		if(member){
			hql+=" where bean.user is not null ";
		}else{
			hql+=" where bean.user is null ";
		}
		Query query= getSession().createQuery(hql);
		return ((Number) (query.iterate().next())).intValue();
	}
	
	public void freshCacheToDB(Ehcache cache){
		List<Long> keys = cache.getKeys();
		if (keys.size() <= 0) {
			return;
		}
		Element e;
		Date lastActiveTime;
		int i = 0;
		String hql = "update BbsSession bean"
				+ " set bean.lastActivetime=:lastActivetime"
				+ " where bean.id=:id";
		Query query = getSession().createQuery(hql);
		for (Long id : keys) {
			e = cache.get(id);
			if (e != null) {
				lastActiveTime = (Date) e.getObjectValue();
				if (lastActiveTime != null) {
					query.setParameter("lastActivetime", lastActiveTime);
					query.setParameter("id", id);
					i += query.executeUpdate();
				}
			}
		}
	}

	public BbsSession findById(Long id) {
		BbsSession entity = get(id);
		return entity;
	}
	
	public BbsSession findBySessionId(String sessionId){
		String hql="from BbsSession bean where bean.sessionId=:sessionId";
		Finder f=Finder.create(hql).setParam("sessionId", sessionId);
		List<BbsSession>li= find(f);
		if(li.size()>0){
			return li.get(0);
		}else{
			return null;
		}
	}

	public BbsSession save(BbsSession bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsSession deleteById(Long id) {
		BbsSession entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsSession> getEntityClass() {
		return BbsSession.class;
	}
}