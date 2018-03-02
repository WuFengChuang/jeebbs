package com.jeecms.plug.live.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_live_user table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_live_user"
 */

public abstract class BaseBbsLiveUser  implements Serializable {

	public static String REF = "BbsLiveUser";
	public static String PROP_BUY_TIME = "buyTime";
	public static String PROP_LIVE = "live";
	public static String PROP_JOIN_USER = "joinUser";
	public static String PROP_JOIN_TIME = "joinTime";
	public static String PROP_ID = "id";


	// constructors
	public BaseBbsLiveUser () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsLiveUser (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsLiveUser (
		java.lang.Long id,
		com.jeecms.plug.live.entity.BbsLive live,
		com.jeecms.bbs.entity.BbsUser joinUser,
		java.util.Date buyTime) {

		this.setId(id);
		this.setLive(live);
		this.setJoinUser(joinUser);
		this.setBuyTime(buyTime);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.util.Date buyTime;
	private java.util.Date joinTime;

	// many to one
	private com.jeecms.plug.live.entity.BbsLive live;
	private com.jeecms.bbs.entity.BbsUser joinUser;
	private com.jeecms.bbs.entity.BbsOrder order;


	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="id"
     */
	public java.lang.Long getId () {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * @param id the new ID
	 */
	public void setId (java.lang.Long id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}




	/**
	 * Return the value associated with the column: buy_time
	 */
	public java.util.Date getBuyTime () {
		return buyTime;
	}

	/**
	 * Set the value related to the column: buy_time
	 * @param buyTime the buy_time value
	 */
	public void setBuyTime (java.util.Date buyTime) {
		this.buyTime = buyTime;
	}


	/**
	 * Return the value associated with the column: join_time
	 */
	public java.util.Date getJoinTime () {
		return joinTime;
	}

	/**
	 * Set the value related to the column: join_time
	 * @param joinTime the join_time value
	 */
	public void setJoinTime (java.util.Date joinTime) {
		this.joinTime = joinTime;
	}


	/**
	 * Return the value associated with the column: live_id
	 */
	public com.jeecms.plug.live.entity.BbsLive getLive () {
		return live;
	}

	/**
	 * Set the value related to the column: live_id
	 * @param live the live_id value
	 */
	public void setLive (com.jeecms.plug.live.entity.BbsLive live) {
		this.live = live;
	}


	/**
	 * Return the value associated with the column: user_id
	 */
	public com.jeecms.bbs.entity.BbsUser getJoinUser () {
		return joinUser;
	}

	/**
	 * Set the value related to the column: user_id
	 * @param joinUser the user_id value
	 */
	public void setJoinUser (com.jeecms.bbs.entity.BbsUser joinUser) {
		this.joinUser = joinUser;
	}

	public com.jeecms.bbs.entity.BbsOrder getOrder() {
		return order;
	}

	public void setOrder(com.jeecms.bbs.entity.BbsOrder order) {
		this.order = order;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.plug.live.entity.BbsLiveUser)) return false;
		else {
			com.jeecms.plug.live.entity.BbsLiveUser bbsLiveUser = (com.jeecms.plug.live.entity.BbsLiveUser) obj;
			if (null == this.getId() || null == bbsLiveUser.getId()) return false;
			else return (this.getId().equals(bbsLiveUser.getId()));
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