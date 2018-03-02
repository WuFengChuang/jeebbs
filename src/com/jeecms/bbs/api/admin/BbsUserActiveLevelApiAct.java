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
import com.jeecms.bbs.entity.BbsUserActiveLevel;
import com.jeecms.bbs.manager.BbsUserActiveLevelMng;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;


@Controller
public class BbsUserActiveLevelApiAct {
	private static final Logger log = LoggerFactory.getLogger(BbsUserActiveLevelApiAct.class);

	@Autowired
	private BbsUserActiveLevelMng manager;
	
	/**
	 * 活跃等级管理列表
	 * @param pageNo 第pageNo页
	 * @param pageSize 每页总数
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/activeLevel/list")
	public void activeLevelList(Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		Pagination page = manager.getPage(pageNo, pageSize);
		List<BbsUserActiveLevel> list = (List<BbsUserActiveLevel>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null && list.size()>0) {
			for (int i = 0; i < list.size() ; i++) {
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
	 * 活跃等级管理详情
	 * @param id 编号
	 * @param response
	 * @param request
	 * @throws JSONException
	 */
	@RequestMapping(value = "/activeLevel/get")
	public void activeLevelGet(Integer id,HttpServletResponse response,HttpServletRequest request) throws JSONException{
		BbsUserActiveLevel level = null;
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (id!=null) {
			if (id.equals(0)) {
				level = new BbsUserActiveLevel();
			}else{
				level = manager.findById(id);
			}
			if (level!=null) {
				JSONObject json = level.convertToJson();
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
	 * 添加活跃等级
	 * @param bean 活跃等级对象
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value= "/activeLevel/save")
	public void save(BbsUserActiveLevel bean,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				bean.getLevelName(),bean.getRequiredHour());
		if (!errors.hasErrors()) {
			try {
				bean = manager.save(bean);
				log.info("save BbsUserActiveLevel id={}",bean.getId());
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 活跃等级更新
	 * @param bean 活跃等级对象
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value= "/activeLevel/update")
	public void update(BbsUserActiveLevel bean,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				bean.getId(),bean.getLevelName(),bean.getRequiredHour());
		if (!errors.hasErrors()) {
			try {
				bean = manager.update(bean);
				log.info("update BbsUserActiveLevel id={}",bean.getId());
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = "{\"id\":"+"\""+bean.getId()+"\"}";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 活跃等级删除
	 * @param ids 活跃等级编号(多个编号用逗号隔开)
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value= "/activeLevel/delete")
	public void delete(String ids,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				BbsUserActiveLevel[] deleteByIds = manager.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsUserActiveLevel id={}",deleteByIds[i]);
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
