package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsConfigCharge;



public class BbsConfigCharge extends BaseBbsConfigCharge {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(Boolean isConfig) throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (isConfig) {
			if (StringUtils.isNotBlank(getWeixinAppId())) {
				json.put("weixinAppId", getWeixinAppId());
			}else{
				json.put("weixinAppId", "");
			}
			if (StringUtils.isNotBlank(getWeixinSecret())) {
				json.put("weixinSecret", getWeixinSecret());
			}else{
				json.put("weixinSecret", "");
			}
			if (StringUtils.isNotBlank(getWeixinAccount())) {
				json.put("weixinAccount", getWeixinAccount());
			}else{
				json.put("weixinAccount", "");
			}
			if (StringUtils.isNotBlank(getWeixinPassword())) {
				json.put("weixinPassword", getWeixinPassword());
			}else{
				json.put("weixinPassword", "");
			}
			if (StringUtils.isNotBlank(getTransferApiPassword())) {
				json.put("transferApiPassword", getTransferApiPassword());
			}else{
				json.put("transferApiPassword", "");
			}
			if (StringUtils.isNotBlank(getPayTransferPassword())) {
				json.put("payTransferPassword", getPayTransferPassword());
			}else{
				json.put("payTransferPassword", "");
			}
			if (getRewardPattern()!=null) {
				json.put("rewardPattern", getRewardPattern());
			}else{
				json.put("rewardPattern", "");
			}
			if (getRewardMin()!=null) {
				json.put("rewardMin", getRewardMin());
			}else{
				json.put("rewardMin", "");
			}
			if (getRewardMax()!=null) {
				json.put("rewardMax", getRewardMax());
			}else{
				json.put("rewardMax", "");
			}
			if (StringUtils.isNotBlank(getAlipayPartnerId())) {
				json.put("alipayPartnerId", getAlipayPartnerId());
			}else{
				json.put("alipayPartnerId", "");
			}
			if (StringUtils.isNotBlank(getAlipayAccount())) {
				json.put("alipayAccount", getAlipayAccount());
			}else{
				json.put("alipayAccount", "");
			}
			if (StringUtils.isNotBlank(getAlipayKey())) {
				json.put("alipayKey", getAlipayKey());
			}else{
				json.put("alipayKey", "");
			}
			if (StringUtils.isNotBlank(getAlipayAppId())) {
				json.put("alipayAppId", getAlipayAppId());
			}else{
				json.put("alipayAppId", "");
			}
			if (StringUtils.isNotBlank(getAlipayPublicKey())) {
				json.put("alipayPublicKey", getAlipayPublicKey());
			}else{
				json.put("alipayPublicKey", "");
			}
			if (StringUtils.isNotBlank(getAlipayPrivateKey())) {
				json.put("alipayPrivateKey", getAlipayPrivateKey());
			}else{
				json.put("alipayPrivateKey", "");
			}
			if (getChargeRatio()!=null) {
				json.put("chargeRatio", getChargeRatio());
			}else{
				json.put("chargeRatio", "");
			}
			if (getMinDrawAmount()!=null) {
				json.put("minDrawAmount", getMinDrawAmount());
			}else{
				json.put("minDrawAmount", "");
			}
			if (getGiftChargeRatio()!=null) {
				json.put("giftChargeRatio", getGiftChargeRatio());
			}else{
				json.put("giftChargeRatio", "");
			}
		}else{
			if (getCommissionTotal()!=null) {
				json.put("commissionTotal", getCommissionTotal());
			}else{
				json.put("commissionTotal", "");
			}
			if (getCommissionYear()!=null) {
				json.put("commissionYear", getCommissionYear());
			}else{
				json.put("commissionYear", "");
			}
			if (getCommissionMonth()!=null) {
				json.put("commissionMonth", getCommissionMonth());
			}else{
				json.put("commissionMonth", "");
			}
			if (getCommissionDay()!=null) {
				json.put("commissionDay", getCommissionDay());
			}else{
				json.put("commissionDay", "");
			}
			
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsConfigCharge () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsConfigCharge (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsConfigCharge (
		java.lang.Integer id,
		java.lang.String weixinAppid,
		java.lang.String weixinSecret,
		java.lang.String weixinAccount,
		java.lang.String weixinPassword,
		java.lang.Double chargeRatio,
		java.lang.Double minDrawAmount,
		java.lang.Double commissionTotal,
		java.lang.Double commissionYear,
		java.lang.Double commissionMonth,
		java.lang.Double commissionDay,
		java.lang.String payTransferPassword,
		java.lang.String transferApiPassword,
		java.lang.Double rewardMin,
		java.lang.Double rewardMax,
		java.lang.Boolean rewardPattern) {

		super (
			id,
			weixinAppid,
			weixinSecret,
			weixinAccount,
			weixinPassword,
			chargeRatio,
			minDrawAmount,
			commissionTotal,
			commissionYear,
			commissionMonth,
			commissionDay,
			payTransferPassword,
			transferApiPassword,
			rewardMin,
			rewardMax,
			rewardPattern);
	}

/*[CONSTRUCTOR MARKER END]*/


}