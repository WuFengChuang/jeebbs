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

import com.jeecms.bbs.manager.BbsCategoryMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsCategory;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class CategoryApiAct {
	
	/**
	 * 分区列表API
	 * @param first   查询开始下标
	 * @param count	  查询数量
	 */
	@RequestMapping(value = "/category/list")
	public void categoryList(HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		Integer siteId=CmsUtils.getSiteId(request);
		List<BbsCategory> list = bbsCategoryMng.getList(siteId);
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
	
	/**
	 * 获取分区信息
	 * @param id 分区id 必选
	 */
	@RequestMapping(value = "/category/get")
	public void categoryGet(Integer id,
			HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsCategory category = null;
		if (id != null) {
			if (id.equals(0)) {
				category = new BbsCategory();
			}else{
				category = bbsCategoryMng.findById(id);
			}
			if (category != null) {
				JSONObject json=category.convertToJson();
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
	@Autowired
	private BbsCategoryMng bbsCategoryMng;
}

