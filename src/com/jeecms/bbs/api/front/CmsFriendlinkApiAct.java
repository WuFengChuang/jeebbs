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
import com.jeecms.bbs.entity.CmsFriendlink;
import com.jeecms.bbs.entity.CmsFriendlinkCtg;
import com.jeecms.bbs.manager.CmsFriendlinkCtgMng;
import com.jeecms.bbs.manager.CmsFriendlinkMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class CmsFriendlinkApiAct {
	
	/**
	 * 友情链接列表
	 * @param https  url返回格式    1 http格式   0 https格式
	 * @param ctgId 分类ID  非必选
	 * @param first 开始
	 * @param count 数量
	 */
	@RequestMapping(value = "/friendlink/list")
	public void friendlinkList(Integer https,
			Integer ctgId,HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		Integer siteId=CmsUtils.getSiteId(request);
		if(https==null){
			https=Constants.URL_HTTP;
		}
		List<CmsFriendlink> list = cmsFriendlinkMng.getList(siteId, ctgId,true,null,null);
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
	 * 友情链接分类API
	 * @param siteId 站点ID 非必选
	 */
	@RequestMapping(value = "/friendlinkctg/list")
	public void friendlinkCtgList(HttpServletRequest request,
			HttpServletResponse response)throws JSONException {
		Integer siteId=CmsUtils.getSiteId(request);
		List<CmsFriendlinkCtg> list = cmsFriendlinkCtgMng.getList(siteId);
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
	private CmsFriendlinkCtgMng cmsFriendlinkCtgMng;
	@Autowired
	private CmsFriendlinkMng cmsFriendlinkMng;
}

