package com.jeecms.bbs.action.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;

/**
 * 版主管理论坛
 * @author tom
 * @date 2017年7月26日
 */
@Controller
public class ForumMngAct {
	public static final String USER_ATTENT_LIST = "tpl.userAttentList";
	public static final String TOPIC_ATTENT_LIST = "tpl.topicAttentList";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	public static final String USER_TOPIC_LIST = "tpl.usertopicList";
	public static final String USER_POST_LIST = "tpl.userPostList";
	public static final String PERSON_ATTENT_LIST = "tpl.personAttentList";

	
	/**关注人
	 * @param userId 被禁言用户ID
	 * @param operate  0禁言 1取消禁言
	 */
	@RequestMapping(value = "/user/forbidden.jspx")
	public void userForbidden(Integer forumId, Integer userId,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model)throws JSONException {
		BbsUser user = CmsUtils.getUser(request);
		BbsUser forbiddenUser = bbsUserMng.findById(userId);
		CmsSite site=CmsUtils.getSite(request);
		JSONObject object = new JSONObject();
		int status=0;
		BbsForum forum=null;
		if(forumId!=null){
			forum=forumMng.findById(forumId);
		}
		if (validateForbidden(user, forbiddenUser, forum)) {
			if(forbiddenUser.getDisabled()){
				bbsUserMng.forbidUser(site.getId(), forbiddenUser, false);
				status=1;
			}else{
				bbsUserMng.forbidUser(site.getId(), forbiddenUser, true);
				status=2;
			}
		} 
		object.put("status", status);
		ResponseUtils.renderJson(response, object.toString());
	}
	
	
	private boolean validateForbidden(BbsUser user, BbsUser forbiddenUser,BbsForum forum) {
		// 用户未登录
		if (user == null) {
			return false;
		}
		if (forum == null) {
			return false;
		}
		// 被禁言者不存在
		if (forbiddenUser == null) {
			return false;
		}
		if(!user.getGroup().hasRight(forum, user)){
			return false;
		}
		// 不允许禁言自己
		if (user.equals(forbiddenUser)) {
			return false;
		}
		//不允许禁言版主
		if (forbiddenUser.getGroup().hasRight(forum, forbiddenUser)) {
			return false;
		}
		//不允许禁用管理员
		if(forbiddenUser.getAdmin()){
			return false;
		}
		return true;
	}

	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsForumMng forumMng;
}
