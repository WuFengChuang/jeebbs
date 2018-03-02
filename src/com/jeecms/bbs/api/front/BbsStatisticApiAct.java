package com.jeecms.bbs.api.front;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.cache.BbsConfigCache;
import com.jeecms.bbs.cache.BbsConfigEhCache;
import com.jeecms.bbs.manager.BbsSessionMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;

@Controller
public class BbsStatisticApiAct {
	/**
	 * 统计API
	 */
	@RequestMapping(value = "/statistic/get")
	public void statisticGet(HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String message = Constants.API_MESSAGE_OBJECT_NOT_FOUND;
		String status = Constants.API_STATUS_FAIL;
		String code = ResponseCode.API_CODE_NOT_FOUND;
		String body = "\"\"";
		CmsSite site = CmsUtils.getSite(request);
		if (site!=null) {
			JSONObject json = convertToJson(site);
			status = Constants.API_STATUS_SUCCESS;
			message = Constants.API_MESSAGE_SUCCESS;
			code = ResponseCode.API_CODE_CALL_SUCCESS;
			body = json.toString();
		}
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	private JSONObject convertToJson(CmsSite site) throws JSONException {
		JSONObject json=new JSONObject();
		Integer membtertotal=bbsSessionMng.total(true);
		Integer visitortotal=bbsSessionMng.total(false);
		Integer total=visitortotal+membtertotal;
		BbsConfigCache configcache = bbsConfigEhCache.getBbsConfigCache(site
				.getId());
		json.put("postMax", configcache.getPostMax());
		json.put("postToday", configcache.getPostToday());
		json.put("postYestoday", configcache.getPostYestoday());
		json.put("topicTotal", configcache.getTopicTotal());
		json.put("postTotal", configcache.getPostTotal());
		json.put("userTotal", configcache.getUserTotal());
		if(configcache.getLastUser()!=null){
			json.put("lastUser", configcache.getLastUser().getUsername());
		}else{
			json.put("lastUser", "");
		}
		if(configcache.getPostMaxDate()!=null){
			json.put("postMaxDate", DateUtils.parseDateToDateStr(configcache.getPostMaxDate()));	
		}else{
			json.put("postMaxDate","");
		}
		json.put("usertotal", total);
		json.put("membtertotal", membtertotal);
		json.put("visitortotal", visitortotal);
		json.put("userOnlineTopNum", site.getConfig().getUserOnlineTopNum());
		json.put("userOnlineTopDay", site.getConfig().getUserOnlineTopDay());
		return json;
	}
	@Autowired
	private BbsSessionMng bbsSessionMng;
	@Autowired
	private BbsConfigEhCache bbsConfigEhCache;
}

