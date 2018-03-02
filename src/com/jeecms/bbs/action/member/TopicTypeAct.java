package com.jeecms.bbs.action.member;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.common.page.SimplePage.cpn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsTopicTypeSubscribeMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.entity.CmsSite;

@Controller
public class TopicTypeAct {
	public static final String TPL_TOPIC_TYPE_MY = "tpl.topicTypeSubscribe";
	public static final String TPL_TOPIC_TYPE_DEFAULT = "tpl.topicTypeDefault";
	public static final String LOGIN_INPUT = "tpl.loginInput";
	public static final int TOPIC_ALL = 0;
	public static final int TOPIC_ESSENCE = 1;
	
	/**
	 * 我的订阅
	 */
	@RequestMapping(value = "/member/myTopicType*.jhtml")
	public String topicTypeIndex(Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user=CmsUtils.getUser(request);
		Pagination tag_pagination=topicTypeSubscribeMng.getPage(user.getId(),
				cpn(pageNo), CookieUtils.getPageSize(request));
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		model.put("tag_pagination", tag_pagination);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_MEMBER, TPL_TOPIC_TYPE_MY);
	}
	
	
	@Autowired
	private BbsTopicTypeSubscribeMng topicTypeSubscribeMng;

}
