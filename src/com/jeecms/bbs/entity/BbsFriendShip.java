package com.jeecms.bbs.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsFriendShip;



public class BbsFriendShip extends BaseBbsFriendShip {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 申请中
	 */
	public static final int APPLYING = 0;
	/**
	 * 接受
	 */
	public static final int ACCEPT = 1;
	/**
	 * 拒绝
	 */
	public static final int REFUSE = 2;
	
	public JSONObject convertToJson() 
			throws JSONException{
		JSONObject json=new JSONObject();
		json.put("id", getId());
		//0:申请中;1:接受;2:拒绝
		json.put("status", getStatus());
		json.put("user", getUser().getUsername());
		json.put("friend", getFriend().getUsername());
		return json;
	}
	
	public void init(){
		if(getStatus()==null){
			setStatus(APPLYING);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsFriendShip () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsFriendShip (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsFriendShip (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		com.jeecms.bbs.entity.BbsUser friend,
		java.lang.Integer status) {

		super (
			id,
			user,
			friend,
			status);
	}

/*[CONSTRUCTOR MARKER END]*/


}