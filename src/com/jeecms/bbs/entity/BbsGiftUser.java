package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsGiftUser;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;



public class BbsGiftUser extends BaseBbsGiftUser {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		json.put("id", getId());
		json.put("user", getUser().getUsername());
		json.put("giftId", getGift().getId());
		json.put("giftName",  getGift().getName());
		json.put("num",  getNum());
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsGiftUser () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsGiftUser (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsGiftUser (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		com.jeecms.bbs.entity.BbsGift gift,
		java.lang.Integer num) {

		super (
			id,
			user,
			gift,
			num);
	}

/*[CONSTRUCTOR MARKER END]*/


}