package com.jeecms.bbs.entity;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsMessage;
import com.jeecms.common.util.DateUtils;



public class BbsMessage extends BaseBbsMessage implements Cloneable{
	private static final long serialVersionUID = 1L;
	//消息
	public static int MESSAGE_TYPE_MESSAGE=1;
	//留言
	public static int MESSAGE_TYPE_GUESTBOOK=2;
	//打招呼
	public static int MESSAGE_TYPE_GREET=3;
	
	public static Short MESSAGE_SEND_TYPE_ALL=2;
	
	public static Short MESSAGE_SEND_TYPE_GROUP=1;
	
	public static Short MESSAGE_SEND_TYPE_USER=0;
	
	public JSONObject convertToJson() throws JSONException {
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getContent())) {
			json.put("content", getContent());
		}else{
			json.put("content", "");
		}
		if (getSys()!=null) {
			json.put("sys", getSys());
		}else{
			json.put("sys", "");
		}
		if (getMsgType()!=null) {
			json.put("msgType", getMsgType());
		}else{
			json.put("msgType", "");
		}
		if (getStatus()!=null) {
			json.put("status", getStatus());
		}else{
			json.put("status", "");
		}
		
		if(getUser()!=null){
			if (StringUtils.isNotBlank(getUser().getUsername())) {
				json.put("user", getUser().getUsername());
			}else{
				json.put("user", "");
			}
		}else{
			json.put("user", "");
		}
		if(getSender()!=null){
			if (StringUtils.isNotBlank(getSender().getUsername())) {
				json.put("sender", getSender().getUsername());
			}else{
				json.put("sender", "");
			}
		}else{
			json.put("sender", "");
		}
		if(getReceiver()!=null){
			if (StringUtils.isNotBlank(getReceiver().getUsername())) {
				json.put("receiver", getReceiver().getUsername());
			}else{
				json.put("receiver", "");
			}
		}else{
			json.put("receiver", "");
		}
		if (getCreateTime()!=null) {
			json.put("createTime", DateUtils.parseDateToTimeStr(getCreateTime()));
		}else{
			json.put("createTime", "");
		}
		return json;
	}
	
	public BbsMessage clone(){
		BbsMessage clone;
		try {
			clone = (BbsMessage) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Clone not support?");
		}
		return clone;
	}
	
	public BbsMessage putDataAndClone(BbsUser sender, BbsUser receiver){
		Date now = new Date();
		setUser(sender);
		setSender(sender);
		setReceiver(receiver);
		setCreateTime(now);
		init();
		BbsMessage clone = clone();
		clone.setUser(receiver);
		return clone;
	}
	
	public BbsMessageReply createReply(){
		BbsMessageReply bean = new BbsMessageReply();
		bean.setContent(getContent());
		bean.setCreateTime(getCreateTime());
		bean.setMessage(this);
		bean.setSender(getSender());
		bean.setReceiver(getReceiver());
		return bean;
	}
	
	public void init(){
		if(getSys()==null){
			setSys(false);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsMessage () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsMessage (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsMessage (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		com.jeecms.bbs.entity.BbsUser receiver,
		java.util.Date createTime,
		java.lang.Boolean sys) {

		super (
			id,
			user,
			receiver,
			createTime,
			sys);
	}

/*[CONSTRUCTOR MARKER END]*/


}