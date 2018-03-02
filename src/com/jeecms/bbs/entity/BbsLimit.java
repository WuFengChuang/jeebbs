package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsLimit;



public class BbsLimit extends BaseBbsLimit {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getIp())) {
			json.put("ip", getIp());
		}else{
			json.put("ip", "");
		}
		if (getUserId()!=null) {
			json.put("userId", getUserId());
		}else{
			json.put("userId", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsLimit () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsLimit (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/


}