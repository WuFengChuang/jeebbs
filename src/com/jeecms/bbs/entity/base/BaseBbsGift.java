package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_gift table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_gift"
 */

public abstract class BaseBbsGift  implements Serializable {

	public static String REF = "BbsGift";
	public static String PROP_GIFT_TYPE = "giftType";
	public static String PROP_PRICE = "price";
	public static String PROP_PRIORITY = "priority";
	public static String PROP_PIC_PATH = "picPath";
	public static String PROP_ID = "id";
	public static String PROP_DISABLED = "disabled";
	public static String PROP_NAME = "name";


	// constructors
	public BaseBbsGift () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsGift (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsGift (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.String picPath,
		java.lang.Double price,
		java.lang.Integer priority,
		java.lang.Boolean disabled,
		java.lang.Short giftType) {

		this.setId(id);
		this.setName(name);
		this.setPicPath(picPath);
		this.setPrice(price);
		this.setPriority(priority);
		this.setDisabled(disabled);
		this.setGiftType(giftType);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;
	private java.lang.String picPath;
	private java.lang.Double price;
	private java.lang.Integer priority;
	private java.lang.Boolean disabled;
	private java.lang.Short giftType;
	private java.lang.Double totalAmount;
	private java.lang.Double yearAmount;
	private java.lang.Double monthAmount;
	private java.lang.Double dayAmount;
	private java.util.Date lastBuyTime;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="gift_id"
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
	 * Return the value associated with the column: name
	 */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the value related to the column: name
	 * @param name the name value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}


	/**
	 * Return the value associated with the column: pic_path
	 */
	public java.lang.String getPicPath () {
		return picPath;
	}

	/**
	 * Set the value related to the column: pic_path
	 * @param picPath the pic_path value
	 */
	public void setPicPath (java.lang.String picPath) {
		this.picPath = picPath;
	}


	/**
	 * Return the value associated with the column: price
	 */
	public java.lang.Double getPrice () {
		return price;
	}

	/**
	 * Set the value related to the column: price
	 * @param price the price value
	 */
	public void setPrice (java.lang.Double price) {
		this.price = price;
	}


	/**
	 * Return the value associated with the column: priority
	 */
	public java.lang.Integer getPriority () {
		return priority;
	}

	/**
	 * Set the value related to the column: priority
	 * @param priority the priority value
	 */
	public void setPriority (java.lang.Integer priority) {
		this.priority = priority;
	}


	/**
	 * Return the value associated with the column: is_disabled
	 */
	public java.lang.Boolean getDisabled () {
		return disabled;
	}

	/**
	 * Set the value related to the column: is_disabled
	 * @param disabled the is_disabled value
	 */
	public void setDisabled (java.lang.Boolean disabled) {
		this.disabled = disabled;
	}


	/**
	 * Return the value associated with the column: gift_type
	 */
	public java.lang.Short getGiftType () {
		return giftType;
	}

	/**
	 * Set the value related to the column: gift_type
	 * @param giftType the gift_type value
	 */
	public void setGiftType (java.lang.Short giftType) {
		this.giftType = giftType;
	}

	public java.lang.Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(java.lang.Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public java.lang.Double getYearAmount() {
		return yearAmount;
	}

	public void setYearAmount(java.lang.Double yearAmount) {
		this.yearAmount = yearAmount;
	}

	public java.lang.Double getMonthAmount() {
		return monthAmount;
	}

	public void setMonthAmount(java.lang.Double monthAmount) {
		this.monthAmount = monthAmount;
	}

	public java.lang.Double getDayAmount() {
		return dayAmount;
	}

	public void setDayAmount(java.lang.Double dayAmount) {
		this.dayAmount = dayAmount;
	}

	public java.util.Date getLastBuyTime() {
		return lastBuyTime;
	}

	public void setLastBuyTime(java.util.Date lastBuyTime) {
		this.lastBuyTime = lastBuyTime;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsGift)) return false;
		else {
			com.jeecms.bbs.entity.BbsGift bbsGift = (com.jeecms.bbs.entity.BbsGift) obj;
			if (null == this.getId() || null == bbsGift.getId()) return false;
			else return (this.getId().equals(bbsGift.getId()));
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