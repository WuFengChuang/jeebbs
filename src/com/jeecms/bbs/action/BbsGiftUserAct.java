package com.jeecms.bbs.action;

import static com.jeecms.common.page.SimplePage.cpn;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.bbs.entity.BbsGift;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsGiftMng;
import com.jeecms.bbs.manager.BbsGiftUserMng;
import com.jeecms.bbs.manager.BbsUserMng;

@Controller
public class BbsGiftUserAct {
	
	@RequiresPermissions("giftUser:v_list")
	@RequestMapping("/giftUser/v_list.do")
	public String list(Integer giftId,String username,
			Integer pageNo, HttpServletRequest request, ModelMap model) {
		Integer userId=null;
		if(StringUtils.isNotBlank(username)){
			BbsUser user=bbsUserMng.findByUsername(username);
			if(user!=null){
				userId=user.getId();
			}
		}
		Pagination pagination = manager.getPage(giftId,userId,
				cpn(pageNo), CookieUtils.getPageSize(request));
		List<BbsGift>gifts=bbsGiftMng.getList(false, 0, 5000);
		model.addAttribute("pagination",pagination);
		model.addAttribute("gifts",gifts);
		model.addAttribute("giftId",giftId);
		model.addAttribute("username",username);
		return "giftUser/list";
	}
	
	@Autowired
	private BbsGiftUserMng manager;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsGiftMng bbsGiftMng;
}