package com.jeecms.bbs.api.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeecms.bbs.entity.BbsWebservice;
import com.jeecms.bbs.manager.BbsWebserviceMng;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;
@Controller
public class BbsWebserviceApiAct {
	private static final Logger log = LoggerFactory.getLogger(BbsWebserviceApiAct.class);
	@Autowired
	private BbsWebserviceMng manager;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/webservice/list")
	public void list(Integer pageNo,Integer pageSize,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		Pagination page = manager.getPage(pageNo, pageSize);
		List<BbsWebservice> list = (List<BbsWebservice>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i<list.size() ; i++){
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
	
	@RequestMapping(value = "/webservice/get")
	public void get(Integer id,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsWebservice bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new BbsWebservice();
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
	@RequestMapping(value = "/webservice/save")
	public void save(BbsWebservice bean,String paramNames,String defaultValues,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getAddress(),
				bean.getType());
		if (!errors.hasErrors()) {
			String[] paramNameArray = paramNames.split(",");
			String[] defaultValueArray = defaultValues.split(",");
			bean = manager.save(bean, paramNameArray, defaultValueArray);
			log.info("save BbsWebservice id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping(value = "/webservice/update")
	public void update(BbsWebservice bean,String paramNames,String defaultValues,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getId(),
				bean.getAddress(),bean.getType());
		if (!errors.hasErrors()) {
			String[] paramArray = paramNames.split(",");
			String[] valueArray = defaultValues.split(",");
			bean = manager.update(bean, paramArray, valueArray);
			log.info("update BbsWebservice id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping(value = "/webservice/delete")
	public void delete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				BbsWebservice[] deleteByIds = manager.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsWebservice id={}",deleteByIds[i].getId());
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
}
