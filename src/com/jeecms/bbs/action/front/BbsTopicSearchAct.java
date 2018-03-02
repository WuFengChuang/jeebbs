package com.jeecms.bbs.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_FORUM;
import static com.jeecms.bbs.Constants.TPLDIR_SPECIAL;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;

@Controller
public class BbsTopicSearchAct {
	public static final String SEARCH_RESULT = "tpl.search";

	@RequestMapping(value = "/topic/search*.jhtml")
	public String searchSubmit(HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		String keywords = RequestUtils.getQueryParam(request, "keywords");
		String forumIdStr = RequestUtils.getQueryParam(request, "forumId");
		Integer forumId = 0;
		if (StringUtils.isNotBlank(forumIdStr)&&
				StringUtils.isNumeric(forumIdStr)) {
			forumId = Integer.parseInt(forumIdStr);
		}
		model.put("keywords", keywords);
		model.put("forumId", forumId);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_SPECIAL, SEARCH_RESULT);
	}
}
