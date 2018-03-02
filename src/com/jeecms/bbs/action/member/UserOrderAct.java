package com.jeecms.bbs.action.member;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.common.page.SimplePage.cpn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsTopicChargeMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

/**
 * 用户账户相关
 * 包含笔者所写文章被用户购买记录
 * 自己的消费记录
 */
@Controller
public class UserOrderAct {
	
	public static final String MEMBER_BUY_LIST = "tpl.memberBuyList";
	public static final String MEMBER_ORDER_LIST = "tpl.memberOrderList";
	public static final String TOPIC_CHARGE_LIST = "tpl.memberChargeList";

	/**
	 * 自己消费记录
	 * @param pageNo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/buy_list.jspx")
	public String buyList(String orderNum,Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (pageNo==null) {
			pageNo=1;
		}
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		Pagination pagination=orderMng.getPage(orderNum,user.getId(),
				null,null,null,null,cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("tag_pagination",pagination);
		model.addAttribute("pageNo",pageNo);
		setDataMap(pagination, model);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_BUY_LIST);
	}
	
	/**
	 * 订单列表(被购买记录)
	 * @param pageNo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/order_list.jspx")
	public String orderList(String orderNum,Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		if (pageNo==null) {
			pageNo=1;
		}
		if(user.getUserAccount()==null){
			WebErrors errors=WebErrors.create(request);
			errors.addErrorCode("error.userAccount.notfound");
			return FrontUtils.showError(request, response, model, errors);
		}
		Pagination pagination=orderMng.getPage(orderNum, null, user.getId(),
				null,BbsOrder.ORDER_TYPE_TOPIC,null,
				cpn(pageNo), CookieUtils.getPageSize(request));
		setDataMap(pagination, model);
		model.addAttribute("tag_pagination",pagination);
		model.addAttribute("pageNo",pageNo);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, MEMBER_ORDER_LIST);
	}
	
	/**
	 * 我的主题收益列表
	 * @param pageNo
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/charge_list.jspx")
	public String contentChargeList(Integer orderBy,
			Integer pageNo,HttpServletRequest request,
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
		if(orderBy==null){
			orderBy=1;
		}
		if (pageNo==null) {
			pageNo=1;
		}
		Pagination pagination=topicChargeMng.getPage(null,user.getId(),
				null,null,orderBy,cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("tag_pagination",pagination);
		model.addAttribute("orderBy", orderBy);
		model.addAttribute("pageNo", pageNo);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_MEMBER, TOPIC_CHARGE_LIST);
	}
	
	private void setDataMap(Pagination pagination,ModelMap model){
		List<BbsOrder> li=(List<BbsOrder>) pagination.getList();
		Map<String,BbsTopic>topicMap=new HashMap<String,BbsTopic>();
		Map<String,BbsCommonMagic>magicMap=new HashMap<String,BbsCommonMagic>();
		for(BbsOrder order:li){
			if(order.getDataType().equals(BbsOrder.ORDER_TYPE_TOPIC)){
				topicMap.put(order.getDataId().toString(), topicMng.findById(order.getDataId()));
			}else if(order.getDataType().equals(BbsOrder.ORDER_TYPE_MAGIC)){
				//topicMap.put(order.getDataId().toString(), null);
				magicMap.put(order.getDataId().toString(),magicMng.findById(order.getDataId()));
			}
		}
		model.addAttribute("topicMap",topicMap);
		model.addAttribute("magicMap",magicMap);
	}
	
	
	
	@Autowired
	private BbsOrderMng orderMng;
	@Autowired
	private BbsTopicChargeMng topicChargeMng;
	@Autowired
	private BbsTopicMng topicMng;
	@Autowired
	private BbsCommonMagicMng magicMng;
}
