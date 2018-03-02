package com.jeecms.plug.live.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveRateDao;
import com.jeecms.plug.live.entity.BbsLiveRate;
import com.jeecms.plug.live.manager.BbsLiveRateMng;

@Service
@Transactional
public class BbsLiveRateMngImpl implements BbsLiveRateMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsLiveRate findById(Integer id) {
		BbsLiveRate entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsLiveRate findByNearLimitNum(Integer userNum) {
		BbsLiveRate entity = dao.findByNearLimitNum(userNum);
		if(entity==null){
			entity=dao.findByNearLimitNum(0);
		}
		return entity;
	}

	public BbsLiveRate save(BbsLiveRate bean) {
		dao.save(bean);
		return bean;
	}

	public BbsLiveRate update(BbsLiveRate bean) {
		Updater<BbsLiveRate> updater = new Updater<BbsLiveRate>(bean);
		BbsLiveRate entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsLiveRate deleteById(Integer id) {
		BbsLiveRate bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsLiveRate[] deleteByIds(Integer[] ids) {
		BbsLiveRate[] beans = new BbsLiveRate[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private BbsLiveRateDao dao;

	@Autowired
	public void setDao(BbsLiveRateDao dao) {
		this.dao = dao;
	}
}