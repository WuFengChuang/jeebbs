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
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.web.WebErrors;
import com.jeecms.plug.live.entity.BbsLiveApply;
import com.jeecms.plug.live.manager.BbsLiveApplyMng;

@Controller
public class BbsLiveApplyAct {
	private static final Logger log = LoggerFactory.getLogger(BbsLiveApplyAct.class);

	@RequiresPermissions("liveApply:v_list")
	@RequestMapping("/liveApply/v_list.do")
	public String list(String mobile,Short status,Integer queryMode,
			String username,Integer pageNo, 
			HttpServletRequest request, ModelMap model) {
		Integer applyUserId=null;
		if(StringUtils.isNotBlank(username)){
			BbsUser user=userMng.findByUsername(username);
			if(user!=null){
				applyUserId=user.getId();
			}else{
				applyUserId=0;
			}
		}
		if(status==null){
			status=BbsLiveApply.STATUS_CHECKING;
		}
		if(queryMode==null){
			queryMode=0;
		}
		if(queryMode==1){
			return "redirect:../liveHost/v_list.do";
		}
		Pagination pagination = manager.getPage(mobile,status,applyUserId,
				cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination",pagination);
		model.addAttribute("mobile",mobile);
		model.addAttribute("status",status);
		model.addAttribute("username",username);
		return "plugPage/live/liveApply/list";
	}
	
	@RequiresPermissions("liveApply:v_edit")
	@RequestMapping("/liveApply/v_edit.do")
	public String edit(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("bbsLiveApply", manager.findById(id));
		return "plugPage/live/liveApply/edit";
	}
	
	@RequiresPermissions("liveApply:v_view")
	@RequestMapping("/liveApply/v_view.do")
	public String view(Integer id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("bbsLiveApply", manager.findById(id));
		return "plugPage/live/liveApply/view";
	}

	@RequiresPermissions("liveApply:o_update")
	@RequestMapping("/liveApply/o_update.do")
	public String update(String mobile,Short status,
			String username,BbsLiveApply bean,String[] picPaths, String[] picDescs,
			Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.update(bean,picPaths,picDescs);
		log.info("update BbsLiveApply id={}.", bean.getId());
		return list(mobile,status,null,username,pageNo, request, model);
	}
	
	@RequiresPermissions("liveApply:v_check")
	@RequestMapping("/liveApply/v_check.do")
	public String check(Integer[] ids,String mobile,Short status,
			String username, Integer pageNo, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		manager.check(ids, CmsUtils.getUser(request));
		return list(mobile, status,null, username, pageNo, request, model);
	}
	
	@RequiresPermissions("liveApply:v_reject")
	@RequestMapping("/liveApply/v_reject.do")
	public String reject(Integer[] ids,String mobile,Short status,
			String username, Integer pageNo, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		manager.cancel(ids, CmsUtils.getUser(request));
		return list(mobile, status,null, username, pageNo, request, model);
	}

	@RequiresPermissions("liveApply:o_delete")
	@RequestMapping("/liveApply/o_delete.do")
	public String delete(String mobile,Short status,
			String username,Integer[] ids, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsLiveApply[] beans = manager.deleteByIds(ids);
		for (BbsLiveApply bean : beans) {
			log.info("delete BbsLiveApply id={}", bean.getId());
		}
		return list(mobile,status,null,username,pageNo, request, model);
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
		BbsLiveApply entity = manager.findById(id);
		if(errors.ifNotExist(entity, BbsLiveApply.class, id)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private BbsLiveApplyMng manager;
	@Autowired
	private BbsUserMng userMng;
}