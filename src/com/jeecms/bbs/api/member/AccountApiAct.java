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
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsAccountDrawMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class AccountApiAct {
	private static final Logger log = LoggerFactory.getLogger(AccountApiAct.class);
	@Autowired
	private BbsAccountDrawMng accountDrawMng;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	
	/**
	 * 提现申请列表
	 * @param applyType 申请类型 (1主题收益申请 2礼物收益申请 3主讲人收益申请) 非必选
	 * @param appId      appid 必选
	 * @param sessionKey 用户会话  必选
	 * @param pageNo
	 * @param pageSize
	 * @throws JSONException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/draw/list")
	public void myDrawApplyList(Short applyType,Integer pageNo,Integer pageSize,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		if(pageNo==null){
			pageNo=1;
		}
		if(pageSize==null){
			pageSize=10;
		}
		JSONArray jsonArray=new JSONArray();
		Pagination page = accountDrawMng.getPage(CmsUtils.getUserId(request), null, null, null, applyType, pageNo, pageSize);
		List<BbsAccountDraw> list = (List<BbsAccountDraw>) page.getList();
		int totalCount = page.getTotalCount();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson());
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
	 * 删除提现申请
	 * @param ids   申请的id ,间隔 比如 1,2   必选
	 * @param appId      appid   必选
	 * @param sessionKey 用户会话  必选
	 * @param nonce_str  随机字符串   必选
	 * @param sign		  签名 必选 
	 */
	@SignValidate
	@RequestMapping(value = "/draw/delete")
	public void deleteApply(String ids,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				BbsAccountDraw[] deleteByIds = accountDrawMng.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsAccountDraw id={}",deleteByIds[i].getId());
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
	 * 申请提现
	 * @param drawAmout  申请金额  必选
	 * @param applyType 申请提现类型 非必选 1主题收益申请 2礼物收益申请 默认1
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 * @param nonce_str  随机字符串  必选
	 * @param sign		  签名  必选
	 */
	@RequestMapping(value = "/draw/apply")
	public void drawApply(Double drawAmout,Short applyType,HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, drawAmout,applyType);
		if (!errors.hasErrors()) {
			BbsConfigCharge config = configChargeMng.getDefault();
			BbsUser user = CmsUtils.getUser(request);
			message = "\"\"";
			if(applyType.equals(BbsAccountDraw.APPLY_TYPE_TOPIC)){
				if(drawAmout>user.getUserAccount().getNoPayAmount()){
					errors.addErrorString("balance not Enough");
					code=ResponseCode.API_CODE_USER_BALANCE_NOT_ENOUGH;
				}
			}else if(applyType.equals(BbsAccountDraw.APPLY_TYPE_GIFT)){
				if(drawAmout>user.getUserAccount().getGiftNoDrawAmount()){
					errors.addErrorString("balance not Enough");
					code=ResponseCode.API_CODE_USER_BALANCE_NOT_ENOUGH;
				}
			}
			if(drawAmout<config.getMinDrawAmount()){
				errors.addErrorString("draw less min amount "+config.getMinDrawAmount());
				code=ResponseCode.API_CODE_DRAW_LESS;
			}
			if (!errors.hasErrors()) {
				accountDrawMng.draw(user, drawAmout, user.getUserAccount().getAccountWeixinOpenId(),applyType);
				status=Constants.API_STATUS_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 获取用户账户信息
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话    必选
	 */
	@RequestMapping(value = "/account/get")
	public void getAccountInfo(HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		BbsUser user = CmsUtils.getUser(request);
		if (user!=null) {
			if(user.getUserAccount()!=null){
				body=user.getUserAccount().convertToJson().toString();
				message=Constants.API_STATUS_SUCCESS;
				status=Constants.API_STATUS_SUCCESS;
			}else{
				message="\"user account not found\"";
				code=ResponseCode.API_CODE_USER_ACCOUNT_NOT_FOUND;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
}
