package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsWebserviceCallRecord;
import com.jeecms.common.util.DateUtils;



public class BbsWebserviceCallRecord extends BaseBbsWebserviceCallRecord {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getServiceCode())) {
			json.put("operate", getServiceCode());
		}else{
			json.put("operate", "");
		}
		if (getAuth()!=null&&StringUtils.isNotBlank(getAuth().getUsername())) {
			json.put("username", getAuth().getUsername());
		}else{
			json.put("username", "");
		}
		if (getAuth()!=null&&StringUtils.isNotBlank(getAuth().getSystem())) {
			json.put("system", getAuth().getSystem());
		}else{
			json.put("system", "");
		}
		if (getRecordTime()!=null) {
			json.put("recordTime", DateUtils.parseDateToDateStr(getRecordTime()));
		}else{
			json.put("recordTime", "");
		}
		return json;
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsWebserviceCallRecord () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsWebserviceCallRecord (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsWebserviceCallRecord (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsWebserviceAuth auth,
		java.lang.String serviceCode,
		java.util.Date recordTime) {

		super (
			id,
			auth,
			serviceCode,
			recordTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}