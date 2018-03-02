package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_user_account table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_user_account"
 */

public abstract class BaseBbsUserAccount  implements Serializable {

	public static String REF = "BbsUserAccount";
	public static String PROP_MONTH_AMOUNT = "monthAmount";
	public static String PROP_USER = "user";
	public static String PROP_DAY_AMOUNT = "dayAmount";
	public static String PROP_YEAR_AMOUNT = "yearAmount";
	public static String PROP_BUY_COUNT = "buyCount";
	public static String PROP_DRAW_COUNT = "drawCount";
	public static String PROP_LAST_BUY_TIME = "lastBuyTime";
	public static String PROP_DRAW_ACCOUNT = "drawAccount";
	public static String PROP_LAST_DRAW_TIME = "lastDrawTime";
	public static String PROP_NO_PAY_AMOUNT = "noPayAmount";
	public static String PROP_ACCOUNT_ALIPY = "accountAlipy";
	public static String PROP_OTAL_AMOUNT = "otalAmount";
	public static String PROP_ACCOUNT_WEIXIN_OPENID = "accountWeixinOpenid";
	public static String PROP_ID = "id";
	public static String PROP_ACCOUNT_WEIXIN = "accountWeixin";


	// constructors
	public BaseBbsUserAccount () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsUserAccount (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsUserAccount (
		java.lang.Integer id,
		java.lang.Short drawAccount,
		java.lang.Double yearAmount,
		java.lang.Double monthAmount,
		java.lang.Double dayAmount) {

		this.setId(id);
		this.setDrawAccount(drawAccount);
		this.setYearAmount(yearAmount);
		this.setMonthAmount(monthAmount);
		this.setDayAmount(dayAmount);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String accountWeixin;
	private java.lang.String accountWeixinOpenId;
	private java.lang.String accountAlipy;
	private java.lang.Short drawAccount;
	private java.lang.Double totalAmount;
	private java.lang.Double noPayAmount;
	private java.lang.Double yearAmount;
	private java.lang.Double monthAmount;
	private java.lang.Double dayAmount;
	private java.lang.Integer buyCount;
	private java.lang.Integer drawCount;
	private java.util.Date lastDrawTime;
	private java.util.Date lastBuyTime;
	
	private java.lang.Double giftTotalAmount;
	private java.lang.Double giftNoDrawAmount;
	private java.lang.Double giftMonthAmount;
	private java.lang.Double giftDayAmount;
	private java.lang.Integer giftReceiverCount;
	private java.lang.Integer giftDrawCount;
	private java.util.Date giftLastReceiverTime;
	private java.lang.Double adAccountMount;
	private java.lang.Double adAccountMountTotal;
	

	// one to one
	private com.jeecms.bbs.entity.BbsUser user;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="foreign"
     *  column="user_id"
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
	 * Return the value associated with the column: account_weixin
	 */
	public java.lang.String getAccountWeixin () {
		return accountWeixin;
	}

	/**
	 * Set the value related to the column: account_weixin
	 * @param accountWeixin the account_weixin value
	 */
	public void setAccountWeixin (java.lang.String accountWeixin) {
		this.accountWeixin = accountWeixin;
	}


	/**
	 * Return the value associated with the column: account_weixin_openId
	 */
	public java.lang.String getAccountWeixinOpenId () {
		return accountWeixinOpenId;
	}

	/**
	 * Set the value related to the column: account_weixin_openId
	 * @param accountWeixinOpenid the account_weixin_openId value
	 */
	public void setAccountWeixinOpenId (java.lang.String accountWeixinOpenId) {
		this.accountWeixinOpenId = accountWeixinOpenId;
	}


	/**
	 * Return the value associated with the column: account_alipy
	 */
	public java.lang.String getAccountAlipy () {
		return accountAlipy;
	}

	/**
	 * Set the value related to the column: account_alipy
	 * @param accountAlipy the account_alipy value
	 */
	public void setAccountAlipy (java.lang.String accountAlipy) {
		this.accountAlipy = accountAlipy;
	}


	/**
	 * Return the value associated with the column: draw_account
	 */
	public java.lang.Short getDrawAccount () {
		return drawAccount;
	}

	/**
	 * Set the value related to the column: draw_account
	 * @param drawAccount the draw_account value
	 */
	public void setDrawAccount (java.lang.Short drawAccount) {
		this.drawAccount = drawAccount;
	}


	/**
	 * Return the value associated with the column: total_amount
	 */
	public java.lang.Double getTotalAmount () {
		return totalAmount;
	}

	/**
	 * Set the value related to the column: total_amount
	 * @param otalAmount the total_amount value
	 */
	public void setTotalAmount (java.lang.Double totalAmount) {
		this.totalAmount = totalAmount;
	}


	/**
	 * Return the value associated with the column: no_pay_amount
	 */
	public java.lang.Double getNoPayAmount () {
		return noPayAmount;
	}

	/**
	 * Set the value related to the column: no_pay_amount
	 * @param noPayAmount the no_pay_amount value
	 */
	public void setNoPayAmount (java.lang.Double noPayAmount) {
		this.noPayAmount = noPayAmount;
	}


	/**
	 * Return the value associated with the column: year_amount
	 */
	public java.lang.Double getYearAmount () {
		return yearAmount;
	}

	/**
	 * Set the value related to the column: year_amount
	 * @param yearAmount the year_amount value
	 */
	public void setYearAmount (java.lang.Double yearAmount) {
		this.yearAmount = yearAmount;
	}


	/**
	 * Return the value associated with the column: month_amount
	 */
	public java.lang.Double getMonthAmount () {
		return monthAmount;
	}

	/**
	 * Set the value related to the column: month_amount
	 * @param monthAmount the month_amount value
	 */
	public void setMonthAmount (java.lang.Double monthAmount) {
		this.monthAmount = monthAmount;
	}


	/**
	 * Return the value associated with the column: day_amount
	 */
	public java.lang.Double getDayAmount () {
		return dayAmount;
	}

	/**
	 * Set the value related to the column: day_amount
	 * @param dayAmount the day_amount value
	 */
	public void setDayAmount (java.lang.Double dayAmount) {
		this.dayAmount = dayAmount;
	}


	/**
	 * Return the value associated with the column: buy_count
	 */
	public java.lang.Integer getBuyCount () {
		return buyCount;
	}

	/**
	 * Set the value related to the column: buy_count
	 * @param buyCount the buy_count value
	 */
	public void setBuyCount (java.lang.Integer buyCount) {
		this.buyCount = buyCount;
	}


	/**
	 * Return the value associated with the column: draw_count
	 */
	public java.lang.Integer getDrawCount () {
		return drawCount;
	}

	/**
	 * Set the value related to the column: draw_count
	 * @param drawCount the draw_count value
	 */
	public void setDrawCount (java.lang.Integer drawCount) {
		this.drawCount = drawCount;
	}


	/**
	 * Return the value associated with the column: last_draw_time
	 */
	public java.util.Date getLastDrawTime () {
		return lastDrawTime;
	}

	/**
	 * Set the value related to the column: last_draw_time
	 * @param lastDrawTime the last_draw_time value
	 */
	public void setLastDrawTime (java.util.Date lastDrawTime) {
		this.lastDrawTime = lastDrawTime;
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

	public java.lang.Double getGiftTotalAmount() {
		return giftTotalAmount;
	}

	public void setGiftTotalAmount(java.lang.Double giftTotalAmount) {
		this.giftTotalAmount = giftTotalAmount;
	}

	public java.lang.Double getGiftNoDrawAmount() {
		return giftNoDrawAmount;
	}

	public void setGiftNoDrawAmount(java.lang.Double giftNoDrawAmount) {
		this.giftNoDrawAmount = giftNoDrawAmount;
	}

	public java.lang.Double getGiftMonthAmount() {
		return giftMonthAmount;
	}

	public void setGiftMonthAmount(java.lang.Double giftMonthAmount) {
		this.giftMonthAmount = giftMonthAmount;
	}

	public java.lang.Double getGiftDayAmount() {
		return giftDayAmount;
	}

	public void setGiftDayAmount(java.lang.Double giftDayAmount) {
		this.giftDayAmount = giftDayAmount;
	}

	public java.lang.Integer getGiftReceiverCount() {
		return giftReceiverCount;
	}

	public void setGiftReceiverCount(java.lang.Integer giftReceiverCount) {
		this.giftReceiverCount = giftReceiverCount;
	}

	public java.lang.Integer getGiftDrawCount() {
		return giftDrawCount;
	}

	public void setGiftDrawCount(java.lang.Integer giftDrawCount) {
		this.giftDrawCount = giftDrawCount;
	}

	public java.util.Date getGiftLastReceiverTime() {
		return giftLastReceiverTime;
	}

	public void setGiftLastReceiverTime(java.util.Date giftLastReceiverTime) {
		this.giftLastReceiverTime = giftLastReceiverTime;
	}

	public java.lang.Double getAdAccountMount() {
		return adAccountMount;
	}

	public void setAdAccountMount(java.lang.Double adAccountMount) {
		this.adAccountMount = adAccountMount;
	}
	
	public java.lang.Double getAdAccountMountTotal() {
		return adAccountMountTotal;
	}

	public void setAdAccountMountTotal(java.lang.Double adAccountMountTotal) {
		this.adAccountMountTotal = adAccountMountTotal;
	}

	/**
	 * Return the value associated with the column: user
	 */
	public com.jeecms.bbs.entity.BbsUser getUser () {
		return user;
	}

	/**
	 * Set the value related to the column: user
	 * @param user the user value
	 */
	public void setUser (com.jeecms.bbs.entity.BbsUser user) {
		this.user = user;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsUserAccount)) return false;
		else {
			com.jeecms.bbs.entity.BbsUserAccount bbsUserAccount = (com.jeecms.bbs.entity.BbsUserAccount) obj;
			if (null == this.getId() || null == bbsUserAccount.getId()) return false;
			else return (this.getId().equals(bbsUserAccount.getId()));
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