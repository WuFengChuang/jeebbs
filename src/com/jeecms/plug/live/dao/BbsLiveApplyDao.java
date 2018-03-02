package com.jeecms.plug.live.dao;

import java.util.List;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLiveApply;

public interface BbsLiveApplyDao {
	public Pagination getPage(String mobile,Short status,
			Integer applyUserId,int pageNo, int pageSize);
	
	public List<BbsLiveApply> getList(String mobile,Short status,
			Integer applyUserId,Integer first, Integer count);
	
	public Long findLiveApplyCount(Short status,Integer applyUserId);

	public BbsLiveApply findById(Integer id);

	public BbsLiveApply save(BbsLiveApply bean);

	public BbsLiveApply updateByUpdater(Updater<BbsLiveApply> updater);

	public BbsLiveApply deleteById(Integer id);
}