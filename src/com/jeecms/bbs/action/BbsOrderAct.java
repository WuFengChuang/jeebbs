package com.jeecms.bbs.action;

import static com.jeecms.common.page.SimplePage.cpn;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.annotations.AttributeAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsTopicChargeMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsUserAccountMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;

/**
 * 订单相关
 * @author tom
 *
 */
@Controller
public class BbsOrderAct {
	
	/**
	 * 主题收费统计列表
	 * @param orderBy
	 */
	@RequiresPermissions("order:charge_list")
	@RequestMapping("/order/charge_list.do")
	public String chargeList(String title,String author,
			Date buyTimeBegin,Date buyTimeEnd,
			Integer orderBy,Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if(orderBy==null){
			orderBy=1;
		}
		Integer authorUserId=null;
		if(StringUtils.isNotBlank(author)){
			BbsUser authorUser=userMng.findByUsername(author);
			if(authorUser!=null){
				authorUserId=authorUser.getId();
			}else{
				authorUserId=0;
			}
		}
		Pagination pagination = topicChargeMng.getPage(title,
				authorUserId,buyTimeBegin, buyTimeEnd,orderBy,
				cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination",pagination);
		model.addAttribute("pageNo",pagination.getPageNo());
		model.addAttribute("orderBy",orderBy);
		model.addAttribute("title",title);
		model.addAttribute("author",author);
		model.addAttribute("buyTimeBegin",buyTimeBegin);
		model.addAttribute("buyTimeEnd",buyTimeEnd);
		return "order/charge_list";
	}
	
	/**
	 * 作者所得统计
	 * @param orderBy
	 * @return
	 */
	@RequiresPermissions("order:user_account_list")
	@RequestMapping("/order/user_account_list.do")
	public String userAccountList(String username,
			Date drawTimeBegin,Date drawTimeEnd,
			Integer orderBy,Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if(orderBy==null){
			orderBy=1;
		}
		Pagination pagination = userAccountMng.getPage(username,
				drawTimeBegin,drawTimeEnd,orderBy, 
				cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination",pagination);
		model.addAttribute("pageNo",pagination.getPageNo());
		model.addAttribute("orderBy",orderBy);
		model.addAttribute("username",username);
		model.addAttribute("drawTimeBegin",drawTimeBegin);
		model.addAttribute("drawTimeEnd",drawTimeEnd);
		return "order/user_account_list";
	}
	
	/**
	 * 用户购买列表
	 * @param orderBy
	 * @param pageNo
	 * @param request
	 * @param model
	 * @return
	 */
	@RequiresPermissions("order:user_order_list")
	@RequestMapping("/order/user_order_list.do")
	public String userBuyList(String orderNum,String buyusername,
			String authorusername,Short payMode,Short dataType,Integer dataId,
			Integer pageNo,HttpServletRequest request, ModelMap model) {
		Integer buyUserId=null,authorUserId=null;
		if(StringUtils.isNotBlank(buyusername)){
			BbsUser u=userMng.findByUsername(buyusername);
			if(u!=null){
				buyUserId=u.getId();
			}
		}
		if(StringUtils.isNotBlank(authorusername)){
			BbsUser u=userMng.findByUsername(authorusername);
			if(u!=null){
				authorUserId=u.getId();
			}
		}
		//0默认所有
		if(payMode==null){
			payMode=0;
		}
		//-1默认所有
		if(dataType==null){
			dataType=-1;
		}
		Pagination pagination = orderMng.getPage(orderNum, buyUserId, authorUserId, 
				payMode,dataType,dataId,cpn(pageNo), CookieUtils.getPageSize(request));
		List<BbsOrder> li=(List<BbsOrder>) pagination.getList();
		Map<String,BbsTopic>topicMap=new HashMap<String,BbsTopic>();
		Map<String,BbsCommonMagic>magicMap=new HashMap<String,BbsCommonMagic>();
		for(BbsOrder order:li){
			if(order.getDataType().equals(BbsOrder.ORDER_TYPE_TOPIC)){
				topicMap.put(order.getDataId().toString(), topicMng.findById(order.getDataId()));
			}else{
				//topicMap.put(order.getDataId().toString(), null);
				magicMap.put(order.getDataId().toString(),magicMng.findById(order.getDataId()));
			}
		}
		List<BbsCommonMagic>magicList=magicMng.getList();
		model.addAttribute("topicMap",topicMap);
		model.addAttribute("magicMap",magicMap);
		model.addAttribute("pagination",pagination);
		model.addAttribute("pageNo",pagination.getPageNo());
		model.addAttribute("orderNum",orderNum);
		model.addAttribute("buyusername",buyusername);
		model.addAttribute("authorusername",authorusername);
		model.addAttribute("payMode",payMode);
		model.addAttribute("dataType",dataType);
		model.addAttribute("dataId",dataId);
		model.addAttribute("magicList",magicList);
		return "order/order_list";
	}
	
	/**
	 * 平台佣金汇总统计
	 * @return
	 */
	@RequiresPermissions("order:commissionStatic")
	@RequestMapping("/order/commissionStatic.do")
	public String commissionStatic(HttpServletRequest request, ModelMap model) {
		BbsConfigCharge config= configChargeMng.getDefault();
		model.addAttribute("config",config);
		return "order/commission";
	}
	
	@Autowired
	private BbsTopicChargeMng topicChargeMng;
	@Autowired
	private BbsUserAccountMng userAccountMng;
	@Autowired
	private BbsOrderMng orderMng;
	@Autowired
	private BbsUserMng userMng;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private BbsTopicMng topicMng;
	@Autowired
	private BbsCommonMagicMng magicMng;
	
	
}