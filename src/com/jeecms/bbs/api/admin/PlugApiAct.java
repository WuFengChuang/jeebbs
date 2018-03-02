package com.jeecms.bbs.api.admin;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsPlug;
import com.jeecms.bbs.manager.BbsPlugMng;
import com.jeecms.bbs.template.manager.CmsResourceMng;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.web.WebErrors;

@Controller
public class PlugApiAct {
	private static final Logger log = LoggerFactory.getLogger(PlugApiAct.class);
	private static final String PLUG_CONFIG_INI="plug.ini";
	private static final String PLUG_CONFIG_KEY_REPAIR="plug_repair";
	@Autowired
	private BbsPlugMng manager;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private CmsResourceMng resourceMng;
	@Autowired
	protected FileRepository fileRepository;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/plug/list")
	public void list(Integer pageNo,Integer pageSize,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		Pagination page = manager.getPage(pageNo, pageSize);
		List<BbsPlug> list = (List<BbsPlug>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value="/plug/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsPlug bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new BbsPlug();
			}else{
				bean = manager.findById(id);
			}
			if (bean!=null) {
				JSONObject json = bean.convertToJson();
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = json.toString();
			}else{
				code = ResponseCode.API_CODE_NOT_FOUND;
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SignValidate
	@RequestMapping(value="/plug/save")
	public void save(BbsPlug bean,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getName(),bean.getPath());
		if (!errors.hasErrors()) {
			File file = new File(realPathResolver.get(bean.getPath()));
			if (file.exists()) {
				if (!isRepairPlug(file)) {
					boolean fileConflict = isFileConflict(file);
					bean.setFileConflict(fileConflict);
				}else{
					bean.setFileConflict(false);
				}
				bean.setUsed(false);
				bean.setUploadTime(Calendar.getInstance().getTime());
				bean = manager.save(bean);
				log.info("save CmsPlug id={}",bean.getId());
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
			}else{
				status = Constants.API_STATUS_FAIL;
				message = Constants.API_MESSAGE_FILE_NOT_FOUNT;
				code = ResponseCode.API_CODE_FILE_NOT_FOUNT;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SignValidate
	@RequestMapping(value = "/plug/update")
	public void update(BbsPlug bean ,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getId(),bean.getName(),bean.getPath());
		if (!errors.hasErrors()) {
			BbsPlug dbPlug = manager.findById(bean.getId());
			if (dbPlug!=null){
				if (dbPlug.getUsed()&&!bean.getPath().equals(dbPlug.getPath())) {
					message = Constants.API_MESSAGE_PLUG_IN_USED;
					code = ResponseCode.API_CODE_PLUG_IN_USED;
				}else{
					File file = new File(realPathResolver.get(bean.getPath()));
					if (file.exists()) {
						if(!isRepairPlug(file)){
							boolean fileConflict=isFileConflict(file);
							bean.setFileConflict(fileConflict);
						}else{
							bean.setFileConflict(false);
						}
						bean = manager.update(bean);
						log.info("update CmsPlug id={}.", bean.getId());
						status = Constants.API_STATUS_SUCCESS;
						message = Constants.API_MESSAGE_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
						body = "{\"id\":"+"\""+bean.getId()+"\"}";
					}else {
						message = Constants.API_MESSAGE_FILE_NOT_FOUNT;
						code = ResponseCode.API_CODE_FILE_NOT_FOUNT;
					}
				}
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/plug/upload")
	public void upload(
			@RequestParam(value = "plugFile",required =false)MultipartFile file,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message=Constants.API_MESSAGE_PARAM_REQUIRED;
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = validateUpload(file, request);
		if (!errors.hasErrors()) {
//			String origName = file.getOriginalFilename();
//			String ext = FilenameUtils.getExtension(origName).toLowerCase(Locale.ENGLISH);
			//检查允许上传的后缀
			try{
				String fileUrl;
				String filename = com.jeecms.bbs.Constants.PLUG_PATH+file.getOriginalFilename();
				File oldFile = new File(realPathResolver.get(filename));
				if (oldFile.exists()) {
					oldFile.delete();
				}
				fileUrl = fileRepository.storeByFilePath(com.jeecms.bbs.Constants.PLUG_PATH, file.getOriginalFilename(), file);
				JSONObject json = new JSONObject();
				if (StringUtils.isNotBlank(fileUrl)) {
					json.put("plugPath", fileUrl);
				}else{
					json.put("plugPath", "");
				}
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = json.toString();
			}catch(Exception e){
				message = Constants.API_MESSAGE_UPLOAD_ERROR;
				code = ResponseCode.API_CODE_UPLOAD_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/plug/install")
	public void install(Integer id,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, id);
		if (!errors.hasErrors()) {
			//解压zip
			BbsPlug plug = manager.findById(id);
			if (plug!=null&&fileExist(plug.getPath())) {
				File zipFile = new File(realPathResolver.get(plug.getPath()));
				boolean isRepariPlug = isRepairPlug(zipFile);
				if (isRepariPlug) {//修复类插件不考虑是否冲突
					installPlug(zipFile, plug, true, request);
					status = Constants.API_STATUS_SUCCESS;
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}else{
					//新功能有冲突不解压
					boolean fileConflict = isFileConflict(zipFile);
					if (fileConflict) {
						message = Constants.API_MESSAGE_PLUG_CONFLICT;
						code = ResponseCode.API_CODE_PLUG_CONFLICT;
					}else{
						installPlug(zipFile, plug, false, request);
						status = Constants.API_STATUS_SUCCESS;
						message = Constants.API_MESSAGE_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
					}
				}
			}else{
				message = Constants.API_MESSAGE_FILE_NOT_FOUNT;
				code = ResponseCode.API_CODE_FILE_NOT_FOUNT;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/plug/uninstall")
	public void uninstall(Integer id,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, id);
		if (!errors.hasErrors()) {
			BbsPlug plug = manager.findById(id);
			if (plug!=null&&fileExist(plug.getPath())) {
				File file = new File(realPathResolver.get(plug.getPath()));
				//修复类插件不允许卸载
				if (plug.getPlugRepair()) {
					message = Constants.API_MESSAGE_CANNOT_UNINSTALL;
					code = ResponseCode.API_CODE_CANNOT_UNINSTALL;
				}else{
					boolean fileConflict = plug.getFileConflict();
					if (!fileConflict) {
						resourceMng.deleteZipFile(file);
						plug.setUninstallTime(Calendar.getInstance().getTime());
						plug.setUsed(false);
						manager.update(plug);
						log.info("plug.log.uninstall "+ plug.getName());
						status = Constants.API_STATUS_SUCCESS;
						message = Constants.API_MESSAGE_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
					}else{
						message = Constants.API_MESSAGE_PLUG_CONFLICT;
						code = ResponseCode.API_CODE_PLUG_CONFLICT;
					}
				}
			}else{
				message = Constants.API_MESSAGE_FILE_NOT_FOUNT;
				code = ResponseCode.API_CODE_FILE_NOT_FOUNT;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SignValidate
	@RequestMapping(value = "/plug/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors =WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				BbsPlug[] deleteByIds = manager.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsPlug id={}",deleteByIds[i].getId());
				}
				status=Constants.API_STATUS_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			} catch (Exception e) {
				status = Constants.API_MESSAGE_DELETE_ERROR;
				message = Constants.API_MESSAGE_DELETE_ERROR;
				code = ResponseCode.API_CODE_DELETE_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors validateUpload(MultipartFile file,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifNull(file, "file")) {
			return errors;
		}
		String filename=file.getOriginalFilename();
		if(filename!=null&&filename.indexOf("\0")!=-1){
			errors.addErrorCode("upload.error.filename", filename);
		}
		String filePath=com.jeecms.bbs.Constants.PLUG_PATH+filename;
		BbsPlug plug=manager.findByPath(filePath);
		File tempFile =new File(realPathResolver.get(filePath));
		//使用中的而且插件已经存在则不允许重新上传
		if(plug!=null&&plug.getUsed()&&tempFile.exists()){
			errors.addErrorCode("error.plug.upload",plug.getName());
		}
		return errors;
	}
	
	private void installPlug(File zipFile,BbsPlug plug,boolean isRepairPlug,
			HttpServletRequest request) throws IOException{
		//resourceMng.unZipFile(zipFile);
		plug.setInstallTime(Calendar.getInstance().getTime());
		plug.setUsed(true);
		plug.setPlugRepair(isRepairPlug);
		resourceMng.installPlug(zipFile, plug);
		log.info("plug.log.install " +plug.getName());
	}
	
	private boolean fileExist(String filePath) {
		File file=new File(realPathResolver.get(filePath));
		return file.exists();
	}
	
	private  boolean isRepairPlug(File file) {
		boolean isRepairPlug=false;
		String plugIni="";
		String repairStr="";
		try {
			plugIni=resourceMng.readFileFromZip(file, PLUG_CONFIG_INI);
			if(StringUtils.isNotBlank(plugIni)){
				String[]configs=plugIni.split(";");
				for(String c:configs){
					String[] configAtt=c.split("=");
					if(configAtt!=null&&configAtt.length==2){
						if(configAtt[0].equals(PLUG_CONFIG_KEY_REPAIR)){
							repairStr=configAtt[1];
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(repairStr)&&repairStr.toLowerCase().equals("true")){
			isRepairPlug=true;
		}
		return isRepairPlug;
	}
	
	@SuppressWarnings("unchecked")
	private boolean isFileConflict(File file) throws IOException{
		ZipFile zip = new ZipFile(file, "GBK");
		ZipEntry entry;
		String name;
		String filename;
		File outFile;
		boolean fileConflict=false;
		Enumeration<ZipEntry> en = zip.getEntries();
		while (en.hasMoreElements()) {
			entry = en.nextElement();
			name = entry.getName();
			if (!entry.isDirectory()) {
				name = entry.getName();
				filename =  name;
				outFile = new File(realPathResolver.get(filename));
				if(outFile.exists()){
					fileConflict=true;
					break;
				}
			}
		}
		zip.close();
		return fileConflict;
	}
}
