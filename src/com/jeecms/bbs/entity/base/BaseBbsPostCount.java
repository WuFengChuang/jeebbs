package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_post_count table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_post_count"
 */

public abstract class BaseBbsPostCount  implements Serializable {

	public static String REF = "BbsPostCount";
	public static String PROP_POST = "post";
	public static String PROP_REPLYS = "replys";
	public static String PROP_UPS = "ups";
	public static String PROP_ID = "id";


	// constructors
	public BaseBbsPostCount () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsPostCount (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsPostCount (
		java.lang.Integer id,
		java.lang.Integer replys,
		java.lang.Integer ups) {

		this.setId(id);
		this.setReplys(replys);
		this.setUps(ups);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.Integer replys;
	private java.lang.Integer ups;

	// one to one
	private com.jeecms.bbs.entity.BbsPost post;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="foreign"
     *  column="POST_ID"
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
	 * Return the value associated with the column: replys
	 */
	public java.lang.Integer getReplys () {
		return replys;
	}

	/**
	 * Set the value related to the column: replys
	 * @param replys the replys value
	 */
	public void setReplys (java.lang.Integer replys) {
		this.replys = replys;
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
	 * Return the value associated with the column: post
	 */
	public com.jeecms.bbs.entity.BbsPost getPost () {
		return post;
	}

	/**
	 * Set the value related to the column: post
	 * @param post the post value
	 */
	public void setPost (com.jeecms.bbs.entity.BbsPost post) {
		this.post = post;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsPostCount)) return false;
		else {
			com.jeecms.bbs.entity.BbsPostCount bbsPostCount = (com.jeecms.bbs.entity.BbsPostCount) obj;
			if (null == this.getId() || null == bbsPostCount.getId()) return false;
			else return (this.getId().equals(bbsPostCount.getId()));
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