package com.jeecms.bbs.entity;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsAccountDraw;
import com.jeecms.common.util.DateUtils;



public class BbsAccountDraw extends BaseBbsAccountDraw {
	private static final long serialVersionUID = 1L;
	

	public static final Short CHECKING = 0;
	public static final Short CHECKED_SUCC = 1;
	public static final Short CHECKED_FAIL = 2;
	public static final Short DRAW_SUCC = 3;
	
	public static final Short APPLY_TYPE_TOPIC = 1;
	public static final Short APPLY_TYPE_GIFT = 2;
	public static final Short APPLY_TYPE_LIVE = 3;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getApplyAmount()!=null) {
			json.put("applyAmount", getApplyAmount());
		}else{
			json.put("applyAmount", "");
		}
		if (getApplyStatus()!=null) {
			json.put("applyStatus", getApplyStatus());
		}else{
			json.put("applyStatus", "");
		}
		
		if(getApplyTime()!=null){
			json.put("applyTime", DateUtils.parseDateToTimeStr(getApplyTime()));
		}else{
			json.put("applyTime", "");
		}
		if (getApplyType()!=null) {
			json.put("applyType", getApplyType());
		}else{
			json.put("applyType", "");
		}
		if (getDrawUser()!=null&&StringUtils.isNotBlank(getDrawUser().getUsername())) {
			json.put("drawUser", getDrawUser().getUsername());
		}else{
			json.put("drawUser", "");
		}
		return json;
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsAccountDraw () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsAccountDraw (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsAccountDraw (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser drawUser,
		java.lang.String applyAccount,
		java.lang.Double applyAmount,
		java.lang.Short applyStatus,
		java.util.Date applyTime) {

		super (
			id,
			drawUser,
			applyAccount,
			applyAmount,
			applyStatus,
			applyTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}