package com.jeecms.bbs.api.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.entity.BbsLiveUser;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.manager.BbsLiveUserMng;

@Controller
public class LiveCustomerorderApiAct {

	private static final Logger log = LoggerFactory
			.getLogger(LiveCustomerorderApiAct.class);

	/**
	 * 直播-用户订单列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/customerOrder/list")
	public void customerOrder(Integer pageNo, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		BbsUser user = CmsUtils.getUser(request);
		if (user != null) {
			Pagination page = orderMng.getPage(null, user.getId(), null, null,
					BbsOrder.ORDER_TYPE_LIVE, null, pageNo, pageSize);
			List<BbsOrder> li = (List<BbsOrder>) page.getList();
			Map<String, BbsLive> liveMap = new HashMap<String, BbsLive>();
			for (BbsOrder order : li) {
				liveMap.put(order.getDataId().toString(),
						liveMng.findById(order.getDataId()));
			}
			JSONArray jsonArray = new JSONArray();
			if (li != null && li.size() > 0) {
				for (int i = 0; i < li.size(); i++) {
					jsonArray.put(i, convertToJson(li.get(i), liveMap));

				}
			}
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = jsonArray.toString();
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 用户订单门票分配列表
	 * 
	 * @param orderId
	 *            订单id
	 * @param https
	 *            url返回格式 1 http格式 0 https格式
	 * @param pageNo
	 * @param pageSize
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/customerOrder/ticketAssignList")
	public void ticketList(Long orderId, Integer https, Integer pageNo,
			Integer pageSize, HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsUser user = CmsUtils.getUser(request);
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		if (https == null) {
			https = Constants.URL_HTTP;
		}
		if (user != null) {
			BbsOrder order = orderMng.findById(orderId);
			if (order != null && order.getBuyUser().equals(user)) {
				Pagination page = liveUserMng.getPage(orderId, null, null,
						pageNo, pageSize);

				List<BbsLiveUser> list = (List<BbsLiveUser>) page.getList();
				int totalCount = page.getTotalCount();
				JSONArray jsonArray = new JSONArray();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						jsonArray.put(i, convertToJson1(list.get(i), order));
					}
				}
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = jsonArray.toString() + ",\"totalCount\":" + totalCount;
			} else {
				message = Constants.API_MESSAGE_LIVE_ERROR_NOTYOURORDER;
				status = Constants.API_STATUS_FAIL;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 
	 * 用户订单门票分配 username 用户名 orderId订单id
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/customerOrder/ticketAssign")
	public void ticketAllotment(String username, Long orderId,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsUser user = CmsUtils.getUser(request);
		if (user != null) {
			if (orderId != null && StringUtils.isNotBlank(username)) {
				BbsUser assignUser = userMng.findByUsername(username);
				BbsOrder order = orderMng.findById(orderId);
				if (order != null && assignUser != null) {
					// 不能操作他人订单数据
					if (!order.getBuyUser().equals(user)) {
						message = Constants.API_MESSAGE_LIVE_ERROR_NOTYOURORDER;
						status = Constants.API_STATUS_FAIL;
					}
					// 票已全部分配出
					if (!order.getHasLiveNotUsed()) {
						message = Constants.API_MESSAGE_LIVE_ERROR_HASNOTENOUGHTICKET;
						status = Constants.API_STATUS_FAIL;
					}
					Integer liveId = order.getDataId();
					BbsLive live = liveMng.findById(liveId);
					if (live != null) {
						liveUserMng.saveUserLiveTicket(order,
								assignUser.getId(), live.getId());
					}
					if (order != null && order.getBuyUser().equals(user)) {
						Pagination page = liveUserMng.getPage(orderId, null,
								null, 1, 10);
						List<BbsLiveUser> list = (List<BbsLiveUser>) page
								.getList();
						JSONArray jsonArray = new JSONArray();
						if (list != null && list.size() > 0) {
							for (int i = 0; i < list.size(); i++) {
								jsonArray.put(i,
										convertToJson1(list.get(i), order));
							}
						}
						status = Constants.API_STATUS_SUCCESS;
						message = Constants.API_MESSAGE_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
						body = jsonArray.toString();
					}
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 用户门票列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/userTicket/list")
	public void userTicketList(Integer pageNo, Integer pageSize,
			HttpServletRequest request, HttpServletResponse response)
			throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (pageNo == null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		BbsUser user = CmsUtils.getUser(request);
		if (user != null) {
			Pagination page = liveUserMng.getPage(null, user.getId(), null,
					pageNo, pageSize);

			List<BbsLiveUser> list = (List<BbsLiveUser>) page.getList();
			JSONArray jsonArray = new JSONArray();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					jsonArray.put(i, convertToJson2(list.get(i)));
				}

			}
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = jsonArray.toString();
		}

		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	private JSONObject convertToJson(BbsOrder order,
			Map<String, BbsLive> liveMap) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("liveMap", liveMap);
		json.put("id", order.getId());// 订单id
		json.put("dataId", order.getDataId());// 数据Id
		json.put("dataType", order.getDataType());// 数据类型(0主题 1道具 2礼物 3活动live
													// 4广告)
		json.put("orderNumber", order.getOrderNumber());// 订单编号
		json.put("buyTime", order.getBuyTime());// 开始时间
		json.put("chargeAmount", order.getChargeAmount());// 成交金额
		json.put("liveUserNum", order.getLiveUserNum());// 门票数
		json.put("liveUsedNum", order.getLiveUsedNum());// 已获取的门票数
		return json;
	}

	private JSONObject convertToJson1(BbsLiveUser liveUser, BbsOrder order)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("orderId", order.getId());// 订单id
		json.put("liveUserNum", order.getLiveUserNum());// 门票数
		json.put("liveUsedNum", order.getLiveUsedNum());// 已获取的门票数
		json.put("username", liveUser.getJoinUser().getUsername());// 用户
		json.put("buyTime", liveUser.getBuyTime());// 分配时间

		return json;
	}

	private JSONObject convertToJson2(BbsLiveUser liveUser)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("title", liveUser.getLive().getTitle());// 标题
		json.put("beginTime", liveUser.getLive().getBeginTime());// 开始时间
		json.put("buyTime", liveUser.getBuyTime());// 购买时间或者接收时间
		json.put("beginPrice", liveUser.getLive().getBeginPrice());// 未开始价格
		json.put("joinUserNum", liveUser.getLive().getJoinUserNum());// 参与人数
		return json;
	}

	@Autowired
	private BbsOrderMng orderMng;
	@Autowired
	private BbsLiveMng liveMng;
	@Autowired
	private BbsLiveUserMng liveUserMng;
	@Autowired
	private BbsUserMng userMng;
}
