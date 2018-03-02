package com.jeecms.bbs.cache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jeecms.bbs.entity.BbsForumCount;
import com.jeecms.bbs.manager.BbsForumCountMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.common.util.DateUtils;


/**
 * 板块计数器缓存实现
 */
@Service
public class ForumCountCacheImpl implements ForumCountCache, DisposableBean {
	
	private enum RefreshForumCountEnum {
		/**
		 * 板块访问数缓存
		 */
		visit,
		/**
		 * 板块帖子数缓存
		 */
		post,
		/**
		 * 板块主题数缓存
		 */
		topic,
		/**
		 *板块全部缓存
		 */
		all
	};

	/**
	 * @see ForumCountCache#viewAndGet(Integer)
	 */
	public int[] viewAndGet(Integer forumId) {
		Date now=Calendar.getInstance().getTime();
		now=DateUtils.parseDateTimeToDay(now);
		BbsForumCount count=forumCountMng.findByForum(forumId, now);
		if (count == null) {
			count=forumCountMng.init(forumId);
			return new int[] { 1 };
		}
		Element e = forumVisitCount.get(count.getId());
		Integer view;
		if (e != null) {
			view = (Integer) e.getObjectValue() + 1;
		} else {
			view = 1;
		}
		forumVisitCount.put(new Element(forumId, view));
		refreshToDB(RefreshForumCountEnum.visit);
		return new int[] { view + count.getVisitCount() };
	}
	
	/**
	 * 发表主题
	 * @param forumId
	 */
	public void addTopic(Integer forumId){
		Date now=Calendar.getInstance().getTime();
		now=DateUtils.parseDateTimeToDay(now);
		BbsForumCount count=forumCountMng.findByForum(forumId, now);
		if (count == null) {
			count=forumCountMng.init(forumId);
		}
		Element e = forumTopicCount.get(count.getId());
		Integer topicCount;
		if (e != null) {
			topicCount = (Integer) e.getObjectValue() + 1;
		} else {
			topicCount = 1;
		}
		forumTopicCount.put(new Element(forumId, topicCount));
		refreshToDB(RefreshForumCountEnum.topic);
	}
	/**
	 * 发表帖子
	 * @param forumId
	 */
	public void addPost(Integer forumId){
		Date now=Calendar.getInstance().getTime();
		now=DateUtils.parseDateTimeToDay(now);
		BbsForumCount count=forumCountMng.findByForum(forumId, now);
		if (count == null) {
			count=forumCountMng.init(forumId);
		}
		Element e = forumPostCount.get(count.getId());
		Integer postCount;
		if (e != null) {
			postCount = (Integer) e.getObjectValue() + 1;
		} else {
			postCount = 1;
		}
		forumPostCount.put(new Element(forumId, postCount));
		refreshToDB(RefreshForumCountEnum.post);
	}


	/**
	 * 销毁BEAN时，缓存入库。
	 */
	public void destroy() throws Exception {
		refreshToDB(RefreshForumCountEnum.all);
	}


	private void refreshToDB(RefreshForumCountEnum e) {
		long time = System.currentTimeMillis();
		Date date=Calendar.getInstance().getTime();
		date=DateUtils.parseDateTimeToDay(date);
			if(e.equals(RefreshForumCountEnum.visit)
					||e.equals(RefreshForumCountEnum.all)){
				if ((time > visitRefreshTime + interval)
						||e.equals(RefreshForumCountEnum.all)) {
					visitRefreshTime = time;
					List<Integer> keys = forumVisitCount.getKeys();
					for (Integer fid : keys) {
						BbsForumCount forumCount = forumCountMng.findByForum(fid, date);
						if(forumCount==null){
							forumCount=forumCountMng.init(fid);
						}
						Element ecache = forumVisitCount.get(fid);
						if(ecache!=null){
							Integer forumVisit = (Integer) ecache.getObjectValue();
							forumCount.setVisitCount(forumCount.getVisitCount()+forumVisit);
							forumCountMng.update(forumCount);
						}
					}
					forumVisitCount.removeAll();
				}
			}
			if(e.equals(RefreshForumCountEnum.post)
					||e.equals(RefreshForumCountEnum.all)){
				if ((time > postRefreshTime + interval)
						||e.equals(RefreshForumCountEnum.all)) {
					postRefreshTime = time;
					List<Integer> keys = forumPostCount.getKeys();
					for (Integer fid : keys) {
						BbsForumCount forumCount = forumCountMng.findByForum(fid, date);
						if(forumCount==null){
							forumCount=forumCountMng.init(fid);
						}
						Element eForumPost = forumPostCount.get(fid);
						Integer forumPost = (Integer) eForumPost.getObjectValue();
						forumCount.setPostCount(forumCount.getPostCount()+forumPost);
						forumCountMng.update(forumCount);
					}
					forumPostCount.removeAll();
				}
			}
			if(e.equals(RefreshForumCountEnum.topic)
					||e.equals(RefreshForumCountEnum.all)){
				if ((time > topicRefreshTime + interval)
						||e.equals(RefreshForumCountEnum.all)) {
					topicRefreshTime = time;
					List<Integer> keys = forumTopicCount.getKeys();
					for (Integer fid : keys) {
						BbsForumCount forumCount = forumCountMng.findByForum(fid, date);
						if(forumCount==null){
							forumCount=forumCountMng.init(fid);
						}
						Element eForumTopic = forumTopicCount.get(fid);
						Integer forumTopic = (Integer) eForumTopic.getObjectValue();
						forumCount.setTopicCount(forumCount.getTopicCount()+forumTopic);
						forumCountMng.update(forumCount);
					}
					forumTopicCount.removeAll();
				}
			}
	}
	
	// 间隔时间
	private int interval = 10 * 60 * 1000; // 10分钟
	// 最后刷新时间
	private long visitRefreshTime = System.currentTimeMillis();
	private long postRefreshTime = System.currentTimeMillis();
	private long topicRefreshTime = System.currentTimeMillis();

	/**
	 * 刷新间隔时间
	 * 
	 * @param interval
	 *            单位分钟
	 */
	public void setInterval(int interval) {
		this.interval = interval * 60 * 1000;
	}
	
	
	@Autowired
	private BbsForumCountMng forumCountMng;
	@Autowired
	private BbsForumMng forumMng;
	private Ehcache forumVisitCount;
	private Ehcache forumTopicCount;
	private Ehcache forumPostCount;
	@Autowired
	public void setForumVisitCount(@Qualifier("forumVisitCount")Ehcache cache) {
		this.forumVisitCount = cache;
	}
	@Autowired
	public void setForumTopicCount(@Qualifier("forumTopicCount")Ehcache cache) {
		this.forumTopicCount = cache;
	}
	@Autowired
	public void setForumPostCountCount(@Qualifier("forumPostCount")Ehcache cache) {
		this.forumPostCount = cache;
	}
}
