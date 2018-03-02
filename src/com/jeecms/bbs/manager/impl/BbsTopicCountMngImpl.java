package com.jeecms.bbs.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsTopicCountDao;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicCount;
import com.jeecms.bbs.manager.BbsTopicCountMng;

@Service
@Transactional
public class BbsTopicCountMngImpl implements BbsTopicCountMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsTopicCount findById(Integer id) {
		BbsTopicCount entity = dao.findById(id);
		return entity;
	}
	
	public int topicUp(Integer id) {
		BbsTopicCount c = dao.findById(id);
		if (c == null) {
			return 0;
		}
		int count = c.getUps() + 1;
		c.setUps(count);
		return count;
	}
	
	public int topicCancelUp(Integer id) {
		BbsTopicCount c = dao.findById(id);
		if (c == null||c.getUps()<=0) {
			return 0;
		}
		int count = c.getUps() - 1;
		c.setUps(count);
		return count;
	}
	
	public int topicCollect(Integer id) {
		BbsTopicCount c = dao.findById(id);
		if (c == null) {
			return 0;
		}
		int count = c.getCollections() + 1;
		c.setCollections(count);
		return count;
	}
	
	public int topicCancelCollect(Integer id) {
		BbsTopicCount c = dao.findById(id);
		if (c == null||c.getCollections()<=0) {
			return 0;
		}
		int count = c.getCollections() - 1;
		c.setCollections(count);
		return count;
	}
	
	public int topicReward(Integer id) {
		BbsTopicCount c = dao.findById(id);
		if (c == null) {
			return 0;
		}
		int count = c.getRewards() + 1;
		c.setRewards(count);
		return count;
	}
	
	public int topicAttent(Integer id) {
		BbsTopicCount c = dao.findById(id);
		if (c == null) {
			return 0;
		}
		int count = c.getAttentions() + 1;
		c.setAttentions(count);
		return count;
	}
	
	public int topicCancelAttent(Integer id) {
		BbsTopicCount c = dao.findById(id);
		if (c == null||c.getAttentions()<=0) {
			return 0;
		}
		int count = c.getAttentions() - 1;
		c.setAttentions(count);
		return count;
	}
	
	public BbsTopicCount save(BbsTopicCount count, BbsTopic topic) {
		count.setTopic(topic);
		count.init();
		dao.save(count);
		topic.setTopicCount(count);
		return count;
	}

	public BbsTopicCount update(BbsTopicCount bean) {
		Updater<BbsTopicCount> updater = new Updater<BbsTopicCount>(bean);
		BbsTopicCount entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsTopicCount deleteById(Integer id) {
		BbsTopicCount bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsTopicCount[] deleteByIds(Integer[] ids) {
		BbsTopicCount[] beans = new BbsTopicCount[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsTopicCountDao dao;

	@Autowired
	public void setDao(BbsTopicCountDao dao) {
		this.dao = dao;
	}
}