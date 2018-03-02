package com.jeecms.plug.live.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.common.util.DateUtils;
import com.jeecms.plug.live.entity.base.BaseBbsLiveUserAccount;



public class BbsLiveUserAccount extends BaseBbsLiveUserAccount {
	private static final long serialVersionUID = 1L;
	
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
		if(getNoPayAmount()==null){
			setNoPayAmount(0d);
		}
		if(getTotalAmount()==null){
			setTotalAmount(0d);
		}
		if(getDrawCount()==null){
			setDrawCount(0);
		}
		if(getBuyCount()==null){
			setBuyCount(0);
		}
		if(getTicketNum()==null){
			setTicketNum(0);
		}
		if(getGiftNum()==null){
			setGiftNum(0);
		}
		if(getTopPriority()==null){
			setTopPriority(0);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsLiveUserAccount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsLiveUserAccount (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsLiveUserAccount (
		java.lang.Integer id,
		java.lang.Double yearAmount,
		java.lang.Double monthAmount,
		java.lang.Double dayAmount) {

		super (
			id,
			yearAmount,
			monthAmount,
			dayAmount);
	}

	public JSONObject convertToJson() throws JSONException {
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getRealname())) {
			json.put("realName", getUser().getRealname());
		}else{
			json.put("realName", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getUsername())) {
			json.put("userName", getUser().getUsername());
		}else{
			json.put("userName", "");
		}
		if (getCheckTime()!=null) {
			json.put("checkTime", DateUtils.parseDateToDateStr(getCheckTime()));
		}else{
			json.put("checkTime", "");
		}
		if (getTicketNum()!=null) {
			json.put("ticketNum", getTicketNum());
		}else{
			json.put("ticketNum", "");
		}
		if (getTotalAmount()!=null) {
			json.put("totalAmount", getTotalAmount());
		}else{
			json.put("totalAmount", "");
		}
		if (getGiftNum()!=null) {
			json.put("giftNum", getGiftNum());
		}else{
			json.put("giftNum", "");
		}
		if (getTopPriority()!=null) {
			json.put("topPriority", getTopPriority());
		}else{
			json.put("topPriority", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER END]*/


}