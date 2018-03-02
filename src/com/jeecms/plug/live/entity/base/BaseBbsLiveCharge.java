package com.jeecms.plug.live.entity.base;

import java.io.Serializable;

import com.jeecms.plug.live.entity.BbsLive;


/**
 * This is an object that contains data related to the bbs_live_charge table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_live_charge"
 */

public abstract class BaseBbsLiveCharge  implements Serializable {

	public static String REF = "BbsLiveCharge";
	public static String PROP_MONTH_AMOUNT = "monthAmount";
	public static String PROP_DAY_AMOUNT = "dayAmount";
	public static String PROP_YEAR_AMOUNT = "yearAmount";
	public static String PROP_TOTAL_AMOUNT = "totalAmount";
	public static String PROP_ID = "id";
	public static String PROP_LAST_BUY_TIME = "lastBuyTime";


	// constructors
	public BaseBbsLiveCharge () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsLiveCharge (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsLiveCharge (
		java.lang.Integer id,
		java.lang.Double totalAmount,
		java.lang.Double yearAmount,
		java.lang.Double monthAmount,
		java.lang.Double dayAmount) {

		this.setId(id);
		this.setTotalAmount(totalAmount);
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
	private java.lang.Double yearAmount;
	private java.lang.Double monthAmount;
	private java.lang.Double dayAmount;
	private java.util.Date lastBuyTime;
	private java.lang.Integer ticketNum;
	private java.lang.Integer giftNum;
	private BbsLive live;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="live_id"
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

	public BbsLive getLive() {
		return live;
	}

	public void setLive(BbsLive live) {
		this.live = live;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.plug.live.entity.BbsLiveCharge)) return false;
		else {
			com.jeecms.plug.live.entity.BbsLiveCharge bbsLiveCharge = (com.jeecms.plug.live.entity.BbsLiveCharge) obj;
			if (null == this.getId() || null == bbsLiveCharge.getId()) return false;
			else return (this.getId().equals(bbsLiveCharge.getId()));
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