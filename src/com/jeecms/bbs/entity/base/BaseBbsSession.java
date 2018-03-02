package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_session table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_session"
 */

public abstract class BaseBbsSession  implements Serializable {

	public static String REF = "BbsSession";
	public static String PROP_USER = "user";
	public static String PROP_SESSION_ID = "sessionId";
	public static String PROP_IP = "ip";
	public static String PROP_FIRST_ACTIVETIME = "firstActivetime";
	public static String PROP_ID = "id";
	public static String PROP_LAST_ACTIVETIME = "lastActivetime";


	// constructors
	public BaseBbsSession () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsSession (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsSession (
		java.lang.Long id,
		java.lang.String sessionId,
		java.lang.String ip,
		java.util.Date firstActivetime,
		java.util.Date lastActivetime) {

		this.setId(id);
		this.setSessionId(sessionId);
		this.setIp(ip);
		this.setFirstActivetime(firstActivetime);
		this.setLastActivetime(lastActivetime);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.String sessionId;
	private java.lang.String ip;
	private java.util.Date firstActivetime;
	private java.util.Date lastActivetime;

	// many to one
	private com.jeecms.bbs.entity.BbsUser user;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="sid"
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
	 * Return the value associated with the column: session_id
	 */
	public java.lang.String getSessionId () {
		return sessionId;
	}

	/**
	 * Set the value related to the column: session_id
	 * @param sessionId the session_id value
	 */
	public void setSessionId (java.lang.String sessionId) {
		this.sessionId = sessionId;
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
	 * Return the value associated with the column: first_activetime
	 */
	public java.util.Date getFirstActivetime () {
		return firstActivetime;
	}

	/**
	 * Set the value related to the column: first_activetime
	 * @param firstActivetime the first_activetime value
	 */
	public void setFirstActivetime (java.util.Date firstActivetime) {
		this.firstActivetime = firstActivetime;
	}


	/**
	 * Return the value associated with the column: last_activetime
	 */
	public java.util.Date getLastActivetime () {
		return lastActivetime;
	}

	/**
	 * Set the value related to the column: last_activetime
	 * @param lastActivetime the last_activetime value
	 */
	public void setLastActivetime (java.util.Date lastActivetime) {
		this.lastActivetime = lastActivetime;
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



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsSession)) return false;
		else {
			com.jeecms.bbs.entity.BbsSession bbsSession = (com.jeecms.bbs.entity.BbsSession) obj;
			if (null == this.getId() || null == bbsSession.getId()) return false;
			else return (this.getId().equals(bbsSession.getId()));
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