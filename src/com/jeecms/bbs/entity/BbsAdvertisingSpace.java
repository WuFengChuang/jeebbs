package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsAdvertisingSpace;




public class BbsAdvertisingSpace extends BaseBbsAdvertisingSpace {
	private static final long serialVersionUID = 1L;
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if(getEnabled()!=null){
			json.put("enabled", getEnabled());
		}else{
			json.put("enabled", "");
		}
		if (StringUtils.isNotBlank(getPreviewImg())) {
			json.put("previewImg", getPreviewImg());
		}else{
			json.put("previewImg", "");
		}
		return json;
	}
	
	public void init(){
		if (getEnabled()==null) {
			setEnabled(false);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsAdvertisingSpace () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsAdvertisingSpace (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsAdvertisingSpace (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		java.lang.String name,
		java.lang.Boolean enabled) {

		super (
			id,
			site,
			name,
			enabled);
	}

/*[CONSTRUCTOR MARKER END]*/


}