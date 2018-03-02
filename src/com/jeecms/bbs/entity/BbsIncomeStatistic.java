package com.jeecms.bbs.entity;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsIncomeStatistic;
import com.jeecms.common.util.DateUtils;



public class BbsIncomeStatistic extends BaseBbsIncomeStatistic {
	private static final long serialVersionUID = 1L;
	
	//0总收益 1广告收益、2道具收益、3礼物收益、4live门票收益、5帖子收益
	public static final Short TYPE_AD = 1;
	public static final Short TYPE_MAGIC = 2;
	public static final Short TYPE_GIFT = 3;
	public static final Short TYPE_TICKET = 4;
	public static final Short TYPE_POST = 5;
	public static final Short TYPE_ALL = 0;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getTotalIncomeAmount()!=null) {
			json.put("totalIncomeAmount", getTotalIncomeAmount());
		}else{
			json.put("totalIncomeAmount","");
		}
		if (getAdIncomeAmount()!=null) {
			json.put("adIncomeAmount", getAdIncomeAmount());
		}else{
			json.put("adIncomeAmount","");
		}
		if (getMagicIncomeAmount()!=null) {
			json.put("magicIncomeAmount", getMagicIncomeAmount());
		}else{
			json.put("magicIncomeAmount","");
		}
		if (getGiftIncomeAmount()!=null) {
			json.put("giftIncomeAmount", getGiftIncomeAmount());
		}else{
			json.put("giftIncomeAmount","");
		}
		if (getLiveIncomeAmount()!=null) {
			json.put("liveIncomeAmount", getLiveIncomeAmount());
		}else{
			json.put("liveIncomeAmount","");
		}
		if (getPostIncomeAmount()!=null) {
			json.put("postIncomeAmount", getPostIncomeAmount());
		}else{
			json.put("postIncomeAmount","");
		}
		if (getIncomeDate()!=null) {
			json.put("incomeDate", DateUtils.parseDateToTimeStr(getIncomeDate()));
		}else{
			json.put("incomeDate","");
		}
		return json;
	}
	
	public enum BbsIncomeType {
		/**
		 * 总收益
		 */
		all,
		/**
		 * 广告收益
		 */
		ad,
		/**
		 * 道具收益
		 */
		magic,
		/**
		 *礼物收益
		 */
		gift,
		/**
		 *live门票收益
		 */
		ticket,
		/**
		 *帖子收益
		 */
		post
	};
	
	public void init(){
		if(getAdIncomeAmount()==null){
			setAdIncomeAmount(0d);
		}
		if(getGiftIncomeAmount()==null){
			setGiftIncomeAmount(0d);
		}
		if(getLiveIncomeAmount()==null){
			setLiveIncomeAmount(0d);
		}
		if(getMagicIncomeAmount()==null){
			setMagicIncomeAmount(0d);
		}
		if(getPostIncomeAmount()==null){
			setPostIncomeAmount(0d);
		}
		if(getTotalIncomeAmount()==null){
			setTotalIncomeAmount(0d);
		}
		if(getIncomeDate()==null){
			setIncomeDate(Calendar.getInstance().getTime());
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsIncomeStatistic () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsIncomeStatistic (java.lang.Integer id) {
		super(id);
	}
	

/*[CONSTRUCTOR MARKER END]*/


}