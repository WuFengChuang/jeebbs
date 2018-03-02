package com.jeecms.bbs.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsCreditExchange;



public class BbsCreditExchange extends BaseBbsCreditExchange {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getExpoint()!=null) {
			json.put("expoint", getExpoint());
		}else{
			json.put("expoint", "");
		}
		if (getExprestige()!=null) {
			json.put("exprestige", getExprestige());
		}else{
			json.put("exprestige", "");
		}
		if (getExmoney()!=null) {
			json.put("exmoney", getExmoney());
		}else{
			json.put("exmoney", "");
		}
		if (getPointoutavailable()!=null) {
			json.put("pointoutavailable", getPointoutavailable());
		}else{
			json.put("pointoutavailable", "");
		}
		if (getPointinavailable()!=null) {
			json.put("pointinavailable", getPointinavailable());
		}else{
			json.put("pointinavailable", "");
		}
		if (getPrestigeoutavailable()!=null) {
			json.put("prestigeoutavailable", getPrestigeoutavailable());
		}else{
			json.put("prestigeoutavailable", "");
		}
		if (getPrestigeinavailable()!=null) {
			json.put("prestigeinavailable", getPrestigeinavailable());
		}else{
			json.put("prestigeinavailable", "");
		}
		if (getExchangetax()!=null) {
			json.put("exchangetax", getExchangetax());
		}else{
			json.put("exchangetax", "");
		}
		if (getMiniBalance()!=null) {
			json.put("miniBalance", getMiniBalance());
		}else{
			json.put("miniBalance", "");
		}
		return json;
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsCreditExchange () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsCreditExchange (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsCreditExchange (
		java.lang.Integer id,
		java.lang.Integer expoint,
		java.lang.Integer exprestige,
		java.lang.Boolean pointoutavailable,
		java.lang.Boolean pointinavailable,
		java.lang.Boolean prestigeoutavailable,
		java.lang.Boolean prestigeinavailable,
		java.lang.Float exchangetax,
		java.lang.Integer miniBalance) {

		super (
			id,
			expoint,
			exprestige,
			pointoutavailable,
			pointinavailable,
			prestigeoutavailable,
			prestigeinavailable,
			exchangetax,
			miniBalance);
	}

/*[CONSTRUCTOR MARKER END]*/


}