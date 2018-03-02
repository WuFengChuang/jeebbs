package com.jeecms.bbs.entity;


import com.jeecms.bbs.entity.base.BaseBbsUserOnline;



public class BbsUserOnline extends BaseBbsUserOnline {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsUserOnline () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsUserOnline (java.lang.Integer id) {
		super(id);
	}
	public void initial(){
		setOnlineDay(0d);
		setOnlineLatest(0d);
		setOnlineMonth(0d);
		setOnlineWeek(0d);
		setOnlineYear(0d);
		setOnlineTotal(0d);
	}
/*[CONSTRUCTOR MARKER END]*/


}