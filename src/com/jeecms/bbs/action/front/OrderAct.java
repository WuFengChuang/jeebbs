package com.jeecms.bbs.action.front;


import static com.jeecms.bbs.Constants.TPLDIR_SPECIAL;
import static com.jeecms.common.page.SimplePage.cpn;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.util.StrUtils;
import com.jeecms.common.util.WeixinPay;
import com.jeecms.common.web.Constants;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.HttpClientUtil;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.jeecms.bbs.MagicConstants;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsGift;
import com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicCharge;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsGiftMng;
import com.jeecms.bbs.manager.BbsGiftUserMng;
import com.jeecms.bbs.manager.BbsIncomeStatisticMng;
import com.jeecms.bbs.manager.BbsMagicLogMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsTopicChargeMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsUserAccountMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.AliPay;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;

@Controller
public class OrderAct {
	//收费
	public static final Integer CONTENT_PAY_MODEL_CHARGE=1;
	//打赏
	public static final Integer CONTENT_PAY_MODEL_REWARD=2;
	public static final String WEIXIN_PAY_URL="weixin.pay.url";
	public static final String ALI_PAY_URL="alipay.openapi.url";
	
	public static final String ORDER_AD_TITLE="order.ad.title";
	

	public static final String REWARD="tpl.reward";
	public static final String ALIPAY_MOBILE="tpl.alipay.mobile";
	public static final String TOPIC_ORDERS="tpl.orders";
	public static final String WEIXIN_AUTH_CODE_URL ="weixin.auth.getCodeUrl";
	public static final String COMMON_BUY="tpl.commonBuy";
	
	//支付购买（先选择支付方式，在进行支付）
	@RequestMapping(value = "/topic/buy.jspx")
	public String topicBuy(Integer topicId,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		BbsUser user=CmsUtils.getUser(request);
		CmsSite site=CmsUtils.getSite(request);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			if(topicId==null){
				errors.addErrorCode("error.required","topicId");
				return FrontUtils.showError(request, response, model, errors);
			}else{
				BbsTopic topic=topicMng.findById(topicId);
			    if(topic!=null){
			  	    if(topic.getChargeAmount()<=0){
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
				  		model.addAttribute("topicId", topicId);
				  		model.addAttribute("orderNumber", orderNumber);
				  		model.addAttribute("topic", topic);
				  		model.addAttribute("type", BbsTopicCharge.MODEL_CHARGE);
				  		model.addAttribute("webCatBrowser", webCatBrowser);
				  		model.addAttribute("wxopenid", wxopenid);
				  		return FrontUtils.getTplPath(request, site.getSolutionPath(),
								TPLDIR_SPECIAL, REWARD);
			  	    }
			    }else{
			    	errors.addErrorCode("error.beanNotFound","topic");
			    	return FrontUtils.showError(request, response, model, errors);
			    }
			}
		}
	}
	
	//打赏（先选择打赏金额，在选择支付方式）
	@RequestMapping(value = "/topic/reward.jspx")
	public String topicReward(Integer topicId,String code,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		CmsSite site=CmsUtils.getSite(request);
		if(topicId==null){
			errors.addErrorCode("error.required","topicId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			BbsTopic topic=topicMng.findById(topicId);
		    if(topic!=null){
	  	    	String ua = ((HttpServletRequest) request).getHeader("user-agent")
			  	          .toLowerCase();
		  		boolean webCatBrowser=false;
		  		String wxopenid=null;
	  	        if (ua.indexOf("micromessenger") > 0) {
	  	        	// 是微信浏览器
	  	        	webCatBrowser=true;
	  	        	wxopenid=(String) session.getAttribute(request, "wxopenid");
	  	        }
	  	      
	  	        BbsConfigCharge config=configChargeMng.getDefault(); 
				Double max=config.getRewardMax();
				Double min=config.getRewardMin();
				List<Double>randomList=new ArrayList<Double>();
				Double s=1d;
				for(int i=0;i<6;i++){
					s=StrUtils.retainTwoDecimal(min + ((max - min) * new Random().nextDouble()));
					randomList.add(s);
				}
	  	    	String orderNumber=System.currentTimeMillis()+RandomStringUtils.random(5,Num62.N10_CHARS);
	  	    	FrontUtils.frontData(request, model, site);
	  			model.addAttribute("topicId", topicId);
		  		model.addAttribute("orderNumber", orderNumber);
		  		model.addAttribute("topic", topic);
		  		model.addAttribute("type", BbsTopicCharge.MODEL_REWARD);
		  		model.addAttribute("webCatBrowser", webCatBrowser);
		  		model.addAttribute("wxopenid", wxopenid);
		  		model.addAttribute("randomList", randomList);
		  		model.addAttribute("randomOne", s);
		  		return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_SPECIAL, REWARD);
		    }else{
		    	errors.addErrorCode("error.beanNotFound","topic");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	
	/**
	 * 道具购买
	 * @param mid 道具标识
	 */
	@RequestMapping(value = "/magic/buy.jspx")
	public String magicBuy(String mid,Integer buyNum,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		BbsUser user=CmsUtils.getUser(request);
		CmsSite site=CmsUtils.getSite(request);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			if(mid==null){
				errors.addErrorCode("error.required","mid");
				return FrontUtils.showError(request, response, model, errors);
			}else{
				BbsCommonMagic magic = magicMng.findByIdentifier(mid);
			    if(magic!=null){
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
			  		model.addAttribute("orderNumber", orderNumber);
			  		model.addAttribute("magic", magic);
			  		model.addAttribute("buyNum", buyNum);
			  		model.addAttribute("type", BbsTopicCharge.MODEL_CHARGE);
			  		model.addAttribute("webCatBrowser", webCatBrowser);
			  		model.addAttribute("wxopenid", wxopenid);
			  		return FrontUtils.getTplPath(request, site.getSolutionPath(),
							TPLDIR_SPECIAL, COMMON_BUY);
			    }else{
			    	errors.addErrorCode("error.beanNotFound","magic");
			    	return FrontUtils.showError(request, response, model, errors);
			    }
			}
		}
	}
	
	/**
	 * 道具购买
	 * @param giftId 礼物Id
	 */
	@RequestMapping(value = "/gift/buy.jspx")
	public String giftBuy(Integer giftId,Integer buyNum,
			String returnUrl,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		BbsUser user=CmsUtils.getUser(request);
		CmsSite site=CmsUtils.getSite(request);
		if(buyNum==null){
			buyNum=1;
		}
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			if(giftId==null){
				errors.addErrorCode("error.required","giftId");
				return FrontUtils.showError(request, response, model, errors);
			}else{
				BbsGift gift = giftMng.findById(giftId);
			    if(gift!=null){
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
			  		model.addAttribute("orderNumber", orderNumber);
			  		model.addAttribute("gift", gift);
			  		model.addAttribute("buyNum", buyNum);
			  		model.addAttribute("returnUrl", returnUrl);
			  		model.addAttribute("type", BbsTopicCharge.MODEL_CHARGE);
			  		model.addAttribute("webCatBrowser", webCatBrowser);
			  		model.addAttribute("wxopenid", wxopenid);
			  		return FrontUtils.getTplPath(request, site.getSolutionPath(),
							TPLDIR_SPECIAL, COMMON_BUY);
			    }else{
			    	errors.addErrorCode("error.beanNotFound","magic");
			    	return FrontUtils.showError(request, response, model, errors);
			    }
			}
		}
	}
	
	@RequestMapping(value = "/topic/fixSelect.jspx")
	public String topicFixSelect(
			Integer topicId,String orderNumber,
			Double rewardAmount,Short chargeReward,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		CmsSite site=CmsUtils.getSite(request);
    	String ua = ((HttpServletRequest) request).getHeader("user-agent")
	  	          .toLowerCase();
  		boolean webCatBrowser=false;
  		String wxopenid=null;
        if (ua.indexOf("micromessenger") > 0) {
        	// 是微信浏览器
        	webCatBrowser=true;
        	wxopenid=(String) session.getAttribute(request, "wxopenid");
        }
        if(topicId==null){
			errors.addErrorCode("error.required","topicId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			BbsTopic topic=topicMng.findById(topicId);
		    if(topic!=null){
		    	FrontUtils.frontData(request, model, site);
				model.addAttribute("topicId", topicId);
		  		model.addAttribute("orderNumber", orderNumber);
		  		model.addAttribute("chargeReward", chargeReward);
		  		model.addAttribute("topic", topic);
		  		model.addAttribute("type", BbsTopicCharge.MODEL_REWARD);
		  		model.addAttribute("webCatBrowser", webCatBrowser);
		  		model.addAttribute("wxopenid", wxopenid);
		  		model.addAttribute("rewardAmount", rewardAmount);
		  		return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_SPECIAL, REWARD);
		    }else{
		    	errors.addErrorCode("error.beanNotFound","topic");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	
	//主题购买或打赏记录
	@RequestMapping(value = "/topic/orders.jspx")
	public String topicOrders(Integer topicId,Short type,Integer pageNo,
			HttpServletRequest request,HttpServletResponse response
			,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		CmsSite site=CmsUtils.getSite(request);
		if(type==null){
			type=BbsTopicCharge.MODEL_REWARD;
		}
		if(topicId==null){
			errors.addErrorCode("error.required","topicId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			BbsTopic topic=topicMng.findById(topicId);
		    if(topic!=null){
	  	    	FrontUtils.frontData(request, model, site);
	  	    	Pagination pagination=orderMng.getPageByTopic(topicId,
	  	    			type, cpn(pageNo), CookieUtils.getPageSize(request));
		  		model.addAttribute("topicId", topicId);
		  		model.addAttribute("type", type);
		  		model.addAttribute("pagination", pagination);
		  		return FrontUtils.getTplPath(request, site.getSolutionPath(),
						TPLDIR_SPECIAL, TOPIC_ORDERS);
		    }else{
		    	errors.addErrorCode("error.beanNotFound","topic");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
		
	@RequestMapping(value = "/reward/random.jspx")
	public void randomReward(HttpServletRequest request,
			HttpServletResponse response) {
		 BbsConfigCharge config=configChargeMng.getDefault(); 
		 Double max=config.getRewardMax();
		 Double min=config.getRewardMin();
	     Double s =StrUtils.retainTwoDecimal(min + ((max - min) * new Random().nextDouble()));
	     ResponseUtils.renderJson(response, s.toString());
	}
	
	/**
	 * 选择支付方式
	 * @param topicId 主题ID
	 * @param orderNumber 订单号
	 * @param payMethod 支付方式 1微信扫码 2支付宝即时支付  3微信浏览器打开[微信移动端] 4支付宝扫码5支付宝手机网页
	 * @param rewardAmount 打赏金额
	 */
	@RequestMapping(value = "/order/topicSelectPay.jspx")
	public String topicSelectPay(Integer topicId,String orderNumber,
			Integer payMethod,Double rewardAmount,Short chargeReward,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		BbsUser user=CmsUtils.getUser(request);
		CmsSite site=CmsUtils.getSite(request);
		initWeiXinPayUrl();
		initAliPayUrl();
		if(topicId==null){
			errors.addErrorCode("error.required","topicId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			BbsTopic topic=topicMng.findById(topicId);
		    if(topic!=null){
		    	//收费模式金额必须大于0
		    	if(topic.getChargeModel().equals(BbsTopicCharge.MODEL_CHARGE)
		    			&&topic.getChargeAmount()<=0){
		  	    	errors.addErrorCode("error.chargeAmountError");
		  	    	return FrontUtils.showError(request, response, model, errors);
		  	    }else{
		  	    	BbsConfigCharge config=configChargeMng.getDefault();
  	    			Double totalAmount=topic.getChargeAmount();
  	    			if(rewardAmount!=null){
  	    				totalAmount=rewardAmount;
  	    			}
		  			//收取模式（收费 和打赏）
		  	    	if(chargeReward==null){
		  	    		chargeReward=BbsTopicCharge.MODEL_CHARGE;
		  	    		if(rewardAmount!=null){
		  	    			chargeReward=BbsTopicCharge.MODEL_REWARD;
		  	    		}
		  	    	}
		  	    	if(user!=null){
		  	    		cache.put(new Element(orderNumber,
			  	    			BbsOrder.ORDER_TYPE_CACHE_FLAG_TOPIC+","+
			  	    			topicId+","+user.getId()+","+rewardAmount+","+chargeReward));
		  	    	}else{
		  	    		cache.put(new Element(orderNumber,
		  	    				BbsOrder.ORDER_TYPE_CACHE_FLAG_TOPIC+","+
		  	    				topicId+",,"+rewardAmount+","+chargeReward));
		  	    	}
		  	    	if(payMethod!=null){
		  	    		if(payMethod==1){
		  	    			return WeixinPay.enterWeiXinPay(getWeiXinPayUrl(),config,
									orderNumber,totalAmount,topic.getTitle(),
		  	    					topic.getUrlWhole(),BbsOrder.PAY_TARGET_TOPIC,
		  	    					request, response, model);
		  	    		}else if(payMethod==3){
		  	    			String openId=(String) session.getAttribute(request, "wxopenid");
		  	    			return WeixinPay.weixinPayByMobile(getWeiXinPayUrl(), 
		  	    					config, openId,orderNumber, totalAmount, topic.getTitle(),
		  	    					topic.getUrlWhole(),BbsOrder.PAY_TARGET_TOPIC,
		  	    					request, response, model);
		  	    		}else if(payMethod==2){
		  	    			return AliPay.enterAliPayImmediate(config, orderNumber,
		  	    					totalAmount, topic.getTitle(), topic.getUrl(), 
		  	    					 topic.getUrlWhole(), BbsOrder.PAY_TARGET_TOPIC,
		  	    					 request, response, model);
		  	    		}else if(payMethod==4){
		  	    			return AliPay.enterAlipayScanCode(request, response, model,
		  	    					getAliPayUrl(), config, topic.getTitle(), topic.getUrlWhole(),
		  	    					orderNumber, totalAmount,BbsOrder.PAY_TARGET_TOPIC);
		  	    		}else if(payMethod==5){
		  	    			model.addAttribute("orderNumber",orderNumber);
		  					model.addAttribute("topic", topic);
		  					model.addAttribute("type", chargeReward);
		  					model.addAttribute("rewardAmount", rewardAmount);
		  					model.addAttribute("returnUrl", topic.getUrlWhole());
		  	    			FrontUtils.frontData(request, model, site);
		  					return FrontUtils.getTplPath(request, site.getSolutionPath(),
		  							TPLDIR_SPECIAL, ALIPAY_MOBILE);
		  	    		}
					}//支付宝
		  	    	return AliPay.enterAliPayImmediate(config, orderNumber, totalAmount, 
		  	    			topic.getTitle(), topic.getUrlWhole(), topic.getUrl(), 
		  	    			BbsOrder.PAY_TARGET_TOPIC,request, response, model);
		  	    }
		    }else{
		    	errors.addErrorCode("error.beanNotFound","topic");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	
	@RequestMapping(value = "/topic/alipayInMobile.jspx")
	public String enterAlipayInMobile(Integer topicId,String orderNumber,
			Double rewardAmount,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		initAliPayUrl();
		if(topicId==null){
			errors.addErrorCode("error.required","topicId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			BbsTopic topic=topicMng.findById(topicId);
			BbsConfigCharge config=configChargeMng.getDefault();
			if(topic!=null){
				Double totalAmount=topic.getChargeAmount();
    			if(rewardAmount!=null){
    				totalAmount=rewardAmount;
    			}
				AliPay.enterAlipayInMobile(request, response, getAliPayUrl(), config,
						topic.getTitle(), topic.getUrlWhole(), orderNumber, totalAmount);
				return "";
			}else{
		    	errors.addErrorCode("error.beanNotFound","topic");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	
	/**
	 * 道具购买选择支付方式
	 * @param mid 道具标识
	 * @param orderNumber 订单号
	 * @param payMethod 支付方式 1微信扫码 2支付宝即时支付  3微信浏览器打开[微信移动端] 4支付宝扫码5支付宝手机网页
	 * @param rewardAmount 打赏金额
	 */
	@RequestMapping(value = "/order/magicSelectPay.jspx")
	public String magicSelectPay(String mid,Integer buyNum,
			String orderNumber,Integer payMethod,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		BbsUser user=CmsUtils.getUser(request);
		CmsSite site=CmsUtils.getSite(request);
		String returnUrl=site.getMagicShopUrl();
		initWeiXinPayUrl();
		initAliPayUrl();
		if(mid==null){
			errors.addErrorCode("error.required","mid");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			BbsCommonMagic magic=magicMng.findByIdentifier(mid);
		    if(magic!=null){
		    	//收费模式金额必须大于0
		    	if(magic.getPrice().equals(BbsTopicCharge.MODEL_CHARGE)
		    			&&magic.getPrice()<=0){
		  	    	errors.addErrorCode("error.chargeAmountError");
		  	    	return FrontUtils.showError(request, response, model, errors);
		  	    }else{
		  	    	BbsConfigCharge config=configChargeMng.getDefault();
  	    			if(buyNum==null){
  	    				buyNum=1;
  	    			}
  	    			Double totalAmount=magic.getPrice()*buyNum.doubleValue();
  	    			Short chargeReward=BbsTopicCharge.MODEL_CHARGE;
		  	    	if(user!=null){
		  	    		cache.put(new Element(orderNumber,
		  	    				BbsOrder.ORDER_TYPE_CACHE_FLAG_MAGIC+","
		  	    				+mid+","+user.getId()+","+buyNum));
		  	    	}else{
		  	    		cache.put(new Element(orderNumber,
		  	    				BbsOrder.ORDER_TYPE_CACHE_FLAG_MAGIC+","+
		  	    				mid+","+","+buyNum));
		  	    	}
		  	    	if(payMethod!=null){
		  	    		if(payMethod==1){
		  	    			return WeixinPay.enterWeiXinPay(getWeiXinPayUrl(),config,
									orderNumber,totalAmount,magic.getName(),
									returnUrl,BbsOrder.PAY_TARGET_MAGIC,
									request, response, model);
		  	    		}else if(payMethod==3){
		  	    			String openId=(String) session.getAttribute(request, "wxopenid");
		  	    			return WeixinPay.weixinPayByMobile(getWeiXinPayUrl(), 
		  	    					config, openId,orderNumber, totalAmount, magic.getName(),
		  	    					returnUrl,BbsOrder.PAY_TARGET_MAGIC,
		  	    					request, response, model);
		  	    		}else if(payMethod==2){
		  	    			return AliPay.enterAliPayImmediate(config, orderNumber,
		  	    					totalAmount, magic.getName(), returnUrl, 
		  	    					returnUrl,BbsOrder.PAY_TARGET_MAGIC,
		  	    					request, response, model);
		  	    		}else if(payMethod==4){
		  	    			return AliPay.enterAlipayScanCode(request, response, model,
		  	    					getAliPayUrl(), config, magic.getName(), 
		  	    					returnUrl,orderNumber, totalAmount,BbsOrder.PAY_TARGET_MAGIC);
		  	    		}else if(payMethod==5){
		  	    			model.addAttribute("orderNumber",orderNumber);
		  					model.addAttribute("magic", magic);
		  					model.addAttribute("buyNum", buyNum);
		  					model.addAttribute("type", chargeReward);
		  					model.addAttribute("returnUrl", returnUrl);
		  	    			FrontUtils.frontData(request, model, site);
		  					return FrontUtils.getTplPath(request, site.getSolutionPath(),
		  							TPLDIR_SPECIAL, ALIPAY_MOBILE);
		  	    		}
					}//支付宝
		  	    	return AliPay.enterAliPayImmediate(config, orderNumber, totalAmount, 
		  	    			magic.getName(), returnUrl, returnUrl, 
		  	    			BbsOrder.PAY_TARGET_MAGIC,request, response, model);
		  	    }
		    }else{
		    	errors.addErrorCode("error.beanNotFound","magic");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	
	@RequestMapping(value = "/magic/alipayInMobile.jspx")
	public String enterMagicAlipayInMobile(String mid,Integer buyNum,
			String orderNumber,HttpServletRequest request,HttpServletResponse response
			,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		CmsSite site=CmsUtils.getSite(request);
		String returnUrl=site.getMagicShopUrl();
		initAliPayUrl();
		if(mid==null){
			errors.addErrorCode("error.required","mid");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			BbsCommonMagic magic=magicMng.findByIdentifier(mid);
			BbsConfigCharge config=configChargeMng.getDefault();
			if(magic!=null){
				if(buyNum==null){
	    			buyNum=1;
	    		}
	    		Double totalAmount=magic.getPrice()*buyNum.doubleValue();
				AliPay.enterAlipayInMobile(request, response, getAliPayUrl(), 
						config,magic.getName(), returnUrl, orderNumber, totalAmount);
				return "";
			}else{
		    	errors.addErrorCode("error.beanNotFound","magic");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	
	/**
	 * 礼物购买选择支付方式
	 * @param giftId 礼物ID
	 * @param buyNum 礼物数量
	 * @param orderNumber 订单号
	 * @param payMethod 支付方式 1微信扫码 2支付宝即时支付  3微信浏览器打开[微信移动端] 4支付宝扫码5支付宝手机网页
	 */
	@RequestMapping(value = "/order/giftSelectPay.jspx")
	public String giftSelectPay(Integer giftId,Integer buyNum,
			String orderNumber,Integer payMethod,
			String returnUrl,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		BbsUser user=CmsUtils.getUser(request);
		CmsSite site=CmsUtils.getSite(request);
		if(StringUtils.isBlank(returnUrl)){
			 returnUrl=site.getGiftIndexUrl();
		}
		initWeiXinPayUrl();
		initAliPayUrl();
		if(giftId==null){
			errors.addErrorCode("error.required","giftId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			BbsGift gift = giftMng.findById(giftId);
		    if(gift!=null){
		    	//收费模式金额必须大于0
		    	if(gift.getPrice().equals(BbsTopicCharge.MODEL_CHARGE)
		    			&&gift.getPrice()<=0){
		  	    	errors.addErrorCode("error.chargeAmountError");
		  	    	return FrontUtils.showError(request, response, model, errors);
		  	    }else{
		  	    	BbsConfigCharge config=configChargeMng.getDefault();
  	    			if(buyNum==null){
  	    				buyNum=1;
  	    			}
  	    			Double totalAmount=gift.getPrice()*buyNum.doubleValue();
  	    			Short chargeReward=BbsTopicCharge.MODEL_CHARGE;
		  	    	if(user!=null){
		  	    		cache.put(new Element(orderNumber,
		  	    				BbsOrder.ORDER_TYPE_CACHE_FLAG_GIFT+","
		  	    				+giftId+","+user.getId()+","+buyNum+","+returnUrl));
		  	    	}else{
		  	    		cache.put(new Element(orderNumber,
		  	    				BbsOrder.ORDER_TYPE_CACHE_FLAG_GIFT+","+
		  	    				giftId+","+","+buyNum+","+returnUrl));
		  	    	}
		  	    	if(payMethod!=null){
		  	    		if(payMethod==1){
		  	    			return WeixinPay.enterWeiXinPay(getWeiXinPayUrl(),config,
									orderNumber,totalAmount,gift.getName(),
									returnUrl,BbsOrder.PAY_TARGET_GIFT,
									request, response, model);
		  	    		}else if(payMethod==3){
		  	    			String openId=(String) session.getAttribute(request, "wxopenid");
		  	    			return WeixinPay.weixinPayByMobile(getWeiXinPayUrl(), 
		  	    					config, openId,orderNumber, totalAmount, gift.getName(),
		  	    					returnUrl,BbsOrder.PAY_TARGET_GIFT,
		  	    					request, response, model);
		  	    		}else if(payMethod==2){
		  	    			return AliPay.enterAliPayImmediate(config, orderNumber,
		  	    					totalAmount, gift.getName(), returnUrl, 
		  	    					returnUrl,BbsOrder.PAY_TARGET_GIFT,
		  	    					request, response, model);
		  	    		}else if(payMethod==4){
		  	    			return AliPay.enterAlipayScanCode(request, response, model,
		  	    					getAliPayUrl(), config, gift.getName(), 
		  	    					returnUrl,orderNumber, totalAmount,BbsOrder.PAY_TARGET_GIFT);
		  	    		}else if(payMethod==5){
		  	    			model.addAttribute("orderNumber",orderNumber);
		  					model.addAttribute("gift", gift);
		  					model.addAttribute("buyNum", buyNum);
		  					model.addAttribute("type", chargeReward);
		  					model.addAttribute("returnUrl", returnUrl);
		  	    			FrontUtils.frontData(request, model, site);
		  					return FrontUtils.getTplPath(request, site.getSolutionPath(),
		  							TPLDIR_SPECIAL, ALIPAY_MOBILE);
		  	    		}
					}//支付宝
		  	    	return AliPay.enterAliPayImmediate(config, orderNumber, totalAmount, 
		  	    			gift.getName(), returnUrl, returnUrl, 
		  	    			BbsOrder.PAY_TARGET_GIFT,request, response, model);
		  	    }
		    }else{
		    	errors.addErrorCode("error.beanNotFound","gift");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	
	@RequestMapping(value = "/gift/alipayInMobile.jspx")
	public String enterGiftAlipayInMobile(Integer giftId,Integer buyNum,
			String orderNumber,HttpServletRequest request,HttpServletResponse response
			,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		CmsSite site=CmsUtils.getSite(request);
		String returnUrl=site.getGiftIndexUrl();
		initAliPayUrl();
		if(giftId==null){
			errors.addErrorCode("error.required","giftId");
			return FrontUtils.showError(request, response, model, errors);
		}else{
			BbsGift gift = giftMng.findById(giftId);
			BbsConfigCharge config=configChargeMng.getDefault();
			if(gift!=null){
				if(buyNum==null){
	    			buyNum=1;
	    		}
	    		Double totalAmount=gift.getPrice()*buyNum.doubleValue();
				AliPay.enterAlipayInMobile(request, response, getAliPayUrl(), 
						config,gift.getName(), returnUrl, orderNumber, totalAmount);
				return "";
			}else{
		    	errors.addErrorCode("error.beanNotFound","gift");
		    	return FrontUtils.showError(request, response, model, errors);
		    }
		}
	}
	

	/**
	 * 广告账户充值
	 */
	@RequestMapping(value = "/ad/buy.jspx")
	public String adRecharge(Double amount,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		WebErrors errors=WebErrors.create(request);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		if(amount==null||amount<=0){
			errors.addErrorCode("error.exceptionParams");
			return FrontUtils.showError(request, response, model, errors);
		}
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
		model.addAttribute("orderNumber", orderNumber);
		model.addAttribute("type", BbsTopicCharge.MODEL_CHARGE);
		model.addAttribute("webCatBrowser", webCatBrowser);
		model.addAttribute("wxopenid", wxopenid);
		model.addAttribute("ad", true);
		model.addAttribute("amount", amount);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_SPECIAL, COMMON_BUY);
	}
	
	/**
	 * 广告充值选择支付方式
	 * @param amount 充值金额
	 * @param payMethod 支付方式 1微信扫码 2支付宝即时支付  3微信浏览器打开[微信移动端] 4支付宝扫码5支付宝手机网页
	 */
	@RequestMapping(value = "/order/adSelectPay.jspx")
	public String adSelectPay(Double amount,
			Integer payMethod,String orderNumber,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) throws JSONException {
		WebErrors errors=WebErrors.create(request);
		BbsUser user=CmsUtils.getUser(request);
		CmsSite site=CmsUtils.getSite(request);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		model.addAttribute("orderNumber", orderNumber);
		String returnUrl=site.getMemberAdIndexUrl();
		initWeiXinPayUrl();
		initAliPayUrl();
	    if(amount!=null&&amount>0){
	    	String orderTitle=WebErrors.create(request).getMessage("order.ad.title", null);
	    	BbsConfigCharge config=configChargeMng.getDefault();
			Short chargeReward=BbsTopicCharge.MODEL_CHARGE;
    		cache.put(new Element(orderNumber,
    				BbsOrder.ORDER_TYPE_CACHE_FLAG_AD+","+amount+","+user.getId()));
  	    	if(payMethod!=null){
  	    		if(payMethod==1){
  	    			return WeixinPay.enterWeiXinPay(getWeiXinPayUrl(),config,
							orderNumber,amount,orderTitle,
							returnUrl,BbsOrder.PAY_TARGET_AD,
							request, response, model);
  	    		}else if(payMethod==3){
  	    			String openId=(String) session.getAttribute(request, "wxopenid");
  	    			return WeixinPay.weixinPayByMobile(getWeiXinPayUrl(), 
  	    					config, openId,orderNumber, amount, orderTitle,
  	    					returnUrl,BbsOrder.PAY_TARGET_AD,
  	    					request, response, model);
  	    		}else if(payMethod==2){
  	    			return AliPay.enterAliPayImmediate(config, orderNumber,
  	    					 amount, orderTitle, returnUrl, 
  	    					returnUrl,BbsOrder.PAY_TARGET_AD,
  	    					request, response, model);
  	    		}else if(payMethod==4){
  	    			return AliPay.enterAlipayScanCode(request, response, model,
  	    					getAliPayUrl(), config, orderTitle, 
  	    					returnUrl,orderNumber, amount,BbsOrder.PAY_TARGET_AD);
  	    		}else if(payMethod==5){
  					model.addAttribute("ad", true);
  					model.addAttribute("type", chargeReward);
  					model.addAttribute("returnUrl", returnUrl);
  					model.addAttribute("amount", amount);
  					model.addAttribute("orderTitle", orderTitle);
  	    			FrontUtils.frontData(request, model, site);
  					return FrontUtils.getTplPath(request, site.getSolutionPath(),
  							TPLDIR_SPECIAL, ALIPAY_MOBILE);
  	    		}
			}//支付宝
  	    	return AliPay.enterAliPayImmediate(config, orderNumber, amount, orderTitle,
  	    			returnUrl, returnUrl, BbsOrder.PAY_TARGET_AD,request, response, model);
	    }else{
	    	errors.addErrorCode("error.required","amount");
	    	return FrontUtils.showError(request, response, model, errors);
	    }
	}
	
	@RequestMapping(value = "/ad/alipayInMobile.jspx")
	public String enterAdAlipayInMobile(
			 Double amount,String orderNumber,
			 HttpServletRequest request,HttpServletResponse response
			,ModelMap model) throws JSONException {
		CmsSite site=CmsUtils.getSite(request);
		BbsUser user=CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}
		String returnUrl=site.getMemberAdIndexUrl();
		initAliPayUrl();
		String orderTitle=WebErrors.create(request).getMessage("order.ad.title", null);
		BbsConfigCharge config=configChargeMng.getDefault();
		AliPay.enterAlipayInMobile(request, response, getAliPayUrl(), 
				config,orderTitle,returnUrl, orderNumber, amount);
		return "";
	}
	
	/**
	 * 微信回调
	 * @param code
	 */
	@RequestMapping(value = "/order/payCallByWeiXin.jspx")
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
	
	//支付宝即时支付回调地址
	@RequestMapping(value = "/order/payCallByAliPay.jspx")
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
	
	//支付宝查询订单状态（扫码支付和手机网页支付均由此处理订单）
	@RequestMapping(value = "/order/orderQuery.jspx")
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
	
	private String payAfter(CmsSite site,String orderNumber,Double ratio,
			String weixinOrderNum,String alipyOrderNum){
		Element e = cache.get(orderNumber);
	    BbsTopic topic = null;
	    Double rewardAmount=null;
		Integer topicId=null;
		String mid="";
		Integer giftId=null;
		Double adAmount=null;
		Integer buyUserId=null;
		String giftReturnUrl="";
		if(e!=null&&StringUtils.isNotBlank(orderNumber)){
	    BbsOrder b=orderMng.findByOrderNumber(orderNumber);
	    //不能重复提交
	    if(b==null){
	    	Object obj= e.getObjectValue();
			String[] objArray = null;
			if(obj!=null){
				String cacheValue=obj.toString();
				if(StringUtils.isNotBlank(cacheValue)&&
		    			!cacheValue.startsWith(BbsOrder.PAY_TARGET_LIVE)){
					objArray=obj.toString().split(",");
					Short chargeReward = BbsTopicCharge.MODEL_REWARD;
					Integer buynum=1;
					String orderType="";
					if(objArray!=null){
						//道具订单4个参数，主题订单5个参数
						if(objArray[2]!=null&&StringUtils.isNotBlank(objArray[2])){
							buyUserId=Integer.parseInt(objArray[2]);
						}
						if(objArray[0]!=null&&StringUtils.isNotBlank(objArray[0])){
							orderType=objArray[0];
						}
						if(orderType.equals(BbsOrder.ORDER_TYPE_CACHE_FLAG_TOPIC)){
							if(objArray[1]!=null){
								topicId=Integer.parseInt(objArray[1]) ;
							}
							if(objArray[3]!=null&&StringUtils.isNotBlank(objArray[3])
									&&!objArray[3].toLowerCase().equals("null")){
								rewardAmount=Double.parseDouble(objArray[3]);
							}
							if(objArray[4]!=null){
								chargeReward=Short.valueOf(objArray[4]);
							}
						}else if(orderType.equals(BbsOrder.ORDER_TYPE_CACHE_FLAG_MAGIC)){
							if(objArray[1]!=null){
								mid=objArray[1] ;
							}
							if(objArray[3]!=null){
								buynum=Integer.parseInt(objArray[3]);
							}
						}else if(orderType.equals(BbsOrder.ORDER_TYPE_CACHE_FLAG_GIFT)){
							if(objArray[1]!=null){
								giftId=Integer.parseInt(objArray[1]);
							}
							if(objArray[3]!=null){
								buynum=Integer.parseInt(objArray[3]);
							}
							if(objArray[4]!=null){
								giftReturnUrl=objArray[4];
							}
						}else if(orderType.equals(BbsOrder.ORDER_TYPE_CACHE_FLAG_AD)){
							if(objArray[1]!=null&&StringUtils.isNotBlank(objArray[1])){
								if(com.jeecms.bbs.web.StrUtils.checkFloat(objArray[1],"0+")){
									adAmount=Double.parseDouble(objArray[1]);
								}
							}
							if(objArray[2]!=null&&StringUtils.isNotBlank(objArray[2])){
								buyUserId=Integer.parseInt(objArray[2]);
							}
						}
					}
				    BbsOrder order=new BbsOrder();
				    if(topicId!=null){
					    //主题订单
				    	topic=topicMng.findById(topicId);
				   	    order.setAuthorUser(topic.getCreater());
				   	    //打赏可以匿名
				   	    if(buyUserId!=null){
				   	    	order.setBuyUser(userMng.findById(buyUserId));
				   	    }
				   	    order.setDataId(topic.getId());
				   	    order.setDataType(BbsOrder.ORDER_TYPE_TOPIC);
				   	    order.setOrderNumber(orderNumber);
				   	    order.setBuyTime(Calendar.getInstance().getTime());
				   	    Double chargeAmount=topic.getChargeAmount();
				   	    Double platAmount=topic.getChargeAmount()*ratio;
				     	Double authorAmount=topic.getChargeAmount()*(1-ratio);
				   	    if(rewardAmount!=null){
				   	    	chargeAmount=rewardAmount;
				   	    	platAmount=rewardAmount*ratio;
				   	    	authorAmount=rewardAmount*(1-ratio);
				   	    }
				   	    if(chargeReward.equals(BbsTopicCharge.MODEL_REWARD)){
				   	    	order.setChargeReward(BbsTopicCharge.MODEL_REWARD);
				   	    }else{
				   	    	order.setChargeReward(BbsTopicCharge.MODEL_CHARGE);
				   	    }
				   	    order.setChargeAmount(chargeAmount);
				   	    order.setPlatAmount(platAmount);
				   	    order.setAuthorAmount(authorAmount);
				 		// 这里是把微信商户的订单号放入了交易号中
			 			order.setOrderNumWeixin(weixinOrderNum);
			 			order.setOrderNumAlipay(alipyOrderNum);
			 			order.setLiveUsedNum(0);
			 			order.setLiveUserNum(0);
			 			order=orderMng.save(order);
			 			BbsUser authorUser=order.getAuthorUser();
			 			//笔者所得统计
			 			userAccountMng.userPay(order.getAuthorAmount(), authorUser);
			 			//平台所得统计
			 			configChargeMng.afterUserPay(order.getPlatAmount(),BbsIncomeType.post);
			 			//主题所得统计
			 			topicChargeMng.afterUserPay(order.getChargeAmount(), topic);
			 			//每日收益统计
			 			incomeStatisticMng.afterPay(order.getPlatAmount(), BbsIncomeType.post);
			 		}else if(StringUtils.isNotBlank(mid)){
				 		//道具订单
				 		BbsCommonMagic magic=magicMng.findByIdentifier(mid);
				 		if(magic!=null&&buyUserId!=null){
				 			order.setAuthorUser(null);
				 			BbsUser user=userMng.findById(buyUserId);
					   	    if(buyUserId!=null){
					   	    	order.setBuyUser(user);
					   	    }
					   	    order.setDataId(magic.getId());
					   	    order.setDataType(BbsOrder.ORDER_TYPE_MAGIC);
					   	    order.setOrderNumber(orderNumber);
					   	    order.setBuyTime(Calendar.getInstance().getTime());
					   	    Double chargeAmount=magic.getPrice().doubleValue()*buynum;
					   	    Double platAmount=chargeAmount;
					     	Double authorAmount=0d;
					     	order.setChargeReward(BbsTopicCharge.MODEL_CHARGE);
					   	    order.setChargeAmount(chargeAmount);
					   	    order.setPlatAmount(platAmount);
					   	    order.setAuthorAmount(authorAmount);
					 		// 这里是把微信商户的订单号放入了交易号中
				 			order.setOrderNumWeixin(weixinOrderNum);
				 			order.setOrderNumAlipay(alipyOrderNum);
				 			order.setLiveUsedNum(0);
				 			order.setLiveUserNum(0);
				 			order=orderMng.save(order);
				 			userMng.updatePoint(user.getId(), null, null,
									mid, buynum, 3);
				 			magicLogMng.buyMagicLog(magic, user, buynum,
									MagicConstants.MAGIC_OPERATOR_BUY);
				 			//平台所得统计
				 			configChargeMng.afterUserPay(order.getPlatAmount(),BbsIncomeType.magic);
				 			//每日收益统计
				 			incomeStatisticMng.afterPay(chargeAmount, BbsIncomeType.magic);
				 		}
				 	}else if(giftId!=null){
				 		//礼物订单
				 		BbsGift gift=giftMng.findById(giftId);
				 		BbsUser user=userMng.findById(buyUserId);
				 		if(gift!=null&&user!=null){
				 			BbsUser buyUser = null;
				 			if(buyUserId!=null){
				 	   	    	buyUser=userMng.findById(buyUserId);
				 	   	    }
			 	    		//外部订单号和内部订单号要一一对应，否则会出现一个外部订单可以用于形成多个内部订单
			 	    		Double orderAmount = gift.getPrice()*buynum;
			 	    		// 这里是把微信商户的订单号放入了交易号中
				 			order.setOrderNumWeixin(weixinOrderNum);
				 			order.setOrderNumAlipay(alipyOrderNum);
	 				   	    order.setAuthorUser(null);
	 			   	    	order.setBuyUser(buyUser);
	 				   	    order.setDataId(gift.getId());
	 				   	    order.setDataType(BbsOrder.ORDER_TYPE_GIFT);
	 				   	    order.setOrderNumber(orderNumber);
	 				   	    order.setBuyTime(Calendar.getInstance().getTime());
	 				   	    Double chargeAmount=orderAmount;
	 				   	    //礼物的抽成比例
	 				   	    ratio=configChargeMng.getDefault().getGiftChargeRatio();
	 				   	    Double platAmount=chargeAmount*ratio;
	 				     	Double authorAmount=0d;
	 				     	order.setChargeReward(BbsTopicCharge.MODEL_CHARGE);
	 				   	    order.setChargeAmount(chargeAmount);
	 				   	    order.setPlatAmount(platAmount);
	 				   	    order.setAuthorAmount(authorAmount);
	 				   	    order.setLiveUsedNum(0);
	 			 			order.setLiveUserNum(0);
	 			 			order=orderMng.save(order);
	 			 			//礼物收益统计
	 			 			giftMng.afterUserPay(chargeAmount, gift);
	 			 			//用户礼物增加
	 			 			bbsGiftUserMng.addUserGift(gift.getId(), buyUser.getId(), buynum);
	 			 			//每日收益统计
				 			incomeStatisticMng.afterPay(platAmount, BbsIncomeType.gift);
				 			//平台所得统计
				 			configChargeMng.afterUserPay(order.getPlatAmount(),BbsIncomeType.gift);
	 			 		  }
				 	}else if(adAmount!=null){
				 		//广告充值订单
				 		BbsUser user=userMng.findById(buyUserId);
				 		if(user!=null){
				 			BbsUser buyUser = null;
				 			if(buyUserId!=null){
				 	   	    	buyUser=userMng.findById(buyUserId);
				 	   	    }
			 	    		//外部订单号和内部订单号要一一对应，否则会出现一个外部订单可以用于形成多个内部订单
			 	    		// 这里是把微信商户的订单号放入了交易号中
				 			order.setOrderNumWeixin(weixinOrderNum);
				 			order.setOrderNumAlipay(alipyOrderNum);
	 				   	    order.setAuthorUser(null);
	 			   	    	order.setBuyUser(buyUser);
	 				   	    order.setDataId(0);
	 				   	    order.setDataType(BbsOrder.ORDER_TYPE_AD);
	 				   	    order.setOrderNumber(orderNumber);
	 				   	    order.setBuyTime(Calendar.getInstance().getTime());
	 				     	Double authorAmount=0d;
	 				     	order.setChargeReward(BbsTopicCharge.MODEL_CHARGE);
	 				   	    order.setChargeAmount(adAmount);
	 				   	    order.setPlatAmount(adAmount);
	 				   	    order.setAuthorAmount(authorAmount);
	 				   	    order.setLiveUsedNum(0);
	 			 			order.setLiveUserNum(0);
	 			 			order=orderMng.save(order);
	 			 			//广告余额增加和累计统计和开通所有广告主的广告
				 			userAccountMng.adRecharge(adAmount, buyUser);
	 			 			//每日收益统计
				 			incomeStatisticMng.afterPay(adAmount, BbsIncomeType.ad);
				 			//平台所得统计
				 			configChargeMng.afterUserPay(adAmount,BbsIncomeType.ad);
	 			 		  }
					 	}
				 	}
				}
			}
		}
		if(topic!=null){
			//主题订单
			return topic.getUrlWhole();
		}else if(mid!=null){
			//道具订单
			return site.getMagicShopUrl();
		}else if(giftId!=null){
			//礼物订单
			if(StringUtils.isNotBlank(giftReturnUrl)){
				return "redirect:"+giftReturnUrl;
			}else{
				return site.getGiftIndexUrl();
			}
		}else {
			return site.getMemberAdIndexUrl();
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
	private BbsTopicMng topicMng;
	@Autowired
	private BbsTopicChargeMng topicChargeMng;
	@Autowired
	private BbsOrderMng orderMng;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private BbsUserAccountMng userAccountMng;
	@Autowired
	private BbsUserMng userMng;
	@Autowired
	private SessionProvider session;
	@Autowired
	private BbsCommonMagicMng magicMng;
	@Autowired
	private BbsMagicLogMng magicLogMng;
	@Autowired
	private BbsGiftMng giftMng;
	@Autowired
	private BbsGiftUserMng bbsGiftUserMng;
	@Autowired
	private BbsIncomeStatisticMng incomeStatisticMng;
	@Autowired
	@Qualifier("OrderTemp")
	private Ehcache cache;
	
}

