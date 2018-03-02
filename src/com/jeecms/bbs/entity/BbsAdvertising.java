package com.jeecms.bbs.entity;


import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsAdvertising;
import com.jeecms.common.util.DateUtils;

public class BbsAdvertising extends BaseBbsAdvertising {
	private static final long serialVersionUID = 1L;
	
	public static final Integer CHARGE_MODE_DAY = 0;
	public static final Integer CHARGE_MODE_CLICK = 1;
	public static final Integer CHARGE_MODE_DISPLAY = 2;

	public int getPercent() {
		if (getDisplayCount() <= 0) {
			return 0;
		} else {
			return (int) (getClickCount() * 100 / getDisplayCount());
		}
	}

	public Long getStartTimeMillis() {
		if (getStartTime() != null) {
			return getStartTime().getTime();
		} else {
			return null;
		}
	}

	public Long getEndTimeMillis() {
		if (getEndTime() != null) {
			return getEndTime().getTime();
		} else {
			return null;
		}
	}

	public void init() {
		if (getClickCount() == null) {
			setClickCount(0L);
		}
		if (getDisplayCount() == null) {
			setDisplayCount(0L);
		}
		if (getEnabled() == null) {
			setEnabled(true);
		}
		if (getWeight() == null) {
			setWeight(1);
		}
		if(getChargeAmount()==null){
			setChargeAmount(0d);
		}
		if(getChargeMode()==null){
			setChargeMode(CHARGE_MODE_DAY);
		}
		if (StringUtils.isBlank(getCategory())) {
			setCategory("image");
		}
		blankToNull();
	}

	public void blankToNull() {
		if (StringUtils.isBlank(getCode())) {
			setCode(null);
		}
	}
	
	public JSONObject convertToJson(Integer https) 
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
		if (getOwner()!=null&&StringUtils.isNotBlank(getOwner().getUsername())) {
			json.put("username", getOwner().getUsername());
		}else{
			json.put("username", "");
		}
		if (StringUtils.isNotBlank(getCategory())) {
			json.put("category", getCategory());
		}else{
			json.put("category", "");
		}
		if (getChargeMode()!=null) {
			json.put("chargeMode", getChargeMode());
		}else{
			json.put("chargeMode", "");
		}
		if (StringUtils.isNotBlank(getCode())) {
			json.put("code", getCode());
		}else{
			json.put("code", "");
		}
		if (getWeight()!=null) {
			json.put("weight", getWeight());
		}else{
			json.put("weight", "");
		}
		if (getDisplayCount()!=null) {
			json.put("displayCount", getDisplayCount());
		}else{
			json.put("displayCount", "");
		}
		if (getClickCount()!=null) {
			json.put("clickCount", getClickCount());
		}else{
			json.put("clickCount", "");
		}
		if(getStartTime()!=null){
			json.put("startTime", DateUtils.parseDateToDateStr(getStartTime()));
		}else{
			json.put("startTime","");
		}
		if(getEndTime()!=null){
			json.put("endTime", DateUtils.parseDateToDateStr(getEndTime()));
		}else{
			json.put("endTime","");
		}
		if (getAdspace()!=null&&StringUtils.isNotBlank(getAdspace().getName())) {
			json.put("adspaceName", getAdspace().getName());
		}else{
			json.put("adspaceName", "");
		}
		if (getAdspace()!=null) {
			json.put("adspaceId", getAdspace().getId());
		}else{
			json.put("adspaceId", "");
		}
		if (getEnabled()!=null) {
			json.put("enabled", getEnabled());
		}else{
			json.put("enabled", "");
		}
		if(getChargeAmount()!=null){
			json.put("chargeAmount", getChargeAmount());
		}else{
			json.put("chargeAmount", 0d);
		}
		json.put("percent", getPercent());
		if(getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("image_link"))){
			json.put("attr_image_link", getAttr().get("image_link"));
		}else{
			json.put("attr_image_link", "");
		}
		if(getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("image_url"))){
			json.put("attr_image_url", getAttr().get("image_url"));
		}else{
			json.put("attr_image_url", "");
		}
		if (getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("image_target"))) {
			json.put("attr_image_target", getAttr().get("image_target"));
		}else{
			json.put("attr_image_target", "");
		}
		if (getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("image_title"))) {
			json.put("attr_image_title", getAttr().get("image_title"));
		}else{
			json.put("attr_image_title", "");
		}
		if (getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("image_width"))) {
			json.put("attr_image_width", Integer.parseInt(getAttr().get("image_width")));
		}else{
			json.put("attr_image_width", "");
		}
		if (getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("image_height"))) {
			json.put("attr_image_height", Integer.parseInt(getAttr().get("image_height")));
		}else{
			json.put("attr_image_height", "");
		}
		if(getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("flash_url"))){
			json.put("attr_flash_url", getAttr().get("flash_url"));
		}else{
			json.put("attr_flash_url", "");
		}
		if (getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("flash_width"))) {
			json.put("attr_flash_width", Integer.parseInt(getAttr().get("flash_width")));
		}else{
			json.put("attr_flash_width", "");
		}
		if (getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("flash_height"))) {
			json.put("attr_flash_height", Integer.parseInt(getAttr().get("flash_height")));
		}else{
			json.put("attr_flash_height", "");
		}
		if (getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("text_link"))) {
			json.put("attr_text_link", getAttr().get("text_link"));
		}else{
			json.put("attr_text_link", "");
		}
		if (getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("text_target"))) {
			json.put("attr_text_target", getAttr().get("text_target"));
		}else{
			json.put("attr_text_target", "");
		}
		if (getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("text_color"))) {
			json.put("attr_text_color", getAttr().get("text_color"));
		}else{
			json.put("attr_text_color", "");
		}
		if (getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("text_font"))) {
			json.put("attr_text_font", Integer.parseInt(getAttr().get("text_font")));
		}else{
			json.put("attr_text_font", "");
		}
		if (getAttr()!=null&&StringUtils.isNotBlank(getAttr().get("text_title"))) {
			json.put("attr_text_title", getAttr().get("text_title"));
		}else{
			json.put("attr_text_title", "");
		}
		return json;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsAdvertising() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsAdvertising(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsAdvertising(java.lang.Integer id,
			com.jeecms.bbs.entity.BbsAdvertisingSpace adspace,
			com.jeecms.core.entity.CmsSite site, java.lang.String name,
			java.lang.String category, java.lang.Integer weight,
			java.lang.Long displayCount, java.lang.Long clickCount,
			java.lang.Boolean enabled) {

		super(id, adspace, site, name, category, weight, displayCount,
				clickCount, enabled);
	}

	/* [CONSTRUCTOR MARKER END] */

}