package com.jeecms.bbs.api.admin;

import java.util.Calendar;
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
import com.jeecms.bbs.entity.BbsReport;
import com.jeecms.bbs.entity.BbsReportExt;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsMessageMng;
import com.jeecms.bbs.manager.BbsReportExtMng;
import com.jeecms.bbs.manager.BbsReportMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsReportApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsReportApiAct.class);
	@Autowired
	private BbsReportMng manage;
	@Autowired
	private BbsUserMng userMng;
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private BbsMessageMng bbsMessageMng;
	@Autowired
	private BbsReportExtMng reportExtMng;
	
	/**
	 * 举报列表
	 * @param statu 举报状态
	 * @param pageNo 第pageNo页
	 * @param pageSize 每页总数
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/report/list")
	public void reportList(Boolean dealStatus,Integer pageNo,Integer pageSize ,Integer https,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		if (dealStatus == null) {
			dealStatus = false;
		}
		if (https==null) {
			https= Constants.URL_HTTP;
		}
		Pagination page = manage.getPage(dealStatus, pageNo, pageSize);
		List<BbsReport> list = (List<BbsReport>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null) {
			for(int i = 0 ; i < list.size() ; i++){
				jsonArray.put(i,list.get(i).convertToJson(https,CmsUtils.getSite(request)));
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount+",\"dealStatus\":"+dealStatus;
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 举报详情
	 * @param id 举报编号
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/report/get")
	public void reportGet(Integer id ,Integer https
			,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (https==null) {
			https=Constants.URL_HTTP;
		}
		BbsReport report = null;
		if (id!=null) {
			if (id.equals(0)) {
				report = new BbsReport();
			}else{
				report = manage.findById(id);
			}
			if (report!=null) {
				JSONObject json = report.convertToJson(https,CmsUtils.getSite(request));
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
	 * 删除举报
	 * @param ids 举报编号(多个编号用逗号隔开)
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/report/delete")
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
				BbsReport[] deleteByIds = manage.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsReport id={}",deleteByIds[i].getId());
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
	 * 处理举报-单个
	 * @param id 举报编号
	 * @param point 积分
	 * @param result 处理结果
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/report/process")
	public void process(Integer id,Integer point,String result,
			HttpServletResponse response,HttpServletRequest request){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		BbsReport report;
		BbsUser reportUser;
		BbsUser user = CmsUtils.getUser(request);
		Calendar calendar = Calendar.getInstance();
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				id,point,result);
		if (!errors.hasErrors()) {
			report = manage.findById(id);
			if (report!=null) {
				reportUser = report.getReportExt().getReportUser();
				userMng.updatePoint(reportUser.getId(), point, null, null, 0, -1);
				log.info("update BbsUser id={}",reportUser.getId());
				report.setProcessResult(result);
				report.setProcessTime(calendar.getTime());
				report.setProcessUser(user);
				report.setStatus(true);
				manage.update(report);
				log.info("update BbsReport id={}",report.getId());
				CmsConfig config = cmsConfigMng.get();
				if (config.getConfigAttr().getReportMsgAuto()&&
						StringUtils.isNotBlank(config.getConfigAttr().getReportMsgTxt())) {
					BbsMessage sMsg = new BbsMessage();
					sMsg.setContent(config.getConfigAttr().getReportMsgTxt());
					bbsMessageMng.sendSysMsg(null, reportUser, null, null, sMsg);
				}
			}
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/report/more")
	public void more(Integer reportId,Integer pageNo,Integer pageSize,
			HttpServletResponse response,HttpServletRequest request) throws JSONException{
		Pagination page = reportExtMng.getPage(reportId, pageNo, pageSize);
		List<BbsReportExt> list = (List<BbsReportExt>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i=0;i<list.size();i++){
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
	
	@SignValidate
	@RequestMapping(value = "/report/ext_delete")
	public void extDelete(String ids,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				BbsReportExt[] deleteByIds = reportExtMng.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsReportExt id={}",deleteByIds[i].getId());
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
