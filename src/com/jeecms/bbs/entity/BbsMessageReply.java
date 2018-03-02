package com.jeecms.bbs.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsMessageReply;
import com.jeecms.common.util.DateUtils;



public class BbsMessageReply extends BaseBbsMessageReply {
	private static final long serialVersionUID = 1L;
	

	public JSONObject convertToJson() throws JSONException {
		JSONObject json=new JSONObject();
		json.put("id", getId());
		json.put("content", getContent());
		json.put("status", getStatus());
		if(getSender()!=null){
			json.put("sender", getSender().getUsername());
		}
		if(getReceiver()!=null){
			json.put("receiver", getReceiver().getUsername());
		}
		json.put("createTime", DateUtils.parseDateToTimeStr(getCreateTime()));
		return json;
	}
	
	
	public BbsMessageReply(BbsMessage message){
		setContent(message.getContent());
		setCreateTime(message.getCreateTime());
		setMessage(message);
		setSender(message.getSender());
		setReceiver(message.getReceiver());
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsMessageReply () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsMessageReply (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsMessageReply (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsMessage message,
		com.jeecms.bbs.entity.BbsUser receiver,
		java.util.Date createTime) {

		super (
			id,
			message,
			receiver,
			createTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}