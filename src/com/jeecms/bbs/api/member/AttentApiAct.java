package com.jeecms.bbs.api.member;

import java.util.ArrayList;
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
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicPostOperate;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsTopicPostOperateMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

@Controller
public class AttentApiAct {
	private static final Logger log = LoggerFactory.getLogger(AccountApiAct.class);
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsTopicPostOperateMng topicPostOperateMng;
	@Autowired
	private BbsTopicMng bbsTopicMng;
	
	/**
	 * 关注列表
	 * @param attent     1关注我的   0我关注的 默认1
	 * @param appId      appid 必选
	 * @param sessionKey 用户会话  必选
	 * @param https      头像地址返回格式  非必选   0https 1http 默认1
	 * @param first   查询开始下标
	 * @param count	  查询数量
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/attent/list")
	public void attentList(Integer attent,Integer https,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		CmsSite site = CmsUtils.getSite(request);
		if (attent==null) {
			attent = 1;
		}
		if (https==null) {
			https=Constants.URL_HTTP;
		}
		Pagination page = bbsUserMng.getPageByAttent(attent, CmsUtils.getUserId(request), pageNo, pageSize);
		List<BbsUser> list = (List<BbsUser>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson(site, https));
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
	 * 关注收藏主题列表
	 * @param type    1收藏   2关注
	 * @param appId      appid 必选
	 * @param sessionKey 用户会话  必选
	 * @param https      地址返回格式  非必选   0https 1http 默认1
	 * @param first   查询开始下标
	 * @param count	  查询数量
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/attent/topicList")
	public void myTopicList(Integer https,Integer type,Integer pageNo,Integer pageSize
			,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		if (https==null) {
			https= Constants.URL_HTTP;
		}
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		if (type==null) {
			type = BbsTopicPostOperate.OPT_ATTENT;
		}
		JSONArray jsonArray = new JSONArray();
		Pagination page = topicPostOperateMng.getPage(null, CmsUtils.getUserId(request), type, pageNo, pageSize);
		List<BbsTopicPostOperate> operateList = (List<BbsTopicPostOperate>) page.getList();
		int totalCount = page.getTotalCount();
		List<BbsTopic> list = new ArrayList<BbsTopic>();
		for (int i = 0; i < operateList.size(); i++) {
			list.add(bbsTopicMng.findById(operateList.get(i).getDataId()));
		}
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson(https, false, false, false, null, null));
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
	 * 关注好友
	 * @param userId   用户ID必选
	 * @param operate   操作参数 0添加关注 1移除关注
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 */
	@RequestMapping(value = "/attent/user")
	public void attentUser(Integer userId,Integer operate,HttpServletRequest request,HttpServletResponse response){
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		WebErrors errors= WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, userId,operate);
		BbsUser attentUser = null;
		if (!errors.hasErrors()) {
			attentUser = bbsUserMng.findById(userId);
			BbsUser user = CmsUtils.getUser(request);
			if (validateAttent(user, attentUser)) {
				bbsUserMng.attentUser(user.getId(), userId, operate);
				log.info("attent BbsUser id={}",user.getId());
			}
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private boolean validateAttent(BbsUser user, BbsUser attentUser) {
		// 用户未登录
		if (user == null) {
			return false;
		}
		// 被关注者不存在
		if (attentUser == null) {
			return false;
		}
		// 不允许关注自己
		if (user.equals(attentUser)) {
			return false;
		}
		return true;
	}
}
