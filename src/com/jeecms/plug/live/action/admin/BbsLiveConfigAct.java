package com.jeecms.plug.live.action.admin;


import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.core.entity.BbsConfigAttr;
import com.jeecms.core.manager.CmsConfigMng;

@Controller
public class BbsLiveConfigAct {
	private static final Logger log = LoggerFactory.getLogger(BbsLiveConfigAct.class);

	@RequiresPermissions("live:v_config")
	@RequestMapping("/live/v_config.do")
	public String configEdit(HttpServletRequest request, ModelMap model) {
		model.addAttribute("cmsConfig", cmsConfigMng.get());
		return "plugPage/live/config";
	}
	
	@RequiresPermissions("live:config_update")
	@RequestMapping("/live/config_update.do")
	public String configUpdate(BbsConfigAttr bbsConfigAttr, HttpServletRequest request, ModelMap model) {
		cmsConfigMng.updateConfigAttr(bbsConfigAttr);
		return configEdit(request, model);
	}
	
	@Autowired
	private CmsConfigMng cmsConfigMng;
}