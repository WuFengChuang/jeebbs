package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsThirdAccount;
import com.jeecms.core.entity.CmsSite;

public class BbsThirdAccount extends BaseBbsThirdAccount {
	private static final long serialVersionUID = 1L;
	
	public static final String QQ_KEY="openId";
	public static final String SINA_KEY="uid";
	public static final String QQ_PLAT="QQ";
	public static final String SINA_PLAT="SINA";
	public static final String WEIXIN_PLAT="WEIXIN";
	public static final String QQ_WEBO_KEY="weboOpenId";
	public static final String QQ_WEBO_PLAT="QQWEBO";
	public static final String WEIXIN_KEY="weixinOpenId";
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
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
		if (StringUtils.isNotBlank(getAccountKey())) {
			json.put("accountKey", getAccountKey());
		}else{
			json.put("accountKey", "");
		}
		if (StringUtils.isNotBlank(getSource())) {
			json.put("source", getSource());
		}else{
			json.put("source", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getComefrom())) {
			json.put("comefrom", getUser().getComefrom());
		}else{
			json.put("comefrom", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getRealname())) {
			json.put("realname", getUser().getRealname());
		}else{
			json.put("realname", "");
		}
		if (getUser().getGender()!=null) {
			json.put("gender", getUser().getGender());
		}else{
			json.put("gender", false);
		}
		return json;
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsThirdAccount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsThirdAccount (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsThirdAccount (
		java.lang.Long id,
		java.lang.String accountKey,
		java.lang.String username,
		java.lang.String source) {

		super (
			id,
			accountKey,
			username,
			source);
	}

/*[CONSTRUCTOR MARKER END]*/


}