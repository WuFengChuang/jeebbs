package com.jeecms.bbs.api.front;

import java.util.Calendar;
import java.util.Date;
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
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.common.web.ResponseUtils;

import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.manager.BbsLiveMng;

@Controller
public class BbsLiveApiAct {

	private static final Logger log = LoggerFactory
			.getLogger(BbsLiveApiAct.class);

	/**
	 * 直播列表
	 * 
	 * @param orderBy
	 *            1置顶级别降序 2日浏览量降序 3周浏览量降序 4月浏览量降序 5总浏览量降序 6回复量降序 7日回复量降序 8id降序
	 *            默认8
	 * @param status
	 *            status 0未开始 1进行中 2已结束
	 * @param pageNo
	 *            非必选 默认1
	 * @param pageSize
	 *            非必选 默认10
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/live/list")
	public void liveList(Integer orderBy, Integer status, Integer pageNo,
			Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status1 = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		Date now = Calendar.getInstance().getTime();
		Date queryStartBegin = null, queryStartEnd = null, queryFinishBegin = null, queryFinishEnd = null;
		if (status != null) {
			if (status.equals(0)) {
				queryStartBegin = now;
			} else if (status.equals(1)) {
				queryFinishBegin = now;
				queryStartEnd = now;
			} else if (status.equals(2)) {
				queryFinishEnd = now;
			}
		}
		List<BbsLive> list = liveMng.getList(null, null, null,
				status.shortValue(), queryStartBegin, queryStartEnd,
				queryFinishBegin, queryFinishEnd, orderBy, pageNo, pageSize);
		JSONArray jsonArray = new JSONArray();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i, convertToJson(list.get(i)));
				status1 = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = jsonArray.toString();
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status1, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);

	}

	/**
	 * 直播获取
	 * 
	 * @param https
	 *            地址返回格式 非必选 0https 1http 默认1
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/live/get")
	public void liveGet(Integer https, Integer id, HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		if (https == null) {
			https = Constants.URL_HTTP;
		}
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (id != null) {
			BbsLive live = liveMng.findById(id);
			if (live != null) {
				if (live.getCheckStatus().equals(BbsLive.CHECKING)) {
					message = Constants.API_MESSAGE_LIVE_ERROR_CHECKING;
					status = Constants.API_STATUS_FAIL;
				} else if (live.getCheckStatus().equals(BbsLive.REJECT)) {
					message = Constants.API_MESSAGE_LIVE_ERROR_REJECT;
					status = Constants.API_STATUS_FAIL;
				} else if (live.getCheckStatus().equals(BbsLive.STOP)) {
					message = Constants.API_MESSAGE_LIVE_ERROR_STOP;
					status = Constants.API_STATUS_FAIL;
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", live.getId());
				jsonObject.put("userId", live.getUser().getId());// 主讲人id
				jsonObject.put("beginPrice", live.getBeginPrice());// 价格
				jsonObject.put("title", live.getTitle());// 标题
				jsonObject.put("username", live.getUser().getUsername());// 主讲人
				jsonObject.put("joinUserNum", live.getJoinUserNum());// 参与人数
				jsonObject.put("description", live.getDescription());// 简介
				jsonObject.put("beginTime", live.getBeginTime());// 开始时间
				jsonObject.put("endTime", live.getEndTime());// 结束时间
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = jsonObject.toString();
			} else {
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				status = Constants.API_STATUS_FAIL;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);

	}

	private JSONObject convertToJson(BbsLive live) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("title", live.getTitle());// 标题
		jsonObject.put("username", live.getUser().getUsername());// 主讲人
		jsonObject.put("joinUserNum", live.getJoinUserNum());// 参与人数
		jsonObject.put("beginTime", live.getBeginTime());// 开始时间
		jsonObject.put("beginPrice", live.getBeginPrice());// 未开始价格

		return jsonObject;

	}

	@Autowired
	private BbsLiveMng liveMng;

}