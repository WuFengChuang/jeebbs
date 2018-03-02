package com.jeecms.bbs.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.bbs.entity.BbsTopicType;

public interface BbsTopicTypeDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsTopicType> getTopList(Boolean displayOnly,
			Integer count, Boolean cacheable,Integer orderBy,Integer first);
	
	public List<BbsTopicType> getChildList(Integer parentId, Boolean displayOnly,
			Integer count, Boolean cacheable,Integer orderBy,Integer first);

	public BbsTopicType findById(Integer id);
	
	public BbsTopicType findByName(String name);
	
	public BbsTopicType findByPath(String path);

	public BbsTopicType save(BbsTopicType bean);

	public BbsTopicType updateByUpdater(Updater<BbsTopicType> updater);

	public BbsTopicType deleteById(Integer id);

	public Pagination getTopPage(Boolean displayOnly, Boolean cacheable, Integer orderBy, Integer pageNo,
			Integer pageSize);

	public Pagination getChildList(Integer parentId, Boolean displayOnly, Boolean cacheable, Integer orderBy,
			Integer pageNo, Integer pageSize);
}