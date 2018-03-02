package com.jeecms.bbs.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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

import com.jeecms.bbs.entity.BbsConfig;
import com.jeecms.bbs.entity.BbsUserActiveLevel;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.entity.CmsSensitivity;
import com.jeecms.bbs.manager.CmsSensitivityMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.BbsConfigAttr;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsConfigMng;

@Controller
public class CmsSensitivityAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsSensitivityAct.class);

	@RequiresPermissions("sensitivity:v_list")
	@RequestMapping("/sensitivity/v_list.do")
	public String list(HttpServletRequest request, ModelMap model) {
		Integer siteId = CmsUtils.getSiteId(request);
		List<CmsSensitivity> list = manager.getList(siteId, false);
		model.addAttribute("list", list);
		return "sensitivity/list";
	}
	
	@RequiresPermissions("sensitivity:v_add")
	@RequestMapping("/sensitivity/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		return "sensitivity/add";
	}
	
	@RequiresPermissions("sensitivity:o_batchSave")
	@RequestMapping("/sensitivity/o_batchSave.do")
	public String batchSave(String words,Integer type,
			HttpServletRequest request,
			ModelMap model) {
		Integer siteId = CmsUtils.getSiteId(request);
		BufferedReader br = new BufferedReader(new StringReader(words));
		List<String>wordList=new ArrayList<>();
		String line = "";
		try {
		while((line = br.readLine())!=null)
			{
				if(StringUtils.isNotBlank(line)){
					wordList.add(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		manager.batchSave(wordList,siteId, type);
		model.addAttribute("message", "global.success");
		return list(request, model);
	}

	@RequiresPermissions("sensitivity:o_save")
	@RequestMapping("/sensitivity/o_save.do")
	public String save(CmsSensitivity bean, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Integer siteId = CmsUtils.getSiteId(request);
		bean = manager.save(bean, siteId);
		model.addAttribute("message", "global.success");
		log.info("save CmsSensitivity id={}", bean.getId());
		return list(request, model);
	}

	@RequiresPermissions("sensitivity:o_update")
	@RequestMapping("/sensitivity/o_update.do")
	public String update(Integer[] id, String[] search, String[] replacement,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpdate(id, search, replacement, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		manager.updateEnsitivity(id, search, replacement);
		model.addAttribute("message", "global.success");
		log.info("update CmsSensitivity.");
		return list(request, model);
	}

	@RequiresPermissions("sensitivity:o_delete")
	@RequestMapping("/sensitivity/o_delete.do")
	public String delete(Integer[] ids, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		CmsSensitivity[] beans = manager.deleteByIds(ids);
		for (CmsSensitivity bean : beans) {
			log.info("delete CmsSensitivity id={}", bean.getId());
		}
		model.addAttribute("message", "global.success");
		return list(request, model);
	}
	
	@RequiresPermissions("sensitivity:v_open_close")
	@RequestMapping("/sensitivity/v_open_close.do")
	public void openClose(String open,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		CmsConfig cmsConfig=cmsConfigMng.get();
		BbsConfigAttr attr=cmsConfig.getConfigAttr();
		if(StringUtils.isNotBlank(open)){
			if(open.equals("true")){
				attr.setSensitivityInputOn(true);
			}else if(open.equals("false")){
				attr.setSensitivityInputOn(false);
			}
		}
		cmsConfigMng.updateConfigAttr(attr);
		ResponseUtils.renderJson(response, attr.getSensitivityInputOn().toString());
	}

	private WebErrors validateSave(CmsSensitivity bean,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		return errors;
	}

	private WebErrors validateUpdate(Integer[] ids, String[] searchs,
			String[] replacements, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifEmpty(ids, "id")) {
			return errors;
		}
		if (errors.ifEmpty(searchs, "name")) {
			return errors;
		}
		if (errors.ifEmpty(replacements, "url")) {
			return errors;
		}
		if (ids.length != searchs.length || ids.length != replacements.length) {
			errors.addErrorString("id, searchs, replacements length"
					+ " not equals");
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, errors);
			return errors;
		}
		return errors;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifEmpty(ids, "ids")) {
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, errors);
		}
		return errors;
	}

	private boolean vldExist(Integer id, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		CmsSensitivity entity = manager.findById(id);
		if (errors.ifNotExist(entity, CmsSensitivity.class, id)) {
			return true;
		}
		return false;
	}

	@Autowired
	private CmsSensitivityMng manager;
	@Autowired
	private CmsConfigMng cmsConfigMng;
}