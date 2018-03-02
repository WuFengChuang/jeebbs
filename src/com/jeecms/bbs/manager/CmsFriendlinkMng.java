package com.jeecms.bbs.manager;

import java.util.List;

import com.jeecms.bbs.entity.CmsFriendlink;
import com.jeecms.common.page.Pagination;


public interface CmsFriendlinkMng {
	public List<CmsFriendlink> getList(Integer siteId, Integer ctgId,
			Boolean enabled,Integer first,Integer count);
	
	public Pagination getPage(Integer siteId, Integer ctgId,
			Boolean enabled,Integer pageNo,Integer pageSize);
	
	public int countByCtgId(Integer ctgId);

	public CmsFriendlink findById(Integer id);

	public int updateViews(Integer id);

	public CmsFriendlink save(CmsFriendlink bean, Integer ctgId);

	public CmsFriendlink update(CmsFriendlink bean, Integer ctgId);

	public void updatePriority(Integer[] ids, Integer[] priorities);

	public CmsFriendlink deleteById(Integer id);

	public CmsFriendlink[] deleteByIds(Integer[] ids);
}