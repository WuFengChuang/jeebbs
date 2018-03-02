package com.jeecms.bbs.entity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsVoteItem;



public class BbsVoteItem extends BaseBbsVoteItem {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		json.put("id", getId());
		json.put("name", getName());
		json.put("voteCount", getVoteCount());
		json.put("percent", getPercent());
		return json;
	}
	
	public int getPercent() {
		Integer totalCount = getTopic().getTotalCount();
		if (totalCount != null && totalCount != 0) {
			return (getVoteCount() * 100) / totalCount;
		} else {
			return 0;
		}
	}
	
	public void init(){
		if(getVoteCount()==null){
			setVoteCount(0);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsVoteItem () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsVoteItem (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsVoteItem (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsVoteTopic topic,
		java.lang.String name,
		java.lang.Integer voteCount) {

		super (
			id,
			topic,
			name,
			voteCount);
	}

/*[CONSTRUCTOR MARKER END]*/


}