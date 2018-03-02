package com.jeecms.bbs.action.front;
import static com.jeecms.bbs.Constants.TPLDIR_FORUM;
import static com.jeecms.bbs.Constants.TPLDIR_INDEX;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.cache.TopicCountEhCache;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicPostOperate;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsTopicCountMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsTopicPostOperateMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;

@Controller
public class BbsTopicExtAct {
	
	public static final String TPL_TOPIC = "tpl.topic";
	public static final String TPL_NO_VIEW = "tpl.noview";
	public static final String TPL_TOPIC_PAGE = "tpl.topicPage";
	public static final String TPL_POST_PAGE = "tpl.postPage";
	public static final String TPL_POST_LIST = "tpl.postList";
	
	/**
	 * 主题操作 
	 * @param topicId 主题Id
	 * @param userId 用户Id
	 * @param operate 操作类型 0点赞 1收藏 2关注  3取消点赞 4取消收藏 5取消关注 
	 */
	@RequestMapping(value = "/topic/operate.jspx")
	public void operate(Integer topicId, Integer operate,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws JSONException {
		BbsUser user = CmsUtils.getUser(request);
		JSONObject object = new JSONObject();
		if (user == null) {
			object.put("result", false);
		} else {
			object.put("result", true);
			topicOperateMng.topicOperate(topicId, user.getId(), operate);
			if(operate!=null){
				if(operate.equals(BbsTopicPostOperate.OPT_ATTENT)){
					topicCountMng.topicAttent(topicId);
				}else if(operate.equals(BbsTopicPostOperate.OPT_CANCEL_ATTENT)){
					topicCountMng.topicCancelAttent(topicId);
				}else if(operate.equals(BbsTopicPostOperate.OPT_CANCEL_COLLECT)){
					topicCountMng.topicCancelCollect(topicId);
				}else if(operate.equals(BbsTopicPostOperate.OPT_CANCEL_UP)){
					topicCountMng.topicCancelUp(topicId);
				}else if(operate.equals(BbsTopicPostOperate.OPT_COLLECT)){
					topicCountMng.topicCollect(topicId);
				}else if(operate.equals(BbsTopicPostOperate.OPT_UP)){
					topicCountMng.topicUp(topicId);
				}
			}
		}
		ResponseUtils.renderJson(response, object.toString());
	}

	@RequestMapping(value = "/topic/collect_exist.jspx")
	public void collect_exist(Integer topicId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		JSONObject object = new JSONObject();
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			object.put("result", false);
		} else {
			List<BbsTopicPostOperate>list=topicOperateMng.getList(topicId, 
					BbsTopicPostOperate.DATA_TYPE_TOPIC,
					user.getId(), BbsTopicPostOperate.OPT_COLLECT,0,1);
			if (list!=null&&list.size()>0) {
				object.put("result", true);
			} else {
				object.put("result", false);
			}
		}
		ResponseUtils.renderJson(response, object.toString());
	}
	
	@RequestMapping(value = "/topic/postListByAuthor*.jspx")
	public String listTopicPostByAuthor(Integer topicId,
			Integer createrId, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		return topic(topicId,createrId, request,response, model);
	}
	
	/**
	 * 设置主题排序
	 * @param topicId
	 * @param orderBy
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/topic/setOrderBy.jspx")
	public String setOrderBy(Integer topicId,Integer orderBy,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model){
		CmsSite site=CmsUtils.getSite(request);
		if(orderBy==null){
			orderBy=1;
		}
		if(topicId!=null){
			BbsTopic topic=bbsTopicMng.findById(topicId);
			if(topic!=null){
				CookieUtils.addOrderByCookie(request, response, 
						topic.getForum().getId(),
						orderBy, 3600*24*365, site.getDomain(), "/");
				return "redirect:"+topic.getRedirectUrl();
			}
		}
		FrontUtils.frontData(request, model, site);
		return FrontUtils.pageNotFound(request, response, model);
	}
	
	/**
	 * 分页获取主题列表
	 * @param forumId
	 * @param topicLevel
	 * @param pageNo
	 */
	@RequestMapping(value = "/topic/getListMore*.jspx")
	public String getListMoreTopic(Integer forumId,Integer topicLevel,
			Integer topicTypeId,Integer orderBy,Integer pageNo,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		CmsSite site = CmsUtils.getSite(request);
		if(forumId!=null){
			if(orderBy==null){
				orderBy= CookieUtils.getOrderBy(forumId, request);
			}
		}
		model.addAttribute("forumId", forumId);
		model.addAttribute("topicLevel", topicLevel);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("orderBy", orderBy);
		model.addAttribute("topicTypeId", topicTypeId);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_TOPIC_PAGE);
	}
	
	@RequestMapping(value = "/topic/getPostPage.jspx")
	public String getPostPageByTopic(Integer topicId,Integer orderBy,
			Integer pageNo, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		CmsSite site = CmsUtils.getSite(request);
		if(pageNo==null){
			pageNo=1;
		}
		if(topicId!=null){
			BbsTopic topic=bbsTopicMng.findById(topicId);
			if(topic!=null){
				if(orderBy==null){
					orderBy= CookieUtils.getOrderBy(topic.getForum().getId(), request);
				}
			}
			model.addAttribute("topicId", topicId);
			model.addAttribute("topic", topic);
		}
		model.put("orderBy",orderBy);
		model.addAttribute("pageNo", pageNo);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_POST_PAGE);
	}
	
	@RequestMapping(value = "/topic/getPostList.jspx")
	public String getPostListByTopic(Integer topicId,
			Integer createrId,Integer option,
			Integer pageNo, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		CmsSite site = CmsUtils.getSite(request);
		if(pageNo==null){
			pageNo=1;
		}
		if(option==null){
			option=BbsPost.OPT_ALL;
		}
		Integer orderBy=0;
		if(topicId!=null){
			BbsTopic topic=bbsTopicMng.findById(topicId);
			if(topic!=null){
				orderBy= CookieUtils.getOrderBy(topic.getForum().getId(), request);
			}
		}
		model.put("orderBy",orderBy);
		model.put("createrId", createrId);
		model.put("topicId", topicId);
		model.put("pageNo", pageNo);
		model.put("option", option);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_POST_LIST);
	}
	
	private String topic(Integer topicId,Integer createrId,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		BbsForum forum;
		BbsTopic topic;
		if(topicId!=null){
			topic= bbsTopicMng.findById(topicId);
			if(topic!=null){
				forum=topic.getForum();
			}else{
				return FrontUtils.pageNotFound(request, response, model); 
			}
		}else{
			return FrontUtils.pageNotFound(request, response, model); 
		}
		boolean check = checkView(forum, user, site);
		if (!check) {
			model.put("msg",MessageResolver.getMessage(request, "member.hasnopermission"));
			return FrontUtils.getTplPath(request, site,
					TPLDIR_FORUM, TPL_NO_VIEW);
		}
		//收费模式
		if(topic.getCharge()){
			if(user==null){
				session.setAttribute(request, response, "loginSource", "charge");
				return FrontUtils.showLogin(request, model, site);
			}else{
				//非作者且非版主未购买
				if(!topic.getCreater().equals(user)&&!user.getModerator()){
					//用户已登录判断是否已经购买
					boolean hasBuy=orderMng.hasBuyTopic(user.getId(), topic.getId());
					if(!hasBuy){
						try {
							String rediretUrl="/topic/buy.jspx?topicId="+topic.getId();
							if(StringUtils.isNotBlank(site.getContextPath())){
								rediretUrl=site.getContextPath()+rediretUrl;
							}
							response.sendRedirect(rediretUrl);
						} catch (IOException e) {
							//e.printStackTrace();
						}
					}
				}
			}
		}
		topicCountEhCache.setViewCount(topicId);
		model.put("moderators", checkModerators(forum, user, site));
		model.put("forum", forum);
		model.put("topic", topic);
		model.put("createrId", createrId);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_TOPIC, TPL_TOPIC);
	}
	
	private boolean checkView(BbsForum forum, BbsUser user, CmsSite site) {
		if(forum==null){
			return false;
		}else if (forum.getGroupViews() == null) {
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				return false;
			}
			if (forum.getGroupViews().indexOf("," + group.getId() + ",") == -1) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkModerators(BbsForum forum, BbsUser user, CmsSite site) {
		if (forum.getGroupViews() == null) {
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				return false;
			}
			if (!group.hasRight(forum, user)) {
				return false;
			}
		}
		return true;
	}
	
	@Autowired
	private BbsTopicPostOperateMng topicOperateMng;
	@Autowired
	private BbsTopicCountMng topicCountMng;
	@Autowired
	private BbsTopicMng bbsTopicMng;
	@Autowired
	private BbsForumMng bbsForumMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private TopicCountEhCache topicCountEhCache;
	@Autowired
	private SessionProvider session;
	@Autowired
	private BbsOrderMng orderMng;
}
