package com.jeecms.bbs.action.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.manager.BbsSessionMng;
import com.jeecms.common.web.ResponseUtils;

@Controller
public class BbsSessionStatisticAct {
	@RequestMapping(value = "/session/statistic.jspx")
	public void statisticSessionCount(HttpServletRequest request,HttpServletResponse response) {
		JSONObject json=new JSONObject();
		Integer membtertotal=bbsSessionMng.total(true);
		Integer visitortotal=bbsSessionMng.total(false);
		Integer total=visitortotal+membtertotal;
		try {
			json.put("membtertotal", membtertotal);
			json.put("visitortotal", visitortotal);
			json.put("usertotal", total);
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	@Autowired
	private BbsSessionMng bbsSessionMng;
}
