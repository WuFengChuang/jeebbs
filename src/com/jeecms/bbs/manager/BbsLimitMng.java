package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsLimit;

public interface BbsLimitMng {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsLimit findById(Integer id);
	
	public boolean ipIsLimit(String ip);
	
	public boolean userIsLimit(Integer userId);

	public BbsLimit save(BbsLimit bean);

	public BbsLimit update(BbsLimit bean);

	public BbsLimit deleteById(Integer id);
	
	public BbsLimit[] deleteByIds(Integer[] ids);
}