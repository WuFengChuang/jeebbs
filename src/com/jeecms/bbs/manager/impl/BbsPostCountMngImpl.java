package com.jeecms.bbs.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsPostCountDao;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsPostCount;
import com.jeecms.bbs.entity.BbsTopicCount;
import com.jeecms.bbs.manager.BbsPostCountMng;

@Service
@Transactional
public class BbsPostCountMngImpl implements BbsPostCountMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsPostCount findById(Integer id) {
		BbsPostCount entity = dao.findById(id);
		return entity;
	}
	
	public BbsPostCount save(BbsPostCount count, BbsPost post) {
		count.setPost(post);
		count.init();
		dao.save(count);
		post.setPostCount(count);
		return count;
	}
	
	public int postUp(Integer id) {
		BbsPostCount c = dao.findById(id);
		if (c == null) {
			return 0;
		}
		int count = c.getUps() + 1;
		c.setUps(count);
		return count;
	}
	
	public int postCancelUp(Integer id) {
		BbsPostCount c = dao.findById(id);
		if (c == null||c.getUps()<=0) {
			return 0;
		}
		int count = c.getUps() - 1;
		c.setUps(count);
		return count;
	}
	
	public int postReply(Integer id) {
		BbsPostCount c = dao.findById(id);
		if (c == null) {
			return 0;
		}
		int count = c.getReplys() + 1;
		c.setReplys(count);
		return count;
	}

	public BbsPostCount update(BbsPostCount bean) {
		Updater<BbsPostCount> updater = new Updater<BbsPostCount>(bean);
		BbsPostCount entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsPostCount deleteById(Integer id) {
		BbsPostCount bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsPostCount[] deleteByIds(Integer[] ids) {
		BbsPostCount[] beans = new BbsPostCount[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsPostCountDao dao;

	@Autowired
	public void setDao(BbsPostCountDao dao) {
		this.dao = dao;
	}
}