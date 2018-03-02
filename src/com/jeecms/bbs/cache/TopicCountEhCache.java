package com.jeecms.bbs.cache;

import com.jeecms.bbs.entity.BbsTopicCountEnum;

public interface TopicCountEhCache {

	public Long getViewCount(Integer topicId);
	
	public Long getViewCount(Integer topicId,BbsTopicCountEnum e);
	
	public Long setViewCount(Integer topicId);

	public boolean getLastReply(Integer userId, long time);

}
