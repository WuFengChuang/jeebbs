package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_forum_count table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_forum_count"
 */

public abstract class BaseBbsForumCount  implements Serializable {

	public static String REF = "BbsForumCount";
	public static String PROP_COUNT_DATE = "countDate";
	public static String PROP_POST_COUNT = "postCount";
	public static String PROP_TOPIC_COUNT = "topicCount";
	public static String PROP_ID = "id";
	public static String PROP_VISIT_COUNT = "visitCount";
	public static String PROP_FORUM = "forum";


	// constructors
	public BaseBbsForumCount () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsForumCount (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsForumCount (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsForum forum,
		java.lang.Integer topicCount,
		java.lang.Integer postCount,
		java.lang.Integer visitCount,
		java.util.Date countDate) {

		this.setId(id);
		this.setForum(forum);
		this.setTopicCount(topicCount);
		this.setPostCount(postCount);
		this.setVisitCount(visitCount);
		this.setCountDate(countDate);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer topicCount;
	private java.lang.Integer postCount;
	private java.lang.Integer visitCount;
	private java.util.Date countDate;

	// many to one
	private com.jeecms.bbs.entity.BbsForum forum;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
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
	 * Return the value associated with the column: topic_count
	 */
	public java.lang.Integer getTopicCount () {
		return topicCount;
	}

	/**
	 * Set the value related to the column: topic_count
	 * @param topicCount the topic_count value
	 */
	public void setTopicCount (java.lang.Integer topicCount) {
		this.topicCount = topicCount;
	}


	/**
	 * Return the value associated with the column: post_count
	 */
	public java.lang.Integer getPostCount () {
		return postCount;
	}

	/**
	 * Set the value related to the column: post_count
	 * @param postCount the post_count value
	 */
	public void setPostCount (java.lang.Integer postCount) {
		this.postCount = postCount;
	}


	/**
	 * Return the value associated with the column: visit_count
	 */
	public java.lang.Integer getVisitCount () {
		return visitCount;
	}

	/**
	 * Set the value related to the column: visit_count
	 * @param visitCount the visit_count value
	 */
	public void setVisitCount (java.lang.Integer visitCount) {
		this.visitCount = visitCount;
	}


	/**
	 * Return the value associated with the column: count_date
	 */
	public java.util.Date getCountDate () {
		return countDate;
	}

	/**
	 * Set the value related to the column: count_date
	 * @param countDate the count_date value
	 */
	public void setCountDate (java.util.Date countDate) {
		this.countDate = countDate;
	}


	/**
	 * Return the value associated with the column: forum_id
	 */
	public com.jeecms.bbs.entity.BbsForum getForum () {
		return forum;
	}

	/**
	 * Set the value related to the column: forum_id
	 * @param forum the forum_id value
	 */
	public void setForum (com.jeecms.bbs.entity.BbsForum forum) {
		this.forum = forum;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsForumCount)) return false;
		else {
			com.jeecms.bbs.entity.BbsForumCount bbsForumCount = (com.jeecms.bbs.entity.BbsForumCount) obj;
			if (null == this.getId() || null == bbsForumCount.getId()) return false;
			else return (this.getId().equals(bbsForumCount.getId()));
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