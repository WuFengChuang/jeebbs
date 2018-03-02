package com.jeecms.bbs.action.member;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;
import static com.jeecms.bbs.entity.BbsFriendShip.REFUSE;
import static com.jeecms.bbs.entity.BbsFriendShip.ACCEPT;
import static com.jeecms.bbs.entity.BbsFriendShip.APPLYING;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.entity.BbsFriendShip;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsFriendShipMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.MagicMessage;


@Controller
public class FriendAct {
	public static final String FRIEND_SEARCH = "tpl.friendSearch";
	public static final String SEARCH_PAGE = "tpl.searchPage";
	public static final String FRIEND_SEARCH_RESULT = "tpl.friendSearchResult";
	public static final String SUGGEST = "tpl.suggest";
	public static final String FRIEND_APPLY_RESULT = "tpl.friendApplyResult";
	public static final String MYFRIEND = "tpl.myfriend";
	public static final String FRIEND_APPLY = "tpl.friendApply";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	public static final String TPL_GET_MSG_SEND = "tpl.msgsendpage";

	@RequestMapping(value = "/member/friendSearch*.jhtml", method = RequestMethod.GET)
	public String search(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		model.addAttribute("kw", RequestUtils.getQueryParam(request, "kw"));
		model.addAttribute("user", user);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, FRIEND_SEARCH);
	}
	
	@RequestMapping(value = "/member/getSearchFriendJson.jhtml")
	public void getSearchPageJson(Integer https,String kw,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException{
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		CmsSite site = CmsUtils.getSite(request);
		List<BbsUser> list = bbsUserMng.getList(kw, null, false, false, false, 0, Integer.MAX_VALUE);
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i <list.size(); i++){
				jsonArray.put(i,list.get(i).convertToJson(site, https));
			}
		}
		ResponseUtils.renderJson(response, jsonArray.toString());
	}
	
	@RequestMapping(value = "/member/getSearchFriend.jhtml")
	public String getSearchPage(Integer https,String kw,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException{
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		CmsSite site = CmsUtils.getSite(request);
		List<BbsUser> list = bbsUserMng.getList(kw, null, false, false, false, 0, Integer.MAX_VALUE);
		model.addAttribute("list",list);
		model.addAttribute("kw",kw);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site, TPLDIR_MEMBER,SEARCH_PAGE);
		
	}

	@RequestMapping("/member/suggest.jhtml")
	public String suggest(HttpServletRequest request,
			HttpServletResponse response, String username, Integer count,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		List<BbsUser> list = bbsUserMng.getSuggestMember(username, count);
		model.addAttribute("suggests", list);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, SUGGEST);
	}

	@RequestMapping(value = "/member/apply.jhtml")
	public String apply(HttpServletRequest request,
			HttpServletResponse response, String u, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		BbsUser friend = bbsUserMng.findByUsername(u);
		if (validateApply(user, friend)) {
			bbsFriendShipMng.apply(user, friend);
			model.addAttribute("message", "friend.applyed");
			return FrontUtils.getTplPath(request, site,
					TPLDIR_MEMBER, FRIEND_APPLY_RESULT);
		}
		return null;
	}

	@RequestMapping(value = "/member/applyJson.jhtml")
	public void applyJson(HttpServletRequest request,
			HttpServletResponse response, String u, ModelMap model)
			throws JSONException {
		BbsUser user = CmsUtils.getUser(request);
		BbsUser friend = bbsUserMng.findByUsername(u);
		JSONObject object = new JSONObject();
		MagicMessage magicMessage = MagicMessage.create(request);
		if (user == null) {
			object.put("message", magicMessage
					.getMessage("friend.apply.nologin"));
		} else if (validateApply(user, friend)) {
			bbsFriendShipMng.apply(user, friend);
			object.put("message", magicMessage.getMessage("friend.applyed"));
		} else {
			int status = validateApplyJson(user, friend);
			if (status == 0) {
				object.put("message", magicMessage
						.getMessage("friend.apply.nologin"));
			} else if (status == 1) {
				object.put("message", magicMessage
						.getMessage("friend.apply.noexistuser"));
			} else if (status == 2) {
				object.put("message", magicMessage
						.getMessage("friend.apply.yourself"));
			} else if (status == 3) {
				object.put("message", magicMessage
						.getMessage("friend.apply.success"));
			} else if (status == 4) {
				object.put("message", magicMessage
						.getMessage("friend.apply.applying"));
			}
		}
		ResponseUtils.renderJson(response, object.toString());
	}
	
	/**
	 * 删除好友 -1 删除成功  0 好友不存在  1 不允许删除自己 3 用户未登录
	 * @param uid 好友编号
	 * @param request
	 * @param response
	 * @param model
	 * @throws JSONException
	 */
	@RequestMapping(value = "/member/delJson.jhtml")
	public void deleteJson(Integer uid,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsUser user = CmsUtils.getUser(request);
		BbsUser friend = bbsUserMng.findById(uid);
		JSONObject object = new JSONObject();
		if (user == null) {//用户未登录
			object.put("status", 2);//2 用户未登录
		}else if(validateDel(user,friend)){
			bbsFriendShipMng.deleteById(friend.getId());
			object.put("status", -1);//-1 成功
		}else{
			int status = validateDelJson(user, friend);
			switch (status) {
			case 0:
				object.put("status", 0);//0 好友不存在
				break;
			case 1:
				object.put("status", 1);//1 不允许删除自己
				break;
			default:
				break;
			}
		}
		ResponseUtils.renderJson(response, object.toString());
	}

	@RequestMapping("/getsendmsgpage.jhtml")
	public String getSendMsgPage(String username, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			model.addAttribute("notlogin",true);
		}
		model.addAttribute("username",username);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, TPL_GET_MSG_SEND);
	}

	@RequestMapping(value = "/member/accept.jhtml")
	public String accept(HttpServletRequest request,
			HttpServletResponse response, Integer id, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		BbsFriendShip friendShip = bbsFriendShipMng.findById(id);
		if (validateAccept(user, friendShip)) {
			bbsFriendShipMng.accept(friendShip);
			model.addAttribute("message", "friend.accepted");
			return FrontUtils.getTplPath(request, site,
					TPLDIR_MEMBER, FRIEND_APPLY_RESULT);
		}
		return null;
	}
	
	/**
	 * 好友请求-同意 返回结果：0用户未登录 -1好友申请不存在 -2只处理申请状态的好友关系 -3只处理自己的好友关系 1通过申请，加为好友
	 * @param request
	 * @param response
	 * @param id 申请人编号
	 * @throws JSONException
	 */
	@RequestMapping(value = "/member/acceptAjax.jhtml")
	public void acceptAjax(HttpServletRequest request,HttpServletResponse response,
			Integer id) throws JSONException{
		BbsUser user = CmsUtils.getUser(request);
		BbsFriendShip friendShip = bbsFriendShipMng.findById(id);
		Integer status = validateAcceptAjax(user, friendShip);
		JSONObject json = new JSONObject();
		if (status.equals(1)) {
			bbsFriendShipMng.accept(friendShip);
		}
		json.put("status", status);
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequestMapping(value = "/member/refuse.jhtml")
	public String refuse(HttpServletRequest request,
			HttpServletResponse response, Integer id, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		BbsFriendShip friendShip = bbsFriendShipMng.findById(id);
		if (validateRefuse(user, friendShip)) {
			bbsFriendShipMng.refuse(friendShip);
			model.addAttribute("message", "friend.refused");
			return FrontUtils.getTplPath(request, site,
					TPLDIR_MEMBER, FRIEND_APPLY_RESULT);
		}
		return null;
	}
	
	/**
	 * 好友请求-拒绝 返回结果：0用户未登录 -1好友申请不存在 -2只处理申请状态的好友关系 -3只处理自己的好友关系 1未通过申请，拒绝加为好友
	 * @param request
	 * @param response
	 * @param id 申请人编号
	 * @throws JSONException
	 */
	@RequestMapping(value = "/member/refuseAjax.jhtml")
	public void refuseAjax(HttpServletRequest request,HttpServletResponse response,
			Integer id) throws JSONException{
		BbsUser user = CmsUtils.getUser(request);
		BbsFriendShip friendShip = bbsFriendShipMng.findById(id);
		Integer status = validateRefuseAjax(user, friendShip);
		JSONObject json = new JSONObject();
		if (status.equals(1)) {
			bbsFriendShipMng.refuse(friendShip);
		}
		json.put("status", status);
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequestMapping("/member/myfriend*.jhtml")
	public String myfriend(Integer pageNo, HttpServletRequest request,
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
				TPLDIR_MEMBER, MYFRIEND);
	}

	@RequestMapping("/member/friendApply*.jhtml")
	public String friendApply(HttpServletRequest request,
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
				TPLDIR_MEMBER, FRIEND_APPLY);
	}

	private int validateDelJson(BbsUser user, BbsUser friend) {
		int returnValue = -1;
		if (friend==null) {
			returnValue = 0;
		}else if (user.equals(friend)) {
			returnValue = 1;
		}
		return returnValue;
	}

	private boolean validateDel(BbsUser user, BbsUser friend) {
		//好友不存在
		if (friend==null) {
			return false;
		}
		//不允许删除自己
		if (user.equals(friend)) {
			return false;
		}
		return true;
	}
	
	private boolean validateApply(BbsUser user, BbsUser friend) {
		// 用户未登录
		if (user == null) {
			return false;
		}
		// 好友不存在
		if (friend == null) {
			return false;
		}
		// 不允许加自己为好友
		if (user.equals(friend)) {
			return false;
		}
		// 申请被拒绝则可以重新申请好友
		BbsFriendShip bean = bbsFriendShipMng.getFriendShip(user.getId(),
				friend.getId());
		if (bean != null && bean.getStatus() != REFUSE) {
			return false;
		}
		return true;
	}
	

	private int validateApplyJson(BbsUser user, BbsUser friend) {
		int returnValue = -1;
		// 用户未登录
		if (user == null) {
			returnValue = 0;
		}
		// 好友不存在
		if (friend == null) {
			returnValue = 1;
		}
		// 不允许加自己为好友
		if (user.equals(friend)) {
			returnValue = 2;
		}
		BbsFriendShip bean = bbsFriendShipMng.getFriendShip(user.getId(),
				friend.getId());
		if (bean != null && bean.getStatus() == ACCEPT) {
			returnValue = 3;
		}
		if (bean != null && bean.getStatus() == APPLYING) {
			returnValue = 4;
		}
		return returnValue;
	}

	private boolean validateAccept(BbsUser user, BbsFriendShip friendShip) {
		// 用户未登录
		if (user == null) {
			return false;
		}
		// 好友申请不存在
		if (friendShip == null) {
			return false;
		}
		// 只处理申请中的好友关系
		if (friendShip.getStatus() != APPLYING) {
			return false;
		}
		// 只处理自己的好友关系
		if (!user.equals(friendShip.getFriend())) {
			return false;
		}
		return true;
	}
	
	private Integer validateAcceptAjax(BbsUser user, BbsFriendShip friendShip) {
		// 用户未登录
		if (user == null) {
			return 0;
		}
		// 好友申请不存在
		if (friendShip == null) {
			return -1;
		}
		// 只处理申请中的好友关系
		if (friendShip.getStatus() != APPLYING) {
			return -2;
		}
		// 只处理自己的好友关系
		if (!user.equals(friendShip.getFriend())) {
			return -3;
		}
		return 1;
	}

	private boolean validateRefuse(BbsUser user, BbsFriendShip friendShip) {
		// 用户未登录
		if (user == null) {
			return false;
		}
		// 好友申请不存在
		if (friendShip == null) {
			return false;
		}
		// 只处理申请中的好友关系
		if (friendShip.getStatus() != APPLYING) {
			return false;
		}
		// 只处理自己的好友关系
		if (!user.equals(friendShip.getFriend())) {
			return false;
		}
		return true;
	}
	private Integer validateRefuseAjax(BbsUser user, BbsFriendShip friendShip) {
		// 用户未登录
		if (user == null) {
			return 0;
		}
		// 好友申请不存在
		if (friendShip == null) {
			return -1;
		}
		// 只处理申请中的好友关系
		if (friendShip.getStatus() != APPLYING) {
			return -2;
		}
		// 只处理自己的好友关系
		if (!user.equals(friendShip.getFriend())) {
			return -3;
		}
		return 1;
	}

	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsFriendShipMng bbsFriendShipMng;
}
