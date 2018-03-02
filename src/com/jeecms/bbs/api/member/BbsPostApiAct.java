package com.jeecms.bbs.api.member;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
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
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsGrade;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicPostOperate;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.ApiRecordMng;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsGradeMng;
import com.jeecms.bbs.manager.BbsLimitMng;
import com.jeecms.bbs.manager.BbsPostCountMng;
import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsTopicPostOperateMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.manager.CmsSensitivityMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.ArrayUtils;
import com.jeecms.common.util.CheckMobile;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsPostApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsPostApiAct.class);
	
	/**
	 * 审核帖子
	 * @param postId 帖子编号
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/post/check")
	public void checkPost(Integer postId,HttpServletRequest request,
			HttpServletResponse response){
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, postId);
		if (!errors.hasErrors()) {
			BbsUser user = CmsUtils.getUser(request);
			if (!user.getModerator()) {
				message = Constants.API_MESSAGE_NOT_MODERATOR;
				code = ResponseCode.API_CODE_NOT_MODERATOR;
			}else{
				BbsPost post = bbsPostMng.findById(postId);
				if (post!=null) {
					post.setCheckStatus(true);
					bbsPostMng.update(post);
					message=Constants.API_MESSAGE_SUCCESS;
					status = Constants.API_STATUS_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
				}else{
					message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
					code = ResponseCode.API_CODE_NOT_FOUND;
				}
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 待审核贴列表
	 * @param https
	 * @param checkStatus 审核状态 默认false
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/post/list")
	public void postList(Integer https,Boolean checkStatus,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		String status = Constants.API_STATUS_FAIL;
		String message = Constants.API_MESSAGE_NOT_MODERATOR;
		String code = ResponseCode.API_CODE_NOT_MODERATOR;
		String body = "\"\"";
		if (https==null) {
			https = Constants.URL_HTTP;
		}
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		if (checkStatus==null) {
			checkStatus = false;
		}
		BbsUser user = CmsUtils.getUser(request);
		if (user.getModerator()) {
			Pagination page = bbsPostMng.getForTag(null, null, null, null,
					checkStatus,BbsPost.OPT_ALL, 0, pageNo, pageSize);
			List<BbsPost> list = (List<BbsPost>) page.getList();
			int totalCount = page.getTotalCount();
			JSONArray jsonArray = new JSONArray();
			if (list!=null&&list.size()>0) {
				for(int i = 0 ; i < list.size(); i++){
					jsonArray.put(i, list.get(i).convertToJson(https));
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
	 * 保存帖子信息
	 * @param topicId 主题id 必选
	 * @param parentId 父帖id  非必选
	 * @param title 标题 必选
	 * @param content 内容 必选
	 * @param hasAttach 内容是否包含附件 非必选 
	 * @param postLatitude  发帖者纬度 非必选
	 * @param postLongitude  发帖者经度  非必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@SignValidate
	@RequestMapping(value = "/post/save")
	public void postSave(Integer topicId,Integer parentId,String content,Boolean hasAttach,
			Float postLatitude,Float postLongitude,String sign,String appId,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors  = ApiValidate.validateRequiredParams(errors, topicId,content);
		if (!errors.hasErrors()) {
			String ip=RequestUtils.getIpAddr(request);
			BbsUser user = CmsUtils.getUser(request);
			CmsSite site=CmsUtils.getSite(request);
			BbsTopic topic = bbsTopicMng.findById(topicId);
			BbsForum forum = null;
			if (topic!=null) {
				forum = topic.getForum();
			}
			//检查发帖权限等
			errors = checkReply(errors, forum, topic, user, ip, site);
			if (!errors.hasErrors()) {
				//检查敏感词
				if (cmsConfigMng.get().getSensitivityInputOn()) {
					if (sensitivityMng.txtHasSensitivity(site.getId(), content)) {
						errors.addErrorString("post txt has Sensitivity");
						code=ResponseCode.API_CODE_POST_TXT_HAS_SENSITIVE;
					}
				}
			}else{
				message=errors.getErrors().get(0);
				code=ResponseCode.API_CODE_POST_HAS_NOT_PERM;
			}
			if (!errors.hasErrors()) {
				//签名数据不可重复利用
				ApiRecord record = apiRecordMng.findBySign(sign, appId);
				if (record==null) {
					String userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();
					Short equip=CheckMobile.getSource(userAgent);
					BbsPost post = bbsPostMng.reply(user.getId(), site.getId(), topicId,parentId,
							content, ip,
							null,hasAttach, null,equip,postLatitude,postLongitude);
					apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
							appId, "/post/save",sign);
					bbsUserGroupMng.findNearByPoint(user.getTotalPoint(), user);
					log.info("save post id={}",post.getId());
					body="{\"id\":"+"\""+post.getId()+"\"}";
					message=Constants.API_MESSAGE_SUCCESS;
					status=Constants.API_STATUS_SUCCESS;
					code=ResponseCode.API_CODE_CALL_SUCCESS;
				}else{
					message=Constants.API_MESSAGE_REQUEST_REPEAT;
					code=ResponseCode.API_CODE_REQUEST_REPEAT;
				}
			}else{
				message=errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 修改帖子信息
	 * @param postId 帖子id 必选
	 * @param title 标题 必选
	 * @param content 内容 必选
	 * @param hasAttach 内容是否包含附件 非必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@SignValidate
	@RequestMapping(value = "/post/update")
	public void postUpdate(Integer postId,
			String content,Boolean hasAttach,String sign,String appId,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, postId,content);
		if (!errors.hasErrors()) {
			BbsPost post = bbsPostMng.findById(postId);
			BbsForum forum = null;
			CmsSite site = CmsUtils.getSite(request);
			String ip=RequestUtils.getIpAddr(request);
			BbsUser user = CmsUtils.getUser(request);
			if (post!=null&&post.getTopic()!=null) {
				forum = post.getTopic().getForum();
			}
			//检查发帖权限等
			errors = checkEdit(errors, forum, post, user, ip);
			if (!errors.hasErrors()) {
				//检查敏感词
				if(cmsConfigMng.get().getSensitivityInputOn()){
					if(sensitivityMng.txtHasSensitivity(site.getId(), content)){
						errors.addErrorString("post txt has sensitivity");
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
					post = bbsPostMng.updatePost(postId,content, user, ip,
							null, null,hasAttach);
					apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
							appId, "/post/update",sign);
					log.info("update post id={}",post.getId());
					body="{\"id\":"+"\""+post.getId()+"\"}";
					message=Constants.API_MESSAGE_SUCCESS;
					status=Constants.API_STATUS_SUCCESS;
					code=ResponseCode.API_CODE_CALL_SUCCESS;
				}
			}else{
				message=errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 评分
	 * @param postId 帖子ID 必选
	 * @param score 分值 必选
	 * @param reason 原因  非必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping(value = "/post/grade")
	public void postGrade(Integer postId,Integer score,String reason,
			String appId,String sign,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, postId,score);
		if (!errors.hasErrors()) {
			BbsPost post = bbsPostMng.findById(postId);
			BbsForum forum = null;
			BbsUser user = CmsUtils.getUser(request);
			CmsSite site = CmsUtils.getSite(request);
			if (post !=null) {
				forum = post.getTopic().getForum();
			}
			errors = checkGrade(errors, forum, user, site);
			if (!errors.hasErrors()) {
				//签名数据不可重复利用
				ApiRecord record=apiRecordMng.findBySign(sign, appId);
				if(record!=null){
					message=Constants.API_MESSAGE_REQUEST_REPEAT;
					code=ResponseCode.API_CODE_REQUEST_REPEAT;
				}else{
					BbsGrade grade=new BbsGrade();
					grade.setGrader(user);
					grade.setGradeTime(Calendar.getInstance().getTime());
					grade.setPost(post);
					grade.setReason(reason);
					grade.setScore(score);
					bbsGradeMng.saveGrade(grade, user, post);
					apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
							appId, "/post/grade",sign);
					log.info("post grade id={}",post.getId());
					body="{\"id\":"+"\""+post.getId()+"\"}";
					message=Constants.API_MESSAGE_SUCCESS;
					status=Constants.API_STATUS_SUCCESS;
					code=ResponseCode.API_CODE_CALL_SUCCESS;
				}
			}else{
				code=ResponseCode.API_CODE_POST_HAS_NOT_PERM;
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 帖子屏蔽解除屏蔽 
	 * @param postId 帖子ID
	 * @param statu 状态  -1屏蔽 0解除
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping(value = "/post/shield")
	public void postShield(Integer postId,Short statu,String sign,String appId,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, postId,statu);
		if (!errors.hasErrors()) {
			BbsPost post = bbsPostMng.findById(postId);
			BbsForum forum = null;
			BbsUser user = CmsUtils.getUser(request);
			CmsSite site = CmsUtils.getSite(request);
			if(post!=null){
				forum=post.getTopic().getForum();
			}
			errors=checkShield(errors,forum,user, site);
			if(!errors.hasErrors()){
				//签名数据不可重复利用
				ApiRecord record=apiRecordMng.findBySign(sign, appId);
				if(record!=null){
					message=Constants.API_MESSAGE_REQUEST_REPEAT;
					code=ResponseCode.API_CODE_REQUEST_REPEAT;
				}else{
					post = bbsPostMng.shield(postId, null, user,statu);
					apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
							appId, "/post/shield",sign);
					log.info("shield post id={}",post.getId());
					body="{\"id\":"+"\""+post.getId()+"\"}";
					message=Constants.API_MESSAGE_SUCCESS;
					status=Constants.API_STATUS_SUCCESS;
					code=ResponseCode.API_CODE_CALL_SUCCESS;
				}
			}else{
				message = errors.getErrors().get(0);
				code=ResponseCode.API_CODE_POST_HAS_NOT_PERM;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 帖子点赞 取消点赞
	 * @param postId 帖子ID
	 * @param operate    0点赞 3取消点赞  默认0
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@RequestMapping(value = "/post/up")
	public void postUp(Integer postId,Integer operate,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, postId);
		if (!errors.hasErrors()) {
			BbsPost post = bbsPostMng.findById(postId);
			int ups = 0;
			if (post!=null) {
				ups = post.getUps();
			}else{
				errors.addError("\"post not exit\"");
				code=ResponseCode.API_CODE_PARAM_REQUIRED;
			}
			if (!errors.hasErrors()) {
				Integer userId = CmsUtils.getUserId(request);
				if(operate.equals(BbsTopicPostOperate.OPT_CANCEL_UP)){
					ups=bbsPostCountMng.postCancelUp(postId);
					topicPostOperateMng.postOperate(postId, userId, operate);
				}else if(operate.equals(BbsTopicPostOperate.OPT_UP)){
					ups=bbsPostCountMng.postUp(postId);
					topicPostOperateMng.postOperate(postId, userId, operate);
				}
				log.info("up post id={}",post.getId());
				body="{\"ups\":"+"\""+ups+"\"}";
				message=Constants.API_MESSAGE_SUCCESS;
				status=Constants.API_STATUS_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 帖子删除
	 * @param forumId 版块ID  必选
	 * @param ids     帖子ID,逗号分隔 必选
	 * @param appId appId 必选 
	 * @param nonce_str 随机字符串  必选
	 * @param sign 签名 必选
	 * @param sessionKey 会话标识必选
	 */
	@SignValidate
	@RequestMapping(value = "/post/delete")
	public void postDelete(Integer forumId,String ids,String sign,String appId,
			HttpServletRequest request, HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		BbsUser user = CmsUtils.getUser(request);
		CmsSite site = CmsUtils.getSite(request);
		errors = ApiValidate.validateRequiredParams(errors, forumId);
		if (!errors.hasErrors()) {
			BbsForum forum = bbsForumMng.findById(forumId);
			if (forum==null) {
				errors.addErrorString("\"forum not exit\"");
				code=ResponseCode.API_CODE_NOT_FOUND;
			}else{
				if(!checkModerators(forum, user, site)){
					errors.addErrorString(" user not moderator");
					code=ResponseCode.API_CODE_NOT_MODERATOR;
				}
			}
			if (!errors.hasErrors()) {
				//签名数据不可重复利用
				ApiRecord record=apiRecordMng.findBySign(sign, appId);
				if(record!=null){
					message=Constants.API_MESSAGE_REQUEST_REPEAT;
					code=ResponseCode.API_CODE_REQUEST_REPEAT;
				}else{
					Integer[]intIds=ArrayUtils.parseStringToArray(ids);
					boolean postNotFound=false;
					for(Integer id:intIds){
						BbsPost c=bbsPostMng.findById(id);
						if(c==null){
							postNotFound=true;
							break;
						}
					}
					if(!postNotFound){
						BbsPost[] deleteByIds = bbsPostMng.deleteByIds(intIds);
						for (int i = 0; i < deleteByIds.length; i++) {
							log.info("delete post id={}",deleteByIds[i].getId());
						}
						apiRecordMng.callApiRecord(RequestUtils.getIpAddr(request),
								appId, "/post/delete",sign);
						message=Constants.API_MESSAGE_SUCCESS;
						status=Constants.API_STATUS_SUCCESS;
						code=ResponseCode.API_CODE_CALL_SUCCESS;
					}else{
						message=Constants.API_MESSAGE_POST_NOT_FOUND;
						code=ResponseCode.API_CODE_NOT_FOUND;
					}
				}
			}else{
				message = errors.getErrors().get(0);
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private boolean checkModerators(BbsForum forum, BbsUser user, CmsSite site) {
		if (forum.getGroupViews() == null) {
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				return false;
			}
			if (!group.hasRight(forum, user)) {
				return false;
			}
		}
		return true;
	}
	
	private WebErrors checkShield(WebErrors errors,BbsForum forum,
			BbsUser user, CmsSite site) {
		if(forum!=null){
			if (forum.getGroupTopics() == null) {
				errors.addErrorString("\"has no permission\"");
				return errors;
			} else {
				BbsUserGroup group = null;
				if (user == null) {
					group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
				} else {
					group = user.getGroup();
				}
				if (group == null) {
					errors.addErrorString("\"has not permission\"");
					return errors;
				}
				if (!group.allowTopic()) {
					errors.addErrorString("\"has not permission\"");
					return errors;
				}
				if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
					errors.addErrorString("\"has not permission\"");
					return errors;
				}
				if (!group.hasRight(forum, user)) {
					errors.addErrorString("\"has not permission\"");
					return errors;
				}
				if (!group.topicShield()) {
					errors.addErrorString("\"has not permission\"");
					return errors;
				}
			}
		}else{
			errors.addErrorString("\"forum not Exist\"");
		}
		return errors;
	}
	
	private WebErrors checkGrade(WebErrors errors,BbsForum forum, 
			BbsUser user, CmsSite site) {
		if(forum!=null){
			if (forum.getGroupReplies() == null) {
				errors.addErrorString("\"has not permission\"");
				return errors;
			} else {
				BbsUserGroup group = null;
				if (user == null) {
					group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
				} else {
					group = user.getGroup();
				}
				if (group == null) {
					errors.addErrorString("\"has not permission\"");
					return errors;
				}
				if (forum.getGroupReplies().indexOf("," + group.getId() + ",") == -1) {
					errors.addErrorString("\"has not permission\"");
					return errors;
				}
				if (user.getGradeToday() != null
						&& user.getGradeToday() >= group.getGradeNum()) {
					errors.addErrorString("\"does no mark\"");
					return errors;
				}
			}
		}else{
			errors.addErrorString("\"forum not exit\"");
			return errors;
		}
		return errors;
	}
	
	
	private WebErrors checkEdit(WebErrors errors,BbsForum forum,
			BbsPost post, BbsUser user,String ip) {
		if (post==null) {
			errors.addErrorString("\"post not exit\"");
			return errors;
		}
		if(forum!=null){
			if (forum.getGroupReplies() == null) {
				errors.addErrorString("\"has not permission\"");
				return errors;
			} else {
				if (user == null) {
					errors.addErrorString("\"has not permission\"");
					return errors;
				}
				BbsUserGroup group = user.getGroup();
				//if (!post.getCreater().equals(user)) {
				//	return "不能编辑别人的帖子";
				//}
				if (forum.getGroupReplies().indexOf("," + group.getId() + ",") == -1) {
					errors.addErrorString("\"has not permission\"");
					return errors;
				}
			}
		}else{
			errors.addErrorString("\"forum not exit\"");
			return errors;
		}
		boolean ipLimit=bbsLimitMng.ipIsLimit(ip);
		boolean userLimit=bbsLimitMng.userIsLimit(user.getId());
		if(ipLimit){
			errors.addErrorString("\"ip forbidden\"");
			return errors;
		}
		if(userLimit){
			errors.addErrorString("\"user forbidden\"");
			return errors;
		}
		return errors;
	}
	
	private WebErrors checkReply(WebErrors errors,BbsForum forum,
			BbsTopic topic,BbsUser user,String ip,CmsSite site) {
		if (topic==null) {
			errors.addErrorString("\"topic not exit\"");
			return errors;
		}
		if(!topic.getAllayReply()){
			errors.addErrorString("\"magic open error\"");
			return errors;
		}else{
			if(forum!=null){
				if (forum.getGroupReplies() == null) {
					errors.addErrorString("\"groupAccessForbidden\"");
					return errors;
				} else {
					BbsUserGroup group = user.getGroup();
					if (user.getProhibit()) {
						errors.addErrorString("\"member gag\"");
						return errors;
					}
					if (group == null) {
						errors.addErrorString("\"has notpermission\"");
						return errors;
					}
					if (!group.allowReply()) {
						errors.addErrorString("\"has not permission\"");
						return errors;
					}
					if (forum.getGroupReplies().indexOf("," + group.getId() + ",") == -1) {
						errors.addErrorString("\"has not permission\"");
						return errors;
					}
					if (!group.checkPostToday(user.getPostToday())) {
						errors.addErrorString("\"post to many\"");
						return errors;
					}
				}
			}else{
				errors.addErrorString("\"forum not exit\"");
				return errors;
			}
		}
		boolean ipLimit=bbsLimitMng.ipIsLimit(ip);
		boolean userLimit=bbsLimitMng.userIsLimit(user.getId());
		if(ipLimit){
			errors.addErrorString("\"ip forbidden\"");
			return errors;
		}
		if(userLimit){
			errors.addErrorString("\"user forbidden\"");
			return errors;
		}
		return errors;
	}
	
	@Autowired
	private BbsPostMng bbsPostMng;
	@Autowired
	private BbsTopicMng bbsTopicMng;
	@Autowired
	private BbsGradeMng bbsGradeMng;
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
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private BbsPostCountMng bbsPostCountMng;
	@Autowired
	private BbsTopicPostOperateMng topicPostOperateMng;
}
