package com.jeecms.bbs.dao;

import java.util.List;

import com.jeecms.bbs.entity.CmsFriendlink;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface CmsFriendlinkDao {
	public List<CmsFriendlink> getList(Integer siteId, Integer ctgId,
			Boolean enabled,Integer first,Integer count);

	public int countByCtgId(Integer ctgId);

	public CmsFriendlink findById(Integer id);

	public CmsFriendlink save(CmsFriendlink bean);

	public CmsFriendlink updateByUpdater(Updater<CmsFriendlink> updater);

	public CmsFriendlink deleteById(Integer id);

	public Pagination getPage(Integer siteId, Integer ctgId, Boolean enabled, Integer pageNo, Integer pageSize);
}