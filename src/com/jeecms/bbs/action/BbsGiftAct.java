package com.jeecms.bbs.action;

import static com.jeecms.common.page.SimplePage.cpn;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.web.WebErrors;
import com.jeecms.bbs.entity.BbsGift;
import com.jeecms.bbs.manager.BbsGiftMng;

@Controller
public class BbsGiftAct {
	private static final Logger log = LoggerFactory.getLogger(BbsGiftAct.class);

	@RequiresPermissions("gift:v_list")
	@RequestMapping("/gift/v_list.do")
	public String list(Integer pageNo, HttpServletRequest request, ModelMap model) {
		Pagination pagination = manager.getPage(cpn(pageNo), CookieUtils
				.getPageSize(request));
		model.addAttribute("pagination",pagination);
		return "gift/list";
	}

	@RequiresPermissions("gift:v_add")
	@RequestMapping("/gift/v_add.do")
	public String add(ModelMap model) {
		return "gift/add";
	}

	@RequiresPermissions("gift:v_edit")
	@RequestMapping("/gift/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("bbsGift", manager.findById(id));
		return "gift/edit";
	}

	@RequiresPermissions("gift:o_save")
	@RequestMapping("/gift/o_save.do")
	public String save(BbsGift bean, HttpServletRequest request, ModelMap model) {
		bean = manager.save(bean);
		log.info("save BbsGift id={}", bean.getId());
		return "redirect:v_list.do";
	}

	@RequiresPermissions("gift:o_update")
	@RequestMapping("/gift/o_update.do")
	public String update(BbsGift bean, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.update(bean);
		log.info("update BbsGift id={}.", bean.getId());
		return list(pageNo, request, model);
	}

	@RequiresPermissions("gift:o_delete")
	@RequestMapping("/gift/o_delete.do")
	public String delete(Integer[] ids, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsGift[] beans = manager.deleteByIds(ids);
		for (BbsGift bean : beans) {
			log.info("delete BbsGift id={}", bean.getId());
		}
		return list(pageNo, request, model);
	}
	
	private WebErrors validateEdit(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id,  errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id,  errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids");
		for (Integer id : ids) {
			vldExist(id,  errors);
		}
		return errors;
	}

	private boolean vldExist(Integer id,WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsGift entity = manager.findById(id);
		if(errors.ifNotExist(entity, BbsGift.class, id)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private BbsGiftMng manager;
}