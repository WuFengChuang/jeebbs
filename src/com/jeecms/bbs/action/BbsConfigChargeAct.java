package com.jeecms.bbs.action;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsConfigChargeAct {
	private static final Logger log = LoggerFactory.getLogger(BbsConfigChargeAct.class);

	@RequiresPermissions("bbs_config:v_charge")
	@RequestMapping("/bbs_config/v_charge.do")
	public String edit(HttpServletRequest request, ModelMap model) {
		CmsConfig config=cmsConfigMng.get();
		model.addAttribute("configCharge", manager.getDefault());
		model.addAttribute("fixMap", config.getRewardFixAttr());
		return "site_config/charge_edit";
	}

	/**
	 * @param weixinPassword 微信支付商户密钥
	 * @param weixinSecret 微信公众号secret
	 * @param alipayKey 阿里支付密钥
	 * @param transferApiPassword 企业转账接口API密钥
	 * @param payTransferPassword 转账登陆密码
	 * @return
	 */
	@RequiresPermissions("bbs_config:o_charge_update")
	@RequestMapping("/bbs_config/o_charge_update.do")
	public String update(BbsConfigCharge bean,String weixinPassword,
			String weixinSecret,String alipayKey,String alipayPublicKey,
			String alipayPrivateKey,
			String transferApiPassword,String payTransferPassword,
			HttpServletRequest request,ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Map<String,String>attrs=new HashMap<String,String>();
		attrs.put("weixinPassword", weixinPassword);
		attrs.put("weixinSecret", weixinSecret);
		attrs.put("alipayKey", alipayKey);
		attrs.put("alipayPublicKey", alipayPublicKey);
		attrs.put("alipayPrivateKey", alipayPrivateKey);
		attrs.put("transferApiPassword", transferApiPassword);
		Map<String,String>fixMap=RequestUtils.getRequestMap(request, "attr_");
		bean = manager.update(bean,payTransferPassword,attrs,fixMap);
		log.info("update BbsConfigCharge id={}.", bean.getId());
		return edit(request, model);
	}
	
	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			return errors;
		}
		return errors;
	}

	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsConfigCharge entity = manager.findById(id);
		if(errors.ifNotExist(entity, BbsConfigCharge.class, id)) {
			return true;
		}
		return false;
	}
	
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private BbsConfigChargeMng manager;
}