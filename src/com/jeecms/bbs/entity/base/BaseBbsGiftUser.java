package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_gift_user table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_gift_user"
 */

public abstract class BaseBbsGiftUser  implements Serializable {

	public static String REF = "BbsGiftUser";
	public static String PROP_GIFT = "gift";
	public static String PROP_USER = "user";
	public static String PROP_NUM = "num";
	public static String PROP_ID = "id";


	// constructors
	public BaseBbsGiftUser () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsGiftUser (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsGiftUser (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		com.jeecms.bbs.entity.BbsGift gift,
		java.lang.Integer num) {

		this.setId(id);
		this.setUser(user);
		this.setGift(gift);
		this.setNum(num);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer num;

	// many to one
	private com.jeecms.bbs.entity.BbsUser user;
	private com.jeecms.bbs.entity.BbsGift gift;



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
	 * Return the value associated with the column: num
	 */
	public java.lang.Integer getNum () {
		return num;
	}

	/**
	 * Set the value related to the column: num
	 * @param num the num value
	 */
	public void setNum (java.lang.Integer num) {
		this.num = num;
	}


	/**
	 * Return the value associated with the column: user_id
	 */
	public com.jeecms.bbs.entity.BbsUser getUser () {
		return user;
	}

	/**
	 * Set the value related to the column: user_id
	 * @param user the user_id value
	 */
	public void setUser (com.jeecms.bbs.entity.BbsUser user) {
		this.user = user;
	}


	/**
	 * Return the value associated with the column: gift_id
	 */
	public com.jeecms.bbs.entity.BbsGift getGift () {
		return gift;
	}

	/**
	 * Set the value related to the column: gift_id
	 * @param gift the gift_id value
	 */
	public void setGift (com.jeecms.bbs.entity.BbsGift gift) {
		this.gift = gift;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsGiftUser)) return false;
		else {
			com.jeecms.bbs.entity.BbsGiftUser bbsGiftUser = (com.jeecms.bbs.entity.BbsGiftUser) obj;
			if (null == this.getId() || null == bbsGiftUser.getId()) return false;
			else return (this.getId().equals(bbsGiftUser.getId()));
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