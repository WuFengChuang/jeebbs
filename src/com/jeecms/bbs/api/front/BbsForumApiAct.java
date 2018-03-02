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

import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class BbsForumApiAct {
	
	/**
	 * 分区版块列表API
	 * @param https  url返回格式    1 http格式   0 https格式
	 * @param categoryId  分区ID
	 */
	@RequestMapping(value = "/forum/list")
	public void forumList(Integer https,Integer categoryId,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		Integer siteId=CmsUtils.getSiteId(request);
		if(https==null){
			https=Constants.URL_HTTP;
		}
		List<BbsForum> list = forumManager.getList(siteId, categoryId,null);
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
	 * 获取版块信息
	 * @param https  url返回格式    1 http格式   0 https格式
	 * @param id 版块id
	 */
	@RequestMapping(value = "/forum/get")
	public void forumGet(Integer id,Integer https,
			HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsForum forum = null;
		if(https==null){
			https=Constants.URL_HTTP;
		}
		if (id != null) {
			if (id.equals(0)) {
				forum = new BbsForum();
			}else{
				forum = forumManager.findById(id);
			}
			if (forum != null) {
				JSONObject json=forum.convertToJson(https);
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
	@Autowired
	private BbsForumMng forumManager;
}

