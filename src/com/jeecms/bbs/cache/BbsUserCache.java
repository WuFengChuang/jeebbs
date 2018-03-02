package com.jeecms.bbs.cache;

import java.util.Date;


/**
 * 用户缓存接口
 */
public interface BbsUserCache {

	/**
	 * 浏览一次
	 * 
	 * @param id
	 *            sessionId
	 * @return 记录最后活动时间
	 */
	public void view(Long sessionId,Date lastActiveTime);
	
	public void refreshToDB();
	
	public void removeCache();
}
