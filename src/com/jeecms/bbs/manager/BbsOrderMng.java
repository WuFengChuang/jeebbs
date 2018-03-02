package com.jeecms.bbs.manager;

import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsGift;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.common.page.Pagination;

import java.util.List;


public interface BbsOrderMng {
	public BbsOrder adOrder(Integer orderType ,Integer buyUserId,
			String outOrderNum);
	public BbsOrder topicOrder(Integer topicId,Integer orderType,
			Short chargeReward,Integer buyUserId,String outOrderNum);
	
	public BbsOrder magicOrder(String mid, Integer num,Integer orderType,
			String outOrderNum,Integer buyUserId);
	
	public BbsOrder giftOrder(BbsGift gift, Integer num,Integer orderType,
			String outOrderNum,Integer buyUserId);
	
	public Double getAliPayOrderAmount(String outOrderNum,
			BbsConfigCharge config);
	
	public Double getWeChatOrderAmount(String outOrderNum,
			BbsConfigCharge config);
	
	
	/**
	 * 
	 * @param orderNum 订单编号
	 * @param buyUserId 购买者ID
	 * @param authorUserId 发帖者ID
	 * @param payMode 支付模式 1收费 2打赏 0所有
	 * @param dataType 数据类型 0主题 1道具 -1所有
	 * @param dataId 数据ID(主要用于道具ID查询)
	 * @param pageNo 
	 * @param pageSize
	 * @return
	 */
	public Pagination getPage(String orderNum,Integer buyUserId,Integer authorUserId
			,Short payMode,Short dataType,Integer dataId,int pageNo, int pageSize);
	
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
	
	public boolean hasBuyTopic(Integer buyUserId,Integer topicId);

	public BbsOrder save(BbsOrder bean);

	public BbsOrder update(BbsOrder bean);

	public BbsOrder deleteById(Long id);
	
	public BbsOrder[] deleteByIds(Long[] ids);
}