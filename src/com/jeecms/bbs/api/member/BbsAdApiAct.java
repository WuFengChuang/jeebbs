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
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.ApiRecord;
import com.jeecms.bbs.entity.BbsAdvertising;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.ApiRecordMng;
import com.jeecms.bbs.manager.BbsAdvertisingMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsAdApiAct {
	
	/**
	 * 我的广告
	 * @param https
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ad/myAdvertises")
	public void myAdvertises(Integer https,Integer pageNo,Integer pageSize
		,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsUser user = CmsUtils.getUser(request);
		CmsSite site = CmsUtils.getSite(request);
		if (https==null) {
			https = Constants.URL_HTTP;
		}
//		BbsUserAccount account = userAccountMng.findById(user.getId());
		Pagination page = advertisingMng.getPage(site.getId(), null, null, null, null
				, user.getId(), pageNo, pageSize);
		List<BbsAdvertising> list = (List<BbsAdvertising>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson(https));
			}
		}
		String status=Constants.API_STATUS_SUCCESS;
		String message=Constants.API_MESSAGE_SUCCESS;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		String body=jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 广告账户充值
	 * @param amount
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/ad/buy")
	public void adRecharge(String appId,String sign,String outOrderNum,Integer orderType,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String body = "\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		BbsUser user = CmsUtils.getUser(request);
		errors = ApiValidate.validateRequiredParams(errors,outOrderNum,orderType);
		if (!errors.hasErrors()) {
			//签名数据不可重复利用
			ApiRecord record=apiRecordMng.findBySign(sign, appId);
			if (record!=null) {
				message = Constants.API_MESSAGE_REQUEST_REPEAT;
				code = ResponseCode.API_CODE_REQUEST_REPEAT;
			}else{
				BbsOrder order = bbsOrderMng.findByOutOrderNum(outOrderNum, orderType);
				if (order!=null) {
					message = Constants.API_MESSAGE_ORDER_NUMBER_USED;
					code = ResponseCode.API_CODE_ORDER_NUMBER_USED;
				}else{
					order = bbsOrderMng.adOrder(orderType ,user.getId(),outOrderNum);
					if (order.getPrePayStatus()==BbsOrder.PRE_PAY_STATUS_SUCCESS) {
						apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
								appId, "/ad/buy",sign);
						code=ResponseCode.API_CODE_CALL_SUCCESS;
						message=Constants.API_MESSAGE_SUCCESS;
						status=Constants.API_STATUS_SUCCESS;
					}else if(order.getPrePayStatus()==BbsOrder.PRE_PAY_STATUS_ORDER_NUM_ERROR){
						message=Constants.API_MESSAGE_ORDER_NUMBER_ERROR;
						code=ResponseCode.API_CODE_ORDER_NUMBER_ERROR;
					}
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private ApiRecordMng apiRecordMng;
	
//	@Autowired
//	private BbsUserAccountMng userAccountMng;
	@Autowired
	private BbsAdvertisingMng advertisingMng;
	@Autowired
	private BbsOrderMng bbsOrderMng;
}
