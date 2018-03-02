package com.jeecms.plug.live.manager;

import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.entity.BbsLiveCharge;

public interface BbsLiveChargeMng {
	
	/**
	 * 用户购买live门票后统计live收益
	 * @param payAmout  支付金额
	 * @param ticketNum 门票数
	 * @param live 活动live
	 */
	public BbsLiveCharge afterUserPay(Double payAmout,Integer ticketNum,BbsLive live);
	
	/**
	 * live接收礼物
	 * @param live 活动live
	 */
	public BbsLiveCharge afterReceiveGift(BbsLive live,Integer giftNum);
	
	public Pagination getPage(int pageNo, int pageSize);

	public BbsLiveCharge findById(Integer id);

	public BbsLiveCharge save(BbsLiveCharge bean,BbsLive live);

	public BbsLiveCharge update(BbsLiveCharge bean);

	public BbsLiveCharge deleteById(Integer id);
	
	public BbsLiveCharge[] deleteByIds(Integer[] ids);
}