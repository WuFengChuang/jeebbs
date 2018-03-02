package com.jeecms.plug.live.manager;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserAccount;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLiveUserAccount;

public interface BbsLiveUserAccountMng {
	
	/**
	 * 1总收益降序 2总收益升序 3年收益降序 4年收益升序  5月收益降序 6月收益升序  
	 * 7日收益降序 8日收益升序  11账户余额降序 12账户余额升序   13总门票降序 14总门票升序  
	 * 15总礼物数降序 16总礼物数升序 17置顶降序 18置顶升序 
	 * @param orderBy
	 */
	public List<BbsLiveUserAccount> getList(Integer userId,Integer orderBy,
			int count);
	
	public Pagination getPage(Integer userId,Integer orderBy,int pageNo, int pageSize);
	/**
	 * 用户购买 主讲人收益统计
	 * @param payAmout
	 * @param ticketNum 门票数
	 * @param user
	 * @return
	 */
	public BbsLiveUserAccount userPay(Double payAmout,Integer ticketNum, BbsUser user);
	
	/**
	 * 转账给主讲者
	 * @param drawAmout
	 * @param user
	 * @param payTime
	 * @return
	 */
	public BbsLiveUserAccount payToHost(Double drawAmout, BbsUser user,Date payTime);
	
	public BbsLiveUserAccount updateAccountInfo(BbsUser user,Integer ticketNum);
	
	public BbsLiveUserAccount afterReceiveGift(BbsUser user,Integer giftNum);
	
	/**
	 * 企业转账给主讲人
	 * @param serverUrl
	 * @param drawId
	 * @param drawUser
	 * @param payUser
	 * @param payAmount
	 * @param orderNum
	 * @return
	 */
	public String weixinTransferPay(String serverUrl,Integer drawId,
			BbsUser drawUser,BbsUser payUser,Double payAmount,String orderNum,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model);

	public BbsLiveUserAccount findById(Integer id);

	public BbsLiveUserAccount save(BbsLiveUserAccount bean,BbsUser user);

	public BbsLiveUserAccount update(BbsLiveUserAccount bean);

	public BbsLiveUserAccount deleteById(Integer id);
	
	public BbsLiveUserAccount[] deleteByIds(Integer[] ids);

	public BbsLiveUserAccount[]  updatePriority(Integer[] ids, Integer[] priority);
}