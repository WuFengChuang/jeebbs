package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseCmsSensitivity;



public class CmsSensitivity extends BaseCmsSensitivity {
	private static final long serialVersionUID = 1L;
	
	public static final Integer TYPE_UPDATE = 1;
	public static final Integer TYPE_REPLACE = 2;
	public static final Integer TYPE_CLEARSAVE = 3;
	public enum CmsSensitivityImportType {
		/**
		 * 更新敏感词，不替换已经存在的敏感词
		 */
		update,
		/**
		 * 替换全部
		 */
		replace,
		/**
		 * 清空当前敏感词并导入新敏感词
		 */
		clearSave
	};

	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getSearch())) {
			json.put("search", getSearch());
		}else{
			json.put("search", getSearch());
		}
		if (StringUtils.isNotBlank(getReplacement())) {
			json.put("replacement", getReplacement());
		}else{
			json.put("replacement", "");
		}
		
		return json;
	}
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public CmsSensitivity () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsSensitivity (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsSensitivity (
		java.lang.Integer id,
		java.lang.String search,
		java.lang.String replacement) {

		super (
			id,
			search,
			replacement);
	}

/*[CONSTRUCTOR MARKER END]*/


}