package com.jeecms.plug.live.action.admin;

import static com.jeecms.common.page.SimplePage.cpn;

import java.io.File;

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

import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsAccountDrawMng;
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
import com.jeecms.core.web.WebErrors;
/**
 * 收益管理
 * @author tom
 *
 */
import com.jeecms.plug.live.manager.BbsLiveUserAccountMng;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
@Controller
public class BbsLiveProfitAct {
	public static final String PAY_LOGIN = "pay_login";
	public static final String WEIXIN_PAY_URL="weixin.transfer.url";
	private static final Logger log = LoggerFactory.getLogger(BbsLiveProfitAct.class);

	/**
	 * 用户账户统计
	 * @param qusername
	 * @param qorderBy
	 */
	@RequiresPermissions("live:v_user_account_list")
	@RequestMapping("/live/v_user_account_list.do")
	public String list(String qusername,Integer qorderBy,
			Integer pageNo, HttpServletRequest request, ModelMap model) {
		Integer hostUserId=null;
		if(StringUtils.isNotBlank(qusername)){
			BbsUser user=bbsUserMng.findByUsername(qusername);
			if(user!=null){
				hostUserId=user.getId();
			}else{
				hostUserId=0;
			}
		}
		if(qorderBy==null){
			qorderBy=1;
		}
		Pagination pagination = liveUserAccountMng.getPage(hostUserId, qorderBy,cpn(pageNo), CookieUtils
				.getPageSize(request));
		model.addAttribute("pagination",pagination);
		model.addAttribute("qusername",qusername);
		model.addAttribute("qorderBy",qorderBy);
		return "plugPage/live/user_account_list";
	}
	
	/**
	 * 平台佣金汇总统计
	 * @return
	 */
	@RequiresPermissions("live:commissionStatic")
	@RequestMapping("/live/commissionStatic.do")
	public String commissionStatic(HttpServletRequest request, ModelMap model) {
		BbsConfigCharge config= configChargeMng.getDefault();
		model.addAttribute("config",config);
		return "plugPage/live/live_plat_statistic";
	}
	
	//每次转账均需要输入密码和验证码
	@RequiresPermissions("live:accountPay")
	@RequestMapping("/live/accountPay/payByWeiXin.do")
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
				String status=liveUserAccountMng.weixinTransferPay(getWeixinPayUrl(), drawId, bean.getDrawUser(), user, bean.getApplyAmount(), 
							System.currentTimeMillis()+RandomStringUtils.random(5,Num62.N10_CHARS), request, response, model);
				model.addAttribute("status", status);
				return "accountPay/pay_status";
			}
		}else{
			return payLogin(request, model);
		}
	}
	
	public String payLogin(HttpServletRequest request,
			ModelMap model) {
		return "accountPay/pay_login";
	}
	
	private String weixinPayUrl;


	public String getWeixinPayUrl() {
		return weixinPayUrl;
	}

	public void setWeixinPayUrl(String weixinPayUrl) {
		this.weixinPayUrl = weixinPayUrl;
	}
	
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsLiveUserAccountMng liveUserAccountMng;
	@Autowired
	private BbsAccountDrawMng accountDrawMng;
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
}