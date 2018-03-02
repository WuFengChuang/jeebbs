package com.jeecms.bbs.action.member;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.bbs.entity.BbsCreditExchange;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.entity.BbsWebservice;
import com.jeecms.bbs.manager.BbsCreditExchangeMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsUserAccountMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.manager.BbsWebserviceMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.security.encoder.PwdEncoder;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsConfigItem;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsConfigItemMng;
import com.jeecms.core.manager.UnifiedUserMng;

@Controller
public class UserPostAct {

	public static final String MEMBER_CENTER = "tpl.memberCenter";
	public static final String MEMBER_INFORM = "tpl.information";
	public static final String MEMBER_TOPIC = "tpl.memberTopic";
	public static final String MEMBER_CHECKING_TOPIC = "tpl.memberCheckingTopic";
	public static final String MEMBER_POST = "tpl.memberPost";
	public static final String MEMBER_CHECKING_POST = "tpl.memberCheckingPost";
	public static final String SEARCH = "tpl.search";
	public static final String SEARCH_RESULT = "tpl.searchResult";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	public static final String TPL_CREDIT = "tpl.creditMng";
	public static final String TPL_EDIT_USERIMG = "tpl.edituserimg";
	public static final String TPL_NO_VIEW = "tpl.noview";
	public static final String TPL_PWD = "tpl.memberPassword";
	public static final String MEMBER_ACCOUNT = "tpl.memberAccount";

	@RequestMapping("/member/index*.jhtml")
	public String index(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, MEMBER_CENTER);
	}

	@RequestMapping("/member/information.jhtml")
	public String information(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		List<CmsConfigItem>items=cmsConfigItemMng.getList(site.getConfig().getId(), CmsConfigItem.CATEGORY_REGISTER);
		model.addAttribute("items", items);
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, MEMBER_INFORM);
	}
	
	
	@RequestMapping("/member/editUserImg.jhtml")
	public String editUserImg(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, TPL_EDIT_USERIMG);
	}
	
	@RequestMapping("/member/updateUserImg.jhtml")
	public String updateUserImg(String email,
			String newPassword, String signed, String avatar, BbsUserExt ext,
			HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if(!user.getId().equals(ext.getId())){
			model.put("msg", "更新错误");
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		manager.updateMember(user.getId(), email, newPassword, null, signed,
				avatar, ext, null,null);
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		return information(request, model);
	}

	@RequestMapping("/member/update.jspx")
	public String informationSubmit(String email,
			String newPassword, String signed, String avatar, BbsUserExt ext,
			HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		WebErrors errors = WebErrors.create(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if(!user.getId().equals(ext.getId())){
			model.put("msg", "更新错误");
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		if (errors.ifNotEmail(email, "email", 100)) {
			return FrontUtils.showError(request, response, model, errors);
		}
		Map<String,String>attrs=RequestUtils.getRequestMap(request, "attr_");
		user = manager.updateMember(user.getId(), email, newPassword, null, signed,
				avatar, ext,attrs, null);
		bbsWebserviceMng.callWebService(BbsWebservice.SERVICE_TYPE_UPDATE_USER, 
				user.getUsername(),newPassword, email, ext);
		List<CmsConfigItem>items=cmsConfigItemMng.getList(site.getConfig().getId(), CmsConfigItem.CATEGORY_REGISTER);
		model.put("user", user);
		model.addAttribute("items", items);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, MEMBER_INFORM);
	}

	@RequestMapping("/member/mytopic*.jhtml")
	public String mytopic(Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, MEMBER_TOPIC);
	}
	/**
	 * 待审核帖子
	 * @param pageNo
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/member/checkingtopic*.jhtml")
	public String checkingTopics(Integer pageNo, HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}else{
			if(!user.getModerator()){
				WebErrors errors=WebErrors.create(request);
				errors.addErrorCode("error.user.notModerator");
				return FrontUtils.showError(request, response, model, errors);
			}
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, MEMBER_CHECKING_TOPIC);
	}
	
	@RequestMapping("/member/checkTopic.jspx")
	public void checkTopic(Integer topicId,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		JSONObject json=new JSONObject();
		int status=-1;
		if (user != null&&topicId!=null) {
			if(!user.getModerator()){
				status=-2;
			}else{
				BbsTopic topic=topicMng.findById(topicId);
				if(topic==null){
					status=-1;
				}else{
					topic.setCheckStatus(true);
					topicMng.update(topic);
					status=1;
				}
			}
		}
		try {
			json.put("status", status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequestMapping("/member/mypost*.jhtml")
	public String mypost(Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, MEMBER_POST);
	}
	
	@RequestMapping("/member/checkingposts*.jhtml")
	public String checkingPosts(Integer pageNo, HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}else{
			if(!user.getModerator()){
				WebErrors errors=WebErrors.create(request);
				errors.addErrorCode("error.user.notModerator");
				return FrontUtils.showError(request, response, model, errors);
			}
		}
		model.put("user", user);
		FrontUtils.frontPageData(request, model);
		String tplPath = FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, MEMBER_CHECKING_POST);
		System.out.println(tplPath);
		return tplPath;
	}
	
	@RequestMapping("/member/checkPost.jspx")
	public void checkPost(Integer postId,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		JSONObject json=new JSONObject();
		int status=-1;
		if (user != null&&postId!=null) {
			if(!user.getModerator()){
				status=-2;
			}else{
				BbsPost post=postMng.findById(postId);
				if(post==null){
					status=-1;
				}else{
					post.setCheckStatus(true);
					postMng.update(post);
					status=1;
				}
			}
		}
		try {
			json.put("status", status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequestMapping(value = "/member/inputSearch*.jhtml", method = RequestMethod.GET)
	public String search(Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, SEARCH);
	}

	@RequestMapping(value = "/member/search*.jhtml")
	public String searchSubmit( Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		String keywords = RequestUtils.getQueryParam(request, "keywords");
		
		Integer forumId=Integer.parseInt(RequestUtils.getQueryParam(request, "forumId"));
		
		model.put("keywords", keywords);
		model.put("forumId", forumId);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, SEARCH_RESULT);
	}

	@RequestMapping("/member/creditManager.jhtml")
	public String creditManager(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsCreditExchange creditExchangeRule = creditExchangeMng.findById(site
				.getId());
		Boolean exchangeAvailable = false;
		if (creditExchangeRule.getPointinavailable()
				&& creditExchangeRule.getPrestigeoutavailable()
				|| creditExchangeRule.getPointoutavailable()
				&& creditExchangeRule.getPrestigeinavailable()) {
			exchangeAvailable = true;
		} else {
			exchangeAvailable = false;
		}
		if (exchangeAvailable) {
			if (!creditExchangeRule.getExpoint().equals(0)
					&& !creditExchangeRule.getExprestige().equals(0)) {
				exchangeAvailable = true;
			} else {
				exchangeAvailable = false;
			}
		}
		List<BbsForum> forums = forumMng.getList(site.getId());
		model.put("user", user);
		model.put("exchangeAvailable", exchangeAvailable);
		model.put("pointInAvail", creditExchangeRule.getPointinavailable());
		model.put("pointOutAvail", creditExchangeRule.getPointoutavailable());
		model.put("prestigeInAvail", creditExchangeRule
				.getPrestigeinavailable());
		model.put("prestigeOutAvail", creditExchangeRule
				.getPrestigeoutavailable());
		model.put("forums", forums);
		model.put("creditExchangeRule", creditExchangeRule);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, TPL_CREDIT);
	}

	@RequestMapping(value = "/member/getCreditOutValue.jspx")
	public void getCreditOutValue(Integer creditIn, Integer creditInType,
			Integer creditOutType, Double exchangetax,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject object = new JSONObject();
		Long creditOutValue = null;
		Double temp = 0.0;
		BbsCreditExchange rule = CmsUtils.getSite(request).getCreditExchange();
		if (exchangetax==null) {
			exchangetax=0d;
		}
		if (creditInType.equals(1) && creditOutType.equals(2)) {
			// 积分换取威望
			temp = creditIn * rule.getExpoint() * 1.0 / rule.getExprestige()
					* (1.0 + exchangetax);
		} else if (creditInType.equals(2) && creditOutType.equals(1)) {
			// 威望换积分
			temp = creditIn * rule.getExprestige() * 1.0 / rule.getExpoint()
					* (1.0 + exchangetax);
		}
		creditOutValue = Long.valueOf((long) Math.ceil(temp));
		try {
			object.put("creditOutValue", creditOutValue);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, object.toString());
	}

	@RequestMapping(value = "/member/creditExchange.jspx")
	public void creditExchange(Integer creditIn, Integer creditOut,
			Integer creditOutType,  String password,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject object = new JSONObject();
		BbsUser user = CmsUtils.getUser(request);
		Boolean result=true;
		String message=MessageResolver.getMessage(request, "cmspoint.success");
		BbsCreditExchange rule = CmsUtils.getSite(request).getCreditExchange();
		Integer miniBalance=rule.getMiniBalance();
		//验证密码
		if (!pwdEncoder.isPasswordValid(unifiedUserMng.getByUsername(
				user.getUsername()).getPassword(), password)) {
			result = false;
			message=MessageResolver.getMessage(request, "cmscredit.passworderror");
		}else{
			//验证兑换规则
			int valid=validExchange(rule, user, creditIn, creditOut, creditOutType);
			if(valid==1){
				result = false;
				message=MessageResolver.getMessage(request, "cmscredit.pointisnotenough",miniBalance);
			}else if(valid==2){
				result = false;
				message=MessageResolver.getMessage(request, "cmscredit.prestigeisnotenough",miniBalance);
			}else if(valid==3){
				result = false;
				message=MessageResolver.getMessage(request, "operate.faile");
			}else{
				if(creditOutType.equals(1)){
					user.setPoint(user.getPoint()-creditOut);
					user.setPrestige(user.getPrestige()+creditIn);
				}else if(creditOutType.equals(2)){
					user.setPrestige(user.getPrestige()-creditOut);
					user.setPoint(user.getPoint()+creditIn);
				}
				//此处更新用户积分威望信息
				manager.updatePwdEmail(user.getId(), password, user.getEmail());
			}
		}
		try {
			object.put("message", message);
			object.put("result", result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, object.toString());
	}
	
	@RequestMapping(value = "/member/pwd.jhtml", method = RequestMethod.GET)
	public String pwd(HttpServletRequest request,ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		return FrontUtils.getTplPath(request, site,TPLDIR_MEMBER, TPL_PWD);
	}
	
	@RequestMapping(value = "/member/pwd.jhtml", method = RequestMethod.POST)
	public String pwd_update(String origPwd,String password,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if(!pwdEncoder.isPasswordValid(unifiedUserMng.getByUsername(user.getUsername()).getPassword(), origPwd)){
			WebErrors errors =WebErrors.create(request);
			errors.addErrorCode("member.update.pwd.error");
			return FrontUtils.showError(request, response, model, errors);
		}
		manager.updatePwdEmail(user.getId(), password, null);
		bbsWebserviceMng.callWebService(BbsWebservice.SERVICE_TYPE_UPDATE_USER, 
				user.getUsername(),password, null, null);
		return pwd(request, model);
	}
	
	/**
	 * 验证密码是否正确
	 * 
	 * @param origPwd
	 *            原密码
	 * @param request
	 * @param response
	 */
	@RequestMapping("/member/checkPwd.jhtml")
	public void checkPwd(String origPwd, HttpServletRequest request,
			HttpServletResponse response) {
		BbsUser user = CmsUtils.getUser(request);
		boolean pass = manager.isPasswordValid(user.getId(), origPwd);
		ResponseUtils.renderJson(response, pass ? "true" : "false");
	}
	
	/**
	 * 完善账户资料
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/account.jspx", method = RequestMethod.GET)
	public String accountInput(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_ACCOUNT);
	}
	
	//完善用户账户资料
	@RequestMapping(value = "/member/account.jspx", method = RequestMethod.POST)
	public String accountSubmit(String accountWeiXin,String  accountAlipy,
			Short drawAccount,String nextUrl,HttpServletRequest request, 
			HttpServletResponse response,ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}

		WebErrors errors=WebErrors.create(request);
		if(drawAccount==null)
		{
			errors.addErrorCode("error.needParams");
		}else{
			if(!(drawAccount==0&&StringUtils.isNotBlank(accountWeiXin)
					||drawAccount==1&&StringUtils.isNotBlank(accountAlipy))){
				errors.addErrorCode("error.needParams");
			}
		}
		if(errors.hasErrors()){
			return FrontUtils.showError(request, response, model, errors);
		}
		userAccountMng.updateAccountInfo(accountWeiXin, accountAlipy, drawAccount,user);
		return FrontUtils.showSuccess(request, model, nextUrl);
	}
	
	
	private int validExchange(BbsCreditExchange rule,BbsUser user,Integer creditIn, Integer creditOut,
			Integer creditOutType){
		//兑出的积分是否充足
		if(user!=null&&creditOutType.equals(1)){
			if(user.getPoint()-creditOut<rule.getMiniBalance()){
				return 1;
			}
		}
		//兑出的威望是否充足
		if(user!=null&&creditOutType.equals(2)){
			if(user.getPrestige()-creditOut<rule.getMiniBalance()){
				return 2;
			}
		}
		Integer creditOutValue = null;
		Double temp = 0.0;
		if (creditOutType.equals(2)) {
			// 兑威望
			temp = creditIn * rule.getExpoint() * 1.0 / rule.getExprestige()
					* (1.0 + rule.getExchangetax());
		} else if (creditOutType.equals(1)) {
			// 兑积分
			temp = creditIn * rule.getExprestige() * 1.0 / rule.getExpoint()
					* (1.0 + rule.getExchangetax());
		}
		creditOutValue = Integer.valueOf((int) Math.ceil(temp));
		//creditOut兑换需要的积分或者威望被恶意修改
		if(!creditOutValue.equals(creditOut)){
			return 3;
		}
		return 0;
	}

	@Autowired
	private BbsUserMng manager;
	@Autowired
	private BbsCreditExchangeMng creditExchangeMng;
	@Autowired
	private BbsForumMng forumMng;
	@Autowired
	private PwdEncoder pwdEncoder;
	@Autowired
	private UnifiedUserMng unifiedUserMng;
	@Autowired
	private CmsConfigItemMng cmsConfigItemMng;
	@Autowired
	private BbsUserAccountMng userAccountMng;
	@Autowired
	private BbsWebserviceMng bbsWebserviceMng;
	@Autowired
	private BbsTopicMng topicMng;
	@Autowired
	private BbsPostMng postMng;
}
