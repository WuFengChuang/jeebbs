package com.jeecms.bbs.dao;

import java.util.List;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsUserActiveLevel;

public interface BbsUserActiveLevelDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsUserActiveLevel> getList(Integer count);

	public BbsUserActiveLevel findById(Integer id);

	public BbsUserActiveLevel save(BbsUserActiveLevel bean);

	public BbsUserActiveLevel updateByUpdater(Updater<BbsUserActiveLevel> updater);

	public BbsUserActiveLevel deleteById(Integer id);

	public List<BbsUserActiveLevel> getApiList(Integer first, Integer count);
}