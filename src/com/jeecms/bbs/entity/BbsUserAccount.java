package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsUserAccount;
import com.jeecms.common.util.DateUtils;



public class BbsUserAccount extends BaseBbsUserAccount {
	private static final long serialVersionUID = 1L;
	
public static final byte DRAW_WEIXIN=0;
	
	public static final byte DRAW_ALIPY=1;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getTotalAmount()!=null) {
			json.put("totalAmount", getTotalAmount());
		}else{
			json.put("totalAmount", "");
		}
		if (getNoPayAmount()!=null) {
			json.put("noPayAmount", getNoPayAmount());
		}else{
			json.put("noPayAmount", "");
		}
		if (getYearAmount()!=null) {
			json.put("yearAmount", getYearAmount());
		}else{
			json.put("yearAmount", "");
		}
		if (getMonthAmount()!=null) {
			json.put("monthAmount", getMonthAmount());
		}else{
			json.put("monthAmount", "");
		}
		if (getDayAmount()!=null) {
			json.put("dayAmount", getDayAmount());
		}else{
			json.put("dayAmount", "");
		}
		if (getDrawCount()!=null) {
			json.put("drawCount", getDrawCount());
		}else{
			json.put("drawCount", "");
		}
		if (getBuyCount()!=null) {
			json.put("buyCount", getBuyCount());
		}else{
			json.put("buyCount", "");
		}
		if (getDrawAccount()!=null) {
			json.put("drawAccount", getDrawAccount());
		}else{
			json.put("drawAccount", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getUsername())) {
			json.put("user", getUser().getUsername());
		}else{
			json.put("user", "");
		}
		if(getLastDrawTime()!=null){
			json.put("lastDrawTime", DateUtils.parseDateToTimeStr(getLastDrawTime()));
		}else{
			json.put("lastDrawTime", "");
		}
		if(getLastBuyTime()!=null){
			json.put("lastBuyTime", DateUtils.parseDateToTimeStr(getLastBuyTime()));
		}else{
			json.put("lastBuyTime", "");
		}
		if (getGiftTotalAmount()!=null) {
			json.put("giftTotalAmount", getGiftTotalAmount());
		}else{
			json.put("giftTotalAmount", "");
		}
		if (getGiftNoDrawAmount()!=null) {
			json.put("giftNoDrawAmount", getGiftNoDrawAmount());
		}else{
			json.put("giftNoDrawAmount", "");
		}
		if (getGiftMonthAmount()!=null) {
			json.put("giftMonthAmount", getGiftMonthAmount());
		}else{
			json.put("giftMonthAmount", "");
		}
		if (getGiftDayAmount()!=null) {
			json.put("giftDayAmount", getGiftDayAmount());
		}else{
			json.put("giftDayAmount", "");
		}
		if (getGiftReceiverCount()!=null) {
			json.put("giftReceiverCount", getGiftReceiverCount());
		}else{
			json.put("giftReceiverCount", "");
		}
		if (getGiftDrawCount()!=null) {
			json.put("giftDrawCount", getGiftDrawCount());
		}else{
			json.put("giftDrawCount", "");
		}
		if(getGiftLastReceiverTime()!=null){
			json.put("giftLastReceiverTime", DateUtils.parseDateToTimeStr(getGiftLastReceiverTime()));
		}else{
			json.put("giftLastReceiverTime", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsUserAccount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsUserAccount (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsUserAccount (
		java.lang.Integer id,
		java.lang.Short drawAccount,
		java.lang.Double yearAmount,
		java.lang.Double monthAmount,
		java.lang.Double dayAmount) {

		super (
			id,
			drawAccount,
			yearAmount,
			monthAmount,
			dayAmount);
	}

/*[CONSTRUCTOR MARKER END]*/


}