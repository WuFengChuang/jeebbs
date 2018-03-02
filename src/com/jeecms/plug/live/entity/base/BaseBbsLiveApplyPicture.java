package com.jeecms.plug.live.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the bbs_live_apply table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_live_apply"
 */

public abstract class BaseBbsLiveApplyPicture  implements Serializable {

	public static String REF = "BbsLiveApplyPicture";
	public static String PROP_IMG_PATH = "imgPath";
	public static String PROP_DESCRIPTION = "description";


	// constructors
	public BaseBbsLiveApplyPicture () {
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsLiveApplyPicture (
		java.lang.String imgPath) {

		this.setImgPath(imgPath);
		initialize();
	}

	protected void initialize () {}



	// fields
	private java.lang.String imgPath;
	private java.lang.String description;






	/**
	 * Return the value associated with the column: img_path
	 */
	public java.lang.String getImgPath () {
		return imgPath;
	}

	/**
	 * Set the value related to the column: img_path
	 * @param imgPath the img_path value
	 */
	public void setImgPath (java.lang.String imgPath) {
		this.imgPath = imgPath;
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






	public String toString () {
		return super.toString();
	}


}