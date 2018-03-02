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
import com.jeecms.bbs.entity.BbsGift;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsGiftMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsGiftApiAct {
	private static final Logger log = LoggerFactory.getLogger(BbsGiftApiAct.class);
	@Autowired
	private BbsGiftMng manager;
	/**
	 * 礼物列表
	 * @param pageNo 第pageNo页
	 * @param pageSize 每页总数
	 * @param https
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gift/list")
	public void giftList(Integer pageNo,Integer pageSize,Integer https,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		Pagination page = manager.getPage(pageNo, pageSize);
		List<BbsGift> list = (List<BbsGift>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		BbsUser user = CmsUtils.getUser(request);
		CmsSite site = CmsUtils.getSite(request);
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson(site, https, user));
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
	 * 礼物详情
	 * @param id 礼物编号
	 * @param https
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/gift/get")
	public void giftGet(Integer id,Integer https,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsGift gift = null;
		BbsUser user = CmsUtils.getUser(request);
		CmsSite site = CmsUtils.getSite(request);
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		if (id!=null) {
			if (id.equals(0)) {
				gift = new BbsGift();
				gift.init();
			}else{
				gift = manager.findById(id);
			}
			if (gift!=null) {
				JSONObject json = gift.convertToJson(site, https, user);
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
	 * 添加礼物
	 * @param bean 礼物对象
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value ="/gift/save")
	public void save(BbsGift bean,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		bean.init();
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				bean.getName(),bean.getPrice(),bean.getPriority());
		if (!errors.hasErrors()) {
			bean = manager.save(bean);
			log.info("save BbsGift id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 修改礼物
	 * @param bean 礼物对象
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/gift/update")
	public void update(BbsGift bean,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				bean.getId(),bean.getName(),bean.getPrice(),bean.getPriority());
		if (!errors.hasErrors()) {
			bean = manager.update(bean);
			log.info("update BbsGift id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 删除礼物
	 * @param ids 礼物编号(多个编号用逗号隔开)
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/gift/delete")
	public void delete(String ids,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				BbsGift[] deleteByIds = manager.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsGift id={}",deleteByIds[i].getId());
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
	
	@RequestMapping(value = "/gift/priority")
	public void priority(String ids,String priorities,String disableds,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids,priorities,disableds);
		if (!errors.hasErrors()) {
			Integer[] idArray = StrUtils.getInts(ids);
			Integer[] prioritiyArray = StrUtils.getInts(priorities);
			Boolean[] disabledArray = strToBoolean(disableds);
			manager.updatePriority(idArray,prioritiyArray,disabledArray);
			log.info("update BbsGift update prioritiy");
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private Boolean[] strToBoolean(String str){
		Boolean[] booleans = null;
		String[] split = str.split(",");
		if (split!=null&&split.length>0) {
			booleans = new Boolean[split.length];
			for (int i = 0; i < split.length; i++) {
				booleans[i] = Boolean.parseBoolean(split[i]);
			}
		}
		return booleans;
	}
}
