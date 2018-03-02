package com.jeecms.bbs.api.member;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.ApiRecord;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.ApiRecordMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;
import com.jeecms.plug.live.entity.BbsLiveApply;
import com.jeecms.plug.live.manager.BbsLiveApplyMng;


@Controller
public class UserApplicationApiAct {

	private static final Logger log = LoggerFactory
			.getLogger(UserApplicationApiAct.class);

	/**
	 * 用户申请直播
	 */
	@RequestMapping(value = "/live/apply")
	public void applyLive(HttpServletRequest request,
			HttpServletResponse response) {
		String body = "\"\"";
		String message = "\"\"";
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			message = "\"user account not found\"";
			code = ResponseCode.API_CODE_USER_ACCOUNT_NOT_FOUND;
		} else {
			boolean haveApplied = liveApplyMng.haveApplied(user.getId());
			if (haveApplied) {
				message = "\"message info haveApplyHost\"";
				status = Constants.API_STATUS_FAIL;

			} else {
				message = Constants.API_STATUS_SUCCESS;
				status = Constants.API_STATUS_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);

	}

	/**
	 * 保存用户申请直播信息
	 * 
	 * @param intro介绍
	 * @param brief简介
	 * @param experience详细履历
	 * @param mobile手机号
	 * @param address地址
	 * @param picPaths申请附加图片地址
	 * @param picDescs
	 *            图片描述
	 * @param nextUrl
	 * @param sign
	 * @param appId
	 */
	@SignValidate
	@RequestMapping(value = "/live/apply/save")
	public void applySave(String intro, String brief, String experience,
			String mobile, String address, String[] picPaths,
			String[] picDescs, String nextUrl, String sign, String appId,
			HttpServletRequest request, HttpServletResponse response) {
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		BbsUser user = CmsUtils.getUser(request);
		errors = ApiValidate.validateRequiredParams(errors, sign, appId, intro,
				brief);
		if (!errors.hasErrors()) {
			errors = validateSendMsg(errors, false, user, intro, brief);
			if (errors.hasErrors()) {
				code = ResponseCode.API_CODE_PARAM_ERROR;
			}
			if (!errors.hasErrors()) {
				// 签名数据不可重复利用
				ApiRecord record = apiRecordMng.findBySign(sign, appId);
				if (record != null) {
					message = Constants.API_MESSAGE_REQUEST_REPEAT;
					code = ResponseCode.API_CODE_REQUEST_REPEAT;
				} else {
					BbsLiveApply liveApply = new BbsLiveApply();
					liveApply.setIntro(intro);
					liveApply.setBrief(brief);
					liveApply.setExperience(experience);
					liveApply.setMobile(mobile);
					liveApply.setAddress(address);
					liveApply.setApplyTime(Calendar.getInstance().getTime());
					liveApply.setStatus(BbsLiveApply.STATUS_CHECKING);
					liveApply.setApplyUser(user);
					liveApplyMng.save(liveApply, picPaths, picDescs);
					apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
							appId, "/live/apply/save", sign);
					body = "{\"id\":" + "\"" + liveApply.getId() + "\"}";
					log.info("save apply id={}", liveApply.getId());
					message = Constants.API_MESSAGE_SUCCESS;
					status = Constants.API_STATUS_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}
			} else {
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	private WebErrors validateSendMsg(WebErrors errors, boolean b,
			BbsUser user, String intro, String brief) {
		if (intro == null) {
			errors.addErrorString("\"intro not exit\"");
			return errors;
		}

		if (brief == null) {
			errors.addErrorString("\"brief not exit\"");
			return errors;
		}
		return errors;
	}

	@Autowired
	private BbsLiveApplyMng liveApplyMng;
	@Autowired
	private ApiRecordMng apiRecordMng;
}
