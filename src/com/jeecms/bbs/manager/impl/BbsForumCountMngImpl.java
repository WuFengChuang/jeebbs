package com.jeecms.bbs.manager.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.bbs.dao.BbsForumCountDao;
import com.jeecms.bbs.entity.BbsForumCount;
import com.jeecms.bbs.manager.BbsForumCountMng;
import com.jeecms.bbs.manager.BbsForumMng;

@Service
@Transactional
public class BbsForumCountMngImpl implements BbsForumCountMng {
	@Transactional(readOnly = true)
	public Pagination getPage(Integer forumId,Date begin,Date end,
			int pageNo, int pageSize) {
		Pagination page = dao.getPage(forumId,begin,end,pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsForumCount> getList(Integer forumId,Date begin,Date end,Integer first,Integer count){
		return dao.getList(forumId,begin,end,first,count);
	}
	
	@Transactional(readOnly = true)
	public List<Object[]> getList(Date begin,Date end,int count){
		return dao.getList(begin,end,count);
	}

	@Transactional(readOnly = true)
	public BbsForumCount findById(Integer id) {
		BbsForumCount entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsForumCount findByForum(Integer forumId,Date date){
		return dao.findByForum(forumId,date);
	}

	public BbsForumCount save(BbsForumCount bean) {
		dao.save(bean);
		return bean;
	}
	
	public BbsForumCount init(Integer forumId){
		Date now=Calendar.getInstance().getTime();
		now=DateUtils.parseDateTimeToDay(now);
		BbsForumCount count=findByForum(forumId, now);
		if(count==null){
			count=new BbsForumCount();
			count.setCountDate(now);
			count.setForum(forumMng.findById(forumId));
			count.setPostCount(0);
			count.setTopicCount(0);
			count.setVisitCount(0);
			return save(count);
		}else{
			return count;
		}
	}

	public BbsForumCount update(BbsForumCount bean) {
		Updater<BbsForumCount> updater = new Updater<BbsForumCount>(bean);
		BbsForumCount entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsForumCount deleteById(Integer id) {
		BbsForumCount bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsForumCount[] deleteByIds(Integer[] ids) {
		BbsForumCount[] beans = new BbsForumCount[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsForumCountDao dao;
	@Autowired
	private BbsForumMng forumMng;

	@Autowired
	public void setDao(BbsForumCountDao dao) {
		this.dao = dao;
	}


}