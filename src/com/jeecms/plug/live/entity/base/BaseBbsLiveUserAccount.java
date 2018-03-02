package com.jeecms.plug.live.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_live_user_account table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_live_user_account"
 */

public abstract class BaseBbsLiveUserAccount  implements Serializable {

	public static String REF = "BbsLiveUserAccount";
	public static String PROP_MONTH_AMOUNT = "monthAmount";
	public static String PROP_USER = "user";
	public static String PROP_DAY_AMOUNT = "dayAmount";
	public static String PROP_YEAR_AMOUNT = "yearAmount";
	public static String PROP_TOTAL_AMOUNT = "totalAmount";
	public static String PROP_BUY_COUNT = "buyCount";
	public static String PROP_DRAW_COUNT = "drawCount";
	public static String PROP_LAST_BUY_TIME = "lastBuyTime";
	public static String PROP_LAST_DRAW_TIME = "lastDrawTime";
	public static String PROP_NO_PAY_AMOUNT = "noPayAmount";
	public static String PROP_ACCOUNT_ALIPY = "accountAlipy";
	public static String PROP_ACCOUNT_WEIXIN_OPENID = "accountWeixinOpenid";
	public static String PROP_ID = "id";
	public static String PROP_ACCOUNT_WEIXIN = "accountWeixin";


	// constructors
	public BaseBbsLiveUserAccount () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsLiveUserAccount (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsLiveUserAccount (
		java.lang.Integer id,
		java.lang.Double yearAmount,
		java.lang.Double monthAmount,
		java.lang.Double dayAmount) {

		this.setId(id);
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
	private java.lang.Double totalAmount;
	private java.lang.Double noPayAmount;
	private java.lang.Double yearAmount;
	private java.lang.Double monthAmount;
	private java.lang.Double dayAmount;
	private java.lang.Integer buyCount;
	private java.lang.Integer drawCount;
	private java.util.Date lastDrawTime;
	private java.util.Date lastBuyTime;
	private java.lang.Integer ticketNum;
	private java.lang.Integer giftNum;
	private java.lang.Integer topPriority;
	private java.util.Date checkTime;

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
	 * Return the value associated with the column: total_amount
	 */
	public java.lang.Double getTotalAmount () {
		return totalAmount;
	}

	/**
	 * Set the value related to the column: total_amount
	 * @param totalAmount the total_amount value
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

	public java.lang.Integer getTicketNum() {
		return ticketNum;
	}

	public void setTicketNum(java.lang.Integer ticketNum) {
		this.ticketNum = ticketNum;
	}

	public java.lang.Integer getGiftNum() {
		return giftNum;
	}

	public void setGiftNum(java.lang.Integer giftNum) {
		this.giftNum = giftNum;
	}

	public java.lang.Integer getTopPriority() {
		return topPriority;
	}

	public void setTopPriority(java.lang.Integer topPriority) {
		this.topPriority = topPriority;
	}

	
	public java.util.Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(java.util.Date checkTime) {
		this.checkTime = checkTime;
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
		if (!(obj instanceof com.jeecms.plug.live.entity.BbsLiveUserAccount)) return false;
		else {
			com.jeecms.plug.live.entity.BbsLiveUserAccount bbsLiveUserAccount = (com.jeecms.plug.live.entity.BbsLiveUserAccount) obj;
			if (null == this.getId() || null == bbsLiveUserAccount.getId()) return false;
			else return (this.getId().equals(bbsLiveUserAccount.getId()));
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