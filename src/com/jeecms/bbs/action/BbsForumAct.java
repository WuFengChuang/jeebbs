package com.jeecms.bbs.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsCategory;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsForumExt;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsCategoryMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.tpl.TplManager;
import com.jeecms.core.web.CoreUtils;

@Controller
public class BbsForumAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsForumAct.class);

	@RequiresPermissions("forum:v_list")
	@RequestMapping("/forum/v_list.do")
	public String list( HttpServletRequest request,
			ModelMap model) {
		Integer siteId=CmsUtils.getSiteId(request);
		List<BbsCategory> categoryList=bbsCategoryMng.getList(siteId);
		model.addAttribute("categoryList", categoryList);
		return "forum/list";
	}

	@RequiresPermissions("forum:v_add")
	@RequestMapping("/forum/v_add.do")
	public String add(Integer categoryId,HttpServletRequest request, ModelMap model) {
		List<BbsCategory> categoryList = bbsCategoryMng.getList(CmsUtils
				.getSiteId(request));
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(CmsUtils
				.getSiteId(request));
		CmsSite site=CmsUtils.getSite(request);
		// 主题列表模板列表
		List<String> forumTplList = getTplForum(request, site, null);
		// 主题详细页模板列表
		List<String> topicTplList = getTplTopic(request, site, null);
		//手机主题列表模板列表
		List<String> mobileForumTplList = getMobileTplForum(request, site, null);
		//手机主题详细页模板列表
		List<String> mobileTopicTplList = getMobileTplTopic(request, site, null);
		model.addAttribute("forumTplList", forumTplList);
		model.addAttribute("topicTplList", topicTplList);
		model.addAttribute("mobileForumTplList", mobileForumTplList);
		model.addAttribute("mobileTopicTplList", mobileTopicTplList);
		model.put("categoryList", categoryList);
		model.put("groupList", groupList);
		model.put("categoryId", categoryId);
		return "forum/add";
	}

	@RequiresPermissions("forum:v_edit")
	@RequestMapping("/forum/v_edit.do")
	public String edit(Integer id, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		List<BbsCategory> categoryList = bbsCategoryMng.getList(CmsUtils
				.getSiteId(request));
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(CmsUtils
				.getSiteId(request));
		BbsForum forum=manager.findById(id);
		CmsSite site=CmsUtils.getSite(request);
		// 当前模板，去除基本路径
		int tplPathLength = site.getTplPath().length();
		String tplForum = forum.getTplForumOrDef(request);
		if (!StringUtils.isBlank(tplForum)) {
			tplForum = tplForum.substring(tplPathLength);
		}
		String tplMobileForum = forum.getMobileTplForumOrDef(request);
		if (!StringUtils.isBlank(tplMobileForum)) {
			tplMobileForum = tplMobileForum.substring(tplPathLength);
		}
		String tplTopic = forum.getTplTopicOrDef(request);
		if (!StringUtils.isBlank(tplTopic)) {
			tplTopic = tplTopic.substring(tplPathLength);
		}
		String tplMobileTopic = forum.getMobileTplTopicOrDef(request);
		if (!StringUtils.isBlank(tplMobileTopic)) {
			tplMobileTopic = tplMobileTopic.substring(tplPathLength);
		}
		// 主题列表模板列表
		List<String> forumTplList = getTplForum(request, site, null);
		// 主题详细页模板列表
		List<String> topicTplList = getTplTopic(request, site, null);
		//手机主题列表模板列表
		List<String> mobileForumTplList = getMobileTplForum(request, site,null);
		//手机主题详细页模板列表
		List<String> mobileTopicTplList = getMobileTplTopic(request, site, null);
		model.addAttribute("forumTplList", forumTplList);
		model.addAttribute("topicTplList", topicTplList);
		model.addAttribute("mobileForumTplList", mobileForumTplList);
		model.addAttribute("mobileTopicTplList", mobileTopicTplList);
		model.addAttribute("tplForum", tplForum);
		model.addAttribute("tplTopic", tplTopic);
		model.addAttribute("tplMobileForum", tplMobileForum);
		model.addAttribute("tplMobileTopic", tplMobileTopic);
		model.put("categoryList", categoryList);
		model.put("groupList", groupList);
		model.addAttribute("bbsForum", forum);
		model.addAttribute("pageNo", pageNo);
		return "forum/edit";
	}

	@RequiresPermissions("forum:o_save")
	@RequestMapping("/forum/o_save.do")
	public String save(BbsForum bean,BbsForumExt ext,
			Integer categoryId, Integer[] views,
			Integer[] topics, Integer[] replies, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		// 加上模板前缀
		String tplPath = site.getTplPath();
		if (!StringUtils.isBlank(ext.getTplForum())) {
			ext.setTplForum(tplPath + ext.getTplForum());
		}
		if (!StringUtils.isBlank(ext.getTplTopic())) {
			ext.setTplTopic(tplPath + ext.getTplTopic());
		}
		if (!StringUtils.isBlank(ext.getTplMobileForum())) {
			ext.setTplMobileForum(tplPath + ext.getTplMobileForum());
		}
		if (!StringUtils.isBlank(ext.getTplMobileTopic())) {
			ext.setTplMobileTopic(tplPath + ext.getTplMobileTopic());
		}
		bean = manager.save(bean,ext, categoryId, site, views, topics, replies);
		log.info("save BbsForum id={}", bean.getId());
		return list(request, model);
	}

	@RequiresPermissions("forum:o_update")
	@RequestMapping("/forum/o_update.do")
	public String update(BbsForum bean,BbsForumExt ext,
			Integer categoryId,  
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		// 加上模板前缀
		String tplPath = site.getTplPath();
		if (!StringUtils.isBlank(ext.getTplForum())) {
			ext.setTplForum(tplPath + ext.getTplForum());
		}
		if (!StringUtils.isBlank(ext.getTplTopic())) {
			ext.setTplTopic(tplPath + ext.getTplTopic());
		}
		if (!StringUtils.isBlank(ext.getTplMobileForum())) {
			ext.setTplMobileForum(tplPath + ext.getTplMobileForum());
		}
		if (!StringUtils.isBlank(ext.getTplMobileTopic())) {
			ext.setTplMobileTopic(tplPath + ext.getTplMobileTopic());
		}
		bean = manager.update(bean,ext,categoryId);
		log.info("update BbsForum id={}.", bean.getId());
		return list(request, model);
	}
	
	@RequiresPermissions("forum:batchUpdate")
	@RequestMapping("/forum/batchUpdate.do")
	public String batchUpdate(Integer[] categoryIds, Integer[] forumIds,
			String[]categoryTitle,String[]categoryPath,
			String[]bbsForumTitle,String[]bbsForumPath,
			HttpServletRequest request, ModelMap model) {
		boolean canUpdateCategory=true;
		canUpdateCategory=categoryIds!=null&&categoryIds.length>0&&categoryTitle!=null&&categoryPath!=null?true:false;
		if(canUpdateCategory){
			canUpdateCategory=categoryPath.length==categoryTitle.length&&categoryIds.length==categoryPath.length?true:false;
		}
		boolean canUpdateForum=true;
		canUpdateForum=forumIds!=null&&forumIds.length>0&&bbsForumTitle!=null&&bbsForumPath!=null?true:false;
		if(canUpdateForum){
			canUpdateForum=forumIds.length==bbsForumTitle.length&&bbsForumTitle.length==bbsForumPath.length?true:false;
		}
		if(canUpdateCategory){
			for(int i=0;i<categoryIds.length;i++){
				BbsCategory category=bbsCategoryMng.findById(categoryIds[i]);
				if(category!=null){
					category.setTitle(categoryTitle[i]);
					category.setPath(categoryPath[i]);
					bbsCategoryMng.update(category);
				}
			}
		}
		if(canUpdateForum){
			for(int i=0;i<forumIds.length;i++){
				BbsForum forum=manager.findById(forumIds[i]);
				if(forum!=null){
					forum.setTitle(bbsForumTitle[i]);
					forum.setPath(bbsForumPath[i]);
					manager.update(forum);
				}
			}
		}
		return list(request, model);
	}

	@RequiresPermissions("forum:o_delete")
	@RequestMapping("/forum/o_delete.do")
	public String delete(Integer[] ids, 
			HttpServletRequest request, ModelMap model) {
		for (int i = 0; i < ids.length; i++) {
			List<BbsTopic> list = bbsTopicMng.getList(ids[i],null,null,(short) 0,0,Integer.MAX_VALUE);
			for (int j = 0; j < list.size(); j++) {
				BbsTopic topic = bbsTopicMng.deleteById(list.get(j).getId());
				log.info("delete BbsTopic id={}", topic.getId());
			}
		}
		BbsForum[] beans = manager.deleteByIds(ids);
		for (BbsForum bean : beans) {
			log.info("delete BbsForum id={}", bean.getId());
		}
		
		return list(request, model);
	}

	@RequiresPermissions("forum:o_priority")
	@RequestMapping("/forum/o_priority.do")
	public String priorityUpdate(Integer[] ids, Integer[] prioritys,
			HttpServletRequest request, ModelMap model) {
		if (ids == null || ids.length <= 0) {
			return "redirect:v_list.do";
		}
		CmsSite site = CmsUtils.getSite(request);
		BbsForum t;
		Integer id;
		Integer priority;
		for (int i = 0; i < ids.length; i++) {
			id = ids[i];
			priority = prioritys[i];
			if (id != null && priority != null) {
				t = manager.findById(id);
				if (t != null && t.getSite().getId().equals(site.getId())) {
					t.setPriority(priority);
					manager.update(t);
				}
			}
		}
		return list(request, model);
	}
	
	@RequiresPermissions("forum:o_priority")
	@RequestMapping("/forum/o_m_priority.do")
	public void priorityMoveUpdate(String forumIds,
			HttpServletRequest request,	
			HttpServletResponse response,  ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsForum t;
		Integer id;
		if(StringUtils.isNotBlank(forumIds)){
			String[]fid=forumIds.split(",");
			for (int i = 0; i < fid.length; i++) {
				id = Integer.parseInt(fid[i]);
				if (id != null) {
					t = manager.findById(id);
					if (t != null && t.getSite().getId().equals(site.getId())) {
						t.setPriority(i);
						manager.update(t);
					}
				}
			}
			ResponseUtils.renderJson(response, "true");
		}
		ResponseUtils.renderJson(response, "false");
	}
	
	private List<String> getTplForum(HttpServletRequest request,CmsSite site,
			String tpl) {
		List<String> tplList = tplManager.getNameListByPrefix(BbsForum.getTplForumDef(site.getSolutionPath(),false,request));
		return CoreUtils.tplTrim(tplList, site.getTplPath(), tpl);
	}
	
	private List<String> getMobileTplForum(HttpServletRequest request,
			CmsSite site,String tpl) {
		List<String> tplList = tplManager.getNameListByPrefix(BbsForum.getTplForumDef(site.getMobileSolutionPath(),false,request));
		return CoreUtils.tplTrim(tplList, site.getTplPath(), tpl);
	}

	private List<String> getTplTopic(HttpServletRequest request,CmsSite site,
			String tpl) {
		List<String> tplList = tplManager.getNameListByPrefix(BbsForum.getTplTopicDef(site.getSolutionPath(),false,request));
		return CoreUtils.tplTrim(tplList, site.getTplPath(), tpl);
	}
	
	private List<String> getMobileTplTopic(HttpServletRequest request,
			CmsSite site,String tpl) {
		List<String> tplList = tplManager.getNameListByPrefix(BbsForum.getTplTopicDef(site.getMobileSolutionPath(),false,request));
		return CoreUtils.tplTrim(tplList, site.getTplPath(), tpl);
	}

	@Autowired
	private BbsForumMng manager;
	@Autowired
	private BbsCategoryMng bbsCategoryMng;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
	@Autowired
	private BbsTopicMng bbsTopicMng;
	@Autowired
	private TplManager tplManager;
}