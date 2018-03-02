package com.jeecms.plug.live.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_live_message table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_live_message"
 */

public abstract class BaseBbsLiveMessage  implements Serializable {

	public static String REF = "BbsLiveMessage";
	public static String PROP_USER = "user";
	public static String PROP_CONTENT = "content";
	public static String PROP_LIVE = "live";
	public static String PROP_IMAGE_URL = "imageUrl";
	public static String PROP_MSG_TYPE = "msgType";
	public static String PROP_ID = "id";
	public static String PROP_MSG_TIME = "msgTime";


	// constructors
	public BaseBbsLiveMessage () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsLiveMessage (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsLiveMessage (
		java.lang.Long id,
		com.jeecms.plug.live.entity.BbsLive live,
		com.jeecms.bbs.entity.BbsUser user,
		java.util.Date msgTime,
		java.lang.Short msgType) {

		this.setId(id);
		this.setLive(live);
		this.setUser(user);
		this.setMsgTime(msgTime);
		this.setMsgType(msgType);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.util.Date msgTime;
	private java.lang.String content;
	private java.lang.String imageUrl;
	private java.lang.Short msgType;

	// many to one
	private com.jeecms.plug.live.entity.BbsLive live;
	private com.jeecms.bbs.entity.BbsUser user;
	private com.jeecms.bbs.entity.BbsUser toUser;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="msg_id"
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
	 * Return the value associated with the column: msg_time
	 */
	public java.util.Date getMsgTime () {
		return msgTime;
	}

	/**
	 * Set the value related to the column: msg_time
	 * @param msgTime the msg_time value
	 */
	public void setMsgTime (java.util.Date msgTime) {
		this.msgTime = msgTime;
	}


	/**
	 * Return the value associated with the column: content
	 */
	public java.lang.String getContent () {
		return content;
	}

	/**
	 * Set the value related to the column: content
	 * @param content the content value
	 */
	public void setContent (java.lang.String content) {
		this.content = content;
	}


	/**
	 * Return the value associated with the column: image_url
	 */
	public java.lang.String getImageUrl () {
		return imageUrl;
	}

	/**
	 * Set the value related to the column: image_url
	 * @param imageUrl the image_url value
	 */
	public void setImageUrl (java.lang.String imageUrl) {
		this.imageUrl = imageUrl;
	}


	/**
	 * Return the value associated with the column: msg_type
	 */
	public java.lang.Short getMsgType () {
		return msgType;
	}

	/**
	 * Set the value related to the column: msg_type
	 * @param msgType the msg_type value
	 */
	public void setMsgType (java.lang.Short msgType) {
		this.msgType = msgType;
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
	
	public com.jeecms.bbs.entity.BbsUser getToUser() {
		return toUser;
	}

	public void setToUser(com.jeecms.bbs.entity.BbsUser toUser) {
		this.toUser = toUser;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.plug.live.entity.BbsLiveMessage)) return false;
		else {
			com.jeecms.plug.live.entity.BbsLiveMessage bbsLiveMessage = (com.jeecms.plug.live.entity.BbsLiveMessage) obj;
			if (null == this.getId() || null == bbsLiveMessage.getId()) return false;
			else return (this.getId().equals(bbsLiveMessage.getId()));
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