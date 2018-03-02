package com.jeecms.bbs.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.bbs.entity.BbsTopicTypeSubscribe;

public interface BbsTopicTypeSubscribeDao {
	public Pagination getPage(Integer userId,int pageNo, int pageSize);

	public BbsTopicTypeSubscribe findById(Integer id);
	
	public List<BbsTopicTypeSubscribe> getList(Integer typeId,Integer userId);
	
	public BbsTopicTypeSubscribe find(Integer typeId,Integer userId);

	public BbsTopicTypeSubscribe save(BbsTopicTypeSubscribe bean);

	public BbsTopicTypeSubscribe updateByUpdater(Updater<BbsTopicTypeSubscribe> updater);

	public BbsTopicTypeSubscribe deleteById(Integer id);
}