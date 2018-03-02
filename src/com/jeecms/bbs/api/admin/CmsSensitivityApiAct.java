package com.jeecms.bbs.api.admin;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.CmsSensitivity;
import com.jeecms.bbs.manager.CmsSensitivityMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class CmsSensitivityApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsSensitivityApiAct.class);
	@Autowired
	private CmsSensitivityMng manager;
	
	/**
	 * 敏感词列表
	 * @param pageNo 第pageNo页
	 * @param pageSize 每页总数
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/sensitivity/list")
	public void sensitivityList(Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		Integer siteId = CmsUtils.getSiteId(request);
		Pagination page = manager.getPage(pageNo, pageSize, siteId, true);
		List<CmsSensitivity> list = (List<CmsSensitivity>) page.getList();
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
	
	/**
	 * 敏感词详情获取
	 * @param id 敏感词编号
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/sensitivity/get")
	public void sensitivityGet(Integer id,
			HttpServletRequest request , HttpServletResponse response) throws JSONException{
		CmsSensitivity bean = null;
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsSensitivity();
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
	
	/**
	 * 敏感词添加
	 * @param bean 敏感词对象
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value="/sensitivity/save")
	public void save(CmsSensitivity bean,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				bean.getReplacement(),bean.getSearch());
		Integer siteId = CmsUtils.getSiteId(request);
		if (!errors.hasErrors()) {
			bean = manager.save(bean, siteId);
			log.info("save CmsSensitivity id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 敏感词批量添加
	 * @param words 批量输入(敏感词：替换词,...,敏感词：替换词)
	 * @param type 替换类型
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/sensitivity/batch_save")
	public void batchSave(String words,Integer type,
			HttpServletRequest request,HttpServletResponse response){
		Integer siteId = CmsUtils.getSiteId(request);
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				words,type);
		if (errors!=null) {
			words = words.replace(":", "=");//字符串中:替换成=
			BufferedReader br = new BufferedReader(new StringReader(words));
			List<String> list = new ArrayList<>();
			String line = "";
			try {
				while((line = br.readLine())!=null){
					if (StringUtils.isNotBlank(line)) {
						list.add(line);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			manager.batchSave(list, siteId, type);
			log.info("batch save CmsSensitivity.");
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 敏感词批量更新
	 * @param ids 敏感词编号(多个编号用逗号隔开)
	 * @param searchs 铭感词(多个敏感词用逗号隔开)
	 * @param replacements 替换词(多个替换词用逗号隔开)
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/sensitivity/batch_update")
	public void batchUpdate(String ids,String searchs,String replacements,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				ids,searchs,replacements);
		if (!errors.hasErrors()) {
			//三个数组的长度必须相同，且数组中每一个元素都不能为空
			Integer[] idArray = StrUtils.getInts(ids);
			String[] searchArray = searchs.split(",");
			String[] replacementArray = replacements.split(",");
			manager.updateEnsitivity(idArray, searchArray, replacementArray);
			log.info("batch update CmsSensitivity.");
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

//	@RequestMapping(value = "/sensitivity/update")
//	public void update(CmsSensitivity bean,
//			HttpServletRequest request,HttpServletResponse response){
//		String body="\"\"";
//		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
//		String status = Constants.API_STATUS_FAIL;
//		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
//		WebErrors errors = WebErrors.create(request);
//		errors = ApiValidate.validateRequiredParams(errors, 
//				bean.getId(),bean.getSearch(),bean.getReplacement());
//		if (!errors.hasErrors()) {
//			bean = manager.update(bean);
//			log.info("update CmsSensitivity id{}",bean.getId());
//			status = Constants.API_STATUS_SUCCESS;
//			message = Constants.API_MESSAGE_SUCCESS;
//			code = ResponseCode.API_CODE_CALL_SUCCESS;
//			body = "{\"id\":"+"\""+bean.getId()+"\"}";
//		}
//		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
//		ResponseUtils.renderApiJson(response, request, apiResponse);
//	}
	
	/**
	 * 敏感词批量删除
	 * @param ids 敏感词编号(多个编号用逗号隔开)
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/sensitivity/delete")
	public void date(String ids,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				ids);
		if (!errors.hasErrors()) {
			Integer[] idArray = StrUtils.getInts(ids);
			CmsSensitivity[] deleteByIds = manager.deleteByIds(idArray);
			for (int i = 0; i < deleteByIds.length; i++) {
				log.info("update CmsSensitivity id{}",deleteByIds[i].getId());
			}
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
}
