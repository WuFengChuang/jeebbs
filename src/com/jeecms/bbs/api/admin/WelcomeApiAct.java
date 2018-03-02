package com.jeecms.bbs.api.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.api.ApiResponse;
import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.api.ResponseCode;
import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.bbs.entity.BbsIncomeStatistic;
import com.jeecms.bbs.manager.BbsAccountDrawMng;
import com.jeecms.bbs.manager.BbsAdvertisingMng;
import com.jeecms.bbs.manager.BbsIncomeStatisticMng;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.entity.BbsLiveApply;
import com.jeecms.plug.live.entity.BbsLiveUserAccount;
import com.jeecms.plug.live.manager.BbsLiveApplyMng;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.manager.BbsLiveUserAccountMng;

@Controller
public class WelcomeApiAct {
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/index")
	public void index(Integer pageNo,Integer pageSize,HttpServletRequest request,HttpServletResponse response) throws JSONException{
		if (pageNo==null) {
			pageNo=1;
		}
		if (pageSize==null) {
			pageSize=10;
		}
		Date now = Calendar.getInstance().getTime();
		Date todayBegin = DateUtils.parseDateTimeToDay(now);
		Date yesterday = DateUtils.afterDate(todayBegin, -1);
		Date adTipTime = DateUtils.afterDate(todayBegin, 7);
		Long hostApplyCount = liveApplyMng.findLiveApplyCount(BbsLiveApply.STATUS_CHECKING, null);
		Long liveApplyCount = liveMng.findLiveCount(null, null, BbsLive.CHECKING, null, null, null, null);
		Long adDueCount = advertisingMng.findAdvertiseCount(null, true, null, todayBegin, adTipTime);
		Long drawCount = accountDrawMng.findAccountDrawCount(null, BbsAccountDraw.CHECKING, null);
		BbsIncomeStatistic toDayIncome = incomeStatisticMng.findByDate(todayBegin);
		BbsIncomeStatistic yesterdayIncome = incomeStatisticMng.findByDate(yesterday);
		Double []incomeUp = new Double[]{0d,0d,0d,0d,0d,0d};
		Double []todayIncome = new Double[]{0d,0d,0d,0d,0d,0d};
		if (toDayIncome!=null) {
			if (yesterdayIncome!=null) {
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
			if (yesterdayIncome!=null) {
				incomeUp[0]=-yesterdayIncome.getTotalIncomeAmount();
				incomeUp[1]=-yesterdayIncome.getAdIncomeAmount();
				incomeUp[2]=-yesterdayIncome.getGiftIncomeAmount();
				incomeUp[3]=-yesterdayIncome.getMagicIncomeAmount();
				incomeUp[4]=-yesterdayIncome.getLiveIncomeAmount();
				incomeUp[5]=-yesterdayIncome.getPostIncomeAmount();
			}
		}
		incomeUp[0]=com.jeecms.common.util.StrUtils.retainTwoDecimal(incomeUp[0]);
		Date qFinishTimeBegin = now;
		Date qTimeEnd = now;
		Short queryDbStatus = BbsLive.CHECKED;
		Pagination page = liveMng.getPage(null, null, null, queryDbStatus, null, 
				qTimeEnd, qFinishTimeBegin, null, null, pageNo, 
				pageSize);
		List<BbsLive> list = (List<BbsLive>) page.getList();
		Integer totalCount = page.getTotalCount();
		List<BbsLiveUserAccount> hosts = liveUserAccountMng.getList(null, 13, 3);
		JSONObject json = convertToJson(hostApplyCount,liveApplyCount,adDueCount,drawCount,incomeUp,todayIncome,list,hosts,totalCount);
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = json.toString();
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}
	
	@RequestMapping(value = "/income/indexStatistic")
	public void incomeIndexStatistic(Integer flag,HttpServletRequest request,HttpServletResponse response) throws JSONException, ParseException{
		Calendar calendar = Calendar.getInstance();
		if (flag==null) {
			flag=1;
		}
		Date begin,end;
		end = calendar.getTime();
		begin = DateUtils.getSpecficMonthStart(end, 0);
		if (flag==0) {//按本周查询
			List<Date> week = DateUtils.dateToWeek(end);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String format = sdf.format(week.get(0));
			begin = sdf.parse(format);
		}
		if (flag==2) {
			begin = DateUtils.getSpecficYearStart(begin, 0);
		}
		List<Object[]> list = incomeStatisticMng.findIncomeStatisticCount(begin, end, flag);
		JSONArray jsonArray = new JSONArray();
		if (list!=null) {
			jsonArray = statisticToJson(list,flag,begin,end);
		}
		String status = Constants.API_STATUS_SUCCESS;
		String message = Constants.API_MESSAGE_SUCCESS;
		String code = ResponseCode.API_CODE_CALL_SUCCESS;
		String body = jsonArray.toString();
		ApiResponse apiResponse = new ApiResponse(body, message, status, code);
		ResponseUtils.renderApiJson(response, request, apiResponse);
	}

	@SuppressWarnings("deprecation")
	private JSONArray statisticToJson(List<Object[]> list, Integer flag,Date begin,Date end) throws ParseException, JSONException {
		JSONArray jsonArray = new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (flag==1) {
			//按月统计
			//计算出本月天数
			int days = daysBetween(begin, end);
			int index = 0;//list下标初始化
			long time =begin.getTime();//获取开始时间毫秒数
			for(int i=0;i<=days;i++){
				JSONObject json = new JSONObject();
				Integer day = -1;
				if (list.size()>0&&index<list.size()) {//list必须有值，且下标不能超过list的大小
					//获取当前日期
					day = (Integer) list.get(index)[6];
				}
				if (day.equals(i+1)) {
					if (list.get(index)[0]!=null) {
						json.put("totalIncome", list.get(index)[0]);
					}else{
						json.put("totalIncome", 0);
					}
					if (list.get(index)[1]!=null) {
						json.put("adIncome", list.get(index)[1]);
					}else{
						json.put("adIncome", 0);
					}
					if (list.get(index)[2]!=null) {
						json.put("giftIncome", list.get(index)[2]);
					}else{
						json.put("giftIncome", 0);
					}
					if (list.get(index)[3]!=null) {
						json.put("magicIncome", list.get(index)[3]);
					}else{
						json.put("magicIncome", 0);
					}
					if (list.get(index)[4]!=null) {
						json.put("liveIncome", list.get(index)[4]);
					}else{
						json.put("liveIncome",0);
					}
					if (list.get(index)[5]!=null) {
						json.put("postIncome", list.get(index)[5]);
					}else{
						json.put("postIncome", 0);
					}
					index++;
				}else{
					json.put("totalIncome", 0);
					json.put("adIncome", 0);
					json.put("giftIncome", 0);
					json.put("magicIncome", 0);
					json.put("liveIncome",0);
					json.put("postIncome", 0);
				}
				String format = sdf.format(new Date(time));
				json.put("xdata", format.substring(5,format.length()));
				jsonArray.put(i,json);
				time+=60*60*1000*24;//时间每循环一次加一天
			}
		}else if (flag==2) {
			//按年统计
			//计算月份数
			int monthCount = end.getMonth()-begin.getMonth()+1;
			int index = 0;//list初始化下标
			for(int i = 0 ; i<monthCount;i++){
				JSONObject json = new JSONObject();
				Integer month = -1;
				if (list.size()>0&&index<list.size()) {
					month = (Integer) list.get(index)[6];
				}
				if (month.equals(i)) {
					if (list.get(index)[0]!=null) {
						json.put("totalIncome", list.get(index)[0]);
					}else{
						json.put("totalIncome", 0);
					}
					if (list.get(index)[1]!=null) {
						json.put("adIncome", list.get(index)[1]);
					}else{
						json.put("adIncome", 0);
					}
					if (list.get(index)[2]!=null) {
						json.put("giftIncome", list.get(index)[2]);
					}else{
						json.put("giftIncome", 0);
					}
					if (list.get(index)[3]!=null) {
						json.put("magicIncome", list.get(index)[3]);
					}else{
						json.put("magicIncome", 0);
					}
					if (list.get(index)[4]!=null) {
						json.put("liveIncome", list.get(index)[4]);
					}else{
						json.put("liveIncome",0);
					}
					if (list.get(index)[5]!=null) {
						json.put("postIncome", list.get(index)[5]);
					}else{
						json.put("postIncome", 0);
					}
					index++;
				}else{
					json.put("totalIncome", 0);
					json.put("adIncome", 0);
					json.put("giftIncome", 0);
					json.put("magicIncome", 0);
					json.put("liveIncome",0);
					json.put("postIncome", 0);
				}
				json.put("xdata", i+1);
				jsonArray.put(i,json);
			}
		}else if (flag==3) {
			//全部查询
			for(int i =0 ; i<list.size();i++){
				JSONObject json = new JSONObject();
				if (list.get(i)[0]!=null) {
					json.put("totalIncome", list.get(i)[0]);
				}else{
					json.put("totalIncome", 0);
				}
				if (list.get(i)[1]!=null) {
					json.put("adIncome", list.get(i)[1]);
				}else{
					json.put("adIncome", 0);
				}
				if (list.get(i)[2]!=null) {
					json.put("giftIncome", list.get(i)[2]);
				}else{
					json.put("giftIncome", 0);
				}
				if (list.get(i)[3]!=null) {
					json.put("magicIncome", list.get(i)[3]);
				}else{
					json.put("magicIncome", 0);
				}
				if (list.get(i)[4]!=null) {
					json.put("liveIncome", list.get(i)[4]);
				}else{
					json.put("liveIncome",0);
				}
				if (list.get(i)[5]!=null) {
					json.put("postIncome", list.get(i)[5]);
				}else{
					json.put("postIncome", 0);
				}
				if (list.get(i)[6]!=null) {
					json.put("xdata", list.get(i)[6]);
				}else{
					json.put("xdata", 0);
				}
				jsonArray.put(i,json);
			}
		}else{
			//按周计算
			//计算出本周天数
			int days = daysBetween(begin, end);
			//获取begin的日期
			String format = sdf.format(begin);
			int date = Integer.parseInt(format.substring(8,format.length()));
			int index = 0;//list下标初始化
			for(int i=0;i<=days;i++){
				JSONObject json = new JSONObject();
				Integer day = -1;
				if (list.size()>0&&index<list.size()) {
					day = (Integer) list.get(index)[6];
				}
				if (day.equals(i+date)) {
					if (list.get(index)[0]!=null) {
						json.put("totalIncome", list.get(index)[0]);
					}else{
						json.put("totalIncome", 0);
					}
					if (list.get(index)[1]!=null) {
						json.put("adIncome", list.get(index)[1]);
					}else{
						json.put("adIncome", 0);
					}
					if (list.get(index)[2]!=null) {
						json.put("giftIncome", list.get(index)[2]);
					}else{
						json.put("giftIncome", 0);
					}
					if (list.get(index)[3]!=null) {
						json.put("magicIncome", list.get(index)[3]);
					}else{
						json.put("magicIncome", 0);
					}
					if (list.get(index)[4]!=null) {
						json.put("liveIncome", list.get(index)[4]);
					}else{
						json.put("liveIncome",0);
					}
					if (list.get(index)[5]!=null) {
						json.put("postIncome", list.get(index)[5]);
					}else{
						json.put("postIncome", 0);
					}
					index++;
				}else{
					json.put("totalIncome", 0);
					json.put("adIncome", 0);
					json.put("giftIncome", 0);
					json.put("magicIncome", 0);
					json.put("liveIncome",0);
					json.put("postIncome", 0);
				}
				json.put("xdata", getWeekDay(i));
				jsonArray.put(i,json);
			}
		
		}
		return jsonArray;
	}
	
	private String getWeekDay(int day){
		String weekDay = "";
		switch (day) {
		case 0:
			weekDay="周一";
			break;
		case 1:
			weekDay="周二";
			break;
		case 2:
			weekDay="周三";
			break;
		case 3:
			weekDay="周四";
			break;
		case 4:
			weekDay="周五";
			break;
		case 5:
			weekDay="周六";
			break;
		case 6:
			weekDay="周日";
			break;
		default:
			break;
		}
		return weekDay;
	}
	
	private int daysBetween(Date smdate,Date bdate) throws ParseException{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));
        bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();             
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();   
        long between_days=(time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(between_days));           
	}    
	
	private JSONObject convertToJson(Long hostApplyCount, Long liveApplyCount, Long adDueCount, Long drawCount,
			Double[] incomeUp, Double[] todayIncome, List<BbsLive> list, List<BbsLiveUserAccount> hosts,Integer totalCount) throws JSONException {
		JSONObject json = new JSONObject();
		if (hostApplyCount!=null) {
			json.put("hostApplyCount", hostApplyCount);
		}else{
			json.put("hostApplyCount", 0);
		}
		if (liveApplyCount!=null) {
			json.put("liveApplyCount", liveApplyCount);
		}else{
			json.put("liveApplyCount", 0);
		}
		if (adDueCount!=null) {
			json.put("adDueCount", adDueCount);
		}else{
			json.put("adDueCount", 0);
		}
		if (drawCount!=null) {
			json.put("drawCount", drawCount);
		}else{
			json.put("drawCount", 0);
		}
		JSONObject todayIncomeJson = new JSONObject();
		if (todayIncome!=null&&todayIncome.length>0) {
			todayIncomeJson.put("totalIncomeAmount", todayIncome[0]);
			todayIncomeJson.put("adIncomeAmount", todayIncome[1]);
			todayIncomeJson.put("giftIncomeAmount", todayIncome[2]);
			todayIncomeJson.put("magicIncomeAmount", todayIncome[3]);
			todayIncomeJson.put("liveIncomeAmount", todayIncome[4]);
			todayIncomeJson.put("postIncomeAmount", todayIncome[5]);
		}else{
			todayIncomeJson.put("totalIncomeAmount", 0);
			todayIncomeJson.put("adIncomeAmount", 0);
			todayIncomeJson.put("giftIncomeAmount", 0);
			todayIncomeJson.put("magicIncomeAmount", 0);
			todayIncomeJson.put("liveIncomeAmount", 0);
			todayIncomeJson.put("postIncomeAmount", 0);
		}
		json.put("todayIncome", todayIncomeJson);
		JSONObject incomeUpJson = new JSONObject();
		if (incomeUp!=null&&incomeUp.length>0) {
			incomeUpJson.put("totalIncomeAmount", incomeUp[0]);
			incomeUpJson.put("adIncomeAmount", incomeUp[1]);
			incomeUpJson.put("giftIncomeAmount", incomeUp[2]);
			incomeUpJson.put("magicIncomeAmount", incomeUp[3]);
			incomeUpJson.put("liveIncomeAmount", incomeUp[4]);
			incomeUpJson.put("postIncomeAmount", incomeUp[5]);
		}else{
			incomeUpJson.put("totalIncomeAmount", 0);
			incomeUpJson.put("adIncomeAmount", 0);
			incomeUpJson.put("giftIncomeAmount",0);
			incomeUpJson.put("magicIncomeAmount", 0);
			incomeUpJson.put("liveIncomeAmount", 0);
			incomeUpJson.put("postIncomeAmount", 0);
		}
		json.put("incomeUp", incomeUpJson);
		JSONArray livesJson = new JSONArray();
		if (list!=null&&list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				livesJson.put(i,liveToJson(list.get(i)));
			}
		}
		json.put("lives", livesJson);
		if (totalCount!=null) {
			json.put("totalCount", totalCount);
		}else{
			json.put("totalCount", 0);
		}
		JSONArray hostsJson = new JSONArray();
		if (hosts!=null&&hosts.size()>0) {
			for (int i = 0; i < hosts.size(); i++) {
				hostsJson.put(i,hostsToJson(hosts.get(i)));
			}
		}
		json.put("hosts", hostsJson);
		return json;
	}

	private JSONObject hostsToJson(BbsLiveUserAccount hosts) throws JSONException {
		JSONObject json = new JSONObject();
		if (hosts.getUser()!=null&&StringUtils.isNotBlank(hosts.getUser().getAvatar())) {
			json.put("avatar", hosts.getUser().getAvatar());
		}else{
			json.put("avatar", "");
		}
		if (hosts.getUser()!=null&&StringUtils.isNotBlank(hosts.getUser().getRealname())) {
			json.put("realname", hosts.getUser().getRealname());
		}else{
			json.put("realname", "");
		}
		if (hosts.getTicketNum()!=null) {
			json.put("ticketNum", hosts.getTicketNum());
		}else{
			json.put("ticketNum", 0);
		}
		return json;
	}

	private JSONObject liveToJson(BbsLive live) throws JSONException {
		JSONObject json = new JSONObject();
		if (live.getId()!=null) {
			json.put("id", live.getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(live.getDemandImageUrl())) {
			json.put("demandImageUrl", live.getDemandImageUrl());
		}else{
			json.put("demandImageUrl", "");
		}
		if (StringUtils.isNotBlank(live.getTitle())) {
			json.put("title", live.getTitle());
		}else{
			json.put("title", "");
		}
		if (live.getUser()!=null&&StringUtils.isNotBlank(live.getUser().getRealname())) {
			json.put("realName", live.getUser().getRealname());
		}else{
			json.put("realName", "");
		}
		if (live.getTicketNum()!=null) {
			json.put("ticketNum", live.getTicketNum());
		}else {
			json.put("ticketNum", 0);
		}
		if (live.getInliveUserNum()!=null) {
			json.put("inliveUserNum", live.getInliveUserNum());
		}else{
			json.put("inliveUserNum", 0);
		}
		if (live.getGiftNum()!=null) {
			json.put("giftNum", live.getGiftNum());
		}else{
			json.put("giftNum", 0);
		}
		return json;
	}
}
