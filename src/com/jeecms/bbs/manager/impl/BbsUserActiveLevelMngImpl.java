package com.jeecms.bbs.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsUserActiveLevelDao;
import com.jeecms.bbs.entity.BbsUserActiveLevel;
import com.jeecms.bbs.manager.BbsUserActiveLevelMng;

@Service
@Transactional
public class BbsUserActiveLevelMngImpl implements BbsUserActiveLevelMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsUserActiveLevel> getApiList(Integer first, Integer count) {
		List<BbsUserActiveLevel> list= dao.getApiList(first,count);
		return list;
	}
	
	@Transactional(readOnly = true)
	public List<BbsUserActiveLevel> getList(Integer count){
		return dao.getList(count);
	}

	@Transactional(readOnly = true)
	public BbsUserActiveLevel findById(Integer id) {
		BbsUserActiveLevel entity = dao.findById(id);
		return entity;
	}

	public BbsUserActiveLevel save(BbsUserActiveLevel bean) {
		dao.save(bean);
		return bean;
	}

	public BbsUserActiveLevel update(BbsUserActiveLevel bean) {
		Updater<BbsUserActiveLevel> updater = new Updater<BbsUserActiveLevel>(bean);
		BbsUserActiveLevel entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsUserActiveLevel deleteById(Integer id) {
		BbsUserActiveLevel bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsUserActiveLevel[] deleteByIds(Integer[] ids) {
		BbsUserActiveLevel[] beans = new BbsUserActiveLevel[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsUserActiveLevelDao dao;

	@Autowired
	public void setDao(BbsUserActiveLevelDao dao) {
		this.dao = dao;
	}

}