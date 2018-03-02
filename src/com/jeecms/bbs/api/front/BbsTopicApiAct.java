package com.jeecms.bbs.api.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class BbsTopicApiAct {
	
	/**
	 * 主题列表API
	 * 可实现今日热门话题    最新主题  精华主题  置顶主题  版块下主题  搜索主题 我的主题
	 * @param https  url返回格式    1 http格式   0 https格式
	 * @param forumId  版块ID
	 * @param status    状态  0正常锁定 -1所有（包含屏蔽） 1锁定 默认0
	 * @param primeLevel    精华级别 默认0
	 * @param keyWords   标题模糊检索词
	 * @param topLevel   置顶级别 1本版置顶  2分区置顶    3全局置顶 0无  默认0
	 * @param topicTypeId  主题分类ID
	 * @param excludeId  排除ID
	 * @param createUserId 创建者ID
	 * @param pageNo
	 * @param pageSize	 
	 * @param orderBy  1置顶级别降序  2日浏览量降序 3周浏览量降序 4月浏览量降序
	 *  5总浏览量降序 6回复量降序 7日回复量降序 8id降序   默认8
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/topic/list")
	public void topicList(Integer https,Integer forumId,
			Integer createUserId,Short statu,Short primeLevel,
			String keyWords,Short topLevel,Integer topicTypeId,Integer excludeId,
			String topicTypeIds,Integer pageNo,Integer pageSize,
			Integer orderBy,HttpServletRequest request,HttpServletResponse response) 
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
		if(statu==null){
			statu=0;
		}
		if(primeLevel==null){
			primeLevel=0;
		}
		if(topLevel==null){
			topLevel=0;
		}
		if(orderBy==null){
			orderBy=8;
		}
		Integer siteId=CmsUtils.getSiteId(request);
		Pagination page = bbsTopicMng.getForTag(siteId, forumId,
				null, null, statu, primeLevel,
				keyWords, null, createUserId, topLevel, 
				topicTypeId, excludeId,true, pageNo, pageSize,null, orderBy,null);
		List<BbsTopic> list = (List<BbsTopic>) page.getList();
		int totalCount = page.getTotalCount();
		JSONArray jsonArray=new JSONArray();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				jsonArray.put(i, list.get(i).convertToJson(https,false,
						false,false,null,null));
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
	private BbsTopicMng bbsTopicMng;
}

