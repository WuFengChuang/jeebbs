package com.jeecms.bbs.api.admin;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class UploadApiAct {
	
	/**
	 * 文件上传API
	 * @param type 上传类型  图片image  附件attach 视频video 必选
	 * @param uploadFile 文件 必选
	 */
	//value= method = {RequestMethod.GET,RequestMethod.POST,RequestMethod.OPTIONS}
	@RequestMapping("/upload/o_upload")
	public void upload(String type,
			@RequestParam(value = "uploadFile", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) {
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		CmsSite site=CmsUtils.getSite(request);
		if(StringUtils.isBlank(type)){
			type="image";
		}
		WebErrors errors = validate(type,file, request);
		if(errors.hasErrors()){
			code=ResponseCode.API_CODE_PARAM_ERROR;
		}else{
			//验证公共非空参数
			errors=ApiValidate.validateRequiredParams(errors,file);
			if(!errors.hasErrors()){
				Integer siteId=CmsUtils.getSiteId(request);
				String origName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(origName).toLowerCase(
						Locale.ENGLISH);
				String filepath = "";
				try {
					filepath = fileRepository.storeByExt(siteMng.findById(
							siteId).getUploadPath(), ext, file);
					if(StringUtils.isNotBlank(site.getContextPath())){
						filepath=site.getContextPath()+filepath;
					}
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
	
	private WebErrors validate(String type,MultipartFile file,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (file == null) {
			errors.addErrorString("imageupload error noFileToUpload");
			return errors;
		}
		if(type==null){
			errors.addErrorString("upload error noSelectType");
			return errors;
		}
		if(!type.equals("image")&&!type.equals("attach")&&!type.equals("video")){
			errors.addErrorString("upload error noSupportType");
			return errors;
		}
		String filename=file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(filename);
		if(filename!=null&&(filename.contains("/")||filename.contains("\\")||filename.indexOf("\0")!=-1)){
			errors.addErrorString("upload error filename");
			return errors;
		}
		if(type.equals("image")){
			//图片校验
			if (!ImageUtils.isValidImageExt(ext)) {
				errors.addErrorString("imageupload error notSupportExt");
				return errors;
			}
			try {
				if (!ImageUtils.isImage(file.getInputStream())) {
					errors.addErrorString("imageupload error notImage");
					return errors;
				}
			} catch (IOException e) {
				errors.addErrorString("imageupload error ioError");
				return errors;
			}
		}
		return errors;
	}
	
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private CmsSiteMng siteMng;
}

