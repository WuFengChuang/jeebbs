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
import com.jeecms.plug.live.entity.BbsLiveMessage;
import com.jeecms.plug.live.manager.BbsLiveMessageMng;

@Controller
public class BbsLiveMessageAct {
	private static final Logger log = LoggerFactory.getLogger(BbsLiveMessageAct.class);

	@RequiresPermissions("liveMsg:v_list")
	@RequestMapping("/liveMsg/v_list.do")
	public String list(Integer liveId,Integer pageNo, HttpServletRequest request, ModelMap model) {
		Pagination pagination = manager.getPage(liveId,cpn(pageNo), CookieUtils
				.getPageSize(request));
		model.addAttribute("pagination",pagination);
		return "plugPage/live/liveMsg/list";
	}
	
	@RequiresPermissions("liveMsg:v_edit")
	@RequestMapping("/liveMsg/v_edit.do")
	public String edit(Long id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("bbsLiveMessage", manager.findById(id));
		return "plugPage/live/liveMsg/edit";
	}

	@RequiresPermissions("liveMsg:o_update")
	@RequestMapping("/liveMsg/o_update.do")
	public String update(Integer liveId,BbsLiveMessage bean, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.update(bean);
		log.info("update BbsLiveMessage id={}.", bean.getId());
		return list(liveId,pageNo, request, model);
	}

	@RequiresPermissions("liveMsg:o_delete")
	@RequestMapping("/liveMsg/o_delete.do")
	public String delete(Long[] ids, Integer liveId,Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsLiveMessage[] beans = manager.deleteByIds(ids);
		for (BbsLiveMessage bean : beans) {
			log.info("delete BbsLiveMessage id={}", bean.getId());
		}
		return list(liveId,pageNo, request, model);
	}
	
	private WebErrors validateEdit(Long id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id,errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateUpdate(Long id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateDelete(Long[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids");
		for (Long id : ids) {
			vldExist(id, errors);
		}
		return errors;
	}

	private boolean vldExist(Long id,WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsLiveMessage entity = manager.findById(id);
		if(errors.ifNotExist(entity, BbsLiveMessage.class, id)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private BbsLiveMessageMng manager;
}