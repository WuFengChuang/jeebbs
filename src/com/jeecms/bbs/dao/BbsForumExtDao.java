package com.jeecms.bbs.dao;

import com.jeecms.bbs.entity.BbsForumExt;
import com.jeecms.common.hibernate4.Updater;

public interface BbsForumExtDao {
	public BbsForumExt save(BbsForumExt bean);

	public BbsForumExt updateByUpdater(Updater<BbsForumExt> updater);
}