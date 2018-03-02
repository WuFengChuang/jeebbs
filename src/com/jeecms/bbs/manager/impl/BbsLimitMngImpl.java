package com.jeecms.bbs.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsLimitDao;
import com.jeecms.bbs.entity.BbsLimit;
import com.jeecms.bbs.manager.BbsLimitMng;

@Service
@Transactional
public class BbsLimitMngImpl implements BbsLimitMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsLimit findById(Integer id) {
		BbsLimit entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public boolean ipIsLimit(String ip){
		List<BbsLimit> li=dao.getList(ip, null);
		if(li!=null&&li.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	@Transactional(readOnly = true)
	public boolean userIsLimit(Integer userId){
		List<BbsLimit> li=dao.getList(null, userId);
		if(li!=null&&li.size()>0){
			return true;
		}else{
			return false;
		}
	}

	public BbsLimit save(BbsLimit bean) {
		dao.save(bean);
		return bean;
	}

	public BbsLimit update(BbsLimit bean) {
		Updater<BbsLimit> updater = new Updater<BbsLimit>(bean);
		BbsLimit entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsLimit deleteById(Integer id) {
		BbsLimit bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsLimit[] deleteByIds(Integer[] ids) {
		BbsLimit[] beans = new BbsLimit[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsLimitDao dao;

	@Autowired
	public void setDao(BbsLimitDao dao) {
		this.dao = dao;
	}
}