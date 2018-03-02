package com.jeecms.bbs.action.member;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.common.page.SimplePage.cpn;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

/**
 * 账户提现
 */
@Controller
public class AccountDrawAct {
	private static final Logger log = LoggerFactory.getLogger(AccountDrawAct.class);
	public static final String WEIXIN_AUTH_CODE_URL ="weixin.auth.getCodeUrl";
	public static final String MEMBER_ACCOUNT_DRAW = "tpl.memberAccountDraw";
	public static final String MEMBER_ACCOUNT_DRAW_LIST = "tpl.memberAccountDrawList";

	public static final String MEMBER_ACCOUNT_GIFT_DRAW = "tpl.memberAccountGiftDraw";
	public static final String MEMBER_WEIXIN_AUTH = "tpl.weixinAuth";
	
	@RequestMapping(value = "/member/draw_list.jspx")
	public String drawList(Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		if(user.getUserAccount()==null){
			WebErrors errors=WebErrors.create(request);
			errors.addErrorCode("error.userAccount.notfound");
			return FrontUtils.showError(request, response, model, errors);
		}
		Pagination pagination=accountDrawMng.getPage(user.getId(),null,null,null,
				null,cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination",pagination);
		model.addAttribute("userAccount",user.getUserAccount());
		String tplPath = FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_ACCOUNT_DRAW_LIST);
		return tplPath;
	}
	
	@RequestMapping(value = "/member/draw_del.jspx")
	public String drawDel(Integer[] ids,Integer pageNo,
			String nextUrl,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors = validateDelete(ids, site, user, request);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		accountDrawMng.deleteByIds(ids);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.showSuccess(request, model, nextUrl);
	}
	
	/**
	 * 提现申请输入页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/draw.jspx", method = RequestMethod.GET)
	public String drawInput(Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		BbsConfigCharge config=configChargeMng.getDefault();
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		if(StringUtils.isBlank(user.getAccountWeixinOpenId())){
			return "redirect:weixin_auth_enter.jspx";
		}
		if (pageNo==null) {
			pageNo=1;
		}
		Double appliedSum=accountDrawMng.getAppliedSum(user.getId(),BbsAccountDraw.APPLY_TYPE_TOPIC);
		model.addAttribute("userAccount", user.getUserAccount());
		model.addAttribute("minDrawAmount",config.getMinDrawAmount());
		if(user.getUserAccount()!=null){
			model.addAttribute("maxDrawAmount",user.getUserAccount().getNoPayAmount()-appliedSum);
		}
		//只显示主题提现列表
		Pagination pagination=accountDrawMng.getPage(user.getId(),null,null,null,
				(short)1,cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("tag_pagination",pagination);
		model.addAttribute("pageNo",pageNo);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_ACCOUNT_DRAW);
	}
	

	/**
	 * 提现申请提交页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/member/draw.jspx", method = RequestMethod.POST)
	public String drawSubmit(Double drawAmout,String applyAcount,
			String nextUrl,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		WebErrors errors=WebErrors.create(request);
		if(user.getUserAccount()==null){
			errors.addErrorCode("error.userAccount.notfound");
			return FrontUtils.showError(request, response, model, errors);
		}
		if(drawAmout!=null){
			BbsConfigCharge config=configChargeMng.getDefault();
			if(drawAmout>user.getUserAccount().getNoPayAmount()){
				errors.addErrorCode("error.userAccount.balanceNotEnough");
			}
			if(drawAmout<config.getMinDrawAmount()){
				errors.addErrorCode("error.userAccount.drawLessMinAmount",config.getMinDrawAmount());
			}
			if(errors.hasErrors()){
				return FrontUtils.showError(request, response, model, errors);
			}
		}
		accountDrawMng.draw(user, drawAmout, applyAcount,BbsAccountDraw.APPLY_TYPE_TOPIC);
		log.info("update BbsUserExt success. id={}", user.getId());
		return FrontUtils.showSuccess(request, model, nextUrl);
	}
	
	/**
	 * 礼物提现申请输入页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/gift_draw.jspx", method = RequestMethod.GET)
	public String giftDrawInput(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		BbsConfigCharge config=configChargeMng.getDefault();
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		Double appliedSum=accountDrawMng.getAppliedSum(user.getId(),BbsAccountDraw.APPLY_TYPE_GIFT);
		model.addAttribute("userAccount", user.getUserAccount());
		model.addAttribute("minDrawAmount",config.getMinDrawAmount());
		if(user.getUserAccount()!=null){
			model.addAttribute("maxDrawAmount",user.getUserAccount().getGiftNoDrawAmount()-appliedSum);
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_ACCOUNT_GIFT_DRAW);
	}
	

	/**
	 * 礼物提现申请提交页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/member/gift_draw.jspx", method = RequestMethod.POST)
	public String giftDrawSubmit(Double drawAmout,String applyAcount,
			String nextUrl,HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		WebErrors errors=WebErrors.create(request);
		if(user.getUserAccount()==null){
			errors.addErrorCode("error.userAccount.notfound");
			return FrontUtils.showError(request, response, model, errors);
		}
		if(drawAmout!=null){
			BbsConfigCharge config=configChargeMng.getDefault();
			if(drawAmout>user.getUserAccount().getGiftNoDrawAmount()){
				errors.addErrorCode("error.userAccount.balanceNotEnough");
			}
			if(drawAmout<config.getMinDrawAmount()){
				errors.addErrorCode("error.userAccount.drawLessMinAmount",config.getMinDrawAmount());
			}
			if(errors.hasErrors()){
				return FrontUtils.showError(request, response, model, errors);
			}
		}
		accountDrawMng.draw(user, drawAmout, applyAcount,BbsAccountDraw.APPLY_TYPE_GIFT);
		log.info("update BbsUserExt success. id={}", user.getId());
		return FrontUtils.showSuccess(request, model, nextUrl);
	}
	
	private WebErrors validateDelete(Integer[] ids, CmsSite site, BbsUser user,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldOpt(errors, site, user, ids)) {
			return errors;
		}
		return errors;
	}
	
	private boolean vldOpt(WebErrors errors, CmsSite site, BbsUser user,
			Integer[] ids) {
		for (Integer id : ids) {
			if (errors.ifNull(id, "id")) {
				return true;
			}
			BbsAccountDraw d = accountDrawMng.findById(id);
			// 数据不存在
			if (errors.ifNotExist(d, BbsAccountDraw.class, id)) {
				return true;
			}
			// 非本用户数据
			if (!d.getDrawUser().getId().equals(user.getId())) {
				errors.noPermission(BbsAccountDraw.class, id);
				return true;
			}
			// 提现申请状态是申请成功待支付和提现成功
			if (d.getApplyStatus()==BbsAccountDraw.CHECKED_SUCC
					||d.getApplyStatus()==BbsAccountDraw.DRAW_SUCC) {
				errors.addErrorCode("error.account.draw.hasChecked");
				return true;
			}
		}
		return false;
	}
	
	@Autowired
	private BbsAccountDrawMng accountDrawMng;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private RealPathResolver realPathResolver;
	
	private String weixinAuthCodeUrl;
	private String weixinAuthTokenUrl;
	

	public String getWeixinAuthCodeUrl() {
		return weixinAuthCodeUrl;
	}

	public void setWeixinAuthCodeUrl(String weixinAuthCodeUrl) {
		this.weixinAuthCodeUrl = weixinAuthCodeUrl;
	}

	public String getWeixinAuthTokenUrl() {
		return weixinAuthTokenUrl;
	}

	public void setWeixinAuthTokenUrl(String weixinAuthTokenUrl) {
		this.weixinAuthTokenUrl = weixinAuthTokenUrl;
	}
}
