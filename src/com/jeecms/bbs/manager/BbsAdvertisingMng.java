package com.jeecms.bbs.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jeecms.bbs.entity.BbsAdvertising;
import com.jeecms.bbs.entity.BbsAdvertisingSpace;
import com.jeecms.common.page.Pagination;

public interface BbsAdvertisingMng {
	public Pagination getPage(Integer siteId, Integer adspaceId,
			Boolean enabled,Integer queryChargeMode,String queryTitle,
			Integer ownerId,int pageNo, int pageSize);

	public List<BbsAdvertising> getList(Integer adspaceId, Boolean enabled,
			Integer ownerId,Integer count);
	
	public Long findAdvertiseCount(Integer adspaceId, Boolean enabled,
			Integer ownerId,Date queryTimeBegin,Date queryTimeEnd);

	public BbsAdvertising findById(Integer id);

	public BbsAdvertising save(BbsAdvertising bean, Integer adspaceId,
			Map<String, String> attr);
	
	public BbsAdvertising save(BbsAdvertising bean, Integer adspace,
			Integer chargeDay,Date startTime,Map<String, String> attr);

	public BbsAdvertising update(BbsAdvertising bean, Integer adspaceId,
			Integer chargeDay,Date startTime,Map<String, String> attr);
	
	public BbsAdvertising update(BbsAdvertising bean);

	public BbsAdvertising deleteById(Integer id);

	public BbsAdvertising[] deleteByIds(Integer[] ids);

	public void display(Integer id);

	public void click(Integer id);

	public List<BbsAdvertising> getApiList(String queryTitle,Integer orderBy,
			Integer queryChargeMode,Integer first,Integer count);
}