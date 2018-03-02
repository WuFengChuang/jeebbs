package com.jeecms.bbs.api.admin;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import com.jeecms.bbs.entity.BbsAdvertising;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsAdvertisingMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsAdvertisingApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsAdvertisingApiAct.class);
	@Autowired
	private BbsAdvertisingMng manager;
	@Autowired
	private BbsUserMng userMng;
	@Autowired
	private CmsConfigMng configMng;
	
	/**
	 * 广告列表
	 * @param queryChargeMode 按付费方式查询
	 * @param queryTitle 按关键词查询
	 * @param orderBy 排列顺序
	 * @param pageNo 第pageNo页
	 * @param pageSize 每页总数
	 * @param https
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/advertising/list")
	public void advertisingList(Integer queryChargeMode,String queryTitle,Integer orderBy,
			Integer pageNo,Integer pageSize,Integer https,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		CmsSite site = CmsUtils.getSite(request);
		Pagination page = manager.getPage(site.getId(), null, null, queryChargeMode, queryTitle, null, pageNo, pageSize);
		List<BbsAdvertising> list = (List<BbsAdvertising>) page.getList();
		int totalCount = page.getTotalCount();
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size() ; i++) {
				jsonArray.put(i,list.get(i).convertToJson(https));
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
	 * 广告消息
	 * @param id 广告编号
	 * @param https
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value="/advertising/get")
	public void advertisiongGet(Integer id,Integer https,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsAdvertising advertising = null;
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		if (id!=null) {
			if (id.equals(0)) {
				advertising = new BbsAdvertising();
				advertising.init();
			}else{
				advertising = manager.findById(id);
			}
			if (advertising!=null) {
				JSONObject json = advertising.convertToJson(https);
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
	 * 新增广告
	 * @param bean 广告对象
	 * @param adspaceId 版位编号
	 * @param chargeDay 广告天数
	 * @param startTime 开始时间
	 * @param username
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/advertising/save")
	public void save(BbsAdvertising bean,Integer adspaceId,Integer chargeDay,Date startTime,
			String username,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		bean.init();
		Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
		Set<String> toRemove = new HashSet<String>();
		attr = getAttr(attr,toRemove);
		if (StringUtils.isNotBlank(username)) {
			bean.setOwner(userMng.findByUsername(username));
		}
		bean.setSite(CmsUtils.getSite(request));
		errors = ApiValidate.validateRequiredParams(errors, bean.getName(),adspaceId,bean.getOwner()
				,bean.getCategory(),bean.getChargeMode());
		if (!errors.hasErrors()){
			if ("image".equals(bean.getCategory())) {
				errors = ApiValidate.validateRequiredParams(errors
						,attr.get("image_url"),attr.get("image_width")
						,attr.get("image_height"),attr.get("image_link")
						,attr.get("image_title"),attr.get("image_target")
						);
			}else if("flash".equals(bean.getCategory())){
				errors = ApiValidate.validateRequiredParams(errors
						,attr.get("flash_url"),attr.get("flash_width")
						,attr.get("flash_height"));
			}else if("text".equals(bean.getCategory())){
				errors = ApiValidate.validateRequiredParams(errors
						,attr.get("text_title"),attr.get("text_link")
						,attr.get("text_color"),attr.get("text_font")
						,attr.get("text_target"));
			}else{
				errors = ApiValidate.validateRequiredParams(errors
						,bean.getCode());
			}
		}
		if (!errors.hasErrors()) {
			if (bean.getChargeMode().equals(0)) {
				errors = ApiValidate.validateRequiredParams(errors, 
						chargeDay,startTime,bean.getChargeAmount());
			}
		}
		if (!errors.hasErrors()) {
			bean = manager.save(bean, adspaceId, chargeDay, startTime, attr);
			log.info("save BbsAdvertising id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 修改广告
	 * @param bean 广告对象
	 * @param chargeDay 广告天数
	 * @param startTime 开始时间
	 * @param adspaceId 版位编号
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/advertising/update")
	public void update(BbsAdvertising bean,Integer chargeDay,Date startTime,
			Integer adspaceId,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
		Set<String> toRemove = new HashSet<>();
		attr = getAttr(attr, toRemove);
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getId(),bean.getName()
				,bean.getCategory());
		if (!errors.hasErrors()){
			if ("image".equals(bean.getCategory())) {
				errors = ApiValidate.validateRequiredParams(errors
						,attr.get("image_url"),attr.get("image_width")
						,attr.get("image_height"),attr.get("image_link")
						,attr.get("image_title"),attr.get("image_target")
						);
			}else if("flash".equals(bean.getCategory())){
				errors = ApiValidate.validateRequiredParams(errors
						,attr.get("flash_url"),attr.get("flash_width")
						,attr.get("flash_height"));
			}else if("text".equals(bean.getCategory())){
				errors = ApiValidate.validateRequiredParams(errors
						,attr.get("text_title"),attr.get("text_link")
						,attr.get("text_color"),attr.get("text_font")
						,attr.get("text_target"));
			}else{
				errors = ApiValidate.validateRequiredParams(errors
						,bean.getCode());
			}
		}
		if (!errors.hasErrors()) {
			bean = manager.update(bean, adspaceId, chargeDay, startTime, attr);
			log.info("update BbsAdvertising id={}.", bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 删除广告
	 * @param ids 广告编号(多个编号用逗号隔开)
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/advertising/delete")
	public void delete(String ids,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try{
				Integer[] idArray = StrUtils.getInts(ids);
				BbsAdvertising[] deleteByIds = manager.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsAdvertising id={}",deleteByIds[i].getId());
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
	
	/**
	 * 计算广告费用
	 * @param chargeDay
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/advertising/get_charge_amount")
	public void getChargeAmount(Integer chargeDay,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, chargeDay);
		if (!errors.hasErrors()) {
			JSONObject json = new JSONObject();
			if (chargeDay<=0) {
				json.put("amount", 0);
			}else{
				CmsConfig config = configMng.get();
				Double adDayCharge = config.getAdDayCharge();
				json.put("amount", adDayCharge*chargeDay);
			}
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
			body = json.toString();
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 计算用户余额是否充足
	 * @param amount
	 * @param username
	 * @param response
	 * @param request
	 * @throws JSONException
	 */
	@RequestMapping(value = "/advertising/check_ad_amount")
	public void checkUserAdAmount(Double amount,String username,
			HttpServletResponse response,HttpServletRequest request) throws JSONException{
		BbsUser user = null;
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, amount,username);
		if (!errors.hasErrors()) {
			JSONObject json = new JSONObject();
			user = userMng.findByUsername(username);
			if (user!=null) {
				if (user.getAdAccountMount()>=amount) {
					json.put("status", "1");
				}else{
					json.put("userAdAmount", user.getAdAccountMount());
					json.put("status", "2");
				}
			}else{
				json.put("status", "-1");
			}
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
			body = json.toString();
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 去除空串的属性
	 * @param attr
	 * @param toRemove
	 * @return
	 */
	private Map<String, String> getAttr(Map<String, String> attr,Set<String> toRemove){
		for(Entry<String, String> entry:attr.entrySet()){
			if (StringUtils.isBlank(entry.getValue())) {
				toRemove.add(entry.getKey());
			}
		}
		for(String key : toRemove){
			attr.remove(key);
		}
		return attr;
	}
}
