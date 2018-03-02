package com.jeecms.bbs.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsForumExtDao;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsForumExt;
import com.jeecms.bbs.manager.BbsForumExtMng;
import com.jeecms.common.hibernate4.Updater;

@Service
@Transactional
public class BbsForumExtMngImpl implements BbsForumExtMng {
	public BbsForumExt save(BbsForumExt ext, BbsForum forum) {
		forum.setForumExt(ext);
		ext.setForum(forum);
		ext.init();
		dao.save(ext);
		return ext;
	}

	public BbsForumExt update(BbsForumExt ext) {
		Updater<BbsForumExt> updater = new Updater<BbsForumExt>(ext);
		BbsForumExt entity = dao.updateByUpdater(updater);
		entity.blankToNull();
		return entity;
	}

	private BbsForumExtDao dao;

	@Autowired
	public void setDao(BbsForumExtDao dao) {
		this.dao = dao;
	}
}