package com.jeecms.bbs.dao;

import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

import java.util.Date;
import java.util.List;


public interface BbsAccountDrawDao {
	public Pagination getPage(Integer userId,Short applyStatus,
			Date applyTimeBegin,Date applyTimeEnd,
			Short applyType,int pageNo, int pageSize);
	
	public List<BbsAccountDraw> getList(Integer userId,Short applyStatus,
			Date applyTimeBegin,Date applyTimeEnd,
			Short applyType,Integer first,Integer count);
	
	public List<BbsAccountDraw> getList(Integer userId,Short[] status,
			Short applyType,Integer count);
	
	public Long findAccountDrawCount(Integer userId,Short applyStatus,Short applyType);

	public BbsAccountDraw findById(Integer id);

	public BbsAccountDraw save(BbsAccountDraw bean);

	public BbsAccountDraw updateByUpdater(Updater<BbsAccountDraw> updater);

	public BbsAccountDraw deleteById(Integer id);
}