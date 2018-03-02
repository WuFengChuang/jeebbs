package com.jeecms.plug.live.entity;

import com.jeecms.plug.live.entity.base.BaseBbsLiveRate;



public class BbsLiveRate extends BaseBbsLiveRate {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsLiveRate () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsLiveRate (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsLiveRate (
		java.lang.Integer id,
		java.lang.Integer userNum,
		java.lang.Double rate) {

		super (
			id,
			userNum,
			rate);
	}

/*[CONSTRUCTOR MARKER END]*/


}