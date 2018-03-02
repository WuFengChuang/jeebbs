package com.jeecms.bbs.action;

import static com.jeecms.common.page.SimplePage.cpn;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.entity.BbsWebservice;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.manager.BbsWebserviceMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsRole;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsRoleMng;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.security.BbsAuthorizingRealm;
import com.jeecms.core.web.WebErrors;

/**
 * 管理员ACTION
 */
@Controller
public class BbsAdminAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsAdminAct.class);

	@RequiresPermissions("admin:v_list")
	@RequestMapping("/admin/v_list.do")
	public String list(String queryUsername, String queryEmail,
			Integer queryGroupId, Boolean queryDisabled,
			Integer orderBy, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser currUser = CmsUtils.getUser(request);
		Pagination pagination = manager.getPage(queryUsername, queryEmail, 
				queryGroupId, queryDisabled, true, null, 
				null, currUser.getRank(),orderBy, 
				cpn(pageNo), CookieUtils.getPageSize(request));
		List<CmsRole> roleList = cmsRoleMng.getList(currUser.getTopRoleLevel());
		model.addAttribute("roleList", roleList);
		model.addAttribute("pagination", pagination);
		appendQueryParam(model, queryUsername, queryEmail, queryGroupId, queryDisabled);
		model.addAttribute("groupList", bbsUserGroupMng.getList(site.getId()));
		return "admin/list";
	}

	@RequiresPermissions("admin:v_add")
	@RequestMapping("/admin/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser currUser = CmsUtils.getUser(request);
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(site.getId());
		List<CmsRole> roleList = cmsRoleMng.getList(currUser.getTopRoleLevel());
		model.addAttribute("site", site);
		model.addAttribute("groupList", groupList);
		model.addAttribute("roleList", roleList);
		model.addAttribute("currRank", currUser.getRank());
		return "admin/add";
	}

	@RequiresPermissions("admin:v_edit")
	@RequestMapping("/admin/v_edit.do")
	public String edit(Integer id, String queryUsername, String queryEmail,
			Integer queryGroupId, Boolean queryDisabled, 
			Integer orderBy,HttpServletRequest request,
			HttpServletResponse  response,ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser currUser = CmsUtils.getUser(request);
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsUser admin = manager.findById(id);
		
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(site.getId());
		List<CmsRole> roleList = cmsRoleMng.getList(currUser.getTopRoleLevel());

		model.addAttribute("cmsAdmin", admin);
		model.addAttribute("site", site);
		model.addAttribute("roleIds", admin.getRoleIds());
		model.addAttribute("groupList", groupList);
		model.addAttribute("roleList", roleList);
		model.addAttribute("currRank", currUser.getRank());

		appendQueryParam(model, queryUsername, queryEmail, queryGroupId,queryDisabled);
		return "admin/edit";
	}

	@RequiresPermissions("admin:o_save")
	@RequestMapping("/admin/o_save.do")
	public String save(BbsUser bean, BbsUserExt ext, String username,
			String email, String password,Integer rank, 
			Integer groupId,Integer[] roleIds,HttpServletRequest request,ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		String ip = RequestUtils.getIpAddr(request);
		try {
			bean = manager.saveAdmin(username, email, password, ip, rank,
					groupId, roleIds, ext);
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage());
		} 
		callWebService(username, password, email, ext,BbsWebservice.SERVICE_TYPE_ADD_USER);
		log.info("save CmsAdmin id={}", bean.getId());
		return "redirect:v_list.do";
	}

	@RequiresPermissions("admin:o_update")
	@RequestMapping("/admin/o_update.do")
	public String update(BbsUser bean, BbsUserExt ext, String password,
			Integer groupId, Integer[] roleIds,String queryUsername, String queryEmail,
			Integer queryGroupId, Boolean queryDisabled, 
			Integer orderBy, Integer pageNo, HttpServletRequest request,ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(),bean.getRank(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.updateAdmin(bean, ext, password, groupId,roleIds);
		callWebService(bean.getUsername(), password, bean.getEmail(), ext,BbsWebservice.SERVICE_TYPE_UPDATE_USER);
		log.info("update CmsAdmin id={}.", bean.getId());
		return list(queryUsername, queryEmail, queryGroupId, queryDisabled,
				orderBy, pageNo, request, model);
	}

	@RequiresPermissions("admin:o_delete")
	@RequestMapping("/admin/o_delete.do")
	public String delete(Integer[] ids, String queryUsername, String queryEmail,
			Integer queryGroupId, Boolean queryDisabled, 
			Integer orderBy,Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsUser[] beans = manager.deleteByIds(ids);
		BbsUser user =CmsUtils.getUser(request);
		boolean deleteCurrentUser=false;
		for (BbsUser bean : beans) {
			Map<String,String>paramsValues=new HashMap<String, String>();
			paramsValues.put("username", bean.getUsername());
			paramsValues.put("admin", "true");
			bbsWebserviceMng.callWebService(BbsWebservice.SERVICE_TYPE_DELETE_USER, paramsValues);
			log.info("delete CmsAdmin id={}", bean.getId());
			if(user.getUsername().equals(bean.getUsername())){
				deleteCurrentUser=true;
			}
		}
		if(deleteCurrentUser){
			 Subject subject = SecurityUtils.getSubject();
			 subject.logout();
			 return "login";
		}
		return list(queryUsername, queryEmail, queryGroupId, queryDisabled,
				orderBy, pageNo, request, model);
	}

	@RequiresPermissions("admin:v_check_username")
	@RequestMapping(value = "/admin/v_check_username.do")
	public void checkUsername(HttpServletRequest request, HttpServletResponse response) {
		checkUserJson(request, response);
	}

	@RequiresPermissions("admin:v_check_email")
	@RequestMapping(value = "/admin/v_check_email.do")
	public void checkEmail(String email, HttpServletResponse response) {
		checkEmailJson(email, response);
	}
	
	private void callWebService(String username,String password,String email,BbsUserExt userExt,String operate){
		if(bbsWebserviceMng.hasWebservice(operate)){
			Map<String,String>paramsValues=new HashMap<String, String>();
			paramsValues.put("username", username);
			paramsValues.put("password", password);
			if(StringUtils.isNotBlank(email)){
				paramsValues.put("email", email);
			}
			if(StringUtils.isNotBlank(userExt.getRealname())){
				paramsValues.put("realname", userExt.getRealname());
			}
			if(userExt.getGender()!=null){
				paramsValues.put("sex", userExt.getGender().toString());
			}
			if(StringUtils.isNotBlank(userExt.getMoble())){
				paramsValues.put("tel",userExt.getMoble());
			}
			bbsWebserviceMng.callWebService(operate, paramsValues);
		}
	}
	
	private void appendQueryParam(ModelMap model,
			String queryUsername, String queryEmail,
			Integer queryGroupId, Boolean queryDisabled){
		model.addAttribute("queryUsername", queryUsername);
		model.addAttribute("queryEmail", queryEmail);
		model.addAttribute("queryGroupId", queryGroupId);
		model.addAttribute("queryDisabled", queryDisabled);
	}
	
	protected void checkUserJson(HttpServletRequest request,HttpServletResponse response) {
		String username=RequestUtils.getQueryParam(request,"username");
		String pass;
		if (StringUtils.isBlank(username)) {
			pass = "false";
		} else {
			pass = manager.usernameNotExist(username) ? "true" : "false";
		}
		ResponseUtils.renderJson(response, pass);
	}

	protected void checkEmailJson(String email, HttpServletResponse response) {
		String pass;
		if (StringUtils.isBlank(email)) {
			pass = "false";
		} else {
			pass = manager.emailNotExist(email) ? "true" : "false";
		}
		ResponseUtils.renderJson(response, pass);
	}

	private WebErrors validateSave(BbsUser bean, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		return errors;
	}

	private WebErrors validateEdit(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateUpdate(Integer id,Integer rank, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			return errors;
		}
		if (vldParams(id,rank, request, errors)) {
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

	private boolean vldExist(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsUser entity = manager.findById(id);
		if (errors.ifNotExist(entity, BbsUser.class, id)) {
			return true;
		}
		return false;
	}

	private boolean vldParams(Integer id,Integer rank, HttpServletRequest request,
			WebErrors errors) {
		BbsUser user = CmsUtils.getUser(request);
		BbsUser entity = manager.findById(id);
		//提升等级大于当前登录用户
		if (rank > user.getRank()) {
			errors.addErrorCode("error.noPermissionToRaiseRank", id);
			return true;
		}
		//修改的用户等级大于当前登录用户 无权限
		if (entity.getRank() > user.getRank()) {
			errors.addErrorCode("error.noPermission", BbsUser.class, id);
			return true;
		}
		return false;
	}
	
	@Autowired
	protected CmsSiteMng cmsSiteMng;
	@Autowired
	protected CmsRoleMng cmsRoleMng;
	@Autowired
	protected BbsUserMng manager;
	@Autowired
	protected BbsWebserviceMng bbsWebserviceMng;
	@Autowired
	protected BbsAuthorizingRealm authorizingRealm;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
}