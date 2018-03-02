package com.jeecms.plug.live.manager.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.plug.live.dao.BbsLiveChapterDao;
import com.jeecms.plug.live.entity.BbsLiveChapter;
import com.jeecms.plug.live.manager.BbsLiveChapterDeleteChecker;
import com.jeecms.plug.live.manager.BbsLiveChapterMng;

@Service
@Transactional
public class BbsLiveChapterMngImpl implements BbsLiveChapterMng {
	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}
	
	@Transactional(readOnly = true)
	public List<BbsLiveChapter> getTopList(Integer userId){
		return dao.getTopList(userId, true);
	}
	
	@Transactional(readOnly = true)
	public List<BbsLiveChapter> getChildList(Integer userId,Integer parentId){
		return dao.getChildList(userId,parentId, true);
	}

	@Transactional(readOnly = true)
	public BbsLiveChapter findById(Integer id) {
		BbsLiveChapter entity = dao.findById(id);
		return entity;
	}
	
	@Transactional(readOnly = true)
	public BbsLiveChapter findByPath(Integer userId,String path){
		return dao.findByPath(userId,path, false);
	}

	public BbsLiveChapter save(BbsLiveChapter bean,Integer parentId) {
		if (parentId != null) {
			bean.setParent(findById(parentId));
		}
		bean=dao.save(bean);
		return bean;
	}

	public BbsLiveChapter update(BbsLiveChapter bean,Integer parentId) {
		Updater<BbsLiveChapter> updater = new Updater<BbsLiveChapter>(bean);
		BbsLiveChapter entity = dao.updateByUpdater(updater);
		BbsLiveChapter parent;
		if (parentId != null) {
			parent = findById(parentId);
		} else {
			parent = null;
		}
		entity.setParent(parent);
		return entity;
	}

	public BbsLiveChapter deleteById(Integer id) {
		BbsLiveChapter bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsLiveChapter[] deleteByIds(Integer[] ids) {
		BbsLiveChapter[] beans = new BbsLiveChapter[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public String checkDelete(Integer chapterId) {
		String msg = null;
		for (BbsLiveChapterDeleteChecker checker : deleteCheckerList) {
			msg = checker.checkForChapterDelete(chapterId);
			if (msg != null) {
				return msg;
			}
		}
		return msg;
	}
	
	public BbsLiveChapter[] updatePriority(Integer[] ids, Integer[] priority){
		int len = ids.length;
		BbsLiveChapter[] beans = new BbsLiveChapter[len];
		for (int i = 0; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setPriority(priority[i]);
		}
		return beans;
	}
	
	private List<BbsLiveChapterDeleteChecker> deleteCheckerList;

	public void setDeleteCheckerList(
			List<BbsLiveChapterDeleteChecker> deleteCheckerList) {
		this.deleteCheckerList = deleteCheckerList;
	}

	private BbsLiveChapterDao dao;

	@Autowired
	public void setDao(BbsLiveChapterDao dao) {
		this.dao = dao;
	}
}