package com.jeecms.bbs.manager.impl;


import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.MagicConstants;
import com.jeecms.bbs.dao.BbsOrderDao;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsGift;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicCharge;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsGiftMng;
import com.jeecms.bbs.manager.BbsGiftUserMng;
import com.jeecms.bbs.manager.BbsIncomeStatisticMng;
import com.jeecms.bbs.manager.BbsMagicLogMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsTopicChargeMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsUserAccountMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.AliPay;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.util.WeixinPay;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.alipay.api.response.AlipayTradeQueryResponse;

@Service
@Transactional
public class BbsOrderMngImpl implements BbsOrderMng {
	
	public static final String WEIXIN_ORDER_QUERY_URL="weixin.orderquery.url";
	public static final String ALI_PAY_URL="alipay.openapi.url";
	
	public BbsOrder adOrder(Integer orderType ,Integer buyUserId,
			String outOrderNum){
		BbsOrder order = new BbsOrder();
		BbsConfigCharge config = configChargeMng.getDefault();
		initWeiXinPayUrl();
		initAliPayUrl();
		Double orderAmount = 0d;
		//把微信商户的订单号放入了交易号中
		if (orderType.equals(BbsOrder.PAY_METHOD_WECHAT)) {
			order.setOrderNumWeixin(outOrderNum);
			orderAmount = getWeChatOrderAmount(outOrderNum, config);
		}else if (orderType.equals(BbsOrder.PAY_METHOD_ALIPAY)) {
			order.setOrderNumAlipay(outOrderNum);
			orderAmount = getAliPayOrderAmount(outOrderNum, config);
		}
		//订单金额不能等于0
		if (orderAmount>0) {
			BbsUser buyUser = userMng.findById(buyUserId);
			order.setAuthorUser(null);
			order.setBuyUser(buyUser);
			order.setDataId(0);
	   	    order.setDataType(BbsOrder.ORDER_TYPE_AD);
	   	    order.setOrderNumber(outOrderNum);
	   	    order.setBuyTime(Calendar.getInstance().getTime());
	     	order.setChargeReward(BbsTopicCharge.MODEL_CHARGE);
	   	    order.setChargeAmount(orderAmount);
	   	    order.setPlatAmount(orderAmount);
	   	    order.setAuthorAmount(0d);
	   	    order.setLiveUsedNum(0);
 			order.setLiveUserNum(0);
 			order=orderMng.save(order);
 			//广告余额增加和累计统计和开通所有广告主的广告
 			userAccountMng.adRecharge(orderAmount, buyUser);
 			//每日收益统计
 			incomeStatisticMng.afterPay(orderAmount, BbsIncomeType.ad);
 			//平台所得统计
 			configChargeMng.afterUserPay(orderAmount,BbsIncomeType.ad);
 			order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_SUCCESS);
		}else{
			order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_ORDER_NUM_ERROR);
		}
		return order;
	}
	
	public BbsOrder topicOrder(Integer topicId,Integer orderType,
			Short chargeReward,Integer buyUserId,String outOrderNum){
		BbsOrder order=new BbsOrder();
	    if(topicId!=null){
	    	BbsTopic topic=topicMng.findById(topicId);
	    	if(topic!=null){
   	    		//外部订单号和内部订单号要一一对应，否则会出现一个外部订单可以用于形成多个内部订单
	    		//内容收费模式且本次订单是购买流程
	    		boolean buy=false;
	    		if(topic.getCharge()&&chargeReward.equals(BbsTopicCharge.MODEL_CHARGE)){
	    			buy=true;
	    		}
	    		BbsConfigCharge config=configChargeMng.getDefault();
	    		initWeiXinPayUrl();
	    		initAliPayUrl();
	    		Double orderAmount = 0d;
		 		// 这里是把微信商户的订单号放入了交易号中
		   	    if(orderType.equals(BbsOrder.PAY_METHOD_WECHAT)){
		   	    	order.setOrderNumWeixin(outOrderNum);
		   	    	orderAmount=getWeChatOrderAmount(outOrderNum, config);
		   	    }else if(orderType.equals(BbsOrder.PAY_METHOD_ALIPAY)){
		   	    	order.setOrderNumAlipay(outOrderNum);
		   	    	orderAmount=getAliPayOrderAmount(outOrderNum, config);
		   	    }
		   	    //订单金额不能等于0
		   	    if(!orderAmount.equals(0d)){
		   	    	//购买行为订单金额需要大于收费金额 或者是打赏
		   	    	if((buy&&orderAmount>=topic.getChargeAmount())||!buy){
		   	    		Double ratio=config.getChargeRatio();
				   	    order.setAuthorUser(topic.getCreater());
				   	    //打赏可以匿名
				   	    if(buyUserId!=null){
				   	    	order.setBuyUser(userMng.findById(buyUserId));
				   	    }
				   	    order.setDataId(topicId);
				   	    order.setDataType(BbsOrder.ORDER_TYPE_TOPIC);
				   	    String orderNumber=System.currentTimeMillis()+RandomStringUtils.random(5,Num62.N10_CHARS);
				   	    order.setOrderNumber(orderNumber);
				   	    order.setBuyTime(Calendar.getInstance().getTime());
				   	    Double chargeAmount=topic.getChargeAmount();
				   	    Double platAmount=topic.getChargeAmount()*ratio;
				     	Double authorAmount=topic.getChargeAmount()*(1-ratio);
				   	    if(orderAmount!=null){
				   	    	chargeAmount=orderAmount;
				   	    	platAmount=orderAmount*ratio;
				   	    	authorAmount=orderAmount*(1-ratio);
				   	    }
				   	    if(chargeReward.equals(BbsTopicCharge.MODEL_REWARD)){
				   	    	order.setChargeReward(BbsTopicCharge.MODEL_REWARD);
				   	    }else{
				   	    	order.setChargeReward(BbsTopicCharge.MODEL_CHARGE);
				   	    }
				   	    order.setChargeAmount(chargeAmount);
				   	    order.setPlatAmount(platAmount);
				   	    order.setAuthorAmount(authorAmount);
				   	    order.setLiveUsedNum(0);
			 			order.setLiveUserNum(0);
			 			order=orderMng.save(order);
			 			BbsUser authorUser=order.getAuthorUser();
			 			//笔者所得统计
			 			userAccountMng.userPay(order.getAuthorAmount(), authorUser);
			 			//平台所得统计
			 			configChargeMng.afterUserPay(order.getPlatAmount(),BbsIncomeType.post);
			 			//主题所得统计
			 			chargeMng.afterUserPay(order.getChargeAmount(), topic);
			 			//每日收益统计
			 			incomeStatisticMng.afterPay(platAmount, BbsIncomeType.post);
			 			order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_SUCCESS);
		   	    	}else{
		   	    		order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_SUCCESS);
		   	    	}
		   	    }else{
		   	    	order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_ORDER_NUM_ERROR);
		   	    }
	    	}
	 	}
	    return order;
	}
	
	public BbsOrder magicOrder(String mid, Integer num,Integer orderType,
			String outOrderNum,Integer buyUserId){
		BbsOrder order=new BbsOrder();
		BbsUser buyUser = null;
		if(buyUserId!=null){
   	    	buyUser=userMng.findById(buyUserId);
   	    }
	    if(mid!=null){
	    	BbsCommonMagic magic=bbsCommonMagicMng.findByIdentifier(mid);
	    	if(magic!=null&&buyUser!=null){
   	    		//外部订单号和内部订单号要一一对应，否则会出现一个外部订单可以用于形成多个内部订单
	    		BbsConfigCharge config=configChargeMng.getDefault();
	    		initWeiXinPayUrl();
	    		initAliPayUrl();
	    		Double orderAmount = 0d;
		 		// 这里是把微信商户的订单号放入了交易号中
		   	    if(orderType.equals(BbsOrder.PAY_METHOD_WECHAT)){
		   	    	order.setOrderNumWeixin(outOrderNum);
		   	    	orderAmount=getWeChatOrderAmount(outOrderNum, config);
		   	    }else if(orderType.equals(BbsOrder.PAY_METHOD_ALIPAY)){
		   	    	order.setOrderNumAlipay(outOrderNum);
		   	    	orderAmount=getAliPayOrderAmount(outOrderNum, config);
		   	    }
		   	    //订单金额不能等于0
		   	    if(!orderAmount.equals(0d)){
		   	    	//购买行为订单金额需要大于收费金额 或者是打赏
		   	    	if((orderAmount>=magic.getPrice()*num)){
				   	    order.setAuthorUser(null);
			   	    	order.setBuyUser(buyUser);
				   	    order.setDataId(magic.getId());
				   	    order.setDataType(BbsOrder.ORDER_TYPE_MAGIC);
				   	    String orderNumber=System.currentTimeMillis()+RandomStringUtils.random(5,Num62.N10_CHARS);
				   	    order.setOrderNumber(orderNumber);
				   	    order.setBuyTime(Calendar.getInstance().getTime());
				   	    Double chargeAmount=orderAmount;
				   	    Double platAmount=chargeAmount;
				     	Double authorAmount=0d;
				     	order.setChargeReward(BbsTopicCharge.MODEL_CHARGE);
				   	    order.setChargeAmount(chargeAmount);
				   	    order.setPlatAmount(platAmount);
				   	    order.setAuthorAmount(authorAmount);
				   	    order.setLiveUsedNum(0);
			 			order.setLiveUserNum(0);
			 			order=orderMng.save(order);
			 			//平台所得统计
			 			configChargeMng.afterUserPay(order.getPlatAmount(),BbsIncomeType.magic);
			 			userMng.updatePoint(buyUserId, null, null,
								mid, num, 3);
			 			magicLogMng.buyMagicLog(magic, buyUser, num,
								MagicConstants.MAGIC_OPERATOR_BUY);
			 			//每日收益统计
			 			incomeStatisticMng.afterPay(platAmount, BbsIncomeType.magic);
			 			order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_SUCCESS);
		   	    	}else{
		   	    		order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_SUCCESS);
		   	    	}
		   	    }else{
		   	    	order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_ORDER_NUM_ERROR);
		   	    }
	    	}
	 	}
	    return order;
	}
	
	public BbsOrder giftOrder(BbsGift gift, Integer num,Integer orderType,
			String outOrderNum,Integer buyUserId){
		BbsOrder order=new BbsOrder();
		BbsUser buyUser = null;
		if(buyUserId!=null){
   	    	buyUser=userMng.findById(buyUserId);
   	    }
    	if(gift!=null&&buyUser!=null){
    		//外部订单号和内部订单号要一一对应，否则会出现一个外部订单可以用于形成多个内部订单
    		BbsConfigCharge config=configChargeMng.getDefault();
    		initWeiXinPayUrl();
    		initAliPayUrl();
    		Double orderAmount = -1d;
	 		// 这里是把微信商户的订单号放入了交易号中
	   	    if(orderType.equals(BbsOrder.PAY_METHOD_WECHAT)){
	   	    	order.setOrderNumWeixin(outOrderNum);
	   	    	orderAmount=getWeChatOrderAmount(outOrderNum, config);
	   	    }else if(orderType.equals(BbsOrder.PAY_METHOD_ALIPAY)){
	   	    	order.setOrderNumAlipay(outOrderNum);
	   	    	orderAmount=getAliPayOrderAmount(outOrderNum, config);
	   	    }
	   	    if(orderAmount.equals(0d)){
	   	    	order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_ORDER_NUM_ERROR);
	   	    }else{
	   	    	//购买行为订单金额需要大于收费金额
	   	    	if((orderAmount>=gift.getPrice()*num)){
			   	    order.setAuthorUser(null);
		   	    	order.setBuyUser(buyUser);
			   	    order.setDataId(gift.getId());
			   	    order.setDataType(BbsOrder.ORDER_TYPE_GIFT);
			   	    String orderNumber=System.currentTimeMillis()+RandomStringUtils.random(5,Num62.N10_CHARS);
			   	    order.setOrderNumber(orderNumber);
			   	    order.setBuyTime(Calendar.getInstance().getTime());
			   	    Double chargeAmount=orderAmount;
			   	    Double platAmount=chargeAmount*config.getGiftChargeRatio();
			     	Double authorAmount=0d;
			     	order.setChargeReward(BbsTopicCharge.MODEL_CHARGE);
			   	    order.setChargeAmount(chargeAmount);
			   	    order.setPlatAmount(platAmount);
			   	    order.setAuthorAmount(authorAmount);
			   	    order.setLiveUsedNum(0);
		 			order.setLiveUserNum(0);
		 			order=orderMng.save(order);
		 			order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_SUCCESS);
		 			//礼物收益统计
		 			bbsGiftMng.afterUserPay(chargeAmount, gift);
		 			//用户礼物增加
		 			bbsGiftUserMng.addUserGift(gift.getId(), buyUser.getId(), num);
		 			//每日收益统计
		 			incomeStatisticMng.afterPay(platAmount, BbsIncomeType.gift);
		 			order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_SUCCESS);
		 		}else{
		 			order.setPrePayStatus(BbsOrder.PRE_PAY_STATUS_ORDER_AMOUNT_NOT_ENOUGH);
	   	    	}
	   	    }
   	    	
    	}
	    return order;
	}
	
	@Transactional(readOnly = true)
	public Pagination getPage(String orderNum,Integer buyUserId,Integer authorUserId,
			Short payMode,Short dataType,Integer dataId,int pageNo, int pageSize) {
		Pagination page = dao.getPage(orderNum,buyUserId,
				authorUserId,payMode,dataType,dataId,pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsOrder> getList(String orderNum,Integer buyUserId,
			Integer authorUserId,Short payMode,Short dataType,
			Integer dataId,Integer first, Integer count){
		return dao.getList(orderNum, buyUserId, authorUserId,
				payMode,dataType,dataId, first, count);
	}
	
	@Transactional(readOnly = true)
	public Pagination getPageByTopic(Integer topicId,
			Short payMode,int pageNo, int pageSize){
		return dao.getPageByTopic(topicId,payMode,pageNo,pageSize);
	}
	
	@Transactional(readOnly = true)
	public List<BbsOrder> getListByTopic(Integer topicId, Short payMode, Integer first, Integer count) {
		return dao.getListByTopic(topicId, payMode, first, count);
	}

	@Transactional(readOnly = true)
	public BbsOrder findById(Long id) {
		BbsOrder entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsOrder findByOrderNumber(String orderNumber){
		return dao.findByOrderNumber(orderNumber);
	}
	
	@Transactional(readOnly = true)
	public BbsOrder findByOutOrderNum(String orderNum,Integer payMethod){
		return dao.findByOutOrderNum(orderNum, payMethod);
	}
	
	@Transactional(readOnly = true)
	public boolean hasBuyTopic(Integer buyUserId,Integer topicId){
		BbsOrder buy=dao.find(buyUserId, topicId);
		//用户已经购买并且是收费订单非打赏订单
		if(buy!=null&&buy.getUserHasPaid()&&buy.getChargeReward()==BbsTopicCharge.MODEL_CHARGE){
			return true;
		}else{
			return false;
		}
	}

	public BbsOrder save(BbsOrder bean) {
		dao.save(bean);
		return bean;
	}

	public BbsOrder update(BbsOrder bean) {
		Updater<BbsOrder> updater = new Updater<BbsOrder>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public BbsOrder deleteById(Long id) {
		BbsOrder bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsOrder[] deleteByIds(Long[] ids) {
		BbsOrder[] beans = new BbsOrder[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public Double getWeChatOrderAmount(String outOrderNum,
			BbsConfigCharge config){
		initWeiXinPayUrl();
		Map<String, String>map=WeixinPay.weixinOrderQuery(outOrderNum,
	    			null, getWeiXinPayUrl(), config);
	    String returnCode = map.get("return_code");
	    Double orderAmount=0d;
		if(StringUtils.isNotBlank(returnCode)){
			if (returnCode.equalsIgnoreCase("SUCCESS")) {
			 if (map.get("result_code").equalsIgnoreCase(
					"SUCCESS")) {
				String trade_state = map.get("trade_state");
				//支付成功
				if(trade_state.equalsIgnoreCase("SUCCESS")){
					String total_fee= map.get("total_fee");
					Integer totalFee=Integer.parseInt(total_fee);
					if(totalFee!=0){
						orderAmount=totalFee/100.0;
					}
				}
			 }
			}
		}
		return orderAmount;
	}
	
	public Double getAliPayOrderAmount(String outOrderNum,
			BbsConfigCharge config){
		initAliPayUrl();
		AlipayTradeQueryResponse res=AliPay.query(getAliPayUrl(), config,
				null,outOrderNum);
		Double orderAmount=0d;
		if (null != res && res.isSuccess()) {
			if (res.getCode().equals("10000")) {
				if ("TRADE_SUCCESS".equalsIgnoreCase(res
						.getTradeStatus())) {
					String totalAmout=res.getTotalAmount();
					if(StringUtils.isNotBlank(totalAmout)){
						orderAmount=Double.parseDouble(totalAmout);
					}
				} 
			}
		}
		return orderAmount;
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

	private BbsOrderDao dao;
	@Autowired
	private BbsTopicMng topicMng;
	@Autowired
	private BbsTopicChargeMng chargeMng;
	@Autowired
	private BbsOrderMng orderMng;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private BbsUserAccountMng userAccountMng;
	@Autowired
	private BbsUserMng userMng;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private BbsCommonMagicMng bbsCommonMagicMng;
	@Autowired
	private BbsMagicLogMng magicLogMng;
	@Autowired
	private BbsGiftMng bbsGiftMng;
	@Autowired
	private BbsGiftUserMng bbsGiftUserMng;
	@Autowired
	private BbsIncomeStatisticMng incomeStatisticMng;
	
	@Autowired
	public void setDao(BbsOrderDao dao) {
		this.dao = dao;
	}
}