package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsWebserviceAuth;



public class BbsWebserviceAuth extends BaseBbsWebserviceAuth {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getUsername())) {
			json.put("username", getUsername());
		}else{
			json.put("username", "");
		}
		if (StringUtils.isNotBlank(getSystem())) {
			json.put("system", getSystem());
		}else{
			json.put("system", "");
		}
		if (StringUtils.isNotBlank(getPassword())) {
			json.put("password", getPassword());
		}else{
			json.put("password", "");
		}
		if (getEnable()!=null) {
			json.put("enable", getEnable());
		}else{
			json.put("enable", "");
		}
		return json;
	}
	
	public void init(){
		if (getEnable()==null) {
			setEnable(false);
		}
	}
	
	public Boolean getEnable(){
		return super.isEnable();
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsWebserviceAuth () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsWebserviceAuth (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsWebserviceAuth (
		java.lang.Integer id,
		java.lang.String username,
		java.lang.String password,
		java.lang.String system,
		boolean enable) {

		super (
			id,
			username,
			password,
			system,
			enable);
	}

/*[CONSTRUCTOR MARKER END]*/


}