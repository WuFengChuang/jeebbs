package com.jeecms.bbs.api.admin;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
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
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserExt;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsAdminApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsAdminApiAct.class);
	@Autowired
	protected BbsUserMng manager;
	
	/**
	 * 管理员列表
	 * @param https
	 * @param queryUsername 按用户名查询
	 * @param queryEmail 按邮箱查询
	 * @param queryDisabled 按禁用标识查询
	 * @param pageNo 第pageNo页
	 * @param pageSize 每页总数
	 * @param request
	 * @param response
	 * @param model
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/admin/list")
	public void adminList(Integer https,String queryUsername,String queryEmail,
			Boolean queryDisabled,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response,ModelMap model) throws JSONException{
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if(https==null){
			https=Constants.URL_HTTP;
		}
		Pagination page = manager.getPage(queryUsername, queryEmail, null, queryDisabled, true, null, null, user.getRank(), null, pageNo, pageSize);
		List<BbsUser> list = (List<BbsUser>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list != null && list.size()>0) {
			for (int i = 0; i < list.size() ; i++) {
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
	 * 管理员添加
	 * @param bean 
	 * @param ext
	 * @param username 用户名
	 * @param email 电子邮箱
	 * @param password 密码
	 * @param rank 等级
	 * @param groupId 会员组编号
	 * @param roleIds 角色id字符串（多个id用逗号隔开）
	 * @param request 
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/admin/save")
	public void save(BbsUser bean , BbsUserExt ext , String username,String email,
			String password,Integer rank,Integer groupId,String roleIds,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String ip = RequestUtils.getIpAddr(request);
		bean.init();
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				username,password,rank,groupId);
		if (!errors.hasErrors()) {
			Integer[] roleArray = StrUtils.getInts(roleIds);
			try {
				//添加的管理员,等级不能超过当前用户等级
				if (rank>=user.getRank()) {
					message = Constants.API_MESSAGE_USER_NOT_HAS_PERM;
					code = ResponseCode.API_CODE_USER_NOT_HAS_PERM;
				}else{
					bean =manager.saveAdmin(username, email, password, ip, rank, groupId, roleArray, ext);
					log.info("save BbsUser id={}",bean.getId());
					status = Constants.API_STATUS_SUCCESS;
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
					body = "{\"id\":"+"\""+bean.getId()+"\"}";
				}
			} catch (Exception e)  {
				e.printStackTrace();
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 管理员更新
	 * @param bean
	 * @param ext
	 * @param email 电子邮箱
	 * @param password 密码
	 * @param rank 等级
	 * @param disabled 禁用标识
	 * @param groupId 会员组编号
	 * @param roleIds 角色id字符串（多个id用逗号隔开）
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/admin/update")
	public void update(BbsUser bean , BbsUserExt ext ,String email,
			String password,Integer rank,Boolean disabled,Integer groupId,String roleIds,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors,bean.getId(), 
				rank,disabled,groupId);
		if (!errors.hasErrors()) {
			Integer[] roleArray = StrUtils.getInts(roleIds);
			try {
				//修改的管理员,等级不能超过当前用户等级
				if (rank>user.getRank()) {
					message = Constants.API_MESSAGE_USER_NOT_HAS_PERM;
					code = ResponseCode.API_CODE_USER_NOT_HAS_PERM;
				}else{
					bean =manager.updateAdmin(bean, ext, password, groupId, roleArray);
					log.info("save BbsUser id={}",bean.getId());
					status = Constants.API_STATUS_SUCCESS;
					message = Constants.API_MESSAGE_SUCCESS;
					code = ResponseCode.API_CODE_CALL_SUCCESS;
					body = "{\"id\":"+"\""+bean.getId()+"\"}";
				}
			} catch (Exception e)  {
				e.printStackTrace();
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
}
