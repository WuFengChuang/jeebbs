package com.jeecms.bbs.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_MEMBER;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;
import static com.jeecms.common.page.SimplePage.cpn;
import static com.jeecms.bbs.Constants.TPLDIR_CSI;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsAdvertising;
import com.jeecms.bbs.entity.BbsAdvertisingSpace;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsAdvertisingMng;
import com.jeecms.bbs.manager.BbsAdvertisingSpaceMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;

/**
 * 广告Action
 */
@Controller
public class AdvertisingAct {
	// private static final Logger log = LoggerFactory
	// .getLogger(AdvertisingAct.class);

	public static final String TPL_AD = "tpl.advertising";
	public static final String TPL_ADSPACE = "tpl.adspace";
	
	@RequestMapping(value = "/ad.jspx")
	public String ad(Integer id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		if (id != null) {
			BbsAdvertising ad = bbsAdvertisingMng.findById(id);
			if(ad!=null&&!ad.getEnabled()){
				WebErrors errors=WebErrors.create(request);
				errors.addErrorCode("error.ad.disabled");
				return FrontUtils.showError(request, response, model, errors);
			}
			model.addAttribute("ad", ad);
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_CSI, TPL_AD);
	}

	@RequestMapping(value = "/adspace.jspx")
	public String adspace(Integer id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		if (id != null) {
			BbsAdvertisingSpace adspace = bbsAdvertisingSpaceMng.findById(id);
			List<BbsAdvertising> adList = bbsAdvertisingMng.getList(id, true,null,100);
			model.addAttribute("adspace", adspace);
			model.addAttribute("adList", adList);
		}
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_CSI, TPL_ADSPACE);
	}

	@RequestMapping(value = "/ad_display.jspx")
	public void display(Integer id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (id != null) {
			BbsAdvertising ad=bbsAdvertisingMng.findById(id);
			if(ad!=null&&ad.getEnabled()){
				bbsAdvertisingMng.display(id);
			}
		}
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
	}

	@RequestMapping(value = "/ad_click.jspx")
	public void click(Integer id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		if (id != null) {
			BbsAdvertising ad=bbsAdvertisingMng.findById(id);
			if(ad!=null&&ad.getEnabled()){
				bbsAdvertisingMng.click(id);
			}
		}
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
	}

	@Autowired
	private BbsAdvertisingMng bbsAdvertisingMng;
	@Autowired
	private BbsAdvertisingSpaceMng bbsAdvertisingSpaceMng;
}
