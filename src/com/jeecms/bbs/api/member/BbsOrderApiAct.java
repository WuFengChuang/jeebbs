package com.jeecms.bbs.api.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsTopicCharge;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsTopicChargeMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class BbsOrderApiAct {
	private static final int OPERATOR_BUY=1;
	private static final int OPERATOR_ORDER=2;
	private static final int OPERATOR_CHARGELIST=3;
	@Autowired
	private BbsOrderMng bbsOrderMng;
	@Autowired
	private BbsTopicChargeMng topicChargeMng;
	
	/**
	 * 我消费的记录和我的主题被打赏记录
	 * @param orderNum 订单号 非必选
	 * @param orderType 类型  1我消费的记录 2我的主题被打赏记录 默认1
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 * @param pageNo 非必选 默认0
	 * @param pageSize 非必选 默认10
	 */
	@RequestMapping(value = "/order/myorders")
	public void myOrderList(String orderNum,Integer orderType,Integer pageNo,Integer pageSize
			,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		getMyInfoList(orderNum, orderType, pageNo, pageSize, request, response);
	}
	
	/**
	 * 我的主题收费统计
	 * @param orderNum 订单号 非必选
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 * @param pageNo 非必选 默认0
	 * @param pageSize 非必选 默认10
	 */
	@RequestMapping(value = "/order/chargelist")
	public void chargeList(String orderNum,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		getMyInfoList(orderNum, OPERATOR_CHARGELIST, pageNo, pageSize, request, response);
	}
	
	@SuppressWarnings("unchecked")
	private void getMyInfoList(String orderNum,Integer orderType,Integer pageNo,Integer pageSize
			,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if (orderType==null) {
			orderType = OPERATOR_BUY;
		}
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		List<BbsOrder> list = null;
		JSONArray jsonArray = new JSONArray();
		List<BbsTopicCharge> chargeList = null;
		int totalCount=0;
		if (orderType == OPERATOR_BUY) {
			Pagination page = bbsOrderMng.getPage(orderNum, CmsUtils.getUserId(request), null, null, null, null, pageNo, pageSize);
			list = (List<BbsOrder>) page.getList();
			totalCount = page.getTotalCount();
		}else if(OPERATOR_ORDER==orderType){
			Pagination page = bbsOrderMng.getPage(orderNum, null,CmsUtils.getUserId(request), null, null, null, pageNo, pageSize);
			list = (List<BbsOrder>) page.getList();
			totalCount = page.getTotalCount();
		}else if(OPERATOR_CHARGELIST==orderType){
			Pagination page = topicChargeMng.getPage(null, CmsUtils.getUserId(request), null, null, 1, pageNo, pageSize);
			chargeList = (List<BbsTopicCharge>) page.getList();
			totalCount = page.getTotalCount();
		}
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson());
			}
		}
		if(chargeList!=null&&chargeList.size()>0){
			for(int i=0;i<chargeList.size();i++){
				jsonArray.put(i, chargeList.get(i).convertToJson());
			}
		}
		body=jsonArray.toString()+",\"totalCount\":"+totalCount;
		status=Constants.API_STATUS_SUCCESS;
		message=Constants.API_MESSAGE_SUCCESS;
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
}
