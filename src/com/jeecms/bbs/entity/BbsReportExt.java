package com.jeecms.bbs.entity;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsReportExt;
import com.jeecms.common.util.DateUtils;



public class BbsReportExt extends BaseBbsReportExt {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsReportExt () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsReportExt (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsReportExt (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser reportUser,
		com.jeecms.bbs.entity.BbsReport report,
		java.util.Date reportTime) {

		super (
			id,
			reportUser,
			report,
			reportTime);
	}

	public JSONObject convertToJson() throws JSONException {
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getReportUser()!=null&&StringUtils.isNotBlank(getReportUser().getUsername())) {
			json.put("username", getReportUser().getUsername());
		}else{
			json.put("username", "");
		}
		if (StringUtils.isNotBlank(getReportReason())) {
			json.put("reportReason", getReportReason());
		}else{
			json.put("reportReason", "");
		}
		if (getReportTime()!=null) {
			json.put("reportTime", DateUtils.parseDateToDateStr(getReportTime()));
		}else{
			json.put("reportTime", "");
		}
		if (getReport()!=null&&getReport().getId()!=null) {
			json.put("reportId", getReport().getId());
		}else{
			json.put("reportId", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER END]*/


}