package com.jeecms.bbs.manager.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsUserAccountDao;
import com.jeecms.bbs.entity.BbsAdvertising;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserAccount;
import com.jeecms.bbs.manager.BbsAdvertisingMng;
import com.jeecms.bbs.manager.BbsUserAccountMng;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsUserAccountMngImpl implements BbsUserAccountMng {
	
	@Transactional(readOnly = true)
	public Pagination getPage(String username,Date drawTimeBegin,Date drawTimeEnd,
			int orderBy,int pageNo,int pageSize){
		return dao.getPage(username,drawTimeBegin,drawTimeEnd,orderBy,pageNo,pageSize);
	}
	
	@Transactional(readOnly = true)
	public List<BbsUserAccount> getList(String username, Date drawTimeBegin, Date drawTimeEnd, Integer orderBy,
			Integer first, Integer count) {
		return dao.getList(username,drawTimeBegin,drawTimeEnd,orderBy,first,count);
	}
	
	@Transactional(readOnly = true)
	public BbsUserAccount findById(Integer userId){
		return dao.findById(userId);
	}
	

	public BbsUserAccount updateAccountInfo(String accountWeiXin,
			String accountAlipy, Short drawAccount,BbsUser user) {
		BbsUserAccount entity = dao.findById(user.getId());
		if (entity == null) {
			BbsUserAccount account=new BbsUserAccount();
			account.setAccountAlipy(accountAlipy);
			account.setAccountWeixin(accountWeiXin);
			account.setDrawAccount(drawAccount);
			account.setDayAmount(0d);
			account.setMonthAmount(0d);
			account.setYearAmount(0d);
			account.setNoPayAmount(0d);
			account.setTotalAmount(0d);
			account.setDrawCount(0);
			account.setBuyCount(0);
			account.setGiftDayAmount(0d);
			account.setGiftDrawCount(0);
			account.setGiftMonthAmount(0d);
			account.setGiftNoDrawAmount(0d);
			account.setGiftReceiverCount(0);
			account.setGiftTotalAmount(0d);
			account.setAdAccountMount(0d);
			account.setAdAccountMountTotal(0d);
			entity = save(account, user);
			Set<BbsUserAccount>accountSet=user.getUserAccountSet();
			if(accountSet==null){
				accountSet=new HashSet<BbsUserAccount>();
			}
			accountSet.add(account);
			return entity;
		} else {
			entity.setAccountAlipy(accountAlipy);
			entity.setAccountWeixin(accountWeiXin);
			entity.setDrawAccount(drawAccount);
			return entity;
		}
	}
	
	/**
	 * 用户打赏或者购买
	 * @param payAmout 作者所得
	 * @param authorUser 作者
	 * @return
	 */
	public BbsUserAccount userPay(Double payAmout, BbsUser authorUser) {
		BbsUserAccount entity = dao.findById(authorUser.getId());
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		if (entity == null) {
			entity=updateAccountInfo("", "", (short) 0, authorUser);
		} 
		if(entity.getLastBuyTime()!=null){
			last.setTime(entity.getLastBuyTime());
			int currDay = curr.get(Calendar.DAY_OF_YEAR);
			int lastDay = last.get(Calendar.DAY_OF_YEAR);
			int currYear=curr.get(Calendar.YEAR);
			int lastYear=last.get(Calendar.YEAR);
			int currMonth = curr.get(Calendar.MONTH);
			int lastMonth = last.get(Calendar.MONTH);
			if(lastYear!=currYear){
				entity.setYearAmount(0d);
				entity.setMonthAmount(0d);
				entity.setDayAmount(0d);
			}else{
				if(currMonth!=lastMonth){
					entity.setMonthAmount(0d);
					entity.setDayAmount(0d);
				}else{
					if (currDay != lastDay) {
						entity.setDayAmount(0d);
					}
				}
			}
		}
		entity.setDayAmount(entity.getDayAmount()+payAmout);
		entity.setMonthAmount(entity.getMonthAmount()+payAmout);
		entity.setYearAmount(entity.getYearAmount()+payAmout);
		entity.setTotalAmount(entity.getTotalAmount()+payAmout);
		entity.setLastBuyTime(curr.getTime());
		entity.setBuyCount(entity.getBuyCount()+1);
		entity.setNoPayAmount(entity.getNoPayAmount()+payAmout);
		return entity;
	}
	
	public BbsUserAccount adRecharge(Double adAmount, BbsUser user) {
		BbsUserAccount entity = dao.findById(user.getId());
		if(entity.getAdAccountMount()!=null){
			entity.setAdAccountMount(adAmount+entity.getAdAccountMount());
		}else{
			entity.setAdAccountMount(adAmount);
		}
		if(entity.getAdAccountMountTotal()!=null){
			entity.setAdAccountMountTotal(adAmount+entity.getAdAccountMountTotal());
		}else{
			entity.setAdAccountMountTotal(adAmount);
		}
		//充值后开通已经禁用的广告
		List<BbsAdvertising>advertise=advertisingMng.getList(null, 
				false, user.getId(), 1000);
		for(BbsAdvertising ad:advertise){
			ad.setEnabled(true);
			advertisingMng.update(ad);
		}
		return entity;
	}
	
	public BbsUserAccount adPay(Double adAmount, BbsUser user) {
		BbsUserAccount entity = dao.findById(user.getId());
		if(entity!=null){
			if(entity.getAdAccountMount()!=null){
				entity.setAdAccountMount(entity.getAdAccountMount()-adAmount);
			}else{
				entity.setAdAccountMount(0d);
			}
		}
		return entity;
	}
	
	/**
	 * 用户接收礼物
	 * @param payAmout 金额
	 * @param receiverUser 作者
	 * @return
	 */
	public BbsUserAccount userReceiveGift(Double payAmout, BbsUser receiverUser) {
		BbsUserAccount entity = dao.findById(receiverUser.getId());
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		if (entity == null) {
			entity=updateAccountInfo("", "", (short) 0, receiverUser);
		}
		if(entity.getGiftLastReceiverTime()!=null){
			last.setTime(entity.getLastBuyTime());
			int currDay = curr.get(Calendar.DAY_OF_YEAR);
			int lastDay = last.get(Calendar.DAY_OF_YEAR);
			int currYear=curr.get(Calendar.YEAR);
			int lastYear=last.get(Calendar.YEAR);
			int currMonth = curr.get(Calendar.MONTH);
			int lastMonth = last.get(Calendar.MONTH);
			if(lastYear!=currYear){
				entity.setGiftMonthAmount(0d);
				entity.setGiftDayAmount(0d);
			}else{
				if(currMonth!=lastMonth){
					entity.setGiftMonthAmount(0d);
					entity.setGiftDayAmount(0d);
				}else{
					if (currDay != lastDay) {
						entity.setGiftDayAmount(0d);
					}
				}
			}
		}
		entity.setGiftDayAmount(entity.getDayAmount()+payAmout);
		entity.setGiftMonthAmount(entity.getMonthAmount()+payAmout);
		entity.setGiftTotalAmount(entity.getTotalAmount()+payAmout);
		entity.setGiftLastReceiverTime(curr.getTime());
		entity.setGiftReceiverCount(entity.getGiftReceiverCount()+1);
		entity.setGiftNoDrawAmount(entity.getGiftNoDrawAmount()+payAmout);
		return entity;
	}
	
	
	public BbsUserAccount payToAuthor(Double drawAmout, BbsUser user,Date payTime){
		BbsUserAccount entity = dao.findById(user.getId());
		if (entity != null&&drawAmout!=null) {
			if(entity.getNoPayAmount()>=drawAmout){
				entity.setDrawCount(entity.getDrawCount()+1);
				entity.setLastDrawTime(payTime);
				entity.setNoPayAmount(entity.getNoPayAmount()-drawAmout);
			}
		} 
		return entity;
	}
	
	public BbsUserAccount updateWeiXinOpenId(Integer userId,String weiXinOpenId){
		BbsUserAccount account=dao.findById(userId);
		account.setAccountWeixinOpenId(weiXinOpenId);
		return account;
	}

	private BbsUserAccount save(BbsUserAccount account, BbsUser user) {
		account.setUser(user);
		dao.save(account);
		return account;
	}

	private BbsUserAccountDao dao;
	@Autowired
	private BbsAdvertisingMng advertisingMng;

	@Autowired
	public void setDao(BbsUserAccountDao dao) {
		this.dao = dao;
	}

}