package com.jeecms.bbs.manager;

import java.util.List;

import com.jeecms.bbs.entity.BbsAdvertisingSpace;


public interface BbsAdvertisingSpaceMng {
	public List<BbsAdvertisingSpace> getList(Integer siteId);

	public BbsAdvertisingSpace findById(Integer id);

	public BbsAdvertisingSpace save(BbsAdvertisingSpace bean);

	public BbsAdvertisingSpace update(BbsAdvertisingSpace bean);

	public BbsAdvertisingSpace deleteById(Integer id);

	public BbsAdvertisingSpace[] deleteByIds(Integer[] ids);
}