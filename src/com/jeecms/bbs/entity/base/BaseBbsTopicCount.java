package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_topic_count table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_topic_count"
 */

public abstract class BaseBbsTopicCount  implements Serializable {

	public static String REF = "BbsTopicCount";
	public static String PROP_REWARDS = "rewards";
	public static String PROP_UPS = "ups";
	public static String PROP_ATTENTIONS = "attentions";
	public static String PROP_ID = "id";
	public static String PROP_COLLECTIONS = "collections";
	public static String PROP_TOPIC = "topic";


	// constructors
	public BaseBbsTopicCount () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsTopicCount (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsTopicCount (
		java.lang.Integer id,
		java.lang.Integer ups,
		java.lang.Integer collections,
		java.lang.Integer rewards,
		java.lang.Integer attentions) {

		this.setId(id);
		this.setUps(ups);
		this.setCollections(collections);
		this.setRewards(rewards);
		this.setAttentions(attentions);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer ups;
	private java.lang.Integer collections;
	private java.lang.Integer rewards;
	private java.lang.Integer attentions;

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
	 * Return the value associated with the column: ups
	 */
	public java.lang.Integer getUps () {
		return ups;
	}

	/**
	 * Set the value related to the column: ups
	 * @param ups the ups value
	 */
	public void setUps (java.lang.Integer ups) {
		this.ups = ups;
	}


	/**
	 * Return the value associated with the column: collections
	 */
	public java.lang.Integer getCollections () {
		return collections;
	}

	/**
	 * Set the value related to the column: collections
	 * @param collections the collections value
	 */
	public void setCollections (java.lang.Integer collections) {
		this.collections = collections;
	}


	/**
	 * Return the value associated with the column: rewards
	 */
	public java.lang.Integer getRewards () {
		return rewards;
	}

	/**
	 * Set the value related to the column: rewards
	 * @param rewards the rewards value
	 */
	public void setRewards (java.lang.Integer rewards) {
		this.rewards = rewards;
	}


	/**
	 * Return the value associated with the column: attentions
	 */
	public java.lang.Integer getAttentions () {
		return attentions;
	}

	/**
	 * Set the value related to the column: attentions
	 * @param attentions the attentions value
	 */
	public void setAttentions (java.lang.Integer attentions) {
		this.attentions = attentions;
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
		if (!(obj instanceof com.jeecms.bbs.entity.BbsTopicCount)) return false;
		else {
			com.jeecms.bbs.entity.BbsTopicCount bbsTopicCount = (com.jeecms.bbs.entity.BbsTopicCount) obj;
			if (null == this.getId() || null == bbsTopicCount.getId()) return false;
			else return (this.getId().equals(bbsTopicCount.getId()));
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