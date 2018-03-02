package com.jeecms.plug.live.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.common.util.DateUtils;
import com.jeecms.plug.live.entity.base.BaseBbsLive;



public class BbsLive extends BaseBbsLive {
	private static final long serialVersionUID = 1L;
	
	public static final Short CHECKING = 0;
	public static final Short CHECKED = 1;
	public static final Short REJECT = 2;
	public static final Short STOP = 3;
	
	public static final Short LIVE_PLAT_BAIDU = 1;
	public static final Short LIVE_PLAT_TENCENT = 2;
	
	public static final String LIVE_PLAT_STR_BAIDU = "baidu";
	public static final String LIVE_PLAT_STR_TENCENT = "tencent";
	
	public String getUrlWhole() {
		return getSite().getUrlBuffer(true, true, false).append(
				"/live/front/get.jspx?id=").append(getId()).toString();
	}
	
	public boolean isCharge(){
		boolean charge=false;
		if(getBeginPrice()!=null&&getBeginPrice()>0){
			charge=true;
		}
		if(getAfterPrice()!=null&&getAfterPrice()>0){
			charge=true;
		}
		return charge;
	}
	
	public boolean isUnlimited(){
		Integer limitUserNum=getLimitUserNum();
		if(limitUserNum!=null&&limitUserNum.equals(0)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean hasEnoughUserNum(){
		Integer limitUserNum=getLimitUserNum();
		Integer joinUserNum= getJoinUserNum();
		if(limitUserNum!=null){
			if(limitUserNum.equals(0)){
				return true;
			}
			if(limitUserNum>joinUserNum){
				return true;
			}
		}
		return false;
	}
	
	public String[] getJoinUserIds() {
		Set<BbsLiveUser> joinUsers = getJoinUsers();
		String[] ids = new String[joinUsers.size()];
		int i = 0;
		for (BbsLiveUser c : joinUsers) {
			ids[i++] = c.getJoinUser().getId().toString();
		}
		return ids;
	}
	
	public BbsLiveCharge getLiveCharge() {
		Set<BbsLiveCharge> set = getChargeSet();
		if (set != null && set.size() > 0) {
			return set.iterator().next();
		} else {
			return null;
		}
	}
	
	public Double getTotalAmount() {
		BbsLiveCharge charge= getLiveCharge();
		if(charge!=null){
			return charge.getTotalAmount();
		}else{
			return 0d;
		}
	}
	
	public Double getYearAmount() {
		BbsLiveCharge charge= getLiveCharge();
		if(charge!=null){
			return charge.getYearAmount();
		}else{
			return 0d;
		}
	}
	
	public Double getMonthAmount() {
		BbsLiveCharge charge= getLiveCharge();
		if(charge!=null){
			return charge.getMonthAmount();
		}else{
			return 0d;
		}
	}
	
	public Double getDayAmount() {
		BbsLiveCharge charge= getLiveCharge();
		if(charge!=null){
			return charge.getDayAmount();
		}else{
			return 0d;
		}
	}
	
	public Integer getTicketNum() {
		BbsLiveCharge charge= getLiveCharge();
		if(charge!=null){
			return charge.getTicketNum();
		}else{
			return 0;
		}
	}
	
	public Integer getGiftNum() {
		BbsLiveCharge charge= getLiveCharge();
		if(charge!=null){
			return charge.getGiftNum();
		}else{
			return 0;
		}
	}
	
	public Date getLastBuyTime() {
		BbsLiveCharge charge= getLiveCharge();
		if(charge!=null){
			return charge.getLastBuyTime();
		}else{
			return null;
		}
	}
	
	public boolean getHasOver() {
		Date endTime=getEndTime();
		Date now =Calendar.getInstance().getTime();
		if(endTime==null){
			return false;
		}else{
			if(endTime.before(now)){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public boolean getHasBegin() {
		Date beginTime=getBeginTime();
		Date now =Calendar.getInstance().getTime();
		if(beginTime==null){
			return false;
		}else{
			if(beginTime.before(now)){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public void init(){
		if(getTotalAmount()==null){
			setTotalAmount(0d);
		}
		if(getJoinUserNum()==null){
			setJoinUserNum(0);
		}
		if(getCommissionRate()==null){
			setCommissionRate(0d);
		}
		if(getAfterPrice()==null){
			setAfterPrice(0d);
		}
		if(getBeginPrice()==null){
			setBeginPrice(0d);
		}
		if(getLimitUserNum()==null){
			setLimitUserNum(0);
		}
		if(getInliveUserNum()==null){
			setInliveUserNum(0);
		}
	}

/*[CONSTRUCTOR MARKER BEGIN]*/
	public BbsLive () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public BbsLive (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public BbsLive (
		java.lang.Integer id,
		com.jeecms.bbs.entity.BbsUser user,
		java.lang.String title,
		java.util.Date createTime,
		java.util.Date beginTime,
		java.util.Date endTime,
		java.lang.Double beginPrice,
		java.lang.Double afterPrice,
		java.lang.Integer limitUserNum,
		java.lang.Double commissionRate,
		java.lang.Short checkStatus,
		java.lang.Integer joinUserNum,
		java.lang.Double totalAmount) {

		super (
			id,
			user,
			title,
			createTime,
			beginTime,
			endTime,
			beginPrice,
			afterPrice,
			limitUserNum,
			commissionRate,
			checkStatus,
			joinUserNum,
			totalAmount);
	}

	public JSONObject convertToJson(Short qStatus) throws JSONException {
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getTitle())) {
			json.put("title", getTitle());
		}else{
			json.put("title", "");
		}
		if (getBeginTime()!=null) {
			json.put("beginTime", DateUtils.parseDateToDateStr(getBeginTime()));
		}else{
			json.put("beginTime", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getRealname())) {
			json.put("realname", getUser().getRealname());
		}else{
			json.put("realname", "");
		}
		if (getUser()!=null&&StringUtils.isNotBlank(getUser().getUsername())) {
			json.put("username", getUser().getUsername());
		}else{
			json.put("username", "");
		}
		if (getBeginPrice()!=null) {
			json.put("beginPrice", getBeginPrice());
		}else{
			json.put("beginPrice", "");
		}
		if (qStatus!=null) {
			if (qStatus==1) {
				if (getLimitUserNum()!=null) {
					json.put("limitUserNum", getLimitUserNum());
				}else{
					json.put("limitUserNum", "");
				}
			}else if (qStatus==2) {
				if (getJoinUserNum()!=null) {
					json.put("joinUserNum", getJoinUserNum());
				}else{
					json.put("joinUserNum", "");
				}
				if (getInliveUserNum()!=null) {
					json.put("inliveUserNum", getInliveUserNum());
				}else{
					json.put("inliveUserNum", "");
				}
				if (getGiftNum()!=null) {
					json.put("giftNum", getGiftNum());
				}else{
					json.put("giftNum", "");
				}
			}else if (qStatus==3) {
				if (getLimitUserNum()!=null) {
					json.put("limitUserNum", getLimitUserNum());
				}else{
					json.put("limitUserNum", "");
				}
				if (getEndTime()!=null) {
					json.put("endTime", DateUtils.parseDateToDateStr(getEndTime()));
				}else{
					json.put("endTime", "");
				}
				if (getGiftNum()!=null) {
					json.put("giftNum", getGiftNum());
				}else{
					json.put("giftNum", "");
				}
			}else if (qStatus==4) {
				if (getCheckStatus()!=null) {
					json.put("checkStatus", getCheckStatus());
				}else{
					json.put("checkStatus", "");
				}
			}else if (qStatus==5) {
				if (getJoinUserNum()!=null) {
					json.put("joinUserNum", getJoinUserNum());
				}else{
					json.put("joinUserNum", 0);
				}
				if (getInliveUserNum()!=null) {
					json.put("inliveUserNum", getInliveUserNum());
				}else{
					json.put("inliveUserNum", 0);
				}
				if (getGiftNum()!=null) {
					json.put("giftNum", getGiftNum());
				}else{
					json.put("giftNum", 0);
				}
			}else{
				if (getCheckStatus()!=null) {
					json.put("checkStatus", getCheckStatus());
				}else{
					json.put("checkStatus", "");
				}
				if (StringUtils.isNotBlank(getDescription())) {
					json.put("description", getDescription());
				}else{
					json.put("description", "");
				}
				if (StringUtils.isNotBlank(getLiveLogo())) {
					json.put("liveLogo", getLiveLogo());
				}else{
					json.put("liveLogo", "");
				}
				if (getEndTime()!=null) {
					json.put("endTime", DateUtils.parseDateToDateStr(getEndTime()));
				}else{
					json.put("endTime", "");
				}
			}
		}else{
			if (getCheckStatus()!=null) {
				json.put("checkStatus", getCheckStatus());
			}else{
				json.put("checkStatus", "");
			}
			if (StringUtils.isNotBlank(getDescription())) {
				json.put("description", getDescription());
			}else{
				json.put("description", "");
			}
			if (StringUtils.isNotBlank(getLiveLogo())) {
				json.put("liveLogo", getLiveLogo());
			}else{
				json.put("liveLogo", "");
			}
			if (getEndTime()!=null) {
				json.put("endTime", DateUtils.parseDateToDateStr(getEndTime()));
			}else{
				json.put("endTime", "");
			}
		}
		return json;
	}

/*[CONSTRUCTOR MARKER END]*/


}