package com.jeecms.core.entity;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.core.entity.base.BaseCmsRole;

public class CmsRole extends BaseCmsRole {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if(getPriority()!=null){
			json.put("priority", getPriority());
		}else{
			json.put("priority", "");
		}
		if (getLevel()!=null) {
			json.put("level", getLevel());
		}else{
			json.put("level", "");
		}
		if (getAll()!=null) {
			json.put("all", getAll());
		}else{
			json.put("all", "");
		}
		
		if (getPerms()!=null) {
			Set<String> set = getPerms();
			String perms = "";
			for (String string : set) {
				perms+=string+",";
			}
			if (perms.length()>0) {
				perms = perms.substring(0, perms.length()-1);
			}
			json.put("perms", perms);
		}else{
			json.put("perms", "");
		}
		return json;
	}
	
	/**
	 * 数据默认初始化
	 */
	public void init(){
		if (getAll()==null) {
			setAll(false);
		}
	}

	public static Integer[] fetchIds(Collection<CmsRole> roles) {
		if (roles == null) {
			return null;
		}
		Integer[] ids = new Integer[roles.size()];
		int i = 0;
		for (CmsRole r : roles) {
			ids[i++] = r.getId();
		}
		return ids;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsRole () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsRole (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsRole (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.Integer priority,
		java.lang.Boolean m_super) {

		super (
			id,
			name,
			priority,
			m_super);
	}
	public void delFromUsers(BbsUser user) {
		if (user == null) {
			return;
		}
		Set<BbsUser> set = getUsers();
		if (set == null) {
			return;
		}
		set.remove(user);
	}

	/* [CONSTRUCTOR MARKER END] */

}