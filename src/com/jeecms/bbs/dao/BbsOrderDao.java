package com.jeecms.bbs.dao;

import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

import java.util.List;


public interface BbsOrderDao {
	public Pagination getPage(String orderNum,Integer buyUserId,Integer authorUserId,
			Short payMode,Short dataType,Integer dataId,int pageNo, int pageSize);
	
	public List<BbsOrder> getList(String orderNum,Integer buyUserId,
			Integer authorUserId,Short payMode,Short dataType,
			Integer dataId,Integer first, Integer count);
	
	public Pagination getPageByTopic(Integer topicId,
			Short payMode,int pageNo, int pageSize);
	
	public List<BbsOrder> getListByTopic(Integer topicId,
			Short payMode,Integer first, Integer count);

	public BbsOrder findById(Long id);
	
	public BbsOrder findByOrderNumber(String orderNumber);
	
	public BbsOrder findByOutOrderNum(String orderNum,Integer payMethod);
	
	public BbsOrder find(Integer buyUserId,Integer topicId);

	public BbsOrder save(BbsOrder bean);

	public BbsOrder updateByUpdater(Updater<BbsOrder> updater);

	public BbsOrder deleteById(Long id);
}