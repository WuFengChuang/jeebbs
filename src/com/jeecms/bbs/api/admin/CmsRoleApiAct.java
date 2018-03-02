package com.jeecms.bbs.api.admin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsRole;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsRoleMng;
import com.jeecms.core.web.WebErrors;

@Controller
public class CmsRoleApiAct {
	private static final Logger log = LoggerFactory.getLogger(CmsRoleApiAct.class);
	
	@Autowired
	private CmsRoleMng manager;
	
	/**
	 * 角色管理列表
	 * @param level 角色等级
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/role/list")
	public void roleList(Integer level,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		List<CmsRole> list = manager.getList(level);
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				jsonArray.put(i,list.get(i).convertToJson());
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 角色详情
	 * @param id 角色编号
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/role/get")
	public void roleGet(Integer id,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		CmsRole role = null;
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		if (id!=null) {
			if (id.equals(0)) {
				role = new CmsRole();
				role.init();
			}else{
				role = manager.findById(id);
			}
			if (role!=null) {
				JSONObject json = role.convertToJson();
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = json.toString();
			}else{
				code = ResponseCode.API_CODE_APP_PARAM_ERROR;
				message = Constants.API_MESSAGE_PARAM_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 角色添加
	 * @param bean 角色对象
	 * @param perms 角色功能权限
	 * @param request
	 * @param response
	 * @param model
	 */
	@SignValidate
	@RequestMapping(value = "/role/save")
	public void save(CmsRole bean,String perms,
			HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		CmsSite site = CmsUtils.getSite(request);
		//验证公共非空参数
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors,
				bean.getName(),bean.getPriority(),bean.getLevel());
		if (!errors.hasErrors()) {
			bean.setSite(site);
			bean = manager.save(bean, splitPerms(perms.split(",")));
			log.info("save CmsRole id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 角色更新
	 * @param bean 角色对象
	 * @param perms 角色功能权限
	 * @param response
	 * @param request
	 * @param model
	 */
	@SignValidate
	@RequestMapping(value = "/role/update")
	public void update(CmsRole bean,String perms,
			HttpServletResponse response,HttpServletRequest request,ModelMap model){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		bean.init();
		//验证公共非空参数
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors,
				bean.getName(),bean.getPriority(),bean.getLevel());
		if (!errors.hasErrors()) {
			//将功能权限字符串转成数组，再转成set集合
			bean = manager.update(bean, splitPerms(perms.split(",")));
			log.info("update CmsRole id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 角色删除
	 * @param ids 角色编号字符串（多个编号用逗号隔开）
	 * @param request
	 * @param response
	 * @param model
	 */
	@SignValidate
	@RequestMapping(value = "/role/delete")
	public void delete(String ids,
			HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		//验证公共非空参数
		errors = ApiValidate.validateRequiredParams(errors, ids);
		if (!errors.hasErrors()) {
			try {
				Integer[] idArray = StrUtils.getInts(ids);
				CmsRole[] deleteByIds = manager.deleteByIds(idArray);
				for (int i = 0; i < deleteByIds.length; i++) {
					log.info("delete BbsUserGroup id={}",deleteByIds[i]);
				}
				status=Constants.API_STATUS_SUCCESS;
				message=Constants.API_MESSAGE_SUCCESS;
				code=ResponseCode.API_CODE_CALL_SUCCESS;
			} catch (Exception e) {
				status = Constants.API_MESSAGE_DELETE_ERROR;
				message = Constants.API_MESSAGE_DELETE_ERROR;
				code = ResponseCode.API_CODE_DELETE_ERROR;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 将角色功能权限数组分割成Set集合
	 * @param perms 角色功能权限数组
	 * @return
	 */
	private Set<String> splitPerms(String[] perms) {
		Set<String> set = new HashSet<String>();
		if (perms != null) {
			for (String perm : perms) {
				for (String p : StringUtils.split(perm, ',')) {
					if (!StringUtils.isBlank(p)) {
						if(p.startsWith("/api/admin")){
							p=p.substring(10);
						}
						set.add(p);
					}
				}
			}
		}
		return set;
	}
}
