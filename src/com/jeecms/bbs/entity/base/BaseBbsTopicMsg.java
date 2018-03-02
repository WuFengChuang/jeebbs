package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_topic_msg table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_topic_msg"
 */

public abstract class BaseBbsTopicMsg  implements Serializable {

	public static String REF = "BbsTopicMsg";
	public static String PROP_USER = "user";
	public static String PROP_STATUS = "status";
	public static String PROP_POST = "post";
	public static String PROP_TOPIC = "topic";
	public static String PROP_ID = "id";


	// constructors
	public BaseBbsTopicMsg () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsTopicMsg (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsTopicMsg (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsTopic topic,
		java.lang.Boolean status) {

		this.setId(id);
		this.setTopic(topic);
		this.setStatus(status);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Boolean status;

	// many to one
	private com.jeecms.bbs.entity.BbsUser user;
	private com.jeecms.bbs.entity.BbsTopic topic;
	private com.jeecms.bbs.entity.BbsPost post;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="msg_id"
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
	 * Return the value associated with the column: is_read
	 */
	public java.lang.Boolean getStatus () {
		return status;
	}

	/**
	 * Set the value related to the column: is_read
	 * @param status the is_read value
	 */
	public void setStatus (java.lang.Boolean status) {
		this.status = status;
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
	 * Return the value associated with the column: topic_id
	 */
	public com.jeecms.bbs.entity.BbsTopic getTopic () {
		return topic;
	}

	/**
	 * Set the value related to the column: topic_id
	 * @param topic the topic_id value
	 */
	public void setTopic (com.jeecms.bbs.entity.BbsTopic topic) {
		this.topic = topic;
	}


	/**
	 * Return the value associated with the column: post_id
	 */
	public com.jeecms.bbs.entity.BbsPost getPost () {
		return post;
	}

	/**
	 * Set the value related to the column: post_id
	 * @param post the post_id value
	 */
	public void setPost (com.jeecms.bbs.entity.BbsPost post) {
		this.post = post;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsTopicMsg)) return false;
		else {
			com.jeecms.bbs.entity.BbsTopicMsg bbsTopicMsg = (com.jeecms.bbs.entity.BbsTopicMsg) obj;
			if (null == this.getId() || null == bbsTopicMsg.getId()) return false;
			else return (this.getId().equals(bbsTopicMsg.getId()));
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