package com.jeecms.bbs.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.bbs.entity.BbsGiftUser;

public interface BbsGiftUserDao {
	public Pagination getPage(Integer giftId,Integer userId,int pageNo, int pageSize);
	
	public List<BbsGiftUser> getList(Integer giftId,Integer userId,
			Integer first, Integer count);

	public BbsGiftUser findById(Integer id);

	public BbsGiftUser save(BbsGiftUser bean);

	public BbsGiftUser updateByUpdater(Updater<BbsGiftUser> updater);

	public BbsGiftUser deleteById(Integer id);
}