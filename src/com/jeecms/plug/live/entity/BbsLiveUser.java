package com.jeecms.plug.live.entity;

import com.jeecms.plug.live.entity.base.BaseBbsLiveUser;



public class BbsLiveUser extends BaseBbsLiveUser {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsLiveUser () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsLiveUser (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsLiveUser (
		java.lang.Long id,
		com.jeecms.plug.live.entity.BbsLive live,
		com.jeecms.bbs.entity.BbsUser joinUser,
		java.util.Date buyTime) {

		super (
			id,
			live,
			joinUser,
			buyTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}