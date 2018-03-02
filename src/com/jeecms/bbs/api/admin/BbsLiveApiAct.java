package com.jeecms.bbs.api.admin;

import java.util.Calendar;
import java.util.Date;
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
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.util.BaiduCloudUtil;
import com.jeecms.plug.live.util.TencentCloudUtil;
import com.jeecms.plug.live.websocket.WebSocketExtHandler;

@Controller
public class BbsLiveApiAct {
	private static final Logger log = LoggerFactory.getLogger(BbsLiveApiAct.class);
	
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsLiveMng manager;
	@Autowired
	private WebSocketExtHandler webSocketExtHandler;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/live/list")
	public void list(String qTitle,String qUsername,Short qStatus,
			Integer qOrderBy,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		Integer hostUserId = null;
		Date qTimeBegin=null;
		Date qTimeEnd = null;
		Date qFinishTimeBegin=null;
		Date qFinishTimeEnd = null;
		if (StringUtils.isNotBlank(qUsername)) {
			BbsUser user = bbsUserMng.findByUsername(qUsername);
			if (user!=null) {
				hostUserId = user.getId();
			}else{
				hostUserId =0;
			}
		}
		Date now = Calendar.getInstance().getTime();
		Short queryDbStatus = qStatus;
		if (qStatus!=null) {
			if (qStatus==1) {
				//未开始
				qTimeBegin = now;
				queryDbStatus = BbsLive.CHECKED;
			}else if (qStatus==2) {
				//进行中
				qFinishTimeBegin = now;
				qTimeEnd = now;
				queryDbStatus = BbsLive.CHECKED;
			}else if (qStatus==3) {
				//已结束
				qFinishTimeEnd = now;
				queryDbStatus = BbsLive.CHECKED;
			}else if (qStatus==4) {
				//已拒绝
				queryDbStatus = BbsLive.REJECT;
			}else if (qStatus==5) {
				//已关闭
				queryDbStatus=BbsLive.STOP;
			}
		}else{
			qStatus = BbsLive.CHECKING;
			queryDbStatus = BbsLive.CHECKING;
		}
		Pagination page = manager.getPage(null, qTitle, hostUserId, queryDbStatus, qTimeBegin, 
				qTimeEnd, qFinishTimeBegin, qFinishTimeEnd, qOrderBy, pageNo, 
				pageSize);
		List<BbsLive> list = (List<BbsLive>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i=0; i<list.size();i++){
				jsonArray.put(i,list.get(i).convertToJson(qStatus));
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/live/get")
	public void get(Integer id,Short qStatus,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsLive bean = null;
		if (id!=null) {
			if (id.equals(0)) {
				bean = new BbsLive();
			}else{
				bean = manager.findById(id);
			}
			if (bean!=null) {
				JSONObject json = bean.convertToJson(qStatus);
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
	@RequestMapping(value = "/live/update")
	public void update(BbsLive bean,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, bean.getId(),bean.getTitle(),bean.getBeginPrice());
		if (!errors.hasErrors()) {
//			bean.setCheckStatus(BbsLive.CHECKED);
			bean = manager.update(bean);
			log.info("update BbsUser id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/live/stop")
	public void stop(String ids,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			CmsSite site = CmsUtils.getSite(request);
			CmsConfig config = site.getConfig();
			Integer[] idArray = StrUtils.getInts(ids);
			Date queryStartBegin=Calendar.getInstance().getTime();
			for (Integer id : idArray) {
				BbsLive live = manager.findById(id);
				live.setCheckStatus(BbsLive.STOP);
				manager.update(live);
				BbsUser host = live.getUser();
				//关闭主播的未开始直播
				List<BbsLive>unBeginLives=manager.getList(
						null, null, host.getId(),BbsLive.CHECKED,
						queryStartBegin, null, null, null, 1, 0, 100);
				for(BbsLive l:unBeginLives){
					l.setCheckStatus(BbsLive.STOP);
					manager.update(l);
				}
				if (live.getLivePlat().equals(BbsLive.LIVE_PLAT_TENCENT)) {
					String streamId = config.getTencentBizId()+"_"+live.getUser().getId();
					TencentCloudUtil.disableStram(config.getTencentApiAuthKey(), config.getTencentAppId(), streamId);
				}
				else if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_BAIDU)){
					try {
						BaiduCloudUtil.disableStram(config.getBaiduAccessKeyId(),
								config.getBaiduSecretAccessKey(),
								config.getBaiduPlayDomain(), 
								live.getUser().getId().toString());
					} catch (Exception e) {
						log.info(e.getMessage());
					}
				}
				webSocketExtHandler.closeLiveWebSocketSession(id);
				log.info("stop BbsLive id={}",live.getId());
			}
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/live/start")
	public void start(String ids,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			CmsSite site = CmsUtils.getSite(request);
			CmsConfig config = site.getConfig();
			Integer[] idArray = StrUtils.getInts(ids);
			Date queryStartBegin=Calendar.getInstance().getTime();
			for (Integer id : idArray) {
				BbsLive live = manager.findById(id);
				live.setCheckStatus(BbsLive.CHECKED);
				manager.update(live);
				BbsUser host = live.getUser();
				//重新启用未开始直播
				List<BbsLive>unBeginLives=manager.getList(
						null, null, host.getId(),BbsLive.STOP,
						queryStartBegin, null, null, null, 1, 0, 100);
				for(BbsLive l:unBeginLives){
					l.setCheckStatus(BbsLive.CHECKED);
					manager.update(l);
				}
				if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_TENCENT)){
					String streamId=config.getTencentBizId()+"_"+live.getUser().getId();
					TencentCloudUtil.connectStram(config.getTencentApiAuthKey()
							,config.getTencentAppId(), streamId);
				}else if (live.getLivePlat().equals(BbsLive.LIVE_PLAT_BAIDU)) {
					try {
						BaiduCloudUtil.connectStram(config.getBaiduAccessKeyId(),
								config.getBaiduSecretAccessKey(),
								config.getBaiduPlayDomain(), 
								live.getUser().getId().toString());
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
				log.info("start BbsLive id={}.", live.getId());
			}
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SignValidate
	@RequestMapping(value = "/live/delete")
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
				BbsLive[] deleteByIds = manager.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsLive id={}",deleteByIds[i].getId());
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
	
	@RequestMapping(value = "/live/check")
	public void check(String ids,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			Integer[] idArray = StrUtils.getInts(ids);
			for (Integer id : idArray) {
				BbsLive live = manager.findById(id);
				live.setCheckStatus(BbsLive.CHECKED);
				manager.update(live);
				log.info("check BbsLive id={}",live.getId());
			}
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/live/reject")
	public void reject(String ids,String reason,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids,reason);
		if (!errors.hasErrors()) {
			Integer[] idArray = StrUtils.getInts(ids);
			for (Integer id : idArray) {
				BbsLive live = manager.findById(id);
				live.setReason(reason);
				live.setCheckStatus(BbsLive.REJECT);
				manager.update(live);
				log.info("reject BbsLive id={}",live.getId());
			}
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
			code=ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
}
