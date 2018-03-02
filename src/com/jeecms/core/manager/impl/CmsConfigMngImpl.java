package com.jeecms.core.manager.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.core.dao.CmsConfigDao;
import com.jeecms.core.entity.BbsConfigAttr;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.manager.CmsConfigMng;

@Service
@Transactional
public class CmsConfigMngImpl implements CmsConfigMng {
	@Transactional(readOnly = true)
	public CmsConfig get() {
		CmsConfig entity = dao.findById(1);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public Map<String,String> getAttr(){
		return get().getAttr();
	}

	public void updateCountCopyTime(Date d) {
		dao.findById(1).setCountCopyTime(d);
	}

	public void updateCountClearTime(Date d) {
		dao.findById(1).setCountClearTime(d);
	}

	public CmsConfig update(CmsConfig bean) {
		Updater<CmsConfig> updater = new Updater<CmsConfig>(bean);
		CmsConfig entity = dao.updateByUpdater(updater);
		entity.blankToNull();
		return entity;
	}
	
	public void updateConfigAttr(BbsConfigAttr bbsconfigAttr){
		Map<String,String>attrMap=bbsconfigAttr.getAttr();
		if(StringUtils.isBlank(attrMap.get(BbsConfigAttr.WEIXIN_APP_SECRET))){
			attrMap.remove(BbsConfigAttr.WEIXIN_APP_SECRET);
		}
		if(StringUtils.isBlank(attrMap.get(BbsConfigAttr.WEIXIN_KEY))){
			attrMap.remove(BbsConfigAttr.WEIXIN_KEY);
		}
		if(StringUtils.isBlank(attrMap.get(BbsConfigAttr.QQWEBO_KEY))){
			attrMap.remove(BbsConfigAttr.QQWEBO_KEY);
		}
		if(StringUtils.isBlank(attrMap.get(BbsConfigAttr.SINA_KEY))){
			attrMap.remove(BbsConfigAttr.SINA_KEY);
		}
		if(StringUtils.isBlank(attrMap.get(BbsConfigAttr.QQ_KEY))){
			attrMap.remove(BbsConfigAttr.QQ_KEY);
		}
		if(StringUtils.isBlank(attrMap.get(BbsConfigAttr.TENCENT_APIAUTHKEY))){
			attrMap.remove(BbsConfigAttr.TENCENT_APIAUTHKEY);
		}
		if(StringUtils.isBlank(attrMap.get(BbsConfigAttr.TENCENT_APPID))){
			attrMap.remove(BbsConfigAttr.TENCENT_APPID);
		}
		if(StringUtils.isBlank(attrMap.get(BbsConfigAttr.TENCENT_BIZID))){
			attrMap.remove(BbsConfigAttr.TENCENT_BIZID);
		}
		if(StringUtils.isBlank(attrMap.get(BbsConfigAttr.TENCENT_PUSH_FLOW_KEY))){
			attrMap.remove(BbsConfigAttr.TENCENT_PUSH_FLOW_KEY);
		}
		if(StringUtils.isBlank(attrMap.get(BbsConfigAttr.BAIDU_ACCESS_KEY_ID))){
			attrMap.remove(BbsConfigAttr.BAIDU_ACCESS_KEY_ID);
		}
		if(StringUtils.isBlank(attrMap.get(BbsConfigAttr.BAIDU_SECRET_ACCESS_KEY))){
			attrMap.remove(BbsConfigAttr.BAIDU_SECRET_ACCESS_KEY);
		}
		if(StringUtils.isBlank(attrMap.get(BbsConfigAttr.BAIDU_STREAM_SAFE_KEY))){
			attrMap.remove(BbsConfigAttr.BAIDU_STREAM_SAFE_KEY);
		}
		get().getAttr().putAll(attrMap);
		//get().getAttr().putAll(bbsconfigAttr.getAttr());
	}
	
	public void updateSsoAttr(Map<String,String> ssoAttr){
		Map<String,String> oldAttr=get().getAttr();
		Iterator<String> keys = oldAttr.keySet().iterator();
	    String key = null;
	    while (keys.hasNext()) {
		    key = (String) keys.next();
		    if (key.startsWith("sso_")){
		      keys.remove();
		     }
	    }
		oldAttr.putAll(ssoAttr);
	}
	
	
	public void updateRewardFixAttr(Map<String,String> fixAttr){
		Map<String,String> oldAttr=get().getAttr();
		Iterator<String> keys = oldAttr.keySet().iterator();
	    String key = null;
	    while (keys.hasNext()) {
		    key = (String) keys.next();
		    if (key.startsWith(CmsConfig.REWARD_FIX_PREFIX)){
		      keys.remove();
		     }
	    }
		oldAttr.putAll(fixAttr);
	}

	private CmsConfigDao dao;

	@Autowired
	public void setDao(CmsConfigDao dao) {
		this.dao = dao;
	}
}