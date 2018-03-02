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
import com.jeecms.bbs.entity.CmsFriendlinkCtg;
import com.jeecms.bbs.manager.CmsFriendlinkCtgMng;
import com.jeecms.bbs.manager.CmsFriendlinkMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class CmsFriendLinkCtgApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(CmsFriendLinkCtgApiAct.class);
	@Autowired
	private CmsFriendlinkCtgMng manager;
	@Autowired
	private CmsFriendlinkMng cmsFriendlinkMng;
	
	/**
	 * 链接类别列表
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/friendLinkCtg/list")
	public void ctgList(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		List<CmsFriendlinkCtg> list = manager.getList(CmsUtils.getSiteId(request));
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size() ; i++) {
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 链接类别详情
	 * @param id 链接类别编号
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/friendLinkCtg/get")
	public void ctgGet(Integer id,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		CmsFriendlinkCtg bean = null;
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (id!=null) {
			if (id.equals(0)) {
				bean = new CmsFriendlinkCtg();
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
	 * 添加链接类别
	 * @param bean 链接类别对象
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/friendLinkCtg/save")
	public void save(CmsFriendlinkCtg bean ,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				bean.getName(),bean.getPriority());
		if (!errors.hasErrors()) {
			bean.setSite(CmsUtils.getSite(request));
			bean = manager.save(bean);
			log.info("save CmsFriendlinkCtg id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 修改链接类别
	 * @param wids 链接类别编号
	 * @param names 名称
	 * @param priorities 排列顺序
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/friendLinkCtg/update")
	public void update(String wids,String names,String priorities,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				wids,names,priorities);
		if (!errors.hasErrors()) {
			Integer[] widArray = StrUtils.getInts(wids);
			String[] nameArray = names.split(",");
			Integer[] priorityArray = StrUtils.getInts(priorities);
			manager.updateFriendlinkCtgs(widArray, nameArray, priorityArray);
			log.info("update CmsFriendlinkCtg priority");
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 删除链接类别
	 * @param ids 链接类别编号
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/friendLinkCtg/delete")
	public void update(String ids,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				ids);
		Integer[] idArray = StrUtils.getInts(ids);
		if (!errors.hasErrors()) {
			if(canDelete(idArray)){
				try {
					CmsFriendlinkCtg[] deleteByIds = manager.deleteByIds(idArray);
					for (int i = 0; i < deleteByIds.length; i++) {
						log.info("delete CmsFriendlinkCtg id={}",deleteByIds[i].getId());
					}
					status=Constants.API_STATUS_SUCCESS;
					message=Constants.API_MESSAGE_SUCCESS;
					code=ResponseCode.API_CODE_CALL_SUCCESS;
				} catch (Exception e) {
					status = Constants.API_MESSAGE_DELETE_ERROR;
					message = Constants.API_MESSAGE_DELETE_ERROR;
					code = ResponseCode.API_CODE_DELETE_ERROR;
				}
			}else{
				status = Constants.API_MESSAGE_DELETE_ERROR;
				message = Constants.API_MESSAGE_DELETE_ERROR;
				code = ResponseCode.API_CODE_DELETE_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private boolean canDelete(Integer[] idArray){
		for(int i = 0 ; i < idArray.length ; i++){
			if (cmsFriendlinkMng.countByCtgId(idArray[i])>0) {
				return false;
			}
		}
		return true;
	}
}
