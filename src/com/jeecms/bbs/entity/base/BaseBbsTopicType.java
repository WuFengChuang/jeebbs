package com.jeecms.bbs.entity.base;

import java.io.Serializable;
import java.util.Set;


/**
 * This is an object that contains data related to the bbs_topic_type table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_topic_type"
 */

public abstract class BaseBbsTopicType  implements Serializable {

	public static String REF = "BbsTopicType";
	public static String PROP_PATH = "path";
	public static String PROP_SUBSCRIBE_COUNT = "subscribeCount";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_PARENT = "parent";
	public static String PROP_PRIORITY = "priority";
	public static String PROP_TOPIC_COUNT = "topicCount";
	public static String PROP_NAME = "name";
	public static String PROP_TOPIC_ESSENCE_COUNT = "topicEssenceCount";
	public static String PROP_TYPE_LOG = "typeLog";
	public static String PROP_LFT = "lft";
	public static String PROP_ID = "id";
	public static String PROP_RGT = "rgt";
	public static String PROP_DISPLAY = "display";


	// constructors
	public BaseBbsTopicType () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsTopicType (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsTopicType (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.String path,
		java.lang.Integer lft,
		java.lang.Integer rgt,
		java.lang.Integer priority,
		boolean display,
		java.lang.Integer topicCount,
		java.lang.Integer topicEssenceCount,
		java.lang.Integer subscribeCount) {

		this.setId(id);
		this.setName(name);
		this.setPath(path);
		this.setLft(lft);
		this.setRgt(rgt);
		this.setPriority(priority);
		this.setDisplay(display);
		this.setTopicCount(topicCount);
		this.setTopicEssenceCount(topicEssenceCount);
		this.setSubscribeCount(subscribeCount);
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
	private boolean display;
	private java.lang.String description;
	private java.lang.String typeLog;
	private java.lang.Integer topicCount;
	private java.lang.Integer topicEssenceCount;
	private java.lang.Integer subscribeCount;

	// many to one
	private com.jeecms.bbs.entity.BbsTopicType parent;
	private com.jeecms.core.entity.CmsSite site;
	private Set<com.jeecms.bbs.entity.BbsTopicType> child;
	private Set<com.jeecms.bbs.entity.BbsTopicTypeSubscribe> subscribes;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="type_id"
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
	 * Return the value associated with the column: type_path
	 */
	public java.lang.String getPath () {
		return path;
	}

	/**
	 * Set the value related to the column: type_path
	 * @param path the type_path value
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
	 * Return the value associated with the column: is_display
	 */
	public boolean isDisplay () {
		return display;
	}

	/**
	 * Set the value related to the column: is_display
	 * @param display the is_display value
	 */
	public void setDisplay (boolean display) {
		this.display = display;
	}


	/**
	 * Return the value associated with the column: description
	 */
	public java.lang.String getDescription () {
		return description;
	}

	/**
	 * Set the value related to the column: description
	 * @param description the description value
	 */
	public void setDescription (java.lang.String description) {
		this.description = description;
	}


	/**
	 * Return the value associated with the column: type_log
	 */
	public java.lang.String getTypeLog () {
		return typeLog;
	}

	/**
	 * Set the value related to the column: type_log
	 * @param typeLog the type_log value
	 */
	public void setTypeLog (java.lang.String typeLog) {
		this.typeLog = typeLog;
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
	 * Return the value associated with the column: topic_essence_count
	 */
	public java.lang.Integer getTopicEssenceCount () {
		return topicEssenceCount;
	}

	/**
	 * Set the value related to the column: topic_essence_count
	 * @param topicEssenceCount the topic_essence_count value
	 */
	public void setTopicEssenceCount (java.lang.Integer topicEssenceCount) {
		this.topicEssenceCount = topicEssenceCount;
	}


	/**
	 * Return the value associated with the column: subscribe_count
	 */
	public java.lang.Integer getSubscribeCount () {
		return subscribeCount;
	}

	/**
	 * Set the value related to the column: subscribe_count
	 * @param subscribeCount the subscribe_count value
	 */
	public void setSubscribeCount (java.lang.Integer subscribeCount) {
		this.subscribeCount = subscribeCount;
	}


	/**
	 * Return the value associated with the column: parent_id
	 */
	public com.jeecms.bbs.entity.BbsTopicType getParent () {
		return parent;
	}

	/**
	 * Set the value related to the column: parent_id
	 * @param parent the parent_id value
	 */
	public void setParent (com.jeecms.bbs.entity.BbsTopicType parent) {
		this.parent = parent;
	}

	public com.jeecms.core.entity.CmsSite getSite() {
		return site;
	}

	public void setSite(com.jeecms.core.entity.CmsSite site) {
		this.site = site;
	}

	public Set<com.jeecms.bbs.entity.BbsTopicType> getChild() {
		return child;
	}

	public void setChild(Set<com.jeecms.bbs.entity.BbsTopicType> child) {
		this.child = child;
	}

	public Set<com.jeecms.bbs.entity.BbsTopicTypeSubscribe> getSubscribes() {
		return subscribes;
	}

	public void setSubscribes(Set<com.jeecms.bbs.entity.BbsTopicTypeSubscribe> subscribes) {
		this.subscribes = subscribes;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsTopicType)) return false;
		else {
			com.jeecms.bbs.entity.BbsTopicType bbsTopicType = (com.jeecms.bbs.entity.BbsTopicType) obj;
			if (null == this.getId() || null == bbsTopicType.getId()) return false;
			else return (this.getId().equals(bbsTopicType.getId()));
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