package com.jeecms.bbs.api.admin;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicCharge;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserAccount;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsTopicChargeMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsUserAccountMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class BbsOrderApiAct {
	
	@Autowired
	private BbsUserMng userMng;
	@Autowired
	private BbsTopicChargeMng topicChargeMng;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private BbsUserAccountMng userAccountMng;
	@Autowired
	private BbsOrderMng orderMng;
	@Autowired
	private BbsTopicMng topicMng;
	@Autowired
	private BbsCommonMagicMng magicMng;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/order/charge_list")
	public void orderChargeList(String title,String author,Date buyTimeBegin,Date buyTimeEnd,Integer orderBy,
			Integer pageNo,Integer pageSize,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		if (orderBy==null) {
			orderBy = 1;
		}
		Integer authorUserId = null;
		if (StringUtils.isNotBlank(author)) {
			BbsUser authorUser = userMng.findByUsername(author);
			if (authorUser!=null) {
				authorUserId = authorUser.getId();
			}else{
				authorUserId = 0;
			}
		}
		Pagination page = topicChargeMng.getPage(title, authorUserId, buyTimeBegin, buyTimeEnd, orderBy, pageNo, pageSize);
		List<BbsTopicCharge> list = (List<BbsTopicCharge>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size() ; i++) {
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
	
	@RequestMapping(value = "/order/commissionStatic")
	public void commissionStaticList(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsConfigCharge charge = null;
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		charge = configChargeMng.getDefault();
		if (charge!=null) {
			JSONObject json = charge.convertToJson(false);
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = json.toString();
		}else{
			code = ResponseCode.API_CODE_NOT_FOUND;
			message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/order/account_list")
	public void orderAccountList(String username,Date drawTimeBegin,Date drawTimeEnd,Integer orderBy,
			Integer pageNo,Integer pageSize,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		if (orderBy==null) {
			orderBy=1;
		}
		Pagination page = userAccountMng.getPage(username, drawTimeBegin, drawTimeEnd, orderBy, pageNo, pageSize);
		List<BbsUserAccount> list = (List<BbsUserAccount>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null) {
			for (int i = 0; i < list.size() ; i++) {
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/order/user_order_list")
	public void userBuyList(String orderNum,String buyusername,
			String authorusername,Short payMode,Short dataType,Integer dataId,
			Integer pageNo,Integer pageSize,HttpServletResponse response,HttpServletRequest request) throws JSONException{
		Integer buyUserId=null,authorUserId = null;
		if (StringUtils.isNotBlank(buyusername)) {
			BbsUser user = userMng.findByUsername(buyusername);
			if (user!=null) {
				buyUserId = user.getId();
			}
		}
		if (StringUtils.isNotBlank(authorusername)) {
			BbsUser user = userMng.findByUsername(authorusername);
			if (user!=null) {
				authorUserId = user.getId();
			}
		}
		if (payMode==null) {
			payMode = 0;
		}
		if (dataType == null) {
			dataType = -1;
		}
		Pagination page = orderMng.getPage(orderNum, buyUserId, authorUserId, payMode, dataType, dataId, pageNo, pageSize);
		List<BbsOrder> list = (List<BbsOrder>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size() ; i++) {
				JSONObject json = new JSONObject();
				json.put("order", list.get(i).convertToJson());
				
				if (list.get(i).getDataType().equals(BbsOrder.ORDER_TYPE_TOPIC)) {
					BbsTopic topic = topicMng.findById(list.get(i).getDataId());
					if(topic!=null){
						json.put("title", topic.getTitle());
					}else{
						json.put("title", "主题已删除");
					}
				}else if(list.get(i).getDataType().equals(BbsOrder.ORDER_TYPE_MAGIC)){
					BbsCommonMagic magic = magicMng.findById(list.get(i).getDataId());
					json.put("title", magic.getName());
				}else if(list.get(i).getDataType().equals(BbsOrder.ORDER_TYPE_GIFT)){
					json.put("title", "礼物");
				}else if(list.get(i).getDataType().equals(BbsOrder.ORDER_TYPE_LIVE)){
					json.put("title", "直播");
				}else if(list.get(i).getDataType().equals(BbsOrder.ORDER_TYPE_AD)){
					json.put("title", "广告");
				}
				jsonArray.put(i,json);
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
}
