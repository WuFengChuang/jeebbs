package com.jeecms.plug.live.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_PLUG;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.util.ChineseCharToEn;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;
import com.jeecms.plug.live.entity.BbsLiveChapter;
import com.jeecms.plug.live.manager.BbsLiveChapterMng;

/**
 * 活动live章节管理
 */
@Controller
public class BbsLiveChapterAct extends AbstractBbsLive {
	 private static final Logger log = LoggerFactory
	 .getLogger(BbsLiveChapterAct.class);

	public static final String TPL_LIVE_CHAPTER_MAIN = "tpl.liveChapterMain";
	public static final String TPL_LIVE_CHAPTER_LEFT = "tpl.liveChapterLeft";
	public static final String TPL_LIVE_CHAPTER_TREE = "tpl.liveChapterTree";
	public static final String TPL_LIVE_CHAPTER_LIST = "tpl.liveChapterList";
	public static final String TPL_LIVE_CHAPTER_EDIT = "tpl.liveChaptereEdit";
	public static final String TPL_LIVE_CHAPTER_ADD = "tpl.liveChapterAdd";
	public static final String TPL_LIVE_CHAPTER_LOAD = "tpl.liveChapterLoad";

	
	@RequestMapping("/live/chapter/load.jspx")
	public String load(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_CHAPTER_LOAD);
	}
	
	@RequestMapping("/live/chapter/main.jspx")
	public String chapterMain(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_CHAPTER_MAIN);
	}
	
	@RequestMapping("/live/chapter/v_left.jspx")
	public String chapterLeft(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_CHAPTER_LEFT);
	}

	@RequestMapping(value = "/live/chapter/v_tree.jspx")
	public String chapterTree(String root, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		log.debug("tree path={}", root);
		CmsSite site=CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		boolean isRoot;
		// jquery treeview的根请求为root=source
		if (StringUtils.isBlank(root) || "source".equals(root)) {
			isRoot = true;
		} else {
			isRoot = false;
		}
		model.addAttribute("isRoot", isRoot);
		WebErrors errors = validateTree(root, request);
		if (errors.hasErrors()) {
			log.error(errors.getErrors().get(0));
			ResponseUtils.renderJson(response, "[]");
			return null;
		}
		List<BbsLiveChapter> list;
		Integer userId = CmsUtils.getUserId(request);
		if (isRoot) {
			list = manager.getTopList(userId);
		} else {
			Integer rootId = Integer.valueOf(root);
			list = manager.getChildList(userId,rootId);
		}
		model.addAttribute("list", list);
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json;charset=UTF-8");
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_CHAPTER_TREE);
	}

	@RequestMapping(value = "/live/chapter/list.jspx")
	public String chapterList(Integer root, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		List<BbsLiveChapter> list;
		Integer userId = CmsUtils.getUserId(request);
		if (root==null) {
			list = manager.getTopList(userId);
		} else {
			Integer rootId = Integer.valueOf(root);
			list = manager.getChildList(userId,rootId);
		}
		model.addAttribute("root", root);
		model.addAttribute("list", list);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_CHAPTER_LIST);
	}
	
	@RequestMapping(value = "/live/chapter/add.jspx")
	public String chapterAdd(Integer root,
			HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		BbsLiveChapter parent = null;
		if (root != null) {
			parent = manager.findById(root);
			model.addAttribute("parent", parent);
			model.addAttribute("root", root);
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_CHAPTER_ADD);
	}
	
	@RequestMapping(value = "/live/chapter/edit.jspx")
	public String chapterEdit(Integer id, Integer root,
			HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		if (root != null) {
			model.addAttribute("root", root);
		}
		// 章节
		BbsLiveChapter chapter = manager.findById(id);
		// 章节列表
		List<BbsLiveChapter> topList = manager.getTopList(user.getId());
		List<BbsLiveChapter> chapterList = BbsLiveChapter.getListForSelect(topList,chapter);
		model.addAttribute("chapterList", chapterList);
		model.addAttribute("chapter", chapter);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_CHAPTER_EDIT);
	}
	
	@RequestMapping(value = "/live/chapter/save.jspx")
	public String chapterSave(Integer root, BbsLiveChapter bean,
			HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		bean.setUser(user);
		bean.init();
		bean = manager.save(bean, root);
		log.info("save BbsLiveChapter id={}, name={}", bean.getId(), bean.getName());
		return chapterList(root, request, response, model);
	}
	
	@RequestMapping(value = "/live/chapter/update.jspx")
	public String chapterUpdate(Integer root,Integer parentId, BbsLiveChapter bean,
			HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		bean = manager.update(bean, parentId);
		log.info("update BbsLiveChapter id={}, name={}", bean.getId(), bean.getName());
		return chapterList(root, request, response, model);
	}
	
	@RequestMapping("/live/chapter/delete.jspx")
	public String chapterDelete(Integer root, Integer[] ids,
			HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		WebErrors errors = validateDelete(ids, request);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		BbsLiveChapter[] beans = manager.deleteByIds(ids);
		for (BbsLiveChapter bean : beans) {
			log.info("delete BbsLiveChapter id={}", bean.getId());
		}
		return chapterList(root, request, response, model);
	}
	
	@RequestMapping("/live/chapter/priority.jspx")
	public String chapterPriority(Integer root, Integer[] wids, Integer[] priority,
			HttpServletResponse response,HttpServletRequest request, ModelMap model) {
		WebErrors errors = validatePriority(wids, priority, request);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		FrontUtils.frontData(request, model, site);
		manager.updatePriority(wids, priority);
		model.addAttribute("message", "global.success");
		return chapterList(root, request, response, model);
	}
	
	@RequestMapping("/live/chapter/v_create_path.jspx")
	public void createPath(String name,HttpServletRequest request, HttpServletResponse response) {
		String path;
		if (StringUtils.isBlank(name)) {
			path = "";
		} else {
			path=ChineseCharToEn.getAllFirstLetter(name);
		}
		ResponseUtils.renderJson(response, path);
	}
	
	@RequestMapping(value = "/live/chapter/v_check_path.jspx")
	public void checkPath(Integer cid,String path,HttpServletRequest request, HttpServletResponse response) {
		String pass;
		BbsUser user = CmsUtils.getUser(request);
		if (StringUtils.isBlank(path)||user==null) {
			pass = "false";
		} else {
			BbsLiveChapter c = manager.findByPath(user.getId(),path);
			if(c==null){
				pass="true" ;
			}else{
				if(c.getId().equals(cid)){
					pass= "true";
				}else{
					pass="false";
				}
			}
		}
		ResponseUtils.renderJson(response, pass);
	}
	
	
	
	private WebErrors validateTree(String path, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		// if (errors.ifBlank(path, "path", 255)) {
		// return errors;
		// }
		return errors;
	}
	
	private WebErrors validateEdit(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			return errors;
		}
		return errors;
	}
	
	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsLiveChapter entity = manager.findById(id);
		if (errors.ifNotExist(entity, BbsLiveChapter.class, id)) {
			return true;
		}
		return false;
	}
	
	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		errors.ifEmpty(ids, "ids");
		for (Integer id : ids) {
			if (vldExist(id, site.getId(), errors)) {
				return errors;
			}
			// 检查是否可以删除
			String code = manager.checkDelete(id);
			if (code != null) {
				errors.addErrorCode(code);
				return errors;
			}
		}
		return errors;
	}
	
	private WebErrors validatePriority(Integer[] wids, Integer[] priority,
			HttpServletRequest request) {
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = WebErrors.create(request);
		if (errors.ifEmpty(wids, "wids")) {
			return errors;
		}
		if (errors.ifEmpty(priority, "priority")) {
			return errors;
		}
		if (wids.length != priority.length) {
			errors.addErrorString("wids length not equals priority length");
			return errors;
		}
		for (int i = 0, len = wids.length; i < len; i++) {
			if (vldExist(wids[i], site.getId(), errors)) {
				return errors;
			}
			if (priority[i] == null) {
				priority[i] = 0;
			}
		}
		return errors;
	}

	@Autowired
	private BbsLiveChapterMng manager;
}
