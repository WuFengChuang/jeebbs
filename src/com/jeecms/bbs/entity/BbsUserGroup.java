package com.jeecms.bbs.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsUserGroup;
import com.jeecms.core.entity.CmsSite;

public class BbsUserGroup extends BaseBbsUserGroup {
	private static final long serialVersionUID = 1L;
	/**
	 * 普通组
	 */
	public static final short NORMAL = 1;
	/**
	 * 系统组
	 */
	public static final short SYSTEM = 2;
	/**
	 * 特殊组
	 */
	public static final short SPECIAL = 3;
	/**
	 * 默认组
	 */
	public static final short DEFAULT = 0;
	
	public JSONObject convertToJson(CmsSite site,Integer https) throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		json.put("type", getType());
		if(StringUtils.isNotBlank(getImgPath())){
			json.put("imgPath", getImgPath());
		}else{
			json.put("imgPath", "");
		}
		if (getPoint()!=null) {
			json.put("point", getPoint()+"");
		}else{
			json.put("point", "0");
		}
		if(getDefault()!=null){
			json.put("default", getDefault());
		}else{
			json.put("default", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if (getGradeNum()!=null) {
			json.put("gradeNum", getGradeNum());
		}else{
			json.put("gradeNum", 0);
		}
		if (getPerms()!=null) {
			if (StringUtils.isNotBlank(getPerms().get("allow_topic"))) {
				json.put("perm_allow_topic", Boolean.parseBoolean(getPerms().get("allow_topic")));
			}else{
				json.put("perm_allow_topic", "");
			}
			if (StringUtils.isNotBlank(getPerms().get("allow_reply"))) {
				json.put("perm_allow_reply", Boolean.parseBoolean(getPerms().get("allow_reply")));
			}else{
				json.put("perm_allow_reply","");
			}
			if (StringUtils.isNotBlank(getPerms().get("allow_attach"))) {
				json.put("perm_allow_attach", Boolean.parseBoolean(getPerms().get("allow_attach")));
			}else{
				json.put("perm_allow_attach","");
			}
			if (StringUtils.isNotBlank(getPerms().get("attach_type"))) {
				json.put("perm_attach_type", getPerms().get("attach_type"));
			}else{
				json.put("perm_attach_type", "");
			}
			if (StringUtils.isNotBlank(getPerms().get("attach_max_size"))) {
				json.put("perm_attach_max_size", Integer.parseInt(getPerms().get("attach_max_size")));
			}else{
				json.put("perm_attach_max_size", "");
			}
			if (StringUtils.isNotBlank(getPerms().get("attach_per_day"))) {
				json.put("perm_attach_per_day",Integer.parseInt( getPerms().get("attach_per_day")));
			}else{
				json.put("perm_attach_per_day", "");
			}
			
			if (getType()==2) {
				if (StringUtils.isNotBlank(getPerms().get("super_moderator"))) {
					json.put("perm_super_moderator", Boolean.parseBoolean(getPerms().get("super_moderator")));
				}else{
					json.put("perm_super_moderator", "");
				}
				if (StringUtils.isNotBlank(getPerms().get("post_limit"))) {
					json.put("perm_post_limit", Boolean.parseBoolean(getPerms().get("post_limit")));
				}else{
					json.put("perm_post_limit", "");
				}
				if (StringUtils.isNotBlank(getPerms().get("topic_top"))) {
					json.put("perm_topic_top", getPerms().get("topic_top"));
				}else{
					json.put("perm_topic_top", "");
				}
				if (StringUtils.isNotBlank(getPerms().get("topic_manage"))) {
					json.put("perm_topic_manage", Boolean.parseBoolean(getPerms().get("topic_manage")));
				}else{
					json.put("perm_topic_manage", "");
				}
				if (StringUtils.isNotBlank(getPerms().get("topic_edit"))) {
					json.put("perm_topic_edit", Boolean.parseBoolean(getPerms().get("topic_edit")));
				}else{
					json.put("perm_topic_edit", "");
				}
				if (StringUtils.isNotBlank(getPerms().get("topic_shield"))) {
					json.put("perm_topic_shield", Boolean.parseBoolean(getPerms().get("topic_shield")));
				}else{
					json.put("perm_topic_shield", "");
				}
				if (StringUtils.isNotBlank(getPerms().get("topic_delete"))) {
					json.put("perm_topic_delete", Boolean.parseBoolean(getPerms().get("topic_delete")));
				}else{
					json.put("perm_topic_delete", "");
				}
				if (StringUtils.isNotBlank(getPerms().get("view_ip"))) {
					json.put("perm_view_ip", Boolean.parseBoolean(getPerms().get("view_ip")));
				}else{
					json.put("perm_view_ip", "");
				}
				if (StringUtils.isNotBlank(getPerms().get("member_prohibit"))) {
					json.put("perm_member_prohibit", Boolean.parseBoolean(getPerms().get("member_prohibit")));
				}else{
					json.put("perm_member_prohibit", "");
				}
			}
		}else{
			json.put("perm_allow_topic", "");
			json.put("perm_allow_reply","");
			json.put("perm_allow_attach","");
			json.put("perm_attach_type", "");
			json.put("perm_attach_max_size", "");
			json.put("perm_attach_per_day", "");
			if (getType()==2) {
					json.put("perm_super_moderator", "");
					json.put("perm_post_limit", "");
					json.put("perm_topic_top", "");
					json.put("perm_topic_manage", "");
					json.put("perm_topic_edit", "");
					json.put("perm_topic_shield", "");
					json.put("perm_topic_delete", "");
					json.put("perm_view_ip", "");
					json.put("perm_member_prohibit", "");
				}
		}
		
		return json;
	}
	/**
	 * 数据默认初始化
	 */
	public void init(){
		if (getPerms()==null) {
			setPerms(new HashMap<String,String>());
			if (!getPerms().containsKey("allow_topic")) {
				getPerms().put("allow_topic", "true");
			}
			if (!getPerms().containsKey("allow_reply")) {
				getPerms().put("allow_reply", "true");
			}
			if (!getPerms().containsKey("allow_attach")) {
				getPerms().put("allow_attach", "true");
			}
			if (!getPerms().containsKey("attach_type")) {
				getPerms().put("attach_type", "");
			}
			if (!getPerms().containsKey("attach_max_size")) {
				getPerms().put("attach_max_size", "0");
			}
			if (!getPerms().containsKey("attach_per_day")) {
				getPerms().put("attach_per_day", "0");
			}
			if (getType()==2) {
				if (!getPerms().containsKey("super_moderator")) {
					getPerms().put("super_moderator", "false");
				}
				if (!getPerms().containsKey("post_limit")) {
					getPerms().put("post_limit", "true");
				}
				if (!getPerms().containsKey("topic_top")) {
					getPerms().put("topic_top", "true");
				}
				if (!getPerms().containsKey("topic_manage")) {
					getPerms().put("topic_manage", "true");
				}
				if (!getPerms().containsKey("topic_edit")) {
					getPerms().put("topic_edit", "true");
				}
				if (!getPerms().containsKey("topic_shield")) {
					getPerms().put("topic_shield", "true");
				}
				if (!getPerms().containsKey("topic_delete")) {
					getPerms().put("topic_delete", "true");
				}
				if (!getPerms().containsKey("view_ip")) {
					getPerms().put("view_ip", "true");
				}
				if (!getPerms().containsKey("member_prohibit")) {
					getPerms().put("member_prohibit", "true");
				}
			}
		}
		if (getMagicPacketSize()==null) {
			setMagicPacketSize(0);
		}
		if (getDefault()==null) {
			setDefault(false);
		}
		if (getGradeNum()==null) {
			setGradeNum(0);
		}
	}
	
	/**
	 * 是否有版主权限
	 * 
	 * @param forum
	 *            相应的板块
	 * @return
	 */
	public boolean hasRight(BbsForum forum, BbsUser user) {
		if (forum == null) {
			return false;
		}
		if (superModerator()) {
			return true;
		}
		if (user == null) {
			return false;
		}
		if (("," + forum.getCategory().getModerators() + ",").indexOf(","
				+ user.getUsername() + ",") > -1
				|| ("," + forum.getModerators() + ",").indexOf(","
						+ user.getUsername() + ",") > -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否允许发表主题
	 * 
	 * @return
	 */
	public boolean allowTopic() {
		String s = getPerms().get("allow_topic");
		return StringUtils.equals(s, "true");
	}

	/**
	 * 是否允许发表回复
	 * 
	 * @return
	 */
	public boolean allowReply() {
		String s = getPerms().get("allow_reply");
		return StringUtils.equals(s, "true");
	}

	/**
	 * 检查今日发贴数
	 * 
	 * @param count
	 *            今日已发贴数
	 * @return true:允许发帖；false:已达最大发贴数
	 */
	public boolean checkPostToday(int count) {
		int max = getPostPerDay();
		if (max == 0) {
			return true;
		} else {
			return max > count;
		}
	}

	/**
	 * 每日最大发贴数
	 * 
	 * @return
	 */
	public int getPostPerDay() {
		return NumberUtils.toInt(getPerms().get("post_per_day"));
	}

	/**
	 * 发帖时间间隔
	 * 
	 * @return
	 */
	public int getPostInterval() {
		return NumberUtils.toInt(getPerms().get("post_interval"));
	}

	/**
	 * 超级版主。无需指定成为板块版主即有管理权限
	 * 
	 * @return
	 */
	public boolean superModerator() {
		String s = getPerms().get("super_moderator");
		return StringUtils.equals(s, "true");
	}

	/**
	 * 发贴不受限制
	 * 
	 * @return
	 */
	public boolean postLimit() {
		String s = getPerms().get("post_limit");
		return StringUtils.equals(s, "true");
	}

	/**
	 * 获得置顶权限级别
	 * 
	 * @return 0：无权限；1：本版置顶；2：分区置顶；3：全局置顶
	 */
	public short topicTop() {
		short top = (short) NumberUtils.toInt(getPerms().get("topic_top"));
		if (top < 1 || top > 3) {
			return 0;
		} else {
			return top;
		}
	}
	
	public Integer getTopicTopLevel() {
		int top =  NumberUtils.toInt(getPerms().get("topic_top"));
		if (top < 1 || top > 3) {
			return 0;
		} else {
			return top;
		}
	}

	/**
	 * 主题管理权限。精、锁、提、亮、压
	 * 
	 * @return
	 */
	public boolean topicManage() {
		String s = getPerms().get("post_limit");
		return StringUtils.equals(s, "true");
	}

	/**
	 * 编辑帖子
	 * 
	 * @return
	 */
	public boolean topicEdit() {
		String s = getPerms().get("topic_edit");
		return StringUtils.equals(s, "true");
	}

	/**
	 * 删除帖子
	 * 
	 * @return
	 */
	public boolean topicDelete() {
		String s = getPerms().get("topic_delete");
		return StringUtils.equals(s, "true");
	}

	/**
	 * 屏蔽帖子,移动帖子
	 * 
	 * @return
	 */
	public boolean topicShield() {
		String s = getPerms().get("topic_shield");
		return StringUtils.equals(s, "true");
	}

	/**
	 * 查看IP
	 * 
	 * @return
	 */
	public boolean viewIp() {
		String s = getPerms().get("view_ip");
		return StringUtils.equals(s, "true");
	}

	/**
	 * 屏蔽帖子
	 * 
	 * @return
	 */
	public boolean memberProhibit() {
		String s = getPerms().get("member_prohibit");
		return StringUtils.equals(s, "true");
	}
	
	public String getUploadType() {
		String s = getPerms().get("attach_type");
		return s;
	}
	
	public boolean isAllowSuffix(String ext) {
		String s = getUploadType();
		if(StringUtils.isNotBlank(s)){
			if(s.contains(ext)){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	
	public boolean isAllowMaxFile(Integer size) {
		String s = getPerms().get("attach_max_size");
		if(StringUtils.isNotBlank(s)){
			Integer maxFileSize=Integer.parseInt(s);
			if(maxFileSize.equals(0)||maxFileSize>=size){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	
	public Integer getAllowMaxFile() {
		String s = getPerms().get("attach_max_size");
		if(StringUtils.isNotBlank(s)){
			Integer maxFileSize=Integer.parseInt(s);
			return maxFileSize;
		}else{
			return 0;
		}
	}
	
	public boolean isAllowPerDay(Integer size) {
		String s = getPerms().get("attach_per_day");
		if(StringUtils.isNotBlank(s)){
			Integer maxFileSize=Integer.parseInt(s);
			if(maxFileSize.equals(0)||maxFileSize>=size){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsUserGroup() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsUserGroup(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsUserGroup(java.lang.Integer id,
			com.jeecms.core.entity.CmsSite site, java.lang.String name,
			java.lang.Short type, java.lang.Long point,
			java.lang.Boolean m_default, java.lang.Integer gradeNum) {

		super(id, site, name, type, point, m_default, gradeNum);
	}

	public void addToPostTypes(BbsPostType postType) {
		Set<BbsPostType> postTypes = getPostTypes();
		if (postTypes == null) {
			postTypes = new HashSet<BbsPostType>();
			setPostTypes(postTypes);
		}
		postTypes.add(postType);
	}

	public static Integer[] fetchIds(Collection<BbsPostType> types) {
		if (types == null) {
			return null;
		}
		Integer[] ids = new Integer[types.size()];
		int i = 0;
		for (BbsPostType t : types) {
			ids[i++] = t.getId();
		}
		return ids;
	}

	/* [CONSTRUCTOR MARKER END] */

}