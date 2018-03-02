package com.jeecms.bbs.api.front;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;

@Controller
public class FriendApiAct {
	
	
	/**
	 * 搜索用户
	 * @param username 用户名
	 * @param https 用户头像返回地址格式    1 http格式   0 https格式
	 * @param pageNo
	 * @param pageSize
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/friend/search")
	public void searchUserList(
			String username,Integer https,
			Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		CmsSite site=CmsUtils.getSite(request);
		if(pageNo==null){
			pageNo=1;
		}
		if(pageSize==null){
			pageSize=10;
		}
		if(https==null){
			https=Constants.URL_HTTP;
		}
		Pagination page = bbsUserMng.getPage(username, null, null, false, false, false, null, null, null, pageNo, pageSize);
		List<BbsUser> list = (List<BbsUser>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson(site,https));
			}
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString()+",\"totalCount\":"+totalCount;
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	
	@Autowired
	private BbsUserMng bbsUserMng;
}

