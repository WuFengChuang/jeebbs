package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsPlug;
import com.jeecms.common.util.DateUtils;



public class BbsPlug extends BaseBbsPlug {
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
		if (StringUtils.isNotBlank(getAuthor())) {
			json.put("author", getAuthor());
		}else{
			json.put("author", "");
		}
		if (getUsed()!=null) {
			json.put("used", getUsed());
		}else{
			json.put("used", "");
		}
		if (getUploadTime()!=null) {
			json.put("uploadTime", DateUtils.parseDateToDateStr(getUploadTime()));
		}else{
			json.put("uploadTime", "");
		}
		if (getInstallTime()!=null) {
			json.put("installTime", DateUtils.parseDateToDateStr(getInstallTime()));
		}else{
			json.put("installTime", "");
		}
		if (getUninstallTime()!=null) {
			json.put("uninstallTime", DateUtils.parseDateToDateStr(getUninstallTime()));
		}else{
			json.put("uninstallTime", "");
		}
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if (getFileConflict()!=null) {
			json.put("fileConflict", getFileConflict());
		}else{
			json.put("fileConflict", "");
		}
		if (StringUtils.isNotBlank(getPath())) {
			json.put("path", getPath());
		}else{
			json.put("path", "");
		}
		return json;
	}
	
	public Boolean getUsed(){
		return isUsed();
	}
	
	public Boolean getFileConflict () {
		return isFileConflict();
	}
	
	public boolean getCanInstall(){
		//未使用 且(文件未冲突或者是修复类)
		if(!getUsed()&&(!getFileConflict()||getPlugRepair())){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean getCanUnInstall(){
		//使用中的修复类插件和未使用的插件 不能卸载
		if((getUsed()&&getPlugRepair())||!getUsed()){
			return false;
		}else{
			return true;
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsPlug () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsPlug (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsPlug (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.String path,
		java.util.Date uploadTime,
		boolean fileConflict,
		boolean used) {

		super (
			id,
			name,
			path,
			uploadTime,
			fileConflict,
			used);
	}

/*[CONSTRUCTOR MARKER END]*/


}