package com.jeecms.bbs.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsMemberMagic;



public class BbsMemberMagic extends BaseBbsMemberMagic {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		json.put("id", getId());
		json.put("num", getNum());
		json.put("user", getUser().getUsername());
		json.put("magicId", getMagic().getId());
		json.put("magicName", getMagic().getName());
		json.put("magicIdentifier", getMagic().getIdentifier());
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsMemberMagic () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsMemberMagic (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsMemberMagic (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		com.jeecms.bbs.entity.BbsCommonMagic magic) {

		super (
			id,
			user,
			magic);
	}

/*[CONSTRUCTOR MARKER END]*/


}