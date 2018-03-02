package com.jeecms.bbs.api.member;

import static com.jeecms.bbs.entity.BbsFriendShip.APPLYING;
import static com.jeecms.bbs.entity.BbsFriendShip.REFUSE;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsFriendShip;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.manager.BbsFriendShipMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class FriendApiAct {
	
	private final static int OPERATE_ACCEPT=1;
	private final static int OPERATE_REFUSE=2;
	
	/**
	 * 好友列表
	 * @param appId      appid 必选
	 * @param sessionKey 用户会话  必选
	 * @param first   查询开始下标
	 * @param count	  查询数量
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/friend/list")
	public void myFrindList(
			Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		if(pageNo==null){
			pageNo=1;
		}
		if(pageSize==null){
			pageSize=10;
		}
		BbsUser user = CmsUtils.getUser(request);
		JSONArray jsonArray=new JSONArray();
		Pagination page = bbsFriendShipMng.getPageByUserId(user.getId(), pageNo, pageSize);
		List<BbsFriendShip> list = (List<BbsFriendShip>) page.getList();
		int totalCount = page.getTotalCount();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson());
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
	 * 好友申请列表
	 * @param appId      appid 必选
	 * @param sessionKey 用户会话  必选
	 * @param first   查询开始下标
	 * @param count	  查询数量
	 */
	@RequestMapping(value = "/friend/applyList")
	public void myFriendApplyList(
			Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if(first==null){
			first=0;
		}
		if(count==null){
			count=10;
		}
		BbsUser user=CmsUtils.getUser(request);
		if(user!=null){
			JSONArray jsonArray=new JSONArray();
			List<BbsFriendShip>list = null;
			list=bbsFriendShipMng.getApplyList(user.getId(), first, count);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					jsonArray.put(i, list.get(i).convertToJson());
				}
			}
			body=jsonArray.toString();
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 好友申请接受
	 * @param id   申请ID必选
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 */
	@RequestMapping(value = "/friend/accept")
	public void friendAccept(Integer id,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		friendApplyProcess(OPERATE_ACCEPT, id, request, response);
	}
	
	/**
	 * 好友申请拒绝
	 * @param id   申请ID必选
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 */
	@RequestMapping(value = "/friend/refuse")
	public void friendRefuse(Integer id,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		friendApplyProcess(OPERATE_REFUSE, id,request, response);
	}
	
	/**
	 * 申请加好友
	 * @param username   用户名 必选
	 * @param appId      appid  必选
	 * @param sessionKey 用户会话  必选
	 */
	@RequestMapping(value = "/friend/apply")
	public void friendApply(String username,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors=WebErrors.create(request);
		BbsUser friend =null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, username);
		if(!errors.hasErrors()){
			friend = bbsUserMng.findByUsername(username);
			errors=ApiValidate.validateRequiredParams(errors,friend);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_USER_NOT_FOUND;
			}
		}else{
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}
		//会话用户
		if(user!=null){
			if(errors.hasErrors()){
				message=errors.getErrors().get(0);
			}else{
				if (validateApply(user, friend)) {
					bbsFriendShipMng.apply(user, friend);
				}
				message=Constants.API_STATUS_SUCCESS;
				status=Constants.API_STATUS_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private void friendApplyProcess(Integer operate,Integer id,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors=WebErrors.create(request);
		BbsFriendShip friendShip =null;
		//验证公共非空参数
		errors=ApiValidate.validateRequiredParams(errors, id);
		if(!errors.hasErrors()){
			friendShip= bbsFriendShipMng.findById(id);
			errors=ApiValidate.validateRequiredParams(errors,friendShip);
			if(errors.hasErrors()){
				code=ResponseCode.API_CODE_USER_NOT_FOUND;
			}
		}else{
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}
		//会话用户
		if(user!=null){
			if(errors.hasErrors()){
				message=errors.getErrors().get(0);
			}else{
				if (validateProcessApply(user, friendShip)) {
					if(operate.equals(OPERATE_ACCEPT)){
						bbsFriendShipMng.accept(friendShip);
					}else if(operate.equals(OPERATE_REFUSE)){
						bbsFriendShipMng.refuse(friendShip);
					}
				}
				message=Constants.API_STATUS_SUCCESS;
				status=Constants.API_STATUS_SUCCESS;
			}
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	private boolean validateApply(BbsUser user, BbsUser friend) {
		// 用户未登录
		if (user == null) {
			return false;
		}
		// 好友不存在
		if (friend == null) {
			return false;
		}
		// 不允许加自己为好友
		if (user.equals(friend)) {
			return false;
		}
		// 申请被拒绝则可以重新申请好友
		BbsFriendShip bean = bbsFriendShipMng.getFriendShip(user.getId(),
				friend.getId());
		if (bean != null && bean.getStatus() != REFUSE) {
			return false;
		}
		return true;
	}
	
	private boolean validateProcessApply(BbsUser user, BbsFriendShip friendShip) {
		// 用户未登录
		if (user == null) {
			return false;
		}
		// 好友申请不存在
		if (friendShip == null) {
			return false;
		}
		// 只处理申请中的好友关系
		if (friendShip.getStatus() != APPLYING) {
			return false;
		}
		// 只处理自己的好友关系
		if (!user.equals(friendShip.getFriend())) {
			return false;
		}
		return true;
	}
	
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsFriendShipMng bbsFriendShipMng;
}

