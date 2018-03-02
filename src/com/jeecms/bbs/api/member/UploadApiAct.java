package com.jeecms.bbs.api.member;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.ApiRecord;
import com.jeecms.bbs.entity.Attachment;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsPostText;
import com.jeecms.bbs.manager.ApiRecordMng;
import com.jeecms.bbs.manager.AttachmentMng;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.image.ImageScale;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class UploadApiAct {
	/**
	 * 用户头像路径
	 */
	private static final String USER_FILE_PATH = "/user/upload";
	
	/**
	 * 文件上传API
	 * @param type 上传类型  图片image  附件attach 必选
	 * @param uploadFile 文件 必选
	 * @param code 文件占位符 必选
	 * @param postId 帖子ID  必选
	 * @param appId appid 必选
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 */
	@RequestMapping("/upload/o_upload")
	public void upload(String type,
			@RequestParam(value = "uploadFile", required = false) MultipartFile file,
			String code,Integer postId,String appId,String sign,
			HttpServletRequest request, HttpServletResponse response) {
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String rescode=ResponseCode.API_CODE_CALL_SUCCESS;
		WebErrors errors =validate(type, file, request);
		if (errors.hasErrors()) {
			rescode=ResponseCode.API_CODE_UPLOAD_ERROR;
		}else{
			errors = ApiValidate.validateRequiredParams(errors, code,postId,file);
			if (!errors.hasErrors()) {
				BbsPost post = bbsPostMng.findById(postId);
				if (post!=null) {
					//签名数据不可重复利用
					ApiRecord record=apiRecordMng.findBySign(sign, appId);
					if(record!=null){
						message=Constants.API_MESSAGE_REQUEST_REPEAT;
						rescode=ResponseCode.API_CODE_REQUEST_REPEAT;
					}else{
						Integer siteId=CmsUtils.getSiteId(request);
						String origName = file.getOriginalFilename();
						String ext = FilenameUtils.getExtension(origName).toLowerCase(
								Locale.ENGLISH);
						String filepath,picZoomPath = null;
						try {
							filepath = fileRepository.storeByExt(siteMng.findById(
									siteId).getUploadPath(), ext, file);
							//图片生成缩率图
							if(ImageUtils.isValidImageExt(ext)){
								picZoomPath = fileRepository.storeByExt(siteMng.findById(
										siteId).getUploadPath(), ext, fileRepository.retrieve(filepath));
								File picFile = fileRepository.retrieve(picZoomPath);
								BufferedImage bufferedImage = ImageIO.read(picFile);   
								int picWidth = bufferedImage.getWidth();   
								int picHeight = bufferedImage.getHeight();
								int reWidth=bbsConfigMng.get().getPicZoomDefWidth();
								int reHeight = (reWidth * picHeight) / picWidth; //高度等比缩放 
								try {
									imageScale.resizeFix(picFile, picFile, reWidth, reHeight);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							Attachment att = attachmentMng.addAttachment(origName, filepath,
									picZoomPath, post,file.getSize());
							//更改帖子内容
							BbsPostText postTxt=post.getPostText();
							String content=postTxt.getContent();
							content=StrUtils.replace(content, code, "[attachment]" + att.getId()
									+ "[/attachment]");
							postTxt.setContent(content);
							post.setPostText(postTxt);
							post.setAffix(true);
							bbsPostMng.update(post);
							body="{\"attachId\":"+"\""+att.getId()+"\"}";
						} catch (IOException e) {
							e.printStackTrace();
						}
						apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
								appId, "/upload/o_upload",sign);
						message=Constants.API_MESSAGE_SUCCESS;
						status=Constants.API_STATUS_SUCCESS;
						rescode = ResponseCode.API_CODE_CALL_SUCCESS;
					}
				}else{
					message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
					rescode=ResponseCode.API_CODE_NOT_FOUND;
				}
			}else{
				 message = Constants.API_MESSAGE_PARAM_REQUIRED;
				 rescode = ResponseCode.API_CODE_PARAM_REQUIRED;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,rescode);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 普通文件上传
	 * @param filename
	 * @param file
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/upload/file_upload")
	public void fileUpload(@RequestParam(value = "uploadFile", required = false) MultipartFile file,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		String filename = "";
		if (file==null) {
			errors.addError(Constants.API_MESSAGE_FILE_NOT_FOUNT);
			code = ResponseCode.API_CODE_FILE_NOT_FOUNT;
		}else{
			filename = file.getOriginalFilename();
			if(filename==null||
					filename.contains("/")
					||filename.contains("\\")
					||filename.indexOf("\0")!=-1){//判断文件名是否包含特殊字符
				errors.addError("\"filename error\"");
				code = ResponseCode.API_CODE_FILENAME_ERROR;
			}
		}
		if (errors.hasErrors()) {
			message = errors.getErrors().get(0);
		}else{
			try {
				String ext = FilenameUtils.getExtension(filename).toLowerCase(
						Locale.ENGLISH);
				String fileUrl;
				String ctx = request.getContextPath();
				if (!StringUtils.isBlank(filename)) {
					filename = filename.substring(ctx.length());
					fileUrl = fileRepository.storeByFilename(filename, file);
				} else {
					fileUrl = fileRepository.storeByExt(USER_FILE_PATH, ext, file);
					// 加上部署路径
					fileUrl = ctx + fileUrl;
				}
			} catch (Exception e) {
				message = Constants.API_MESSAGE_UPLOAD_ERROR;
				code = ResponseCode.API_CODE_UPLOAD_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
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
		if(!type.equals("image")&&!type.equals("attach")){
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
	private ImageScale imageScale;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private ApiRecordMng apiRecordMng;
	@Autowired
	private CmsSiteMng siteMng;
	@Autowired
	private AttachmentMng attachmentMng;
	@Autowired
	private BbsPostMng bbsPostMng;
}
