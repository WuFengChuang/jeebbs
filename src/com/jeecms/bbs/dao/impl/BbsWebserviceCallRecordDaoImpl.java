package com.jeecms.bbs.dao.impl;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.jeecms.common.hibernate4.HibernateBaseDao;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.dao.BbsWebserviceCallRecordDao;
import com.jeecms.bbs.entity.BbsWebserviceCallRecord;

@Repository
public class BbsWebserviceCallRecordDaoImpl extends HibernateBaseDao<BbsWebserviceCallRecord, Integer> implements BbsWebserviceCallRecordDao {
	public Pagination getPage(int pageNo, int pageSize) {
		Criteria crit = createCriteria();
		Pagination page = findByCriteria(crit, pageNo, pageSize);
		return page;
	}

	public BbsWebserviceCallRecord findById(Integer id) {
		BbsWebserviceCallRecord entity = get(id);
		return entity;
	}

	public BbsWebserviceCallRecord save(BbsWebserviceCallRecord bean) {
		getSession().save(bean);
		return bean;
	}

	public BbsWebserviceCallRecord deleteById(Integer id) {
		BbsWebserviceCallRecord entity = super.get(id);
		if (entity != null) {
			getSession().delete(entity);
		}
		return entity;
	}
	
	@Override
	protected Class<BbsWebserviceCallRecord> getEntityClass() {
		return BbsWebserviceCallRecord.class;
	}
}