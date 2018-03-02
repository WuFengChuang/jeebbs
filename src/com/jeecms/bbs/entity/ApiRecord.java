package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseApiRecord;
import com.jeecms.common.util.DateUtils;



public class ApiRecord extends BaseApiRecord {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getApiAccount()!=null&&StringUtils.isNotBlank(getApiAccount().getAppId())) {
			json.put("apiAccountId", getApiAccount().getAppId());
		}else{
			json.put("apiAccountId", "");
		}
		if (getApiInfo()!=null&&StringUtils.isNotBlank(getApiInfo().getName())) {
			json.put("apiInfoName", getApiInfo().getName());
		}else{
			json.put("apiInfoName", "");
		}
		if (StringUtils.isNotBlank(getCallIp())) {
			json.put("callIp", getCallIp());
		}else{
			json.put("callIp", "");
		}
		if (getCallTime()!=null) {
			json.put("callTime", DateUtils.parseDateToDateStr(getCallTime()));
		}else{
			json.put("callTime", "");
		}
		return json;
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public ApiRecord () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ApiRecord (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public ApiRecord (
		java.lang.Long id,
		com.jeecms.bbs.entity.ApiAccount apiAccount,
		com.jeecms.bbs.entity.ApiInfo apiInfo,
		java.util.Date callTime,
		java.lang.Long callTimeStamp) {

		super (
			id,
			apiAccount,
			apiInfo,
			callTime,
			callTimeStamp);
	}

/*[CONSTRUCTOR MARKER END]*/


}