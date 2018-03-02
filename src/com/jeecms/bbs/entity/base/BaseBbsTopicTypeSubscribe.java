package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_topic_type_subscribe table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_topic_type_subscribe"
 */

public abstract class BaseBbsTopicTypeSubscribe  implements Serializable {

	public static String REF = "BbsTopicTypeSubscribe";
	public static String PROP_TYPE = "type";
	public static String PROP_USER = "user";
	public static String PROP_ID = "id";


	// constructors
	public BaseBbsTopicTypeSubscribe () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsTopicTypeSubscribe (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsTopicTypeSubscribe (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		com.jeecms.bbs.entity.BbsTopicType type) {

		this.setId(id);
		this.setUser(user);
		this.setType(type);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// many to one
	private com.jeecms.bbs.entity.BbsUser user;
	private com.jeecms.bbs.entity.BbsTopicType type;



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
	 * Return the value associated with the column: type_id
	 */
	public com.jeecms.bbs.entity.BbsTopicType getType () {
		return type;
	}

	/**
	 * Set the value related to the column: type_id
	 * @param type the type_id value
	 */
	public void setType (com.jeecms.bbs.entity.BbsTopicType type) {
		this.type = type;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsTopicTypeSubscribe)) return false;
		else {
			com.jeecms.bbs.entity.BbsTopicTypeSubscribe bbsTopicTypeSubscribe = (com.jeecms.bbs.entity.BbsTopicTypeSubscribe) obj;
			if (null == this.getId() || null == bbsTopicTypeSubscribe.getId()) return false;
			else return (this.getId().equals(bbsTopicTypeSubscribe.getId()));
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