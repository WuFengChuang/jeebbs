package com.jeecms.plug.live.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.common.util.DateUtils;
import com.jeecms.plug.live.entity.base.BaseBbsLiveApply;



public class BbsLiveApply extends BaseBbsLiveApply {
	private static final long serialVersionUID = 1L;
	
	public static final Short STATUS_CHECKING = 0;
	public static final Short STATUS_CHECKED = 1;
	public static final Short STATUS_REBACK = 2;
	public static final Short STATUS_STOP = 3;
	
	public void addToPictures(String path, String desc) {
		List<BbsLiveApplyPicture> list = getPictures();
		if (list == null) {
			list = new ArrayList<BbsLiveApplyPicture>();
			setPictures(list);
		}
		BbsLiveApplyPicture cp = new BbsLiveApplyPicture();
		cp.setImgPath(path);
		cp.setDescription(desc);
		list.add(cp);
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsLiveApply () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsLiveApply (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsLiveApply (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser checkUser,
		com.jeecms.bbs.entity.BbsUser applyUser,
		java.lang.String brief,
		java.util.Date applyTime,
		java.lang.Short status) {

		super (
			id,
			checkUser,
			applyUser,
			brief,
			applyTime,
			status);
	}

	public JSONObject convertToJson() throws JSONException {
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getApplyUser()!=null&&StringUtils.isNotBlank(getApplyUser().getUsername())) {
			json.put("username", getApplyUser().getUsername());
		}else{
			json.put("username", "");
		}
		if (getApplyTime()!=null) {
			json.put("applyTime", DateUtils.parseDateToDateStr(getApplyTime()));
		}else{
			json.put("applyTime", "");
		}
		if (StringUtils.isNotBlank(getIntro())) {
			json.put("intro", getIntro());
		}else{
			json.put("intro", "");
		}
		if (StringUtils.isNotBlank(getBrief())) {
			json.put("brief", getBrief());
		}else{
			json.put("brief", "");
		}
		if (StringUtils.isNotBlank(getExperience())) {
			json.put("experience", getExperience());
		}else{
			json.put("experience", "");
		}
		if (StringUtils.isNotBlank(getMobile())) {
			json.put("mobile", getMobile());
		}else{
			json.put("mobile", "");
		}
		if (StringUtils.isNotBlank(getAddress())) {
			json.put("address", getAddress());
		}else{
			json.put("address", "");
		}
		JSONArray jsonArray = new JSONArray();
		if (getPictures()!=null&&getPictures().size()>0) {
			List<BbsLiveApplyPicture> list = getPictures();
			for (int i = 0; i < list.size(); i++) {
				JSONObject json2 = new JSONObject();

				if (StringUtils.isNotBlank(list.get(i).getImgPath())) {
					json2.put("picPaths", list.get(i).getImgPath());
				}else{
					json2.put("picPaths", "");
				}
				if (StringUtils.isNotBlank(list.get(i).getDescription())) {
					json2.put("picDescs", list.get(i).getDescription());
				}else{
					json2.put("picDescs", "");
				}
				jsonArray.put(i,json2);
			}
		}
		json.put("images", jsonArray);
		return json;
	}

/*[CONSTRUCTOR MARKER END]*/


}