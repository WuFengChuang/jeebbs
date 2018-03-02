package com.jeecms.bbs.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsForumCount;
import com.jeecms.common.util.DateUtils;



public class BbsForumCount extends BaseBbsForumCount {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsForumCount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsForumCount (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsForumCount (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsForum forum,
		java.lang.Integer topicCount,
		java.lang.Integer postCount,
		java.lang.Integer visitCount,
		java.util.Date countDate) {

		super (
			id,
			forum,
			topicCount,
			postCount,
			visitCount,
			countDate);
	}

	public JSONObject convertToJson() throws JSONException {
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getTopicCount()!=null) {
			json.put("topicCount", getTopicCount());
		}else{
			json.put("topicCount", "");
		}
		if (getPostCount()!=null) {
			json.put("postCount", getPostCount());
		}else{
			json.put("postCount", "");
		}
		if (getVisitCount()!=null) {
			json.put("visitCount", getVisitCount());
		}else{
			json.put("visitCount", "");
		}
		if (getCountDate()!=null) {
			json.put("countDate", DateUtils.parseDateToTimeStr(getCountDate()));
		}else{
			json.put("countDate", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER END]*/


}