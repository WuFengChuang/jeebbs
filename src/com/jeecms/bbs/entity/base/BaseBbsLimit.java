package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_limit table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_limit"
 */

public abstract class BaseBbsLimit  implements Serializable {

	public static String REF = "BbsLimit";
	public static String PROP_IP = "ip";
	public static String PROP_ID = "id";
	public static String PROP_USER_ID = "userId";


	// constructors
	public BaseBbsLimit () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsLimit (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String ip;
	private java.lang.Integer userId;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="limit_id"
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
	 * Return the value associated with the column: ip
	 */
	public java.lang.String getIp () {
		return ip;
	}

	/**
	 * Set the value related to the column: ip
	 * @param ip the ip value
	 */
	public void setIp (java.lang.String ip) {
		this.ip = ip;
	}


	/**
	 * Return the value associated with the column: user_id
	 */
	public java.lang.Integer getUserId () {
		return userId;
	}

	/**
	 * Set the value related to the column: user_id
	 * @param userId the user_id value
	 */
	public void setUserId (java.lang.Integer userId) {
		this.userId = userId;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsLimit)) return false;
		else {
			com.jeecms.bbs.entity.BbsLimit bbsLimit = (com.jeecms.bbs.entity.BbsLimit) obj;
			if (null == this.getId() || null == bbsLimit.getId()) return false;
			else return (this.getId().equals(bbsLimit.getId()));
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