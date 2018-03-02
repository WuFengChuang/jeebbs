package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_advertising_space table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jb_advertising_space"
 */

public abstract class BaseBbsAdvertisingSpace  implements Serializable {

	public static String REF = "BbsAdvertisingSpace";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_SITE = "site";
	public static String PROP_ENABLED = "enabled";
	public static String PROP_NAME = "name";
	public static String PROP_ID = "id";


	// constructors
	public BaseBbsAdvertisingSpace () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsAdvertisingSpace (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsAdvertisingSpace (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		java.lang.String name,
		java.lang.Boolean enabled) {

		this.setId(id);
		this.setSite(site);
		this.setName(name);
		this.setEnabled(enabled);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String name;
	private java.lang.String description;
	private java.lang.String previewImg;
	private java.lang.Boolean enabled;

	// many to one
	private com.jeecms.core.entity.CmsSite site;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="identity"
     *  column="adspace_id"
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
	 * Return the value associated with the column: ad_name
	 */
	public java.lang.String getName () {
		return name;
	}

	/**
	 * Set the value related to the column: ad_name
	 * @param name the ad_name value
	 */
	public void setName (java.lang.String name) {
		this.name = name;
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


	public java.lang.String getPreviewImg() {
		return previewImg;
	}

	public void setPreviewImg(java.lang.String previewImg) {
		this.previewImg = previewImg;
	}

	/**
	 * Return the value associated with the column: is_enabled
	 */
	public java.lang.Boolean getEnabled () {
		return enabled;
	}

	/**
	 * Set the value related to the column: is_enabled
	 * @param enabled the is_enabled value
	 */
	public void setEnabled (java.lang.Boolean enabled) {
		this.enabled = enabled;
	}


	/**
	 * Return the value associated with the column: site_id
	 */
	public com.jeecms.core.entity.CmsSite getSite () {
		return site;
	}

	/**
	 * Set the value related to the column: site_id
	 * @param site the site_id value
	 */
	public void setSite (com.jeecms.core.entity.CmsSite site) {
		this.site = site;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.bbs.entity.BbsAdvertisingSpace)) return false;
		else {
			com.jeecms.bbs.entity.BbsAdvertisingSpace bbsAdvertisingSpace = (com.jeecms.bbs.entity.BbsAdvertisingSpace) obj;
			if (null == this.getId() || null == bbsAdvertisingSpace.getId()) return false;
			else return (this.getId().equals(bbsAdvertisingSpace.getId()));
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