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

import com.jeecms.core.web.WebErrors;
import com.jeecms.bbs.entity.BbsTopicType;
import com.jeecms.bbs.manager.BbsTopicTypeMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.util.ChineseCharToEn;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class BbsTopicTypeAct {
	private static final Logger log = LoggerFactory.getLogger(BbsTopicTypeAct.class);

	@RequiresPermissions("topicType:main")
	@RequestMapping("/topicType/main.do")
	public String topicTypeMain(ModelMap model) {
		return "topicType/main";
	}
	
	@RequiresPermissions("topicType:v_left")
	@RequestMapping("/topicType/v_left.do")
	public String left() {
		return "topicType/left";
	}

	@RequiresPermissions("topicType:v_tree")
	@RequestMapping(value = "/topicType/v_tree.do")
	public String tree(String root, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		log.debug("tree path={}", root);
		boolean isRoot;
		// jquery treeview的根请求为root=source
		if (StringUtils.isBlank(root) || "source".equals(root)) {
			isRoot = true;
		} else {
			isRoot = false;
		}
		model.addAttribute("isRoot", isRoot);
		List<BbsTopicType> list;
		if (isRoot) {
			list = manager.getTopList(false, true,BbsTopicType.ORDER_PRIORITY_DESC,null,null);
		} else {
			Integer rootId = Integer.valueOf(root);
			list = manager.getChildList(rootId, false,true,BbsTopicType.ORDER_PRIORITY_DESC,null,null);
		}
		model.addAttribute("list", list);
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json;charset=UTF-8");
		return "topicType/tree";
	}
	
	@RequiresPermissions("topicType:v_list")
	@RequestMapping("/topicType/v_list.do")
	public String list(Integer root, HttpServletRequest request, ModelMap model) {
		List<BbsTopicType> list;
		if (root == null) {
			list = manager.getTopList(false,false,BbsTopicType.ORDER_PRIORITY_DESC,null,null);
		} else {
			list = manager.getChildList(root, false,false,BbsTopicType.ORDER_PRIORITY_DESC,null,null);
		}
		model.addAttribute("list", list);
		model.addAttribute("root", root);
		return "topicType/list";
	}

	@RequiresPermissions("topicType:v_add")
	@RequestMapping("/topicType/v_add.do")
	public String add(Integer root,ModelMap model) {
		BbsTopicType parent = null;
		if (root != null) {
			parent = manager.findById(root);
			model.addAttribute("parent", parent);
			model.addAttribute("root", root);
		}
		return "topicType/add";
	}

	@RequiresPermissions("topicType:v_edit")
	@RequestMapping("/topicType/v_edit.do")
	public String edit(Integer id, Integer root, 
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		if (root != null) {
			model.addAttribute("root", root);
		}
		BbsTopicType type = manager.findById(id);
		List<BbsTopicType> topList = manager.getTopList(false, true,BbsTopicType.ORDER_PRIORITY_DESC,null,null);
		List<BbsTopicType> typeList = BbsTopicType.getListForSelect(
				topList,type);
		model.addAttribute("bbsTopicType", type);
		model.addAttribute("typeList", typeList);
		return "topicType/edit";
	}

	@RequiresPermissions("topicType:o_save")
	@RequestMapping("/topicType/o_save.do")
	public String save(BbsTopicType bean, Integer root,Integer parentId,
			HttpServletRequest request, ModelMap model) {
		if(parentId!=null){
			bean.setParent(manager.findById(root));
		}
		bean.init();
		bean.setSite(CmsUtils.getSite(request));
		bean = manager.save(bean);
		log.info("save BbsTopicType id={}", bean.getId());
		return list(root, request, model);
	}

	@RequiresPermissions("topicType:o_update")
	@RequestMapping("/topicType/o_update.do")
	public String update(BbsTopicType bean, Integer root,Integer parentId, 
			HttpServletRequest request,ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.update(bean,parentId);
		log.info("update BbsTopicType id={}.", bean.getId());
		return list(root, request, model);
	}

	@RequiresPermissions("topicType:o_delete")
	@RequestMapping("/topicType/o_delete.do")
	public String delete(Integer[] ids, Integer root, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsTopicType[] beans = manager.deleteByIds(ids);
		for (BbsTopicType bean : beans) {
			log.info("delete BbsTopicType id={}", bean.getId());
		}
		return list(root, request, model);
	}
	
	@RequiresPermissions("topicType:v_create_path")
	@RequestMapping(value = "/topicType/v_create_path.do")
	public void createPath(String name,HttpServletRequest request, HttpServletResponse response) {
		String path;
		if (StringUtils.isBlank(name)) {
			path = "";
		} else {
			path=ChineseCharToEn.getAllFirstLetter(name);
		}
		ResponseUtils.renderJson(response, path);
	}
	
	@RequiresPermissions("topicType:v_create_path")
	@RequestMapping(value = "/topicType/v_check_path.do")
	public void checkPath(Integer id,String path,HttpServletRequest request, HttpServletResponse response) {
		String pass;
		if (StringUtils.isBlank(path)) {
			pass = "false";
		} else {
			BbsTopicType c = manager.findByPath(path);
			if(c==null){
				pass="true" ;
			}else{
				if(c.getId().equals(id)){
					pass= "true";
				}else{
					pass="false";
				}
			}
		}
		ResponseUtils.renderJson(response, pass);
	}
	
	private WebErrors validateEdit(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids");
		for (Integer id : ids) {
			vldExist(id, errors);
		}
		return errors;
	}

	private boolean vldExist(Integer id,WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsTopicType entity = manager.findById(id);
		if(errors.ifNotExist(entity, BbsTopicType.class, id)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private BbsTopicTypeMng manager;
}