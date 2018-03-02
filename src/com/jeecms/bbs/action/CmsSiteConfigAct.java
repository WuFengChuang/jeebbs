package com.jeecms.bbs.action;

import static com.jeecms.common.web.Constants.MESSAGE;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsConfig;
import com.jeecms.bbs.entity.BbsCreditExchange;
import com.jeecms.bbs.entity.BbsUserActiveLevel;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsCreditExchangeMng;
import com.jeecms.bbs.manager.BbsUserActiveLevelMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.core.entity.BbsConfigAttr;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsConfigItem;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.entity.Config.ConfigEmailSender;
import com.jeecms.core.entity.Config.ConfigLogin;
import com.jeecms.core.entity.Config.ConfigMessageTemplate;
import com.jeecms.core.manager.CmsConfigItemMng;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.manager.ConfigMng;
import com.jeecms.core.manager.FtpMng;

@Controller
public class CmsSiteConfigAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsSiteConfigAct.class);

	@RequiresPermissions("site_config:v_system_edit")
	@RequestMapping("/site_config/v_system_edit.do")
	public String systemEdit(HttpServletRequest request, ModelMap model) {
		model.addAttribute("cmsConfig", cmsConfigMng.get());
		return "site_config/system_edit";
	}

	@RequiresPermissions("site_config:o_system_update")
	@RequestMapping("/site_config/o_system_update.do")
	public String systemUpdate(CmsConfig bean, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSystemUpdate(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = cmsConfigMng.update(bean);
		model.addAttribute("message", "global.success");
		log.info("update systemConfig of CmsConfig.");
		return systemEdit(request, model);
	}

	@RequiresPermissions("site_config:v_base_edit")
	@RequestMapping("/site_config/v_base_edit.do")
	public String baseEdit(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		List<Ftp> ftpList = ftpMng.getList();
		model.addAttribute("ftpList", ftpList);
		model.addAttribute("cmsSite", site);
		return "site_config/base_edit";
	}

	@RequiresPermissions("site_config:o_base_update")
	@RequestMapping("/site_config/o_base_update.do")
	public String baseUpdate(CmsSite bean, Integer uploadFtpId,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateBaseUpdate(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		CmsSite site = CmsUtils.getSite(request);
		bean.setId(site.getId());
		bean = manager.update(bean, uploadFtpId);
		model.addAttribute("message", "global.success");
		log.info("update CmsSite success. id={}", site.getId());
		return baseEdit(request, model);
	}

	@RequiresPermissions("bbs_config:v_edit")
	@RequestMapping("/bbs_config/v_edit.do")
	public String bbsEdit(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsConfig config = bbsConfigMng.findById(site.getId());
		CmsConfig cmsConfig=cmsConfigMng.get();
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(site.getId());
		List<BbsUserActiveLevel>activeLevels= bbsUserActiveLevelMng.getList(100);
		model.addAttribute("config", config);
		model.addAttribute("cmsConfig", cmsConfig);
		model.addAttribute("groupList", groupList);
		model.addAttribute("activeLevels", activeLevels);
		return "site_config/bbs_edit";
	}

	@RequiresPermissions("bbs_config:o_update")
	@RequestMapping("/bbs_config/o_update.do")
	public String bbsUpdate(BbsConfig bean,BbsConfigAttr bbsConfigAttr, Integer registerGroupId,
			Integer defaultGroupId, HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		bean.setSite(site);
		bean.setRegisterGroup(bbsUserGroupMng.findById(registerGroupId));
		bean.setDefaultGroup(bbsUserGroupMng.findById(defaultGroupId));
		bbsConfigMng.update(bean);
		cmsConfigMng.updateConfigAttr(bbsConfigAttr);
		return bbsEdit(request, model);
	}
	
	@RequiresPermissions("bbs_config:v_login_edit")
	@RequestMapping("/bbs_config/v_login_edit.do")
	public String loginEdit(HttpServletRequest request, ModelMap model) {
		model.addAttribute("configLogin", configMng.getConfigLogin());
		model.addAttribute("emailSender", configMng.getEmailSender());
		model.addAttribute("forgotPasswordTemplate", configMng.getForgotPasswordMessageTemplate());
		model.addAttribute("registerTemplate", configMng.getRegisterMessageTemplate());
		model.addAttribute("serviceExpiration", configMng.getServiceExpirationMessageTemplate());
		return "site_config/login_edit";
	}

	@RequiresPermissions("bbs_config:o_login_update")
	@RequestMapping("/bbs_config/o_login_update.do")
	public String loginUpdate(ConfigLogin configLogin,
			ConfigEmailSender emailSender, ConfigMessageTemplate msgTpl,
			HttpServletRequest request, ModelMap model) {
		//留空则默认原有密码
		if(StringUtils.isBlank(emailSender.getPassword())){
			emailSender.setPassword(configMng.getEmailSender().getPassword());
		}
		configMng.updateOrSave(configLogin.getAttr());
		configMng.updateOrSave(emailSender.getAttr());
		configMng.updateOrSave(msgTpl.getAttr());
		model.addAttribute("message", "global.success");
		log.info("update loginCoinfig of Config.");
		return loginEdit(request, model);
	}
	
	@RequiresPermissions("bbs_config:v_creditExchangeEdit")
	@RequestMapping("/bbs_config/v_creditExchangeEdit.do")
	public String bbsCreditExchangeEdit(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsCreditExchange creditExchange =creditExchangeMng.findById(site.getId());
		model.addAttribute("creditExchange",creditExchange);
		return "site_config/credit_exchange_rule";
	}
	
	@RequiresPermissions("bbs_config:v_creditExchangeUpdate")
	@RequestMapping("/bbs_config/v_creditExchangeUpdate.do")
	public String bbsCreditExchangeUpdate(BbsCreditExchange creditExchange ,HttpServletRequest request, ModelMap model) {
		if(creditExchange.getExchangetax()<1.0&&creditExchange.getExchangetax()>=0.0){
			creditExchangeMng.update(creditExchange);
			model.addAttribute("message", "global.success");
			log.info("update BbsCreditExchange");
		}
		return bbsCreditExchangeEdit(request, model);
	}
	
	
	@RequiresPermissions("bbs_config:v_api_edit")
	@RequestMapping("/bbs_config/v_api_edit.do")
	public String apiEdit(HttpServletRequest request, ModelMap model) {
		model.addAttribute("configAttr", cmsConfigMng.get().getConfigAttr());
		return "site_config/api_edit";
	}
	
	@RequiresPermissions("bbs_config:o_aip_update")
	@RequestMapping("/bbs_config/o_aip_update.do")
	public String apiUpdate(BbsConfigAttr configAttr,HttpServletRequest request, ModelMap model) {
		cmsConfigMng.updateConfigAttr(configAttr);
		model.addAttribute("message", "global.success");
		log.info("update attrs of CmsConfig.");
		return apiEdit(request, model);
	}
	
	@RequiresPermissions("bbs_config:v_sso_edit")
	@RequestMapping("/bbs_config/v_sso_edit.do")
	public String ssoAuthenticateEdit(HttpServletRequest request, ModelMap model) {
		CmsConfig config=cmsConfigMng.get();
		model.addAttribute("ssoMap", config.getSsoAttr());
		model.addAttribute("configAttr", config.getConfigAttr());
		return "site_config/sso_edit";
	}
	
	@RequiresPermissions("bbs_config:o_sso_update")
	@RequestMapping("/bbs_config/o_sso_update.do")
	public String ssoAuthenticateUpdate(HttpServletRequest request, ModelMap model) {
		Map<String,String>ssoMap=RequestUtils.getRequestMap(request, "attr_");
		cmsConfigMng.updateSsoAttr(ssoMap);
		model.addAttribute("message", "global.success");
		log.info("update attrs of CmsConfig.");
		return ssoAuthenticateEdit(request, model);
	}
	
	@RequiresPermissions("bbs_config:item_list")
	@RequestMapping("/bbs_config/item_list.do")
	public String itemList(HttpServletRequest request, ModelMap model) {
		model.addAttribute("registerItems",cmsConfigItemMng.getList(cmsConfigMng.get().getId(), CmsConfigItem.CATEGORY_REGISTER));
		return "site_config/item_list";
	}
	
	@RequiresPermissions("bbs_config:item_add")
	@RequestMapping("/bbs_config/item_add.do")
	public String itemAdd(HttpServletRequest request, ModelMap model) {
		return "site_config/item_add";
	}
	
	@RequiresPermissions("bbs_config:item_save")
	@RequestMapping("/bbs_config/item_save.do")
	public String itemSave(CmsConfigItem bean,HttpServletRequest request, ModelMap model) {
		bean.setConfig(cmsConfigMng.get());
		bean.setCategory(CmsConfigItem.CATEGORY_REGISTER);
		cmsConfigItemMng.save(bean);
		return itemList(request, model);
	}
	
	@RequiresPermissions("bbs_config:item_edit")
	@RequestMapping("/bbs_config/item_edit.do")
	public String itemEdit(Integer id,HttpServletRequest request, ModelMap model) {
		CmsConfigItem item = cmsConfigItemMng.findById(id);
		model.addAttribute("cmsConfigItem", item);
		return "site_config/item_edit";
	}
	
	@RequiresPermissions("bbs_config:item_update")
	@RequestMapping("/bbs_config/item_update.do")
	public String itemUpdate(CmsConfigItem bean,HttpServletRequest request, ModelMap model) {
		cmsConfigItemMng.update(bean);
		return itemList(request, model);
	}
	
	@RequiresPermissions("bbs_config:item_priority")
	@RequestMapping("/bbs_config/item_priority.do")
	public String itemPriority(Integer[] wids, Integer[] priority, String[] label,
			Boolean[] single, Boolean[] display, Integer modelId,
			Boolean isChannel, HttpServletRequest request, ModelMap model) {
		if (wids != null && wids.length > 0) {
			cmsConfigItemMng.updatePriority(wids, priority, label);
		}
		model.addAttribute(MESSAGE, "global.success");
		return itemList(request, model);
	}
	
	@RequiresPermissions("bbs_config:item_delete")
	@RequestMapping("/bbs_config/item_delete.do")
	public String itemDelete(Integer[] ids, Integer modelId, Boolean isChannel,
			HttpServletRequest request, ModelMap model) {
		CmsConfigItem[] beans = cmsConfigItemMng.deleteByIds(ids);
		for (CmsConfigItem bean : beans) {
			log.info("delete CmsConfigItem id={}", bean.getId());
		}
		return itemList(request, model);
	}
	
	@RequiresPermissions("bbs_config:v_message_edit")
	@RequestMapping("/bbs_config/v_message_edit.do")
	public String messageEdit(HttpServletRequest request, ModelMap model) {
		model.addAttribute("configAttr", cmsConfigMng.get().getConfigAttr());
		return "site_config/message_edit";
	}

	@RequiresPermissions("bbs_config:o_message_update")
	@RequestMapping("/bbs_config/o_message_update.do")
	public String messageUpdate(BbsConfigAttr configAttr, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		cmsConfigMng.updateConfigAttr(configAttr);
		model.addAttribute("message", "global.success");
		log.info("update message of CmsConfig.");
		return messageEdit(request, model);
	}
	
	@RequiresPermissions("bbs_config:v_ad_edit")
	@RequestMapping("/bbs_config/v_ad_edit.do")
	public String adEdit(HttpServletRequest request, ModelMap model) {
		model.addAttribute("configAttr", cmsConfigMng.get().getConfigAttr());
		return "site_config/ad_edit";
	}

	@RequiresPermissions("bbs_config:o_ad_update")
	@RequestMapping("/bbs_config/o_ad_update.do")
	public String adUpdate(BbsConfigAttr configAttr, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		cmsConfigMng.updateConfigAttr(configAttr);
		model.addAttribute("message", "global.success");
		log.info("update ad of CmsConfig.");
		return adEdit(request, model);
	}

	private WebErrors validateSystemUpdate(CmsConfig bean,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		return errors;
	}

	private WebErrors validateBaseUpdate(CmsSite bean,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		return errors;
	}

	@Autowired
	private CmsSiteMng manager;
	@Autowired
	private FtpMng ftpMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private ConfigMng configMng;
	@Autowired
	private BbsCreditExchangeMng creditExchangeMng;
	@Autowired
	private CmsConfigItemMng cmsConfigItemMng;
	@Autowired
	private BbsUserActiveLevelMng bbsUserActiveLevelMng;
}