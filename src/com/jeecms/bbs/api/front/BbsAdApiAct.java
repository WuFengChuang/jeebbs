package com.jeecms.bbs.api.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsAdvertising;
import com.jeecms.bbs.entity.BbsAdvertisingSpace;
import com.jeecms.bbs.manager.BbsAdvertisingMng;
import com.jeecms.bbs.manager.BbsAdvertisingSpaceMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class BbsAdApiAct {
	
	/**广告列表
	 * @param adspaceId 版位ID 必选
	 * @param https      地址返回格式  非必选   0https 1http 默认1
	 */
	@RequestMapping(value = "/ad/list")
	public void adList(Integer https,Integer adspaceId,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		if(https==null){
			https=Constants.URL_HTTP;
		}
		List<BbsAdvertising> list = advertisingMng.getList(adspaceId, true,null,100);
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson(https));
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
	 * 获取广告信息
	 * @param id 广告ID 必选
	 * @param https      地址返回格式  非必选   0https 1http 默认1
	 */
	@RequestMapping(value = "/ad/get")
	public void adGet(Integer https,Integer id,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		if(https==null){
			https=Constants.URL_HTTP;
		}
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsAdvertising ad = null;
		if (id!=null) {
			if (id.equals(0)) {
				ad = new BbsAdvertising();
			}else{
				ad = advertisingMng.findById(id);
			}
			if(ad!=null){
				JSONObject json = ad.convertToJson(https);
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
	 * 广告版块
	 */
	@RequestMapping(value = "/adctg/list")
	public void adCtgList(HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		Integer siteId=CmsUtils.getSiteId(request);
		List<BbsAdvertisingSpace> list = advertisingSpaceMng.getList(siteId);
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson());
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private BbsAdvertisingSpaceMng advertisingSpaceMng;
	@Autowired
	private BbsAdvertisingMng advertisingMng;
}

