package com.jeecms.bbs.manager;

import com.jeecms.bbs.entity.BbsConfig;
import com.jeecms.core.entity.BbsConfigAttr;

public interface BbsConfigMng {
	public BbsConfig findById(Integer id);
	
	public BbsConfig get();

	public BbsConfig updateConfigForDay(Integer siteId);

	public BbsConfig update(BbsConfig bean);
	
}