package com.jeecms.bbs.action;

import static com.jeecms.common.page.SimplePage.cpn;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.core.entity.CmsSite;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.jeecms.core.web.WebErrors;
import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.bbs.entity.BbsAccountPay;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsAccountDrawMng;
import com.jeecms.bbs.manager.BbsAccountPayMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.security.encoder.Md5PwdEncoder;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.RealPathResolver;

@Controller
public class BbsAccountPayAct {
	private static final Logger log = LoggerFactory.getLogger(BbsAccountPayAct.class);
	public static final String PAY_LOGIN = "pay_login";
	public static final String WEIXIN_PAY_URL="weixin.transfer.url";
	
	@RequiresPermissions("accountPay:payLogin")
	@RequestMapping(value="/accountPay/payLogin.do", method = RequestMethod.GET)
	public String payLogin(HttpServletRequest request,
			ModelMap model) {
		return "accountPay/pay_login";
	}
	
	@RequiresPermissions("accountPay:payLogin")
	@RequestMapping(value="/accountPay/payLogin.do", method = RequestMethod.POST)
	public String payLoginSubmit(String password, String captcha,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		WebErrors errors = WebErrors.create(request);
		try {
			if (!imageCaptchaService.validateResponseForID(session
					.getSessionId(request, response), captcha)) {
				errors.addErrorCode("error.invalidCaptcha");
			}
		} catch (CaptchaServiceException e) {
			errors.addErrorCode("error.exceptionCaptcha");
		}
		if(!errors.hasErrors()){
			BbsConfigCharge config=configChargeMng.getDefault();
			if(pwdEncoder.encodePassword(password).equals(config.getPayTransferPassword())){
				session.setAttribute(request, response, PAY_LOGIN, true);
			}else{
				errors.addErrorCode("error.invalidPassword");
			}
		}
		if(errors.hasErrors()){
			model.addAttribute("errors",errors.getErrors());
			return "accountPay/pay_error";
		}else{
			return list(null, null, null, null, null, 1, request, model);
		}
	}
	
	@RequiresPermissions("accountPay:goToPay")
	@RequestMapping("/accountPay/goToPay.do")
	public String goToPay(Integer id, 
			Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		Boolean is_login=(Boolean) session.getAttribute(request, PAY_LOGIN);
		if(is_login!=null&&is_login){
			BbsAccountDraw bean = accountDrawMng.findById(id);
			model.addAttribute("draw", bean);
			return "accountPay/pay";
		}else{
			return payLogin(request, model);
		}
	}
	
	//每次转账均需要输入密码和验证码
	@RequiresPermissions("accountPay:payByWeiXin")
	@RequestMapping("/accountPay/payByWeiXin.do")
	public String payByWeiXin(Integer drawId, String password,String captcha,
			Integer pageNo, HttpServletRequest request,HttpServletResponse response,
			ModelMap model) {
		BbsUser user=CmsUtils.getUser(request);
		Boolean is_login=(Boolean) session.getAttribute(request, PAY_LOGIN);
		if(is_login!=null&&is_login){
			WebErrors errors = WebErrors.create(request);
			try {
				if (!imageCaptchaService.validateResponseForID(session
						.getSessionId(request, response), captcha)) {
					errors.addErrorCode("error.invalidCaptcha");
				}
			} catch (CaptchaServiceException e) {
				errors.addErrorCode("error.exceptionCaptcha");
			}
			if(!errors.hasErrors()){
				BbsConfigCharge config=configChargeMng.getDefault();
				if(pwdEncoder.encodePassword(password).equals(config.getPayTransferPassword())){
					session.setAttribute(request, response, PAY_LOGIN, true);
				}else{
					errors.addErrorCode("error.invalidPassword");
				}
			}
			if(errors.hasErrors()){
				model.addAttribute("errors",errors.getErrors());
				return "accountPay/pay_error";
			}else{
				BbsAccountDraw bean = accountDrawMng.findById(drawId);
				model.addAttribute("draw", bean);
				if(StringUtils.isBlank(getWeixinPayUrl())){
					setWeixinPayUrl(PropertyUtils.getPropertyValue(
							new File(realPathResolver.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),WEIXIN_PAY_URL));
				}
				String status=accountPayMng.weixinTransferPay(getWeixinPayUrl(), drawId, bean.getDrawUser(), user, bean.getApplyAmount(), 
							System.currentTimeMillis()+RandomStringUtils.random(5,Num62.N10_CHARS), request, response, model);
				model.addAttribute("status", status);
				return "accountPay/pay_status";
			}
		}else{
			return payLogin(request, model);
		}
	}
	
	@RequiresPermissions("accountPay:v_list")
	@RequestMapping("/accountPay/v_list.do")
	public String list(String drawNum,String payUserName,String drawUserName,
			Date payTimeBegin,Date payTimeEnd,Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		Boolean is_login=(Boolean) session.getAttribute(request, PAY_LOGIN);
		if(is_login!=null&&is_login){
			Integer payUserId=null;
			Integer drawUserId=null;
			if(StringUtils.isNotBlank(payUserName)){
				BbsUser payUser=userMng.findByUsername(payUserName);
				if(payUser!=null){
					payUserId=payUser.getId();
				}else{
					payUserId=0;
				}
			}
			if(StringUtils.isNotBlank(drawUserName)){
				BbsUser drawUser=userMng.findByUsername(drawUserName);
				if(drawUser!=null){
					drawUserId=drawUser.getId();
				}else{
					drawUserId=0;
				}
			}
			Pagination pagination = accountPayMng.getPage(drawNum,payUserId
					,drawUserId,payTimeBegin,payTimeEnd,
					cpn(pageNo), CookieUtils.getPageSize(request));
			model.addAttribute("pagination",pagination);
			model.addAttribute("pageNo",pagination.getPageNo());
			model.addAttribute("drawNum",drawNum);
			model.addAttribute("payUserName",payUserName);
			model.addAttribute("drawUserName",drawUserName);
			model.addAttribute("payTimeBegin",payTimeBegin);
			model.addAttribute("payTimeEnd",payTimeEnd);
			return "accountPay/pay_list";
		}else{
			return payLogin(request, model);
		}
	}
	
	@RequiresPermissions("accountPay:o_delete")
	@RequestMapping("/accountPay/o_delete.do")
	public String delete(String drawNum,String payUserName,String drawUserName,
			Date payTimeBegin,Date payTimeEnd,
			Long[] ids, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Boolean is_login=(Boolean) session.getAttribute(request, PAY_LOGIN);
		if(is_login!=null&&is_login){
			BbsAccountPay[] beans = accountPayMng.deleteByIds(ids);
			for (BbsAccountPay bean : beans) {
				log.info("delete BbsAccountPay id={}", bean.getId());
			}
			return list(drawNum, payUserName,drawUserName,
					payTimeBegin, payTimeEnd, pageNo, request, model);
		}else{
			return payLogin(request, model);
		}
	}

	private WebErrors validateDelete(Long[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		if (errors.ifEmpty(ids, "ids")) {
			return errors;
		}
		for (Long id : ids) {
			vldExist(id, site.getId(), errors);
		}
		return errors;
	}

	private boolean vldExist(Long id, Integer siteId, WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsAccountPay entity = accountPayMng.findById(id);
		if(errors.ifNotExist(entity, BbsAccountPay.class, id)) {
			return true;
		}
		return false;
	}

	
	private String weixinPayUrl;


	public String getWeixinPayUrl() {
		return weixinPayUrl;
	}

	public void setWeixinPayUrl(String weixinPayUrl) {
		this.weixinPayUrl = weixinPayUrl;
	}

	@Autowired
	private BbsAccountDrawMng accountDrawMng;
	@Autowired
	private BbsAccountPayMng accountPayMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private ImageCaptchaService imageCaptchaService;
	@Autowired
	private Md5PwdEncoder pwdEncoder;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private BbsUserMng userMng;
	
}