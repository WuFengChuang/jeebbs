package com.jeecms.bbs.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsAdvertisingSpaceDao;
import com.jeecms.bbs.entity.BbsAdvertisingSpace;
import com.jeecms.bbs.manager.BbsAdvertisingSpaceMng;
import com.jeecms.common.hibernate4.Updater;

@Service
@Transactional
public class BbsAdvertisingSpaceMngImpl implements BbsAdvertisingSpaceMng {
	@Transactional(readOnly = true)
	public List<BbsAdvertisingSpace> getList(Integer siteId) {
		return dao.getList(siteId);
	}

	@Transactional(readOnly = true)
	public BbsAdvertisingSpace findById(Integer id) {
		BbsAdvertisingSpace entity = dao.findById(id);
		return entity;
	}

	public BbsAdvertisingSpace save(BbsAdvertisingSpace bean) {
		dao.save(bean);
		return bean;
	}

	public BbsAdvertisingSpace update(BbsAdvertisingSpace bean) {
		Updater<BbsAdvertisingSpace> updater = new Updater<BbsAdvertisingSpace>(
				bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public BbsAdvertisingSpace deleteById(Integer id) {
		BbsAdvertisingSpace bean = dao.deleteById(id);
		return bean;
	}

	public BbsAdvertisingSpace[] deleteByIds(Integer[] ids) {
		BbsAdvertisingSpace[] beans = new BbsAdvertisingSpace[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsAdvertisingSpaceDao dao;

	@Autowired
	public void setDao(BbsAdvertisingSpaceDao dao) {
		this.dao = dao;
	}
}