package com.jeecms.bbs.manager;

import com.jeecms.common.page.Pagination;

import java.util.List;

import com.jeecms.bbs.entity.BbsTopicPostOperate;

public interface BbsTopicPostOperateMng {
	public Pagination getPage(Short dataType,
			Integer userId,Integer operate,int pageNo, int pageSize);

	public BbsTopicPostOperate findById(Long id);
	
	public List<BbsTopicPostOperate> getList(Integer dateId,Short dateType,
			Integer userId,Integer operate,Integer first,Integer count);
	
	/**
	 * 主题操作 
	 * @param topicId 主题Id
	 * @param userId 用户Id
	 * @param operate 操作类型 0点赞 1收藏 2关注  3取消点赞 4取消收藏 5取消关注 
	 * @return
	 */
	public BbsTopicPostOperate topicOperate(Integer topicId,
			Integer userId,Integer operate);
	
	public BbsTopicPostOperate postOperate(Integer postId,
			Integer userId,Integer operate);

	public BbsTopicPostOperate save(BbsTopicPostOperate bean);

	public BbsTopicPostOperate update(BbsTopicPostOperate bean);

	public BbsTopicPostOperate deleteById(Long id);
	
	public BbsTopicPostOperate[] deleteByIds(Long[] ids);
}