package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_order table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_order"
 */

public abstract class BaseBbsOrder  implements Serializable {

	public static String REF = "BbsOrder";
	public static String PROP_BUY_TIME = "buyTime";
	public static String PROP_BUY_USER = "buyUser";
	public static String PROP_ORDER_NUM_WEIXIN = "orderNumWeixin";
	public static String PROP_ORDER_NUMBER = "orderNumber";
	public static String PROP_CHARGE_AMOUNT = "chargeAmount";
	public static String PROP_PLAT_AMOUNT = "platAmount";
	public static String PROP_DATA_ID = "dataId";
	public static String PROP_ORDER_NUM_ALIPAY = "orderNumAlipay";
	public static String PROP_CHARGE_REWARD = "chargeReward";
	public static String PROP_DATA_TYPE = "dataType";
	public static String PROP_ID = "id";
	public static String PROP_AUTHOR_USER_ID = "authorUserId";
	public static String PROP_AUTHOR_AMOUNT = "authorAmount";


	// constructors
	public BaseBbsOrder () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsOrder (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsOrder (
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

		this.setId(id);
		this.setOrderNumber(orderNumber);
		this.setDataId(dataId);
		this.setDataType(dataType);
		this.setChargeAmount(chargeAmount);
		this.setAuthorAmount(authorAmount);
		this.setPlatAmount(platAmount);
		this.setBuyTime(buyTime);
		this.setChargeReward(chargeReward);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String orderNumber;
	private java.lang.Integer dataId;
	private java.lang.Short dataType;
	private java.lang.Double chargeAmount;
	private java.lang.Double authorAmount;
	private java.lang.Double platAmount;
	private java.util.Date buyTime;
	private java.lang.String orderNumWeixin;
	private java.lang.String orderNumAlipay;
	private java.lang.Short chargeReward;
	private Integer liveUserNum;
	private Integer liveUsedNum;

	// many to one
	private com.jeecms.bbs.entity.BbsUser buyUser;
	private com.jeecms.bbs.entity.BbsUser authorUser;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="order_id"
     */
	public java.lang.Long getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Long id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: order_number
	 */
	public java.lang.String getOrderNumber () {
		return orderNumber;
	}

	/**
	 * Set the value related to the column: order_number
	 * @param orderNumber the order_number value
	 */
	public void setOrderNumber (java.lang.String orderNumber) {
		this.orderNumber = orderNumber;
	}


	/**
	 * Return the value associated with the column: data_id
	 */
	public java.lang.Integer getDataId () {
		return dataId;
	}

	/**
	 * Set the value related to the column: data_id
	 * @param dataId the data_id value
	 */
	public void setDataId (java.lang.Integer dataId) {
		this.dataId = dataId;
	}


	/**
	 * Return the value associated with the column: data_type
	 */
	public java.lang.Short getDataType () {
		return dataType;
	}

	/**
	 * Set the value related to the column: data_type
	 * @param dataType the data_type value
	 */
	public void setDataType (java.lang.Short dataType) {
		this.dataType = dataType;
	}

	/**
	 * Return the value associated with the column: charge_amount
	 */
	public java.lang.Double getChargeAmount () {
		return chargeAmount;
	}

	/**
	 * Set the value related to the column: charge_amount
	 * @param chargeAmount the charge_amount value
	 */
	public void setChargeAmount (java.lang.Double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}


	/**
	 * Return the value associated with the column: author_amount
	 */
	public java.lang.Double getAuthorAmount () {
		return authorAmount;
	}

	/**
	 * Set the value related to the column: author_amount
	 * @param authorAmount the author_amount value
	 */
	public void setAuthorAmount (java.lang.Double authorAmount) {
		this.authorAmount = authorAmount;
	}


	/**
	 * Return the value associated with the column: plat_amount
	 */
	public java.lang.Double getPlatAmount () {
		return platAmount;
	}

	/**
	 * Set the value related to the column: plat_amount
	 * @param platAmount the plat_amount value
	 */
	public void setPlatAmount (java.lang.Double platAmount) {
		this.platAmount = platAmount;
	}


	/**
	 * Return the value associated with the column: buy_time
	 */
	public java.util.Date getBuyTime () {
		return buyTime;
	}

	/**
	 * Set the value related to the column: buy_time
	 * @param buyTime the buy_time value
	 */
	public void setBuyTime (java.util.Date buyTime) {
		this.buyTime = buyTime;
	}


	/**
	 * Return the value associated with the column: order_num_weixin
	 */
	public java.lang.String getOrderNumWeixin () {
		return orderNumWeixin;
	}

	/**
	 * Set the value related to the column: order_num_weixin
	 * @param orderNumWeixin the order_num_weixin value
	 */
	public void setOrderNumWeixin (java.lang.String orderNumWeixin) {
		this.orderNumWeixin = orderNumWeixin;
	}


	/**
	 * Return the value associated with the column: order_num_alipay
	 */
	public java.lang.String getOrderNumAlipay () {
		return orderNumAlipay;
	}

	/**
	 * Set the value related to the column: order_num_alipay
	 * @param orderNumAlipay the order_num_alipay value
	 */
	public void setOrderNumAlipay (java.lang.String orderNumAlipay) {
		this.orderNumAlipay = orderNumAlipay;
	}


	/**
	 * Return the value associated with the column: charge_reward
	 */
	public java.lang.Short getChargeReward () {
		return chargeReward;
	}

	/**
	 * Set the value related to the column: charge_reward
	 * @param chargeReward the charge_reward value
	 */
	public void setChargeReward (java.lang.Short chargeReward) {
		this.chargeReward = chargeReward;
	}


	public Integer getLiveUserNum() {
		return liveUserNum;
	}

	public void setLiveUserNum(Integer liveUserNum) {
		this.liveUserNum = liveUserNum;
	}

	public Integer getLiveUsedNum() {
		return liveUsedNum;
	}

	public void setLiveUsedNum(Integer liveUsedNum) {
		this.liveUsedNum = liveUsedNum;
	}

	/**
	 * Return the value associated with the column: buy_user_id
	 */
	public com.jeecms.bbs.entity.BbsUser getBuyUser () {
		return buyUser;
	}

	/**
	 * Set the value related to the column: buy_user_id
	 * @param buyUser the buy_user_id value
	 */
	public void setBuyUser (com.jeecms.bbs.entity.BbsUser buyUser) {
		this.buyUser = buyUser;
	}

	public com.jeecms.bbs.entity.BbsUser getAuthorUser() {
		return authorUser;
	}

	public void setAuthorUser(com.jeecms.bbs.entity.BbsUser authorUser) {
		this.authorUser = authorUser;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsOrder)) return false;
		else {
			com.jeecms.bbs.entity.BbsOrder bbsOrder = (com.jeecms.bbs.entity.BbsOrder) obj;
			if (null == this.getId() || null == bbsOrder.getId()) return false;
			else return (this.getId().equals(bbsOrder.getId()));
		}
	}

	public int hashCode () {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}


	public String toString () {
		return super.toString();
	}


}