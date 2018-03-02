package com.jeecms.bbs.entity;

import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsReport;
import com.jeecms.core.entity.CmsSite;



public class BbsReport extends BaseBbsReport {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(Integer https,CmsSite site) throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getReportUrl())) {
			json.put("reportUrl", getReportUrl());
		}else{
			json.put("reportUrl", "");
		}
		if (getProcessTime()!=null) {
			json.put("processTime", getProcessTime());
		}else{
			json.put("processTime", "");
		}
		if (StringUtils.isNotBlank(getProcessResult())) {
			json.put("processResult", getProcessResult());
		}else{
			json.put("processResult", "");
		}
		json.put("status", getStatus());
		if (getReportTime()!=null) {
			json.put("reportTime", getReportTime());
		}else{
			json.put("reportTime", "");
		}
		if (getReportExt()!=null
				&&getReportExt().getReportUser()!=null
				&&StringUtils.isNotBlank(getReportExt().getReportUser().getUsername())) {
			json.put("reportUserName", getReportExt().getReportUser().getUsername());
		}else{
			json.put("reportUserName", "");
		}
		json.put("point", 0);
		return json;
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsReport () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsReport (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsReport (
		java.lang.Integer id,
		java.lang.String reportUrl,
		java.util.Date reportTime) {

		super (
			id,
			reportUrl,
			reportTime);
	}
	public BbsReportExt getReportExt(){
		Set<BbsReportExt>sets=getBbsReportExtSet(); 
		Iterator<BbsReportExt>it = null;
		if (sets!=null) {
			it=sets.iterator();
		}
		if (it!=null) {
			return it.next();
		}
		return null;
	}

/*[CONSTRUCTOR MARKER END]*/


}