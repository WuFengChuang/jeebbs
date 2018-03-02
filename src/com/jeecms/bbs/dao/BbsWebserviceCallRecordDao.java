package com.jeecms.bbs.dao;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsWebserviceCallRecord;

public interface BbsWebserviceCallRecordDao {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsWebserviceCallRecord findById(Integer id);

	public BbsWebserviceCallRecord save(BbsWebserviceCallRecord bean);

	public BbsWebserviceCallRecord updateByUpdater(Updater<BbsWebserviceCallRecord> updater);

	public BbsWebserviceCallRecord deleteById(Integer id);
}