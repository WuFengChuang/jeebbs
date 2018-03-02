package com.jeecms.bbs.entity;

import static com.jeecms.bbs.Constants.DAY_MILLIS;
import static com.jeecms.bbs.Constants.TPLDIR_FORUM;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;
import static com.jeecms.bbs.Constants.TPL_SUFFIX;
import static com.jeecms.common.web.Constants.INDEX;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.bbs.entity.base.BaseBbsForum;
import com.jeecms.common.hibernate4.PriorityInterface;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.springmvc.MessageResolver;

public class BbsForum extends BaseBbsForum implements PriorityInterface{
	private static final long serialVersionUID = 1L;
	
	public JSONObject convertToJson(Integer https) 
			throws JSONException{
		JSONObject json=new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (getPath()!=null) {
			json.put("path", getPath());
		}else{
			json.put("path", "");
		}
		if (getTitle()!=null) {
			json.put("title", getTitle());
		}else{
			json.put("title", "");
		}
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
				json.put("lastPostUrl", getLastPost().getWholeUrl());
			}else{
				json.put("lastPostUrl", "");
			}
		}
		json.put("priority", getPriority());
		if(StringUtils.isNotBlank(getDescription())){
			json.put("description", getDescription());
		}else{
			json.put("description", "");
		}
		if(StringUtils.isNotBlank(getKeywords())){
			json.put("keywords", getKeywords());
		}else{
			json.put("keywords", "");
		}
		if(StringUtils.isNotBlank(getForumRule())){
			json.put("forumRule", getForumRule());
		}else{
			json.put("forumRule", "");
		}
		if(getTopicTotal()!=null){
			json.put("topicTotal", getTopicTotal());
		}else{
			json.put("topicTotal", "0");
		}
		if(getPostTotal()!=null){
			json.put("postTotal", getPostTotal());
		}else{
			json.put("postTotal", 0);
		}
		if(getPostToday()!=null){
			json.put("postToday", getPostToday());
		}else{
			json.put("postToday", 0);
		}
		if(getPointTopic()!=null){
			json.put("pointTopic", getPointTopic());
		}else{
			json.put("pointTopic", 0);
		}
		if(getPointReply()!=null){
			json.put("pointReply", getPointReply());
		}else{
			json.put("pointReply", 0);
		}
		if(getPointPrime()!=null){
			json.put("pointPrime", getPointPrime());
		}else{
			json.put("pointPrime", "");
		}
		if(getLastTime()!=null){
			json.put("lastTime",DateUtils.parseDateToTimeStr(getLastTime()));
		}else{
			json.put("lastTime", "");
		}
		if(StringUtils.isNotBlank(getModerators())){
			json.put("moderators", getModerators());
		}else{
			json.put("moderators", "");
		}
		if(getSupportReward()!=null){
			json.put("supportReward", getSupportReward());
		}else{
			json.put("supportReward", false);
		}
		json.put("categoryId", getCategory().getId());
		json.put("categoryTitle", getCategory().getTitle());
		if(getLastReply()!=null){
			json.put("lastReplyId", getLastReply().getId());
			json.put("lastReplyUsername", getLastReply().getUsername());
		}else{
			json.put("lastReplyId","");
			json.put("lastReplyUsername", "");
		}
		if(getLastPost()!=null){
			json.put("lastPostId", getLastPost().getId());
			json.put("lastPostTitle", getLastPost().getTitle());
		}else{
			json.put("lastPostId", "");
			json.put("lastPostTitle", "");
		}
		if(StringUtils.isNotBlank(getOuterUrl())){
			json.put("outerUrl", getOuterUrl());
		}else{
			json.put("outerUrl", "");
		}
		if(StringUtils.isNotBlank(getGroupViews())){
			json.put("groupViews", getGroupViews());
		}else{
			json.put("groupViews", "");
		}
		if(StringUtils.isNotBlank(getGroupTopics())){
			json.put("groupTopics", getGroupTopics());
		}else{
			json.put("groupTopics", "");
		}
		if(StringUtils.isNotBlank(getGroupReplies())){
			json.put("groupReplies", getGroupReplies());
		}else{
			json.put("groupReplies", "");
		}
		if(StringUtils.isNotBlank(getTplForum())){
			json.put("tplForum", getTplForum());
		}else{
			json.put("tplForum", "");
		}
		if(StringUtils.isNotBlank(getTplTopic())){
			json.put("tplTopic", getTplTopic());
		}else{
			json.put("tplTopic", "");
		}
		if(StringUtils.isNotBlank(getTplMobileForum())){
			json.put("tplMobileForum", getTplMobileForum());
		}else{
			json.put("tplMobileForum", "");
		}
		if(StringUtils.isNotBlank(getTplMobileTopic())){
			json.put("tplMobileTopic", getTplMobileTopic());
		}else{
			json.put("tplMobileTopic", "");
		}
		json.put("prestigeTopic",getPrestigeTopic());
		json.put("prestigeReply", getPrestigeReply());
		json.put("prestigePrime0", getPrestigePrime0());
		json.put("prestigePrime1", getPrestigePrime1());
		if(getTopicNeedCheck()!=null){
			json.put("topicNeedCheck", getTopicNeedCheck());
		}else{
			json.put("topicNeedCheck", false);
		}
		if(getPostNeedCheck()!=null){
			json.put("postNeedCheck", getPostNeedCheck());
		}else{
			json.put("postNeedCheck", false);
		}
		return json;
	}

	public void init() {
		if (getPointPrime() == null) {
			setPointPrime(0);
		}
		if (getPointReply() == null) {
			setPointReply(0);
		}
		if (getPointTopic() == null) {
			setPointTopic(0);
		}
		if (getPostToday() == null) {
			setPostToday(0);
		}
		if (getPostTotal() == null) {
			setPostTotal(0);
		}
		if (getTopicLockLimit() == null) {
			setTopicLockLimit(0);
		}
		if (getTopicTotal() == null) {
			setTopicTotal(0);
		}
		if(getPrestigeAvailable()==null){
			setPrestigeAvailable(true);
		}
		if(getPointAvailable()==null){
			setPointAvailable(true);
		}
		if(getSupportReward()==null){
			setPointAvailable(false);
		}
		if(getPrestigePrime1()==null){
			setPrestigePrime1(0);
		}
		if(getPrestigePrime2()==null){
			setPrestigePrime2(getPrestigePrime1());
		}
		if(getPrestigePrime3()==null){
			setPrestigePrime3(getPrestigePrime1());
		}
		if(getTopicLockLimit()==null){
			setTopicLockLimit(0);
		}
		if(getPrestigeTopic()==null){
			setPrestigeTopic(0);
		}
		if(getPrestigePrime0()==null){
			setPrestigePrime0(0);
		}
		if(getPrestigeReply()==null){
			setPrestigeReply(0);
		}
		if(getSupportReward()==null){
			setSupportReward(false);
		}
		if(getPriority()==null){
			setPriority(10);
		}
		if(getTopicNeedCheck()==null){
			setTopicNeedCheck(false);
		}
		if(getPostNeedCheck()==null){
			setPostNeedCheck(false);
		}
	}

	/**
	 * 获得访问路径。如：http://bbs.jeecms.com/luntan/index.htm
	 * 
	 * @return
	 */
	public String getUrl() {
		String url = getOuterUrl();
		if (!StringUtils.isBlank(url)) {
			// 外部链接
			if (url.startsWith("http://")) {
				return url;
			} else if (url.startsWith("/")) {
				return getSite().getUrl() + url;
			} else {
				return "http://" + url;
			}
		} else {
			return getSite().getUrlBuffer(true, null, false).append("/")
					.append(getPath()).append("/").append(INDEX).append(
							getSite().getDynamicSuffix()).toString();
		}
	}
	
	public String getWholeUrl() {
		String url = getOuterUrl();
		if (!StringUtils.isBlank(url)) {
			// 外部链接
			if (url.startsWith("http://")) {
				return url;
			} else if (url.startsWith("/")) {
				return getSite().getUrl() + url;
			} else {
				return "http://" + url;
			}
		} else {
			return getSite().getUrlBuffer(true, true, false).append("/")
					.append(getPath()).append("/").append(INDEX).append(
							getSite().getDynamicSuffix()).toString();
		}
	}
	
	public String getHttpsUrl() {
		String url = getOuterUrl();
		if (!StringUtils.isBlank(url)) {
			// 外部链接
			if (url.startsWith("https://")) {
				return url;
			} else if (url.startsWith("/")) {
				return getSite().getUrl() + url;
			} else {
				return "https://" + url;
			}
		} else {
			return getSite().getHttpsUrlBuffer(true, null, false).append("/")
					.append(getPath()).append("/").append(INDEX).append(
							getSite().getDynamicSuffix()).toString();
		}
	}
	
	public String getWholeHttpsUrl() {
		String url = getOuterUrl();
		if (!StringUtils.isBlank(url)) {
			// 外部链接
			if (url.startsWith("https://")) {
				return url;
			} else if (url.startsWith("/")) {
				return getSite().getUrl() + url;
			} else {
				return "https://" + url;
			}
		} else {
			return getSite().getHttpsUrlBuffer(true, true, false).append("/")
					.append(getPath()).append("/").append(INDEX).append(
							getSite().getDynamicSuffix()).toString();
		}
	}

	public String getRedirectUrl() {
		String url = "/" + getPath() + "/" + INDEX
				+ getSite().getDynamicSuffix();
		return url;
	}

	/**
	 * 是否锁定主题
	 * 
	 * @param time
	 *            主题发表时间
	 * @return
	 */
	public boolean isTopicLock(long time) {
		if (getTopicLockLimit() == 0) {
			return false;
		}
		return System.currentTimeMillis() - time > getTopicLockLimit()
				* DAY_MILLIS;
	}
	
	public String getTplForumOrDef(HttpServletRequest request) {
		String tpl = getTplForum();
		if (!StringUtils.isBlank(tpl)) {
			return tpl;
		} else {
			return getTplForumDef(getSite().getSolutionPath(),true,request);
		}
	}
	
	public String getMobileTplForumOrDef(HttpServletRequest request) {
		String tpl = getTplMobileForum();
		if (!StringUtils.isBlank(tpl)) {
			return tpl;
		} else {
			return getTplForumDef(getSite().getMobileSolutionPath(),true, request);
		}
	}

	public String getTplTopicOrDef(HttpServletRequest request) {
		String tpl = getTplTopic();
		if (!StringUtils.isBlank(tpl)) {
			return tpl;
		} else {
			return getTplTopicDef(getSite().getSolutionPath(),true,request);
		}
	}
	
	public String getMobileTplTopicOrDef(HttpServletRequest request) {
		String tpl = getTplMobileTopic();
		if (!StringUtils.isBlank(tpl)) {
			return tpl;
		} else {
			return getTplTopicDef(getSite().getMobileSolutionPath(),true,request);
		}
	}
	
	public static String getTplTopicDef(String sol,boolean def,
			HttpServletRequest request){
		StringBuilder t = new StringBuilder();
		t.append(sol).append("/");
		t.append(TPLDIR_TOPIC);
		t.append("/");
		t.append(MessageResolver.getMessage(request, "tpl.topic"));
		if(def){
			t.append(TPL_SUFFIX);
		}
		return t.toString();
	}
	
	public static String getTplForumDef(String sol, boolean def,
			HttpServletRequest request){
		StringBuilder t = new StringBuilder();
		t.append(sol).append("/");
		t.append(TPLDIR_FORUM);
		t.append("/");
		t.append(MessageResolver.getMessage(request, "tpl.forum"));
		if(def){
			t.append(TPL_SUFFIX);
		}
		return t.toString();
	}
	
	public String getTplForum() {
		BbsForumExt ext = getForumExt();
		if (ext != null) {
			return ext.getTplForum();
		} else {
			return null;
		}
	}
	
	public String getTplTopic() {
		BbsForumExt ext = getForumExt();
		if (ext != null) {
			return ext.getTplTopic();
		} else {
			return null;
		}
	}

	public String getTplMobileForum() {
		BbsForumExt ext = getForumExt();
		if (ext != null) {
			return ext.getTplMobileForum();
		} else {
			return null;
		}
	}

	public String getTplMobileTopic() {
		BbsForumExt ext = getForumExt();
		if (ext != null) {
			return ext.getTplMobileTopic();
		} else {
			return null;
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public BbsForum() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsForum(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsForum(java.lang.Integer id,
			com.jeecms.bbs.entity.BbsCategory category,
			com.jeecms.core.entity.CmsSite site, java.lang.String path,
			java.lang.String title, java.lang.Integer topicLockLimit,
			java.lang.Integer priority, java.lang.Integer topicTotal,
			java.lang.Integer postTotal, java.lang.Integer postToday,
			java.lang.Integer pointTopic, java.lang.Integer pointReply,
			java.lang.Integer pointPrime) {

		super(id, category, site, path, title, topicLockLimit, priority,
				topicTotal, postTotal, postToday, pointTopic, pointReply,
				pointPrime);
	}

	

	/* [CONSTRUCTOR MARKER END] */

}