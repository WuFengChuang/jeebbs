package com.jeecms.bbs.api.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsMagicConfig;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsMagicConfigMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.SignValidate;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsCommonMagicApiAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsCommonMagicApiAct.class);
	@Autowired
	private BbsCommonMagicMng manager;
	@Autowired
	private BbsMagicConfigMng magicConfigMng;
	@Autowired
	private BbsUserMng userMng;
	
	/**
	 * 道具列表
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/magic/list")
	public void magicList(HttpServletRequest request,HttpServletResponse response) throws JSONException{
		List<BbsCommonMagic> list = manager.getList();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i < list.size(); i++){
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
	 * 道具详情
	 * @param id
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/magic/get")
	public void magicGet(Integer id,
			HttpServletRequest request , HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsCommonMagic magic = null;
		if (id!=null) {
			if (id.equals(0)) {
				magic = new BbsCommonMagic();
			}else{
				magic = manager.findById(id);
			}
			if (magic != null) {
				JSONObject json = magic.convertToJson();
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
				body = json.toString();
			}else{
				code = ResponseCode.API_CODE_NOT_FOUND;
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 查看配置
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/magic/config_get")
	public void configGet(HttpServletRequest request , HttpServletResponse response) throws JSONException{
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsMagicConfig config = magicConfigMng.findById(CmsUtils.getSiteId(request));
		if (config!=null) {
			JSONObject json = config.convertToJson();
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = json.toString();
		}else{
			code = ResponseCode.API_CODE_NOT_FOUND;
			message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
		}
	ApiResponse apiResponse = new ApiResponse(body, message, status, code);
	ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 修改配置
	 * @param request
	 * @param response
	 * @param bean
	 */
	@SignValidate
	@RequestMapping(value = "/magic/config_update")
	public void configUpdate(HttpServletRequest request,HttpServletResponse response,BbsMagicConfig bean){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		bean.setId(CmsUtils.getSiteId(request));
		errors = ApiValidate.validateRequiredParams(errors, 
				bean.getMagicDiscount(),bean.getMagicSwitch(),bean.getMagicSofaLines());
		if (!errors.hasErrors()) {
			bean = magicConfigMng.update(bean);
			log.info("update BbsMagicConfig id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 报错排列顺序
	 * @param ids 道具编号(多个编号用逗号隔开)
	 * @param prioritys 对应道具的排列顺序
	 * @param magicAvails 对应道具的是否可用标识
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/magic/o_priority")
	public void batchUpdate(String ids,String prioritys,String magicAvails,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				ids,prioritys,magicAvails);
		if (!errors.hasErrors()) {
			BbsCommonMagic magic;
			Integer[] idArray = StrUtils.getInts(ids);
			Integer[] availArray = getInt(magicAvails);
			Byte[] priorityArray = getBytes(prioritys);
			Integer id;
			Byte priority;
			Integer magicAvailable;
			int count = 0 ;
			for (int i = 0; i < idArray.length; i++) {
				id = idArray[i];
				priority = priorityArray[i];
				magicAvailable = availArray[i];
				if (id != null && priority != null) {
					magic = manager.findById(id);
					if (magic != null) {
						magic.setDisplayorder(priority);
						if (magicAvailable.equals(1)) {
							magic.setAvailable(true);
						} else {
							magic.setAvailable(false);
						}
						magic = manager.update(magic);
						log.info("update BbsCommonMagic id={}",magic.getId());
						count++;
					}
				}
			}
			if (count==idArray.length) {
				status = Constants.API_STATUS_SUCCESS;
				message = Constants.API_MESSAGE_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 道具修改
	 * @param bean 道具对象
	 * @param groupIds 允许使用的用户组
	 * @param beUsedGroupIds 允许被使用的用户组
	 * @param request
	 * @param response
	 */
	@SignValidate
	@RequestMapping(value = "/magic/update")
	public void update(BbsCommonMagic bean,String groupIds,String beUsedGroupIds,
			HttpServletRequest request,HttpServletResponse response){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, 
				bean.getId(),bean.getName(),bean.getPrice(),bean.getNum(),
				bean.getDescription());
		if (!errors.hasErrors()) {
			Integer[] groupIdArray = StrUtils.getInts(groupIds);
			Integer[] beUserdArray = StrUtils.getInts(beUsedGroupIds);
			bean = manager.updateByGroup(bean, groupIdArray, beUserdArray);
			log.info("update BbsCommonMagic id={}",bean.getId());
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = "{\"id\":"+"\""+bean.getId()+"\"}";
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	/**
	 * 赠送道具
	 * @param userIds
	 * @param ids
	 * @param nums
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/magic/give")
	public void getMagic(String userIds,String ids,String nums
			,HttpServletResponse response,HttpServletRequest request){
		String body="\"\"";
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors,
				userIds,ids,nums);
		if (!errors.hasErrors()) {
			BbsUser targetUser = null;
			BbsCommonMagic magic = null;
			Integer id = null;
			Integer numTemp = null;
			Integer[] userIdArray = StrUtils.getInts(userIds);
			Integer[] idArray = StrUtils.getInts(ids);
			Integer[] numArray = StrUtils.getInts(nums);
			for (int i = 0; i < userIdArray.length; i++) {
				targetUser = userMng.findById(userIdArray[i]);
				for (int j = 0; j < idArray.length; j++) {
					id = idArray[j];
					numTemp = numArray[j];
					magic = manager.findById(id);
					userMng.updatePoint(userIdArray[i], null, null, magic.getIdentifier(), numTemp, 4);
					log.info("update BbsUser id={}",targetUser.getId());
				}
			}
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	private Integer[] getInt(String magicAvails) {
		String[] split = magicAvails.split(",");
		Integer[] integers = new Integer[split.length];
		if (split!=null&&split.length>0) {
			for(int i = 0 ; i <split.length ; i++){
				if (Boolean.parseBoolean(split[i])) {
					integers[i] = 1;
				}else{
					integers[i]=0;
				}
			}
		}
		return integers;
	}
	
	private Byte[] getBytes(String str){
		String[] split = str.split(",");
		Byte[] bytes = new Byte[split.length];
		for (int i = 0; i < split.length; i++) {
			bytes[i] = Byte.parseByte(split[i]);
		}
		return bytes;
	}
}
