package com.jeecms.core.entity;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.bbs.web.StrUtils;


public class BbsConfigAttr {
	public BbsConfigAttr() {
	}

	public BbsConfigAttr(Map<String, String> attr) {
		this.attr = attr;
	}

	private Map<String, String> attr;

	public Map<String, String> getAttr() {
		if (attr == null) {
			attr = new HashMap<String, String>();
		}
		return attr;
	}

	public void setAttr(Map<String, String> attr) {
		this.attr = attr;
	}

	public Boolean getSsoEnable() {
		String enable = getAttr().get(SSO_ENABLE);
		return !"false".equals(enable);
	}
	
	public String getKeepMinute() {
		 return getAttr().get(KEEPMINUTE);
	}
	
	public void setKeepMinute(String keepMinute) {
		getAttr().put(KEEPMINUTE, keepMinute);
	}
	
	public String getDefaultActiveLevel() {
		 return getAttr().get(DEFAULT_ACTIVE_LEVEL);
	}
	
	public void setDefaultActiveLevel(String level) {
		getAttr().put(DEFAULT_ACTIVE_LEVEL, level);
	}
	
	public Boolean getQqEnable() {
		String enable = getAttr().get(QQ_ENABLE);
		return !"false".equals(enable);
	}
	
	public String getQqID() {
		return getAttr().get(QQ_ID);
	}
	
	public String getQqKey() {
		return getAttr().get(QQ_KEY);
	}
	
	public Boolean getSinaEnable() {
		String enable = getAttr().get(SINA_ENABLE);
		return !"false".equals(enable);
	}
	
	public String getSinaID() {
		return getAttr().get(SINA_ID);
	}
	
	public String getSinaKey() {
		return getAttr().get(SINA_KEY);
	}
	
	public Boolean getQqWeboEnable() {
		String enable = getAttr().get(QQWEBO_ENABLE);
		return !"false".equals(enable);
	}
	
	public String getQqWeboID() {
		return getAttr().get(QQWEBO_ID);
	}
	
	public String getQqWeboKey() {
		return getAttr().get(QQWEBO_KEY);
	}
	
	public String getUserOnlineTopDay() {
		return getAttr().get(USER_ONLINE_TOP_DAY);
	}
	
	public String getUserOnlineTopNum() {
		return getAttr().get(USER_ONLINE_TOP_NUM);
	}
	
	public void setUserOnlineTopDay(String day) {
		getAttr().put(USER_ONLINE_TOP_DAY, day);
	}
	
	public void setUserOnlineTopNum(String num) {
		getAttr().put(USER_ONLINE_TOP_NUM, num);
	}
	
	public void setQqEnable(Boolean enable) {
		getAttr().put(QQ_ENABLE, String.valueOf(enable));
	}
	
	public void setQqID(String id) {
		getAttr().put(QQ_ID, id);
	}
	
	public void setQqKey(String key) {
		getAttr().put(QQ_KEY, key);
	}
	
	public void setSinaEnable(Boolean enable) {
		getAttr().put(SINA_ENABLE, String.valueOf(enable));
	}
	
	public void setSinaID(String id) {
		getAttr().put(SINA_ID,id);
	}
	
	public void setSinaKey(String key) {
		getAttr().put(SINA_KEY,key);
	}
	
	public void setQqWeboEnable(Boolean enable) {
		getAttr().put(QQWEBO_ENABLE, String.valueOf(enable));
	}
	
	public void setQqWeboID(String id) {
		getAttr().put(QQWEBO_ID, id);
	}
	
	public void setQqWeboKey(String key) {
		getAttr().put(QQWEBO_KEY, key);
	}

	public void setWeixinLoginId(String id) {
		getAttr().put(WEIXIN_ID, id);
	}
	
	public void setWeixinLoginSecret(String key) {
		getAttr().put(WEIXIN_KEY, key);
	}
	
	public void setWeixinEnable(Boolean enable) {
		getAttr().put(WEIXIN_ENABLE, String.valueOf(enable));
	}
	public void setWeixinAppId(String weixinAppId) {
		getAttr().put(WEIXIN_APP_ID, weixinAppId);
	}
	
	public void setWeixinAppSecret(String weixinAppSecret) {
		getAttr().put(WEIXIN_APP_SECRET, weixinAppSecret);
	}
	
	public String getWeixinLoginId() {
		return getAttr().get(WEIXIN_ID);
	}
	
	public String getWeixinLoginSecret() {
		return getAttr().get(WEIXIN_KEY);
	}
	
	public Boolean getWeixinEnable() {
		String enable = getAttr().get(WEIXIN_ENABLE);
		return !"false".equals(enable);
	}
	public String getWeixinAppId() {
		return getAttr().get(WEIXIN_APP_ID);
	}
	
	public String getWeixinAppSecret() {
		return getAttr().get(WEIXIN_APP_SECRET);
	}
	
	public Boolean getAutoChangeGroup() {
		String enable = getAttr().get(AUTOCHANGE_GROUP);
		return !"false".equals(enable);
	}
	
	public Boolean getServiceExpirationEmailNotice() {
		String enable = getAttr().get(SERVICE_EXPIRATION_EMAIL_NOTICE);
		return !"false".equals(enable);
	}
	
	public String getServiceExpirationEmailNoticeCount() {
		return getAttr().get(SERVICE_EXPIRATION_EMAIL_NOTICE_COUNT);
	}
	
	public String getChangeGroup() {
		return getAttr().get(CHANGE_GROUP);
	}
	
	public void setAutoChangeGroup(Boolean auto) {
		getAttr().put(AUTOCHANGE_GROUP, String.valueOf(auto));
	}
	
	public void setChangeGroup(String groupId) {
		getAttr().put(CHANGE_GROUP, groupId);
	}
	
	public void setServiceExpirationEmailNotice(Boolean notice) {
		getAttr().put(SERVICE_EXPIRATION_EMAIL_NOTICE, String.valueOf(notice));
	}
	
	public void setServiceExpirationEmailNoticeCount(String count) {
		getAttr().put(SERVICE_EXPIRATION_EMAIL_NOTICE_COUNT, count);
	}
	
	public Boolean getSensitivityInputOn() {
		String enable = getAttr().get(SENSITIVITY_INPUT_ON);
		return !"false".equals(enable);
	}
	
	public void setSensitivityInputOn(Boolean on) {
		getAttr().put(SENSITIVITY_INPUT_ON, String.valueOf(on));
	}
	
	public Boolean getReportMsgAuto() {
		String enable = getAttr().get(REPORT_MSG_AUTO);
		return !"false".equals(enable);
	}
	
	public void setReportMsgAuto(Boolean on) {
		getAttr().put(REPORT_MSG_AUTO, String.valueOf(on));
	}
	
	public String getReportMsgTxt() {
		return getAttr().get(REPORT_MSG_TXT);
	}
	
	public void setReportMsgTxt(String txt) {
		getAttr().put(REPORT_MSG_TXT, txt);
	}
	
	public Boolean getLiveCheck() {
		String check = getAttr().get(LIVE_CHECK);
		return !"false".equals(check);
	}
	
	public void setLiveCheck(Boolean check) {
		getAttr().put(LIVE_CHECK, String.valueOf(check));
	}
	
	public void setAdDayCharge(String adDayCharge) {
		getAttr().put(AD_DAY_CHARGE, adDayCharge);
	}
	
	public Double getAdDayCharge() {
		String adDayCharge = getAttr().get(AD_DAY_CHARGE);
		if(StrUtils.checkFloat(adDayCharge, "0+")){
			return Double.parseDouble(adDayCharge);
		}else{
			return 0d;
		}
	}
	
	public void setAdClickCharge(String adClickCharge) {
		getAttr().put(AD_CLICK_CHARGE, adClickCharge);
	}
	
	public Double getAdClickCharge() {
		String adClickCharge = getAttr().get(AD_CLICK_CHARGE);
		if(StrUtils.checkFloat(adClickCharge, "0+")){
			return Double.parseDouble(adClickCharge);
		}else{
			return 0d;
		}
	}

	public void setAdDisplayCharge(String adDisplayCharge) {
		getAttr().put(AD_DISPLAY_CHARGE, adDisplayCharge);
	}
	
	public Double getAdDisplayCharge() {
		String adDisplayCharge = getAttr().get(AD_DISPLAY_CHARGE);
		if(StrUtils.checkFloat(adDisplayCharge, "0+")){
			return Double.parseDouble(adDisplayCharge);
		}else{
			return 0d;
		}
	}
	
	public String getAdOrderTitle() {
		return getAttr().get(AD_ORDER_TITLE);
	}
	
	public void setTencentPushFlowKey(String adDisplayCharge) {
		getAttr().put(TENCENT_PUSH_FLOW_KEY, adDisplayCharge);
	}
	
	public String getTencentPushFlowKey() {
		return getAttr().get(TENCENT_PUSH_FLOW_KEY);
	}
	
	public void setTencentApiAuthKey(String authKey) {
		getAttr().put(TENCENT_APIAUTHKEY, authKey);
	}
	
	public String getTencentApiAuthKey() {
		return getAttr().get(TENCENT_APIAUTHKEY);
	}
	
	public void setTencentBizId(String bizId) {
		getAttr().put(TENCENT_BIZID, bizId);
	}
	
	public String getTencentBizId() {
		return getAttr().get(TENCENT_BIZID);
	}
	
	public void setTencentAppId(String tencentAppId) {
		getAttr().put(TENCENT_APPID, tencentAppId);
	}
	
	public String getTencentAppId() {
		return getAttr().get(TENCENT_APPID);
	}
	
	public void setLivePlat(String livePlat) {
		getAttr().put(LIVE_PLAT, livePlat);
	}
	
	public String getLivePlat() {
		return getAttr().get(LIVE_PLAT);
	}
	
	public void setBaiduAccessKeyId(String accessKeyId) {
		getAttr().put(BAIDU_ACCESS_KEY_ID,accessKeyId);
	}
	
	public String getBaiduAccessKeyId() {
		return getAttr().get(BAIDU_ACCESS_KEY_ID);
	}
	
	public void setBaiduPlayDomain(String baiduPlayDomain) {
		getAttr().put(BAIDU_PLAY_DOMAIN,baiduPlayDomain);
	}
	
	public String getBaiduPlayDomain() {
		return getAttr().get(BAIDU_PLAY_DOMAIN);
	}
	
	public void setBaiduPushDomain(String baiduPushDomain) {
		getAttr().put(BAIDU_PUSH_DOMAIN,baiduPushDomain);
	}
	
	public String getBaiduPushDomain() {
		return getAttr().get(BAIDU_PUSH_DOMAIN);
	}
	
	public void setBaiduSecretAccessKey(String baiduSecretAccessKey) {
		getAttr().put(BAIDU_SECRET_ACCESS_KEY,baiduSecretAccessKey);
	}
	
	public String getBaiduSecretAccessKey() {
		return getAttr().get(BAIDU_SECRET_ACCESS_KEY);
	}
	
	public void setBaiduStreamSafeKey(String baiduStreamSafeKey) {
		getAttr().put(BAIDU_STREAM_SAFE_KEY,baiduStreamSafeKey);
	}
	
	public String getBaiduStreamSafeKey() {
		return getAttr().get(BAIDU_STREAM_SAFE_KEY);
	}
	
	public static final String SSO_ENABLE = "ssoEnable";
	public static final String KEEPMINUTE = "keepMinute";
	public static final String DEFAULT_ACTIVE_LEVEL = "defaultActiveLevel";
	public static final String QQ_ENABLE = "qqEnable";
	public static final String QQ_ID = "qqID";
	public static final String QQ_KEY = "qqKey";
	public static final String SINA_ENABLE = "sinaEnable";
	public static final String SINA_ID = "sinaID";
	public static final String SINA_KEY = "sinaKey";
	public static final String QQWEBO_ENABLE = "qqWeboEnable";
	public static final String QQWEBO_ID = "qqWeboID";
	public static final String QQWEBO_KEY = "qqWeboKey";
	public static final String USER_ONLINE_TOP_DAY = "useronlinetopday";
	public static final String USER_ONLINE_TOP_NUM = "useronlinetopnum";
	public static final String AUTOCHANGE_GROUP = "autoChangeGroup";
	public static final String CHANGE_GROUP = "changeGroup";
	public static final String SERVICE_EXPIRATION_EMAIL_NOTICE_COUNT = "expirationEmailNoticeCount";
	public static final String SERVICE_EXPIRATION_EMAIL_NOTICE = "serviceExpirationEmailNotice";
	public static final String WEIXIN_ENABLE = "weixinEnable";
	public static final String WEIXIN_ID = "weixinLoginId";
	public static final String WEIXIN_KEY = "weixinLoginSecret";
	public static final String WEIXIN_APP_ID = "weixinAppId";
	public static final String WEIXIN_APP_SECRET = "weixinAppSecret";
	public static final String SENSITIVITY_INPUT_ON = "sensitivity_input_on";
	public static final String REPORT_MSG_AUTO = "reportMsgAuto";
	public static final String REPORT_MSG_TXT = "reportMsgTxt";
	public static final String LIVE_CHECK = "liveCheck";
	
	public static final String AD_DAY_CHARGE = "adDayCharge";
	public static final String AD_CLICK_CHARGE = "adClickCharge";
	public static final String AD_DISPLAY_CHARGE = "adDisplayCharge";
	public static final String AD_ORDER_TITLE = "adOrderTitle";
	public static final String TENCENT_PUSH_FLOW_KEY = "tencentPushFlowKey";
	public static final String TENCENT_APIAUTHKEY = "tencentApiAuthKey";
	public static final String TENCENT_BIZID = "tencentBizid";
	public static final String TENCENT_APPID = "tencentAppId";
	public static final String LIVE_PLAT = "livePlat";
	
	public static final String BAIDU_PUSH_DOMAIN = "baiduPushDomain";
	public static final String BAIDU_PLAY_DOMAIN = "baiduPlayDomain";
	public static final String BAIDU_SECRET_ACCESS_KEY= "baiduSecretAccessKey";
	public static final String BAIDU_ACCESS_KEY_ID = "baiduAccessKeyId";
	public static final String BAIDU_STREAM_SAFE_KEY= "baiduStreamSafeKey";
	
}
