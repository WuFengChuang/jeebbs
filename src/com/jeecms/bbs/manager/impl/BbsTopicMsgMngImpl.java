package com.jeecms.bbs.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsTopicMsgDao;
import com.jeecms.bbs.entity.BbsTopicMsg;
import com.jeecms.bbs.manager.BbsTopicMsgMng;

@Service
@Transactional
public class BbsTopicMsgMngImpl implements BbsTopicMsgMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsTopicMsg findById(Integer id) {
		BbsTopicMsg entity = dao.findById(id);
		return entity;
	}

	public BbsTopicMsg save(BbsTopicMsg bean) {
		dao.save(bean);
		return bean;
	}

	public BbsTopicMsg update(BbsTopicMsg bean) {
		Updater<BbsTopicMsg> updater = new Updater<BbsTopicMsg>(bean);
		BbsTopicMsg entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsTopicMsg deleteById(Integer id) {
		BbsTopicMsg bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsTopicMsg[] deleteByIds(Integer[] ids) {
		BbsTopicMsg[] beans = new BbsTopicMsg[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public List<BbsTopicMsg> getTopicList(Integer userId,Integer count) {
		return dao.getTopicList(userId, count);
		
	}

	private BbsTopicMsgDao dao;

	@Autowired
	public void setDao(BbsTopicMsgDao dao) {
		this.dao = dao;
	}
}