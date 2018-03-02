package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsGrade;
import com.jeecms.common.util.DateUtils;



public class BbsGrade extends BaseBbsGrade {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() throws JSONException {
		JSONObject json=new JSONObject();
		json.put("id", getId());
		json.put("score", getScore());
		json.put("gradeTime", DateUtils.parseDateToTimeStr(getGradeTime()));
		json.put("graderUser", getGrader().getUsername());
		if(StringUtils.isNotBlank(getReason())){
			json.put("reason", getReason());
		}else{
			json.put("reason","");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsGrade () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsGrade (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsGrade (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsPost post,
		com.jeecms.bbs.entity.BbsUser grader) {

		super (
			id,
			post,
			grader);
	}

/*[CONSTRUCTOR MARKER END]*/


}