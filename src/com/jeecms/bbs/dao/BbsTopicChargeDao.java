package com.jeecms.bbs.dao;

import java.util.Date;
import java.util.List;

import com.jeecms.bbs.entity.BbsTopicCharge;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface BbsTopicChargeDao {
	
	public List<BbsTopicCharge> getList(String title,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,int orderBy,Integer first,Integer count);
	
	public Pagination getPage(String title,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,
			int orderBy,int pageNo,int pageSize);
	
	public BbsTopicCharge findById(Integer id);

	public BbsTopicCharge save(BbsTopicCharge bean);

	public BbsTopicCharge updateByUpdater(Updater<BbsTopicCharge> updater);
}