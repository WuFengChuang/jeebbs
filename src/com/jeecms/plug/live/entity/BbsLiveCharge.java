package com.jeecms.plug.live.entity;

import com.jeecms.plug.live.entity.base.BaseBbsLiveCharge;



public class BbsLiveCharge extends BaseBbsLiveCharge {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsLiveCharge () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsLiveCharge (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsLiveCharge (
		java.lang.Integer id,
		java.lang.Double totalAmount,
		java.lang.Double yearAmount,
		java.lang.Double monthAmount,
		java.lang.Double dayAmount) {

		super (
			id,
			totalAmount,
			yearAmount,
			monthAmount,
			dayAmount);
	}

/*[CONSTRUCTOR MARKER END]*/


}