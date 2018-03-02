package com.jeecms.bbs.dao;

import java.util.List;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsTopicMsg;

public interface BbsTopicMsgDao {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsTopicMsg findById(Integer id);

	public BbsTopicMsg save(BbsTopicMsg bean);

	public BbsTopicMsg updateByUpdater(Updater<BbsTopicMsg> updater);

	public BbsTopicMsg deleteById(Integer id);
	
	public List<BbsTopicMsg> getTopicList(Integer userId,Integer count) ;
}