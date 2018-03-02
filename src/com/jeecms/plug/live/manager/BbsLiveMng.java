package com.jeecms.plug.live.manager;

import java.util.Date;
import java.util.List;

import com.jeecms.bbs.entity.BbsOrder;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLive;

public interface BbsLiveMng {
	/**
	 * 列表查询
	 * @param cid 章节id
	 * @param title 标题
	 * @param hostUserId 创建者
	 * @param status 状态  0待审 1审核通过 2未通过 为空则所有
	 * @param timeBegin 开始时间大于
	 * @param timeEnd  开始时间小于 
	 * @param liveEndTimeBegin 结束时间大于
	 * @param liveEndTimeEnd  结束时间小于 
	 * @param orderBy 排序 1开始时间降序 2总收益降序 3ID降序 4参与人数降序 5限制人数降序
	 * 					 6开始时间升序 7总收益升序 8ID升序 9参与人数升序 10限制人数升序
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getPage(Integer cid,String title,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd,Integer orderBy,
			int pageNo, int pageSize);
	
	public List<BbsLive> getList(Integer cid,String title,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd,Integer orderBy,
			Integer first, Integer count);
	
	public Long findLiveCount(Integer cid,Integer hostUserId,
			Short status,Date timeBegin,Date timeEnd,
			Date liveEndTimeBegin,Date liveEndTimeEnd);
	
	public BbsOrder liveOrder(BbsLive live, Integer num,Integer orderType,
			String orderNumber,String outOrderNum,Integer buyUserId,boolean selfOnly);

	public BbsLive findById(Integer id);

	public BbsLive save(BbsLive bean);

	public BbsLive update(BbsLive bean);

	public BbsLive deleteById(Integer id);
	
	public BbsLive[] deleteByIds(Integer[] ids);

	/**
	 * 清空用户实时人数
	 */
	public void clearLiveUserNum();
	
	public int sessionConnect(Integer liveId,boolean isClosed);
	
}