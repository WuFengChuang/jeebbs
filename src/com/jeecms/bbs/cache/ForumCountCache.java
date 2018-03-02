package com.jeecms.bbs.cache;

/**
 * 板块计数器缓存接口
 */
public interface ForumCountCache {

	/**
	 * 浏览一次
	 * 
	 * @param id
	 *            板块ID
	 * @return 返回浏览次数。
	 */
	public int[] viewAndGet(Integer forumId);
	/**
	 * 发表主题
	 * @param forumId
	 */
	public void addTopic(Integer forumId);
	/**
	 * 发表帖子
	 * @param forumId
	 */
	public void addPost(Integer forumId);
}
