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
import com.jeecms.plug.live.entity.BbsLiveChapter;
import com.jeecms.plug.live.manager.BbsLiveChapterMng;

@Controller
public class BbsLiveChapterAct {
	private static final Logger log = LoggerFactory.getLogger(BbsLiveChapterAct.class);

	@RequiresPermissions("liveChapter:v_list")
	@RequestMapping("/liveChapter/v_list.do")
	public String list(Integer pageNo, HttpServletRequest request, ModelMap model) {
		Pagination pagination = manager.getPage(cpn(pageNo), CookieUtils
				.getPageSize(request));
		model.addAttribute("pagination",pagination);
		return "plugPage/live/liveChapter/list";
	}
	
	@RequiresPermissions("liveChapter:o_delete")
	@RequestMapping("/liveChapter/o_delete.do")
	public String delete(Integer[] ids, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsLiveChapter[] beans = manager.deleteByIds(ids);
		for (BbsLiveChapter bean : beans) {
			log.info("delete BbsLiveChapter id={}", bean.getId());
		}
		return list(pageNo, request, model);
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
		BbsLiveChapter entity = manager.findById(id);
		if(errors.ifNotExist(entity, BbsLiveChapter.class, id)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private BbsLiveChapterMng manager;
}