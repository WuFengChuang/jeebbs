package com.jeecms.bbs.api.admin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
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

import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.google.gson.JsonObject;
import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.template.manager.CmsResourceMng;
import com.jeecms.common.util.Zipper;
import com.jeecms.common.util.Zipper.FileEntry;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.tpl.FileTpl;
import com.jeecms.core.tpl.Tpl;
import com.jeecms.core.tpl.TplManager;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsTemplateApiAct {
	
	private static final Logger log = LoggerFactory
			.getLogger(BbsTemplateApiAct.class);
	/**
	 * 模板树API
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/template/tree")
	public void tree(HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		CmsSite site=CmsUtils.getSite(request);
		JSONArray jsonArray=new JSONArray();
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_SUCCESS;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		String root = site.getTplPath();
		JSONObject result=new JSONObject();
		List<FileTpl>list=(List<FileTpl>) tplManager.getChild(root);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToTreeJson(list.get(i)));
			}
		}
		result.put("resources", jsonArray);
		result.put("rootPath", root);
		message=Constants.API_MESSAGE_SUCCESS;
		body=result.toString();
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 模板列表API
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/template/list")
	public void templateList(String root,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		CmsSite site=CmsUtils.getSite(request);
		JSONArray jsonArray=new JSONArray();
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if (StringUtils.isBlank(root)) {
			root = site.getTplPath();
		}
		WebErrors errors = validateList(root, site.getTplPath(), request);
		if (errors.hasErrors()) {
			code=ResponseCode.API_CODE_PARAM_ERROR;
			message=Constants.API_MESSAGE_PARAM_ERROR;
		}else{
			List<FileTpl>list=(List<FileTpl>) tplManager.getChild(root);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					jsonArray.put(i, list.get(i).convertToJson());
				}
			}
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
			body=jsonArray.toString();
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/template/dir_save")
	public void createDir(String root, String dirName,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		CmsSite site=CmsUtils.getSite(request);
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		if (StringUtils.isBlank(root)) {
			root = site.getTplPath();
		}
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors,
				root,dirName);
		if(!errors.hasErrors()){
			errors=validateList(root, site.getTplPath(), request);
		}
		if(!errors.hasErrors()){
			tplManager.save(root + "/" + dirName, null, true);
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/template/get")
	public void get(String name,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		CmsSite site=CmsUtils.getSite(request);
		String body="";
		String message=Constants.G_API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.G_API_STATUS_FAIL;
		String code=ResponseCode.G_API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors,name);
		if(!errors.hasErrors()){
			errors=validateList(name, site.getTplPath(), request);
		}
		if(!errors.hasErrors()){
			FileTpl tpl=(FileTpl) tplManager.get(name);
			status=Constants.G_API_STATUS_SUCCESS;
			message=Constants.G_API_MESSAGE_SUCCESS;
			code=ResponseCode.G_API_CODE_CALL_SUCCESS;
			body=tpl.getSource();
		}else{
			message=Constants.G_API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.G_API_CODE_PARAM_ERROR;
		}
		JsonObject json=new JsonObject();
		json.addProperty("body", body);
		json.addProperty("message",message);
		json.addProperty("code", code);
		json.addProperty("status", status);
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@SignValidate
	@RequestMapping("/template/save")
	public void save(String root, String filename, String source,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		if (StringUtils.isBlank(root)) {
			root = site.getTplPath();
		}
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors,
				 root,filename,source);
		if(!errors.hasErrors()){
			errors=validateList(root, site.getTplPath(), request);
		}
		if(!errors.hasErrors()){
			String name = root + "/" + filename + com.jeecms.bbs.Constants.TPL_SUFFIX;
			tplManager.save(name, source, false);
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/template/update")
	public void update( String filename, String source,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		String root= site.getTplPath();
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors,
				 filename,source);
		if(!errors.hasErrors()){
			errors=validateList(root, site.getTplPath(), request);
		}
		if(!errors.hasErrors()){
			tplManager.update(filename, source);
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
			log.error(errors.getErrors().get(0));
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/template/delete")
	public void delete(String names,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, names);
		String[]nameArray = null;
		if(!errors.hasErrors()){
			nameArray=names.split(Constants.API_ARRAY_SPLIT_STR);
			for(String n:nameArray){
				errors=validatePath(n, site.getTplPath(), errors);
				if(errors.hasErrors()){
					break;
				}
			}
		}
		if(!errors.hasErrors()){
			try {
				if(nameArray!=null){
					tplManager.delete(nameArray);
				}
				status=Constants.API_STATUS_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			}  catch (Exception e) {
				status=Constants.API_MESSAGE_DELETE_ERROR;
				message=Constants.API_MESSAGE_DELETE_ERROR;
				code=ResponseCode.API_CODE_DELETE_ERROR;
			}
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/template/rename")
	public void rename(String origName, String distName,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, origName,distName);
		if(!errors.hasErrors()){
			errors=validateRename(origName,distName, site.getTplPath(), request);
		}
		if(!errors.hasErrors()){
			tplManager.rename(origName,distName);
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/template/getSolutions")
	public void getSolutions(HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		CmsSite site=CmsUtils.getSite(request);
		JSONArray jsonArray=new JSONArray();
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		String[] solutions = resourceMng.getSolutions(site.getTplPath());
		for(int i=0;i<solutions.length;i++){
			jsonArray.put(i, solutions[i]);
		}
		status=Constants.API_STATUS_SUCCESS;
		message=Constants.API_MESSAGE_SUCCESS;
		code=ResponseCode.API_CODE_CALL_SUCCESS;
		body=jsonArray.toString();
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping("/template/solutionupdate")
	public void setTempate(String solution,Boolean mobile, 
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, solution);
		if(mobile==null){
			mobile=false;
		}
		if(!errors.hasErrors()){
			cmsSiteMng.updateTplSolution(site.getId(), solution,mobile);
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/template/exportTpl")
	public void tplExport(String solution,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, solution);
		if(!errors.hasErrors()){
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
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/template/importTpl")
	public void tplImport(
			@RequestParam(value = "uploadFile", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) {
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		CmsSite site=CmsUtils.getSite(request);
		WebErrors errors = validate(file, request);
		if(errors.hasErrors()){
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}else{
			//验证公共非空参数
			errors=ApiValidate.validateRequiredParams(errors,file);
			if(!errors.hasErrors()){
				String origName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(origName).toLowerCase(
						Locale.ENGLISH);
				String filepath = "";
				try {
					File tempFile = File.createTempFile("tplZip", "temp");
					file.transferTo(tempFile);
					resourceMng.imoport(tempFile, site);
					tempFile.delete();
					JSONObject json=new JSONObject();
					json.put("ext", ext.toUpperCase());
					json.put("size", file.getSize());
					json.put("url", filepath);
					json.put("name", file.getOriginalFilename());
					body=json.toString();
					message=Constants.API_MESSAGE_SUCCESS;
					status=Constants.API_STATUS_SUCCESS;
				} catch (Exception e) {
					//e.printStackTrace();
					code=ResponseCode.API_CODE_UPLOAD_ERROR;
				} 
			}else{
				code=ResponseCode.API_CODE_PARAM_REQUIRED;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping("/template/upload")
	public void upload(String root,
			@RequestParam(value = "uploadFile", required = false) MultipartFile file,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors=WebErrors.create(request);
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, file);
		if(!errors.hasErrors()){
			 errors = validateUpload(root,site.getTplPath(),file, request);
		}
		if(!errors.hasErrors()){
			try {
				String origName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(origName).toLowerCase(
						Locale.ENGLISH);
				tplManager.save(root, file);
				JSONObject json=new JSONObject();
				json.put("ext", ext.toUpperCase());
				json.put("size", file.getSize());
				json.put("url", root+"/"+origName);
				json.put("name", file.getOriginalFilename());
				body=json.toString();
				status=Constants.API_STATUS_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			}  catch (Exception e) {
				e.printStackTrace();
				status=Constants.API_MESSAGE_UPLOAD_ERROR;
				message=Constants.API_MESSAGE_UPLOAD_ERROR;
				code=ResponseCode.API_CODE_UPLOAD_ERROR;
			}
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateUpload(String root,String tplPath,
			MultipartFile file,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (file == null) {
			errors.addErrorString("\"error noFileToUpload\"");
			return errors;
		}
		if(isUnValidName(root, root, tplPath, errors)){
			errors.addErrorCode("template.invalidParams");
		}
		String filename=file.getOriginalFilename();
		if(filename!=null&&(filename.contains("/")||filename.contains("\\")||filename.indexOf("\0")!=-1)){
			errors.addErrorString("\"upload error filename\"");
			return errors;
		}
		return errors;
	}
	
	private WebErrors validate(MultipartFile file,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (file == null) {
			errors.addErrorString("imageupload error noFileToUpload");
			return errors;
		}
		String filename=file.getOriginalFilename();
		if(filename!=null&&(filename.contains("/")||filename.contains("\\")||filename.indexOf("\0")!=-1)){
			errors.addErrorString("upload error filename");
			return errors;
		}
		return errors;
	}
	
	private WebErrors validateList(String name, String tplPath,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(name, errors)) {
			return errors;
		}
		if(isUnValidName(name, name, tplPath, errors)){
			errors.addErrorCode("template.invalidParams");
		}
		return errors;
	}
	
	private WebErrors validatePath(String name, String tplPath,
			WebErrors errors) {
		if (vldExist(name, errors)) {
			return errors;
		}
		if(isUnValidName(name, name, tplPath, errors)){
			errors.addErrorCode("template.invalidParams");
		}
		return errors;
	}
	
	private WebErrors validateRename(String name, String newName,
			String tplPath,HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(name, errors)) {
			return errors;
		}
		if(isUnValidName(name, name, tplPath, errors)){
			errors.addErrorCode("template.invalidParams");
		}
		if(isUnValidName(newName, newName, tplPath, errors)){
			errors.addErrorCode("template.invalidParams");
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
	
	@Autowired
	private TplManager tplManager;
	@Autowired
	private CmsResourceMng resourceMng;
	@Autowired
	private CmsSiteMng cmsSiteMng;
}

