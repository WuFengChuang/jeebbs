package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsCategory;

public class BbsCategory extends BaseBbsCategory {
	private static final long serialVersionUID = 1L;
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if(getId()!=null){
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if(StringUtils.isNotBlank(getPath())){
			json.put("path", getPath());
		}else{
			json.put("path", "");
		}
		if(StringUtils.isNotBlank(getTitle())){
			json.put("title", getTitle());
		}else{
			json.put("title", "");
		}
		if(getPriority()!=null){
			json.put("priority", getPriority());
		}else{
			json.put("priority", "");
		}
		return json;
	}
	
	public void init(){
		if(getForumCols()==null){
			setForumCols(3);
		}
		if(getPriority()==null){
			setPriority(0);
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsCategory () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsCategory (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsCategory (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		java.lang.String path,
		java.lang.String title,
		java.lang.Integer priority,
		java.lang.Integer forumCols) {

		super (
			id,
			site,
			path,
			title,
			priority,
			forumCols);
	}

	/* [CONSTRUCTOR MARKER END] */

}