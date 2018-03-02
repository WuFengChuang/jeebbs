package com.jeecms.bbs.manager;

import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsForumExt;

public interface BbsForumExtMng {
	public BbsForumExt save(BbsForumExt ext, BbsForum forum);

	public BbsForumExt update(BbsForumExt ext);
}