package com.jeecms.bbs.action.member;

import static com.jeecms.common.page.SimplePage.cpn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;
import static com.jeecms.bbs.Constants.TPLDIR_SPECIAL;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicPostOperate;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsTopicPostOperateMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.MagicMessage;
import com.jeecms.core.web.front.URLHelper;


@Controller
public class AttentAct {
	public static final String USER_ATTENT_LIST = "tpl.userAttentList";
	public static final String TOPIC_ATTENT_LIST = "tpl.topicAttentList";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	public static final String USER_TOPIC_LIST = "tpl.usertopicList";
	public static final String USER_POST_LIST = "tpl.userPostList";
	public static final String PERSON_ATTENT_LIST = "tpl.personAttentList";

	
	/**关注人
	 * @param userId 被关注者ID
	 * @param operate  0关注 1取消关注
	 */
	@RequestMapping(value = "/member/attent.jhtml")
	public void userAttent( Integer userId,
			HttpServletRequest request,HttpServletResponse response, ModelMap model)
			throws JSONException {
		BbsUser user = CmsUtils.getUser(request);
		BbsUser attentUser = bbsUserMng.findById(userId);
		JSONObject object = new JSONObject();
		MagicMessage magicMessage = MagicMessage.create(request);
		int status=-1;
		if (user == null) {
			object.put("message", magicMessage
					.getMessage("friend.apply.nologin"));
		} else if (validateAttent(user, attentUser)) {
			if(user.getMyAttentions().contains(attentUser)){
				status=bbsUserMng.attentUser(user.getId(), userId,1);
			}else{
				status=bbsUserMng.attentUser(user.getId(), userId,0);
			}
		} 
		object.put("status", status);
		ResponseUtils.renderJson(response, object.toString());
	}
	
	//查看用户主题列表
	@RequestMapping(value = "/person/topics*.jhtml")
	public String personTopics(Integer userId, HttpServletRequest request,
				ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		BbsUser user=bbsUserMng.findById(userId);
		model.put("attentUser", user);
		model.put("userId", userId);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_SPECIAL, USER_TOPIC_LIST);
	}
	
	//用户的帖子回复列表
	@RequestMapping(value = "/person/posts*.jhtml")
	public String personPosts(Integer userId, HttpServletRequest request,
				ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		BbsUser user=bbsUserMng.findById(userId);
		model.put("attentUser", user);
		model.put("userId", userId);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_SPECIAL, USER_POST_LIST);
	}
	
	//用户的关注列表
	@RequestMapping(value = "/person/attentUsers*.jhtml")
	public String personAttents(Integer userId, HttpServletRequest request,
				ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		Integer pageNo=URLHelper.getPageNo(request);
		Pagination page=bbsUserMng.getPageByAttent(0, userId, 
				cpn(pageNo), CookieUtils.getPageSize(request));
		BbsUser user=bbsUserMng.findById(userId);
		model.put("user", user);
		model.put("tag_pagination", page);
		model.put("userId", userId);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_SPECIAL, PERSON_ATTENT_LIST);
	}
	
	//我关注的人
	@RequestMapping(value = "/member/myAttentUsers*.jhtml")
	public String myAttentUsers(HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		Integer pageNo=URLHelper.getPageNo(request);
		Pagination page=bbsUserMng.getPageByAttent(0, user.getId(), 
				cpn(pageNo), CookieUtils.getPageSize(request));
		model.put("user", user);
		model.put("tag_pagination", page);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, USER_ATTENT_LIST);
	}
	
	//关注我的人
	@RequestMapping(value = "/member/myFans*.jhtml")
	public String myFans(HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		Integer pageNo=URLHelper.getPageNo(request);
		Pagination page=bbsUserMng.getPageByAttent(1, user.getId(), 
				cpn(pageNo), CookieUtils.getPageSize(request));
		model.put("user", user);
		model.put("tag_pagination", page);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, USER_ATTENT_LIST);
	}
	
	//我关注的主题
	@RequestMapping(value = "/member/myAttentTopics*.jhtml")
	public String myAttentTopics(HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		Integer pageNo=URLHelper.getPageNo(request);
		Pagination page=topicPostOperateMng.getPage(
				BbsTopicPostOperate.DATA_TYPE_TOPIC, user.getId(), 
				BbsTopicPostOperate.OPT_ATTENT, pageNo, CookieUtils.getPageSize(request));
		model.put("user", user);
		model.put("tag_pagination", page);
		setDataMap(page, model);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, TOPIC_ATTENT_LIST);
	}
	
	private void setDataMap(Pagination pagination,ModelMap model){
		List<BbsTopicPostOperate> li=(List<BbsTopicPostOperate>) pagination.getList();
		Map<String,BbsTopic>topicMap=new HashMap<String,BbsTopic>();
		for(BbsTopicPostOperate data:li){
			if(data.getDataType().equals(BbsOrder.ORDER_TYPE_TOPIC)){
				topicMap.put(data.getDataId().toString(), topicMng.findById(data.getDataId()));
			}
		}
		model.addAttribute("topicMap",topicMap);
	}
	
	private boolean validateAttent(BbsUser user, BbsUser friend) {
		// 用户未登录
		if (user == null) {
			return false;
		}
		// 被关注者不存在
		if (friend == null) {
			return false;
		}
		// 不允许关注自己
		if (user.equals(friend)) {
			return false;
		}
		return true;
	}

	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsTopicMng topicMng;
	@Autowired
	private BbsTopicPostOperateMng topicPostOperateMng;
}
