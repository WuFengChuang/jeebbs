package com.jeecms.plug.live.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_PLUG;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.TextMessage;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.google.gson.GsonBuilder;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsGift;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsGiftMng;
import com.jeecms.bbs.manager.BbsGiftUserMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.util.AliPay;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.util.WeixinPay;
import com.jeecms.common.web.Constants;
import com.jeecms.common.web.HttpClientUtil;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.entity.BbsLiveMessage;
import com.jeecms.plug.live.manager.BbsLiveMessageMng;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.manager.BbsLiveUserMng;
import com.jeecms.plug.live.websocket.Message;
import com.jeecms.plug.live.websocket.WebSocketExtHandler;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * 活动live对外公众功能
 */
@Controller
public class BbsFrontLiveAct extends AbstractBbsLive{
	 private static final Logger log = LoggerFactory
	 .getLogger(BbsFrontLiveAct.class);
	 
	public static final String WEIXIN_PAY_URL="weixin.pay.url";
    public static final String ALI_PAY_URL="alipay.openapi.url";

	public static final String TPL_LIVE_INDEX = "tpl.liveIndex";
	public static final String TPL_LIVE_GET = "tpl.liveGet";
	public static final String TPL_LIVE_BUY = "tpl.liveBuy";
	public static final String ALIPAY_MOBILE="tpl.alipay.mobile";
	public static final String TPL_LIVE_DISCUSS_BAIDU = "tpl.liveDiscussBaidu";
	public static final String TPL_LIVE_DISCUSS_TENCENT = "tpl.liveDiscussTencent";
	
	//首页
	@RequestMapping(value = "/live/front/index*.jspx")
	public String index(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_INDEX);
	}
	
	//进入预览页
	@RequestMapping(value = "/live/front/get.jspx")
	public String liveGet(Integer id,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsLive live=null;
		WebErrors errors=WebErrors.create(request);
		if(id!=null){
			live=manager.findById(id);
		}else{
			errors.addErrorCode("error.required",id);
		}
		if(live!=null){
			if(live.getCheckStatus().equals(BbsLive.CHECKING)){
				errors.addErrorCode("live.error.checking");
			}else if(live.getCheckStatus().equals(BbsLive.REJECT)){
				errors.addErrorCode("live.error.reject");
			}else if(live.getCheckStatus().equals(BbsLive.STOP)){
				errors.addErrorCode("live.error.stop");
			}
			model.addAttribute("live", live);
		}else{
			errors.addErrorCode("error.required",live);
		}
		FrontUtils.frontData(request, model, site);
		if(errors.hasErrors()){
			return FrontUtils.showError(request, response, model, errors);
		}else{
			List<BbsGift>gifts=bbsGiftMng.getList(false, 0, 100);
			CmsConfig config=site.getConfig();
			model.addAttribute("live", live);
			model.addAttribute("config", config);
			model.addAttribute("gifts", gifts);
			return FrontUtils.getTplPath(request, site.getSolutionPath(),
					TPLDIR_PLUG, TPL_LIVE_GET);
		}
	}
	
	//进入讨论页
	@RequestMapping(value = "/live/front/discuss.jspx")
	public String liveDiscuss(Integer id,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user=CmsUtils.getUser(request);
		BbsLive live=null;
		WebErrors errors=WebErrors.create(request);
		if(id!=null){
			live=manager.findById(id);
		}else{
			errors.addErrorCode("error.required",id);
		}
		if(live!=null){
			if(live.getCheckStatus().equals(BbsLive.CHECKING)){
				errors.addErrorCode("live.error.checking");
			}else if(live.getCheckStatus().equals(BbsLive.REJECT)){
				errors.addErrorCode("live.error.reject");
			}else if(live.getCheckStatus().equals(BbsLive.STOP)){
				errors.addErrorCode("live.error.stop");
			}
			//收费模式
			if(live.isCharge()){
				if(user==null){
					session.setAttribute(request, response, "loginSource", "charge");
					return FrontUtils.showLogin(request, model, site);
				}else{
					//非主讲者且未购买
					if(!live.getUser().equals(user)){
						//用户已登录判断是否已经购买
						boolean hasBuy=liveUserMng.hasBuyLive(user.getId(), live.getId());
						//达限制人数(且用户未购买)，不可以报名
						if(!live.hasEnoughUserNum()&&!hasBuy){
							errors.addErrorCode("live.error.hasNotEnoughUserNum");
							return FrontUtils.showError(request, response, model, errors);
						}
						if(!hasBuy){
							try {
								String rediretUrl="/live/front/buy.jspx?liveId="+live.getId();
								if(StringUtils.isNotBlank(site.getContextPath())){
									rediretUrl=site.getContextPath()+rediretUrl;
								}
								response.sendRedirect(rediretUrl);
							} catch (IOException e) {
								//e.printStackTrace();
							}
						}
					}
				}
			}
			List<BbsGift>gifts=bbsGiftMng.getList(false, 0, 100);
			CmsConfig config=site.getConfig();
			model.addAttribute("live", live);
			model.addAttribute("config", config);
			model.addAttribute("gifts", gifts);
		}
		FrontUtils.frontData(request, model, site);
		if(errors.hasErrors()){
			return FrontUtils.showError(request, response, model, errors);
		}else{
			if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_BAIDU)){
				return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_PLUG, TPL_LIVE_DISCUSS_BAIDU);
			}else{
				return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_PLUG, TPL_LIVE_DISCUSS_TENCENT);
			}
		}
	}
	//赠送礼物给主播
	@RequestMapping(value = "/live/front/sendGiftToHost.jspx")
	public void sendGiftToHost(Integer liveId,Integer giftId,
			Integer num,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user=CmsUtils.getUser(request);
		BbsLive live=null;
		BbsGift gift = null;
		BbsUser giftReceiverUser=null;
		if(liveId!=null){
			live=manager.findById(liveId);
			if(live!=null){
				giftReceiverUser=live.getUser();
			}
		}
		if(giftId!=null){
			gift=bbsGiftMng.findById(giftId);
		}
		if(num==null){
			num=1;
		}
		FrontUtils.frontData(request, model, site);
		Boolean succ=false;
		boolean canSend=true;
		Integer status=1;
		JSONObject json=new JSONObject();
		if(live!=null&&user!=null&&gift!=null){
			//非主讲者且未购买
			if(!live.getUser().equals(user)){
				//判断是否已经购买
				boolean hasBuy=liveUserMng.hasBuyLive(user.getId(), live.getId());
				if(!hasBuy){
					canSend=false;
					status=2;
				}
			}
			if(canSend){
				int giveStatus=bbsGiftUserMng.giveUserGift(
						gift, user, giftReceiverUser,liveId,num);
				if(giveStatus==1){
					String giftMessage=MessageResolver.getMessage(request, 
							"live.sendGiftMessage", user.getUsername(),num,gift.getName());
					succ=true;
					Message msg = new Message();
					msg.setDate(new Date());
					msg.setFrom(user.getId());
					msg.setFromName(user.getUsername());
					msg.setTo(0);
					msg.setText(giftMessage);
					try {
						socketHandler.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
					} catch (IOException e) {
						log.error(e.getMessage());
					}
					liveMessageMng.speak(live, user, null,giftMessage,BbsLiveMessage.MST_TYPE_GIFT);
				}else{
					//礼物数量不足
					status=3;
				}
			}
		}
		try {
			json.put("succ", succ.toString());
			//status 2用户不是live参与者   3礼物数量不足需要购买
			json.put("status", status);
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	
	/**
	 * 购买live门票
	 * @param liveId liveID
	 * @param self 1自用  2赠送  默认1
	 * @param num 购买数量  默认1
	 */
	@RequestMapping(value = "/live/front/buy.jspx")
	public String liveBuy(Integer liveId,Integer self,Integer num,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		BbsUser user=CmsUtils.getUser(request);
		CmsSite site=CmsUtils.getSite(request);
		if(self==null){
			self=1;
		}
		if(num==null){
			num=1;
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			if(liveId==null){
				errors.addErrorCode("error.required","topicId");
				return FrontUtils.showError(request, response, model, errors);
			}else{
				BbsLive live=manager.findById(liveId);
			    if(live!=null){
			    	Double needAmount=live.getBeginPrice()*num;
			    	Date now=Calendar.getInstance().getTime();
			    	if(now.after(live.getEndTime())){
			    		needAmount=live.getAfterPrice()*num;
			    	}
			  	    if(needAmount<=0){
			  	    	errors.addErrorCode("error.chargeAmountError");
			  	    	return FrontUtils.showError(request, response, model, errors);
			  	    }else{
			  	    	String ua = ((HttpServletRequest) request).getHeader("user-agent")
					  	          .toLowerCase();
				  		boolean webCatBrowser=false;
				  		String wxopenid=null;
			  	        if (ua.indexOf("micromessenger") > 0) {
			  	        	// 是微信浏览器
			  	        	webCatBrowser=true;
			  	        	wxopenid=(String) session.getAttribute(request, "wxopenid");
			  	        }
			  	    	String orderNumber=System.currentTimeMillis()+RandomStringUtils.random(5,Num62.N10_CHARS);
			  	    	FrontUtils.frontData(request, model, site);
				  		model.addAttribute("liveId", liveId);
				  		model.addAttribute("orderNumber", orderNumber);
				  		model.addAttribute("needAmount", needAmount);
				  		model.addAttribute("live", live);
				  		model.addAttribute("self", self);
				  		model.addAttribute("num", num);
				  		model.addAttribute("webCatBrowser", webCatBrowser);
				  		model.addAttribute("wxopenid", wxopenid);
				  		return FrontUtils.getTplPath(request, site.getSolutionPath(),
								TPLDIR_PLUG, TPL_LIVE_BUY);
			  	    }
			    }else{
			    	errors.addErrorCode("error.beanNotFound","topic");
			    	return FrontUtils.showError(request, response, model, errors);
			    }
			}
		}
	}
	
	/**
	 * 购买live门票进入支付页面
	 * 选择支付方式
	 * @param liveId liveID
	 * @param orderNumber 订单号
	 * @param self 1自用  2赠送  默认1
	 * @param num 购买数量  默认1
	 * @param payMethod 支付方式 1微信扫码 2支付宝即时支付  3微信浏览器打开[微信移动端] 4支付宝扫码5支付宝手机网页
	 */
	@RequestMapping(value = "/order/liveSelectPay.jspx")
	public String liveSelectPay(Integer liveId,String orderNumber,Integer num,
			Integer payMethod,Integer self,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		BbsUser user=CmsUtils.getUser(request);
		CmsSite site=CmsUtils.getSite(request);
		initWeiXinPayUrl();
		initAliPayUrl();
		if(num==null){
			num=1;
		}
		if(liveId==null){
			errors.addErrorCode("error.required","liveId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			BbsLive live=manager.findById(liveId);
		    if(live!=null){
		    	//收费模式金额必须大于0
		    	Double needAmount=live.getBeginPrice()*num;
		    	Date now=Calendar.getInstance().getTime();
		    	if(now.after(live.getEndTime())){
		    		needAmount=live.getAfterPrice()*num;
		    	}
		    	if(needAmount<=0){
		  	    	errors.addErrorCode("error.chargeAmountError");
		  	    	return FrontUtils.showError(request, response, model, errors);
		  	    }else{
		  	    	BbsConfigCharge config=configChargeMng.getDefault();
  	    			Double totalAmount=needAmount;
  	    			boolean selfOnly=true;
  	    			if(self!=null&&self.equals(2)){
  	    				selfOnly=false;
  	    			}
		  	    	if(user!=null){
		  	    		cache.put(new Element(orderNumber,
			  	    			BbsOrder.ORDER_TYPE_CACHE_FLAG_LIVE+
			  	    			","+liveId+","+user.getId()+","+needAmount+","+selfOnly+","+num));
		  	    	}else{
		  	    		cache.put(new Element(orderNumber,
		  	    				BbsOrder.ORDER_TYPE_CACHE_FLAG_LIVE+
		  	    				","+liveId+",,"+needAmount+","+selfOnly+","+num));
		  	    	}
		  	    	if(payMethod!=null){
		  	    		if(payMethod==1){
		  	    			return WeixinPay.enterWeiXinPay(getWeiXinPayUrl(),config,
									orderNumber,totalAmount,live.getTitle(),
		  	    					live.getUrlWhole(),BbsOrder.PAY_TARGET_LIVE,
		  	    					request, response, model);
		  	    		}else if(payMethod==3){
		  	    			String openId=(String) session.getAttribute(request, "wxopenid");
		  	    			return WeixinPay.weixinPayByMobile(getWeiXinPayUrl(), 
		  	    					config, openId,orderNumber, totalAmount, live.getTitle(),
		  	    					live.getUrlWhole(),BbsOrder.PAY_TARGET_LIVE,request, response, model);
		  	    		}else if(payMethod==2){
		  	    			return AliPay.enterAliPayImmediate(config, orderNumber,
		  	    					totalAmount, live.getTitle(), live.getUrlWhole(), 
		  	    					 live.getUrlWhole(), BbsOrder.PAY_TARGET_LIVE,
		  	    					 request, response, model);
		  	    		}else if(payMethod==4){
		  	    			return AliPay.enterAlipayScanCode(request, response, model,
		  	    					getAliPayUrl(), config, live.getTitle(), live.getUrlWhole(),
		  	    					orderNumber, totalAmount,BbsOrder.PAY_TARGET_LIVE);
		  	    		}else if(payMethod==5){
		  	    			model.addAttribute("orderNumber",orderNumber);
		  					model.addAttribute("live", live);
		  					model.addAttribute("num", num);
		  					model.addAttribute("amount", needAmount);
		  					model.addAttribute("returnUrl", live.getUrlWhole());
		  	    			FrontUtils.frontData(request, model, site);
		  					return FrontUtils.getTplPath(request, site.getSolutionPath(),
		  							TPLDIR_PLUG, ALIPAY_MOBILE);
		  	    		}
					}//支付宝
		  	    	return AliPay.enterAliPayImmediate(config, orderNumber, totalAmount, 
		  	    			live.getTitle(), live.getUrlWhole(), live.getUrlWhole(), 
		  	    			BbsOrder.PAY_TARGET_LIVE,request, response, model);
		  	    }
		    }else{
		    	errors.addErrorCode("error.beanNotFound","live");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	//购买live进入手机支付宝支付页面
	@RequestMapping(value = "/live/front/alipayInMobile.jspx")
	public String enterAlipayInMobile(Integer liveId,String orderNumber,Integer num,
			HttpServletRequest request,HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		initAliPayUrl();
		if(liveId==null){
			errors.addErrorCode("error.required","liveId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			BbsLive live=manager.findById(liveId);
			BbsConfigCharge config=configChargeMng.getDefault();
			if(live!=null){
				Double totalAmount=live.getBeginPrice()*num;
		    	Date now=Calendar.getInstance().getTime();
		    	if(now.after(live.getEndTime())){
		    		totalAmount=live.getAfterPrice()*num;
		    	}
				AliPay.enterAlipayInMobile(request, response, getAliPayUrl(), config,
						live.getTitle(), live.getUrlWhole(), orderNumber, totalAmount);
				return "";
			}else{
		    	errors.addErrorCode("error.beanNotFound","live");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	
	/**
	 * 购买live微信支付微信回调
	 * @param code
	 */
	@RequestMapping(value = "/live/order/payCallByWeiXin.jspx")
	public void orderPayCallByWeiXin(String orderNumber,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws JDOMException, IOException, JSONException {
		JSONObject json = new JSONObject();
		CmsSite site=CmsUtils.getSite(request);
		BbsConfigCharge config=configChargeMng.getDefault();
		if (StringUtils.isNotBlank(orderNumber)) {
			BbsOrder order=orderMng.findByOrderNumber(orderNumber);
			if (order!=null&&StringUtils.isNotBlank(order.getOrderNumWeixin())) {
				//已成功支付过
				WeixinPay.noticeWeChatSuccess(getWeiXinPayUrl());
				json.put("status", 4);
			} else {
				//订单未成功支付
				json.put("status", 6);
			}
		}else{
			// 回调结果
			String xml_receive_result = PayUtil.getWeiXinResponse(request);
			if (StringUtils.isBlank(xml_receive_result)) {
				//检测到您可能没有进行扫码支付，请支付
				json.put("status", 5);
			} else {
				Map<String, String> result_map = PayUtil.parseXMLToMap(xml_receive_result);
				String sign_receive = result_map.get("sign");
				result_map.remove("sign");
				String key = config.getWeixinPassword();
				if (key == null) {
					//微信扫码支付密钥错误，请通知商户
					json.put("status", 1);
				}
				String checkSign = PayUtil.createSign(result_map, key);
				if (checkSign != null && checkSign.equals(sign_receive)) {
					try {
						if (result_map != null) {
							String return_code = result_map.get("return_code");
							if ("SUCCESS".equals(return_code)
									&& "SUCCESS".equals(result_map
											.get("result_code"))) {
								// 微信返回的微信订单号（属于微信商户管理平台的订单号，跟自己的系统订单号不一样）
								String transaction_id = result_map
										.get("transaction_id");
								// 商户系统的订单号，与请求一致。
								String out_trade_no = result_map.get("out_trade_no");
								// 通知微信该订单已处理
								WeixinPay.noticeWeChatSuccess(getWeiXinPayUrl());
								payAfter(site,out_trade_no,config.getChargeRatio(),
										transaction_id, null);
								//支付成功
								json.put("status", 0);
							} else if ("SUCCESS".equals(return_code)
									&& result_map.get("err_code") != null) {
								String message = result_map.get("err_code_des");
								json.put("status", 2);
								json.put("error", message);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Map<String, String> parames = new HashMap<String, String>();
					parames.put("return_code", "FAIL");
					parames.put("return_msg", "校验错误");
					// 将参数转成xml格式
					String xmlWeChat = PayUtil.assembParamToXml(parames);
					try {
						HttpClientUtil.post(getWeiXinPayUrl(), xmlWeChat, Constants.UTF8);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//支付参数错误，请重新支付!
					json.put("status", 3);
				}
			}
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	//购买live支付宝即时支付回调地址
	@RequestMapping(value = "/live/order/payCallByAliPay.jspx")
	public String payCallByAliPay(HttpServletRequest request,
			HttpServletResponse response, ModelMap model)
					throws UnsupportedEncodingException {
		BbsConfigCharge config=configChargeMng.getDefault();
		CmsSite site=CmsUtils.getSite(request);
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		//商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		FrontUtils.frontData(request, model, site);
		if(PayUtil.verifyAliPay(params,config.getAlipayPartnerId(),config.getAlipayKey())){//验证成功
			if(trade_status.equals("TRADE_FINISHED")||trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
				String nextUrl=payAfter(site,out_trade_no,config.getChargeRatio(),
						null, trade_no);
				try {
					response.sendRedirect(nextUrl);
				} catch (IOException e) {
					//e.printStackTrace();
				}
				return nextUrl;
				//注意：TRADE_FINISHED
				//该种交易状态只在两种情况下出现
				//1、开通了普通即时到账，买家付款成功后。
				//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
				//TRADE_SUCCESS
				//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
			}
		}else{//验证失败
			return  FrontUtils.showMessage(request, model,"error.alipay.status.valifail");
		}
		return  FrontUtils.showMessage(request, model,"error.alipay.status.payfail");
	}
	
	//购买live，手机支付宝查询订单状态（扫码支付和手机网页支付均由此处理订单）
	@RequestMapping(value = "/live/order/orderQuery.jspx")
	public void aliPayOrderQuery(String orderNumber,HttpServletRequest request,
			HttpServletResponse response, ModelMap model)
					throws UnsupportedEncodingException {
		BbsConfigCharge config=configChargeMng.getDefault();
		JSONObject json = new JSONObject();
		CmsSite site=CmsUtils.getSite(request);
		initAliPayUrl();
		FrontUtils.frontData(request, model, site);
		AlipayTradeQueryResponse res=AliPay.query(getAliPayUrl(), config,
				orderNumber,null);
		try {
			if (null != res && res.isSuccess()) {
				if (res.getCode().equals("10000")) {
					if ("TRADE_SUCCESS".equalsIgnoreCase(res
							.getTradeStatus())) {
							json.put("status", 0);
							payAfter(site,orderNumber, config.getChargeRatio(),
									null, res.getTradeNo());
					} else if ("WAIT_BUYER_PAY".equalsIgnoreCase(res
							.getTradeStatus())) {
						// 等待用户付款状态，需要轮询查询用户的付款结果
						json.put("status", 1);
					} else if ("TRADE_CLOSED".equalsIgnoreCase(res.getTradeStatus())) {
						// 表示未付款关闭，或已付款的订单全额退款后关闭
						json.put("status", 2);
					} else if ("TRADE_FINISHED".equalsIgnoreCase(res.getTradeStatus())) {
						// 此状态，订单不可退款或撤销
						json.put("status", 0);
					}
				} else {
					// 如果请求未成功，请重试
					json.put("status", 3);
				}
			}else{
				json.put("status", 4);
			}
		} catch (JSONException e) {
				e.printStackTrace();
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	//支付后处理流程【保存订单、门票处理、数据统计】
	private String payAfter(CmsSite site,String orderNumber,Double ratio,
			String weixinOrderNum,String alipyOrderNum){
		Element e = cache.get(orderNumber);
	    BbsLive live = null;
		if(e!=null&&StringUtils.isNotBlank(orderNumber)){
		    BbsOrder b=orderMng.findByOrderNumber(orderNumber);
		    //不能重复提交
		    if(b==null){
		    	Object obj= e.getObjectValue();
		    	String cacheValue=obj.toString();
		    	if(StringUtils.isNotBlank(cacheValue)&&
		    			cacheValue.startsWith(BbsOrder.PAY_TARGET_LIVE)){
		    		String[] objArray = null;
					if(obj!=null){
						objArray=cacheValue.split(",");
					}
					Integer buyUserId=null;
					Integer buynum=1;
					String orderType="";
					Integer liveId=null;
					String outOrderNum="";
					boolean selfOnly=true;
					if(objArray!=null){
						if(objArray[2]!=null&&StringUtils.isNotBlank(objArray[2])){
							buyUserId=Integer.parseInt(objArray[2]);
						}
						if(objArray[0]!=null&&StringUtils.isNotBlank(objArray[0])){
							orderType=objArray[0];
						}
						if(objArray[1]!=null){
							liveId=Integer.parseInt(objArray[1]) ;
						}
						if(objArray[5]!=null){
							buynum=Integer.parseInt(objArray[5]) ;
						}
						if(objArray[4]!=null&&StringUtils.isNotBlank(objArray[4])
								&&!objArray[4].equals("null")){
							selfOnly=Boolean.valueOf(objArray[4]);
						}
					}
				    if(liveId!=null){
				    	live=manager.findById(liveId);
			 			Integer outOrderType=BbsOrder.PAY_METHOD_WECHAT;
			 			if(StringUtils.isNotBlank(weixinOrderNum)){
			 				outOrderNum=weixinOrderNum;
			 			}
			 			if(StringUtils.isNotBlank(alipyOrderNum)){
			 				outOrderNum=alipyOrderNum;
			 				outOrderType=BbsOrder.PAY_METHOD_ALIPAY;
			 			}
			 			manager.liveOrder(live, buynum, outOrderType,
			 					orderNumber,outOrderNum, buyUserId,selfOnly);
				 	}
		    	}
		    }
		}
		if(live!=null){
			return live.getUrlWhole();
		}else{
			return site.getUrl();
		}
	}
	

	private void initAliPayUrl(){
		if(getAliPayUrl()==null){
			setAliPayUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),ALI_PAY_URL));
		}
	}
	
	private void initWeiXinPayUrl(){
		if(getWeiXinPayUrl()==null){
			setWeiXinPayUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),WEIXIN_PAY_URL));
		}
	}
	
	private String weiXinPayUrl;
	
	private String aliPayUrl;
	private String weixinAuthCodeUrl;
	
	public String getWeiXinPayUrl() {
		return weiXinPayUrl;
	}

	public void setWeiXinPayUrl(String weiXinPayUrl) {
		this.weiXinPayUrl = weiXinPayUrl;
	}

	public String getAliPayUrl() {
		return aliPayUrl;
	}

	public void setAliPayUrl(String aliPayUrl) {
		this.aliPayUrl = aliPayUrl;
	}
	
	public String getWeixinAuthCodeUrl() {
		return weixinAuthCodeUrl;
	}

	public void setWeixinAuthCodeUrl(String weixinAuthCodeUrl) {
		this.weixinAuthCodeUrl = weixinAuthCodeUrl;
	}
	
	@Autowired
	private BbsLiveMng manager;
	@Autowired
	private SessionProvider session;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private BbsOrderMng orderMng;
	@Autowired
	@Qualifier("OrderTemp")
	private Ehcache cache;
	@Autowired
	private BbsLiveUserMng liveUserMng;
	@Autowired
	private BbsGiftMng bbsGiftMng;
	@Autowired
	private BbsGiftUserMng bbsGiftUserMng;
	@Autowired
	private BbsUserMng userMng;
	@Resource
	WebSocketExtHandler socketHandler;
	@Autowired
	private BbsLiveMessageMng liveMessageMng;
}
