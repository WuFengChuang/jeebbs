package com.jeecms.plug.live.entity;

import com.jeecms.plug.live.entity.base.BaseBbsLiveMessage;



public class BbsLiveMessage extends BaseBbsLiveMessage {
	private static final long serialVersionUID = 1L;
	
	public static final Short MST_TYPE_TEXT = 0;
	public static final Short MST_TYPE_IMAGE = 1;
	public static final Short MST_TYPE_GIFT = 2;
	
	public static final String SYSTEM_MSG_LIVE_STOP = "_liveStop";
	

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsLiveMessage () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsLiveMessage (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsLiveMessage (
		java.lang.Long id,
		com.jeecms.plug.live.entity.BbsLive live,
		com.jeecms.bbs.entity.BbsUser user,
		java.util.Date msgTime,
		java.lang.Short msgType) {

		super (
			id,
			live,
			user,
			msgTime,
			msgType);
	}

/*[CONSTRUCTOR MARKER END]*/


}