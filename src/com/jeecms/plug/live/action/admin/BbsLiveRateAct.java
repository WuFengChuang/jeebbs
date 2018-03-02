package com.jeecms.plug.live.action.admin;

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
import com.jeecms.plug.live.entity.BbsLiveRate;
import com.jeecms.plug.live.manager.BbsLiveRateMng;

@Controller
public class BbsLiveRateAct {
	private static final Logger log = LoggerFactory.getLogger(BbsLiveRateAct.class);

	@RequiresPermissions("liveRate:v_list")
	@RequestMapping("/liveRate/v_list.do")
	public String list(Integer pageNo, HttpServletRequest request, ModelMap model) {
		Pagination pagination = manager.getPage(cpn(pageNo), CookieUtils
				.getPageSize(request));
		model.addAttribute("pagination",pagination);
		return "plugPage/live/liveRate/list";
	}

	@RequiresPermissions("liveRate:v_add")
	@RequestMapping("/liveRate/v_add.do")
	public String add(ModelMap model) {
		return "plugPage/live/liveRate/add";
	}

	@RequiresPermissions("liveRate:v_edit")
	@RequestMapping("/liveRate/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("bbsLiveRate", manager.findById(id));
		return "plugPage/live/liveRate/edit";
	}

	@RequiresPermissions("liveRate:o_save")
	@RequestMapping("/liveRate/o_save.do")
	public String save(BbsLiveRate bean, HttpServletRequest request, ModelMap model) {
		bean = manager.save(bean);
		log.info("save BbsLiveRate id={}", bean.getId());
		return "redirect:v_list.do";
	}

	@RequiresPermissions("liveRate:o_update")
	@RequestMapping("/liveRate/o_update.do")
	public String update(BbsLiveRate bean, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.update(bean);
		log.info("update BbsLiveRate id={}.", bean.getId());
		return list(pageNo, request, model);
	}

	@RequiresPermissions("liveRate:o_delete")
	@RequestMapping("/liveRate/o_delete.do")
	public String delete(Integer[] ids, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsLiveRate[] beans = manager.deleteByIds(ids);
		for (BbsLiveRate bean : beans) {
			log.info("delete BbsLiveRate id={}", bean.getId());
		}
		return list(pageNo, request, model);
	}
	
	private WebErrors validateEdit(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id,errors)) {
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
		BbsLiveRate entity = manager.findById(id);
		if(errors.ifNotExist(entity, BbsLiveRate.class, id)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private BbsLiveRateMng manager;
}