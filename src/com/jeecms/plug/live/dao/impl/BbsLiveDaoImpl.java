package com.jeecms.plug.live.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveDao;
import com.jeecms.plug.live.entity.BbsLive;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

@Repository
public class BbsLiveDaoImpl extends HibernateBaseDao<BbsLive, Integer> implements BbsLiveDao {
	public Pagination getPage(Integer cid,String title,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd,
			Integer orderBy,int pageNo, int pageSize) {
		Finder f=createFinder(cid, title, hostUserId, status,
				timeBegin, timeEnd,liveEndTimeBegin,liveEndTimeEnd, orderBy);
		return find(f, pageNo, pageSize);
	}
	
	public List<BbsLive> getList(Integer cid,String title,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd,
			Integer orderBy,Integer first, Integer count){
		Finder f=createFinder(cid, title, hostUserId, status,
				timeBegin, timeEnd,liveEndTimeBegin,liveEndTimeEnd, orderBy);
		if(first!=null){
			f.setFirstResult(first);
		}
		if(count!=null){
			f.setMaxResults(count);
		}
		return find(f);
	}
	
	public Long findLiveCount(Integer cid,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd){
		Finder f=Finder.create("select count(bean.id) from BbsLive bean ");
		if(cid!=null){
			f.append(" join bean.chapter chapter,BbsLiveChapter parent");
			f.append(" where (chapter.lft between parent.lft and parent.rgt");
			f.append(" and parent.id=:parentId)");
			f.setParam("parentId", cid);
		}else{
			f.append(" where 1=1 ");
		}
		if(hostUserId!=null){
			f.append(" and bean.user.id=:hostUserId").setParam("hostUserId", hostUserId);
		}
		if(status!=null){
			f.append(" and bean.checkStatus=:status").setParam("status", status);
		}
		if(timeBegin!=null){
			f.append(" and bean.beginTime>=:timeBegin").setParam("timeBegin", timeBegin);
		}
		if(timeEnd!=null){
			f.append(" and bean.beginTime<=:timeEnd").setParam("timeEnd", timeEnd);
		}
		if(liveEndTimeBegin!=null){
			f.append(" and bean.endTime>=:endTimeBegin").setParam("endTimeBegin", liveEndTimeBegin);
		}
		if(liveEndTimeEnd!=null){
			f.append(" and bean.endTime<=:endTimeEnd").setParam("endTimeEnd", liveEndTimeEnd);
		}
		Query query = f.createQuery(getSession());
		query.setCacheable(true).setMaxResults(1);
		return (Long) query.uniqueResult(); 
	}

	public BbsLive findById(Integer id) {
		BbsLive entity = get(id);
		return entity;
	}

	public BbsLive save(BbsLive bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsLive deleteById(Integer id) {
		BbsLive entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	public int countByChapterId(int chapterId) {
		String hql = "select count(*) from BbsLive bean"
				+ " join bean.chapter chapter,BbsLiveChapter parent"
				+ " where chapter.lft between parent.lft and parent.rgt"
				+ " and parent.id=:parentId";
		Query query = getSession().createQuery(hql);
		query.setParameter("parentId", chapterId);
		return ((Number) (query.iterate().next())).intValue();
	}
	
	public void freshCacheToDB(Ehcache cache){
		List<Integer> keys = cache.getKeys();
		if (keys.size() > 0) {
			Element e;
			Integer count;
			String hql = "update BbsLive bean"
					+ " set bean.inliveUserNum=bean.inliveUserNum+:count" + " where bean.id=:id";
			Query query = getSession().createQuery(hql);
			for (Integer id : keys) {
				e = cache.get(id);
				if (e != null) {
					count = (Integer) e.getObjectValue();
					if (count != null) {
						query.setParameter("count", count);
						query.setParameter("id", id);
						query.executeUpdate();
					}
				}
			}
		}	
	}
	
	public void clearLiveUserNum(){
		String hql = "update BbsLive bean set bean.inliveUserNum=0";
		Query query = getSession().createQuery(hql);
		query.executeUpdate();
	}
	
	private Finder createFinder(Integer cid,String title,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd,Integer orderBy){
		Finder f=Finder.create("select bean from BbsLive bean ");
		if(cid!=null){
			f.append(" join bean.chapter chapter,BbsLiveChapter parent");
			f.append(" where (chapter.lft between parent.lft and parent.rgt");
			f.append(" and parent.id=:parentId)");
			f.setParam("parentId", cid);
		}else{
			f.append(" where 1=1 ");
		}
		if(StringUtils.isNotBlank(title)){
			f.append(" and bean.title like:title").setParam("title", "%"+title+"%");
		}
		if(hostUserId!=null){
			f.append(" and bean.user.id=:hostUserId").setParam("hostUserId", hostUserId);
		}
		if(status!=null){
			f.append(" and bean.checkStatus=:status").setParam("status", status);
		}
		if(timeBegin!=null){
			f.append(" and bean.beginTime>=:timeBegin").setParam("timeBegin", timeBegin);
		}
		if(timeEnd!=null){
			f.append(" and bean.beginTime<=:timeEnd").setParam("timeEnd", timeEnd);
		}
		if(liveEndTimeBegin!=null){
			f.append(" and bean.endTime>=:endTimeBegin").setParam("endTimeBegin", liveEndTimeBegin);
		}
		if(liveEndTimeEnd!=null){
			f.append(" and bean.endTime<=:endTimeEnd").setParam("endTimeEnd", liveEndTimeEnd);
		}
		if(orderBy!=null){
			if(orderBy==1){
				f.append(" order by bean.beginTime desc");
			}else if(orderBy==2){
				f.append(" order by bean.totalAmount desc");
			}else if(orderBy==3){
				f.append(" order by bean.id desc");
			}else if(orderBy==4){
				f.append(" order by bean.joinUserNum desc");
			}else if(orderBy==5){
				f.append(" order by bean.limitUserNum desc");
			}else if(orderBy==6){
				f.append(" order by bean.beginTime asc");
			}else if(orderBy==7){
				f.append(" order by bean.totalAmount asc");
			}else if(orderBy==8){
				f.append(" order by bean.id asc");
			}else if(orderBy==9){
				f.append(" order by bean.joinUserNum asc");
			}else if(orderBy==10){
				f.append(" order by bean.limitUserNum asc");
			}
		}else{
			f.append(" order by bean.beginTime desc");
		}
		return f;
	}
	
	@Override
	protected Class<BbsLive> getEntityClass() {
		return BbsLive.class;
	}
}