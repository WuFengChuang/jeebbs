package com.jeecms.bbs.api.member;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.ApiRecord;
import com.jeecms.bbs.entity.BbsMessage;
import com.jeecms.bbs.entity.BbsMessageReply;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.ApiRecordMng;
import com.jeecms.bbs.manager.BbsMessageMng;
import com.jeecms.bbs.manager.BbsMessageReplyMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class MessageApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(MessageApiAct.class);
	@Autowired
	private ApiRecordMng apiRecordMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsMessageMng bbsMessageMng;
	@Autowired
	private BbsMessageReplyMng bbsMessageReplyMng;
	
	/**
	 * 信息列表API
	 * @param sys  是否系统消息 非必选 默认false
	 * @param senderId   发送者id 非必选
	 * @param receiverId   接收者id 非必选
	 * @param typeId   类型 1消息，2留言,3打招呼  非必选默认1
	 * @param statu   状态 非必选
	 * @param pageNo   查询开始下标 非必选 默认0
	 * @param pageSize	  查询数量 非必选 默认10
	 * @param appId appId 必选 
	 * @param sessionKey 会话标识必选
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/message/list")
	public void messageList(Boolean sys,
			Integer senderId,Integer receiverId,
			Integer typeId, Boolean statu,
			Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if(pageNo==null){
			pageNo=1;
		}
		if(pageSize==null){
			pageSize=10;
		}
		if(typeId==null){
			typeId=BbsMessage.MESSAGE_TYPE_MESSAGE;
		}
		if(sys==null){
			sys=false;
		}
		WebErrors errors = WebErrors.create(request);
		BbsUser user = CmsUtils.getUser(request);
		//不能查看他人消息
		if(senderId!=null&&receiverId!=null){
			if(!senderId.equals(user.getId())){
				errors.addErrorString("not Your Info");
				code=ResponseCode.API_CODE_NOT_YOUR_INFO;
			}
		}else if(senderId==null&&receiverId==null){
			errors.addErrorString(Constants.API_MESSAGE_PARAM_REQUIRED);
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}else{
			if(senderId!=null){
				if(!senderId.equals(user.getId())){
					errors.addErrorString("not Your Info");
					code=ResponseCode.API_CODE_NOT_YOUR_INFO;
				}
			}
			if(receiverId!=null){
				if(!receiverId.equals(user.getId())){
					errors.addErrorString("not Your Info");
					code=ResponseCode.API_CODE_NOT_YOUR_INFO;
				}
			}
		}
		if (!errors.hasErrors()) {
			Pagination page = bbsMessageMng.getPagination(sys, null, senderId, receiverId, typeId, statu, null, pageNo, pageSize);
			List<BbsMessage> list = (List<BbsMessage>) page.getList();
			JSONArray jsonArray=new JSONArray();
			int totalCount = page.getTotalCount();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					jsonArray.put(i, list.get(i).convertToJson());
				}
			}
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		}else{
			message = errors.getErrors().get(0);
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 获取消息的回复列表
	 * @param msgId 消息Id 必选
	 * @param first   查询开始下标 非必选默认0
	 * @param count	  查询数 非必选默认10
	 * @param appId appId 必选 
	 * @param sessionKey 会话标识必选
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/replyMsg/list")
	public void replyMessageList(Integer msgId,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		WebErrors errors = WebErrors.create(request);
		if(pageNo==null){
			pageNo=0;
		}
		if(pageSize==null){
			pageSize=10;
		}
		BbsUser user = CmsUtils.getUser(request);
		errors = ApiValidate.validateRequiredParams(errors, msgId);
		if (!errors.hasErrors()) {
			BbsMessage msg = bbsMessageMng.findById(msgId);
			if (msg==null) {
				errors.addErrorString(Constants.API_MESSAGE_MSG_NOT_FOUND);
				code=ResponseCode.API_CODE_NOT_FOUND;
			}
			if (!validateReply(user, msg)) {
				errors.addErrorString("not Your Info");
				code=ResponseCode.API_CODE_NOT_YOUR_INFO;
			}
			if (!errors.hasErrors()) {
				Pagination page = bbsMessageReplyMng.getPageByMsgId(msgId, pageNo, pageSize);
				List<BbsMessageReply> list = (List<BbsMessageReply>) page.getList();
				int totalCount = page.getTotalCount();
				JSONArray jsonArray=new JSONArray();
				if(list!=null&&list.size()>0){
					for(int i=0;i<list.size();i++){
						jsonArray.put(i, list.get(i).convertToJson());
					}
				}
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = jsonArray.toString()+",\"totalCount\":"+totalCount;
			}else{
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 获取单条信息
	 * @param id 信息id 必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping(value = "/message/get")
	public void messageGet(Integer id,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsMessage msg = null;
		WebErrors errors = WebErrors.create(request);
		BbsUser user = CmsUtils.getUser(request);
		if (id!=null) {
			if (id.equals(0)) {
				msg = new BbsMessage();
			}else{
				msg = bbsMessageMng.findById(id);
			}
			if (msg!=null) {
				//不能查看他人消息
				if(msg.getUser()!=null&&!user.getId().equals(msg.getUser().getId())){
					errors.addErrorString("not Your Info");
					code=ResponseCode.API_CODE_NOT_YOUR_INFO;
				}	
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
			if (!errors.hasErrors()) {
				JSONObject json = msg.convertToJson();
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = json.toString();
			}else{
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 发送消息
	 * @param receiver 消息目标用户名  必选
	 * @param msgType 消息类型  非必选 1消息,2留言,3打招呼 默认1
	 * @param content 内容 必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@SignValidate
	@RequestMapping(value = "/message/save")
	public void messageSave(String receiver,String content,Integer msgType,
			String appId,String sign,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors = WebErrors.create(request);
		if (msgType==null) {
			msgType=1;
		}
		errors = ApiValidate.validateRequiredParams(errors,receiver,content);
		if (!errors.hasErrors()) {
			errors=validateSendMsg(errors, false, user, receiver, content);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_PARAM_ERROR;
			}
			if (!errors.hasErrors()) {
				//签名数据不可重复利用
				ApiRecord record=apiRecordMng.findBySign(sign, appId);
				if(record!=null){
					message=Constants.API_MESSAGE_REQUEST_REPEAT;
					code=ResponseCode.API_CODE_REQUEST_REPEAT;
				}else{
					BbsUser receiverUser=bbsUserMng.findByUsername(receiver);
					BbsMessage msg=new BbsMessage();
					msg.setContent(content);
					msg.setCreateTime(Calendar.getInstance().getTime());
					msg.setMsgType(msgType);
					msg.setReceiver(receiverUser);
					msg.setSender(user);
					msg.setStatus(false);
					msg.setSys(false);
					msg.setUser(user);
					msg.init();
					BbsMessage mess=bbsMessageMng.sendMsg(user, receiverUser, msg);
					apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
							appId, "/message/save",sign);
					body="{\"id\":"+"\""+mess.getId()+"\"}";
					log.info("save message id={}",mess.getId());
					message=Constants.API_MESSAGE_SUCCESS;
					status=Constants.API_STATUS_SUCCESS;
					code=ResponseCode.API_CODE_CALL_SUCCESS;
				}
			}else{
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 发送系统消息
	 * @param content 内容 必选
	 * @param appId appId 必选 
	 * @param receiverId 接收用户ID 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping(value = "/message/sysSend")
	public void sysMessageSave(String content,Integer receiverId,String appId,String sign,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		BbsUser user = CmsUtils.getUser(request);
		BbsUser receiverUser=null;
		errors = ApiValidate.validateRequiredParams(errors,content);
		if (!errors.hasErrors()) {
			errors=validateSendMsg(errors, true, user, null, content);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_PARAM_ERROR;
			}
			if (!errors.hasErrors()) {
				//签名数据不可重复利用
				ApiRecord record=apiRecordMng.findBySign(sign, appId);
				if(record!=null){
					message=Constants.API_MESSAGE_REQUEST_REPEAT;
					code=ResponseCode.API_CODE_REQUEST_REPEAT;
				}else{
					BbsMessage msg=new BbsMessage();
					msg.setContent(content);
					msg.setCreateTime(Calendar.getInstance().getTime());
					msg.setMsgType(BbsMessage.MESSAGE_TYPE_MESSAGE);
					msg.setSender(user);
					msg.setStatus(false);
					msg.setSys(true);
					msg.setUser(user);
					if(receiverId!=null){
						receiverUser=bbsUserMng.findById(receiverId);
					}
					BbsMessage mess=bbsMessageMng.sendSysMsg(user,receiverUser,null,null,msg);
					apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
							appId, "/message/sysSend",sign);
					body="{\"id\":"+"\""+mess.getId()+"\"}";
					log.info("sysSend message id={}",mess.getId());
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
	 * 消息删除
	 * @param id 消息ID 必选 
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@SignValidate
	@RequestMapping(value = "/message/delete")
	public void messageDelete(Integer id,String appId,String sign,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, id);
		if (!errors.hasErrors()) {
			BbsMessage msg = bbsMessageMng.findById(id);
			errors=ApiValidate.validateRequiredParams(errors, msg);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_NOT_FOUND;
			}
			if (!errors.hasErrors()) {
				//签名数据不可重复利用
				ApiRecord record=apiRecordMng.findBySign(sign, appId);
				if(record!=null){
					message=Constants.API_MESSAGE_REQUEST_REPEAT;
					code=ResponseCode.API_CODE_REQUEST_REPEAT;
				}else{
					if (validateDeleteMessage(user, msg)) {
						bbsMessageMng.deleteById(id);
						apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
								appId, "/message/delete",sign);
						log.info("delete message id={}",id);
						message=Constants.API_MESSAGE_SUCCESS;
						status=Constants.API_STATUS_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
					}else{
						message=Constants.API_MESSAGE_DELETE_ERROR;
						code=ResponseCode.API_CODE_DELETE_ERROR;
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
	 * 回复消息删除
	 * @param id 消息回复ID
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@SignValidate
	@RequestMapping(value = "/replyMsg/delete")
	public void replyMessageDelete(Integer id,String sign,String appId,
			HttpServletResponse response,HttpServletRequest request){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, id);
		if (!errors.hasErrors()) {
			BbsMessageReply replyMsg = bbsMessageReplyMng.findById(id);
			errors=ApiValidate.validateRequiredParams(errors, replyMsg);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_NOT_FOUND;
			}
			if (!errors.hasErrors()) {
				//签名数据不可重复利用
				ApiRecord record=apiRecordMng.findBySign(sign, appId);
				if(record!=null){
					message=Constants.API_MESSAGE_REQUEST_REPEAT;
					code=ResponseCode.API_CODE_REQUEST_REPEAT;
				}else{
					if (validateDeleteReply(user, replyMsg)) {
						bbsMessageReplyMng.deleteById(id);
						apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
								appId, "/replyMsg/delete",sign);
						log.info("delete reply message id={}",id);
						message=Constants.API_MESSAGE_SUCCESS;
						status=Constants.API_STATUS_SUCCESS;
						code = ResponseCode.API_CODE_CALL_SUCCESS;
					}else{
						message="\""+Constants.API_MESSAGE_DELETE_ERROR+"\"";
						code=ResponseCode.API_CODE_DELETE_ERROR;
					}
				}
			}else{
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private boolean validateDeleteReply(BbsUser user, BbsMessageReply msg) {
		if (user == null) {
			return false;
		}
		if (msg == null) {
			return false;
		}
		if (!msg.getMessage().getUser().equals(user)) {
			return false;
		}
		return true;
	}
	
	
	private boolean validateDeleteMessage(BbsUser user, BbsMessage msg) {
		if (user == null) {
			return false;
		}
		if (msg == null) {
			return false;
		}
		if (!msg.getUser().equals(user)) {
			return false;
		}
		return true;
	}
	
	private WebErrors validateSendMsg(WebErrors errors,boolean sysMessage,
			BbsUser user,String receiver,String content) {
		if(!sysMessage){
			// 用户名为空
			if (StringUtils.isBlank(receiver)) {
				errors.addErrorString("friend send no name");
				return errors;
			}
			// 不允许发送消息给自己
			if (receiver.equals(user.getUsername())) {
				errors.addErrorString("friend send myself");
				return errors;
			}
			// 用户名不存在
			if (bbsUserMng.findByUsername(receiver) == null) {
				errors.addErrorString("friend send no exist name");
				return errors;
			}
		}else{
			if(user.getOfficial()==null||!user.getOfficial()){
				errors.addErrorString("friend send not official user");
				return errors;
			}
		}
		if(StringUtils.isBlank(filterUserInputContent(content))){
			errors.addErrorString("operate faile");
			return errors;
		}
		return errors;
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
	
	private boolean validateReply(BbsUser user, BbsMessage msg) {
		if(msg!=null){
			if(msg.getSys()!=null&&msg.getSys()){
				return true;
			}
			if (!msg.getUser().equals(user)) {
				return false;
			}
			return true;
		}else{
			return false;
		}
	}
	
	private final static Whitelist user_content_filter = Whitelist.relaxed();
	static {
		user_content_filter.addTags("embed","object","param","span","div");
		user_content_filter.addAttributes(":all", "style", "class", "id", "name");
		user_content_filter.addAttributes("object", "width", "height","classid","codebase");	
		user_content_filter.addAttributes("param", "name", "value");
		user_content_filter.addAttributes("embed", "src","quality","width","height","allowFullScreen","allowScriptAccess","flashvars","name","type","pluginspage");
	}
}
