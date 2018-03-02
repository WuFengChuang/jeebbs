package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_config_charge table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_config_charge"
 */

public abstract class BaseBbsConfigCharge  implements Serializable {

	public static String REF = "BbsConfigCharge";
	public static String PROP_REWARD_MIN = "rewardMin";
	public static String PROP_WEIXIN_SECRET = "weixinSecret";
	public static String PROP_TRANSFER_API_PASSWORD = "transferApiPassword";
	public static String PROP_WEIXIN_APPID = "weixinAppid";
	public static String PROP_CHARGE_RATIO = "chargeRatio";
	public static String PROP_ALIPAY_PUBLIC_KEY = "alipayPublicKey";
	public static String PROP_COMMISSION_DAY = "commissionDay";
	public static String PROP_ALIPAY_PRIVATE_KEY = "alipayPrivateKey";
	public static String PROP_ALIPAY_ACCOUNT = "alipayAccount";
	public static String PROP_ALIPAY_KEY = "alipayKey";
	public static String PROP_LAST_BUY_TIME = "lastBuyTime";
	public static String PROP_REWARD_PATTERN = "rewardPattern";
	public static String PROP_WEIXIN_PASSWORD = "weixinPassword";
	public static String PROP_MIN_DRAW_AMOUNT = "minDrawAmount";
	public static String PROP_ALIPAY_APPID = "alipayAppid";
	public static String PROP_PAY_TRANSFER_PASSWORD = "payTransferPassword";
	public static String PROP_COMMISSION_MONTH = "commissionMonth";
	public static String PROP_REWARD_MAX = "rewardMax";
	public static String PROP_COMMISSION_YEAR = "commissionYear";
	public static String PROP_ID = "id";
	public static String PROP_COMMISSION_TOTAL = "commissionTotal";
	public static String PROP_WEIXIN_ACCOUNT = "weixinAccount";
	public static String PROP_ALIPAY_PARTNER_ID = "alipayPartnerId";


	// constructors
	public BaseBbsConfigCharge () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsConfigCharge (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsConfigCharge (
		java.lang.Integer id,
		java.lang.String weixinAppid,
		java.lang.String weixinSecret,
		java.lang.String weixinAccount,
		java.lang.String weixinPassword,
		java.lang.Double chargeRatio,
		java.lang.Double minDrawAmount,
		java.lang.Double commissionTotal,
		java.lang.Double commissionYear,
		java.lang.Double commissionMonth,
		java.lang.Double commissionDay,
		java.lang.String payTransferPassword,
		java.lang.String transferApiPassword,
		java.lang.Double rewardMin,
		java.lang.Double rewardMax,
		java.lang.Boolean rewardPattern) {

		this.setId(id);
		this.setWeixinAppId(weixinAppId);
		this.setWeixinSecret(weixinSecret);
		this.setWeixinAccount(weixinAccount);
		this.setWeixinPassword(weixinPassword);
		this.setChargeRatio(chargeRatio);
		this.setMinDrawAmount(minDrawAmount);
		this.setCommissionTotal(commissionTotal);
		this.setCommissionYear(commissionYear);
		this.setCommissionMonth(commissionMonth);
		this.setCommissionDay(commissionDay);
		this.setPayTransferPassword(payTransferPassword);
		this.setTransferApiPassword(transferApiPassword);
		this.setRewardMin(rewardMin);
		this.setRewardMax(rewardMax);
		this.setRewardPattern(rewardPattern);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String weixinAppId;
	private java.lang.String weixinSecret;
	private java.lang.String weixinAccount;
	private java.lang.String weixinPassword;
	private java.lang.String alipayPartnerId;
	private java.lang.String alipayAccount;
	private java.lang.String alipayKey;
	private java.lang.String alipayAppId;
	private java.lang.String alipayPublicKey;
	private java.lang.String alipayPrivateKey;
	private java.lang.Double chargeRatio;
	private java.lang.Double minDrawAmount;
	private java.lang.Double commissionTotal;
	private java.lang.Double commissionYear;
	private java.lang.Double commissionMonth;
	private java.lang.Double commissionDay;
	private java.util.Date lastBuyTime;
	private java.lang.String payTransferPassword;
	private java.lang.String transferApiPassword;
	private java.lang.Double rewardMin;
	private java.lang.Double rewardMax;
	private java.lang.Boolean rewardPattern;
	private java.lang.Double giftChargeRatio;
	private java.lang.Double profitMagicTotal;
	private java.lang.Double profitGiftTotal;
	private java.lang.Double profitPostTotal;
	private java.lang.Double profitLiveTotal;
	private java.lang.Double profitAdTotal;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="assigned"
     *  column="config_id"
     */
	public java.lang.Integer getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: weixin_appid
	 */
	public java.lang.String getWeixinAppId () {
		return weixinAppId;
	}

	/**
	 * Set the value related to the column: weixin_appid
	 * @param weixinAppid the weixin_appid value
	 */
	public void setWeixinAppId (java.lang.String weixinAppId) {
		this.weixinAppId = weixinAppId;
	}


	/**
	 * Return the value associated with the column: weixin_secret
	 */
	public java.lang.String getWeixinSecret () {
		return weixinSecret;
	}

	/**
	 * Set the value related to the column: weixin_secret
	 * @param weixinSecret the weixin_secret value
	 */
	public void setWeixinSecret (java.lang.String weixinSecret) {
		this.weixinSecret = weixinSecret;
	}


	/**
	 * Return the value associated with the column: weixin_account
	 */
	public java.lang.String getWeixinAccount () {
		return weixinAccount;
	}

	/**
	 * Set the value related to the column: weixin_account
	 * @param weixinAccount the weixin_account value
	 */
	public void setWeixinAccount (java.lang.String weixinAccount) {
		this.weixinAccount = weixinAccount;
	}


	/**
	 * Return the value associated with the column: weixin_password
	 */
	public java.lang.String getWeixinPassword () {
		return weixinPassword;
	}

	/**
	 * Set the value related to the column: weixin_password
	 * @param weixinPassword the weixin_password value
	 */
	public void setWeixinPassword (java.lang.String weixinPassword) {
		this.weixinPassword = weixinPassword;
	}


	/**
	 * Return the value associated with the column: alipay_partner_id
	 */
	public java.lang.String getAlipayPartnerId () {
		return alipayPartnerId;
	}

	/**
	 * Set the value related to the column: alipay_partner_id
	 * @param alipayPartnerId the alipay_partner_id value
	 */
	public void setAlipayPartnerId (java.lang.String alipayPartnerId) {
		this.alipayPartnerId = alipayPartnerId;
	}


	/**
	 * Return the value associated with the column: alipay_account
	 */
	public java.lang.String getAlipayAccount () {
		return alipayAccount;
	}

	/**
	 * Set the value related to the column: alipay_account
	 * @param alipayAccount the alipay_account value
	 */
	public void setAlipayAccount (java.lang.String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}


	/**
	 * Return the value associated with the column: alipay_key
	 */
	public java.lang.String getAlipayKey () {
		return alipayKey;
	}

	/**
	 * Set the value related to the column: alipay_key
	 * @param alipayKey the alipay_key value
	 */
	public void setAlipayKey (java.lang.String alipayKey) {
		this.alipayKey = alipayKey;
	}


	/**
	 * Return the value associated with the column: alipay_appid
	 */
	public java.lang.String getAlipayAppId () {
		return alipayAppId;
	}

	/**
	 * Set the value related to the column: alipay_appid
	 * @param alipayAppid the alipay_appid value
	 */
	public void setAlipayAppId (java.lang.String alipayAppId) {
		this.alipayAppId = alipayAppId;
	}


	/**
	 * Return the value associated with the column: alipay_public_key
	 */
	public java.lang.String getAlipayPublicKey () {
		return alipayPublicKey;
	}

	/**
	 * Set the value related to the column: alipay_public_key
	 * @param alipayPublicKey the alipay_public_key value
	 */
	public void setAlipayPublicKey (java.lang.String alipayPublicKey) {
		this.alipayPublicKey = alipayPublicKey;
	}


	/**
	 * Return the value associated with the column: alipay_private_key
	 */
	public java.lang.String getAlipayPrivateKey () {
		return alipayPrivateKey;
	}

	/**
	 * Set the value related to the column: alipay_private_key
	 * @param alipayPrivateKey the alipay_private_key value
	 */
	public void setAlipayPrivateKey (java.lang.String alipayPrivateKey) {
		this.alipayPrivateKey = alipayPrivateKey;
	}


	/**
	 * Return the value associated with the column: charge_ratio
	 */
	public java.lang.Double getChargeRatio () {
		return chargeRatio;
	}

	/**
	 * Set the value related to the column: charge_ratio
	 * @param chargeRatio the charge_ratio value
	 */
	public void setChargeRatio (java.lang.Double chargeRatio) {
		this.chargeRatio = chargeRatio;
	}


	/**
	 * Return the value associated with the column: min_draw_amount
	 */
	public java.lang.Double getMinDrawAmount () {
		return minDrawAmount;
	}

	/**
	 * Set the value related to the column: min_draw_amount
	 * @param minDrawAmount the min_draw_amount value
	 */
	public void setMinDrawAmount (java.lang.Double minDrawAmount) {
		this.minDrawAmount = minDrawAmount;
	}


	/**
	 * Return the value associated with the column: commission_total
	 */
	public java.lang.Double getCommissionTotal () {
		return commissionTotal;
	}

	/**
	 * Set the value related to the column: commission_total
	 * @param commissionTotal the commission_total value
	 */
	public void setCommissionTotal (java.lang.Double commissionTotal) {
		this.commissionTotal = commissionTotal;
	}


	/**
	 * Return the value associated with the column: commission_year
	 */
	public java.lang.Double getCommissionYear () {
		return commissionYear;
	}

	/**
	 * Set the value related to the column: commission_year
	 * @param commissionYear the commission_year value
	 */
	public void setCommissionYear (java.lang.Double commissionYear) {
		this.commissionYear = commissionYear;
	}


	/**
	 * Return the value associated with the column: commission_month
	 */
	public java.lang.Double getCommissionMonth () {
		return commissionMonth;
	}

	/**
	 * Set the value related to the column: commission_month
	 * @param commissionMonth the commission_month value
	 */
	public void setCommissionMonth (java.lang.Double commissionMonth) {
		this.commissionMonth = commissionMonth;
	}


	/**
	 * Return the value associated with the column: commission_day
	 */
	public java.lang.Double getCommissionDay () {
		return commissionDay;
	}

	/**
	 * Set the value related to the column: commission_day
	 * @param commissionDay the commission_day value
	 */
	public void setCommissionDay (java.lang.Double commissionDay) {
		this.commissionDay = commissionDay;
	}


	/**
	 * Return the value associated with the column: last_buy_time
	 */
	public java.util.Date getLastBuyTime () {
		return lastBuyTime;
	}

	/**
	 * Set the value related to the column: last_buy_time
	 * @param lastBuyTime the last_buy_time value
	 */
	public void setLastBuyTime (java.util.Date lastBuyTime) {
		this.lastBuyTime = lastBuyTime;
	}


	/**
	 * Return the value associated with the column: pay_transfer_password
	 */
	public java.lang.String getPayTransferPassword () {
		return payTransferPassword;
	}

	/**
	 * Set the value related to the column: pay_transfer_password
	 * @param payTransferPassword the pay_transfer_password value
	 */
	public void setPayTransferPassword (java.lang.String payTransferPassword) {
		this.payTransferPassword = payTransferPassword;
	}


	/**
	 * Return the value associated with the column: transfer_api_password
	 */
	public java.lang.String getTransferApiPassword () {
		return transferApiPassword;
	}

	/**
	 * Set the value related to the column: transfer_api_password
	 * @param transferApiPassword the transfer_api_password value
	 */
	public void setTransferApiPassword (java.lang.String transferApiPassword) {
		this.transferApiPassword = transferApiPassword;
	}


	/**
	 * Return the value associated with the column: reward_min
	 */
	public java.lang.Double getRewardMin () {
		return rewardMin;
	}

	/**
	 * Set the value related to the column: reward_min
	 * @param rewardMin the reward_min value
	 */
	public void setRewardMin (java.lang.Double rewardMin) {
		this.rewardMin = rewardMin;
	}


	/**
	 * Return the value associated with the column: reward_max
	 */
	public java.lang.Double getRewardMax () {
		return rewardMax;
	}

	/**
	 * Set the value related to the column: reward_max
	 * @param rewardMax the reward_max value
	 */
	public void setRewardMax (java.lang.Double rewardMax) {
		this.rewardMax = rewardMax;
	}


	/**
	 * Return the value associated with the column: reward_pattern
	 */
	public java.lang.Boolean getRewardPattern () {
		return rewardPattern;
	}

	/**
	 * Set the value related to the column: reward_pattern
	 * @param rewardPattern the reward_pattern value
	 */
	public void setRewardPattern (java.lang.Boolean rewardPattern) {
		this.rewardPattern = rewardPattern;
	}

	public java.lang.Double getGiftChargeRatio() {
		return giftChargeRatio;
	}

	public void setGiftChargeRatio(java.lang.Double giftChargeRatio) {
		this.giftChargeRatio = giftChargeRatio;
	}
	
	public java.lang.Double getProfitMagicTotal() {
		return profitMagicTotal;
	}

	public void setProfitMagicTotal(java.lang.Double profitMagicTotal) {
		this.profitMagicTotal = profitMagicTotal;
	}

	public java.lang.Double getProfitGiftTotal() {
		return profitGiftTotal;
	}

	public void setProfitGiftTotal(java.lang.Double profitGiftTotal) {
		this.profitGiftTotal = profitGiftTotal;
	}

	public java.lang.Double getProfitPostTotal() {
		return profitPostTotal;
	}

	public void setProfitPostTotal(java.lang.Double profitPostTotal) {
		this.profitPostTotal = profitPostTotal;
	}

	public java.lang.Double getProfitLiveTotal() {
		return profitLiveTotal;
	}

	public void setProfitLiveTotal(java.lang.Double profitLiveTotal) {
		this.profitLiveTotal = profitLiveTotal;
	}

	public java.lang.Double getProfitAdTotal() {
		return profitAdTotal;
	}

	public void setProfitAdTotal(java.lang.Double profitAdTotal) {
		this.profitAdTotal = profitAdTotal;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsConfigCharge)) return false;
		else {
			com.jeecms.bbs.entity.BbsConfigCharge bbsConfigCharge = (com.jeecms.bbs.entity.BbsConfigCharge) obj;
			if (null == this.getId() || null == bbsConfigCharge.getId()) return false;
			else return (this.getId().equals(bbsConfigCharge.getId()));
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