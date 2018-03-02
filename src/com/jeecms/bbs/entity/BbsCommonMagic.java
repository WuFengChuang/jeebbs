package com.jeecms.bbs.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsCommonMagic;



public class BbsCommonMagic extends BaseBbsCommonMagic {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
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
		if (StringUtils.isNotBlank(getIdentifier())) {
			json.put("identifier", getIdentifier());
		}else{
			json.put("identifier", "");
		}
		if (getDescription()!=null) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if (getDisplayorder()!=null) {
			json.put("displayorder", getDisplayorder());
		}else{
			json.put("displayorder", "");
		}
		if (getCredit()!=null) {
			json.put("credit", getCredit());
		}else{
			json.put("credit", "");
		}
		if (getPrice()!=null) {
			json.put("price", getPrice());
		}else{
			json.put("price", "");
		}
		if (getNum()!=null) {
			json.put("num", getNum());
		}else{
			json.put("num", "");
		}
		if (StringUtils.isNotBlank(getMagicLogo())) {
			json.put("magicLogo", getMagicLogo());
		}else{
			json.put("magicLogo", "");
		}
		if (getAvailable()!=null) {
			json.put("available", getAvailable());
		}else{
			json.put("available", false);
		}
		String groupIds = "";
		if (getGroupIds()!=null&&getGroupIds().length>0) {
			for(int i = 0 ; i < getGroupIds().length ; i++){
				groupIds +=getGroupIds()[i]+",";
			}
			groupIds = groupIds.substring(0,groupIds.length()-1);
		}
		json.put("groupIds", groupIds);
		String beUsedGroupIds = "";
		if (getToUseGroupIds()!=null&&getToUseGroupIds().length>0) {
			for(int i = 0 ; i < getToUseGroupIds().length ; i++){
				beUsedGroupIds += getToUseGroupIds()[i]+",";
			}
			beUsedGroupIds = beUsedGroupIds.substring(0,beUsedGroupIds.length()-1);
			json.put("canBeUsed", true);
		}else{
			json.put("canBeUsed", false);
		}
		json.put("beUsedGroupIds", beUsedGroupIds);
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsCommonMagic () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsCommonMagic (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsCommonMagic (
		java.lang.Integer id,
		java.lang.Boolean available,
		java.lang.String name,
		java.lang.String identifier,
		java.lang.String description,
		java.lang.Byte displayorder,
		java.lang.Byte credit,
		java.lang.Double price,
		java.lang.Integer num,
		java.lang.Integer salevolume,
		java.lang.Integer supplytype,
		java.lang.Integer supplynum,
		java.lang.Integer useperoid,
		java.lang.Integer usenum,
		java.lang.Integer weight,
		java.lang.Boolean useevent) {

		super (
			id,
			available,
			name,
			identifier,
			description,
			displayorder,
			credit,
			price,
			num,
			salevolume,
			supplytype,
			supplynum,
			useperoid,
			usenum,
			weight,
			useevent);
	}
	public void addToGroups(BbsUserGroup group) {
		Set<BbsUserGroup> groups = getUseGroups();
		if (groups == null) {
			groups = new HashSet<BbsUserGroup>();
			setUseGroups(groups);
		}
		groups.add(group);
	}
	public void addToToUseGroups(BbsUserGroup group) {
		Set<BbsUserGroup> groups = getToUseGroups();
		if (groups == null) {
			groups = new HashSet<BbsUserGroup>();
			setToUseGroups(groups);
		}
		groups.add(group);
	}
	public Integer[] getGroupIds() {
		Set<BbsUserGroup> groups = getUseGroups();
		return fetchIds(groups);
	}
	public Integer[] getToUseGroupIds() {
		Set<BbsUserGroup> groups = getToUseGroups();
		return fetchIds(groups);
	}
	public static Integer[] fetchIds(Collection<BbsUserGroup> groups) {
		if (groups == null) {
			return null;
		}
		Integer[] ids = new Integer[groups.size()];
		int i = 0;
		for (BbsUserGroup g : groups) {
			ids[i++] =g.getId();
		}
		return ids;
	}

/*[CONSTRUCTOR MARKER END]*/


}