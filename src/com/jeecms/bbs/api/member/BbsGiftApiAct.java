package com.jeecms.bbs.api.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsGift;
import com.jeecms.bbs.entity.BbsGiftUser;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsAccountDrawMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsGiftMng;
import com.jeecms.bbs.manager.BbsGiftUserMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsGiftApiAct {
	private static final Logger log = LoggerFactory.getLogger(BbsGiftApiAct.class);
	
	/**
	 * 礼物提现申请
	 * @param drawAmout 提现申请金额
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/gift/draw")
	public void giftDraw(Double drawAmount ,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, drawAmount);
		if (!errors.hasErrors()) {
			BbsUser user = CmsUtils.getUser(request);
			if (user.getUserAccount()!=null) {
				BbsConfigCharge config = configChargeMng.getDefault();
				if (drawAmount>user.getUserAccount().getGiftNoDrawAmount()) {
					errors.addError("\"balance not enough\"");
					code = ResponseCode.API_CODE_DRAW_BALANCE_NOT_ENOUGH;
				}else if(drawAmount<config.getMinDrawAmount()){
					errors.addError("\"draw less min amount\"");
					code = ResponseCode.API_CODE_DRAW_LESS;
				}
			}else{
				errors.addError(Constants.API_MESSAGE_OBJECT_NOT_FOUND);
			}
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
			}else{
				accountDrawMng.draw(user, drawAmount,null , BbsAccountDraw.APPLY_TYPE_GIFT);
				log.info("update BbsUserExt success. id={}", user.getId());
				message = Constants.API_MESSAGE_SUCCESS;
				status = Constants.API_STATUS_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 礼物列表
	 * @param https      图片地址返回格式  非必选   0https 1http 默认1
	 * @param appId appId 非必选 
	 * @param sessionKey 会话标识 非必选 和appId均有值则增加用户拥有该礼物数量值
	 * @param pageNo   查询开始下标
	 * @param pageSize	  查询数量
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gift/list")
	public void giftList(Integer https,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		if (https==null) {
			https= Constants.URL_HTTP;
		}
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		Pagination page = bbsGiftMng.getPage(pageNo, pageSize);
		List<BbsGift> list = (List<BbsGift>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson(CmsUtils.getSite(request), https, CmsUtils.getUser(request)));
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
	 * 我拥有的礼物列表
	 * @param appId appId 必选 
	 * @param sessionKey 会话标识必选
	 * @param giftId 礼物ID 非必选 
	 * @param pageNo   查询开始下标
	 * @param pageSize	  查询数量
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gift/myGifts")
	public void myGiftList(Integer giftId ,Integer pageNo,Integer pageSize,
			HttpServletRequest request, HttpServletResponse response) throws JSONException{
		Pagination page = bbsGiftUserMng.getPage(giftId, null, pageNo, pageSize);
		List<BbsGiftUser> list = (List<BbsGiftUser>) page.getList();
		int totalCount = page.getTotalCount();
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
	 * 购买礼物
	 * @param giftId 礼物ID 必选
	 * @param num  购买数量  非必选 默认1
	 * @param outOrderNum 外部订单号 必选
	 * @param orderType  1微信支付   2支付宝支付 必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping(value = "/gift/buy")
	public void buyGift(Integer giftId,Integer num,String outOrderNum,Integer orderType,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if (num==null) {
			num=1;
		}
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, giftId,outOrderNum,orderType);
		if (!errors.hasErrors()) {
			BbsGift gift = bbsGiftMng.findById(giftId);
			if (gift!=null) {
				//外部订单号不可以用多次
				BbsOrder order = bbsOrderMng.findByOutOrderNum(outOrderNum, orderType);
				if (order==null) {
					order = bbsOrderMng.giftOrder(gift, num, orderType, outOrderNum, CmsUtils.getUserId(request));
					if (order.getPrePayStatus()==BbsOrder.PRE_PAY_STATUS_SUCCESS) {
						message=Constants.API_MESSAGE_SUCCESS;
						status=Constants.API_STATUS_SUCCESS;
					}else if(order.getPrePayStatus()==BbsOrder.PRE_PAY_STATUS_ORDER_NUM_ERROR){
						message=Constants.API_MESSAGE_ORDER_NUMBER_ERROR;
					}else if(order.getPrePayStatus()==BbsOrder.PRE_PAY_STATUS_ORDER_AMOUNT_NOT_ENOUGH){
						message=Constants.API_MESSAGE_ORDER_AMOUNT_NOT_ENOUGH;
					}
				}else{
					message=Constants.API_MESSAGE_ORDER_NUMBER_USED;
				}
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 赠送礼物
	 * @param giftId 礼物ID 必选
	 * @param num  赠送数量  非必选 默认1
	 * @param receiverUserId 接收用户ID 必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping(value = "/gift/give")
	public void giveGift(Integer giftId,Integer num,Integer receiverUserId,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		if (num==null) {
			num=1;
		}
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, giftId,receiverUserId);
		if (!errors.hasErrors()) {
			BbsGift gift = bbsGiftMng.findById(giftId);
			if (gift!=null) {
				BbsUser receiverUser = bbsUserMng.findById(receiverUserId);
				BbsUser user = CmsUtils.getUser(request);
				if (receiverUser!=null) {
					int haveGiftCount=user.getGiftCount(giftId);
					if(haveGiftCount<num){
						message=Constants.API_MESSAGE_GIFT_NOT_ENOUGH;
						code=ResponseCode.API_CODE_GIFT_NOT_ENOUGH;
					}else{
						bbsGiftUserMng.giveUserGift(gift, user, receiverUser, null,num);
						message=Constants.API_MESSAGE_SUCCESS;
						status=Constants.API_STATUS_SUCCESS;
					}
				}else{
					message = Constants.API_MESSAGE_USER_NOT_FOUND;
					code=ResponseCode.API_CODE_USER_NOT_FOUND;
				}
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code=ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsGiftMng bbsGiftMng;
	@Autowired
	private BbsOrderMng bbsOrderMng;
	@Autowired
	private BbsGiftUserMng bbsGiftUserMng;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private BbsAccountDrawMng accountDrawMng;
}
