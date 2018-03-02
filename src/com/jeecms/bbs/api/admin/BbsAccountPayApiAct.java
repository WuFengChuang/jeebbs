package com.jeecms.bbs.api.admin;

import java.io.File;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
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
import com.jeecms.bbs.entity.BbsAccountPay;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsAccountDrawMng;
import com.jeecms.bbs.manager.BbsAccountPayMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.security.encoder.Md5PwdEncoder;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsAccountPayApiAct {
	private static final Logger log = LoggerFactory.getLogger(BbsAccountPayApiAct.class);
	@Autowired
	private BbsUserMng userMng;
	@Autowired
	private BbsAccountPayMng accountPayMng;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private Md5PwdEncoder pwdEncoder;
	@Autowired
	private BbsAccountDrawMng accountDrawMng;
	@Autowired
	private RealPathResolver realPathResolver;
	public static final String PAY_LOGIN = "pay_login";
	public static final String WEIXIN_PAY_URL="weixin.transfer.url";
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/accountPay/list")
	public void accountPayList(String drawNum,String payUserName,String drawUserName,
			Date payTimeBegin,Date payTimeEnd,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		Integer payUserId = null;
		Integer drawUserId = null;
		if (StringUtils.isNotBlank(payUserName)) {
			BbsUser payUser = userMng.findByUsername(payUserName);
			if (payUser!=null) {
				payUserId = payUser.getId();
			}else{
				payUserId = 0;
			}
		}
		if (StringUtils.isNotBlank(drawUserName)) {
			BbsUser drawUser = userMng.findByUsername(drawUserName);
			if (drawUser!=null) {
				drawUserId = drawUser.getId();
			}else{
				drawUserId = 0;
			}
		}
		Pagination page = accountPayMng.getPage(drawNum, payUserId, drawUserId, payTimeBegin, payTimeEnd, pageNo, pageSize);
		List<BbsAccountPay> list = (List<BbsAccountPay>) page.getList();
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
	
	@SignValidate
	@RequestMapping(value = "/accountPay/delete")
	public void delete(String ids,
			HttpServletResponse response, HttpServletRequest request){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try {
				Long[] idArray = StrUtils.getLongs(ids);
				BbsAccountPay[] deleteByIds = accountPayMng.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsAccountPay id={}",deleteByIds[i].getId());
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
	
	@RequestMapping(value = "/accountPay/payByWeiXin")
	public void payByWX(Integer drawId,String password,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		BbsUser user = CmsUtils.getUser(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				drawId,password);
		if (!errors.hasErrors()) {
			BbsConfigCharge config = configChargeMng.getDefault();
			if (pwdEncoder.encodePassword(password).equals(config.getPayTransferPassword())) {//判断密码是否正确
				BbsAccountDraw bean = accountDrawMng.findById(drawId);
				if (StringUtils.isBlank(getWeixinPayUrl())) {
					setWeixinPayUrl(PropertyUtils.getPropertyValue(new File(realPathResolver.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)), WEIXIN_PAY_URL));
				}
				String statu = accountPayMng.weixinTransferPay(getWeixinPayUrl(), drawId, bean.getDrawUser(), user, bean.getApplyAmount() 
						,System.currentTimeMillis()+RandomStringUtils.random(5,Num62.N10_CHARS), request, response, null);
				message = "\"" + statu +"\"";
				if (MessageResolver.getMessage(request,"transferPay.success").equals(statu)) {
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}
				status = Constants.API_STATUS_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_PASSWORD_ERROR;
				code = ResponseCode.API_CODE_PASSWORD_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private String weixinPayUrl;


	public String getWeixinPayUrl() {
		return weixinPayUrl;
	}

	public void setWeixinPayUrl(String weixinPayUrl) {
		this.weixinPayUrl = weixinPayUrl;
	}
}
