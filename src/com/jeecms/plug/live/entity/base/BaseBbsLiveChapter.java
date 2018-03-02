package com.jeecms.plug.live.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_live_chapter table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_live_chapter"
 */

public abstract class BaseBbsLiveChapter  implements Serializable {

	public static String REF = "BbsLiveChapter";
	public static String PROP_PATH = "path";
	public static String PROP_USER = "user";
	public static String PROP_PARENT = "parent";
	public static String PROP_PRIORITY = "priority";
	public static String PROP_LFT = "lft";
	public static String PROP_ID = "id";
	public static String PROP_RGT = "rgt";
	public static String PROP_NAME = "name";


	// constructors
	public BaseBbsLiveChapter () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsLiveChapter (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsLiveChapter (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		java.lang.String name,
		java.lang.String path,
		java.lang.Integer lft,
		java.lang.Integer rgt,
		java.lang.Integer priority) {

		this.setId(id);
		this.setUser(user);
		this.setName(name);
		this.setPath(path);
		this.setLft(lft);
		this.setRgt(rgt);
		this.setPriority(priority);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;
	private java.lang.String path;
	private java.lang.Integer lft;
	private java.lang.Integer rgt;
	private java.lang.Integer priority;

	// many to one
	private com.jeecms.plug.live.entity.BbsLiveChapter parent;
	private com.jeecms.bbs.entity.BbsUser user;

	// collections
	private java.util.Set<com.jeecms.plug.live.entity.BbsLiveChapter> child;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="chapter_id"
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
	 * Return the value associated with the column: name
	 */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the value related to the column: name
	 * @param name the name value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
	}


	/**
	 * Return the value associated with the column: chapter_path
	 */
	public java.lang.String getPath () {
		return path;
	}

	/**
	 * Set the value related to the column: chapter_path
	 * @param path the chapter_path value
	 */
	public void setPath (java.lang.String path) {
		this.path = path;
	}


	/**
	 * Return the value associated with the column: lft
	 */
	public java.lang.Integer getLft () {
		return lft;
	}

	/**
	 * Set the value related to the column: lft
	 * @param lft the lft value
	 */
	public void setLft (java.lang.Integer lft) {
		this.lft = lft;
	}


	/**
	 * Return the value associated with the column: rgt
	 */
	public java.lang.Integer getRgt () {
		return rgt;
	}

	/**
	 * Set the value related to the column: rgt
	 * @param rgt the rgt value
	 */
	public void setRgt (java.lang.Integer rgt) {
		this.rgt = rgt;
	}


	/**
	 * Return the value associated with the column: priority
	 */
	public java.lang.Integer getPriority () {
		return priority;
	}

	/**
	 * Set the value related to the column: priority
	 * @param priority the priority value
	 */
	public void setPriority (java.lang.Integer priority) {
		this.priority = priority;
	}


	/**
	 * Return the value associated with the column: parent_id
	 */
	public com.jeecms.plug.live.entity.BbsLiveChapter getParent () {
		return parent;
	}

	/**
	 * Set the value related to the column: parent_id
	 * @param parent the parent_id value
	 */
	public void setParent (com.jeecms.plug.live.entity.BbsLiveChapter parent) {
		this.parent = parent;
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
	 * Return the value associated with the column: child
	 */
	public java.util.Set<com.jeecms.plug.live.entity.BbsLiveChapter> getChild () {
		return child;
	}

	/**
	 * Set the value related to the column: child
	 * @param child the child value
	 */
	public void setChild (java.util.Set<com.jeecms.plug.live.entity.BbsLiveChapter> child) {
		this.child = child;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.plug.live.entity.BbsLiveChapter)) return false;
		else {
			com.jeecms.plug.live.entity.BbsLiveChapter bbsLiveChapter = (com.jeecms.plug.live.entity.BbsLiveChapter) obj;
			if (null == this.getId() || null == bbsLiveChapter.getId()) return false;
			else return (this.getId().equals(bbsLiveChapter.getId()));
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