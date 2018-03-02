package com.jeecms.bbs.api.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsMagicConfig;
import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.manager.BbsMagicConfigMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class BbsMagicApiAct {
	
	/**
	 * 道具列表
	 */
	@RequestMapping(value = "/magic/list")
	public void magicList(
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		Integer siteId=CmsUtils.getSiteId(request);
		JSONArray jsonArray=new JSONArray();
		List<BbsCommonMagic>list = null;
		BbsMagicConfig magicConfig = magicConfigMng.findById(siteId);
		if (magicConfig.getMagicSwitch()) {
			list = magicMng.getList();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					jsonArray.put(i, list.get(i).convertToJson());
				}
			}
			body=jsonArray.toString();
			status=Constants.API_STATUS_SUCCESS;
			message=Constants.API_MESSAGE_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_MAGIC_CLOSE;
			code=ResponseCode.API_CODE_MAGIC_CLOSE;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private BbsCommonMagicMng magicMng;
	@Autowired
	private BbsMagicConfigMng magicConfigMng;
}

