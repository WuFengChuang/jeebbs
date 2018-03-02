package com.jeecms.bbs.entity;


import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsGift;
import com.jeecms.core.entity.CmsSite;

public class BbsGift extends BaseBbsGift {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(CmsSite site,Integer https,BbsUser user) 
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
		if (getPrice()!=null) {
			json.put("price", getPrice());
		}else{
			json.put("price", "");
		}
		if (getPriority()!=null) {
			json.put("priority", getPriority());
		}else{
			json.put("priority", "");
		}
		if (getGiftType()!=null) {
			json.put("giftType", getGiftType());
		}else{
			json.put("giftType", "");
		}
		json.put("userGiftCount", user.getGiftCount(getId()));
		if(StringUtils.isNotBlank(getPicPath())){
			json.put("picPath", getPicPath());
		}else{
			json.put("picPath", "");
		}
		if (getDisabled()!=null) {
			json.put("disabled", getDisabled());
		}else{
			json.put("disabled", "");
		}
		if (getPriority()!=null) {
			json.put("priority", getPriority());
		}else{
			json.put("priority", "");
		}
		return json;
	}
	
	public void init(){
		if(getDayAmount()==null){
			setDayAmount(0d);
		}
		if(getMonthAmount()==null){
			setMonthAmount(0d);
		}
		if(getYearAmount()==null){
			setYearAmount(0d);
		}
		if(getTotalAmount()==null){
			setTotalAmount(0d);
		}
		if (getDisabled()==null) {
			setDisabled(false);
		}
		if (getGiftType()==null) {
			setGiftType((short) 0);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsGift () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsGift (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsGift (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.String picPath,
		java.lang.Double price,
		java.lang.Integer priority,
		java.lang.Boolean disabled,
		java.lang.Short giftType) {

		super (
			id,
			name,
			picPath,
			price,
			priority,
			disabled,
			giftType);
	}

/*[CONSTRUCTOR MARKER END]*/


}