package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseApiAccount;



public class ApiAccount extends BaseApiAccount {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getAppId())) {
			json.put("setAppId", getAppId());
		}else{
			json.put("setAppId", "");
		}
		if (getDisabled()!=null) {
			json.put("disabled", getDisabled());
		}else{
			json.put("disabled", "");
		}
		if (StringUtils.isNotBlank(getAppKey())) {
			json.put("appKey", getAppKey());
		}else{
			json.put("appKey", "");
		}
		if (StringUtils.isNotBlank(getAesKey())) {
			json.put("aesKey", getAesKey());
		}else{
			json.put("aesKey", "");
		}
		if (StringUtils.isNotBlank(getIvKey())) {
			json.put("ivKey", getIvKey());
		}else{
			json.put("ivKey", "");
		}
		return json;
	}
	
	public void init(){
		if (getDisabled()==null) {
			setDisabled(false);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ApiAccount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ApiAccount (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ApiAccount (
		java.lang.Integer id,
		java.lang.String appId,
		java.lang.String appKey,
		java.lang.Boolean disabled) {

		super (
			id,
			appId,
			appKey,
			disabled);
	}

/*[CONSTRUCTOR MARKER END]*/


}