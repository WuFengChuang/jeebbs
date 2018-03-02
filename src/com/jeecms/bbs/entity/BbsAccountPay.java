package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsAccountPay;
import com.jeecms.common.util.DateUtils;



public class BbsAccountPay extends BaseBbsAccountPay {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsAccountPay () {
		super();
	}
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getDrawNum())) {
			json.put("drawNum", getDrawNum());
		}else{
			json.put("drawNum", "");
		}
		if (StringUtils.isNotBlank(getPayAccount())) {
			json.put("payAccount", getPayAccount());
		}else{
			json.put("payAccount", "");
		}
		if (StringUtils.isNotBlank(getDrawAccount())) {
			json.put("drawAccount", getDrawAccount());
		}else{
			json.put("drawAccount", "");
		}
		if (getPayTime()!=null) {
			json.put("payTime", DateUtils.parseDateToTimeStr(getPayTime()));
		}else{
			json.put("payTime", "");
		}
		if (StringUtils.isNotBlank(getWeixinNum())) {
			json.put("weixinNum", getWeixinNum());
		}else{
			json.put("weixinNum", "");
		}
		if (StringUtils.isNotBlank(getAlipayNum())) {
			json.put("alipayNum", getAlipayNum());
		}else{
			json.put("alipayNum", "");
		}
		return json;
	}
	
	/**
	 * Constructor for primary key
	 */
	public BbsAccountPay (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsAccountPay (
		java.lang.Long id,
		com.jeecms.bbs.entity.BbsUser payUser,
		com.jeecms.bbs.entity.BbsUser drawUser,
		java.lang.String drawNum,
		java.lang.String payAccount,
		java.lang.String drawAccount,
		java.util.Date payTime) {

		super (
			id,
			payUser,
			drawUser,
			drawNum,
			payAccount,
			drawAccount,
			payTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}