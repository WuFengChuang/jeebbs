package com.jeecms.bbs.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.core.entity.CmsSite;
import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.bbs.entity.BbsIncomeStatistic;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsAccountDrawMng;
import com.jeecms.bbs.manager.BbsAdvertisingMng;
import com.jeecms.bbs.manager.BbsIncomeStatisticMng;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.plug.live.action.admin.BbsLiveHostAct;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.entity.BbsLiveApply;
import com.jeecms.plug.live.entity.BbsLiveUserAccount;
import com.jeecms.plug.live.manager.BbsLiveApplyMng;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.manager.BbsLiveUserAccountMng;
import com.jeecms.bbs.web.AdminContextInterceptor;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.util.DateUtils;

@Controller
public class WelcomeAct {
	@RequiresPermissions("index")
	@RequestMapping("/index.do")
	public String index(HttpServletRequest request, ModelMap model) {
		return "index";
	}

	@RequiresPermissions("top")
	@RequestMapping("/top.do")
	public String top(HttpServletRequest request, ModelMap model) {
		// 需要获得站点列表
		List<CmsSite> siteList = cmsSiteMng.getList();
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		model.addAttribute("siteList", siteList);
		model.addAttribute("site", site);
		model.addAttribute("siteParam", AdminContextInterceptor.SITE_PARAM);
		model.addAttribute("user", user);
		return "top";
	}

	@RequiresPermissions("main")
	@RequestMapping("/main.do")
	public String main() {
		return "main";
	}

	@RequiresPermissions("left")
	@RequestMapping("/left.do")
	public String left() {
		return "left";
	}

	@RequiresPermissions("right")
	@RequestMapping("/right.do")
	public String right(HttpServletRequest request, ModelMap model) {
		Date now=Calendar.getInstance().getTime();
		Date todayBegin=DateUtils.parseDateTimeToDay(now);
		Date yesterday=DateUtils.afterDate(todayBegin, -1);
		Date adTipTime=DateUtils.afterDate(todayBegin, 7);
		Long hostApplyCount=liveApplyMng.findLiveApplyCount(BbsLiveApply.STATUS_CHECKING, null);
		Long liveApplyCount=liveMng.findLiveCount(null, null, BbsLive.CHECKING, 
				null, null, null, null);
		Long adDueCount=advertisingMng.findAdvertiseCount(null, true,
				null, todayBegin, adTipTime);
		Long drawCount=accountDrawMng.findAccountDrawCount(null, BbsAccountDraw.CHECKING, null);
		BbsIncomeStatistic toDayIncome=incomeStatisticMng.findByDate(todayBegin);
		BbsIncomeStatistic yesterdayIncome=incomeStatisticMng.findByDate(yesterday);
		Double[]incomeUp=new Double[]{0d,0d,0d,0d,0d,0d};
		Double[]todayIncome=new Double[]{0d,0d,0d,0d,0d,0d};
		if(toDayIncome!=null){
			if(yesterdayIncome!=null){
				incomeUp[0]=toDayIncome.getTotalIncomeAmount()-yesterdayIncome.getTotalIncomeAmount();
				incomeUp[1]=toDayIncome.getAdIncomeAmount()-yesterdayIncome.getAdIncomeAmount();
				incomeUp[2]=toDayIncome.getGiftIncomeAmount()-yesterdayIncome.getGiftIncomeAmount();
				incomeUp[3]=toDayIncome.getMagicIncomeAmount()-yesterdayIncome.getMagicIncomeAmount();
				incomeUp[4]=toDayIncome.getLiveIncomeAmount()-yesterdayIncome.getLiveIncomeAmount();
				incomeUp[5]=toDayIncome.getPostIncomeAmount()-yesterdayIncome.getPostIncomeAmount();
			}else{	
				incomeUp[0]=toDayIncome.getTotalIncomeAmount();
				incomeUp[1]=toDayIncome.getAdIncomeAmount();
				incomeUp[2]=toDayIncome.getGiftIncomeAmount();
				incomeUp[3]=toDayIncome.getMagicIncomeAmount();
				incomeUp[4]=toDayIncome.getLiveIncomeAmount();
				incomeUp[5]=toDayIncome.getPostIncomeAmount();
			}
			todayIncome[0]=toDayIncome.getTotalIncomeAmount();
			todayIncome[1]=toDayIncome.getAdIncomeAmount();
			todayIncome[2]=toDayIncome.getGiftIncomeAmount();
			todayIncome[3]=toDayIncome.getMagicIncomeAmount();
			todayIncome[4]=toDayIncome.getLiveIncomeAmount();
			todayIncome[5]=toDayIncome.getPostIncomeAmount();
		}else{
			if(yesterdayIncome!=null){
				incomeUp[0]=-yesterdayIncome.getTotalIncomeAmount();
				incomeUp[1]=-yesterdayIncome.getAdIncomeAmount();
				incomeUp[2]=-yesterdayIncome.getGiftIncomeAmount();
				incomeUp[3]=-yesterdayIncome.getMagicIncomeAmount();
				incomeUp[4]=-yesterdayIncome.getLiveIncomeAmount();
				incomeUp[5]=-yesterdayIncome.getPostIncomeAmount();
			}
		}
		//开始时间降序
		List<BbsLive>lives=liveMng.getList(null, null, null, null, null, now, 
				now, null, null, 0, 2);
		//门票数倒序
		List<BbsLiveUserAccount> hosts=liveUserAccountMng.getList(null,13, 3);
		model.addAttribute("hostApplyCount", hostApplyCount);
		model.addAttribute("liveApplyCount", liveApplyCount);
		model.addAttribute("adDueCount", adDueCount);
		model.addAttribute("drawCount", drawCount);
		model.addAttribute("todayIncome", todayIncome);
		model.addAttribute("incomeUp", incomeUp);
		model.addAttribute("lives", lives);
		model.addAttribute("hosts", hosts);
		model.addAttribute("site", CmsUtils.getSite(request));
		return "right";
	}
	
	@RequestMapping("/income/indexStatistic.do")
	public String incomeIndexStatistic(Integer flag,HttpServletRequest request, ModelMap model) {
		Calendar calendar=Calendar.getInstance();
		//flag 1 按本月统计 2本年度统计 3历史年度统计 
		if(flag==null){
			flag=1;
		}
		Date begin, end;
		//默认一个月
		end=calendar.getTime();
		begin=DateUtils.getSpecficMonthStart(end, 0);
		List<Object[]> list;
		if(flag==2){
			//今年
			begin=DateUtils.getSpecficYearStart(begin,0);
		}
		list=incomeStatisticMng.findIncomeStatisticCount(begin, end, flag);
		model.addAttribute("flag", flag);
		model.addAttribute("list", list);
		return "data/incomeChart";
	}

	@Autowired
	private CmsSiteMng cmsSiteMng;
	@Autowired
	private BbsLiveApplyMng liveApplyMng;
	@Autowired
	private BbsLiveMng liveMng;
	@Autowired
	private BbsAdvertisingMng advertisingMng;
	@Autowired
	private BbsAccountDrawMng accountDrawMng;
	@Autowired
	private BbsIncomeStatisticMng incomeStatisticMng;
	@Autowired
	private BbsLiveUserAccountMng liveUserAccountMng;
}
