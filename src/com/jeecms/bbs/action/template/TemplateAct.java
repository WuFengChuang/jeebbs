package com.jeecms.bbs.action.template;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.Constants;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.template.manager.CmsResourceMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.util.Zipper;
import com.jeecms.common.util.Zipper.FileEntry;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.tpl.Tpl;
import com.jeecms.core.tpl.TplManager;

/**
 * JEEBBS模板的Action
 * 
 * @author tom
 * 
 */
// TODO 验证path必须以TPL_BASE开头，不能有..后退关键字
@Controller
public class TemplateAct {
	private static final Logger log = LoggerFactory
			.getLogger(TemplateAct.class);
	private static final String INVALID_PARAM = "template.invalidParams";

	@RequiresPermissions("template:v_left")
	@RequestMapping("/template/v_left.do")
	public String left(String path, HttpServletRequest request, ModelMap model) {
		return "template/left";
	}

	@RequiresPermissions("template:v_tree")
	@RequestMapping(value = "/template/v_tree.do", method = RequestMethod.GET)
	public String tree(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String root = RequestUtils.getQueryParam(request, "root");
		log.debug("tree path={}", root);
		// jquery treeview的根请求为root=source
		if (StringUtils.isBlank(root) || "source".equals(root)) {
			root = site.getTplPath();
			model.addAttribute("isRoot", true);
		} else {
			model.addAttribute("isRoot", false);
		}
		WebErrors errors = validateTree(root, request);
		if (errors.hasErrors()) {
			log.error(errors.getErrors().get(0));
			ResponseUtils.renderJson(response, "[]");
			return null;
		}
		List<? extends Tpl> tplList = tplManager.getChild(root);
		model.addAttribute("tplList", tplList);
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json;charset=UTF-8");
		return "template/tree";
	}

	// 直接调用方法需要把root参数保存至model中
	@RequiresPermissions("template:v_list")
	@RequestMapping(value = "/template/v_list.do", method = RequestMethod.GET)
	public String list(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String root = (String) model.get("root");
		if (root == null) {
			root = RequestUtils.getQueryParam(request, "root");
		}
		log.debug("list Template root: {}", root);
		if (StringUtils.isBlank(root)) {
			root = site.getTplPath();
		}
		WebErrors errors = validateList(root, site.getTplPath(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		String rel = root.substring(site.getTplPath().length());
		if (rel.length() == 0) {
			rel = "/";
		}
		model.addAttribute("root", root);
		model.addAttribute("rel", rel);
		model.addAttribute("list", tplManager.getChild(root));
		return "template/list";
	}

	@RequiresPermissions("template:o_create_dir")
	@RequestMapping(value = "/template/o_create_dir.do")
	public String createDir(String root, String dirName,
			HttpServletRequest request, ModelMap model) {
		// TODO 检查dirName是否存在
		tplManager.save(root + "/" + dirName, null, true);
		model.addAttribute("root", root);
		return list(request, model);
	}

	@RequiresPermissions("template:v_add")
	@RequestMapping(value = "/template/v_add.do", method = RequestMethod.GET)
	public String add(HttpServletRequest request, ModelMap model) {
		String root = RequestUtils.getQueryParam(request, "root");
		model.addAttribute("root", root);
		return "template/add";
	}

	@RequiresPermissions("template:v_edit")
	@RequestMapping("/template/v_edit.do")
	public String edit(HttpServletRequest request, ModelMap model) {
		String root = RequestUtils.getQueryParam(request, "root");
		String name = RequestUtils.getQueryParam(request, "name");
		WebErrors errors = validateEdit(root, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("template", tplManager.get(name));
		model.addAttribute("root", root);
		return "template/edit";
	}

	@RequiresPermissions("template:o_save")
	@RequestMapping("/template/o_save.do")
	public String save(String root, String filename, String source,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(filename, source, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		String name = root + "/" + filename + Constants.TPL_SUFFIX;
		tplManager.save(name, source, false);
		model.addAttribute("root", root);
		log.info("save Template name={}", filename);
		return "redirect:v_list.do";
	}

	// AJAX请求，不返回页面
	@RequiresPermissions("template:o_update")
	@RequestMapping("/template/o_update.do")
	public void update(String root, String name, String source,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = validateUpdate(root, name,site.getTplPath(), source, request);
		if (errors.hasErrors()) {
			ResponseUtils.renderJson(response, "{success:false}");
		}else{
			tplManager.update(name, source);
			log.info("update Template name={}.", name);
			model.addAttribute("root", root);
			ResponseUtils.renderJson(response, "{success:true}");
		}
	}

	@RequiresPermissions("template:o_delete")
	@RequestMapping("/template/o_delete.do")
	public String delete(String root, String[] names,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateDelete(root, names, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		int count = tplManager.delete(names);
		log.info("delete Template count: {}", count);
		for (String name : names) {
			log.info("delete Template name={}", name);
		}
		model.addAttribute("root", root);
		return list(request, model);
	}

	@RequiresPermissions("template:o_delete")
	@RequestMapping("/template/o_delete_single.do")
	public String deleteSingle(HttpServletRequest request, ModelMap model) {
		// TODO 输入验证
		String root = RequestUtils.getQueryParam(request, "root");
		String name = RequestUtils.getQueryParam(request, "name");
		int count = tplManager.delete(new String[] { name });
		log.info("delete Template {}, count {}", name, count);
		model.addAttribute("root", root);
		return list(request, model);
	}

	@RequiresPermissions("template:v_rename")
	@RequestMapping(value = "/template/v_rename.do", method = RequestMethod.GET)
	public String renameInput(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String root = RequestUtils.getQueryParam(request, "root");
		String name = RequestUtils.getQueryParam(request, "name");
		String origName = name.substring(site.getTplPath().length());
		model.addAttribute("origName", origName);
		model.addAttribute("root", root);
		return "template/rename";
	}

	@RequiresPermissions("template:o_rename")
	@RequestMapping(value = "/template/o_rename.do", method = RequestMethod.POST)
	public String renameSubmit(String root, String origName, String distName,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String orig = site.getTplPath() + origName;
		String dist = site.getTplPath() + distName;
		if(!distName.endsWith("html")){
			WebErrors webErrors=WebErrors.create(request);
			webErrors.addErrorCode("template.filename.error");
			return webErrors.showErrorPage(model);
		}
		tplManager.rename(orig, dist);
		log.info("name Template from {} to {}", orig, dist);
		model.addAttribute("root", root);
		return list(request, model);
	}

	@RequiresPermissions("template:o_swfupload")
	@RequestMapping(value = "/template/o_swfupload.do", method = RequestMethod.POST)
	public void swfUpload(
			String root,
			@RequestParam(value = "Filedata", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IllegalStateException, IOException {
		WebErrors errors = validateUpload( file, request);
		JSONObject jsonObt = new JSONObject();
		JSONObject data=new JSONObject();
		try {
			if (errors.hasErrors()) {
				data.put("error", errors.getErrors().get(0));
			}else{
				String origName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(origName).toLowerCase(
						Locale.ENGLISH);
				tplManager.save(root, file);
				try {
					jsonObt.put("name", origName);
					jsonObt.put("ext", ext.toUpperCase());
					jsonObt.put("size", file.getSize());
					data.put("files", jsonObt);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				model.addAttribute("root", root);
				log.info("file upload seccess: {}, size:{}.", file
						.getOriginalFilename(), file.getSize());
			}	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, data.toString());
	}

	@RequiresPermissions("template:v_setting")
	@RequestMapping(value = "/template/v_setting.do")
	public String setting(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String[] solutions = resourceMng.getSolutions(site.getTplPath());
		model.addAttribute("solutions", solutions);
		model.addAttribute("defSolution", site.getTplSolution());
		return "template/setting";
	}

	@RequiresPermissions("template:o_def_template")
	@RequestMapping(value = "/template/o_def_template.do")
	public void defTempate(String solution, HttpServletRequest request,
			HttpServletResponse response) {
		CmsSite site = CmsUtils.getSite(request);
		cmsSiteMng.updateTplSolution(site.getId(), solution,false);
		ResponseUtils.renderJson(response, "{'success':true}");
	}

	@RequiresPermissions("template:o_export")
	@RequestMapping(value = "/template/o_export.do")
	public void exportSubmit(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String solution = RequestUtils.getQueryParam(request, "solution");
		CmsSite site = CmsUtils.getSite(request);
		List<FileEntry> fileEntrys = resourceMng.export(site, solution);
		response.setContentType("application/x-download;charset=UTF-8");
		response.addHeader("Content-disposition", "filename=template-"
				+ solution + ".zip");
		try {
			// 模板一般都在windows下编辑，所以默认编码为GBK
			Zipper.zip(response.getOutputStream(), fileEntrys, "GBK");
		} catch (IOException e) {
			log.error("export template error!", e);
		}
	}

	@RequiresPermissions("template:o_import")
	@RequestMapping(value = "/template/o_import.do")
	public String importSubmit(
			@RequestParam(value = "tplZip", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model,boolean mobileSetting) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		File tempFile = File.createTempFile("tplZip", "temp");
		file.transferTo(tempFile);
		resourceMng.imoport(tempFile, site);
		tempFile.delete();
		if(mobileSetting){
			return mobile_setting(request, response, model);
		}else{
			return setting(request, response, model);
		}
	}
	
	@RequiresPermissions("template:v_mobile_setting")
	@RequestMapping(value = "/template/v_mobile_setting.do")
	public String mobile_setting(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String[] solutions = resourceMng.getSolutions(site.getTplPath());
		model.addAttribute("solutions", solutions);
		model.addAttribute("defSolution", site.getTplMobileSolution());
		return "template/mobile_setting";
	}

	@RequiresPermissions("template:o_mobile_def_template")
	@RequestMapping(value = "/template/o_mobile_def_template.do")
	public void mobile_defTempate(String solution, HttpServletRequest request,
			HttpServletResponse response) {
		CmsSite site = CmsUtils.getSite(request);
		cmsSiteMng.updateTplSolution(site.getId(), solution,true);
		ResponseUtils.renderJson(response, "{'success':true}");
	}
	
	private WebErrors validateList(String name, String tplPath,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(name, errors)) {
			return errors;
		}
		if(isUnValidName(name, name, tplPath, errors)){
			errors.addErrorCode(INVALID_PARAM);
		}
		return errors;
	}
	
	private boolean isUnValidName(String path,String name,String tplPath, WebErrors errors) {
		if (!path.startsWith(tplPath)||path.contains("../")||path.contains("..\\")||name.contains("..\\")||name.contains("../")) {
			return true;
		}else{
			return false;
		}
	}
	
	private WebErrors validateTree(String path, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		// if (errors.ifBlank(path, "path", 255)) {
		// return errors;
		// }
		return errors;
	}

	private WebErrors validateSave(String name, String source,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		return errors;
	}

	private WebErrors validateEdit(String id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			return errors;
		}
		return errors;
	}
	
	private WebErrors validateUpdate(String root, String name, String tplPath,String source,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(name, errors)) {
			return errors;
		}
		if(isUnValidName(root, name, tplPath, errors)){
			errors.addErrorCode(INVALID_PARAM);
		}
		return errors;
	}
	
	protected WebErrors validateUpload(MultipartFile file,
			HttpServletRequest request) {
		BbsUser user=CmsUtils.getUser(request);
		WebErrors errors = WebErrors.create(request);
		if (file == null) {
			errors.addErrorCode("imageupload.error.noFileToUpload");
			return errors;
		}
		String filename=file.getOriginalFilename();
		if (StringUtils.isBlank(filename)) {
			filename = file.getOriginalFilename();
		}
		if(filename!=null&&filename.indexOf("\0")!=-1){
			errors.addErrorCode("upload.error.filename", filename);
		}
		//验证文件大小
		errors=validateFile(file, user, request);
		return errors;
	}
	
	protected WebErrors validateFile(MultipartFile file,BbsUser user,
			HttpServletRequest request) {
		String origName = file.getOriginalFilename();
		int fileSize = (int) (file.getSize() / 1024);
		WebErrors errors = WebErrors.create(request);
		if(origName!=null&&origName.indexOf("\0")!=-1){
			errors.addErrorCode("upload.error.filename", origName);
		}
		//超过附件大小限制
		if(!user.getGroup().isAllowMaxFile((int)(file.getSize()/1024))){
			errors.addErrorCode("upload.error.toolarge",origName,user.getGroup().getAllowMaxFile());
			return errors;
		}
		//超过每日上传限制
		if (!user.getGroup().isAllowPerDay(fileSize)) {
			errors.addErrorCode("upload.error.dailylimit", fileSize);
		}
		return errors;
	}

	private WebErrors validateDelete(String root, String[] names,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(names, "names");
		for (String id : names) {
			vldExist(id, errors);
		}
		return errors;
	}

	private boolean vldExist(String name, WebErrors errors) {
		if (errors.ifNull(name, "name")) {
			return true;
		}
		Tpl entity = tplManager.get(name);
		if (errors.ifNotExist(entity, Tpl.class, name)) {
			return true;
		}
		return false;
	}

	private TplManager tplManager;
	private CmsResourceMng resourceMng;
	private CmsSiteMng cmsSiteMng;

	public void setTplManager(TplManager tplManager) {
		this.tplManager = tplManager;
	}

	@Autowired
	public void setResourceMng(CmsResourceMng resourceMng) {
		this.resourceMng = resourceMng;
	}

	@Autowired
	public void setCmsSiteMng(CmsSiteMng cmsSiteMng) {
		this.cmsSiteMng = cmsSiteMng;
	}
}