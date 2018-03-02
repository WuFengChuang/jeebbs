package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;
import com.jeecms.bbs.entity.BbsWebserviceCallRecord;

public interface BbsWebserviceCallRecordMng {
	public Pagination getPage(int pageNo, int pageSize);

	public BbsWebserviceCallRecord findById(Integer id);
	
	public BbsWebserviceCallRecord save(String clientUsername,String serviceCode);

	public BbsWebserviceCallRecord save(BbsWebserviceCallRecord bean);

	public BbsWebserviceCallRecord update(BbsWebserviceCallRecord bean);

	public BbsWebserviceCallRecord deleteById(Integer id);
	
	public BbsWebserviceCallRecord[] deleteByIds(Integer[] ids);
}