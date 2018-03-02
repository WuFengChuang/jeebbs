package com.jeecms.bbs.api.member;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.ApiRecord;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicCharge;
import com.jeecms.bbs.entity.BbsTopicPostOperate;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.entity.BbsVoteItem;
import com.jeecms.bbs.entity.BbsVoteTopic;
import com.jeecms.bbs.manager.ApiRecordMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsLimitMng;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.bbs.manager.BbsTopicCountMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsTopicPostOperateMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.manager.BbsVoteItemMng;
import com.jeecms.bbs.manager.BbsVoteRecordMng;
import com.jeecms.bbs.manager.CmsSensitivityMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.ArrayUtils;
import com.jeecms.common.util.CheckMobile;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.web.WebErrors;
@Controller
public class BbsTopicApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsTopicApiAct.class);
	//置顶
	private static final String OPERATE_TOP="top";
	//加精
	private static final String OPERATE_PRIME="prime";
	//加亮
	private static final String OPERATE_LIGHT="light";
	//锁定
	private static final String OPERATE_LOCK="lock";
	//打开
	private static final String OPERATE_OPEN="open";
	//上升下沉
	private static final String OPERATE_UP_DOWN="upOrDown";
	//移动
	private static final String OPERATE_MOVE="move";
	//屏蔽
	private static final String OPERATE_SHIELD="shield";
	//解除屏蔽
	private static final String OPERATE_SHIELD_OPEN="shieldOpen";
	//删除
	private static final String OPERATE_DELETE="delete";
	//打赏
	private static final String OPERATE_REWARD="reward";
	//购买
	private static final String OPERATE_BUY="buy";
	//投票
	private static final String OPERATE_VOTE="vote";
	
	/**
	 * 我关注的主题列表
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/topic/myAttent_topic")
	public void myAttentTopic(Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		BbsUser user = CmsUtils.getUser(request);
		if (pageNo==null) {
			pageNo = 1;
		}
		if (pageSize == null) {
			pageSize = 10;
		}
		Pagination page = topicPostOperateMng.getPage(BbsTopicPostOperate.DATA_TYPE_TOPIC, user.getId(),
				BbsTopicPostOperate.OPT_ATTENT, pageNo, pageSize);
		List<BbsTopicPostOperate> list = (List<BbsTopicPostOperate>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i < list.size() ; i++){
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 审核主题
	 * @param topicId 主题编号
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/topic/check")
	public void checkTopic(Integer topicId,HttpServletRequest request,
			HttpServletResponse response){
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, topicId);
		if (!errors.hasErrors()) {
			BbsUser user = CmsUtils.getUser(request);
			if (!user.getModerator()) {
				message = Constants.API_MESSAGE_NOT_MODERATOR;
				code = ResponseCode.API_CODE_NOT_MODERATOR;
			}else{
				BbsTopic topic = bbsTopicMng.findById(topicId);
				if (topic==null) {
					message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
					code = ResponseCode.API_CODE_NOT_FOUND;
				}else{
					topic.setCheckStatus(true);
					bbsTopicMng.update(topic);
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
					status = Constants.API_STATUS_SUCCESS;
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 主题列表API
	 * @param https
	 * @param checkStatus 审核状态
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/topic/list")
	public void topicList(Integer https,Boolean checkStatus,Integer pageNo,Integer pageSize,
			 HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String status = Constants.API_STATUS_FAIL;
		String message = Constants.API_MESSAGE_NOT_MODERATOR;
		String code = ResponseCode.API_CODE_NOT_MODERATOR;
		String body = "\"\"";
		if(pageNo==null){
			pageNo=1;
		}
		if(pageSize==null){
			pageSize=10;
		}
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		if (checkStatus==null) {
			checkStatus=false;
		}
		BbsUser user = CmsUtils.getUser(request);
		//判断用户是否为版主
		if (user.getModerator()) {
			Pagination page = bbsTopicMng.getForTag(null, null, null, null, null, null,
					null, null, null, null, null, null, checkStatus, pageNo, pageSize, null, 8,null);
			List<BbsTopic> list = (List<BbsTopic>) page.getList();
			int totalCount = page.getTotalCount();
			JSONArray jsonArray=new JSONArray();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					jsonArray.put(i, list.get(i).convertToJson(https,false,
							false,false,null,null));
				}
			}
			body = jsonArray.toString()+",\"totalCount\":"+totalCount;
			message = Constants.API_MESSAGE_SUCCESS;
			status = Constants.API_STATUS_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 获取主题信息
	 * @param https  url返回格式    1 http格式   0 https格式  默认0
	 * @param id 主题id 必选
	 * @param sessionKey 会话标识 非必选
	 * @param appId appId 非必选 如果存在以及sessionKey存在则判断用户是否收藏 关注  顶
	 */
	@RequestMapping(value = "/topic/get")
	public void topicGet(Integer id,Integer https,
			HttpServletRequest request,HttpServletResponse response) throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsTopic topic = null;
		if(https==null){
			https=Constants.URL_HTTP;
		}
		if (id != null) {
			if (id.equals(0)) {
				topic = new BbsTopic();
			}else{
				topic = bbsTopicMng.findById(id);
			}
			if (topic != null) {
				boolean hasCollect=false;
				boolean hasAttent=false;
				boolean hasUp=false;
				BbsUser user=CmsUtils.getUser(request);
				Boolean voted=null;
				if(user!=null){
					hasCollect=user.hasCollectTopic(id);
					hasAttent=user.hasAttentTopic(id);
					hasUp=user.hasUpTopic(id);
				}
				List<BbsVoteItem> list = null;
				//投票贴补充投票信息
				if(topic.getCategory()!=BbsTopic.TOPIC_NORMAL){
					if(user!=null){
						if (bbsVoteRecordMng.findRecord(user.getId(), id) != null) {
							voted=true;
						}else{
							voted=false;
						}
					}
					list = bbsVoteItemMng.findByTopic(id);
				}
				JSONObject json=topic.convertToJson(https,hasCollect,hasAttent,hasUp,
						voted,list);
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = json.toString();
			}else{
				code = ResponseCode.API_CODE_PARAM_ERROR;
				message = Constants.API_MESSAGE_PARAM_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 保存主题信息
	 * @param forumId 版块id 必选
	 * @param title 主题标题 必选
	 * @param content 主题内容 必选
	 * @param category 100普通帖 101 投票帖 非必选 默认100
	 * @param categoryType 1多选 2 单选  非必选 
	 * @param items 投票项标题 ,号分隔 非必选 
	 * @param hasAttach 内容是否包含附件
	 * @param charge 收费模式  0免费 1收费 2打赏   非必选 默认0
	 * @param chargeAmount 收费金额  非必选
	 * @param postLatitude  发帖者纬度 非必选
	 * @param postLongitude  发帖者经度  非必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@SignValidate
	@RequestMapping(value = "/topic/save")
	public void topicSave(Integer forumId, 
			String title,String content, Integer category, Integer categoryType,
			String items,Boolean hasAttach,Short charge,Double chargeAmount,
			Float postLatitude,Float postLongitude,String sign,String appId,
			HttpServletRequest request,HttpServletResponse response)throws JSONException {
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors=WebErrors.create(request);
		BbsUser user = CmsUtils.getUser(request);
		CmsSite site=CmsUtils.getSite(request);
		Integer siteId=site.getId();
		String ip=RequestUtils.getIpAddr(request);
		String[] voteItems=null;
		if(category==null){
			category=BbsTopic.TOPIC_NORMAL;
		}
		if(charge==null){
			charge=BbsTopicCharge.MODEL_FREE;
		}
		if(chargeAmount==null){
			chargeAmount=0d;
		}
		errors = ApiValidate.validateRequiredParams(errors,forumId,title,content);
		if (!errors.hasErrors()) {
			BbsForum forum = bbsForumMng.findById(forumId);
			//检查发帖权限等
			errors=checkTopic(errors, forum, user, ip);
			if (!errors.hasErrors()) {
				//检查敏感词
				if(cmsConfigMng.get().getSensitivityInputOn()){
					if(sensitivityMng.txtHasSensitivity(site.getId(), content)||
							sensitivityMng.txtHasSensitivity(site.getId(), title)){
						errors.addErrorString("post Txt Has Sensitivity");
						code=ResponseCode.API_CODE_POST_TXT_HAS_SENSITIVE;
					}
				}
			}else{
				code=ResponseCode.API_CODE_POST_HAS_NOT_PERM;
			}
			if (!errors.hasErrors()) {
				//签名数据不可重复利用
				ApiRecord record=apiRecordMng.findBySign(sign, appId);
				if(record!=null){
					message=Constants.API_MESSAGE_REQUEST_REPEAT;
					code=ResponseCode.API_CODE_REQUEST_REPEAT;
				}else{
					String userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();
					Short equip=CheckMobile.getSource(userAgent);
					BbsConfigCharge configCharge=configChargeMng.getDefault();
					CmsConfig cmsConfig=cmsConfigMng.get();
					Integer[] topicTypeIds=null;
					Double[]fixValues=ArrayUtils.convertStrArrayToDouble(cmsConfig.getRewardFixValues());
					BbsTopic bean = bbsTopicMng.postTopic(user.getId(),siteId, forumId,
							title, content, ip, category, categoryType,
							topicTypeIds,voteItems,null,hasAttach, null, equip,
							charge,chargeAmount,postLatitude,postLongitude,
							configCharge.getRewardPattern(),
							configCharge.getRewardMin(),configCharge.getRewardMax(),
							fixValues);
					Integer firstPostId=bean.getFirstPost().getId();
					bbsUserGroupMng.findNearByPoint(user.getTotalPoint(), user);
					log.info("save topic id={}",bean.getId());
					body="{\"id\":"+"\""+bean.getId()+"\",\"firstPostId\":"+"\""+firstPostId+"\"}";
					message=Constants.API_MESSAGE_SUCCESS;
					status=Constants.API_STATUS_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}
			}else{
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 主题置顶
	 * @param ids 主题id ,逗号分隔 必选
	 * @param forumId 版块id 必选
	 * @param topLevel 置顶级别  必选 1 本版置顶 2分区置顶  3全局置顶 0解除置顶
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping("/topic/top")
	public void topicTop(String  ids ,Short topLevel,String appId,String sign,
			HttpServletRequest request,HttpServletResponse response) {
		topicsOperate(OPERATE_TOP, ids, topLevel,null,
				null,null,null,null,appId,sign,request, response);
	}
	
	/**
	 * 主题加精
	 * @param ids 主题id ,逗号分隔 必选
	 * @param forumId 版块id 必选
	 * @param primeLevel 加精级别  必选  1精华I 2精华II 3精华III 0解除精华
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping("/topic/prime")
	public void topicPrime(String  ids ,Short primeLevel,
			String appId,String sign,
			 HttpServletRequest request,HttpServletResponse response) {
		topicsOperate(OPERATE_PRIME, ids, null,primeLevel,
				null,null,null,null,appId,sign,request, response);
	}
	
	/**
	 * 主题加亮
	 * @param ids 主题id ,逗号分隔 必选
	 * @param forumId 版块id 必选
	 * @param color 加亮颜色值  必选 
	 * @param bold 是否加粗  非必选 
	 * @param italic 是否斜体  非必选 
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping("/topic/light")
	public void topicLight(String  ids ,String color,
			Boolean bold, Boolean italic, String time,
			String appId ,String sign,
			HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		topicsOperate(OPERATE_LIGHT, ids, null,null,
				color,bold,italic,time,appId,sign,request, response);
	}
	
	/**
	 * 主题锁定
	 * @param ids 主题id ,逗号分隔 必选
	 * @param forumId 版块id 必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping("/topic/lock")
	public void topicLock(String  ids ,String appId,String sign, 
			HttpServletRequest request,HttpServletResponse response) {
		topicsOperate(OPERATE_LOCK, ids, null,null,
				null,null,null,null,appId,sign,request, response);
	}
	
	/**
	 * 主题打开
	 * @param ids 主题id ,逗号分隔 必选
	 * @param forumId 版块id 必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping("/topic/open")
	public void topicOpen(String  ids,String appId,String sign,
			HttpServletRequest request,HttpServletResponse response) {
		topicsOperate(OPERATE_OPEN, ids, null,null,
				null,null,null,null,appId,sign,request, response);
	}
	
	/**
	 * 主题提升下沉
	 * @param ids 主题id ,逗号分隔 必选
	 * @param forumId 版块id 必选
	 * @param time 排序时间 非必选 例如"2017-03-02 17:30:59"
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping("/topic/upOrDown")
	public void topicUpOrDown(String  ids , String time,
			String appId,String sign,
			HttpServletRequest request,HttpServletResponse response) {
		topicsOperate(OPERATE_UP_DOWN, ids, null,null,
				null,null,null,time,appId,sign,request, response);
	}
	
	/**
	 * 主题移动
	 * @param ids 主题id ,逗号分隔 必选
	 * @param forumId 移动至版块id 必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping("/topic/move")
	public void topicMove(String  ids , String appId,String sign,
			HttpServletRequest request,HttpServletResponse response) {
		topicsOperate(OPERATE_MOVE, ids, null,null,
				null,null,null,null,appId,sign,request, response);
	}
	
	/**
	 * 主题屏蔽
	 * @param ids 主题id ,逗号分隔 必选
	 * @param forumId 版块id 必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping("/topic/shield")
	public void topicShield(String  ids,String appId,String sign,
			HttpServletRequest request,HttpServletResponse response) {
		topicsOperate(OPERATE_SHIELD, ids, null,null,
				null,null,null,null,appId,sign,request, response);
	}
	
	/**
	 * 主题解除屏蔽
	 * @param ids 主题id ,逗号分隔 必选
	 * @param forumId 版块id 必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping("/topic/shieldOpen")
	public void topicShieldOpen(String  ids , String appId,String sign,
			HttpServletRequest request,HttpServletResponse response) {
		topicsOperate(OPERATE_SHIELD_OPEN, ids, null,null,
				null,null,null,null,appId,sign,request, response);
	}
	
	/**
	 * 主题删除
	 * @param ids 主题id ,逗号分隔 必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@SignValidate
	@RequestMapping("/topic/delete")
	public void topicDelete(String  ids,String appId,String sign,
			HttpServletRequest request,HttpServletResponse response) {
		topicsOperate(OPERATE_DELETE, ids, null,null,
				null,null,null,null,appId,sign,request, response);
	}
	
	/**
	 * 主题购买API
	 * @param id  主题ID 必选
	 * @param outOrderNum 外部订单号 必选
	 * @param orderType  1微信支付   2支付宝支付 必选
	 * @param appId appid 必选
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识  必选
	 */
	@RequestMapping(value = "/topic/buy")
	public void topicBuy(
			Integer id,String outOrderNum,
			Integer orderType,String appId,String sign,
			HttpServletRequest request,HttpServletResponse response)
					throws JSONException {
		singleTopicOperate(OPERATE_BUY, id, appId,sign,
				outOrderNum,orderType,null,request, response);
	}
	
	/**
	 * 主题打赏API
	 * @param id  主题ID 必选
	 * @param outOrderNum 外部订单号 必选
	 * @param orderType  1微信支付   2支付宝支付 必选
	 * @param appId appid 必选
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识 必选
	 */
	@RequestMapping(value = "/topic/reward")
	public void topicReward(
			Integer id,String outOrderNum,
			Integer orderType,String appId,String sign, 
			HttpServletRequest request,HttpServletResponse response)
					throws JSONException {
		singleTopicOperate(OPERATE_REWARD, id, appId,sign, 
				outOrderNum,orderType,null,request, response);
	}
	
	/**
	 * 主题投票
	 * @param id 主题id 必选
	 * @param itemIds 选项id ,逗号分隔 必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping(value = "/topic/vote")
	public void topicVote(
			Integer id,String itemIds,String appId,String sign, 
			HttpServletRequest request,HttpServletResponse response)
					throws JSONException {
		singleTopicOperate(OPERATE_VOTE, id, appId, sign, 
				null,null,itemIds,request, response);
	}
	
	/**
	 * 主题操作
	 * @param topicId 主题Id
	 * @param operate 操作类型 0点赞 1收藏 2关注  3取消点赞 4取消收藏 5取消关注 
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 */
	@RequestMapping(value = "/topic/operate")
	public void topicOperate(Integer topicId,Integer operate,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		if (operate==null) {
			operate = 0;
		}
		int count = 0;
		errors = ApiValidate.validateRequiredParams(errors, topicId);
		if (!errors.hasErrors()) {
			BbsTopic topic = bbsTopicMng.findById(topicId);
			if (topic!=null) {
				topicPostOperateMng.topicOperate(topicId,CmsUtils.getUserId(request), operate);
				if(operate!=null){
					if(operate.equals(BbsTopicPostOperate.OPT_ATTENT)){
						count=topicCountMng.topicAttent(topicId);
					}else if(operate.equals(BbsTopicPostOperate.OPT_CANCEL_ATTENT)){
						count=topicCountMng.topicCancelAttent(topicId);
					}else if(operate.equals(BbsTopicPostOperate.OPT_CANCEL_COLLECT)){
						count=topicCountMng.topicCancelCollect(topicId);
					}else if(operate.equals(BbsTopicPostOperate.OPT_CANCEL_UP)){
						count=topicCountMng.topicCancelUp(topicId);
					}else if(operate.equals(BbsTopicPostOperate.OPT_COLLECT)){
						count=topicCountMng.topicCollect(topicId);
					}else if(operate.equals(BbsTopicPostOperate.OPT_UP)){
						count=topicCountMng.topicUp(topicId);
					}
				}
				body="{\"count\":"+"\""+count+"\"}";
				message=Constants.API_STATUS_SUCCESS;
				status=Constants.API_STATUS_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				code=ResponseCode.API_CODE_NOT_FOUND;
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private void singleTopicOperate(String operate,Integer id,String appId,String sign ,
			String outOrderNum,Integer orderType,String itemIds,
			HttpServletRequest request,HttpServletResponse response) {
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, id);
		if (!errors.hasErrors()) {
			//签名数据不可重复利用
			ApiRecord record=apiRecordMng.findBySign(sign, appId);
			if(record!=null){
				message=Constants.API_MESSAGE_REQUEST_REPEAT;
				code=ResponseCode.API_CODE_REQUEST_REPEAT;
			}else{
				BbsTopic c=bbsTopicMng.findById(id);
				if(c!=null){
					if(operate.equals(OPERATE_BUY)){
						if(StringUtils.isNotBlank(outOrderNum)&&orderType!=null){
							//购买
							//外部订单号不可以用多次
							BbsOrder order=bbsOrderMng.findByOutOrderNum(outOrderNum, orderType);
							if(order==null){
								order=bbsOrderMng.topicOrder(id, orderType,
										BbsTopicCharge.MODEL_CHARGE, user.getId(),outOrderNum);
								if(order.getPrePayStatus()==BbsOrder.PRE_PAY_STATUS_SUCCESS){
									apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
											appId, "/topic/buy",sign);
									log.info("pay topic order");
									code=ResponseCode.API_CODE_CALL_SUCCESS;
									message=Constants.API_MESSAGE_SUCCESS;
									status=Constants.API_STATUS_SUCCESS;
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
						}else{
							message=Constants.API_MESSAGE_PARAM_REQUIRED;
							code=ResponseCode.API_CODE_PARAM_REQUIRED;
						}
					}else if(operate.equals(OPERATE_REWARD)){
						if(StringUtils.isNotBlank(outOrderNum)
								&&orderType!=null){
							//打赏
							//外部订单号不可以用多次
							BbsOrder buy=bbsOrderMng.findByOutOrderNum(outOrderNum, orderType);
							if(buy==null){
								//允许匿名打赏
								if(user!=null){
									buy=bbsOrderMng.topicOrder(id, orderType,
											BbsTopicCharge.MODEL_REWARD, user.getId(), outOrderNum);
								}else{
									buy=bbsOrderMng.topicOrder(id, orderType,
											BbsTopicCharge.MODEL_REWARD, null, outOrderNum);
								}
								if(buy.getPrePayStatus()==BbsOrder.PRE_PAY_STATUS_SUCCESS){
									apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
											appId, "/topic/reward",sign);
									code=ResponseCode.API_CODE_CALL_SUCCESS;
									message=Constants.API_MESSAGE_SUCCESS;
									status=Constants.API_STATUS_SUCCESS;
								}else if(buy.getPrePayStatus()==BbsOrder.PRE_PAY_STATUS_ORDER_NUM_ERROR){
									message=Constants.API_MESSAGE_ORDER_NUMBER_ERROR;
									code=ResponseCode.API_CODE_ORDER_NUMBER_ERROR;
								}
							}else{
								message=Constants.API_MESSAGE_ORDER_NUMBER_USED;
								code=ResponseCode.API_CODE_ORDER_NUMBER_USED;
							}
						}else{
							message=Constants.API_MESSAGE_PARAM_REQUIRED;
							code=ResponseCode.API_CODE_PARAM_REQUIRED;
						}
					}else if(operate.equals(OPERATE_VOTE)){
						if(StringUtils.isNotBlank(itemIds)){
							Integer[]intIds=ArrayUtils.parseStringToArray(itemIds);
							BbsVoteTopic topic = (BbsVoteTopic) bbsTopicMng.findById(id);
							errors = checkVote(errors, user,topic, intIds);
							if (errors.hasErrors()) {
								message=errors.getErrors().get(0);
								code=ResponseCode.API_CODE_PARAM_ERROR;
							} else {
								bbsVoteItemMng.vote(user, topic, intIds);
								code=ResponseCode.API_CODE_CALL_SUCCESS;
								message=Constants.API_MESSAGE_SUCCESS;
								status=Constants.API_STATUS_SUCCESS;
							}
						}else{
							message=Constants.API_MESSAGE_PARAM_REQUIRED;
							code=ResponseCode.API_CODE_PARAM_REQUIRED;
						}
					}
				}else{
					message=Constants.API_MESSAGE_TOPIC_NOT_FOUND;
					code=ResponseCode.API_CODE_NOT_FOUND;
				}
			}	
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private WebErrors checkVote(WebErrors errors, BbsUser user,
			BbsTopic topic, Integer[] itemIds) {
		if (user == null) {
			errors.addErrorString(Constants.API_MESSAGE_USER_NOT_LOGIN);
			return errors;
		}
		if (itemIds == null) {
			errors.addErrorString("no Choose");
			return errors;
		}
		// 选项不是同一主题
		for (Integer itemid : itemIds) {
			if (!topic.equals(bbsVoteItemMng.findById(itemid).getTopic())) {
				errors.addErrorString("vote wrongItem");
				return errors;
			}
		}
		// 已经投过票
		if (bbsVoteRecordMng.findRecord(user.getId(), topic.getId()) != null) {
			errors.addErrorString("vote hasVoted");
			return errors;
		}
		return errors;
	}
	

	private void topicsOperate(String operate,String  ids,Short topLevel,Short primeLevel,
			String color, Boolean bold, Boolean italic, String lighttime,
			String appId,String sign,
			HttpServletRequest request,HttpServletResponse response) {
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors =WebErrors.create(request);
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if(operate.equals(OPERATE_TOP)){
			errors=ApiValidate.validateRequiredParams(errors,topLevel);
		}else if(operate.equals(OPERATE_PRIME)){
			errors=ApiValidate.validateRequiredParams(errors,primeLevel);
		}else if(operate.equals(OPERATE_LIGHT)){
			errors=ApiValidate.validateRequiredParams(errors,color);
		}
		if (!errors.hasErrors()) {
			//签名数据不可重复利用
			ApiRecord record=apiRecordMng.findBySign(sign, appId);
			if(record!=null){
				message=Constants.API_MESSAGE_REQUEST_REPEAT;
				code=ResponseCode.API_CODE_REQUEST_REPEAT;
			}else{
				Integer[]intIds=ArrayUtils.parseStringToArray(ids);
				boolean topicNotFound=false;
				for(Integer id:intIds){
					BbsTopic c=bbsTopicMng.findById(id);
					if(c==null){
						topicNotFound=true;
						break;
					}
				}
				if(!topicNotFound){
					if(operate.equals(OPERATE_TOP)){
						errors = checkUpTop(errors, user, 
								site, BbsTopic.NORMAL);
						if (errors.hasErrors()) {
							message=errors.getErrors().get(0);
							code=ResponseCode.API_CODE_NOT_MODERATOR;
						}else{
							bbsTopicMng.upTop(intIds, topLevel, "", user);
							apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
									appId, "/topic/up",sign);
							log.info("up topic");
							message=Constants.API_MESSAGE_SUCCESS;
							status=Constants.API_STATUS_SUCCESS;
							code = ResponseCode.API_CODE_CALL_SUCCESS;
						}
					}else if(operate.equals(OPERATE_PRIME)){
						errors = checkManager(errors, user, site);
						if (errors.hasErrors()) {
							message=errors.getErrors().get(0);
							code=ResponseCode.API_CODE_NOT_MODERATOR;
						}else{
							bbsTopicMng.prime(intIds, primeLevel, "", user);
							apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
									appId, "/topic/prime",sign);
							log.info("prime topic");
							message=Constants.API_MESSAGE_SUCCESS;
							status=Constants.API_STATUS_SUCCESS;
							code = ResponseCode.API_CODE_CALL_SUCCESS;
						}
					}else if(operate.equals(OPERATE_LIGHT)){
						errors = checkManager(errors, user, site);
						if (errors.hasErrors()) {
							message=errors.getErrors().get(0);
							code=ResponseCode.API_CODE_NOT_MODERATOR;
						}else{
							if(bold==null){
								bold=false;
							}
							if(italic==null){
								italic=false;
							}
							Date time=null;
							if(StringUtils.isNotBlank(lighttime)){
								time=DateUtils.parseTimeStrToDate(lighttime);
							}
							bbsTopicMng.highlight(intIds, color, bold, italic,
									time, "", user);
							apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
									appId, "/topic/light",sign);
							log.info("highlight topic");
							message=Constants.API_MESSAGE_SUCCESS;
							status=Constants.API_STATUS_SUCCESS;
							code = ResponseCode.API_CODE_CALL_SUCCESS;
						}
					}else if(operate.equals(OPERATE_LOCK)){
						errors = checkManager(errors, user, site);
						if (errors.hasErrors()) {
							message=errors.getErrors().get(0);
							code=ResponseCode.API_CODE_NOT_MODERATOR;
						}else{
							bbsTopicMng.lockOrOpen(intIds, true, "", user);
							apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
									appId, "/topic/lock",sign);
							log.info("lock topic");
							message=Constants.API_MESSAGE_SUCCESS;
							status=Constants.API_STATUS_SUCCESS;
							code = ResponseCode.API_CODE_CALL_SUCCESS;
						}
					}else if(operate.equals(OPERATE_OPEN)){
						errors = checkManager(errors, user, site);
						if (errors.hasErrors()) {
							message=errors.getErrors().get(0);
							code=ResponseCode.API_CODE_NOT_MODERATOR;
						}else{
							bbsTopicMng.lockOrOpen(intIds, false, "", user);
							apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
									appId, "/topic/open",sign);
							log.info("open topic");
							message=Constants.API_MESSAGE_SUCCESS;
							status=Constants.API_STATUS_SUCCESS;
							code = ResponseCode.API_CODE_CALL_SUCCESS;
						}
					}else if(operate.equals(OPERATE_UP_DOWN)){
						errors = checkManager(errors, user, site);
						if (errors.hasErrors()) {
							message=errors.getErrors().get(0);
							code=ResponseCode.API_CODE_NOT_MODERATOR;
						}else{
							Date time=null;
							if(StringUtils.isNotBlank(lighttime)){
								time=DateUtils.parseTimeStrToDate(lighttime);
							}
							bbsTopicMng.upOrDown(intIds, time, "", user);
							apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
									appId, "/topic/upOrDown",sign);
							log.info("up down topic");
							message=Constants.API_MESSAGE_SUCCESS;
							status=Constants.API_STATUS_SUCCESS;
							code = ResponseCode.API_CODE_CALL_SUCCESS;
						}
					}else if(operate.equals(OPERATE_MOVE)){
						errors = checkShield(errors, user, site);
						if (errors.hasErrors()) {
							message=errors.getErrors().get(0);
							code=ResponseCode.API_CODE_NOT_MODERATOR;
						}else{
//							bbsTopicMng.move(intIds, forumId, "", user);
							apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
									appId, "/topic/move",sign);
							log.info("move topic");
							message=Constants.API_MESSAGE_SUCCESS;
							status=Constants.API_STATUS_SUCCESS;
							code = ResponseCode.API_CODE_CALL_SUCCESS;
						}
					}else if(operate.equals(OPERATE_SHIELD)){
						errors = checkShield(errors, user, site);
						if (errors.hasErrors()) {
							message=errors.getErrors().get(0);
							code=ResponseCode.API_CODE_NOT_MODERATOR;
						}else{
							bbsTopicMng.shieldOrOpen(intIds, true, "", user);
							apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
									appId, "/topic/shield",sign);
							log.info("shield topic");
							message=Constants.API_MESSAGE_SUCCESS;
							status=Constants.API_STATUS_SUCCESS;
							code = ResponseCode.API_CODE_CALL_SUCCESS;
						}
					}else if(operate.equals(OPERATE_SHIELD_OPEN)){
						errors = checkShield(errors, user, site);
						if (errors.hasErrors()) {
							message=errors.getErrors().get(0);
							code=ResponseCode.API_CODE_NOT_MODERATOR;
						}else{
							bbsTopicMng.shieldOrOpen(intIds, false, "", user);
							apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
									appId, "/topic/shieldOpen",sign);
							log.info("shield open topic");
							message=Constants.API_MESSAGE_SUCCESS;
							status=Constants.API_STATUS_SUCCESS;
							code = ResponseCode.API_CODE_CALL_SUCCESS;
						}
					}else if(operate.equals(OPERATE_DELETE)){
						bbsTopicMng.deleteByIds(intIds);
						apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
								appId, "/topic/delete",sign);
						log.info("delete topic");
						message=Constants.API_MESSAGE_SUCCESS;
						status=Constants.API_STATUS_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
					}
				}else{
					message=Constants.API_MESSAGE_TOPIC_NOT_FOUND;
					code=ResponseCode.API_CODE_NOT_FOUND;
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	private WebErrors checkManager(WebErrors errors, BbsUser user,
			CmsSite site) {
		BbsUserGroup group = null;
		if (user == null) {
			group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
		} else {
			group = user.getGroup();
		}
		if (group == null) {
			errors.addErrorString("has not permission");
			return errors;
		}
		if (!group.allowTopic()) {
			errors.addErrorString("has not permission");
			return errors;
		}
//		if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
//			errors.addErrorString("has not permission");
//			return errors;
//		}
//		if (!group.hasRight(forum, user)) {
//			errors.addErrorString("has not permission");
//			return errors;
//		}
		if (!group.topicManage()) {
			errors.addErrorString("has not permission");
			return errors;
		}
		return errors;
	}
	
	private WebErrors checkShield(WebErrors errors ,
			BbsUser user, CmsSite site) {
				BbsUserGroup group = null;
		if (user == null) {
			group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
		} else {
			group = user.getGroup();
		}
		if (group == null) {
			errors.addErrorString("has not permission");
			return errors;
		}
		if (!group.allowTopic()) {
			errors.addErrorString("has not permission");
			return errors;
		}
//		if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
//			errors.addErrorString("has not permission");
//			return errors;
//		}
//		if (!group.hasRight(forum, user)) {
//			errors.addErrorString("has not permission");
//			return errors;
//		}
		if (!group.topicShield()) {
			errors.addErrorString("has not permission");
			return errors;
		}
		return errors;
	}
	
	private WebErrors checkUpTop(WebErrors errors, BbsUser user, CmsSite site,
			Short topLevel) {
		BbsUserGroup group = null;
		if (user == null) {
			group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
		} else {
			group = user.getGroup();
		}
		if (group == null) {
			errors.addErrorString("has not permission");
			return errors;
		}
		if (!group.allowTopic()) {
			errors.addErrorString("has not permission");
			return errors;
		}
//		if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
//			errors.addErrorString("has not permission");
//			return errors;
//		}
//		if (!group.hasRight(forum, user)) {
//			errors.addErrorString("has not permission");
//			return errors;
//		}
		if (group.topicTop() == 0) {
			errors.addErrorString("has not permission");
			return errors;
		}
		if (topLevel > 0) {
			if (group.topicTop() < topLevel) {
				errors.addErrorString("has not permission");
				return errors;
			}
		}
		return errors;
	}
	
	private WebErrors checkTopic(WebErrors errors,BbsForum forum, BbsUser user, 
			String ip) {
		if(forum!=null){
			if (forum.getGroupTopics() == null) {
				errors.addErrorString("has not permission");
				return errors;
			} else {
				BbsUserGroup group = user.getGroup();
				if (user.getProhibit()) {
					errors.addErrorString("member gag");
					return errors;
				}
				if (group == null) {
					errors.addErrorString("has not permission");
					return errors;
				}
				if (!group.allowTopic()) {
					errors.addErrorString("has not permission");
					return errors;
				}
				if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
					errors.addErrorString("has not permission");
					return errors;
				}
				if (!group.checkPostToday(user.getPostToday())) {
					errors.addErrorString("post to many");
					return errors;
				}
			}
		}else{
			errors.addErrorString("forum not Exist");
			return errors;
		}
		boolean ipLimit=bbsLimitMng.ipIsLimit(ip);
		if(ipLimit){
			errors.addErrorString("ip forbidden");
			return errors;
		}
		if(user!=null){
			boolean userLimit=bbsLimitMng.userIsLimit(user.getId());
			if(userLimit){
				errors.addErrorString("user forbidden");
				return errors;
			}
		}
		return errors;
	}
	
	@Autowired
	private BbsTopicMng bbsTopicMng;
	@Autowired
	private ApiRecordMng apiRecordMng;
	@Autowired
	private BbsForumMng bbsForumMng;
	@Autowired
	private BbsLimitMng bbsLimitMng;
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private CmsSensitivityMng sensitivityMng;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsOrderMng bbsOrderMng;
	@Autowired
	private BbsVoteItemMng bbsVoteItemMng;
	@Autowired
	private BbsVoteRecordMng bbsVoteRecordMng;
	@Autowired
	private BbsTopicCountMng topicCountMng;
	@Autowired
	private BbsTopicPostOperateMng topicPostOperateMng;
}
