package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsOrder;
import com.jeecms.common.util.DateUtils;



public class BbsOrder extends BaseBbsOrder {
	private static final long serialVersionUID = 1L;
	
	public static final Integer PAY_METHOD_WECHAT=1;
	public static final Integer PAY_METHOD_ALIPAY=2;
	
	//订单号错误
	public static final Integer PRE_PAY_STATUS_ORDER_NUM_ERROR=2;
	//订单成功
	public static final Integer PRE_PAY_STATUS_SUCCESS=1;
	//订单金额不足以购买内容
	public static final Integer PRE_PAY_STATUS_ORDER_AMOUNT_NOT_ENOUGH=3;
	/**
	 * 主题订单类型 
	 */
	public static final Short ORDER_TYPE_TOPIC=0;
	/**
	 * 道具订单类型
	 */
	public static final Short ORDER_TYPE_MAGIC=1;
	/**
	 * 礼物订单类型
	 */
	public static final Short ORDER_TYPE_GIFT=2;
	/**
	 * 活动订单类型
	 */
	public static final Short ORDER_TYPE_LIVE=3;
	/**
	 * 广告订单类型
	 */
	public static final Short ORDER_TYPE_AD=4;
	
	/**
	 * 主题订单cache 0位标识
	 */
	public static final String ORDER_TYPE_CACHE_FLAG_TOPIC="topic";
	/**
	 * 道具订单cache 0位标识
	 */
	public static final String ORDER_TYPE_CACHE_FLAG_MAGIC="magic";
	/**
	 * 礼物订单cache 0位标识
	 */
	public static final String ORDER_TYPE_CACHE_FLAG_GIFT="gift";
	/**
	 * 活动订单cache 0位标识
	 */
	public static final String ORDER_TYPE_CACHE_FLAG_LIVE="live";
	/**
	 * 广告订单cache 0位标识
	 */
	public static final String ORDER_TYPE_CACHE_FLAG_AD="ad";
	
	/**
	 * 支付目标标识-LIVE
	 */
	public static final String PAY_TARGET_LIVE="live";
	/**
	 * 支付目标标识-主题
	 */
	public static final String PAY_TARGET_TOPIC="topic";
	/**
	 * 支付目标标识-道具
	 */
	public static final String PAY_TARGET_MAGIC="magic";
	/**
	 * 支付目标标识-礼物
	 */
	public static final String PAY_TARGET_GIFT="gfit";
	/**
	 * 支付目标标识-广告
	 */
	public static final String PAY_TARGET_AD="ad";
	
	public boolean getUserHasPaid(){
		if(StringUtils.isNotBlank(getOrderNumWeixin())
				||StringUtils.isNotBlank(getOrderNumAlipay())){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean getHasLiveNotUsed(){
		if(getLiveUserNum()>getLiveUsedNum()){
			return true;
		}else{
			return false;
		}
	}
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getChargeAmount()!=null) {
			json.put("chargeAmount", getChargeAmount());
		}else{
			json.put("chargeAmount", "");
		}
		if (getBuyTime()!=null) {
			json.put("buyTime", DateUtils.parseDateToTimeStr(getBuyTime()));
		}else{
			json.put("buyTime", "");
		}
		if (StringUtils.isNotBlank(getOrderNumber())) {
			json.put("orderNumber", getOrderNumber());
		}else{
			json.put("orderNumber", "");
		}
		if (getChargeReward()!=null) {
			json.put("chargeReward", getChargeReward());
		}else{
			json.put("chargeReward", "");
		}
		if(getBuyUser()!=null&&StringUtils.isNotBlank(getBuyUser().getUsername())){
			json.put("buyUserUserName", getBuyUser().getUsername());
		}else{
			json.put("buyUserUserName", "");
		}
		if(getBuyUser()!=null&&StringUtils.isNotBlank(getBuyUser().getRealname())){
			json.put("buyUserRealName", getBuyUser().getRealname());
		}else{
			json.put("buyUserRealName", "");
		}
		if (getDataType()!=null) {
			json.put("dataType", getDataType());
		}else{
			json.put("dataType", "");
		}
		if (getDataId()!=null) {
			json.put("dataId", getDataId());
		}else{
			json.put("dataId", "");
		}
		if (getAuthorUser()!=null&&StringUtils.isNotBlank(getAuthorUser().getRealname())) {
			json.put("authorRealName", getAuthorUser().getRealname());
		}else{
			json.put("authorRealName", "");
		}
		if (getAuthorUser()!=null&&StringUtils.isNotBlank(getAuthorUser().getUsername())) {
			json.put("authorUserName", getAuthorUser().getUsername());
		}else{
			json.put("authorUserName", "");
		}
		if (getAuthorAmount()!=null) {
			json.put("authorAmount", getAuthorAmount());
		}else{
			json.put("authorAmount", "");
		}
		if (getPlatAmount()!=null) {
			json.put("platAmount", getPlatAmount());
		}else {
			json.put("platAmount", "");
		}
		if (StringUtils.isNotBlank(getOrderNumWeixin())) {
			json.put("orderNumWeixin", getOrderNumWeixin());
		}else{
			json.put("orderNumWeixin", "");
		}
		if (StringUtils.isNotBlank(getOrderNumAlipay())) {
			json.put("orderNumAlipay", getOrderNumAlipay());
		}else{
			json.put("orderNumAlipay", "");
		}
		if (getChargeReward()!=null) {
			json.put("chargeReward", getChargeReward());
		}else{
			json.put("chargeReward", "");
		}
		return json;
	}
	
	public int getPrePayStatus() {
		return prePayStatus;
	}

	public void setPrePayStatus(int prePayStatus) {
		this.prePayStatus = prePayStatus;
	}

	private int prePayStatus;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsOrder () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsOrder (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsOrder (
		java.lang.Long id,
		java.lang.String orderNumber,
		java.lang.Integer dataId,
		java.lang.Short dataType,
		java.lang.Integer authorUserId,
		java.lang.Double chargeAmount,
		java.lang.Double authorAmount,
		java.lang.Double platAmount,
		java.util.Date buyTime,
		java.lang.Short chargeReward) {

		super (
			id,
			orderNumber,
			dataId,
			dataType,
			authorUserId,
			chargeAmount,
			authorAmount,
			platAmount,
			buyTime,
			chargeReward);
	}

/*[CONSTRUCTOR MARKER END]*/


}