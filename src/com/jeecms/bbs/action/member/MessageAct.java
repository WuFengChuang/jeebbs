package com.jeecms.bbs.action.member;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.entity.BbsMessage;
import com.jeecms.bbs.entity.BbsMessageReply;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsMessageMng;
import com.jeecms.bbs.manager.BbsMessageReplyMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.MagicMessage;
import com.jeecms.core.web.front.URLHelper;

@Controller
public class MessageAct {
	public static final String SEND_MSG = "tpl.sendMsg";
	public static final String SEND_SYS_MSG = "tpl.sendSysMsg";
	public static final String MY_MSG = "tpl.myMsg";
	public static final String SYS_MSG = "tpl.sysMsg";
	public static final String REPLY = "tpl.reply";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	public static final String MY_REMIND = "tpl.myremind";

	@RequestMapping(value = "/member/sendMsg.jhtml", method = RequestMethod.GET)
	public String message(String username, Integer type,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		FrontUtils.frontData(request, model, site);
		model.addAttribute("username", username);
		model.addAttribute("type", type);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, SEND_MSG);
	}

	@RequestMapping(value = "/member/sendMsg.jhtml", method = RequestMethod.POST)
	public String messageSubmit(HttpServletRequest request,HttpServletResponse response, String u,
			BbsMessage msg, String content,ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if (validateSendMsg(request,false,user, u,msg.getContent(), model)) {
			bbsMessageMng.sendMsg(user, bbsUserMng.findByUsername(u), msg);
		}
		response.sendRedirect("myMsg.jhtml");
		return null;
	}
	
	/**
	 * 发送消息 -1用户未登录 0发送成功 1用户名为空 2不允许发送消息给自己 3用户名不存在 5消息过滤失败
	 * @param u 接收者名称
	 * @param msg 消息对象
	 * @param content 消息内容
	 * @param request 
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/member/sendMsgAjax.jhtml")
	public void messageSendJson(String u,BbsMessage msg,String content,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsUser user = CmsUtils.getUser(request);
		JSONObject json = new JSONObject();
		if (user == null) {
			json.put("status", -1);//用户未登录
		}else{
			Integer returnValue = validateSendMsgJson(request, false, user, u, content);
			switch (returnValue) {
			case 0:
				bbsMessageMng.sendMsg(user, bbsUserMng.findByUsername(u), msg);
				json.put("status", 0);
				break;
			case 1:
				json.put("status", 1);//用户名为空
				break;
			case 2:
				json.put("status", 2);//不允许发送消息给自己
				break;
			case 3:
				json.put("status", 3);//用户名不存在
				break;
			case 5:
				json.put("status", 5);//用户信息过滤失败
				break;
			default:
				break;
			}
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequestMapping(value = "/member/sysMsg*.jhtml")
	public String sysMsg(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
//		Pagination pagination=bbsMessageMng.getPagination(true,user.getId(),user.getGroup().getId(),
//				URLHelper.getPageNo(request), CookieUtils.getPageSize(request));
		Pagination pagination= bbsMessageMng.getPagination(true, null, null, user.getId(), null, null, user.getGroup().getId(), 
				URLHelper.getPageNo(request), CookieUtils.getPageSize(request));
		model.addAttribute("tag_pagination", pagination);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,TPLDIR_MEMBER, SYS_MSG);
	}
	
	@RequestMapping(value = "/member/sendSysMsg.jhtml", method = RequestMethod.GET)
	public String sendSysMessage(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if(user.getOfficial()==null||!user.getOfficial()){
			return FrontUtils.showMessage(request, model, "friend.send.notofficialuser");
		}
		return FrontUtils.getTplPath(request, site,TPLDIR_MEMBER, SEND_SYS_MSG);
	}
	
	@RequestMapping(value = "/member/sendSysMsg.jhtml", method = RequestMethod.POST)
	public String sysMessageSubmit(HttpServletRequest request,HttpServletResponse response,
			BbsMessage msg,ModelMap model) throws IOException {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if (validateSendMsg(request,true,user, null,msg.getContent(), model)) {
			bbsMessageMng.sendSysMsg(user,null,null,null,msg);
		}
		return sendSysMessage(request, model);
	}
	
	@RequestMapping(value = "/member/sendMsgJson.jhtml", method = RequestMethod.POST)
	public void messageJsonSubmit(HttpServletRequest request,HttpServletResponse response, String username,
			String content,Integer msgType, ModelMap model) throws JSONException {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		JSONObject object = new JSONObject();
		BbsUser receiver=bbsUserMng.findByUsername(username);
		MagicMessage messageResource = MagicMessage.create(request);
		if (user == null) {
			object.put("message", messageResource
					.getMessage("friend.apply.nologin"));
		}
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		if (validateSendMsg(request,false,user, username,content, model)) {
			BbsMessage msg=new BbsMessage();
			msg.setContent(content);
			msg.setCreateTime(new Date());
			msg.setReceiver(receiver);
			msg.setSender(user);
			msg.setUser(user);
			msg.setMsgType(msgType);
			msg.init();
			bbsMessageMng.sendMsg(user, receiver, msg);
			object.put("message", messageResource
					.getMessage("greet.success"));
		}else{
			object.put("message", messageResource
					.getMessage("greet.fail"));
		}
		ResponseUtils.renderJson(response, object.toString());
	}

	//我的信息
	@RequestMapping(value = "/member/myMsg*.jhtml")
	public String myMsg(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		//put允许加null值
//		model.put("user", user);
		model.addAttribute("kw", RequestUtils.getQueryParam(request, "kw"));
		model.addAttribute("user", user);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, MY_MSG);
	}
	
	/**
	 * 获取消息
	 * @param typeId 消息类型 1消息，2留言,3打招呼
	 * @param status 消息状态 信息状态 false未读 true已读 null全部
	 * @param first
	 * @param count
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/member/myMsgJson.jhtml")
	public void myMsgJson(Integer typeId,Integer first,Boolean status,Integer count,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsUser user = CmsUtils.getUser(request);
		if (typeId==null) {
			typeId=1;
		}
		if (first==null) {
			first=0;
		}
		if (count==null) {
			count=10;
		}
		JSONObject json = new JSONObject();
		if (user == null) {
			json.put("message", Constants.API_MESSAGE_USER_NOT_LOGIN);
		}else{
			List<BbsMessage> list = bbsMessageMng.getList(false, user.getId(), null, null, typeId, status, first, count);
			JSONArray jsonArray = new JSONArray();
			if (list!=null&&list.size()>0) {
				for(int i = 0 ; i < list.size(); i++){
					list.get(i).setStatus(true);
					bbsMessageMng.update(list.get(i));
					jsonArray.put(i,list.get(i).convertToJson());
				}
			}
			json.put("message", jsonArray.toString());
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	

	//我的留言
	@RequestMapping(value = "/member/myguestbook*.jhtml")
	public String myguestbook(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		model.put("user", user);
		model.put("typeId", 2);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, MY_MSG);
	}
	//我的提醒
	@RequestMapping(value = "/member/myremind*.jhtml")
	public String myremind(HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		model.put("user", user);
		model.put("typeId", 3);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, MY_REMIND);
	}

	@RequestMapping(value = "/member/reply*.jhtml")
	public String reply(HttpServletRequest request, Integer mid, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		model.put("user", user);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		if (validateReply(user, mid, model)) {
			model.put("msg", bbsMessageMng.findById(mid));
			return FrontUtils.getTplPath(request, site,
					TPLDIR_MEMBER, REPLY);
		}
		return "redirect:/member/myMsg.jhtml";
	}

	@RequestMapping(value = "/member/ajax_delete_msg.jhtml")
	public void deleteMessage(HttpServletRequest request,
			HttpServletResponse response, Integer mid, ModelMap model)
			throws JSONException {
		JSONObject json = new JSONObject();
		BbsUser user = CmsUtils.getUser(request);
		if (validateDeleteMessage(user, mid)) {
			bbsMessageMng.deleteById(mid);
			json.put("success", true);
		} else {
			json.put("success", false);
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequestMapping(value = "/member/ajax_delete_reply.jhtml")
	public void deleteReply(HttpServletRequest request,
			HttpServletResponse response, Integer rid, ModelMap model)
			throws JSONException {
		JSONObject json = new JSONObject();
		BbsUser user = CmsUtils.getUser(request);
		if (validateDeleteReply(user, rid)) {
			bbsMessageReplyMng.deleteById(rid);
			json.put("success", true);
		} else {
			json.put("success", false);
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	private boolean validateDeleteMessage(BbsUser user, Integer rid) {
		if (user == null) {
			return false;
		}
		BbsMessage bean = bbsMessageMng.findById(rid);
		if (bean == null) {
			return false;
		}
		if (!bean.getUser().equals(user)) {
			return false;
		}
		return true;
	}

	private boolean validateDeleteReply(BbsUser user, Integer rid) {
		if (user == null) {
			return false;
		}
		BbsMessageReply bean = bbsMessageReplyMng.findById(rid);
		if (bean == null) {
			return false;
		}
		if (!bean.getMessage().getUser().equals(user)) {
			return false;
		}
		return true;
	}

	private boolean validateReply(BbsUser user, Integer mid, ModelMap model) {
		BbsMessage bean = bbsMessageMng.findById(mid);
		if (bean == null) {
			return false;
		}
		if(bean.getSys()!=null&&bean.getSys()){
			return true;
		}
		if (!bean.getUser().equals(user)) {
			return false;
		}
		return true;
	}

	private boolean validateSendMsg(HttpServletRequest request, boolean sysMessage,BbsUser user,String receiver,String content,
			ModelMap model) {
		if(!sysMessage){
			// 用户名为空
			if (StringUtils.isBlank(receiver)) {
				model.addAttribute("message", MessageResolver.getMessage(request, "friend.send.noname"));
				return false;
			}
			// 不允许发送消息给自己
			if (receiver.equals(user.getUsername())) {
				model.addAttribute("message", MessageResolver.getMessage(request, "friend.sendmyself"));
				return false;
			}
			// 用户名不存在
			if (bbsUserMng.findByUsername(receiver) == null) {
				model.addAttribute("message", MessageResolver.getMessage(request, "friend.send.noexistname"));
				return false;
			}
		}else{
			if(user.getOfficial()==null||!user.getOfficial()){
				model.addAttribute("message", MessageResolver.getMessage(request, "friend.send.notofficialuser"));
				return false;
			}
		}
		if(StringUtils.isBlank(filterUserInputContent(content))){
			model.addAttribute("message", MessageResolver.getMessage(request, "operate.faile"));
			return false;
		}
		return true;
	}
	
	private Integer validateSendMsgJson(HttpServletRequest request,boolean sysMessage,BbsUser user,
			String receiver,String content){
		if (!sysMessage) {
			//用户名为空
			if (StringUtils.isBlank(receiver)) {
				return 1;
			}
			//不允许发送消息给自己
			if (receiver.equals(user.getUsername())) {
				return 2;
			}
			//用户名不存在
			if (bbsUserMng.findByUsername(receiver)==null) {
				return 3;
			}
		}else{
			//非官方账号
			if (user.getOfficial()==null||!user.getOfficial()) {
				return 4;
			}
		}
		//用户信息过滤失败
		if (StringUtils.isBlank(filterUserInputContent(content))) {
			return 5;
		}
		return 0;
	}
	
	private final static Whitelist user_content_filter = Whitelist.relaxed();
	static {
		user_content_filter.addTags("embed","object","param","span","div");
		user_content_filter.addAttributes(":all", "style", "class", "id", "name");
		user_content_filter.addAttributes("object", "width", "height","classid","codebase");	
		user_content_filter.addAttributes("param", "name", "value");
		user_content_filter.addAttributes("embed", "src","quality","width","height","allowFullScreen","allowScriptAccess","flashvars","name","type","pluginspage");
	}

	/**
	 * 对用户输入内容进行过滤
	 * @param html
	 * @return
	 */
	public static String filterUserInputContent(String html) {
		if(StringUtils.isBlank(html)) return "";
		return Jsoup.clean(html, user_content_filter);
	}

	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsMessageMng bbsMessageMng;
	@Autowired
	private BbsMessageReplyMng bbsMessageReplyMng;
}
