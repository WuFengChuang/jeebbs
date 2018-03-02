package com.jeecms.bbs.api.admin;

import java.util.ArrayList;
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
import com.jeecms.bbs.entity.BbsTopicType;
import com.jeecms.bbs.manager.BbsTopicTypeMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsTopicTypeApiAct {
	private static final Logger log = LoggerFactory.getLogger(BbsTopicTypeApiAct.class);
	@Autowired
	private BbsTopicTypeMng manager;
	
	/**
	 * 主题分类列表
	 * @param root 父分类编号
	 * @param pageNo 第pageNo页
	 * @param pageSize 每页总数
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/topicType/list")
	public void topicTypeList(Integer root,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		List<BbsTopicType> list = new ArrayList<BbsTopicType>();
		int totalCount = 0;
		if (root==null) {
			Pagination page = manager.getTopPage(false, false, BbsTopicType.ORDER_PRIORITY_DESC, pageNo, pageSize);
			list = (List<BbsTopicType>) page.getList();
			totalCount = page.getTotalCount();
		}else{
			Pagination page = manager.getChildPage(root, false, false, BbsTopicType.ORDER_PRIORITY_DESC, pageNo, pageSize);
			list = (List<BbsTopicType>) page.getList();
			totalCount = page.getTotalCount();
		}
		JSONArray jsonArray = new JSONArray();
		if (list!=null && list.size()>0) {
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
	 * 主题分类详情
	 * @param id 主题编号
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value="/topicType/get")
	public void topicTypeGet(Integer id,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsTopicType topic = null;
		if (id!=null) {
			if (id.equals(0)) {
				topic = new BbsTopicType();
			}else{
				topic = manager.findById(id);
			}
			if (topic!=null) {
				JSONObject json = topic.convertToJson();
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
	 * 添加主题
	 * @param bean 主题对象
	 * @param root 父分类编号
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/topicType/save")
	public void save(BbsTopicType bean, Integer root ,
			HttpServletRequest request, HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if(root!=null){
			bean.setParent(manager.findById(root));
		}
		bean.init();
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				bean.getName(),bean.getPath(),bean.getPriority());
		if (!errors.hasErrors()) {
			bean.setSite(CmsUtils.getSite(request));
			bean = manager.save(bean);
			log.info("save BbsTopicType id={}", bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping(value = "/topicType/update")
	public void update(BbsTopicType bean, Integer root,
			HttpServletRequest request, HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				bean.getId(),bean.getName(),bean.getPath(),bean.getPriority());
		if (!errors.hasErrors()) {
			bean = manager.update(bean,root);
			log.info("update BbsTopicType id={}", bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping(value ="/topicType/delete")
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
				BbsTopicType[] deleteByIds = manager.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsTopicType id={}",deleteByIds[i]);
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
	
	@RequestMapping(value = "/topicType/tree")
	public void tree(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		List<BbsTopicType> list = manager.getTopList(false, true, BbsTopicType.ORDER_PRIORITY_DESC, null, null);
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i<list.size(); i++){
				JSONObject json = getJson(list.get(i),1);
				jsonArray.put(i,json);
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
	 * 递归获取树状数据
	 * @param topicType
	 * @return
	 * @throws JSONException
	 */
	private JSONObject getJson(BbsTopicType topicType,Integer count) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("id", topicType.getId());
		json.put("name", topicType.getName());
		json.put("count", count);
		count++;
		if (topicType.getChild()!=null&&topicType.getChild().size()>0) {
			List<BbsTopicType> childList = manager.getChildList(topicType.getId(), false, true, BbsTopicType.ORDER_PRIORITY_DESC, null, null);
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < childList.size(); i++) {
				JSONObject jsonChild = getJson(childList.get(i),count);
				jsonArray.put(i,jsonChild);
			}
			json.put("child", jsonArray);
		}else{
			json.put("child", "");
		}
		return json;
	}
}
