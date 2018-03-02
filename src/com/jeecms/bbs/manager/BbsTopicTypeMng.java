package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.bbs.entity.BbsTopicType;

public interface BbsTopicTypeMng {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsTopicType findById(Integer id);
	
	public List<BbsTopicType>getTopicTypeListFromTitle(String title);
	
	public BbsTopicType findByName(String name);
	
	public boolean typeNameExist(String name);
	
	public BbsTopicType findByPath(String path);

	public BbsTopicType save(BbsTopicType bean);

	public BbsTopicType update(BbsTopicType bean,Integer parentId);

	public BbsTopicType deleteById(Integer id);
	
	public BbsTopicType[] deleteByIds(Integer[] ids);

	public List<BbsTopicType> getTopList(Boolean displayOnly, 
			Boolean cacheable,Integer orderBy,Integer first,Integer count);
	
	public Pagination getTopPage(Boolean displayOnly, 
			Boolean cacheable,Integer orderBy,Integer pageNo,Integer pageSize);
	
	public List<BbsTopicType> getChildList(Integer parentId, Boolean displayOnly,
			 Boolean cacheable,Integer orderBy,Integer firs,Integer count);

	public Pagination getChildPage(Integer parentId,Boolean displayOnly, 
			Boolean cacheable,Integer orderBy,Integer pageNo,Integer pageSize);
}