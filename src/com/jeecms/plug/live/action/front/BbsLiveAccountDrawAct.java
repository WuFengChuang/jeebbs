package com.jeecms.plug.live.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_PLUG;
import static com.jeecms.common.page.SimplePage.cpn;

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
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;
import com.jeecms.plug.live.entity.BbsLiveUserAccount;
import com.jeecms.plug.live.manager.BbsLiveUserAccountMng;

/**
 * 活动live主讲人账户提现
 */
@Controller
public class BbsLiveAccountDrawAct {
	private static final Logger log = LoggerFactory.getLogger(BbsLiveAccountDrawAct.class);

	public static final String LIVE_HOST_ACCOUNT_DRAW = "tpl.liveAccountDraw";
	public static final String LIVE_HOST_ACCOUNT_DRAW_LIST = "tpl.liveAccountDrawList";
	
	@RequestMapping(value = "/live/host/draw_list.jspx")
	public String drawList(Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		BbsLiveUserAccount liveUserAccount=liveUserAccountMng.findById(user.getId());
		if(liveUserAccount==null){
			WebErrors errors=WebErrors.create(request);
			errors.addErrorCode("error.userAccount.notfound");
			return FrontUtils.showError(request, response, model, errors);
		}
		Pagination pagination=accountDrawMng.getPage(user.getId(),null,null,null,
				BbsAccountDraw.APPLY_TYPE_LIVE,cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination",pagination);
		model.addAttribute("userAccount",liveUserAccount);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, LIVE_HOST_ACCOUNT_DRAW_LIST);
	}
	
	@RequestMapping(value = "/live/host/draw_del.jspx")
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
	@RequestMapping(value = "/live/host/draw.jspx", method = RequestMethod.GET)
	public String drawInput(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		BbsConfigCharge config=configChargeMng.getDefault();
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		if(StringUtils.isBlank(user.getAccountWeixinOpenId())){
			return "redirect:../../member/weixin_auth_enter.jspx";
		}
		BbsLiveUserAccount liveUserAccount=liveUserAccountMng.findById(user.getId());
		Double appliedSum=accountDrawMng.getAppliedSum(user.getId(),BbsAccountDraw.APPLY_TYPE_LIVE);
		model.addAttribute("userAccount",liveUserAccount);
		model.addAttribute("minDrawAmount",config.getMinDrawAmount());
		if(liveUserAccount!=null){
			model.addAttribute("maxDrawAmount",liveUserAccount.getNoPayAmount()-appliedSum);
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, LIVE_HOST_ACCOUNT_DRAW);
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
	@RequestMapping(value = "/live/host/draw.jspx", method = RequestMethod.POST)
	public String drawSubmit(Double drawAmout,
			String nextUrl,HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		WebErrors errors=WebErrors.create(request);
		BbsLiveUserAccount liveUserAccount=liveUserAccountMng.findById(user.getId());
		if(liveUserAccount==null){
			errors.addErrorCode("error.userAccount.notfound");
			return FrontUtils.showError(request, response, model, errors);
		}
		if(drawAmout!=null){
			BbsConfigCharge config=configChargeMng.getDefault();
			if(drawAmout>liveUserAccount.getNoPayAmount()){
				errors.addErrorCode("error.userAccount.balanceNotEnough");
			}
			if(drawAmout<config.getMinDrawAmount()){
				errors.addErrorCode("error.userAccount.drawLessMinAmount",config.getMinDrawAmount());
			}
			if(errors.hasErrors()){
				return FrontUtils.showError(request, response, model, errors);
			}
		}
		accountDrawMng.draw(user, drawAmout, null,BbsAccountDraw.APPLY_TYPE_LIVE);
		log.info("draw Submit success. id={}", user.getId());
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
	private BbsLiveUserAccountMng liveUserAccountMng;
}
