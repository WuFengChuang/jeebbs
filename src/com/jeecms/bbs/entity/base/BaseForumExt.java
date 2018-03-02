package com.jeecms.bbs.entity.base;

import java.io.Serializable;

import com.jeecms.bbs.entity.BbsForum;


/**
 * This is an object that contains data related to the jc_channel_ext table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_channel_ext"
 */

public abstract class BaseForumExt  implements Serializable {

	public static String REF = "ForumExt";
	public static String PROP_ID = "id";


	// constructors
	public BaseForumExt () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseForumExt (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	

	protected void initialize () {}



	public BaseForumExt(String tplForum, String tplTopic, String tplMobileForum, String tplMobileTopic,
			BbsForum forum) {
		super();
		this.tplForum = tplForum;
		this.tplTopic = tplTopic;
		this.tplMobileForum = tplMobileForum;
		this.tplMobileTopic = tplMobileTopic;
		this.forum = forum;
	}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;
	
	private java.lang.String tplForum;
	private java.lang.String tplTopic;
	private java.lang.String tplMobileForum;
	private java.lang.String tplMobileTopic;

	// one to one
	private com.jeecms.bbs.entity.BbsForum forum;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="foreign"
     *  column="channel_id"
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
	
	public java.lang.String getTplForum() {
		return tplForum;
	}

	public void setTplForum(java.lang.String tplForum) {
		this.tplForum = tplForum;
	}

	public java.lang.String getTplTopic() {
		return tplTopic;
	}

	public void setTplTopic(java.lang.String tplTopic) {
		this.tplTopic = tplTopic;
	}

	public java.lang.String getTplMobileForum() {
		return tplMobileForum;
	}

	public void setTplMobileForum(java.lang.String tplMobileForum) {
		this.tplMobileForum = tplMobileForum;
	}

	public java.lang.String getTplMobileTopic() {
		return tplMobileTopic;
	}

	public void setTplMobileTopic(java.lang.String tplMobileTopic) {
		this.tplMobileTopic = tplMobileTopic;
	}

	public com.jeecms.bbs.entity.BbsForum getForum() {
		return forum;
	}

	public void setForum(com.jeecms.bbs.entity.BbsForum forum) {
		this.forum = forum;
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