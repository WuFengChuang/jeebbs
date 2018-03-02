package com.jeecms.bbs.entity;

import java.util.Calendar;
import java.util.Date;

import com.jeecms.bbs.entity.base.BaseBbsSession;



public class BbsSession extends BaseBbsSession {
	private static final long serialVersionUID = 1L;
	
	public boolean isOnline(){
		Date lastActiveTime=getLastActivetime();
		Date now=Calendar.getInstance().getTime();
		long interval=now.getTime()-lastActiveTime.getTime();
		long intervalMinu=interval/1000/60;
		if(intervalMinu>20){
			return false;
		}else{
			return true;
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsSession () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsSession (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsSession (
		java.lang.Long id,
		java.lang.String sessionId,
		java.lang.String ip,
		java.util.Date firstActivetime,
		java.util.Date lastActivetime) {

		super (
			id,
			sessionId,
			ip,
			firstActivetime,
			lastActivetime);
	}

/*[CONSTRUCTOR MARKER END]*/


}