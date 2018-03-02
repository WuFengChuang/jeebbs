package com.jeecms.plug.live.entity.base;

import java.io.Serializable;

import com.jeecms.core.entity.CmsSite;
import com.jeecms.plug.live.entity.BbsLiveChapter;


/**
 * This is an object that contains data related to the bbs_live table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="bbs_live"
 */

public abstract class BaseBbsLive  implements Serializable {

	public static String REF = "BbsLive";
	public static String PROP_LIVE_LOGO = "liveLogo";
	public static String PROP_AFTER_PRICE = "afterPrice";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_USER = "user";
	public static String PROP_END_TIME = "endTime";
	public static String PROP_CREATE_TIME = "createTime";
	public static String PROP_BEGIN_PRICE = "beginPrice";
	public static String PROP_CHECK_STATUS = "checkStatus";
	public static String PROP_JOIN_USER_NUM = "joinUserNum";
	public static String PROP_TITLE = "title";
	public static String PROP_BEGIN_TIME = "beginTime";
	public static String PROP_DEMAND_URL = "demandUrl";
	public static String PROP_TOTAL_AMOUNT = "totalAmount";
	public static String PROP_LIVE_URL = "liveUrl";
	public static String PROP_COMMISSION_RATE = "commissionRate";
	public static String PROP_ID = "id";
	public static String PROP_LIMIT_USER_NUM = "limitUserNum";


	// constructors
	public BaseBbsLive () {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseBbsLive (java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseBbsLive (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		java.lang.String title,
		java.util.Date createTime,
		java.util.Date beginTime,
		java.util.Date endTime,
		java.lang.Double beginPrice,
		java.lang.Double afterPrice,
		java.lang.Integer limitUserNum,
		java.lang.Double commissionRate,
		java.lang.Short checkStatus,
		java.lang.Integer joinUserNum,
		java.lang.Double totalAmount) {

		this.setId(id);
		this.setUser(user);
		this.setTitle(title);
		this.setCreateTime(createTime);
		this.setBeginTime(beginTime);
		this.setEndTime(endTime);
		this.setBeginPrice(beginPrice);
		this.setAfterPrice(afterPrice);
		this.setLimitUserNum(limitUserNum);
		this.setCommissionRate(commissionRate);
		this.setCheckStatus(checkStatus);
		this.setJoinUserNum(joinUserNum);
		this.setTotalAmount(totalAmount);
		initialize();
	}

	protected void initialize () {}



	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private java.lang.String title;
	private java.lang.String description;
	private java.lang.String liveLogo;
	private java.util.Date createTime;
	private java.util.Date beginTime;
	private java.util.Date endTime;
	private java.lang.Double beginPrice;
	private java.lang.Double afterPrice;
	private java.lang.Integer limitUserNum;
	private java.lang.Double commissionRate;
	private java.lang.Short checkStatus;
	private java.lang.Integer joinUserNum;
	private java.lang.Integer inliveUserNum;
	private java.lang.String liveUrl;
	private java.lang.String demandUrl;
	private java.lang.String demandImageUrl;
	private java.lang.String liveMobileUrl;
	private java.lang.String livePlatKey;
	private java.lang.Short livePlat;
	private java.lang.String reason;
	
	private java.lang.Double totalAmount;

	// many to one
	private com.jeecms.bbs.entity.BbsUser user;
	private BbsLiveChapter chapter;
	private com.jeecms.core.entity.CmsSite site;

	// collections
	private java.util.Set<com.jeecms.plug.live.entity.BbsLiveMessage> messages;
	private java.util.Set<com.jeecms.plug.live.entity.BbsLiveUser> joinUsers;
	private java.util.Set<com.jeecms.plug.live.entity.BbsLiveCharge> chargeSet;


	/**
	 * Return the unique identifier of this class
     * @hibernate.id
     *  generator-class="sequence"
     *  column="live_id"
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
	 * Return the value associated with the column: title
	 */
	public java.lang.String getTitle () {
		return title;
	}

	/**
	 * Set the value related to the column: title
	 * @param title the title value
	 */
	public void setTitle (java.lang.String title) {
		this.title = title;
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
	 * Return the value associated with the column: live_logo
	 */
	public java.lang.String getLiveLogo () {
		return liveLogo;
	}

	/**
	 * Set the value related to the column: live_logo
	 * @param liveLogo the live_logo value
	 */
	public void setLiveLogo (java.lang.String liveLogo) {
		this.liveLogo = liveLogo;
	}


	/**
	 * Return the value associated with the column: create_time
	 */
	public java.util.Date getCreateTime () {
		return createTime;
	}

	/**
	 * Set the value related to the column: create_time
	 * @param createTime the create_time value
	 */
	public void setCreateTime (java.util.Date createTime) {
		this.createTime = createTime;
	}


	/**
	 * Return the value associated with the column: begin_time
	 */
	public java.util.Date getBeginTime () {
		return beginTime;
	}

	/**
	 * Set the value related to the column: begin_time
	 * @param beginTime the begin_time value
	 */
	public void setBeginTime (java.util.Date beginTime) {
		this.beginTime = beginTime;
	}


	/**
	 * Return the value associated with the column: end_time
	 */
	public java.util.Date getEndTime () {
		return endTime;
	}

	/**
	 * Set the value related to the column: end_time
	 * @param endTime the end_time value
	 */
	public void setEndTime (java.util.Date endTime) {
		this.endTime = endTime;
	}


	/**
	 * Return the value associated with the column: begin_price
	 */
	public java.lang.Double getBeginPrice () {
		return beginPrice;
	}

	/**
	 * Set the value related to the column: begin_price
	 * @param beginPrice the begin_price value
	 */
	public void setBeginPrice (java.lang.Double beginPrice) {
		this.beginPrice = beginPrice;
	}


	/**
	 * Return the value associated with the column: after_price
	 */
	public java.lang.Double getAfterPrice () {
		return afterPrice;
	}

	/**
	 * Set the value related to the column: after_price
	 * @param afterPrice the after_price value
	 */
	public void setAfterPrice (java.lang.Double afterPrice) {
		this.afterPrice = afterPrice;
	}


	/**
	 * Return the value associated with the column: limit_user_num
	 */
	public java.lang.Integer getLimitUserNum () {
		return limitUserNum;
	}

	/**
	 * Set the value related to the column: limit_user_num
	 * @param limitUserNum the limit_user_num value
	 */
	public void setLimitUserNum (java.lang.Integer limitUserNum) {
		this.limitUserNum = limitUserNum;
	}


	/**
	 * Return the value associated with the column: commission_rate
	 */
	public java.lang.Double getCommissionRate () {
		return commissionRate;
	}

	/**
	 * Set the value related to the column: commission_rate
	 * @param commissionRate the commission_rate value
	 */
	public void setCommissionRate (java.lang.Double commissionRate) {
		this.commissionRate = commissionRate;
	}


	/**
	 * Return the value associated with the column: check_status
	 */
	public java.lang.Short getCheckStatus () {
		return checkStatus;
	}

	/**
	 * Set the value related to the column: check_status
	 * @param checkStatus the check_status value
	 */
	public void setCheckStatus (java.lang.Short checkStatus) {
		this.checkStatus = checkStatus;
	}


	/**
	 * Return the value associated with the column: join_user_num
	 */
	public java.lang.Integer getJoinUserNum () {
		return joinUserNum;
	}

	/**
	 * Set the value related to the column: join_user_num
	 * @param joinUserNum the join_user_num value
	 */
	public void setJoinUserNum (java.lang.Integer joinUserNum) {
		this.joinUserNum = joinUserNum;
	}


	public java.lang.Integer getInliveUserNum() {
		return inliveUserNum;
	}

	public void setInliveUserNum(java.lang.Integer inliveUserNum) {
		this.inliveUserNum = inliveUserNum;
	}

	/**
	 * Return the value associated with the column: live_url
	 */
	public java.lang.String getLiveUrl () {
		return liveUrl;
	}

	/**
	 * Set the value related to the column: live_url
	 * @param liveUrl the live_url value
	 */
	public void setLiveUrl (java.lang.String liveUrl) {
		this.liveUrl = liveUrl;
	}


	/**
	 * Return the value associated with the column: demand_url
	 */
	public java.lang.String getDemandUrl () {
		return demandUrl;
	}

	/**
	 * Set the value related to the column: demand_url
	 * @param demandUrl the demand_url value
	 */
	public void setDemandUrl (java.lang.String demandUrl) {
		this.demandUrl = demandUrl;
	}

	public java.lang.String getDemandImageUrl() {
		return demandImageUrl;
	}

	public void setDemandImageUrl(java.lang.String demandImageUrl) {
		this.demandImageUrl = demandImageUrl;
	}

	public java.lang.String getLiveMobileUrl() {
		return liveMobileUrl;
	}

	public void setLiveMobileUrl(java.lang.String liveMobileUrl) {
		this.liveMobileUrl = liveMobileUrl;
	}

	public java.lang.String getLivePlatKey() {
		return livePlatKey;
	}

	public void setLivePlatKey(java.lang.String livePlatKey) {
		this.livePlatKey = livePlatKey;
	}

	public java.lang.Short getLivePlat() {
		return livePlat;
	}

	public void setLivePlat(java.lang.Short livePlat) {
		this.livePlat = livePlat;
	}

	public java.lang.String getReason() {
		return reason;
	}

	public void setReason(java.lang.String reason) {
		this.reason = reason;
	}

	/**
	 * Return the value associated with the column: total_amount
	 */
	public java.lang.Double getTotalAmount () {
		return totalAmount;
	}

	/**
	 * Set the value related to the column: total_amount
	 * @param totalAmount the total_amount value
	 */
	public void setTotalAmount (java.lang.Double totalAmount) {
		this.totalAmount = totalAmount;
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

	public BbsLiveChapter getChapter() {
		return chapter;
	}

	public void setChapter(BbsLiveChapter chapter) {
		this.chapter = chapter;
	}

	public CmsSite getSite() {
		return site;
	}

	public void setSite(CmsSite site) {
		this.site = site;
	}

	/**
	 * Return the value associated with the column: messages
	 */
	public java.util.Set<com.jeecms.plug.live.entity.BbsLiveMessage> getMessages () {
		return messages;
	}

	/**
	 * Set the value related to the column: messages
	 * @param messages the messages value
	 */
	public void setMessages (java.util.Set<com.jeecms.plug.live.entity.BbsLiveMessage> messages) {
		this.messages = messages;
	}

	public java.util.Set<com.jeecms.plug.live.entity.BbsLiveUser> getJoinUsers() {
		return joinUsers;
	}

	public void setJoinUsers(java.util.Set<com.jeecms.plug.live.entity.BbsLiveUser> joinUsers) {
		this.joinUsers = joinUsers;
	}

	public java.util.Set<com.jeecms.plug.live.entity.BbsLiveCharge> getChargeSet() {
		return chargeSet;
	}

	public void setChargeSet(java.util.Set<com.jeecms.plug.live.entity.BbsLiveCharge> chargeSet) {
		this.chargeSet = chargeSet;
	}

	public boolean equals (Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof com.jeecms.plug.live.entity.BbsLive)) return false;
		else {
			com.jeecms.plug.live.entity.BbsLive bbsLive = (com.jeecms.plug.live.entity.BbsLive) obj;
			if (null == this.getId() || null == bbsLive.getId()) return false;
			else return (this.getId().equals(bbsLive.getId()));
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