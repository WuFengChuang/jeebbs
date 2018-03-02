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

import com.jeecms.bbs.entity.ApiUserLogin;
import com.jeecms.bbs.manager.ApiUserLoginMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;

@Controller
public class ApiUserLoginAct {
	private static final Logger log = LoggerFactory.getLogger(ApiUserLoginAct.class);

	@RequiresPermissions("apiUserLogin:v_list")
	@RequestMapping("/apiUserLogin/v_list.do")
	public String list(Integer pageNo, HttpServletRequest request, ModelMap model) {
		Pagination pagination = manager.getPage(cpn(pageNo), CookieUtils
				.getPageSize(request));
		model.addAttribute("pagination",pagination);
		model.addAttribute("pageNo",pagination.getPageNo());
		return "apiUserLogin/list";
	}

	@RequiresPermissions("apiUserLogin:o_delete")
	@RequestMapping("/apiUserLogin/o_delete.do")
	public String delete(Long[] ids, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		ApiUserLogin[] beans = manager.deleteByIds(ids);
		for (ApiUserLogin bean : beans) {
			log.info("delete ApiUserLogin id={}", bean.getId());
		}
		return list(pageNo, request, model);
	}
	
	private WebErrors validateDelete(Long[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifEmpty(ids, "ids")) {
			return errors;
		}
		for (Long id : ids) {
			vldExist(id,  errors);
		}
		return errors;
	}

	private boolean vldExist(Long id, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		ApiUserLogin entity = manager.findById(id);
		if(errors.ifNotExist(entity, ApiUserLogin.class, id)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private ApiUserLoginMng manager;
}