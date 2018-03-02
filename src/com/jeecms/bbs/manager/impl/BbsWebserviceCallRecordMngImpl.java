package com.jeecms.bbs.manager.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsWebserviceCallRecordDao;
import com.jeecms.bbs.entity.BbsWebserviceCallRecord;
import com.jeecms.bbs.manager.BbsWebserviceAuthMng;
import com.jeecms.bbs.manager.BbsWebserviceCallRecordMng;

@Service
@Transactional
public class BbsWebserviceCallRecordMngImpl implements BbsWebserviceCallRecordMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsWebserviceCallRecord findById(Integer id) {
		BbsWebserviceCallRecord entity = dao.findById(id);
		return entity;
	}
	
	public BbsWebserviceCallRecord save(String clientUsername,String serviceCode){
		BbsWebserviceCallRecord record=new BbsWebserviceCallRecord();
		record.setAuth(bbsWebserviceAuthMng.findByUsername(clientUsername));
		record.setRecordTime(Calendar.getInstance().getTime());
		record.setServiceCode(serviceCode);
		return save(record);
	}

	public BbsWebserviceCallRecord save(BbsWebserviceCallRecord bean) {
		dao.save(bean);
		return bean;
	}

	public BbsWebserviceCallRecord update(BbsWebserviceCallRecord bean) {
		Updater<BbsWebserviceCallRecord> updater = new Updater<BbsWebserviceCallRecord>(bean);
		BbsWebserviceCallRecord entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsWebserviceCallRecord deleteById(Integer id) {
		BbsWebserviceCallRecord bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsWebserviceCallRecord[] deleteByIds(Integer[] ids) {
		BbsWebserviceCallRecord[] beans = new BbsWebserviceCallRecord[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	@Autowired
	private BbsWebserviceAuthMng bbsWebserviceAuthMng;
	private BbsWebserviceCallRecordDao dao;

	@Autowired
	public void setDao(BbsWebserviceCallRecordDao dao) {
		this.dao = dao;
	}
}