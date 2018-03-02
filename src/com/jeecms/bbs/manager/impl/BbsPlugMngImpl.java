package com.jeecms.bbs.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsPlugDao;
import com.jeecms.bbs.entity.BbsPlug;
import com.jeecms.bbs.manager.BbsPlugMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

@Service
@Transactional
public class BbsPlugMngImpl implements BbsPlugMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	public List<BbsPlug> getList(String author,Boolean used){
		return dao.getList(author,used);
	}

	@Transactional(readOnly = true)
	public BbsPlug findById(Integer id) {
		BbsPlug entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsPlug findByPath(String plugPath){
		return dao.findByPath(plugPath);
	}

	public BbsPlug save(BbsPlug bean) {
		dao.save(bean);
		return bean;
	}

	public BbsPlug update(BbsPlug bean) {
		Updater<BbsPlug> updater = new Updater<BbsPlug>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public BbsPlug deleteById(Integer id) {
		BbsPlug bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsPlug[] deleteByIds(Integer[] ids) {
		BbsPlug[] beans = new BbsPlug[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsPlugDao dao;

	@Autowired
	public void setDao(BbsPlugDao dao) {
		this.dao = dao;
	}
}