package com.jeecms.bbs.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecms.bbs.dao.BbsForumDao;
import com.jeecms.bbs.entity.BbsCategory;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsForumExt;
import com.jeecms.bbs.manager.BbsCategoryMng;
import com.jeecms.bbs.manager.BbsForumExtMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.core.entity.CmsSite;

@Service
@Transactional
public class BbsForumMngImpl implements BbsForumMng {

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public List<BbsForum> getList(Integer siteId) {
		return dao.getList(siteId);
	}

	@Transactional(readOnly = true)
	public List<BbsForum> getList(Integer siteId, Integer categoryId,Integer groupId) {
		List<BbsForum>list= dao.getList(siteId, categoryId);
		//查询用户组拥有发帖权限的板块
		if(groupId!=null){
			List<BbsForum>topicForumlist=new ArrayList<BbsForum>();
			for(BbsForum f:list){
				if(StringUtils.isNotBlank(f.getGroupTopics())&&
						f.getGroupTopics().contains(","+groupId+",")){
					topicForumlist.add(f);
				}
			}
			return topicForumlist;
		}
		return list;
	}

	@Transactional(readOnly = true)
	public int countPath(Integer siteId, String path) {
		return dao.countPath(siteId, path);
	}

	@Transactional(readOnly = true)
	public BbsForum getByPath(Integer siteId, String path) {
		return dao.getByPath(siteId, path);
	}

	@Transactional(readOnly = true)
	public BbsForum findById(Integer id) {
		BbsForum entity = dao.findById(id);
		return entity;
	}

	public BbsForum save(BbsForum bean, BbsForumExt ext,
			Integer categoryId, CmsSite site,
			Integer[] views, Integer[] topics, Integer[] replies) {
		BbsCategory category = bbsCategoryMng.findById(categoryId);
		bean.setCategory(category);
		bean.setSite(site);
		if (views != null && views.length > 0) {
			String v = ",";
			for (Integer vgroupId : views) {
				v = v + vgroupId + ",";
			}
			bean.setGroupViews(v);
		}
		if (topics != null && topics.length > 0) {
			String t = ",";
			for (Integer tgroupId : topics) {
				t = t + tgroupId + ",";
			}
			bean.setGroupTopics(t);
		}
		if (replies != null && replies.length > 0) {
			String r = ",";
			for (Integer rgroupId : replies) {
				r = r + rgroupId + ",";
			}
			bean.setGroupReplies(r);
		}
		bean.init();
		bean=dao.save(bean);
		forumExtMng.save(ext, bean);
		return bean;
	}

	public BbsForum save(BbsForum bean) {
		dao.save(bean);
		return bean;
	}

	public BbsForum update(BbsForum bean) {
		Updater<BbsForum> updater = new Updater<BbsForum>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public BbsForum update(BbsForum bean,BbsForumExt ext,
			Integer categoryId, CmsSite site,
			Integer[] views, Integer[] topics, Integer[] replies) {
		Updater<BbsForum> updater = new Updater<BbsForum>(bean);
		bean = dao.updateByUpdater(updater);
		if(ext!=null){
			forumExtMng.update(ext);
		}
		BbsCategory category = bbsCategoryMng.findById(categoryId);
		bean.setCategory(category);
		bean.setSite(site);
		if (views != null && views.length > 0) {
			String v = ",";
			for (Integer vgroupId : views) {
				v = v + vgroupId + ",";
			}
			bean.setGroupViews(v);
		} else {
			bean.setGroupViews("");
		}
		if (topics != null && topics.length > 0) {
			String t = ",";
			for (Integer tgroupId : topics) {
				t = t + tgroupId + ",";
			}
			bean.setGroupTopics(t);
		} else {
			bean.setGroupTopics("");
		}
		if (replies != null && replies.length > 0) {
			String r = ",";
			for (Integer rgroupId : replies) {
				r = r + rgroupId + ",";
			}
			bean.setGroupReplies(r);
		} else {
			bean.setGroupReplies("");
		}
		bean.init();
		return bean;
	}
	
	public BbsForum update(BbsForum bean,BbsForumExt ext,
			Integer categoryId) {
		Updater<BbsForum> updater = new Updater<BbsForum>(bean);
		bean = dao.updateByUpdater(updater);
		if(ext!=null){
			forumExtMng.update(ext);
		}
		BbsCategory category = bbsCategoryMng.findById(categoryId);
		bean.setCategory(category);
		return bean;
	}
	
	public void update(Integer groupId,Integer[]forumIds,Integer[] views, Integer[] topics, Integer[] replies) {
		List<Integer> unSelectForumIds=getUnselectForumId(forumIds, views);
		for(int i=0;i<unSelectForumIds.size();i++){
			BbsForum bean=findById(unSelectForumIds.get(i));
			if(StringUtils.isNotBlank(bean.getGroupViews())){
				if(bean.getGroupViews().contains(","+groupId+",")){
					bean.setGroupViews(bean.getGroupViews().replace(","+groupId+",", ""));
				}	
			}
		}
		if (views != null && views.length > 0) {
			for (Integer forumId : views) {
				BbsForum bean=findById(forumId);
				if(StringUtils.isNotBlank(bean.getGroupViews())){
					if(!bean.getGroupViews().contains(","+groupId+",")){
						bean.setGroupViews(bean.getGroupViews()+","+groupId+",");
					}
				}else{
					bean.setGroupViews(bean.getGroupViews()+","+groupId+",");
				}
			}
		}
		List<Integer> unSelectTopicForumIds=getUnselectForumId(forumIds, topics);
		for(int i=0;i<unSelectTopicForumIds.size();i++){
			BbsForum bean=findById(unSelectTopicForumIds.get(i));
			if (StringUtils.isNotBlank(bean.getGroupTopics())) {
				if(bean.getGroupTopics().contains(","+groupId+",")){
					bean.setGroupTopics(bean.getGroupTopics().replace(","+groupId+",", ""));
				}
			}
			
		}
		if (topics != null && topics.length > 0) {
			for (Integer forumId : topics) {
				BbsForum bean=findById(forumId);
				if (StringUtils.isNotBlank(bean.getGroupTopics())) {
					if(!bean.getGroupTopics().contains(","+groupId+",")){
						bean.setGroupTopics(bean.getGroupTopics()+","+groupId+",");
					}
				}else{
					bean.setGroupTopics(bean.getGroupTopics()+","+groupId+",");
				}
			}
		}
		List<Integer> unSelectReplyForumIds=getUnselectForumId(forumIds, replies);
		for(int i=0;i<unSelectReplyForumIds.size();i++){
			BbsForum bean=findById(unSelectReplyForumIds.get(i));
			if (StringUtils.isNotBlank(bean.getGroupReplies())) {
				if(bean.getGroupReplies().contains(","+groupId+",")){
					bean.setGroupReplies(bean.getGroupReplies().replace(","+groupId+",", ""));
				}
			}
		}
		if (replies != null && replies.length > 0) {
			for (Integer forumId : replies) {
				BbsForum bean=findById(forumId);
				if (StringUtils.isNotBlank(bean.getGroupReplies())) {
					if(!bean.getGroupReplies().contains(","+groupId+",")){
						bean.setGroupReplies(bean.getGroupReplies()+","+groupId+",");
					}
				}else{
					bean.setGroupReplies(bean.getGroupReplies()+","+groupId+",");
				}
			}
		}
	}

	public BbsForum deleteById(Integer id) {
		BbsForum bean = dao.deleteById(id);
		return bean;
	}

	public BbsForum[] deleteByIds(Integer[] ids) {
		BbsForum[] beans = new BbsForum[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public String getModerators(Integer siteId){
		List<BbsForum>forums=getList(siteId);
		StringBuffer moderatorsBuf=new StringBuffer();
		for(BbsForum forum:forums){
			moderatorsBuf.append(forum.getModerators());
		}
		return moderatorsBuf.toString();
	}
	
	private List<Integer>getUnselectForumId(Integer[]forumIds,Integer[]selectIds){
		List<Integer>list=new ArrayList<Integer>();
		if(selectIds==null){
			for(int fid:forumIds){
				list.add(fid);
			}
		}else{
			for(int fid:forumIds){
				for(int sid:selectIds){
					if(fid!=sid){
						list.add(fid);
					}
				}
			}
		}
		return list;
	}

	private BbsCategoryMng bbsCategoryMng;
	private BbsForumDao dao;
	@Autowired
	private BbsForumExtMng forumExtMng;

	@Autowired
	public void setDao(BbsForumDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setBbsCategoryMng(BbsCategoryMng bbsCategoryMng) {
		this.bbsCategoryMng = bbsCategoryMng;
	}

	public void updateAll_topic_today() {
		dao.updateAll_topic_today();
	}

	@Override
	public void update(Integer sitId ,Integer[] groupIds) {
		for (int i = 0; i < groupIds.length; i++) {
			List<BbsForum> forums = getList(sitId);//获取所有的板块信息
			for (BbsForum forum : forums) {//遍历所有的板块
				//判断对应板块是否对要删除的用户组授予权限，若有则将其删除
				if (StringUtils.isNotBlank(forum.getGroupViews())) {
					if (forum.getGroupViews().contains(groupIds[i]+",")) {
						forum.setGroupViews(forum.getGroupViews().replace(groupIds[i]+",", ""));
					}
				}
				if (StringUtils.isNotBlank(forum.getGroupTopics())) {
					if (forum.getGroupTopics().contains(groupIds[i]+",")) {
						forum.setGroupTopics(forum.getGroupTopics().replace(groupIds[i]+",", ""));
					}
				}
				if (StringUtils.isNotBlank(forum.getGroupReplies())) {
					if (forum.getGroupReplies().contains(groupIds[i]+",")) {
						forum.setGroupReplies(forum.getGroupReplies().replace(groupIds[i]+",", ""));
					}
				}
			}
		}
	}
}