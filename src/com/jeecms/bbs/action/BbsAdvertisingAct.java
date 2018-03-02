package com.jeecms.bbs.action;

import static com.jeecms.common.page.SimplePage.cpn;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.entity.BbsAdvertising;
import com.jeecms.bbs.entity.BbsAdvertisingSpace;
import com.jeecms.bbs.entity.BbsConfig;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsAdvertisingMng;
import com.jeecms.bbs.manager.BbsAdvertisingSpaceMng;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.entity.Ftp;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.manager.DbFileMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsAdvertisingAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsAdvertisingAct.class);

	@RequiresPermissions("advertising:v_list")
	@RequestMapping("/advertising/v_list.do")
	public String list(Integer queryAdspaceId, Boolean queryEnabled,
			Integer queryChargeMode,String queryTitle,
			Integer pageNo, HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		Pagination pagination = manager.getPage(site.getId(), queryAdspaceId,
				queryEnabled,queryChargeMode,queryTitle,null,
				cpn(pageNo), CookieUtils.getPageSize(request));
		List<BbsAdvertisingSpace> adspaceList = bbsAdvertisingSpaceMng
				.getList(site.getId());
		model.addAttribute("pagination", pagination);
		model.addAttribute("adspaceList", adspaceList);
		model.addAttribute("pageNo", pagination.getPageNo());
		if (queryAdspaceId != null) {
			model.addAttribute("queryAdspaceId", queryAdspaceId);
		}
		if (queryEnabled != null) {
			model.addAttribute("queryEnabled", queryEnabled);
		}
		model.addAttribute("queryChargeMode", queryChargeMode);
		model.addAttribute("queryTitle", queryTitle);
		return "advertising/list";
	}

	@RequiresPermissions("advertising:v_add")
	@RequestMapping("/advertising/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		List<BbsAdvertisingSpace> adspaceList = bbsAdvertisingSpaceMng
				.getList(site.getId());
		model.addAttribute("adspaceList", adspaceList);
		return "advertising/add";
	}

	@RequiresPermissions("advertising:v_edit")
	@RequestMapping("/advertising/v_edit.do")
	public String edit(Integer id, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsAdvertising cmsAdvertising = manager.findById(id);
		int adDay=0;
		if(cmsAdvertising!=null){
			if(cmsAdvertising.getEndTime()!=null&&cmsAdvertising.getStartTime()!=null){
				 adDay=DateUtils.getDaysBetween(cmsAdvertising.getStartTime(), cmsAdvertising.getEndTime());
			}
		}
		model.addAttribute("cmsAdvertising", cmsAdvertising);
		model.addAttribute("attr", cmsAdvertising.getAttr());
		model.addAttribute("adspaceList", bbsAdvertisingSpaceMng.getList(site
				.getId()));
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("adDay", adDay);
		return "advertising/edit";
	}

	/*
	@RequiresPermissions("advertising:o_save")
	@RequestMapping("/advertising/o_save.do")
	public String save(BbsAdvertising bean, Integer adspaceId,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
		// 去除为空串的属性
		Set<String> toRemove = new HashSet<String>();
		for (Entry<String, String> entry : attr.entrySet()) {
			if (StringUtils.isBlank(entry.getValue())) {
				toRemove.add(entry.getKey());
			}
		}
		for (String key : toRemove) {
			attr.remove(key);
		}
		bean = manager.save(bean, adspaceId, attr);
		log.info("save BbsAdvertising id={}", bean.getId());
		return "redirect:v_list.do";
	}
	*/
	
	@RequiresPermissions("advertising:o_save")
	@RequestMapping("/advertising/o_save.do")
	public String save(BbsAdvertising bean, Integer adspaceId,
			Integer chargeDay,Date startTime,String username,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
		// 去除为空串的属性
		Set<String> toRemove = new HashSet<String>();
		for (Entry<String, String> entry : attr.entrySet()) {
			if (StringUtils.isBlank(entry.getValue())) {
				toRemove.add(entry.getKey());
			}
		}
		for (String key : toRemove) {
			attr.remove(key);
		}
		if(StringUtils.isNotBlank(username)){
			bean.setOwner(userMng.findByUsername(username));
		}
		bean = manager.save(bean, adspaceId,chargeDay,startTime,attr);
		log.info("save BbsAdvertising id={}", bean.getId());
		return "redirect:v_list.do";
	}

	@RequiresPermissions("advertising:o_update")
	@RequestMapping("/advertising/o_update.do")
	public String update(Integer queryAdspaceId, Boolean queryEnabled,
			BbsAdvertising bean, Integer adspaceId,
			Integer chargeDay,Date startTime,
			Integer queryChargeMode,String queryTitle, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
		// 去除为空串的属性
		Set<String> toRemove = new HashSet<String>();
		for (Entry<String, String> entry : attr.entrySet()) {
			if (StringUtils.isBlank(entry.getValue())) {
				toRemove.add(entry.getKey());
			}
		}
		for (String key : toRemove) {
			attr.remove(key);
		}
		bean = manager.update(bean, adspaceId,chargeDay,startTime, attr);
		log.info("update BbsAdvertising id={}.", bean.getId());
		return list(queryAdspaceId, queryEnabled, queryChargeMode,
				queryTitle,pageNo, request, model);
	}
	
	@RequiresPermissions("advertising:getChargeAmount")
	@RequestMapping("/advertising/getChargeAmount.do")
	public void getChargeAmount(Integer chargeDay,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		JSONObject json=new JSONObject();
		try {
			if(chargeDay<=0){
				json.put("amount",0);
			}else{
				CmsConfig config=configMng.get();
				Double adDayCharge=config.getAdDayCharge();
				json.put("amount",adDayCharge*chargeDay);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	
	@RequiresPermissions("advertising:checkAdAmount")
	@RequestMapping("/advertising/checkAdAmount.do")
	public void checkUserAdAmount(Double amount,String username,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		BbsUser user=null;
		JSONObject json=new JSONObject();
		try {
			if(StringUtils.isBlank(username)||amount==null||amount<=0){
				json.put("status", "-1");
			}else{
				user=userMng.findByUsername(username);
				if(user!=null){
					if(user.getAdAccountMount()>=amount){
						json.put("status", "1");
					}else{
						json.put("userAdAmount",user.getAdAccountMount());
						json.put("status", "2");
					}
				}else{
					json.put("status", "-1");
				}
			}
		}catch (Exception e) {
			
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@RequiresPermissions("advertising:o_delete")
	@RequestMapping("/advertising/o_delete.do")
	public String delete(Integer[] ids, Integer queryAdspaceId,
			Boolean queryEnabled, Integer queryChargeMode,String queryTitle,
			Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsAdvertising[] beans = manager.deleteByIds(ids);
		for (BbsAdvertising bean : beans) {
			log.info("delete BbsAdvertising id={}", bean.getId());
		}
		return list(queryAdspaceId, queryEnabled, queryChargeMode,
				queryTitle, pageNo, request, model);
	}

	@RequiresPermissions("advertising:o_upload_flash")
	@RequestMapping("/advertising/o_upload_flash.do")
	public String uploadFlash(
			@RequestParam(value = "flashFile", required = false) MultipartFile file,
			String flashNum, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpload(file, request);
		if (errors.hasErrors()) {
			model.addAttribute("error", errors.getErrors().get(0));
			return "advertising/flash_iframe";
		}
		CmsSite site = CmsUtils.getSite(request);
		String origName = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origName).toLowerCase(
				Locale.ENGLISH);
		// TODO 检查允许上传的后缀
		try {
			String fileUrl;
			if (site.getConfig().getUploadToDb()) {
				String dbFilePath = site.getConfig().getDbFileUri();
				fileUrl = dbFileMng.storeByExt(site.getUploadPath(), ext, file
						.getInputStream());
				// 加上访问地址
				fileUrl = request.getContextPath() + dbFilePath + fileUrl;
			} else if (site.getUploadFtp() != null) {
				Ftp ftp = site.getUploadFtp();
				String ftpUrl = ftp.getUrl();
				fileUrl = ftp.storeByExt(site.getUploadPath(), ext, file
						.getInputStream());
				// 加上url前缀
				fileUrl = ftpUrl + fileUrl;
			} else {
				String ctx = request.getContextPath();
				fileUrl = fileRepository.storeByExt(site.getUploadPath(), ext,
						file);
				// 加上部署路径
				fileUrl = ctx + fileUrl;
			}
			model.addAttribute("flashPath", fileUrl);
			model.addAttribute("flashName", origName);
			model.addAttribute("flashNum", flashNum);
		} catch (IllegalStateException e) {
			model.addAttribute("error", e.getMessage());
			log.error("upload file error!", e);
		} catch (IOException e) {
			model.addAttribute("error", e.getMessage());
			log.error("upload file error!", e);
		}
		return "advertising/flash_iframe";
	}

	private WebErrors validateSave(BbsAdvertising bean,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		bean.setSite(site);
		return errors;
	}

	private WebErrors validateEdit(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (vldExist(id, site.getId(), errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (errors.ifEmpty(ids, "ids")) {
			return errors;
		}
		for (Integer id : ids) {
			vldExist(id, site.getId(), errors);
		}
		return errors;
	}

	private boolean vldExist(Integer id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsAdvertising entity = manager.findById(id);
		if (errors.ifNotExist(entity, BbsAdvertising.class, id)) {
			return true;
		}
		return false;
	}

	private WebErrors validateUpload(MultipartFile file,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifNull(file, "file")) {
			return errors;
		}
		return errors;
	}

	@Autowired
	private BbsAdvertisingSpaceMng bbsAdvertisingSpaceMng;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private DbFileMng dbFileMng;
	@Autowired
	private BbsAdvertisingMng manager;
	@Autowired
	private CmsConfigMng configMng;
	@Autowired
	private BbsUserMng userMng;
}