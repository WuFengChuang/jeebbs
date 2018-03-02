package com.jeecms.plug.live.action.admin;

import static com.jeecms.common.page.SimplePage.cpn;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.web.WebErrors;
import com.jeecms.plug.live.entity.BbsLiveApply;
import com.jeecms.plug.live.entity.BbsLiveUserAccount;
import com.jeecms.plug.live.manager.BbsLiveUserAccountMng;

/**
 * @author tom
 * @date 2017年7月21日
 */
@Controller
public class BbsLiveHostAct {
	private static final Logger log = LoggerFactory.getLogger(BbsLiveHostAct.class);

	@RequiresPermissions("liveHost:v_list")
	@RequestMapping("/liveHost/v_list.do")
	public String list(String username, Integer orderBy,Integer queryMode,
			Integer pageNo, HttpServletRequest request, ModelMap model) {
		Integer queryUserId = null;
		if (StringUtils.isNotBlank(username)) {
			BbsUser user = userMng.findByUsername(username);
			if (user != null) {
				queryUserId = user.getId();
			} else {
				queryUserId = 0;
			}
		}
		if(queryMode==null){
			queryMode=1;
		}
		if(queryMode==0){
			return "redirect:../liveApply/v_list.do";
		}
		Pagination pagination = manager.getPage(queryUserId, orderBy, cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("username", username);
		model.addAttribute("orderBy", orderBy);
		model.addAttribute("pageNo",  cpn(pageNo));
		return "plugPage/live/liveHost/list";
	}
	
	@RequiresPermissions("liveHost:o_delete")
	@RequestMapping("/liveHost/o_delete.do")
	public String delete(Integer[] ids,String username,
			Integer orderBy,Integer pageNo, 
			HttpServletRequest request, ModelMap model) {
		for(Integer id:ids){
			manager.deleteById(id);
		}
		return list(username, orderBy, null, pageNo, request, model);
	}

	@RequiresPermissions("liveHost:o_priority")
	@RequestMapping("/liveHost/o_priority.do")
	public String priority(String username, Integer orderBy,
			Integer[] wids, Integer[] priority,
			Integer pageNo, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validatePriority(wids, priority, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		manager.updatePriority(wids, priority);
		model.addAttribute("message", "global.success");
		return list(username, orderBy,null, pageNo, request, model);
	}

	private WebErrors validatePriority(Integer[] wids, Integer[] priority, HttpServletRequest request) {
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
			if (vldExist(wids[i], errors)) {
				return errors;
			}
			if (priority[i] == null) {
				priority[i] = 0;
			}
		}
		return errors;
	}

	private boolean vldExist(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsLiveUserAccount entity = manager.findById(id);
		if (errors.ifNotExist(entity, BbsLiveApply.class, id)) {
			return true;
		}
		return false;
	}

	@Autowired
	private BbsLiveUserAccountMng manager;
	@Autowired
	private BbsUserMng userMng;
}