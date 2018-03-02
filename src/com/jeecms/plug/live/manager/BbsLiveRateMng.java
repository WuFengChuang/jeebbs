package com.jeecms.plug.live.manager;

import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLiveRate;

public interface BbsLiveRateMng {
	public Pagination getPage(int pageNo, int pageSize);
	
	public BbsLiveRate findByNearLimitNum(Integer userNum);

	public BbsLiveRate findById(Integer id);

	public BbsLiveRate save(BbsLiveRate bean);

	public BbsLiveRate update(BbsLiveRate bean);

	public BbsLiveRate deleteById(Integer id);
	
	public BbsLiveRate[] deleteByIds(Integer[] ids);
}