package com.jeecms.bbs.api.admin;

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
import com.jeecms.bbs.entity.BbsMessage;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsMessageMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsMessageApiAct {
	private static final Logger log = LoggerFactory.getLogger(BbsMessageApiAct.class);
	@Autowired
	private BbsMessageMng bbsMessageMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	
	/**
	 * 系统消息列表
	 * @param pageNo 第pageNo页
	 * @param pageSize 每页总数
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/message/sys_list")
	public void messageSYSList(Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		Pagination pagination = bbsMessageMng.getPagination(true, null, null, null, null, null, null, pageNo, pageSize);
		List<BbsMessage> list = (List<BbsMessage>) pagination.getList();
		int totalCount = pagination.getTotalCount();
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
	 * 系统消息详情
	 * @param id 系统消息编号
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/message/get")
	public void messageGet(Integer id,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsMessage entity = null;
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if (id!=null) {
			if (id.equals(0)) {
				entity = new BbsMessage();
			}else{
				entity = bbsMessageMng.findById(id);
			}
			if (entity!=null) {
				JSONObject json = entity.convertToJson();
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
	 * 发送系统消息
	 * @param content 发送内容
	 * @param username 接收人
	 * @param groupId 会员组编号
	 * @param toAll 是否发送给所有人
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/message/sendSys")
	public void sendSys(String content,String username,Integer groupId,
			Boolean toAll,HttpServletResponse response,HttpServletRequest request){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		BbsUser user = CmsUtils.getUser(request);
		BbsUser receiverUser = null;
		BbsMessage sMsg = new BbsMessage();
		WebErrors errors = WebErrors.create(request);
		if (toAll) {
			errors = ApiValidate.validateRequiredParams(errors, content);
		}else{
			if (groupId==null) {
				errors = ApiValidate.validateRequiredParams(errors, content,username);
			}
			if (username==null) {
				errors = ApiValidate.validateRequiredParams(errors, content,groupId);
			}
		}
		if (!errors.hasErrors()) {
			sMsg.setContent(content);
			if (StringUtils.isNotBlank(username)) {
				receiverUser = bbsUserMng.findByUsername(username);
				if (receiverUser==null) {
					errors.addError("\"receiver user not found\"");
				}
			}
			if (!errors.hasErrors()) {
				sMsg = bbsMessageMng.sendSysMsg(user, receiverUser, groupId, toAll, sMsg);
				log.info("save BbsMessage id={}",sMsg.getId());
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = "{\"id\":"+"\""+sMsg.getId()+"\"}";
			}else{
				message = errors.getErrors().get(0);
				code = ResponseCode.API_CODE_USER_NOT_FOUND;
			}
			
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 系统消息删除
	 * @param ids 系统消息编号(多个系统消息编号用逗号隔开)
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/message/delete")
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
				BbsMessage[] deleteByIds = bbsMessageMng.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsMessage id={}",deleteByIds[i]);
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
