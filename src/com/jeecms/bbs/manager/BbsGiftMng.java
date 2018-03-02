package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.bbs.entity.BbsGift;

public interface BbsGiftMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public List<BbsGift> getList(Boolean disabled,Integer first, Integer count);
	
	public BbsGift afterUserPay(Double payAmout, BbsGift gift);

	public BbsGift findById(Integer id);

	public BbsGift save(BbsGift bean);

	public BbsGift update(BbsGift bean);

	public BbsGift deleteById(Integer id);
	
	public BbsGift[] deleteByIds(Integer[] ids);

	public void updatePriority(Integer[] idArray, Integer[] prioritiyArray, Boolean[] disabledArray);
}