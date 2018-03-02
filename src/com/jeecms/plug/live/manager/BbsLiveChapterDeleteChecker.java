package com.jeecms.plug.live.manager;

/**
 * 检查栏目是否可以删除的接口
 */
public interface BbsLiveChapterDeleteChecker {
	/**
	 * 如不能删除，则返回国际化提示信息；否则返回null。
	 */
	public String checkForChapterDelete(Integer chapterId);
}
