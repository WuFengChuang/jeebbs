package com.jeecms.bbs.entity;

import static com.jeecms.common.web.Constants.INDEX;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsTopicType;
import com.jeecms.common.hibernate4.HibernateTree;
import com.jeecms.common.hibernate4.PriorityInterface;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;


public class BbsTopicType extends BaseBbsTopicType implements HibernateTree<Integer>,
PriorityInterface {
	private static final long serialVersionUID = 1L;
	
	public static final int ORDER_PRIORITY_DESC = 1;
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getName())) {
			json.put("name", getName());
		}else{
			json.put("name", "");
		}
		if (StringUtils.isNotBlank(getPath())) {
			json.put("path", getPath());
		}else{
			json.put("path", "");
		}
		if (getLft()!=null) {
			json.put("lft", getLft());
		}else{
			json.put("lft", "");
		}
		if (getRgt()!=null) {
			json.put("rgt", getRgt());
		}else{
			json.put("rgt", "");
		}
		if (getPriority()!=null) {
			json.put("priority", getPriority());
		}else{
			json.put("priority", "");
		}
		json.put("display", getDisplay());
		if (StringUtils.isNotBlank(getDescription())) {
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		
		if(StringUtils.isNotBlank(getTypeLog())){
			json.put("typeLog", getTypeLog());
		}else{
			json.put("typeLog", "");
		}
	
		if (getTopicCount()!=null) {
			json.put("topicCount", getTopicCount());
		}else{
			json.put("topicCount", "");
		}
		if (getTopicEssenceCount()!=null) {
			json.put("topicEssenceCount", getTopicEssenceCount());
		}else{
			json.put("topicEssenceCount","");
		}
		if (getSubscribeCount()!=null) {
			json.put("subscribeCount", getSubscribeCount());
		}else{
			json.put("subscribeCount", "");
		}
		if (getChild()!=null&&getChild().size()>0) {
			json.put("haveChild", true);
		}else{
			json.put("haveChild", false);
		}
		if (getParent()!=null&&getParent().getId()!=null) {
			json.put("parentId", getParent().getId());
		}else{
			json.put("parentId", "");
		}
		if (getParent()!=null&&StringUtils.isNotBlank(getParent().getName())) {
			json.put("parentName", getParent().getName());
		}else{
			json.put("parentName", "");
		}
		return json;
	}
	
	/**
	 * 分类详细访问URL
	 * @return
	 */
	public String getUrl() {
		return getSite().getUrlBuffer(true, null, false).append("/topicType")
				.append("/").append(getPath()).append("/").append(INDEX).append(
						getSite().getDynamicSuffix()).toString();
	}
	
	public String getRedirectUrl() {
		StringBuffer buff=new StringBuffer();
		buff.append("/topicType")
				.append("/").append(getPath()).append("/").append(INDEX).append(
						getSite().getDynamicSuffix());
		return buff.toString();
	}
	/**
	 * 分类精华主题访问url
	 * @return
	 */
	public String getTopUrl() {
		return getSite().getUrlBuffer(true, null, false).append("/topicType")
				.append("/").append(getPath()).append("/").
				append(com.jeecms.common.web.Constants.TOP).append(
						getSite().getDynamicSuffix()).toString();
	}
	/**
	 * 获取子类目访问URL
	 * @return
	 */
	public String getTypeUrl() {
		return getSite().getUrlBuffer(true, null, false).append("/topicType")
				.append("/").append(getPath()).append(
						getSite().getDynamicSuffix()).toString();
	}
	
	public  void init(){
		if(getTopicCount()==null){
			setTopicCount(0);
		}
		if(getTopicEssenceCount()==null){
			setTopicEssenceCount(0);
		}
		if(getSubscribeCount()==null){
			setSubscribeCount(0);
		}
	}
	
	/**
	 * 获得深度
	 * 
	 * @return 第一层为0，第二层为1，以此类推。
	 */
	public int getDeep() {
		int deep = 0;
		BbsTopicType parent = getParent();
		while (parent != null) {
			deep++;
			parent = parent.getParent();
		}
		return deep;
	}
	
	public static List<BbsTopicType> getListForSelect(List<BbsTopicType> topList,
			 BbsTopicType exclude) {
		List<BbsTopicType> list = new ArrayList<BbsTopicType>();
		for (BbsTopicType c : topList) {
			addChildToList(list, c,exclude);
		}
		return list;
	}

	/**
	 * 递归将子分类加入列表。
	 * 
	 * @param list
	 *            分类容器
	 * @param type
	 *            待添加的分类，且递归添加子栏目
	 */
	private static void addChildToList(List<BbsTopicType> list, BbsTopicType type,
			BbsTopicType exclude) {
		list.add(type);
		Set<BbsTopicType> child = type.getChild();
		for (BbsTopicType c : child) {
			addChildToList(list, c, exclude);
		}
	}
	
	public String getLftName() {
		return DEF_LEFT_NAME;
	}

	public String getRgtName() {
		return DEF_RIGHT_NAME;
	}

	public String getParentName() {
		return DEF_PARENT_NAME;
	}

	public Integer getParentId() {
		BbsTopicType parent=getParent();
		if (parent != null) {
			return parent.getId();
		} else {
			return null;
		}
	}
	
	public boolean getDisplay() {
		return super.isDisplay();
	}

	public String getTreeCondition() {
		return "";
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsTopicType () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsTopicType (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsTopicType (
		java.lang.Integer id,
		java.lang.String name,
		java.lang.String path,
		java.lang.Integer lft,
		java.lang.Integer rgt,
		java.lang.Integer priority,
		boolean display,
		java.lang.Integer topicCount,
		java.lang.Integer topicEssenceCount,
		java.lang.Integer subscribeCount) {

		super (
			id,
			name,
			path,
			lft,
			rgt,
			priority,
			display,
			topicCount,
			topicEssenceCount,
			subscribeCount);
	}

/*[CONSTRUCTOR MARKER END]*/


}