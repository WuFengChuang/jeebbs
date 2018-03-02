package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_user_active_level table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_user_active_level"
 */

public abstract class BaseBbsUserActiveLevel  implements Serializable {

	public static String REF = "BbsUserActiveLevel";
	public static String PROP_REQUIRED_HOUR = "requiredHour";
	public static String PROP_LEVEL_NAME = "levelName";
	public static String PROP_ID = "id";
	public static String PROP_LEVEL_IMG = "levelImg";


	// constructors
	public BaseBbsUserActiveLevel () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsUserActiveLevel (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsUserActiveLevel (
		java.lang.Integer id,
		java.lang.String levelName,
		java.lang.Long requiredHour) {

		this.setId(id);
		this.setLevelName(levelName);
		this.setRequiredHour(requiredHour);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String levelName;
	private java.lang.Long requiredHour;
	private java.lang.String levelImg;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="level_id"
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
	 * Return the value associated with the column: level_name
	 */
	public java.lang.String getLevelName () {
		return levelName;
	}

	/**
	 * Set the value related to the column: level_name
	 * @param levelName the level_name value
	 */
	public void setLevelName (java.lang.String levelName) {
		this.levelName = levelName;
	}


	/**
	 * Return the value associated with the column: required_hour
	 */
	public java.lang.Long getRequiredHour () {
		return requiredHour;
	}

	/**
	 * Set the value related to the column: required_hour
	 * @param requiredHour the required_hour value
	 */
	public void setRequiredHour (java.lang.Long requiredHour) {
		this.requiredHour = requiredHour;
	}


	/**
	 * Return the value associated with the column: level_img
	 */
	public java.lang.String getLevelImg () {
		return levelImg;
	}

	/**
	 * Set the value related to the column: level_img
	 * @param levelImg the level_img value
	 */
	public void setLevelImg (java.lang.String levelImg) {
		this.levelImg = levelImg;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsUserActiveLevel)) return false;
		else {
			com.jeecms.bbs.entity.BbsUserActiveLevel bbsUserActiveLevel = (com.jeecms.bbs.entity.BbsUserActiveLevel) obj;
			if (null == this.getId() || null == bbsUserActiveLevel.getId()) return false;
			else return (this.getId().equals(bbsUserActiveLevel.getId()));
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