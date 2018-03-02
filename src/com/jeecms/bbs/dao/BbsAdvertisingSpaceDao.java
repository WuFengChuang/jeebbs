package com.jeecms.bbs.dao;

import java.util.List;

import com.jeecms.bbs.entity.BbsAdvertisingSpace;
import com.jeecms.common.hibernate4.Updater;

public interface BbsAdvertisingSpaceDao {
	public List<BbsAdvertisingSpace> getList(Integer siteId);

	public BbsAdvertisingSpace findById(Integer id);

	public BbsAdvertisingSpace save(BbsAdvertisingSpace bean);

	public BbsAdvertisingSpace updateByUpdater(
			Updater<BbsAdvertisingSpace> updater);

	public BbsAdvertisingSpace deleteById(Integer id);
}