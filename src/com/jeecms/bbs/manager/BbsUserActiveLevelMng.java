package com.jeecms.bbs.manager;

import java.util.List;

import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsUserActiveLevel;

public interface BbsUserActiveLevelMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsUserActiveLevel> getApiList(Integer first,Integer count);
	
	public List<BbsUserActiveLevel> getList(Integer count);

	public BbsUserActiveLevel findById(Integer id);

	public BbsUserActiveLevel save(BbsUserActiveLevel bean);

	public BbsUserActiveLevel update(BbsUserActiveLevel bean);

	public BbsUserActiveLevel deleteById(Integer id);
	
	public BbsUserActiveLevel[] deleteByIds(Integer[] ids);
}