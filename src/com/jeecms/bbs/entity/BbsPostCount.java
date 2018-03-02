package com.jeecms.bbs.entity;

import com.jeecms.bbs.entity.base.BaseBbsPostCount;



public class BbsPostCount extends BaseBbsPostCount {
	private static final long serialVersionUID = 1L;
	
	public void init() {
		if(getUps()==null){
			setUps(0);
		}
		if(getReplys()==null){
			setReplys(0);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsPostCount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsPostCount (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsPostCount (
		java.lang.Integer id,
		java.lang.Integer replys,
		java.lang.Integer ups) {

		super (
			id,
			replys,
			ups);
	}

/*[CONSTRUCTOR MARKER END]*/


}