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
import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.bbs.entity.BbsTopicCharge;
import com.jeecms.bbs.manager.BbsOrderMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class BbsOrderApiAct {
	
	/**
	 * 主题打赏购买记录
	 * @param topicId  主题ID 必选 
	 * @param type 1购买记录  2打赏记录 非必选 默认2
	 * @param pageNo 非必选 默认0
	 * @param pageSize 非必选 默认10
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/order/getByTopic")
	public void getOrderListByTopic(
			Integer topicId,Short type,Integer pageNo,Integer pageSize,
			HttpServletRequest request,HttpServletResponse response) 
					throws JSONException {
		String body="\"\"";
		String message="\"\"";
		String status=Constants.API_STATUS_FAIL;
		String code=ResponseCode.API_CODE_CALL_SUCCESS;
		if(type==null){
			type=BbsTopicCharge.MODEL_REWARD;
		}
		if(pageNo==null){
			pageNo=1;
		}
		if(pageSize==null){
			pageSize=10;
		}
		List<BbsOrder>list;
		if(topicId!=null){
			Pagination page = bbsOrderMng.getPageByTopic(topicId, type, pageNo, pageSize);
			list = (List<BbsOrder>) page.getList();
			int totalCount = page.getTotalCount();
			JSONArray jsonArray=new JSONArray();
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					jsonArray.put(i, list.get(i).convertToJson());
				}
			}
			body=jsonArray.toString()+",\"totalCount\":"+totalCount;
			message=Constants.API_MESSAGE_SUCCESS;
			status=Constants.API_STATUS_SUCCESS;
		}else{
			message=Constants.API_MESSAGE_PARAM_ERROR;
			code=ResponseCode.API_CODE_PARAM_REQUIRED;
		}
		ApiResponse apiResponse=new ApiResponse(body, message, status,code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@Autowired
	private BbsOrderMng bbsOrderMng;
}

