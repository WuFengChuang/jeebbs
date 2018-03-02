package com.jeecms.bbs.dao;

import java.util.Date;
import java.util.List;

import com.jeecms.bbs.entity.BbsAdvertising;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface BbsAdvertisingDao {
	public Pagination getPage(Integer siteId, Integer adspaceId,
			Boolean enabled,  Integer queryChargeMode,String queryTitle,
			Integer ownerId, int pageNo, int pageSize);

	public List<BbsAdvertising> getList(Integer adspaceId, Boolean enabled
			,Integer ownerId,Integer count);
	
	public List<BbsAdvertising> getApiList(String queryTitle,Integer orderBy,
			Integer queryChargeMode,Integer first,Integer count);
	
	public Long findAdvertiseCount(Integer adspaceId, Boolean enabled,
			Integer ownerId,Date queryTimeBegin,Date queryTimeEnd);

	public BbsAdvertising findById(Integer id);

	public BbsAdvertising save(BbsAdvertising bean);

	public BbsAdvertising updateByUpdater(Updater<BbsAdvertising> updater);

	public BbsAdvertising deleteById(Integer id);
}