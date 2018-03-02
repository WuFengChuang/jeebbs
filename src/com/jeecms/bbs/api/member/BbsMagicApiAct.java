package com.jeecms.bbs.api.member;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.MagicConstants;
import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.ApiRecord;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsMagicConfig;
import com.jeecms.bbs.entity.BbsMemberMagic;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.ApiRecordMng;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsMagicConfigMng;
import com.jeecms.bbs.manager.BbsMagicLogMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsMagicApiAct {
	
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsCommonMagicMng magicMng;
	@Autowired
	private BbsMagicConfigMng magicConfigMng;
	@Autowired
	private BbsMagicLogMng magicLogMng;
	@Autowired
	private BbsPostMng postMng;
	@Autowired
	private BbsTopicMng topicMng;
	@Autowired
	private BbsOrderMng bbsOrderMng;
	@Autowired
	private ApiRecordMng apiRecordMng;
	
	/**
	 * 我的道具列表
	 * @param appId      appid 必选
	 * @param sessionKey 用户会话  必选
	 */
	@RequestMapping(value = "/magic/myMagic")
	public void myMagicList(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		BbsUser user = CmsUtils.getUser(request);
		BbsMagicConfig magicConfig = magicConfigMng.findById(CmsUtils.getSiteId(request));
		List<BbsMemberMagic> list = new ArrayList<BbsMemberMagic>();
		JSONArray jsonArray = new JSONArray();
		if (magicConfig.getMagicSwitch()) {
			Set<BbsMemberMagic> memberMagics = user.getMemberMagics();
			if (memberMagics!=null&&memberMagics.size()>0) {
				Iterator<BbsMemberMagic> it = memberMagics.iterator();
				BbsMemberMagic temp;
				while(it.hasNext()){
					temp = it.next();
					if (temp.getNum()>0) {
						list.add(temp);
					}
				}
			}
			if (list!=null&&list.size()>0) {
				for(int i = 0; i<list.size() ; i++){
					jsonArray.put(i,list.get(i).convertToJson());
				}
			}
			body=jsonArray.toString();
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_MAGIC_CLOSE;
			code=ResponseCode.API_CODE_MAGIC_CLOSE;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	/**
	 * 购买道具
	 * @param mid   道具标识  必选
	 * @param num   购买数量 非必选 默认1
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 */
	@RequestMapping(value = "/magic/buy")
	public void buyMagic(String mid,Integer num,HttpServletRequest request,HttpServletResponse response){
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if(num==null){
			num=1;
		}
		BbsCommonMagic magic = magicMng.findByIdentifier(mid);
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, mid,magic);
		if (!errors.hasErrors()) {
			BbsMagicConfig magicConfig = magicConfigMng.findById(CmsUtils.getSiteId(request));
			Integer paymoney = (int) (magic.getPrice()*num);
			if (magicConfig.getMagicSwitch()) {
					// 获取道具的购买方式-积分或者威望或者现金
					if (magic.getCredit() == 1) {
						if ((user.getPoint() - paymoney) < 0) {
							// 积分不足
							errors.addErrorString("\"Insufficient integration\"");
							code=ResponseCode.API_CODE_POINT_NOT_ENOUGH;
						}
					} else if (magic.getCredit() == 2) {
						if ((user.getPrestige() - paymoney) < 0) {
							// 威望不足
							errors.addErrorString("\"Lack of prestige\"");
							code=ResponseCode.API_CODE_PRESTIGE_NOT_ENOUGH;
						}
					}
			}else{
				errors.addErrorString(Constants.API_MESSAGE_MAGIC_CLOSE);
				code=ResponseCode.API_CODE_MAGIC_CLOSE;
			}
			if (!errors.hasErrors()) {
				// 获取道具的购买方式-积分或者威望或者现金
				if (magic.getCredit() == 1) {
					// 需要增加用户道具数量，减少系统道具数量，减少道具包容量，减少用户积分
					bbsUserMng.updatePoint(user.getId(), -paymoney, null,
							mid, num, 3);
				} else if (magic.getCredit() == 2) {
					bbsUserMng.updatePoint(user.getId(), null, -paymoney,
							mid, num, 3);
				}
				magicLogMng.buyMagicLog(magic, user, num,
						MagicConstants.MAGIC_OPERATOR_BUY);
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 购买道具
	 * @param mid   道具标识  必选
	 * @param num   购买数量 非必选 默认1
	 * @param outOrderNum 外部订单号 必选
	 * @param orderType  1微信支付   2支付宝支付 必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping(value = "/magic/buyUseMoney")
	public void buyMagicWithMoney(String mid,Integer num,String outOrderNum,Integer orderType,
			String appId,String sign,
			HttpServletRequest request,HttpServletResponse response){
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsUser user = CmsUtils.getUser(request);
		Integer siteId = CmsUtils.getSiteId(request);
		BbsCommonMagic magic = magicMng.findByIdentifier(mid);
		if (num==null) {
			num=1;
		}
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, mid,outOrderNum,orderType,
				appId,sign,magic);
		if (!errors.hasErrors()) {
			BbsMagicConfig magicConfig = magicConfigMng.findById(siteId);
			if (magicConfig.getMagicSwitch()) {
					// 获取道具的购买方式-积分或者威望
					if (magic.getCredit() == 1||magic.getCredit() == 2) {
						errors.addErrorString("\"payMethod error\"");
						code = ResponseCode.API_CODE_PARAM_ERROR;
					}
			}else{
				errors.addErrorString(Constants.API_MESSAGE_MAGIC_CLOSE);
				code=ResponseCode.API_CODE_MAGIC_CLOSE;
			}
			if (!errors.hasErrors()) {
				//签名数据不可重复利用
				ApiRecord record=apiRecordMng.findBySign(sign, appId);
				if(record!=null){
					message=Constants.API_MESSAGE_REQUEST_REPEAT;
					code=ResponseCode.API_CODE_REQUEST_REPEAT;
				}else{
					//外部订单号不可以用多次
					BbsOrder order=bbsOrderMng.findByOutOrderNum(outOrderNum, orderType);
					if(order==null){
						
						order=bbsOrderMng.magicOrder(mid, num, orderType, 
								outOrderNum, user.getId());
						if(order.getPrePayStatus()==BbsOrder.PRE_PAY_STATUS_SUCCESS){
				 			apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
									appId, "/magic/buyUseMoney",sign);
				 			status = Constants.API_STATUS_SUCCESS;
							message = Constants.API_MESSAGE_SUCCESS;
							code = ResponseCode.API_CODE_CALL_SUCCESS;
						}else if(order.getPrePayStatus()==BbsOrder.PRE_PAY_STATUS_ORDER_NUM_ERROR){
							message=Constants.API_MESSAGE_ORDER_NUMBER_ERROR;
							code=ResponseCode.API_CODE_ORDER_NUMBER_ERROR;
						}else if(order.getPrePayStatus()==BbsOrder.PRE_PAY_STATUS_ORDER_AMOUNT_NOT_ENOUGH){
							message=Constants.API_MESSAGE_ORDER_AMOUNT_NOT_ENOUGH;
							code=ResponseCode.API_CODE_ORDER_AMOUNT_NOT_ENOUGH;
						}
					}else{
						message=Constants.API_MESSAGE_ORDER_NUMBER_USED;
						code=ResponseCode.API_CODE_ORDER_NUMBER_USED;
					}
				}
			}else{
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 出售道具
	 * @param mid   道具标识  必选
	 * @param num   购买数量 默认1
	 * @param  operator 0出售  2丢弃 默认0
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 */
	@RequestMapping(value = "/magic/sell")
	public void sellMagic(String mid,Integer num,Integer operator,
			HttpServletRequest request,HttpServletResponse response){
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsUser user = CmsUtils.getUser(request);
		BbsCommonMagic magic = magicMng.findByIdentifier(mid);
		if (num == null) {
			num = 1;
		}
		if (operator==null) {
			operator=0;
		}
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, mid,magic);
		if (!errors.hasErrors()) {
			BbsMagicConfig magicConfig = magicConfigMng.findById(CmsUtils.getSiteId(request));
			int magicDiscount = 0;
			magicDiscount = magicConfig.getMagicDiscount();
			BbsMemberMagic user_magic = user.getMemberMagic(mid);
			int sellMoney = (int) (magic.getPrice() * magicDiscount / 100);
			int totalMoney = sellMoney * num;
			if (magicConfig.getMagicSwitch()) {
				// 获取道具的购买方式-积分或者威望
				if (user_magic !=null) {
					// 用户拥有的数量大于输入的数量才可用出售道具
					if (user_magic.getNum()<num) {
						// 道具数量不够
						errors.addErrorString("\"The number of props is not enough\"");
						code=ResponseCode.API_CODE_MAGIC_NUM_NOT_ENOUGH;
					}
				}else{
					// 没有该道具
					errors.addErrorString("\"has not this magic\"");
					code=ResponseCode.API_CODE_NOT_FOUND;
				}
			}else{
				errors.addErrorString(Constants.API_MESSAGE_MAGIC_CLOSE);
				code=ResponseCode.API_CODE_MAGIC_CLOSE;
			}
			if (!errors.hasErrors()) {
				if (operator==0) {
					//出售
					// 获取道具的购买方式-积分或者威望
					if (magic.getCredit() == 1) {
						// 用户拥有的数量大于输入的数量才可用出售道具
						bbsUserMng.updatePoint(user.getId(), totalMoney, null,
								mid, num, 0);
					} else if (magic.getCredit() == 2) {
						bbsUserMng.updatePoint(user.getId(), null, totalMoney,
								mid, num, 0);
					} 
					magicLogMng.buyMagicLog(magic, user, num,
							MagicConstants.MAGIC_OPERATOR_SELL);
				}else if (operator == 2) {
					// 丢弃
					bbsUserMng.updatePoint(user.getId(), null, null, mid, num,
							2);
					magicLogMng.buyMagicLog(magic, user, num,
							MagicConstants.MAGIC_OPERATOR_DROP);
				}
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 使用道具
	 * @param pid  		主题id  
	 * @param mid		道具标识   	必选
	 * @param color		变色卡给主题标题的颜色值
	 * @param username	雷达卡和窥视卡查看的用户名
	 * @param ppid		悔悟卡删除的帖子ID
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 */
	@RequestMapping(value = "/magic/use")
	public void userMagic(Integer pid,String mid,String color,String username,Integer ppid,
			HttpServletRequest request,HttpServletResponse response){
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsUser user = CmsUtils.getUser(request);
		BbsCommonMagic magic = magicMng.findByIdentifier(mid);
		BbsMemberMagic user_magic = null;
		String useMsg = "";
		BbsUser findUser;
		Integer userId=null;
		Integer siteId = CmsUtils.getSiteId(request);
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, mid,magic);
		if (!errors.hasErrors()) {
			BbsMagicConfig magicConfig = magicConfigMng.findById(siteId);
			user_magic = user.getMemberMagic(mid);
			int permission = hasPermission(magic, user, ppid);
			if (magicConfig.getMagicSwitch()) {
				if (permission == 1) {
					// 允许使用
					// 用户没有该道具，防止直接输入地址刷道具
					if (user_magic == null
							|| user_magic.getNum() <= 0) {
						errors.addErrorString("\"has not magic\"");
						code=ResponseCode.API_CODE_MAGIC_NUM_NOT_ENOUGH;
					}
					if (mid.equals(MagicConstants.MAGIC_SHOWIP)
							|| mid.equals(MagicConstants.MAGIC_CHECKONLINE)) {
						if (StringUtils.isNotBlank(username)) {
							if (bbsUserMng.usernameNotExist(username)) {
								errors.addErrorString(Constants.API_MESSAGE_USER_NOT_FOUND);
								code=ResponseCode.API_CODE_USER_NOT_FOUND;
							} else{
								findUser=bbsUserMng.findByUsername(username);
								userId=findUser.getId();
							}
						}else{
							errors.addErrorString(Constants.API_MESSAGE_USER_NOT_FOUND);
							code=ResponseCode.API_CODE_USER_NOT_FOUND;
						}
					}
				} else if (permission == 0) {
					// 不允许使用
					errors.addErrorString("\"has not permission\"");
					code=ResponseCode.API_CODE_MAGIC_FORBIDDEN;
				} else if (permission == -1) {
					// 不允许使用
					errors.addErrorString("\"has not beused permission\"");
					code=ResponseCode.API_CODE_MAGIC_FORBIDDEN;
				}
			}else{
				errors.addErrorString(Constants.API_MESSAGE_MAGIC_CLOSE);
				code=ResponseCode.API_CODE_MAGIC_CLOSE;
			}
			if (!errors.hasErrors()) {
				useMsg=topicMng.useMagic(request,siteId, pid, ppid,mid, 
						user.getId(), RequestUtils.getIpAddr(request),
						color, userId);
				magicLogMng.buyMagicLog(magic,user,1,
						MagicConstants.MAGIC_OPERATOR_USE);
			}
			if (hasNoPermission(useMsg)) {
				errors.addErrorString(useMsg);
			}
			if (errors.hasErrors()) {
				message = errors.getErrors().get(0);
			}else{
				if (useMsg.contains(MagicConstants.MAGIC_NAMEPOST_SUCCESS)) {
					// 从manager返回的时候拼接了常量，这里去掉常量取得用户名 匿名
					username = useMsg
							.split(MagicConstants.MAGIC_NAMEPOST_SUCCESS)[1];
					body="{\"username\":"+"\""+username+"\"}";
				} else if (useMsg
						.contains(MagicConstants.MAGIC_CHECKONLINE_ONLINE)) {
					// 在线
					body="{\"status\":"+"\""+MagicConstants.MAGIC_CHECKONLINE_ONLINE+"\"}";
				} else if (useMsg
						.contains(MagicConstants.MAGIC_CHECKONLINE_OFFLINE)) {
					// 离线
					body="{\"status\":"+"\""+MagicConstants.MAGIC_CHECKONLINE_OFFLINE+"\"}";
				} else if (useMsg
						.contains(MagicConstants.MAGIC_SHOWIP_SUCCESS)) {
					// 窥视
					username = useMsg
							.split(MagicConstants.MAGIC_SHOWIP_SUCCESS)[0];
					String ip = useMsg.split(MagicConstants.MAGIC_SHOWIP_SUCCESS)[1];
					body="{\"ip\":"+"\""+ip+"\"}";
				} else if (useMsg
						.contains(MagicConstants.MAGIC_MONEY_SUCCESS)) {
					// 金钱卡
					String  credit = useMsg
							.split(MagicConstants.MAGIC_MONEY_SUCCESS)[0];
					String money = useMsg
							.split(MagicConstants.MAGIC_MONEY_SUCCESS)[1];
					body="{\"credit\":"+"\""+money+credit+"\"}";
				}
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private Boolean hasNoPermission(String messageCode) {
		if (messageCode.equals(MagicConstants.MAGIC_OPEN_ERROR_NOIN_MODERATORS)
				|| messageCode.equals(MagicConstants.MAGIC_SOFEED_ERROR)) {
			return true;
		}
		return false;
	}
	
	private int hasPermission(BbsCommonMagic magic, BbsUser user, Integer pid) {
		Set<BbsUserGroup> userGroups = magic.getUseGroups();
		Set<BbsUserGroup> toUserGroups = magic.getToUseGroups();
		BbsPost post = null;
		BbsUserGroup postCreaterGroup = null;
		if (pid != null) {
			post = postMng.findById(pid);
			post.getCreater().getGroup();
		}
		// 道具没有勾选使用组，默认全部组用户可以使用
		if (userGroups == null || userGroups.size() == 0) {
			// 被使用为空则有权限使用该道具
			if (toUserGroups == null || toUserGroups.size() == 0) {
				return 1;
			} else {
				// 道具被的被使用组包含帖子的创建者所属组,pid存在则需要检查帖子的创建者组
				if (pid != null) {
					if (toUserGroups.contains(postCreaterGroup)) {
						return 1;
					} else {
						// 不允许被使用
						return -1;
					}
				} else {
					return 1;
				}

			}
		} else {
			// 道具使用组有勾选使用组，需要判断被使用组权限
			if (userGroups.contains(user.getGroup())) {
				if (toUserGroups == null || toUserGroups.size() == 0) {
					return 1;
				} else {
					// 道具被的被使用组包含帖子的创建者所属组
					if (pid != null) {
						if (toUserGroups.contains(postCreaterGroup)) {
							return 1;
						} else {
							// 不允许被使用
							return -1;
						}
					} else {
						return 1;
					}
				}
			} else {
				// 不允许使用
				return 0;
			}
		}
	}
}
