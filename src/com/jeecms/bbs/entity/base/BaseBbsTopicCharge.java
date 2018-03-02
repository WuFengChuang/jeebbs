package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_topic_charge table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_topic_charge"
 */

public abstract class BaseBbsTopicCharge  implements Serializable {

	public static String REF = "BbsTopicCharge";
	public static String PROP_REWARD_PATTERN = "rewardPattern";
	public static String PROP_MONTH_AMOUNT = "monthAmount";
	public static String PROP_DAY_AMOUNT = "dayAmount";
	public static String PROP_REWARD_RANDOM_MAX = "rewardRandomMax";
	public static String PROP_CHARGE_REWARD = "chargeReward";
	public static String PROP_REWARD_RANDOM_MIN = "rewardRandomMin";
	public static String PROP_YEAR_AMOUNT = "yearAmount";
	public static String PROP_TOTAL_AMOUNT = "totalAmount";
	public static String PROP_ID = "id";
	public static String PROP_TOPIC = "topic";
	public static String PROP_CHARGE_AMOUNT = "chargeAmount";
	public static String PROP_LAST_BUY_TIME = "lastBuyTime";


	// constructors
	public BaseBbsTopicCharge () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsTopicCharge (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsTopicCharge (
		java.lang.Integer id,
		java.lang.Double chargeAmount,
		java.lang.Double totalAmount,
		java.lang.Double yearAmount,
		java.lang.Double monthAmount,
		java.lang.Double dayAmount,
		java.lang.Short chargeReward,
		java.lang.Double rewardRandomMin,
		java.lang.Double rewardRandomMax,
		java.lang.Boolean rewardPattern) {

		this.setId(id);
		this.setChargeAmount(chargeAmount);
		this.setTotalAmount(totalAmount);
		this.setYearAmount(yearAmount);
		this.setMonthAmount(monthAmount);
		this.setDayAmount(dayAmount);
		this.setChargeReward(chargeReward);
		this.setRewardRandomMin(rewardRandomMin);
		this.setRewardRandomMax(rewardRandomMax);
		this.setRewardPattern(rewardPattern);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Double chargeAmount;
	private java.lang.Double totalAmount;
	private java.lang.Double yearAmount;
	private java.lang.Double monthAmount;
	private java.lang.Double dayAmount;
	private java.util.Date lastBuyTime;
	private java.lang.Short chargeReward;
	private java.lang.Double rewardRandomMin;
	private java.lang.Double rewardRandomMax;
	private java.lang.Boolean rewardPattern;

	// one to one
	private com.jeecms.bbs.entity.BbsTopic topic;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="foreign"
     *  column="topic_id"
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


	/**
	 * Return the value associated with the column: reward_random_min
	 */
	public java.lang.Double getRewardRandomMin () {
		return rewardRandomMin;
	}

	/**
	 * Set the value related to the column: reward_random_min
	 * @param rewardRandomMin the reward_random_min value
	 */
	public void setRewardRandomMin (java.lang.Double rewardRandomMin) {
		this.rewardRandomMin = rewardRandomMin;
	}


	/**
	 * Return the value associated with the column: reward_random_max
	 */
	public java.lang.Double getRewardRandomMax () {
		return rewardRandomMax;
	}

	/**
	 * Set the value related to the column: reward_random_max
	 * @param rewardRandomMax the reward_random_max value
	 */
	public void setRewardRandomMax (java.lang.Double rewardRandomMax) {
		this.rewardRandomMax = rewardRandomMax;
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


	/**
	 * Return the value associated with the column: topic
	 */
	public com.jeecms.bbs.entity.BbsTopic getTopic () {
		return topic;
	}

	/**
	 * Set the value related to the column: topic
	 * @param topic the topic value
	 */
	public void setTopic (com.jeecms.bbs.entity.BbsTopic topic) {
		this.topic = topic;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsTopicCharge)) return false;
		else {
			com.jeecms.bbs.entity.BbsTopicCharge bbsTopicCharge = (com.jeecms.bbs.entity.BbsTopicCharge) obj;
			if (null == this.getId() || null == bbsTopicCharge.getId()) return false;
			else return (this.getId().equals(bbsTopicCharge.getId()));
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