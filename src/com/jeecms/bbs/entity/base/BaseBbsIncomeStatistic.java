package com.jeecms.bbs.entity.base;

import java.io.Serializable;
import java.util.Date;


/**
 * This is an object that contains data related to the bbs_income_statistic table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_income_statistic"
 */

public abstract class BaseBbsIncomeStatistic  implements Serializable {

	public static String REF = "BbsIncomeStatistic";
	public static String PROP_INCOME_AMOUNT = "incomeAmount";
	public static String PROP_INCOME_DATE = "incomeDate";
	public static String PROP_INCOME_TYPE = "incomeType";
	public static String PROP_ID = "id";


	// constructors
	public BaseBbsIncomeStatistic () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsIncomeStatistic (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	

	protected void initialize () {}
	
	public BaseBbsIncomeStatistic(Double totalIncomeAmount, Double adIncomeAmount, Double magicIncomeAmount,
			Double giftIncomeAmount, Double liveIncomeAmount, Double postIncomeAmount, Date incomeDate) {
		super();
		this.totalIncomeAmount = totalIncomeAmount;
		this.adIncomeAmount = adIncomeAmount;
		this.magicIncomeAmount = magicIncomeAmount;
		this.giftIncomeAmount = giftIncomeAmount;
		this.liveIncomeAmount = liveIncomeAmount;
		this.postIncomeAmount = postIncomeAmount;
		this.incomeDate = incomeDate;
	}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Double totalIncomeAmount;
	private java.lang.Double adIncomeAmount;
	private java.lang.Double magicIncomeAmount;
	private java.lang.Double giftIncomeAmount;
	private java.lang.Double liveIncomeAmount;
	private java.lang.Double postIncomeAmount;
	private java.util.Date incomeDate;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="income_id"
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

	public java.lang.Double getTotalIncomeAmount() {
		return totalIncomeAmount;
	}

	public void setTotalIncomeAmount(java.lang.Double totalIncomeAmount) {
		this.totalIncomeAmount = totalIncomeAmount;
	}

	public java.lang.Double getAdIncomeAmount() {
		return adIncomeAmount;
	}

	public void setAdIncomeAmount(java.lang.Double adIncomeAmount) {
		this.adIncomeAmount = adIncomeAmount;
	}

	public java.lang.Double getMagicIncomeAmount() {
		return magicIncomeAmount;
	}

	public void setMagicIncomeAmount(java.lang.Double magicIncomeAmount) {
		this.magicIncomeAmount = magicIncomeAmount;
	}

	public java.lang.Double getGiftIncomeAmount() {
		return giftIncomeAmount;
	}

	public void setGiftIncomeAmount(java.lang.Double giftIncomeAmount) {
		this.giftIncomeAmount = giftIncomeAmount;
	}

	public java.lang.Double getLiveIncomeAmount() {
		return liveIncomeAmount;
	}

	public void setLiveIncomeAmount(java.lang.Double liveIncomeAmount) {
		this.liveIncomeAmount = liveIncomeAmount;
	}

	public java.lang.Double getPostIncomeAmount() {
		return postIncomeAmount;
	}

	public void setPostIncomeAmount(java.lang.Double postIncomeAmount) {
		this.postIncomeAmount = postIncomeAmount;
	}

	/**
	 * Return the value associated with the column: income_date
	 */
	public java.util.Date getIncomeDate () {
		return incomeDate;
	}

	/**
	 * Set the value related to the column: income_date
	 * @param incomeDate the income_date value
	 */
	public void setIncomeDate (java.util.Date incomeDate) {
		this.incomeDate = incomeDate;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsIncomeStatistic)) return false;
		else {
			com.jeecms.bbs.entity.BbsIncomeStatistic bbsIncomeStatistic = (com.jeecms.bbs.entity.BbsIncomeStatistic) obj;
			if (null == this.getId() || null == bbsIncomeStatistic.getId()) return false;
			else return (this.getId().equals(bbsIncomeStatistic.getId()));
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