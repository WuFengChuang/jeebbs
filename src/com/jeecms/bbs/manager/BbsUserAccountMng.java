package com.jeecms.bbs.manager;

import java.util.Date;
import java.util.List;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserAccount;
import com.jeecms.common.page.Pagination;

public interface BbsUserAccountMng {
	
	public Pagination getPage(String username,Date drawTimeBegin,Date drawTimeEnd,
			int orderBy,int pageNo,int pageSize);
	
	public List<BbsUserAccount> getList(String username,Date drawTimeBegin,Date drawTimeEnd,
			Integer orderBy,Integer first,Integer count);
	
	public BbsUserAccount findById(Integer userId);
	/**
	 * 平台转账给笔者
	 * @param drawAmout
	 * @param user
	 * @param payTime
	 * @return
	 */
	public BbsUserAccount payToAuthor(Double drawAmout, BbsUser user,Date payTime);
	
	/**
	 * 用户购买 笔者金额统计
	 * @param payAmout
	 * @param user
	 * @return
	 */
	public BbsUserAccount userPay(Double payAmout, BbsUser user);

	/**
	 * 用户收到礼物
	 * @param payAmout  礼物金额
	 * @param receiverUser  接收者
	 * @return
	 */
	public BbsUserAccount userReceiveGift(Double payAmout, BbsUser receiverUser);
	
	/**
	 * 用户广告充值
	 * @param adAmount 充值金额
	 * @param user	充值用户
	 * @return
	 */
	public BbsUserAccount adRecharge(Double adAmount, BbsUser user);
	
	/**
	 * 广告消费（按点击、展现量收费）
	 * @param adAmount 广告金额
	 * @param user
	 * @return
	 */
	public BbsUserAccount adPay(Double adAmount, BbsUser user);
	
	public BbsUserAccount updateAccountInfo(String accountWeiXin,
			String accountAlipy, Short drawAccount,BbsUser user);
	
	public BbsUserAccount updateWeiXinOpenId(Integer userId,String weiXinOpenId);
}