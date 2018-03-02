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

import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class BbsPostApiAct {
	/**
	 * 帖子列表API
	 * @param https  url返回格式    1 http格式   0 https格式
	 * @param topicId  主题ID
	 * @param parentId   父帖ID
	 * @param createUserId   创建者用户ID
	 * @param first   查询开始下标
	 * @param count	  查询数量
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/post/list")
	public void postList(Integer https,Integer topicId,
			Integer parentId,Integer createUserId,
			Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		if(pageNo==null){
			pageNo=1;
		}
		if(pageSize==null){
			pageSize=10;
		}
		if(https==null){
			https=Constants.URL_HTTP;
		}
		Pagination page = bbsPostMng.getPostPageByTopic(topicId, parentId, createUserId, true, pageNo, pageSize);
		List<BbsPost> list = (List<BbsPost>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson(https));
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
	 * 获取帖子信息
	 * @param https  url返回格式    1 http格式   0 https格式
	 * @param id 帖子id
	 */
	@RequestMapping(value = "/post/get")
	public void postGet(Integer id,Integer https,
			HttpServletRequest request,HttpServletResponse response)
					throws JSONException {
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsPost post = null;
		if(https==null){
			https=Constants.URL_HTTP;
		}
		if (id != null) {
			if (id.equals(0)) {
				post=new BbsPost();
			}else{
				post = bbsPostMng.findById(id);
			}
			if (post != null) {
				JSONObject json=post.convertToJson(https);
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
	private BbsPostMng bbsPostMng;
}

