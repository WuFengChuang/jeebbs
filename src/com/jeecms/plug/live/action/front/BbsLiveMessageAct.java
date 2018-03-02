package com.jeecms.plug.live.action.front;


import static com.jeecms.bbs.Constants.TPLDIR_PLUG;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.google.gson.GsonBuilder;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.front.URLHelper;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.entity.BbsLiveMessage;
import com.jeecms.plug.live.manager.BbsLiveMessageMng;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.manager.BbsLiveUserMng;
import com.jeecms.plug.live.websocket.Message;
import com.jeecms.plug.live.websocket.WebSocketExtHandler;


/**
 * 活动live消息功能
 */
@Controller
public class BbsLiveMessageAct extends AbstractBbsLive{
	 private static final Logger log = LoggerFactory
	 .getLogger(BbsLiveMessageAct.class);

	public static final String TPL_LIVE_MESSAGE_LIST = "tpl.liveMessageList";
	
	@ResponseBody
	@RequestMapping(value = "/live/message/sayToAll.jspx", method = RequestMethod.POST)
	public void sayToAll(String text,Integer liveId,HttpServletRequest request) throws IOException {
		BbsUser user=CmsUtils.getUser(request);
		BbsLive live=null;
		boolean canSend=true;
		if(liveId!=null){
			live=manager.findById(liveId);
		}else{
			canSend=false;
		}
		if(user==null){
			canSend=false;
		}
		if(live!=null&&live.isCharge()&&user!=null){
			//非主讲者且未购买
			if(!live.getUser().equals(user)){
				//用户已登录判断是否已经购买
				boolean hasBuy=liveUserMng.hasBuyLive(user.getId(), live.getId());
				if(!hasBuy){
					canSend=false;
				}
			}
		}
		if(canSend){
			Message msg = new Message();
			msg.setDate(new Date());
			msg.setFrom(user.getId());
			msg.setFromName(user.getUsername());
			msg.setTo(0);
			msg.setText(text);
			socketHandler.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
			liveMessageMng.speak(live, user, null,text,BbsLiveMessage.MST_TYPE_TEXT);
		}
	}

	/**
	 * live消息列表
	 */
	@RequestMapping(value = "/live/message/list*.jspx")
	public String liveMessages(Integer liveId,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user=CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			Pagination page=liveMessageMng.getPage(liveId,
					URLHelper.getPageNo(request), 20);
			model.put("tag_pagination", page);
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_PLUG, TPL_LIVE_MESSAGE_LIST);
		}
	}
	
	
	@Autowired
	private BbsLiveMng manager;
	@Autowired
	private BbsLiveUserMng liveUserMng;
	@Autowired
    WebSocketExtHandler socketHandler;
	@Autowired
	private BbsLiveMessageMng liveMessageMng;
}
