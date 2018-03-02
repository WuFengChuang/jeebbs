package com.jeecms.bbs.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsTopicPostOperate;



public class BbsTopicPostOperate extends BaseBbsTopicPostOperate {
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getDataType()!=null) {
			json.put("dataType", getDataType());
		}else{
			json.put("dataType", "");
		}
		if (getDataId()!=null) {
			json.put("dataId", getDataId());
		}else{
			json.put("dataId", "");
		}
		return json;
	}

	/**
	 * 操作类型 0点赞 
	 */
	public static final Integer OPT_UP = 0;
	/**
	 * 操作类型  1收藏 
	 */
	public static final Integer OPT_COLLECT = 1;
	/**
	 * 操作类型  2关注
	 */
	public static final Integer OPT_ATTENT = 2;
	
	/**
	 * 操作类型   3取消点赞 
	 */
	public static final Integer OPT_CANCEL_UP = 3;
	/**
	 * 操作类型 4取消收藏 
	 */
	public static final Integer OPT_CANCEL_COLLECT = 4;
	/**
	 * 操作类型  5取消关注 
	 */
	public static final Integer OPT_CANCEL_ATTENT = 5;
	/**
	 * 主题数据
	 */
	public static final Short DATA_TYPE_TOPIC = 0;
	/**
	 * 帖子数据
	 */
	public static final Short DATA_TYPE_POST = 1;
	
/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsTopicPostOperate () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsTopicPostOperate (java.lang.Long id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsTopicPostOperate (
		java.lang.Long id,
		com.jeecms.bbs.entity.BbsUser user,
		java.lang.Integer dataId,
		java.lang.Short dataType,
		java.lang.Integer operate,
		java.util.Date opTime) {

		super (
			id,
			user,
			dataId,
			dataType,
			operate,
			opTime);
	}

/*[CONSTRUCTOR MARKER END]*/


}