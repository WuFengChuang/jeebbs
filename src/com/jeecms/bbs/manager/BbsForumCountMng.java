package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;

import java.util.Date;
import java.util.List;

import com.jeecms.bbs.entity.BbsForumCount;

public interface BbsForumCountMng {
	public Pagination getPage(Integer forumId,Date begin,Date end,
			int pageNo, int pageSize);
	
	public List<BbsForumCount> getList(Integer forumId,Date begin,Date end,
			Integer first,Integer count);
	
	public List<Object[]> getList(Date begin,Date end,int count);

	public BbsForumCount findById(Integer id);
	
	public BbsForumCount findByForum(Integer forumId,Date date);

	public BbsForumCount save(BbsForumCount bean);
	
	public BbsForumCount init(Integer forumId);

	public BbsForumCount update(BbsForumCount bean);

	public BbsForumCount deleteById(Integer id);
	
	public BbsForumCount[] deleteByIds(Integer[] ids);
}