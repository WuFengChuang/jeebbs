package com.jeecms.plug.live.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_live_rate table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_live_rate"
 */

public abstract class BaseBbsLiveRate  implements Serializable {

	public static String REF = "BbsLiveRate";
	public static String PROP_RATE = "rate";
	public static String PROP_USER_NUM = "userNum";
	public static String PROP_ID = "id";


	// constructors
	public BaseBbsLiveRate () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsLiveRate (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsLiveRate (
		java.lang.Integer id,
		java.lang.Integer userNum,
		java.lang.Double rate) {

		this.setId(id);
		this.setUserNum(userNum);
		this.setRate(rate);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer userNum;
	private java.lang.Double rate;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="id"
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
	 * Return the value associated with the column: user_num
	 */
	public java.lang.Integer getUserNum () {
		return userNum;
	}

	/**
	 * Set the value related to the column: user_num
	 * @param userNum the user_num value
	 */
	public void setUserNum (java.lang.Integer userNum) {
		this.userNum = userNum;
	}


	/**
	 * Return the value associated with the column: rate
	 */
	public java.lang.Double getRate () {
		return rate;
	}

	/**
	 * Set the value related to the column: rate
	 * @param rate the rate value
	 */
	public void setRate (java.lang.Double rate) {
		this.rate = rate;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.plug.live.entity.BbsLiveRate)) return false;
		else {
			com.jeecms.plug.live.entity.BbsLiveRate bbsLiveRate = (com.jeecms.plug.live.entity.BbsLiveRate) obj;
			if (null == this.getId() || null == bbsLiveRate.getId()) return false;
			else return (this.getId().equals(bbsLiveRate.getId()));
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