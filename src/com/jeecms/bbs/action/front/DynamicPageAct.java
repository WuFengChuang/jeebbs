package com.jeecms.bbs.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_FORUM;
import static com.jeecms.bbs.Constants.TPLDIR_INDEX;
import static com.jeecms.common.web.Constants.INDEX;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.bbs.cache.ForumCountCache;
import com.jeecms.bbs.cache.TopicCountEhCache;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsPostType;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsPostTypeMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.front.URLHelper;

@Controller
public class DynamicPageAct {
	private static final Logger log = LoggerFactory
			.getLogger(DynamicPageAct.class);

	public static final String TPL_INDEX = "tpl.index";
	public static final String TPL_INDEX_MODERATOR = "tpl.indexModerator";
	public static final String TPL_INDEX_RECOMMEND = "tpl.indexRecommend";
	public static final String TPL_FORUM = "tpl.forum";
	public static final String TPL_TOPIC = "tpl.topic";
	public static final String TPL_NO_VIEW = "tpl.noview";
	public static final String TPL_TOPIC_TYPE = "tpl.topicType";
	public static final String LOGIN_INPUT = "tpl.loginInput";
	public static final int TOPIC_ALL = 0;
	public static final int TOPIC_ESSENCE = 1;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap model,HttpServletResponse response) {
		CmsSite site = CmsUtils.getSite(request);
	//	model.addAttribute("site", site);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_INDEX, TPL_INDEX);
	}
	
	/**
	 * WEBLOGIC的默认路径
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index.jhtml", method = RequestMethod.GET)
	public String indexForWeblogic(HttpServletRequest request, ModelMap model,HttpServletResponse response) {
		return index(request, model,response);
	}
	
	@RequestMapping(value = "/indexModerator*.jhtml", method = RequestMethod.GET)
	public String indexForModerator(HttpServletRequest request, ModelMap model,HttpServletResponse response) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_INDEX, TPL_INDEX_MODERATOR);
	}
	
	@RequestMapping(value = "/indexRecommend*.jhtml", method = RequestMethod.GET)
	public String indexRecommend(HttpServletRequest request, ModelMap model,HttpServletResponse response) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_INDEX, TPL_INDEX_RECOMMEND);
	}

	@RequestMapping(value = "/**/*.*", method = RequestMethod.GET)
	public String dynamic(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,String ty) {
		if(ty==null){
			ty="a";
		}
		String[] paths = URLHelper.getPaths(request);
		String requestUrl = request.getRequestURI();
		String address = null;
		if(0<=requestUrl.lastIndexOf('.')){
			 address = requestUrl.substring(requestUrl.lastIndexOf('/')+1,requestUrl.lastIndexOf('.'));
		}
		if(ty!=null){
			if(ty.length()>=4){
				if("jing".equals(ty.substring(0,4))){
					return forum(paths[0],null, request, response, model,"index_jing",ty);
				}
			}
		}
		
		if("index_jing".equals(address)){
			return forum(paths[0], null,request, response, model,"index_jing",ty);
		}
		int len = paths.length;
		if (len == 1) {
			return null;
		} else if (len == 2) {
			if (paths[1].equals(INDEX)) {
				// 主题列表
				return forum(paths[0],null, request, response, model,"index",ty);
			} else {
				// 主题详细页
				try {
					Integer topicId = Integer.parseInt(paths[1]);
					return topic(paths[0], topicId, request, response, model);
				} catch (NumberFormatException e) {
					log.debug("topic id must String: {}", paths[1]);
					return FrontUtils.pageNotFound(request, response, model);
				}
			}
		}else if(len==3){
			// 主题分类列表
			return forum(paths[0], paths[2],request, response, model,"index",ty);
		}else {
			log.debug("Illegal path length: {}, paths: {}", len, paths);
			return FrontUtils.pageNotFound(request, response, model);
		}
	}
	
	private String forum(String path,String typeId,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model,String type,String ty) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		BbsForum forum = bbsForumMng.getByPath(site.getId(), path);
		boolean check = checkView(forum, user, site);
		if (!check) {
			model.put("msg",MessageResolver.getMessage(request, "member.hasnopermission"));
			return FrontUtils.getTplPath(request, site,
					TPLDIR_FORUM, TPL_NO_VIEW);
		}
		model.put("manager", checkManager(forum, user, site));
		model.put("uptop", checkUpTop(forum, user, site));
		model.put("sheild", checkShield(forum, user, site));
		model.put("moderators", checkModerators(forum, user, site));
		model.put("forum", forum);
		if(StringUtils.isNotBlank(typeId)&&StringUtils.isNumeric(typeId)){
			Integer typeIds=Integer.valueOf(typeId);
			BbsPostType postType=bbsPostTypeMng.findById(typeIds);
			if(postType!=null&&postType.getParent()==null){
				//有子类的，包含子类
				if(postType.getChilds()!=null&&postType.getChilds().size()>0){
					model.put("parentTypeId", typeIds);
				}else{
					//帖子大类(没有子类)
					model.put("typeId", typeIds);
				}
			}else{
				model.put("typeId", typeIds);
			}
		}
		model.put("type", type);
		model.put("ty", ty);
		forumCountCache.viewAndGet(forum.getId());
		FrontUtils.frontPageData(request, model);
		String equipment=(String) request.getAttribute("ua");
		if(StringUtils.isNotBlank(equipment)&&equipment.equals("mobile")){
			return forum.getMobileTplForumOrDef(request);
		}
		return forum.getTplForumOrDef(request);
		/*
		return FrontUtils.getTplPath(request, site,
				TPLDIR_FORUM, TPL_FORUM);
				*/
	}

	private String topic(String path, Integer topicId,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		BbsForum forum = bbsForumMng.getByPath(site.getId(), path);
		BbsTopic topic = bbsTopicMng.findById(topicId);
		//防止跳跃路径访问
		if(topic!=null){
			forum=topic.getForum();
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
		forumCountCache.viewAndGet(forum.getId());
		model.put("moderators", checkModerators(forum, user, site));
		model.put("forum", forum);
		model.put("topic", topic);
		model.put("orderBy", CookieUtils.getOrderBy(forum.getId(), request));
		FrontUtils.frontPageData(request, model);
		String equipment=(String) request.getAttribute("ua");
		if(StringUtils.isNotBlank(equipment)&&equipment.equals("mobile")){
			return forum.getMobileTplTopicOrDef(request);
		}
		return forum.getTplTopicOrDef(request);
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

	private boolean checkManager(BbsForum forum, BbsUser user, CmsSite site) {
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
			if (!group.topicManage()) {
				return false;
			}
		}
		return true;
	}

	private boolean checkUpTop(BbsForum forum, BbsUser user, CmsSite site) {
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
			if (group.topicTop() == 0) {
				return false;
			}
		}
		return true;
	}

	private boolean checkShield(BbsForum forum, BbsUser user, CmsSite site) {
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
			if (!group.topicShield()) {
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
	private BbsTopicMng bbsTopicMng;
	@Autowired
	private BbsForumMng bbsForumMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private TopicCountEhCache topicCountEhCache;
	@Autowired
	private BbsPostTypeMng bbsPostTypeMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private BbsOrderMng orderMng;
	@Autowired
	private ForumCountCache forumCountCache;

}
