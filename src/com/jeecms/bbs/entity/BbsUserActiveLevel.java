package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsUserActiveLevel;



public class BbsUserActiveLevel extends BaseBbsUserActiveLevel {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getLevelName())) {
			json.put("levelName", getLevelName());
		}else{
			json.put("levelName", "");
		}
		if (getRequiredHour()!=null) {
			json.put("requiredHour", getRequiredHour());
		}else{
			json.put("requiredHour", "");
		}
		if (StringUtils.isNotBlank(getLevelImg())) {
			json.put("levelImg", getLevelImg());
		}else{
			json.put("levelImg", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsUserActiveLevel () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsUserActiveLevel (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsUserActiveLevel (
		java.lang.Integer id,
		java.lang.String levelName,
		java.lang.Long requiredHour) {

		super (
			id,
			levelName,
			requiredHour);
	}

/*[CONSTRUCTOR MARKER END]*/


}