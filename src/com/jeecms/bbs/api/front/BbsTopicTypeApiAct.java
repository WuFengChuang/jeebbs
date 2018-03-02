package com.jeecms.bbs.api.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsTopicType;
import com.jeecms.bbs.manager.BbsTopicTypeMng;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class BbsTopicTypeApiAct {
	/**
	 * 主题分类列表
	 * @param parentId 父分类编号
	 * @param orderBy 排列顺序
	 * @param first 
	 * @param count
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@RequestMapping(value = "/topicType/list")
	public void topicTypeList(Integer parentId,Integer orderBy,Integer first,Integer count,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		if (first==null) {
			first=0;
		}
		if (count==null) {
			count=10;
		}
		if (orderBy==null) {
			orderBy=1;
		}
		List<BbsTopicType> list;
		if (parentId!=null) {
			list = bbsTopicTypeMng.getChildList(parentId, true, true, orderBy, first, count);
		}else{
			list = bbsTopicTypeMng.getTopList(true, true, orderBy, first, count);
		}
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i<list.size(); i++){
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
	
	@Autowired
	private BbsTopicTypeMng bbsTopicTypeMng;
}
