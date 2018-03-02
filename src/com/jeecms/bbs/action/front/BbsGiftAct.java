package com.jeecms.bbs.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_SPECIAL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.bbs.manager.BbsGiftMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.front.URLHelper;

@Controller
public class BbsGiftAct {
	private static final Logger log = LoggerFactory
			.getLogger(BbsGiftAct.class);

	public static final String TPL_GIFT_INDEX = "tpl.giftIndex";
	public static final String LOGIN_INPUT = "tpl.loginInput";
	public static final int TOPIC_ALL = 0;
	public static final int TOPIC_ESSENCE = 1;
	/**
	 * 礼物页
	 */
	@RequestMapping(value = "/gift/index*.jhtml", method = RequestMethod.GET)
	public String giftIndex(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		Pagination pagination=bbsGiftMng.getPage(URLHelper.getPageNo(request), CookieUtils.getPageSize(request));
		model.addAttribute("tag_pagination", pagination);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_SPECIAL, TPL_GIFT_INDEX);
	}
	
	@Autowired
	private BbsGiftMng bbsGiftMng;

}
