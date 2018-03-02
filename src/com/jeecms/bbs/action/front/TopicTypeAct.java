package com.jeecms.bbs.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_FORUM;
import static com.jeecms.common.web.Constants.INDEX;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.jeecms.bbs.entity.BbsTopicType;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsTopicTypeMng;
import com.jeecms.bbs.manager.BbsTopicTypeSubscribeMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.front.URLHelper;

@Controller
public class TopicTypeAct {
	private static final Logger log = LoggerFactory
			.getLogger(TopicTypeAct.class);

	public static final String TPL_INDEX = "tpl.index";
	public static final String TPL_FORUM = "tpl.forum";
	public static final String TPL_TOPIC = "tpl.topic";
	public static final String TPL_NO_VIEW = "tpl.noview";
	public static final String TPL_TOPIC_TYPE_INDEX = "tpl.topicTypeIndex";
	public static final String TPL_TOPIC_TYPE_DEFAULT = "tpl.topicTypeDefault";
	public static final String TPL_TOPIC_TYPE_USER = "tpl.topicTypeUserIndex";
	public static final String LOGIN_INPUT = "tpl.loginInput";
	public static final int TOPIC_ALL = 0;
	public static final int TOPIC_ESSENCE = 1;
	

	/**
	 * 话题首页
	 */
	@RequestMapping(value = "/topicType/userIndex.jhtml", method = RequestMethod.GET)
	public String topicIndex(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		BbsUser user=CmsUtils.getUser(request);
		if(user!=null&&user.getHasSubscribe()){
			return "redirect:"+user.getFirstSubscribeType().getRedirectUrl();
		}else{
			return topicTypeIndex(request, response, model);
		}
	}
	
	/**
	 * 话题广场首页
	 */
	@RequestMapping(value = "/topicType/index.jhtml", method = RequestMethod.GET)
	public String topicTypeIndex(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_FORUM, TPL_TOPIC_TYPE_INDEX);
	}
	
	/**
	 * 子分类页
	 */
	@RequestMapping(value = "/topicType/*.jhtml", method = RequestMethod.GET)
	public String topicTypeChild(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String[] paths = URLHelper.getPaths(request);
		if(paths.length==2){
			BbsTopicType type=bbsTopicTypeMng.findByPath(paths[1]);
			model.put("type", type);
		}
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_FORUM, TPL_TOPIC_TYPE_INDEX);
	}
	
	/**
	 * 类目访问页
	 */
	@RequestMapping(value = "/topicType/**/*.*", method = RequestMethod.GET)
	public String topicTypeDefault(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String[] paths = URLHelper.getPaths(request);
		int len = paths.length;
		if (len == 3) {
			if(paths[2].equals(INDEX)){
				//topicType/path/index.jhtml 主题分类下所有主题列表
				return topicType(paths[1],TOPIC_ALL, request, response, model);
			}else{
				//topicType/path/top.jhtml  主题分类下精华主题列表
				return topicType(paths[1],TOPIC_ESSENCE, request, response, model);
			}
		}else {
			log.debug("Illegal path length: {}, paths: {}", len, paths);
			return FrontUtils.pageNotFound(request, response, model);
		}
	}
	
	/**
	 * 订阅分类
	 * @param typeId 分类ID
	 * @param operate 1订阅 0取消订阅
	 */
	@RequestMapping(value = "/topicType/subscribe.jspx")
	public void topicTypeSubscribe(
			Integer typeId,Integer operate,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		BbsUser user=CmsUtils.getUser(request);
		JSONObject json=new JSONObject();
		try {
			if(user!=null){
				if(typeId!=null){
					BbsTopicType type=bbsTopicTypeMng.findById(typeId);
					if(type!=null){
						topicTypeSubscribeMng.subscribe(type.getId(), user.getId(),operate);
						json.put("statu",1);
					}else{
						//分类为找到
						json.put("statu",0);
					}
				}else{
					//没有分类id参数
					json.put("statu",-1);
				}
			}else{
				//用户未登录
				json.put("statu", -2);
			}
		} catch (JSONException e) {
//			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	private String topicType(String path,Integer essence,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsTopicType type=bbsTopicTypeMng.findByPath(path);
		FrontUtils.frontData(request, model, site);
		if(type!=null){
			model.put("type", type);
			model.put("essence", essence);
			FrontUtils.frontPageData(request, model);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_FORUM, TPL_TOPIC_TYPE_DEFAULT);
		}else{
			return FrontUtils.pageNotFound(request, response, model);
		}
	}
	@Autowired
	private BbsTopicTypeMng bbsTopicTypeMng;
	@Autowired
	private BbsTopicTypeSubscribeMng topicTypeSubscribeMng;

}
