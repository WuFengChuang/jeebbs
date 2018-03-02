package com.jeecms.bbs.entity;

import static com.jeecms.bbs.web.FrontUtils.replaceSensitivity;
import static com.jeecms.common.web.Constants.SPT;
import static com.jeecms.bbs.Constants.DAY_MILLIS;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsTopic;
import com.jeecms.common.util.DateUtils;
import com.jeecms.core.entity.CmsSite;

public class BbsTopic extends BaseBbsTopic {
	private static final long serialVersionUID = 1L;
	/**
	 * 正常状态
	 */
	public static final short NORMAL = 0;
	/**
	 * 屏蔽状态
	 */
	public static final short SHIELD = -1;
	/**
	 * 锁定状态
	 */
	public static final short LOCKED = 1;
	/**
	 * 待审核状态
	 */
	public static final boolean CHECKING = false;
	/**
	 * 审核状态
	 */
	public static final boolean CHECKED = true;
	
	/**
	 * 普通帖
	 */
	public static final int TOPIC_NORMAL = 100;
	
	/**
	 * 投票帖(多选)
	 */
	public static final int TOPIC_VOTE = 101;
	/**
	 * 投票帖（单选）
	 */
	public static final int TOPIC_VOTE_SINGLE = 102;
	/**
	 * 未推荐
	 */
	public static final short TOPIC_NO_RECOMMEND= 0;
	/**
	 * 版主推荐
	 */
	public static final short TOPIC_MODERATOR_RECOMMEND= 1;
	/**
	 * 前台状态
	 * 
	 * @return 3:锁;2:旧;1:新
	 */
	public short getFrontStatus() {
		if (isLocked()) {
			return 3;
		} else if (System.currentTimeMillis() - getLastTime().getTime() > DAY_MILLIS) {
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * 是否热帖
	 * 
	 * @return
	 */
	public boolean isHot() {
		return getReplyCount() >= 30;
	}

	/**
	 * 是否锁定
	 * 
	 * @return
	 */
	public boolean isLocked() {
		return getStatus() == LOCKED
				|| getForum().isTopicLock(getCreateTime().getTime());
	}

	/**
	 * 是否屏蔽
	 * 
	 * @return
	 */
	public boolean isShield() {
		return getStatus() == SHIELD;
	}

	/**
	 * 样式是否有效
	 * 
	 * @return
	 */
	public boolean isStyle() {
		Date d = getStyleTime();
		if (d == null) {
			return true;
		}
		long time = d.getTime();
		return time - System.currentTimeMillis() > 0;
	}

	public String getUrl() {
		return getSite().getUrlBuffer(true, null, false).append("/").append(
				getForum().getPath()).append("/").append(getId()).append(
				getSite().getDynamicSuffix()).toString();
	}
	
	public String getWholeUrl() {
		return getSite().getUrlBuffer(true, true, false).append("/").append(
				getForum().getPath()).append("/").append(getId()).append(
				getSite().getDynamicSuffix()).toString();
	}
	
	public String getWholeHttpsUrl() {
		return getSite().getHttpsUrlBuffer(true, true, false).append("/").append(
				getForum().getPath()).append("/").append(getId()).append(
				getSite().getDynamicSuffix()).toString();
	}

	/**
	 * 获得访问路径前缀。如：http://bbs.jeecms.com/luntan/2
	 * 
	 * @return
	 */
	public StringBuilder getUrlPerfix() {
		return getSite().getUrlBuffer(true, null, false).append("/").append(
				getForum().getPath()).append("/").append(getId());
	}
	
	public StringBuilder getWholeUrlPerfix() {
		return getSite().getUrlBuffer(true, true, false).append("/").append(
				getForum().getPath()).append("/").append(getId());
	}
	
	public StringBuilder getWholeHttpsUrlPerfix() {
		return getSite().getHttpsUrlBuffer(true, true, false).append("/").append(
				getForum().getPath()).append("/").append(getId());
	}

	public String getRedirectUrl() {
		String path = getForum().getPath();
		String url = "/" + path + "/" + getId() + getSite().getDynamicSuffix();
		return url;
	}
	
	public JSONObject convertToJson(Integer https,boolean hasCollect,
			boolean hasAttent,boolean hasUp,Boolean voted,List<BbsVoteItem>items) 
			throws JSONException{
		JSONObject json=new JSONObject();
		json.put("id", getId());
		json.put("title", getTitle());
		json.put("createTime", DateUtils.parseDateToTimeStr(getCreateTime()));
		json.put("viewCount", getViewCount());
		json.put("viewsDay", getViewsDay());
		json.put("viewsWeek", getViewsWeek());
		json.put("viewsMonth", getViewsMonth());
		json.put("replyCount", getReplyCount());
		json.put("replyCountDay", getReplyCountDay());
		json.put("topLevel", getTopLevel());
		json.put("primeLevel", getPrimeLevel());
		json.put("status", getStatus());
		json.put("hasAttach", getAffix());
		json.put("moderatorReply", getModeratorReply());
		json.put("replyIds", getHaveReply());
		json.put("allowReply", getAllayReply());
		if(https==com.jeecms.bbs.api.Constants.URL_HTTP){
			json.put("url", getWholeUrl());
			if(getLastPost()!=null){
				json.put("lastPostUrl", getLastPost().getWholeUrl());
			}else{
				json.put("lastPostUrl", "");
			}
		}else{
			json.put("url", getWholeHttpsUrl());
			if(getLastPost()!=null){
				json.put("lastPostUrl", getLastPost().getWholeHttpsUrl());
			}else{
				json.put("lastPostUrl", "");
			}
		}
		if(getLastTime()!=null){
			json.put("lastTime",DateUtils.parseDateToTimeStr(getLastTime()));
		}else{
			json.put("lastTime", "");
		}
		if(getLastReply()!=null){
			json.put("lastReplyId", getLastReply().getId());
			json.put("lastReplyUsername", getLastReply().getUsername());
		}else{
			json.put("lastReplyId","");
			json.put("lastReplyUsername", "");
		}
		json.put("createrId", getCreater().getId());
		json.put("createrUsername", getCreater().getUsername());
		json.put("forumId", getForum().getId());
		json.put("forumTitle", getForum().getTitle());
		if(getLastPost()!=null){
			json.put("lastPostId", getLastPost().getId());
			json.put("lastPostTitle", getLastPost().getTitle());
		}else{
			json.put("lastPostId", "");
			json.put("lastPostTitle", "");
		}
		if(getFirstPost()!=null){
			json.put("firstPostId", getFirstPost().getId());
			json.put("firstPostTitle", getFirstPost().getTitle());
		}else{
			json.put("firstPostId", "");
			json.put("firstPostTitle", "");
		}
		json.put("hasCollect", hasCollect);
		json.put("hasAttent", hasAttent);
		json.put("collections", getCollections());
		json.put("rewardCount", getRewards());
		json.put("upCount", getUps());
		json.put("attentCount", getAttentions());
		json.put("charge", getCharge());
		json.put("chargeModel", getChargeModel());
		json.put("chargeAmount", getChargeAmount());
		if(voted!=null){
			json.put("voted", voted);
		}
		if(items!=null&&items.size()>0){
			JSONArray voteItemArray=new JSONArray();
			for(BbsVoteItem item:items){
				voteItemArray.put(item.convertToJson());
			}
			json.put("voteItems",voteItemArray);
		}
		return json;
	}
	
	public String getUrlWhole() {
		return getUrlDynamic(true);
	}
	
	public String getUrlDynamic(Boolean whole) {
		CmsSite site = getSite();
		StringBuilder url = site.getUrlBuffer(true, whole, false);
		url.append(SPT).append(getForum().getPath());
		url.append(SPT).append(getId()).append(site.getDynamicSuffix());
		return url.toString();
	}

	public void init() {
		Date now = new Timestamp(System.currentTimeMillis());
		if (getCreateTime() == null) {
			setCreateTime(now);
		}
		if (getLastTime() == null) {
			setLastTime(now);
		}
		if (getPrimeLevel() == null) {
			setPrimeLevel(NORMAL);
		}
		if (getSortTime() == null) {
			setSortTime(now);
		}
		if (getViewCount() == null) {
			setViewCount(0L);
		}
		if (getReplyCount() == null) {
			setReplyCount(0);
		}
		if(getReplyCountDay()==null){
			setReplyCountDay(0);
		}
		if (getTopLevel() == null) {
			setTopLevel(NORMAL);
		}
		if (getStyleBold() == null) {
			setStyleBold(false);
		}
		if (getStatus() == null) {
			setStatus(NORMAL);
		}
		if (getModeratorReply() == null) {
			setModeratorReply(false);
		}
		if(getAllayReply()==null){
			setAllayReply(true);
		}
		if(getHasSofeed()==null){
			setHasSofeed(false);
		}
		if(getViewsDay()==null){
			setViewsDay(0L);
		}
		if(getViewsWeek()==null){
			setViewsWeek(0L);
		}
		if(getViewsMonth()==null){
			setViewsMonth(0L);
		}
		if ( getAffix () == null) {
			setAffix(true);
		}
		if(getCheckStatus()==null){
			setCheckStatus(true);
		}
		if(getRecommend()==null){
			setRecommend(TOPIC_NO_RECOMMEND);
		}
	}
	
	public String getTitle(){
		BbsTopicText text = getTopicText();
		if (text == null) {
			return null;
		} else {
			return replaceSensitivity(text.getTitle());
		}
	}
	
	public void setTitle(String title) {
		BbsTopicText text = getTopicText();
		if (text != null) {
			text.setTitle(title);
		} 
	}
	public void setTopicTitle(String title) {
		super.setTitle(title);
	}
	
	public void setTopicCharge(BbsTopicCharge charge) {
		Set<BbsTopicCharge> set = getChargeSet();
		if (set == null) {
			set = new HashSet<BbsTopicCharge>();
			setChargeSet(set);
		}
		if (!set.isEmpty()) {
			set.clear();
		}
		set.add(charge);
	}
	
	
	public short getCategory(){
		return TOPIC_NORMAL;
	}
	
	public void addToTypes(BbsTopicType type) {
		Set<BbsTopicType> types = getTypes();
		if (types == null) {
			types = new HashSet<BbsTopicType>();
			setTypes(types);
		}
		types.add(type);
	}
	
	public Integer getCollections(){
		BbsTopicCount count = getTopicCount();
		if (count == null) {
			return 0;
		} else {
			return count.getCollections();
		}
	}
	
	public Integer getRewards(){
		BbsTopicCount count = getTopicCount();
		if (count == null) {
			return 0;
		} else {
			return count.getRewards();
		}
	}
	
	public Integer getUps(){
		BbsTopicCount count = getTopicCount();
		if (count == null) {
			return 0;
		} else {
			return count.getUps();
		}
	}
	
	public Integer getAttentions(){
		BbsTopicCount count = getTopicCount();
		if (count == null) {
			return 0;
		} else {
			return count.getAttentions();
		}
	}
	
	public BbsTopicCharge getTopicCharge() {
		Set<BbsTopicCharge> set = getChargeSet();
		if (set != null && set.size() > 0) {
			return set.iterator().next();
		} else {
			return null;
		}
	}
	
	public boolean getCharge() {
		BbsTopicCharge c=getTopicCharge();
		return c!=null&&c.getChargeAmount()>0&&c.getChargeReward().equals(BbsTopicCharge.MODEL_CHARGE);
	}
	
	public Short getChargeModel() {
		BbsTopicCharge c=getTopicCharge();
		if(c==null){
			return BbsTopicCharge.MODEL_FREE;
		}else{
			return c.getChargeReward();
		}
	}
	
	public Double getChargeAmount() {
		BbsTopicCharge charge= getTopicCharge();
		if(charge!=null){
			return charge.getChargeAmount();
		}else{
			return 0d;
		}
	}
	
	public void addToRewardFixs(Double fixVal) {
		List<BbsTopicRewardFix> list = getRewardFixs();
		if (list == null) {
			list = new ArrayList<BbsTopicRewardFix>();
			setRewardFixs(list);
		}
		BbsTopicRewardFix rewardFix = new BbsTopicRewardFix();
		rewardFix.setFixVal(fixVal);
		list.add(rewardFix);
	}
	
	public Boolean getRewardPattern() {
		BbsTopicCharge charge= getTopicCharge();
		if(charge!=null){
			return charge.getRewardPattern();
		}else{
			return false;
		}
	}
	
	public Double getRewardRandomMax() {
		BbsTopicCharge charge= getTopicCharge();
		if(charge!=null){
			return charge.getRewardRandomMax();
		}else{
			return 0d;
		}
	}
	
	public Double getRewardRandomMin() {
		BbsTopicCharge charge= getTopicCharge();
		if(charge!=null){
			return charge.getRewardRandomMin();
		}else{
			return 0d;
		}
	}
	
	public Double[] getRewardFixValues() {
		Double[] fixs=null;
		List<BbsTopicRewardFix>list= getRewardFixs();
		if(list!=null&&list.size()>0){
			fixs=new Double[list.size()];
			for(int i=0;i<list.size();i++){
				fixs[i]=list.get(i).getFixVal();
			}
		}
		return fixs;
	}
	
	public Double getDayAmount() {
		BbsTopicCharge charge= getTopicCharge();
		if(charge!=null){
			return charge.getDayAmount();
		}else{
			return 0d;
		}
	}
	
	public Double getMonthAmount() {
		BbsTopicCharge charge= getTopicCharge();
		if(charge!=null){
			return charge.getMonthAmount();
		}else{
			return 0d;
		}
	}
	
	public Double getYearAmount() {
		BbsTopicCharge charge= getTopicCharge();
		if(charge!=null){
			return charge.getYearAmount();
		}else{
			return 0d;
		}
	}
	
	public Double getTotalAmount() {
		BbsTopicCharge charge= getTopicCharge();
		if(charge!=null){
			return charge.getTotalAmount();
		}else{
			return 0d;
		}
	}
	
	public Date getLastBuyTime() {
		BbsTopicCharge charge= getTopicCharge();
		if(charge!=null){
			return charge.getLastBuyTime();
		}else{
			return null;
		}
	}
	
	public String getCreateTimeHtml(){
		Date time=getCreateTime();
		Date now=Calendar.getInstance().getTime();
		if(DateUtils.isInHour(time)){
			return DateUtils.getDiffIntMinuteTwoDate(time, now)+"分钟前"; 
		}else if(DateUtils.isToday(time)){
			return DateUtils.getDiffIntHourTwoDate(time, now)+"小时前"; 
		}else{
			return DateUtils.parseDateToDateStr(time);
		}
	}
	
	public String getLastReplyTimeHtml(){
		Date time=getLastTime();
		Date now=Calendar.getInstance().getTime();
		if(DateUtils.isInHour(time)){
			return DateUtils.getDiffIntMinuteTwoDate(time, now)+"分钟前"; 
		}else if(DateUtils.isToday(time)){
			return DateUtils.getDiffIntHourTwoDate(time, now)+"小时前"; 
		}else{
			return DateUtils.parseDateToDateStr(time);
		}
	}
	
	public static Integer[] fetchIds(Collection<BbsTopicType> types) {
		if (types == null) {
			return null;
		}
		Integer[] ids = new Integer[types.size()];
		int i = 0;
		for (BbsTopicType c : types) {
			ids[i++] = c.getId();
		}
		return ids;
	}
	
	public Integer getTypeId(){
		Set<BbsTopicType>typeSet=getTypes();
		if(typeSet!=null&&typeSet.size()>0){
			return typeSet.iterator().next().getId();
		}else{
			return 0;
		}
	}
	
	public String getTypeIds(){
		StringBuffer buff=new StringBuffer();
		Set<BbsTopicType>typeSet=getTypes();
		for(BbsTopicType t:typeSet){
			buff.append(t.getId()+",");
		}
		return buff.toString();
	}
	
	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsTopic () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsTopic (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsTopic (
		java.lang.Integer id,
		com.jeecms.core.entity.CmsSite site,
		com.jeecms.bbs.entity.BbsForum forum,
		com.jeecms.bbs.entity.BbsUser creater,
		com.jeecms.bbs.entity.BbsUser lastReply,
		java.util.Date createTime,
		java.util.Date lastTime,
		java.util.Date sortTime,
		java.lang.Long viewCount,
		java.lang.Integer replyCount,
		java.lang.Short topLevel,
		java.lang.Short primeLevel,
		java.lang.Boolean styleBold,
		java.lang.Boolean styleItalic,
		java.lang.Short status,
		java.lang.Boolean affix,
		java.lang.Boolean moderatorReply,
		java.lang.Short recommend,
		java.util.Date topTime) {

		super (
			id,
			site,
			forum,
			creater,
			lastReply,
			createTime,
			lastTime,
			sortTime,
			viewCount,
			replyCount,
			topLevel,
			primeLevel,
			styleBold,
			styleItalic,
			status,
			affix,
			moderatorReply,
			recommend,
			topTime);
	}
	/* [CONSTRUCTOR MARKER END] */

}