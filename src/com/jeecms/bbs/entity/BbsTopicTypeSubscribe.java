package com.jeecms.bbs.entity;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsTopicTypeSubscribe;

public class BbsTopicTypeSubscribe extends BaseBbsTopicTypeSubscribe {
	private static final long serialVersionUID = 1L;
	
	public static final Integer SUBSCRIBE_OK = 1;
	
	public static final Integer SUBSCRIBE_CANCEL = 0;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getType()!=null&&StringUtils.isNotBlank(getType().getTypeLog())) {
			json.put("typeLog", getType().getTypeLog());
		}else{
			json.put("typeLog", "");
		}
		if (getType()!=null&&StringUtils.isNotBlank(getType().getUrl())) {
			json.put("url", getType().getUrl());
		}else{
			json.put("url", "");
		}
		if (getType()!=null&&StringUtils.isNotBlank(getType().getName())) {
			json.put("name", getType().getName());
		}else{
			json.put("name", "");
		}
		return json;
	}
	
	public static Integer[] fetchIds(Collection<BbsTopicTypeSubscribe> subs) {
		if (subs == null) {
			return null;
		}
		Integer[] ids = new Integer[subs.size()];
		int i = 0;
		for (BbsTopicTypeSubscribe u : subs) {
			ids[i++] = u.getId();
		}
		return ids;
	}
	
	public static String[] fetchTypeIds(Collection<BbsTopicTypeSubscribe> subs) {
		if (subs == null) {
			return null;
		}
		String[] ids = new String[subs.size()];
		int i = 0;
		for (BbsTopicTypeSubscribe u : subs) {
			ids[i++] = u.getType().getId().toString();
		}
		return ids;
	}
	

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsTopicTypeSubscribe () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsTopicTypeSubscribe (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsTopicTypeSubscribe (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		com.jeecms.bbs.entity.BbsTopicType type) {

		super (
			id,
			user,
			type);
	}

/*[CONSTRUCTOR MARKER END]*/


}