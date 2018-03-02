package com.jeecms.plug.live.manager.impl;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsTopicCharge;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsIncomeStatisticMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.plug.live.dao.BbsLiveDao;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.manager.BbsLiveChapterDeleteChecker;
import com.jeecms.plug.live.manager.BbsLiveChargeMng;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.manager.BbsLiveUserAccountMng;
import com.jeecms.plug.live.manager.BbsLiveUserMng;

@Service
@Transactional
public class BbsLiveMngImpl implements BbsLiveMng ,BbsLiveChapterDeleteChecker{
	
	public static final String WEIXIN_ORDER_QUERY_URL="weixin.orderquery.url";
	public static final String ALI_PAY_URL="alipay.openapi.url";
	
	@Transactional(readOnly = true)
	public Pagination getPage(Integer cid,String title,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd,
			Integer orderBy,int pageNo, int pageSize) {
		Pagination page = dao.getPage(cid,title,hostUserId,status,
				timeBegin,timeEnd, liveEndTimeBegin,liveEndTimeEnd,
				orderBy,pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsLive> getList(Integer cid,String title,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd,
			Integer orderBy,Integer first, Integer count){
		return dao.getList(cid, title, hostUserId, status, 
				timeBegin, timeEnd, liveEndTimeBegin,liveEndTimeEnd,
				orderBy, first, count);
	}
	
	public Long findLiveCount(Integer cid,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd){
		return dao.findLiveCount(cid,hostUserId,status,timeBegin,
				timeEnd,liveEndTimeBegin,liveEndTimeEnd);
	}
	
	public BbsOrder liveOrder(BbsLive live, Integer num,Integer orderType,
			String orderNumber,String outOrderNum,Integer buyUserId,
			boolean selfOnly){
		BbsOrder order=new BbsOrder();
		BbsUser buyUser = null;
		if(buyUserId!=null){
   	    	buyUser=userMng.findById(buyUserId);
   	    }
    	if(live!=null&&buyUser!=null){
    		//外部订单号和内部订单号要一一对应，否则会出现一个外部订单可以用于形成多个内部订单
    		BbsConfigCharge config=configChargeMng.getDefault();
    		initWeiXinPayUrl();
    		initAliPayUrl();
    		Double orderAmount = -1d;
	 		// 这里是把微信商户的订单号放入了交易号中
	   	    if(orderType.equals(BbsOrder.PAY_METHOD_WECHAT)){
	   	    	order.setOrderNumWeixin(outOrderNum);
	   	    	orderAmount=orderMng.getWeChatOrderAmount(outOrderNum, config);
	   	    }else if(orderType.equals(BbsOrder.PAY_METHOD_ALIPAY)){
	   	    	order.setOrderNumAlipay(outOrderNum);
	   	    	orderAmount=orderMng.getAliPayOrderAmount(outOrderNum, config);
	   	    }
	   	    if(orderAmount.equals(0d)){
	   	    	order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_ORDER_NUM_ERROR);
	   	    }else{
	   	    	Date now=Calendar.getInstance().getTime();
	   	    	Double needPrice=live.getBeginPrice();
	   	    	if(now.after(live.getEndTime())){
	   	    		needPrice=live.getAfterPrice();
	   	    	}
	   	    	//购买行为订单金额需要大于收费金额
	   	    	if((orderAmount>=needPrice*num)){
			   	    order.setAuthorUser(null);
		   	    	order.setBuyUser(buyUser);
			   	    order.setDataId(live.getId());
			   	    order.setDataType(BbsOrder.ORDER_TYPE_LIVE);
			   	    order.setOrderNumber(orderNumber);
			   	    order.setBuyTime(now);
			   	    Double chargeAmount=orderAmount;
			   	    Double platAmount=chargeAmount*live.getCommissionRate()/100;
			     	Double authorAmount=chargeAmount*(1-live.getCommissionRate()/100);
			     	order.setChargeReward(BbsTopicCharge.MODEL_CHARGE);
			   	    order.setChargeAmount(chargeAmount);
			   	    order.setPlatAmount(platAmount);
			   	    order.setAuthorAmount(authorAmount);
			   	    order.setLiveUserNum(num);
			   	    if(selfOnly){
			   	    	order.setLiveUsedNum(1);
			   	    }else{
			   	    	order.setLiveUsedNum(0);
			   	    }
		 			order=orderMng.save(order);
		 			order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_SUCCESS);
		 			//已售出票数
		 			if(live.getJoinUserNum()!=null){
		 				live.setJoinUserNum(live.getJoinUserNum()+num);
		 			}else{
		 				live.setJoinUserNum(num);
		 			}
		 			liveMng.update(live);
		 			//live门票若是只是自己用，则自动购买票
		 			if(selfOnly){
		 				liveUserMng.saveUserLiveTicket(order,buyUserId, live.getId());
		 			}
		 			//平台所得统计
		 			configChargeMng.afterUserPay(platAmount,BbsIncomeType.ticket);
		 			//主播收益统计并统计总门票
		 			liveUserAccountMng.userPay(authorAmount,num,live.getUser());
		 			//live收益统计并统计live总门票
		 			liveChargeMng.afterUserPay(chargeAmount,num,live);
		 			//每日收益统计
		 			incomeStatisticMng.afterPay(platAmount, BbsIncomeType.ticket);
		 			order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_SUCCESS);
		 		}else{
		 			order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_ORDER_AMOUNT_NOT_ENOUGH);
	   	    	}
	   	    }
    	}
	    return order;
	}
	
	
	public void clearLiveUserNum(){
		dao.clearLiveUserNum();
	}
	
	public int sessionConnect(Integer liveId,boolean isClosed){
		int count=0;
		BbsLive live=null;
		if(liveId!=null){
			live=findById(liveId);
		}
		if(live!=null){
			Integer dbNum=live.getInliveUserNum();
			if(isClosed){
				//关闭
				if(dbNum!=null){
					count=dbNum-1;
				}
			}else{
				//开启
				if(dbNum!=null){
					count=dbNum+1;
				}else{
					count=1;
				}
			}
			if(count<0){
				count=0;
			}
			live.setInliveUserNum(count);
			update(live);
		}
		return count;
	}

	@Transactional(readOnly = true)
	public BbsLive findById(Integer id) {
		BbsLive entity = dao.findById(id);
		return entity;
	}

	public BbsLive save(BbsLive bean) {
		dao.save(bean);
		return bean;
	}

	public BbsLive update(BbsLive bean) {
		Updater<BbsLive> updater = new Updater<BbsLive>(bean);
		BbsLive entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsLive deleteById(Integer id) {
		BbsLive bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsLive[] deleteByIds(Integer[] ids) {
		BbsLive[] beans = new BbsLive[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public String checkForChapterDelete(Integer chapterId) {
		int count = dao.countByChapterId(chapterId);
		if (count > 0) {
			return "chapter.error.cannotDeleteChapter";
		} else {
			return null;
		}
	}
	
	private void initAliPayUrl(){
		if(getAliPayUrl()==null){
			setAliPayUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),ALI_PAY_URL));
		}
	}
	
	private void initWeiXinPayUrl(){
		if(getWeiXinPayUrl()==null){
			setWeiXinPayUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),WEIXIN_ORDER_QUERY_URL));
		}
	}
	
	private String weiXinPayUrl;
	
	private String aliPayUrl;
	
	public String getWeiXinPayUrl() {
		return weiXinPayUrl;
	}

	public void setWeiXinPayUrl(String weiXinPayUrl) {
		this.weiXinPayUrl = weiXinPayUrl;
	}

	public String getAliPayUrl() {
		return aliPayUrl;
	}

	public void setAliPayUrl(String aliPayUrl) {
		this.aliPayUrl = aliPayUrl;
	}

	private BbsLiveDao dao;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private BbsOrderMng orderMng;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private BbsUserMng userMng;
	@Autowired
	private BbsLiveMng liveMng;
	@Autowired
	private BbsLiveUserMng liveUserMng;
	@Autowired
	private BbsLiveUserAccountMng liveUserAccountMng;
	@Autowired
	private BbsLiveChargeMng liveChargeMng;
	@Autowired
	private BbsIncomeStatisticMng incomeStatisticMng;

	@Autowired
	public void setDao(BbsLiveDao dao) {
		this.dao = dao;
	}
}