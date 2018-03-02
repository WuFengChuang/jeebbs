package com.jeecms.bbs.action.member;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.bbs.Constants.TPLDIR_SPECIAL;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;
import static com.jeecms.common.page.SimplePage.cpn;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsAdvertising;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsTopicCharge;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserAccount;
import com.jeecms.bbs.manager.BbsAdvertisingMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsUserAccountMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.Num62;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.core.entity.CmsSite;

/**
 * 用户广告
 * @author tom
 */
@Controller
public class UserAdAct {
	public static final String USER_ADVERTISE_LIST = "tpl.advertiseList";
	public static final String USER_ADVERTISE_INPUT="tpl.advertiseBuyInput";
	public static final String TPL_ADSPACE_LIST = "tpl.adspaceList";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	
	@RequestMapping(value = "/adspaceList.jspx")
	public String adspaceList(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		List<BbsAdvertising> tag_list=advertisingMng.getList(null, true, user.getId(), 50);
		model.addAttribute("tag_list",tag_list);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, TPL_ADSPACE_LIST);
	}
	
	/**
	 * 我的广告
	 */
	@RequestMapping(value = "/member/myAdvertises.jspx")
	public String myAdvertises(Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		BbsUserAccount userAccount=userAccountMng.findById(user.getId());
		Pagination pagination=advertisingMng.getPage(site.getId(),
				null, null, null, null, user.getId(), 
				cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("tag_pagination",pagination);
		model.addAttribute("userAccount",userAccount);
		model.addAttribute("pageNo",pagination.getPageNo());
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, USER_ADVERTISE_LIST);
	}
	
	/**
	 * 广告账户充值输入页面
	 */
	@RequestMapping(value = "/member/adRechargeInput.jspx")
	public String buyInput(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, USER_ADVERTISE_INPUT);
	}
	
	
	@Autowired
	private BbsUserAccountMng userAccountMng;
	@Autowired
	private BbsAdvertisingMng advertisingMng;
	@Autowired
	private SessionProvider session;
}
