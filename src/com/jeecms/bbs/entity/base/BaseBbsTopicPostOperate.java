package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_topic_operate table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_topic_operate"
 */

public abstract class BaseBbsTopicPostOperate  implements Serializable {

	public static String REF = "BbsTopicOperate";
	public static String PROP_USER = "user";
	public static String PROP_OPERATE = "operate";
	public static String PROP_ID = "id";
	public static String PROP_TOPIC = "topic";
	public static String PROP_OP_TIME = "opTime";


	// constructors
	public BaseBbsTopicPostOperate () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsTopicPostOperate (java.lang.Long id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsTopicPostOperate (
		java.lang.Long id,
		com.jeecms.bbs.entity.BbsUser user,
		java.lang.Integer dataId,
		java.lang.Short dataType,
		java.lang.Integer operate,
		java.util.Date opTime) {

		this.setId(id);
		this.setUser(user);
		this.setDataId(dataId);
		this.setDataType(dataType);
		this.setOperate(operate);
		this.setOpTime(opTime);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Long id;

	// fields
	private java.lang.Integer operate;
	private java.lang.Integer dataId;
	private java.lang.Short dataType;
	private java.util.Date opTime;

	// many to one
	private com.jeecms.bbs.entity.BbsUser user;



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
	 * Return the value associated with the column: operate
	 */
	public java.lang.Integer getOperate () {
		return operate;
	}

	/**
	 * Set the value related to the column: operate
	 * @param operate the operate value
	 */
	public void setOperate (java.lang.Integer operate) {
		this.operate = operate;
	}


	/**
	 * Return the value associated with the column: op_time
	 */
	public java.util.Date getOpTime () {
		return opTime;
	}

	/**
	 * Set the value related to the column: op_time
	 * @param opTime the op_time value
	 */
	public void setOpTime (java.util.Date opTime) {
		this.opTime = opTime;
	}


	public java.lang.Integer getDataId() {
		return dataId;
	}

	public void setDataId(java.lang.Integer dataId) {
		this.dataId = dataId;
	}

	public java.lang.Short getDataType() {
		return dataType;
	}

	public void setDataType(java.lang.Short dataType) {
		this.dataType = dataType;
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
		if (!(obj instanceof com.jeecms.bbs.entity.BbsTopicPostOperate)) return false;
		else {
			com.jeecms.bbs.entity.BbsTopicPostOperate bbsTopicOperate = (com.jeecms.bbs.entity.BbsTopicPostOperate) obj;
			if (null == this.getId() || null == bbsTopicOperate.getId()) return false;
			else return (this.getId().equals(bbsTopicOperate.getId()));
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