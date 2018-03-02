package com.jeecms.bbs.action;

import static com.jeecms.common.page.SimplePage.cpn;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
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
import com.jeecms.core.entity.CmsConfigItem;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsConfigItemMng;
@Controller
public class BbsUserAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsUserGroupAct.class);

	@RequiresPermissions("user:v_list")
	@RequestMapping("/user/v_list.do")
	public String list(String username, Integer groupId,Integer lastLoginDay,
			Integer orderBy, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		BbsUser user=CmsUtils.getUser(request);
		Pagination pagination = manager.getPage(username, null, groupId, null,
				false,false,lastLoginDay, user.getRank(),orderBy,
				cpn(pageNo), CookieUtils.getPageSize(request));
		CmsSite site = CmsUtils.getSite(request);
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(site.getId());
		model.addAttribute("groupList", groupList);
		model.addAttribute("pagination", pagination);
		model.addAttribute("username", username);
		model.addAttribute("groupId", groupId);
		model.addAttribute("orderBy", orderBy);
		model.addAttribute("pageNo", pagination.getPageNo());
		return "user/list";
	}
	
	@RequiresPermissions("user:v_officialuser_list")
	@RequestMapping("/user/v_officialuser_list.do")
	public String listOfficialUserList(Integer pageNo, HttpServletRequest request,ModelMap model) {
		BbsUser user=CmsUtils.getUser(request);
		Pagination pagination = manager.getPage(null, null, null, null,
				false,true,null,  user.getRank(),null,cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageNo", pagination.getPageNo());
		return "user/listOfficialUser";
	}

	@RequiresPermissions("user:o_onlinestatistics")
	@RequestMapping("/user/o_onlinestatistics.do")
	public String onlinestatistics(Integer[] ids, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		BbsUser[] beans = manager.deleteByIds(ids);
		for (BbsUser bean : beans) {
			log.info("delete BbsUser id={}", bean.getId());
		}
		return "redirect:v_list.do";
	}

	@RequiresPermissions("user:v_add")
	@RequestMapping("/user/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(site.getId());
		List<CmsConfigItem>registerItems=cmsConfigItemMng.getList(site.getConfig().getId(), CmsConfigItem.CATEGORY_REGISTER);
		model.addAttribute("registerItems", registerItems);
		model.addAttribute("groupList", groupList);
		return "user/add";
	}

	@RequiresPermissions("user:v_edit")
	@RequestMapping("/user/v_edit.do")
	public String edit(Integer userId, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = manager.findById(userId);
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(site.getId());
		List<CmsConfigItem>registerItems=cmsConfigItemMng.getList(site.getConfig().getId(), CmsConfigItem.CATEGORY_REGISTER);
		List<String>userAttrValues=new ArrayList<String>();
		for(CmsConfigItem item:registerItems){
			userAttrValues.add(user.getAttr().get(item.getField()));
		}
		model.addAttribute("groupList", groupList);
		model.addAttribute("cmsMember",user);
		model.addAttribute("registerItems", registerItems);
		model.addAttribute("userAttrValues", userAttrValues);
		return "user/edit";
	}
	
	@RequiresPermissions("user:v_officialuser_add")
	@RequestMapping("/user/v_officialuser_add.do")
	public String addOfficialUser(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(site.getId());
		model.addAttribute("groupList", groupList);
		return "user/addOfficialUser";
	}

	@RequiresPermissions("user:v_officialuser_edit")
	@RequestMapping("/user/v_officialuser_edit.do")
	public String editOfficialUser(Integer userId, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = manager.findById(userId);
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(site.getId());
		model.addAttribute("groupList", groupList);
		model.addAttribute("cmsMember",user);
		return "user/editOfficialUser";
	}
	
	@RequiresPermissions("user:v_check_username")
	@RequestMapping(value = "/user/v_check_username.do")
	public void checkUsername(String username, HttpServletResponse response) {
		String pass;
		if (StringUtils.isBlank(username)) {
			pass = "false";
		} else {
			pass = manager.usernameNotExist(username) ? "true" : "false";
		}
		ResponseUtils.renderJson(response, pass);
	}
	
	@RequiresPermissions("user:v_find_username")
	@RequestMapping(value = "/user/v_find_username.do")
	public void findUsername(String username, HttpServletResponse response) {
		String pass;
		if (StringUtils.isBlank(username)) {
			pass = "false";
		} else {
			pass = manager.usernameNotExist(username) ? "false" : "true";
		}
		ResponseUtils.renderJson(response, pass);
	}


	@RequiresPermissions("user:o_save")
	@RequestMapping("/user/o_save.do")
	public String save(BbsUser bean, BbsUserExt ext, String username,Boolean official,
			String email, String password, Integer groupId,
			HttpServletRequest request, ModelMap model) throws UnsupportedEncodingException, MessagingException {
		String ip = RequestUtils.getIpAddr(request);
		Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
		bean = manager.registerMember(username, email,official, password, ip, groupId,ext,attrs);
		callWebService(username, password, email, ext,BbsWebservice.SERVICE_TYPE_ADD_USER);
		if(official!=null&&official){
			return "redirect:v_officialuser_list.do";
		}else{
			return "redirect:v_list.do";
		}
	}

	@RequiresPermissions("user:o_update")
	@RequestMapping("/user/o_update.do")
	public String update(Integer id, String email, String password,
			Boolean disabled, BbsUserExt ext, Integer groupId,
			HttpServletRequest request, ModelMap model) {
		
		Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
		BbsUser bean=manager.updateMember(id, email, password, disabled, null, null, ext,attrs,groupId);
		callWebService(bean.getUsername(), password, email, ext,BbsWebservice.SERVICE_TYPE_UPDATE_USER);
		if(bean.getOfficial()!=null&&bean.getOfficial()){
			return "redirect:v_officialuser_list.do";
		}else{
			return "redirect:v_list.do";
		}
	}
	
	@RequiresPermissions("user:o_delete")
	@RequestMapping("/user/o_delete.do")
	public String delete(Integer[] ids, Boolean official,Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		for (Integer id : ids) {
			Map<String,String>paramsValues=new HashMap<String, String>();
			BbsUser bean=manager.findById(id);
			if(bean!=null){
				paramsValues.put("username", bean.getUsername());
				bbsWebserviceMng.callWebService(BbsWebservice.SERVICE_TYPE_DELETE_USER, paramsValues);
				log.info("delete BbsUser id={}", bean.getId());
			}
		}
		manager.deleteByIds(ids);
		if(official!=null&&official){
			return "redirect:v_officialuser_list.do";
		}else{
			return "redirect:v_list.do";
		}
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

	@Autowired
	private BbsWebserviceMng bbsWebserviceMng;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
	@Autowired
	private BbsUserMng manager;
	@Autowired
	private CmsConfigItemMng cmsConfigItemMng;
}
