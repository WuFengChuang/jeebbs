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

public abstract class BaseBbsLiveApply  implements Serializable {

	public static String REF = "BbsLiveApply";
	public static String PROP_STATUS = "status";
	public static String PROP_APPLY_TIME = "applyTime";
	public static String PROP_CHECK_TIME = "checkTime";
	public static String PROP_EXPERIENCE = "experience";
	public static String PROP_APPLY_USER = "applyUser";
	public static String PROP_INTRO = "intro";
	public static String PROP_ADDRESS = "address";
	public static String PROP_CHECK_USER = "checkUser";
	public static String PROP_ID = "id";
	public static String PROP_BRIEF = "brief";
	public static String PROP_MOBILE = "mobile";


	// constructors
	public BaseBbsLiveApply () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsLiveApply (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsLiveApply (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser checkUser,
		com.jeecms.bbs.entity.BbsUser applyUser,
		java.lang.String brief,
		java.util.Date applyTime,
		java.lang.Short status) {

		this.setId(id);
		this.setCheckUser(checkUser);
		this.setApplyUser(applyUser);
		this.setBrief(brief);
		this.setApplyTime(applyTime);
		this.setStatus(status);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String intro;
	private java.lang.String brief;
	private java.lang.String experience;
	private java.lang.String mobile;
	private java.lang.String address;
	private java.util.Date applyTime;
	private java.util.Date checkTime;
	private java.lang.Short status;

	// many to one
	private com.jeecms.bbs.entity.BbsUser checkUser;
	private com.jeecms.bbs.entity.BbsUser applyUser;

	// collections
	private java.util.List<com.jeecms.plug.live.entity.BbsLiveApplyPicture> pictures;



	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="apply_id"
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
	 * Return the value associated with the column: intro
	 */
	public java.lang.String getIntro () {
		return intro;
	}

	/**
	 * Set the value related to the column: intro
	 * @param intro the intro value
	 */
	public void setIntro (java.lang.String intro) {
		this.intro = intro;
	}


	/**
	 * Return the value associated with the column: brief
	 */
	public java.lang.String getBrief () {
		return brief;
	}

	/**
	 * Set the value related to the column: brief
	 * @param brief the brief value
	 */
	public void setBrief (java.lang.String brief) {
		this.brief = brief;
	}


	/**
	 * Return the value associated with the column: experience
	 */
	public java.lang.String getExperience () {
		return experience;
	}

	/**
	 * Set the value related to the column: experience
	 * @param experience the experience value
	 */
	public void setExperience (java.lang.String experience) {
		this.experience = experience;
	}


	/**
	 * Return the value associated with the column: mobile
	 */
	public java.lang.String getMobile () {
		return mobile;
	}

	/**
	 * Set the value related to the column: mobile
	 * @param mobile the mobile value
	 */
	public void setMobile (java.lang.String mobile) {
		this.mobile = mobile;
	}


	/**
	 * Return the value associated with the column: address
	 */
	public java.lang.String getAddress () {
		return address;
	}

	/**
	 * Set the value related to the column: address
	 * @param address the address value
	 */
	public void setAddress (java.lang.String address) {
		this.address = address;
	}


	/**
	 * Return the value associated with the column: apply_time
	 */
	public java.util.Date getApplyTime () {
		return applyTime;
	}

	/**
	 * Set the value related to the column: apply_time
	 * @param applyTime the apply_time value
	 */
	public void setApplyTime (java.util.Date applyTime) {
		this.applyTime = applyTime;
	}


	/**
	 * Return the value associated with the column: check_time
	 */
	public java.util.Date getCheckTime () {
		return checkTime;
	}

	/**
	 * Set the value related to the column: check_time
	 * @param checkTime the check_time value
	 */
	public void setCheckTime (java.util.Date checkTime) {
		this.checkTime = checkTime;
	}


	/**
	 * Return the value associated with the column: status
	 */
	public java.lang.Short getStatus () {
		return status;
	}

	/**
	 * Set the value related to the column: status
	 * @param status the status value
	 */
	public void setStatus (java.lang.Short status) {
		this.status = status;
	}


	/**
	 * Return the value associated with the column: check_user_id
	 */
	public com.jeecms.bbs.entity.BbsUser getCheckUser () {
		return checkUser;
	}

	/**
	 * Set the value related to the column: check_user_id
	 * @param checkUser the check_user_id value
	 */
	public void setCheckUser (com.jeecms.bbs.entity.BbsUser checkUser) {
		this.checkUser = checkUser;
	}


	/**
	 * Return the value associated with the column: apply_user_id
	 */
	public com.jeecms.bbs.entity.BbsUser getApplyUser () {
		return applyUser;
	}

	/**
	 * Set the value related to the column: apply_user_id
	 * @param applyUser the apply_user_id value
	 */
	public void setApplyUser (com.jeecms.bbs.entity.BbsUser applyUser) {
		this.applyUser = applyUser;
	}


	/**
	 * Return the value associated with the column: pictures
	 */
	public java.util.List<com.jeecms.plug.live.entity.BbsLiveApplyPicture> getPictures () {
		return pictures;
	}

	/**
	 * Set the value related to the column: pictures
	 * @param pictures the pictures value
	 */
	public void setPictures (java.util.List<com.jeecms.plug.live.entity.BbsLiveApplyPicture> pictures) {
		this.pictures = pictures;
	}



	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.plug.live.entity.BbsLiveApply)) return false;
		else {
			com.jeecms.plug.live.entity.BbsLiveApply bbsLiveApply = (com.jeecms.plug.live.entity.BbsLiveApply) obj;
			if (null == this.getId() || null == bbsLiveApply.getId()) return false;
			else return (this.getId().equals(bbsLiveApply.getId()));
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