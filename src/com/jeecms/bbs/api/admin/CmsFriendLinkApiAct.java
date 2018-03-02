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

import com.jeecms.bbs.action.CmsFriendlinkAct;
import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.CmsFriendlink;
import com.jeecms.bbs.manager.CmsFriendlinkMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

@Controller
public class CmsFriendLinkApiAct {
	@Autowired
	private CmsFriendlinkMng manager;
	private static final Logger log = LoggerFactory
			.getLogger(CmsFriendlinkAct.class);
	
	/**
	 * 链接列表
	 * @param queryCtgId 按网站类别查询
	 * @param https
	 * @param pageNo 第pageNo页
	 * @param pageSize 每页总数
	 * @param response
	 * @param request
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/friendLink/list")
	public void friendLinkList(Integer queryCtgId,Integer https,Integer pageNo,Integer pageSize,
			HttpServletResponse response,HttpServletRequest request) throws JSONException{
		CmsSite site = CmsUtils.getSite(request);
		
		if(https==null){
			https=Constants.URL_HTTP;
		}
		Pagination page = manager.getPage(site.getId(), queryCtgId, null, pageNo, pageSize);
		List<CmsFriendlink> list = (List<CmsFriendlink>) page.getList();
		int totalCount = page.getTotalCount();
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
	 * 链接详情
	 * @param id 链接编号
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/friendLink/get")
	public void friendLinkGet(Integer id,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		CmsFriendlink link = null;
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (id!=null) {
			if (id.equals(0)) {
				link = new CmsFriendlink();
				link.init();
			}else{
				link = manager.findById(id);
			}
			if (link!=null) {
				JSONObject json = link.convertToJson(CmsUtils.getSiteId(request));
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
	 * 新增链接
	 * @param bean 链接对象
	 * @param ctgId 网站类别
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value= "/friendLink/save")
	public void save(CmsFriendlink  bean,Integer ctgId,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		bean.init();
		bean.setSite(CmsUtils.getSite(request));
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				bean.getName(),bean.getDomain(),ctgId);
		if (!errors.hasErrors()) {
			try {
				bean = manager.save(bean, ctgId);
				log.info("save CmsFriendlink id={}",bean.getId());
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
	 * 修改链接
	 * @param bean 链接对象
	 * @param ctgId 网站类别编号
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value= "/friendLink/update")
	public void update(CmsFriendlink  bean,Integer ctgId,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				bean.getId(),bean.getName(),bean.getDomain(),ctgId);
		if (!errors.hasErrors()) {
			try {
				bean = manager.update(bean, ctgId);
				log.info("update CmsFriendlink id={}",bean.getId());
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
	 * 删除链接
	 * @param ids 链接编号(多个链接编号用逗号隔开)
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/friendLink/delete")
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
				CmsFriendlink[] deleteByIds = manager.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete CmsFriendlink id={}",deleteByIds[i]);
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
	 * 批量保存链接排列顺序
	 * @param wids 链接编号
	 * @param priorities 排列顺序标识
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/friendLink/priority")
	public void priority(String wids,String priorities,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				wids,priorities);
		if (!errors.hasErrors()) {
			Integer[] widArray = StrUtils.getInts(wids);
			Integer[] priorityArray = StrUtils.getInts(priorities);
			manager.updatePriority(widArray, priorityArray);
			log.info("update CmsFriendlink priorities");
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
}
