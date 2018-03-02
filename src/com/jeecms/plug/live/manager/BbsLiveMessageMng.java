package com.jeecms.plug.live.manager;

import java.io.IOException;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.entity.BbsLiveMessage;

public interface BbsLiveMessageMng {
	public Pagination getPage(Integer liveId,int pageNo, int pageSize);

	public BbsLiveMessage findById(Long id);

	public BbsLiveMessage save(BbsLiveMessage bean);
	
	public BbsLiveMessage speak(BbsLive live,BbsUser user,BbsUser toUser
			,String content,Short msgType);

	public BbsLiveMessage update(BbsLiveMessage bean);

	public BbsLiveMessage deleteById(Long id);
	
	public BbsLiveMessage[] deleteByIds(Long[] ids);
}