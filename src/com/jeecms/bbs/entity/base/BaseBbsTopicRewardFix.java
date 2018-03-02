package com.jeecms.bbs.entity.base;

import java.io.Serializable;


/**
 * This is an object that contains data related to the jc_content table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_content"
 */

public abstract class BaseBbsTopicRewardFix  implements Serializable {


	// constructors
	public BaseBbsTopicRewardFix () {
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsTopicRewardFix (
		java.lang.Double fixVal) {
		this.setFixVal(fixVal);
		initialize();
	}

	protected void initialize () {}



	// fields
	private java.lang.Double fixVal;
	

	public java.lang.Double getFixVal() {
		return fixVal;
	}

	public void setFixVal(java.lang.Double fixVal) {
		this.fixVal = fixVal;
	}

	public String toString () {
		return super.toString();
	}


}