package com.jeecms.bbs.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.bbs.entity.BbsTopicPostOperate;

public interface BbsTopicPostOperateDao {
	public Pagination getPage(Short dataType,
			Integer userId,Integer operate,int pageNo, int pageSize);
	
	public List<BbsTopicPostOperate> getList(Integer dataId,Short dateType,
			Integer userId,Integer operate,Integer first,Integer count);

	public BbsTopicPostOperate findById(Long id);

	public BbsTopicPostOperate save(BbsTopicPostOperate bean);

	public BbsTopicPostOperate updateByUpdater(Updater<BbsTopicPostOperate> updater);

	public BbsTopicPostOperate deleteById(Long id);
}