package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.bbs.entity.BbsGift;
import com.jeecms.bbs.entity.BbsGiftUser;
import com.jeecms.bbs.entity.BbsUser;

public interface BbsGiftUserMng {
	public Pagination getPage(Integer giftId,Integer userId,
			int pageNo, int pageSize);
	
	public List<BbsGiftUser> getList(Integer giftId,Integer userId,
			Integer first, Integer count);

	public BbsGiftUser findById(Integer id);
	
	public BbsGiftUser addUserGift(Integer giftId,Integer userId,Integer num);
	
	/**
	 * 赠送用户礼物
	 * @param gift 礼物
	 * @param giveUser 赠送者
	 * @param receiverUser  接收者
	 * @param num 礼物数量 
	 * @param liveId 活动liveId
	 * @return 0赠送失败 1成功
	 */
	public int giveUserGift(BbsGift gift,BbsUser giveUser,
			BbsUser receiverUser,Integer liveId,Integer num);

	public BbsGiftUser save(BbsGiftUser bean);

	public BbsGiftUser update(BbsGiftUser bean);

	public BbsGiftUser deleteById(Integer id);
	
	public BbsGiftUser[] deleteByIds(Integer[] ids);
}