package com.jeecms.bbs.manager;

import java.util.Date;
import java.util.List;

import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicCharge;
import com.jeecms.common.page.Pagination;

public interface BbsTopicChargeMng {
	
	public List<BbsTopicCharge> getList(String title,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,int orderBy,Integer first,Integer count);
	
	public Pagination getPage(String title,Integer authorUserId,
			Date buyTimeBegin,Date buyTimeEnd,
			int orderBy,int pageNo,int pageSize);
	
	public BbsTopicCharge save(Double chargeAmount, Short charge,
			Boolean rewardPattern,Double rewardRandomMin,Double rewardRandomMax,
			BbsTopic topic);
	
	public void afterContentUpdate(BbsTopic  bean,Short charge,Double chargeAmount,
			Boolean rewardPattern,Double rewardRandomMin,Double rewardRandomMax);

	public BbsTopicCharge update(BbsTopicCharge charge);
	
	public BbsTopicCharge afterUserPay(Double payAmout,BbsTopic topic);
}