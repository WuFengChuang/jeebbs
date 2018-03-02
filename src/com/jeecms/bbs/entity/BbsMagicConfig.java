package com.jeecms.bbs.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsMagicConfig;



public class BbsMagicConfig extends BaseBbsMagicConfig {
	private static final long serialVersionUID = 1L;
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("magicSwitch", getMagicSwitch());
		json.put("magicDiscount", getMagicDiscount());
		json.put("magicSofaLines", getMagicSofaLines());
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsMagicConfig () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsMagicConfig (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsMagicConfig (
		java.lang.Integer id,
		boolean magicSwitch) {

		super (
			id,
			magicSwitch);
	}

/*[CONSTRUCTOR MARKER END]*/


}