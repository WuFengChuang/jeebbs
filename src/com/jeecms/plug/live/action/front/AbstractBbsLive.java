package com.jeecms.plug.live.action.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.core.entity.CmsSite;

public abstract class AbstractBbsLive {
	protected String hostCheck(BbsUser user,CmsSite site,HttpServletRequest request,
			HttpServletResponse response,ModelMap model){
		if (user == null) {
			return FrontUtils.showLogin(request, model, site);
		}else{
			boolean liveHost=user.getLiveHost();
			if(!liveHost){
				return FrontUtils.showMessage(request, model, "message.info.youAreNoHost");
			}
		}
		return null;
	}
}
