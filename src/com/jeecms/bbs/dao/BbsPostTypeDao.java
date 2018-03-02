package com.jeecms.bbs.dao;

import java.util.List;

import com.jeecms.bbs.entity.BbsPostType;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface BbsPostTypeDao {
	public Pagination getPage(Integer siteId,Integer forumId,Integer parentId,int pageNo, int pageSize);
	
	public List<BbsPostType> getList(Integer siteId,Integer forumId,Integer parentId);

	public BbsPostType findById(Integer id);

	public BbsPostType save(BbsPostType bean);

	public BbsPostType updateByUpdater(Updater<BbsPostType> updater);

	public BbsPostType deleteById(Integer id);
}