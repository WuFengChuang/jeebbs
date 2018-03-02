package com.jeecms.plug.live.manager.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveMessageDao;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.entity.BbsLiveMessage;
import com.jeecms.plug.live.manager.BbsLiveMessageMng;
import com.jeecms.plug.live.manager.BbsLiveMng;

@Service
@Transactional
public class BbsLiveMessageMngImpl implements BbsLiveMessageMng {
	@Transactional(readOnly = true)
	public Pagination getPage(Integer liveId,int pageNo, int pageSize) {
		Pagination page = dao.getPage(liveId,pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsLiveMessage findById(Long id) {
		BbsLiveMessage entity = dao.findById(id);
		return entity;
	}

	public BbsLiveMessage save(BbsLiveMessage bean) {
		dao.save(bean);
		return bean;
	}
	
	
	public BbsLiveMessage speak(BbsLive live,BbsUser user,BbsUser toUser,
			String content,Short msgType){
		BbsLiveMessage message=new BbsLiveMessage();
		message.setLive(live);
		message.setMsgTime(Calendar.getInstance().getTime());
		if(msgType!=null){
			message.setMsgType(msgType);
		}else{
			message.setMsgType(BbsLiveMessage.MST_TYPE_TEXT);
		}
		message.setToUser(toUser);
		message.setUser(user);
		message.setContent(content);
		return save(message);
	}

	public BbsLiveMessage update(BbsLiveMessage bean) {
		Updater<BbsLiveMessage> updater = new Updater<BbsLiveMessage>(bean);
		BbsLiveMessage entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsLiveMessage deleteById(Long id) {
		BbsLiveMessage bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsLiveMessage[] deleteByIds(Long[] ids) {
		BbsLiveMessage[] beans = new BbsLiveMessage[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsLiveMessageDao dao;
	@Autowired
	private BbsLiveMng liveMng;

	@Autowired
	public void setDao(BbsLiveMessageDao dao) {
		this.dao = dao;
	}
}