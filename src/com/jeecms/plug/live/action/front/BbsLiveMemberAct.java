package com.jeecms.plug.live.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_PLUG;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

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
import com.jeecms.plug.live.manager.BbsLiveMessageMng;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.manager.BbsLiveUserMng;

/**
 * 活动live用户中心功能
 */
@Controller
public class BbsLiveMemberAct extends AbstractBbsLive{
	 private static final Logger log = LoggerFactory
	 .getLogger(BbsLiveMemberAct.class);

	public static final String TPL_LIVE_TICKET_INDEX = "tpl.liveTicketIndex";
	public static final String TPL_LIVE_TICKET_USER_INDEX = "tpl.liveTicketUserIndex";
	public static final String TPL_USER_LIVE_LIST = "tpl.userLiveList";
	
	public static final String TPL_LIVE_MESSAGE_LIST = "tpl.liveMessageList";
	
	@RequestMapping(value = "/live/ticket/index*.jspx")
	public String index(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user=CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			Pagination page=orderMng.getPage(null, user.getId(), null, 
					null, BbsOrder.ORDER_TYPE_LIVE, null, 
					URLHelper.getPageNo(request), CookieUtils.getPageSize(request));
			List<BbsOrder> li=(List<BbsOrder>) page.getList();
			Map<String,BbsLive>liveMap=new HashMap<String,BbsLive>();
			for(BbsOrder order:li){
				liveMap.put(order.getDataId().toString(), manager.findById(order.getDataId()));
			}
			model.put("tag_pagination", page);
			model.addAttribute("liveMap",liveMap);
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_PLUG, TPL_LIVE_TICKET_INDEX);
		}
	}
	
	@RequestMapping(value = "/live/ticket/userlist*.jspx")
	public String ticketUserList(Long orderId,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user=CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			BbsOrder order=orderMng.findById(orderId);
			if(order!=null&&order.getBuyUser().equals(user)){
				Pagination page=liveUserMng.getPage(orderId, null, null,
						URLHelper.getPageNo(request), CookieUtils.getPageSize(request));
				model.put("tag_pagination", page);
				model.put("order", order);
				return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_PLUG, TPL_LIVE_TICKET_USER_INDEX);
			}else{
				WebErrors errors=WebErrors.create(request);
				errors.addErrorCode("live.error.notYourOrder");
				return FrontUtils.showError(request, response, model, errors);
			}
		}
	}
	
	@RequestMapping(value = "/live/ticket/assign.jspx")
	public String ticketUserAssign(String username,Long orderId,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user=CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			if(orderId!=null&&StringUtils.isNotBlank(username)){
				BbsUser assignUser=userMng.findByUsername(username);
				BbsOrder order=orderMng.findById(orderId);
				if(order!=null&&assignUser!=null){
					//不能操作他人订单数据
					if(!order.getBuyUser().equals(user)){
						WebErrors errors=WebErrors.create(request);
						errors.addErrorCode("live.error.notYourOrder");
						return FrontUtils.showError(request, response, model, errors);
					}
					//票已全部分配出
					if(!order.getHasLiveNotUsed()){
						WebErrors errors=WebErrors.create(request);
						errors.addErrorCode("live.error.hasNotEnoughTicket");
						return FrontUtils.showError(request, response, model, errors);
					}
					Integer liveId=order.getDataId();
					BbsLive live=manager.findById(liveId);
					if(live!=null){
						liveUserMng.saveUserLiveTicket(order, assignUser.getId(), live.getId());
					}
				}
			}
			return ticketUserList(orderId, request, response, model);
		}
	}
	
	/**
	 * 我参与的live
	 */
	@RequestMapping(value = "/live/live/mylist*.jspx")
	public String myLives(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user=CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			Pagination page=liveUserMng.getPage(null,user.getId(),null,
					URLHelper.getPageNo(request), 20);
			model.put("tag_pagination", page);
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_PLUG, TPL_USER_LIVE_LIST);
		}
	}
	
	@Autowired
	private BbsLiveMng manager;
	@Autowired
	private BbsLiveUserMng liveUserMng;
	@Autowired
	private BbsOrderMng orderMng;
	@Autowired
	private BbsUserMng userMng;
}
