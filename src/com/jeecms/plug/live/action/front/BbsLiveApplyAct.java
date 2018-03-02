package com.jeecms.plug.live.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_PLUG;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.plug.live.entity.BbsLiveApply;
import com.jeecms.plug.live.manager.BbsLiveApplyMng;

/**
 * 活动live主讲人申请
 */
@Controller
public class BbsLiveApplyAct {
	 private static final Logger log = LoggerFactory
	 .getLogger(BbsLiveApplyAct.class);

	public static final String TPL_APPLY_HOST = "tpl.applyHost";

	@RequestMapping(value = "/live/host/apply.jspx", method = RequestMethod.GET)
	public String hostApplyGet(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			boolean haveApplied=liveApplyMng.haveApplied(user.getId());
			if(haveApplied){
				return FrontUtils.showMessage(request, model, "message.info.haveApplyHost");
			}
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_APPLY_HOST);
	}
	
	@RequestMapping(value = "/live/host/apply.jspx",method = RequestMethod.POST)
	public String hostApplyPost(String intro,String brief,String experience,
			String mobile,String address,
			String[] picPaths, String[] picDescs,String nextUrl,
			HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			boolean haveApplied=liveApplyMng.haveApplied(user.getId());
			if(haveApplied){
				return FrontUtils.showMessage(request, model, "message.info.haveApplyHost");
			}
		}
		BbsLiveApply liveApply=new BbsLiveApply();
		liveApply.setIntro(intro);
		liveApply.setBrief(brief);
		liveApply.setExperience(experience);
		liveApply.setMobile(mobile);
		liveApply.setAddress(address);
		liveApply.setApplyTime(Calendar.getInstance().getTime());
		liveApply.setStatus(BbsLiveApply.STATUS_CHECKING);
		liveApply.setApplyUser(user);
		liveApplyMng.save(liveApply, picPaths, picDescs);
		return FrontUtils.showSuccess(request, model, nextUrl);
	}

	@Autowired
	private BbsLiveApplyMng liveApplyMng;
}
