package com.jeecms.bbs.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.bbs.entity.BbsGift;

public interface BbsGiftDao {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsGift> getList(Boolean disabled,Integer first, Integer count);

	public BbsGift findById(Integer id);

	public BbsGift save(BbsGift bean);

	public BbsGift updateByUpdater(Updater<BbsGift> updater);

	public BbsGift deleteById(Integer id);
}