package com.jeecms.bbs.api.member;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.ApiValidate;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsTopicType;
import com.jeecms.bbs.entity.BbsTopicTypeSubscribe;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsTopicTypeMng;
import com.jeecms.bbs.manager.BbsTopicTypeSubscribeMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

@Controller
public class BbsTopicTypeApiAct {
	/**
	 * 我关注的主题分类列表
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @param response
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/topicType/my_topicType")
	public void myTopicType(Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) throws JSONException{
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		BbsUser user = CmsUtils.getUser(request);
		Pagination page = topicTypeSubscribeMng.getPage(user.getId(), pageNo, pageSize);
		List<BbsTopicTypeSubscribe> list = (List<BbsTopicTypeSubscribe>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray = new JSONArray();
		if (list!=null&&list.size()>0) {
			for(int i = 0 ; i < list.size() ; i++){
				jsonArray.put(i,list.get(i).convertToJson());
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
	 * 订阅、取消分类
	 * @param typeId 分类编号
	 * @param operate 1订阅 0取消订阅
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/topicType/subscribe")
	public void topicTypeSubscribe(Integer typeId,Integer operate
			,HttpServletRequest request,HttpServletResponse response){
		String message = Constants.API_MESSAGE_PARAM_REQUIRED;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_PARAM_REQUIRED;
		String body = "\"\"";
		BbsUser user = CmsUtils.getUser(request);
		WebErrors errors = WebErrors.create(request);
		errors = ApiValidate.validateRequiredParams(errors, typeId,operate);
		if (!errors.hasErrors()) {
			BbsTopicType type = bbsTopicTypeMng.findById(typeId);
			if (type!=null) {
				topicTypeSubscribeMng.subscribe(typeId, user.getId(), operate);
				message = Constants.API_MESSAGE_SUCCESS;
				status = Constants.API_STATUS_SUCCESS;
				code = ResponseCode.API_CODE_CALL_SUCCESS;
			}else{
				message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
				code = ResponseCode.API_CODE_NOT_FOUND;
			}
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private BbsTopicTypeMng bbsTopicTypeMng;
	@Autowired
	private BbsTopicTypeSubscribeMng topicTypeSubscribeMng;
}
