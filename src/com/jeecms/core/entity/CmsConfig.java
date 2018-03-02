package com.jeecms.core.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.common.util.DateUtils;
import com.jeecms.core.entity.base.BaseCmsConfig;

public class CmsConfig extends BaseCmsConfig {
	private static final long serialVersionUID = 1L;
	
	public static final String REWARD_FIX_PREFIX = "reward_fix_";
	
	public JSONObject convertToJson() throws JSONException{
		JSONObject json = new JSONObject();
		if (getId()!=null) {
			json.put("id", getId());
		}else{
			json.put("id", "");
		}
		if (StringUtils.isNotBlank(getContextPath())) {
			json.put("contextPath", getContextPath());
		}else{
			json.put("contextPath", "");
		}
		if (getPort()!=null) {
			json.put("port", getPort());
		}else{
			json.put("port", "");
		}
		if (StringUtils.isNotBlank(getDbFileUri())) {
			json.put("dbFileUri", getDbFileUri());
		}else{
			json.put("dbFileUri", "");
		}
		if (getUploadToDb()!=null) {
			json.put("uploadToDb", getUploadToDb());
		}else{
			json.put("uploadToDb", "");
		}
		if (StringUtils.isNotBlank(getDefImg())) {
			json.put("defImg", getDefImg());
		}else{
			json.put("defImg", "");
		}
		if (StringUtils.isNotBlank(getAllowSuffix())) {
			json.put("allowSuffix", getAllowSuffix());
		}else{
			json.put("allowSuffix", "");
		}
		return json;
	}
	
	public BbsConfigAttr getConfigAttr() {
		BbsConfigAttr configAttr = new BbsConfigAttr(getAttr());
		return configAttr;
	}
	

	public void setConfigAttr(BbsConfigAttr configAttr) {
		getAttr().putAll(configAttr.getAttr());
	}
	
	public Boolean getSsoEnable(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getSsoEnable();
	}
	
	public Map<String,String> getSsoAttr() {
		Map<String,String>ssoMap=new HashMap<String, String>();
		Map<String,String>attr=getAttr();
		for(String ssoKey:attr.keySet()){
			if(ssoKey.startsWith("sso_")){
				ssoMap.put(ssoKey, attr.get(ssoKey));
			}
		}
		return ssoMap;
	}
	
	public List<String> getSsoAuthenticateUrls() {
		Map<String,String>ssoMap=getSsoAttr();
		List<String>values=new ArrayList<String>();
		for(String key:ssoMap.keySet()){
			values.add(ssoMap.get(key));
		}
		return values;
	}
	
	public Map<String,String> getRewardFixAttr() {
		Map<String,String>attrMap=new HashMap<String, String>();
		Map<String,String>attr=getAttr();
		for(String fixKey:attr.keySet()){
			if(fixKey.startsWith(REWARD_FIX_PREFIX)){
				attrMap.put(fixKey, attr.get(fixKey));
			}
		}
		return attrMap;
	}
	
	public Object[] getRewardFixValues() {
		Map<String,String>attrMap=getRewardFixAttr();
		Collection<String>fixStrings=attrMap.values();
		return fixStrings.toArray();
	}
	
	public Boolean getQqEnable(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getQqEnable();
	}
	
	public Boolean getSinaEnable(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getSinaEnable();
	}
	
	public Boolean getQqWeboEnable(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getQqWeboEnable();
	}
	
	public String getQqID(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getQqID();
	}
	
	public String getSinaID(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getSinaID();
	}
	
	public String getQqWeboID(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getQqWeboID();
	}
	
	public String getWeixinAppId(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getWeixinAppId();
	}
	
	public String getWeixinAppSecret(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getWeixinAppSecret();
	}
	
	public Boolean getWeixinLoginEnable(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getWeixinEnable();
	}
	
	public String getWeixinLoginId(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getWeixinLoginId();
	}
	
	public String getWeixinLoginSecret(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getWeixinLoginSecret();
	}
	
	public Integer getDefaultActiveLevel(){
		BbsConfigAttr configAttr=getConfigAttr();
		String defaultActiveLevel= configAttr.getDefaultActiveLevel();
		int defaultActiveLevelId=1;
		try {
			defaultActiveLevelId=Integer.parseInt(defaultActiveLevel);
		} catch (Exception e) {
		}
		return defaultActiveLevelId;
	}
	
	public int getKeepMinute(){
		BbsConfigAttr configAttr=getConfigAttr();
		String keep= configAttr.getKeepMinute();
		int keepMinut=10;
		try {
			keepMinut=Integer.parseInt(keep);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return keepMinut;
	}
	
	public int getUserOnlineTopNum(){
		BbsConfigAttr configAttr=getConfigAttr();
		String top= configAttr.getUserOnlineTopNum();
		int topNum=0;
		try {
			topNum=Integer.parseInt(top);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return topNum;
	}
	
	public Boolean getAutoChangeGroup(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getAutoChangeGroup();
	}
	
	
	public Boolean getServiceExpirationEmailNotice(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getServiceExpirationEmailNotice();
	}
	
	public Integer getServiceExpirationEmailNoticeCount(){
		BbsConfigAttr configAttr=getConfigAttr();
		String noticeCount= configAttr.getServiceExpirationEmailNoticeCount();
		int emailNoticeCount=0;
		try {
			emailNoticeCount=Integer.parseInt(noticeCount);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return emailNoticeCount;
	}
	
	public Integer getChangeToGroupId(){
		BbsConfigAttr configAttr=getConfigAttr();
		String changeToGroup= configAttr.getChangeGroup();
		int changeToGroupId=1;
		try {
			changeToGroupId=Integer.parseInt(changeToGroup);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return changeToGroupId;
	}
	
	public String getUserOnlineTopDay(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getUserOnlineTopDay();
	}
	

	public Boolean getSensitivityInputOn(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getSensitivityInputOn();
	}
	
	public Boolean getReportMsgAuto(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getReportMsgAuto();
	}
	
	public String getReportMsgTxt(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getReportMsgTxt();
	}
	
	public Boolean getLiveCheck(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getLiveCheck();
	}
	
	public Double getAdDayCharge(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getAdDayCharge();
	}
	
	public Double getAdClickCharge(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getAdClickCharge();
	}
	
	public Double getAdDisplayCharge(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getAdDisplayCharge();
	}
	
	public String getAdOrderTitle(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getAdOrderTitle();
	}
	
	public String getTencentApiAuthKey(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getTencentApiAuthKey();
	}
	
	public String getTencentBizId(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getTencentBizId();
	}
	
	public String getTencentPushFlowKey(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getTencentPushFlowKey();
	}
	
	public String getTencentAppId(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getTencentAppId();
	}
	
	public String getLivePlat(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getLivePlat();
	}
	
	public String getBaiduPlayDomain(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getBaiduPlayDomain();
	}
	
	public String getBaiduPushDomain(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getBaiduPushDomain();
	}
	
	public String getBaiduSecretAccessKey(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getBaiduSecretAccessKey();
	}
	
	public String getBaiduAccessKeyId(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getBaiduAccessKeyId();
	}
	
	public String getBaiduStreamSafeKey(){
		BbsConfigAttr configAttr=getConfigAttr();
		return configAttr.getBaiduStreamSafeKey();
	}
	
	public void blankToNull() {
		// oracle varchar2把空串当作null处理，这里为了统一这个特征，特做此处理。
		if (StringUtils.isBlank(getProcessUrl())) {
			setProcessUrl(null);
		}
		if (StringUtils.isBlank(getContextPath())) {
			setContextPath(null);
		}
		if (StringUtils.isBlank(getServletPoint())) {
			setServletPoint(null);
		}
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CmsConfig () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CmsConfig (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CmsConfig (
		java.lang.Integer id,
		java.lang.String dbFileUri,
		java.lang.Boolean uploadToDb,
		java.lang.String defImg,
		java.lang.String loginUrl,
		java.util.Date countClearTime,
		java.util.Date countCopyTime,
		java.lang.String downloadCode,
		java.lang.Integer downloadTime) {

		super (
			id,
			dbFileUri,
			uploadToDb,
			defImg,
			loginUrl,
			countClearTime,
			countCopyTime,
			downloadCode,
			downloadTime);
	}

	/* [CONSTRUCTOR MARKER END] */

}