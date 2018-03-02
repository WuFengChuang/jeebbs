package com.jeecms.bbs.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.Finder;
import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsForumCountDao;
import com.jeecms.bbs.entity.BbsForumCount;

@Repository
public class BbsForumCountDaoImpl extends HibernateBaseDao<BbsForumCount, Integer> implements BbsForumCountDao {
	public Pagination getPage(Integer forumId,Date begin,Date end,
			int pageNo, int pageSize) {
		String hql="select bean from BbsForumCount bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(forumId!=null){
			f.append(" and bean.forum.id=:forumId").setParam("forumId", forumId);
		}
		if(begin!=null){
			f.append(" and bean.countDate>=:begin").setParam("begin", begin);
		}
		if(end!=null){
			f.append(" and bean.countDate<=:end").setParam("end", end);
		}
		f.append(" order by bean.id desc");
		f.setCacheable(true);
		return find(f, pageNo, pageSize);
	}
	
	public List<BbsForumCount> getList(Integer forumId,Date begin,Date end,Integer first,
			Integer count){
		String hql="select bean from BbsForumCount bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(forumId!=null){
			f.append(" and bean.forum.id=:forumId").setParam("forumId", forumId);
		}
		if(begin!=null){
			f.append(" and bean.countDate>=:begin").setParam("begin", begin);
		}
		if(end!=null){
			f.append(" and bean.countDate<=:end").setParam("end", end);
		}
		f.append(" order by bean.id desc");
		if (first!=null) {
			f.setFirstResult(first);
		}
		if (count!=null) {
			f.setMaxResults(count);
		}
		f.setCacheable(true);
		
		return find(f);
	}
	
	public List<Object[]> getList(Date begin,Date end,int count){
		String hql="select bean.forum.id,bean.forum.title,sum(bean.topicCount),"
				+ "sum(bean.postCount),sum(bean.visitCount)"
				+ " from BbsForumCount bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(begin!=null){
			f.append(" and bean.countDate>=:begin").setParam("begin", begin);
		}
		if(end!=null){
			f.append(" and bean.countDate<=:end").setParam("end", end);
		}
		f.append(" group by bean.forum.id,bean.forum.title");
		f.setMaxResults(count);
		f.setCacheable(true);
		return find(f);
	}

	public BbsForumCount findById(Integer id) {
		BbsForumCount entity = get(id);
		return entity;
	}
	
	public BbsForumCount findByForum(Integer forumId,Date date){
		String hql="select bean from BbsForumCount bean where 1=1 ";
		Finder f=Finder.create(hql);
		if(forumId!=null){
			f.append(" and bean.forum.id=:forumId").setParam("forumId", forumId);
		}
		if(date!=null){
			f.append(" and bean.countDate=:date").setParam("date", date);
		}
		List<BbsForumCount>list=find(f);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public BbsForumCount save(BbsForumCount bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsForumCount deleteById(Integer id) {
		BbsForumCount entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsForumCount> getEntityClass() {
		return BbsForumCount.class;
	}

}