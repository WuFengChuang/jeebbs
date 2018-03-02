package com.jeecms.bbs.entity;

import com.jeecms.bbs.entity.base.BaseBbsTopicCount;



public class BbsTopicCount extends BaseBbsTopicCount {
	private static final long serialVersionUID = 1L;


	public void init() {
		if(getCollections()==null){
			setCollections(0);
		}
		if(getRewards()==null){
			setRewards(0);
		}
		if(getUps()==null){
			setUps(0);
		}
		if(getAttentions()==null){
			setAttentions(0);
		}
		
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsTopicCount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsTopicCount (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsTopicCount (
		java.lang.Integer id,
		java.lang.Integer ups,
		java.lang.Integer collections,
		java.lang.Integer rewards,
		java.lang.Integer attentions) {

		super (
			id,
			ups,
			collections,
			rewards,
			attentions);
	}

/*[CONSTRUCTOR MARKER END]*/


}