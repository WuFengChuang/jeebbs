package com.jeecms.bbs.manager.impl;

import static com.jeecms.bbs.entity.BbsTopic.TOPIC_VOTE;
import static com.jeecms.bbs.entity.BbsTopic.TOPIC_VOTE_SINGLE;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.MagicConstants;
import com.jeecms.bbs.cache.BbsConfigEhCache;
import com.jeecms.bbs.cache.ForumCountCache;
import com.jeecms.bbs.dao.BbsTopicDao;
import com.jeecms.bbs.entity.BbsCommonMagic;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsSession;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicCount;
import com.jeecms.bbs.entity.BbsTopicText;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsVoteItem;
import com.jeecms.bbs.entity.BbsVoteTopic;
import com.jeecms.bbs.entity.BbsTopicCountEnum;
import com.jeecms.bbs.entity.BbsTopicPostOperate;
import com.jeecms.bbs.entity.BbsVoteTopicSingle;
import com.jeecms.bbs.manager.BbsCommonMagicMng;
import com.jeecms.bbs.manager.BbsForumMng;
import com.jeecms.bbs.manager.BbsMagicConfigMng;
import com.jeecms.bbs.manager.BbsOperationMng;
import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.bbs.manager.BbsSessionMng;
import com.jeecms.bbs.manager.BbsTopicChargeMng;
import com.jeecms.bbs.manager.BbsTopicCountMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsTopicPostOperateMng;
import com.jeecms.bbs.manager.BbsTopicTypeMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.manager.BbsVoteItemMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.CheckMobile;
import com.jeecms.common.util.DateUtils;
import com.jeecms.core.manager.CmsSiteMng;
import com.jeecms.core.web.MagicMessage;

@Service
@Transactional
public class BbsTopicMngImpl implements BbsTopicMng {
	
	public static final String AUTH_KEY = "auth_key";
	
	public void move(Integer[] ids, Integer forumId, String reason,
			BbsUser operator) {
		BbsTopic topic;
		BbsForum origForum;
		BbsForum currForum;

		for (Integer id : ids) {
			topic = dao.findById(id);
			origForum = topic.getForum();
			if (!origForum.getId().equals(forumId)) {
				currForum = bbsForumMng.findById(forumId);
				topic.setForum(currForum);
				origForum.setTopicTotal(origForum.getTopicTotal() - 1);
				currForum.setTopicTotal(currForum.getTopicTotal() + 1);
				if (origForum.getLastPost() != null) {
					if (origForum.getLastPost().getTopic().getId() == id) {
						BbsPost lastPost = bbsPostMng.getLastPost(origForum
								.getId(), 0);
						if (lastPost != null) {
							origForum.setLastPost(lastPost);
						} else {
							origForum.setLastPost(null);
						}
					}
				}
			}
			bbsOperationMng.saveOpt(topic.getSite(), operator, "移动主题", reason,
					topic);
		}
	}

	public void shieldOrOpen(Integer[] ids, boolean shield, String reason,
			BbsUser operator) {
		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			short status = topic.getStatus();
			if (shield) {
				if (status == BbsTopic.NORMAL) {
					topic.setStatus(BbsTopic.SHIELD);
				}
				bbsOperationMng.saveOpt(topic.getSite(), operator, "屏蔽主题",
						reason, topic);
			} else {
				if (status == BbsTopic.SHIELD) {
					topic.setStatus(BbsTopic.NORMAL);
				}
				bbsOperationMng.saveOpt(topic.getSite(), operator, "解除主题",
						reason, topic);
			}
		}
	}
	
	public void recommend(Integer[] ids ,short recommend) {
		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			if (recommend==1) {//版主推荐
				topic.setRecommend(BbsTopic.TOPIC_MODERATOR_RECOMMEND);
			}else{//取消推荐
				topic.setRecommend(BbsTopic.TOPIC_NO_RECOMMEND);
			}
		}
		
	}

	public void lockOrOpen(Integer[] ids, boolean lock, String reason,
			BbsUser operator) {
		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			short status = topic.getStatus();
			if (lock) {
				if (status == BbsTopic.NORMAL) {
					topic.setStatus(BbsTopic.LOCKED);
				}
				bbsOperationMng.saveOpt(topic.getSite(), operator, "锁定主题",
						reason, topic);
			} else {
				if (status == BbsTopic.LOCKED) {
					topic.setStatus(BbsTopic.NORMAL);
				}
				bbsOperationMng.saveOpt(topic.getSite(), operator, "解除主题",
						reason, topic);
			}
		}
	}

	public void upOrDown(Integer[] ids, Date time, String reason,
			BbsUser operator) {
		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			topic.setSortTime(time);
			bbsOperationMng.saveOpt(topic.getSite(), operator, "提升/下沉主题",
					reason, topic);
		}
	}

	public void prime(Integer[] ids, short primeLevel, String reason,
			BbsUser operator) {
		BbsTopic topic;
		BbsUser toUser;
		BbsForum topicForum;
		for (Integer id : ids) {
			topic = dao.findById(id);
			topic.setPrimeLevel(primeLevel);
			toUser=topic.getCreater();
			topicForum=topic.getForum();
			if(primeLevel==1){
				toUser.setPrestige(toUser.getPrestige()+topicForum.getPrestigePrime1());
				toUser.setPrimeCount(toUser.getPrimeCount()+1);
			}else if(primeLevel==2){
				toUser.setPrestige(toUser.getPrestige()+topicForum.getPrestigePrime2());
				toUser.setPrimeCount(toUser.getPrimeCount()+1);
			}else if(primeLevel==3){
				toUser.setPrestige(toUser.getPrestige()+topicForum.getPrestigePrime3());
				toUser.setPrimeCount(toUser.getPrimeCount()+1);
			}else if(primeLevel==0){
				toUser.setPrestige(toUser.getPrestige()+topicForum.getPrestigePrime0());
				toUser.setPrimeCount(toUser.getPrimeCount()-1);
			}
			//加了精华 积分就加，去除精华后不取消
			toUser.setPoint(toUser.getPoint()+topicForum.getPointPrime());
			bbsOperationMng.saveOpt(topic.getSite(), operator, "精华", reason,
					topic);
		}
	}

	public void upTop(Integer[] ids, short topLevel, String reason,
			BbsUser operator) {
		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			topic.setTopLevel(topLevel);
			if (topLevel==0) {
				topic.setTopTime(null);
			}
			bbsOperationMng.saveOpt(topic.getSite(), operator, "置顶", reason,
					topic);
		}
	}

	public void highlight(Integer[] ids, String color, boolean bold,
			boolean italic, Date time, String reason, BbsUser operator) {
		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			topic.setStyleColor(color);
			topic.setStyleTime(time);
			topic.setStyleBold(bold);
			topic.setStyleItalic(italic);
			bbsOperationMng.saveOpt(topic.getSite(), operator, "高亮", reason,
					topic);
		}
	}
	
	public void highlightWithNoLog(Integer[] ids, String color, boolean bold,
			boolean italic, Date time, String reason, BbsUser operator) {
		BbsTopic topic;
		for (Integer id : ids) {
			topic = dao.findById(id);
			topic.setStyleColor(color);
			topic.setStyleTime(time);
			topic.setStyleBold(bold);
			topic.setStyleItalic(italic);
		}
	}

	public BbsTopic updateTitle(Integer id, String title, BbsUser editor) {
		BbsTopic topic = dao.findById(id);
		topic.setTitle(title);
		bbsOperationMng.saveOpt(topic.getSite(), editor, "修改主题标题", null, topic);
		return topic;
	}

	public BbsTopic updateTopic(Integer id, String title, String content,
			BbsUser editor, String ip) {
		BbsTopic topic = dao.findById(id);
		topic.setTitle(title);
		bbsPostMng.updatePost(topic.getFirstPost().getId(), content,
				editor, ip);
		return topic;
	}

	public BbsTopic postTopic(Integer userId, Integer siteId, Integer forumId,
			String title, String content, String ip,
			Integer category, Integer categoryType,
			Integer[] topicTypeIds,String[] name, List<MultipartFile> file,
			Boolean hasAttach,List<String> code,
			Short equipSource,Short charge,Double chargeAmount,
			Float postLatitude,Float postLongitude,
			Boolean rewardPattern,Double rewardRandomMin,
			Double rewardRandomMax,Double[] rewardFix) {
		BbsForum forum = bbsForumMng.findById(forumId);
		BbsUser user = bbsUserMng.findById(userId);
		BbsTopicText text = new BbsTopicText();
		BbsTopic topic = createTopic(category,categoryType);
		topic.setSite(siteMng.findById(siteId));
		topic.setForum(forum);
		/*
		if (postTypeId != null) {
			topic.setPostType(bbsPostTypeMng.findById(postTypeId));
		}
		*/
		topic.setCreater(user);
		topic.setLastReply(user);
		topic.setTopicText(text);
		topic.setTopicTitle(title);
		text.setTitle(title);
		text.setTopic(topic);
		//上传的文件或者API接口传递的attachs参数
		if ((file != null && file.size() > 0)||(hasAttach != null &&hasAttach)) {
			topic.setAffix(true);
		} else {
			topic.setAffix(false);
		}
		topic.init();
		if(forum.getTopicNeedCheck()&&!user.getModerator()){
			topic.setCheckStatus(BbsTopic.CHECKING);
		}
		topic=save(topic);
		topicCountMng.save(new BbsTopicCount(), topic);
		// 保存分类
		if (topicTypeIds != null && topicTypeIds.length > 0) {
			for (Integer typeId : topicTypeIds) {
				topic.addToTypes(bbsTopicTypeMng.findById(typeId));
			}
		}
		handleVoteItem(topic, name);
		BbsPost post = bbsPostMng.post(userId, siteId, topic.getId(),
			    content, ip, file, hasAttach,code,
				equipSource,postLatitude,postLongitude);
		topic.setFirstPost(post);
		updateTopicCount(topic, user);
		bbsConfigEhCache.setBbsConfigCache(1, 1, 1, 0, null, siteId);
		topicChargeMng.save(chargeAmount,charge,
				rewardPattern,rewardRandomMin,rewardRandomMax,topic);
		//打赏固定值
		if(rewardPattern!=null&&rewardPattern){
			if (rewardFix != null && rewardFix.length > 0) {
				for (int i = 0, len = rewardFix.length; i < len; i++) {
					if (rewardFix[i]!=null) {
						topic.addToRewardFixs(rewardFix[i]);
					}
				}
			}
		}
		forumCountCache.addTopic(forumId);
		return topic;
	}
	
	
	public BbsTopic postTopic(Integer userId, Integer siteId, Integer forumId,
			String title,String ip,Short equipSource ) {
		String topictitle;
		if(10<title.length()){
			topictitle = title.substring(0, 10);
		}else{
			topictitle = title;
		}
		 
		BbsForum forum = bbsForumMng.findById(forumId);
		BbsUser user = bbsUserMng.findById(userId);
		BbsTopicText text = new BbsTopicText();
		BbsTopic topic = new BbsTopic();
		topic.setSite(siteMng.findById(siteId));
		topic.setForum(forum);
		/*
		if (postTypeId != null) {
			topic.setPostType(bbsPostTypeMng.findById(postTypeId));
		}
		*/
		topic.setCreater(user);
		topic.setLastReply(user);
		topic.setTopicText(text);
		topic.setTopicTitle(topictitle);
		text.setTitle(topictitle);
		text.setTopic(topic);
		topic.init();
		save(topic);
		BbsPost post = bbsPostMng.post(userId, siteId, topic.getId(),
				title, ip,equipSource);
		topic.setFirstPost(post);
		updateTopicCount(topic, user);
		bbsConfigEhCache.setBbsConfigCache(1, 1, 1, 0, null, siteId);
		return topic;
	}

	@Transactional(readOnly = true)
	public Pagination getForTag(Integer siteId, Integer forumId,Integer parentPostTypeId,Integer postTypeId, Short status,
			Short primeLevel, String keyWords, String creater,
			Integer createrId, Short topLevel,Integer topicTypeId,Integer excludeId,
			Boolean checkStatus,int pageNo,int pageSize, String jinghua,Integer orderBy,Short recommend) {
		return dao.getForTag(siteId, forumId,parentPostTypeId,postTypeId, status, primeLevel, keyWords,
				creater, createrId, topLevel, topicTypeId,excludeId,
				checkStatus, pageNo,pageSize, jinghua,orderBy,recommend);
	}
	
	@Transactional(readOnly = true)
	public List<BbsTopic> getListForTag(Integer siteId, Integer forumId,
			Integer parentPostTypeId, Integer postTypeId, Short status,
			Short primeLevel, String keyWords, String creater,
			Integer createrId, Short topLevel, Integer topicTypeId,Integer excludeId,
			Boolean checkStatus, int first,int count,String jinghua, Integer orderBy,Short recommend){
		return dao.getListForTag(siteId, forumId,parentPostTypeId,postTypeId,
				status, primeLevel, keyWords,creater, createrId,
				topLevel, topicTypeId,excludeId,checkStatus, first,count,jinghua, orderBy,recommend);
	}

	@Transactional(readOnly = true)
	public Pagination getMemberTopic(Integer webId, Integer memberId,
			int pageNo, int pageSize) {
		return dao.getMemberTopic(webId, memberId, pageNo, pageSize);
	}

	@Transactional(readOnly = true)
	public List<BbsTopic> getTopTopic(Integer webId, Integer ctgId,
			Integer forumId) {
		return dao.getTopTopic(webId, ctgId, forumId);
	}

	@Transactional(readOnly = true)
	public Pagination getMemberReply(Integer webId, Integer memberId,
			int pageNo, int pageSize) {
		return dao.getMemberReply(webId, memberId, pageNo, pageSize);
	}
	
	@Transactional(readOnly = true)
	public List<BbsTopic> getMemberReply(Integer siteId, Integer userId,
			Integer first,Integer count) {
		return dao.getMemberReply(siteId, userId, first, count);
	}

	@Transactional(readOnly = true)
	public Pagination getTopicByTime(Integer webId, int pageNo, int pageSize) {
		return dao.getTopicByTime(webId, pageNo, pageSize);
	}

	@Transactional(readOnly = true)
	public Pagination getForSearchDate(Integer siteId, Integer forumId,
			Short primeLevel, Integer day, int pageNo, int pageSize) {
		return dao.getForSearchDate(siteId, forumId, primeLevel, day, pageNo,
				pageSize);
	}

	public BbsTopic save(BbsTopic topic) {
		initTopic(topic);
		dao.save(topic);
		return topic;
	}

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsTopic findById(Integer id) {
		BbsTopic entity = dao.findById(id);
		return entity;
	}

	public BbsTopic update(BbsTopic bean) {
		Updater<BbsTopic> updater = new Updater<BbsTopic>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public BbsTopic deleteById(Integer id) {
		BbsTopic bean =null;
		if(id!=null){
			bean = dao.findById(id);
			if(bean!=null){
				bean.setFirstPost(null);
				bean.setLastPost(null);
				List<BbsPost> postList = bbsPostMng.getPostByTopic(id);
				for (BbsPost post : postList) {
					if (post.equals(bean.getForum().getLastPost())) {
						BbsPost post1 = bbsPostMng.getLastPost(bean.getForum().getId(),
								id);
						if (post1 == null) {
							bean.getForum().setLastPost(null);
							bean.getForum().setLastReply(null);
							bean.getForum().setLastTime(null);
						} else {
							bean.getForum().setLastPost(post1);
							bean.getForum().setLastReply(post1.getCreater());
							bean.getForum().setLastTime(post1.getCreateTime());
						}
					}
					BbsForum forum = bean.getForum();
					bbsForumMng.update(forum);
					bbsPostMng.deleteById(post.getId());	
				}
				List<BbsTopicPostOperate>list=topicPostOperateMng.getList(bean.getId(), 
						BbsTopicPostOperate.DATA_TYPE_TOPIC,null, null,0,Integer.MAX_VALUE);
				for(BbsTopicPostOperate p:list){
					topicPostOperateMng.deleteById(p.getId());
				}
				dao.deleteById(id);
			}
		}
		return bean;
	}

	public BbsTopic[] deleteByIds(Integer[] ids) {
		BbsTopic[] beans = new BbsTopic[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private void initTopic(BbsTopic topic) {
		Date now = new Timestamp(System.currentTimeMillis());
		topic.setCreateTime(now);
		topic.setLastTime(now);
		topic.setSortTime(now);
		topic.setViewCount(0L);
		topic.setReplyCount(0);
		if(topic.getStatus()==null){
			topic.setStatus(BbsTopic.NORMAL);
		}
		if (topic.getTopLevel() == null) {
			topic.setTopLevel((short) 0);
		}
		if (topic.getPrimeLevel() == null) {
			topic.setPrimeLevel((short) 0);
		}
		if (topic.getStyleBold() == null) {
			topic.setStyleBold(false);
		}
		if (topic.getStyleItalic() == null) {
			topic.setStyleItalic(false);
		}
	}

	public void updateTopicCount(BbsTopic topic, BbsUser user) {
		BbsForum forum = topic.getForum();
		forum.setLastPost(topic.getFirstPost());
		forum.setLastReply(topic.getCreater());
		forum.setLastTime(topic.getSortTime());
		forum.setPostToday(forum.getPostToday() + 1);
		forum.setPostTotal(forum.getPostTotal() + 1);
		forum.setTopicTotal(forum.getTopicTotal() + 1);
		//是否启用积分
		if(forum.getPointAvailable()){
			user.setPoint(user.getPoint() + forum.getPointTopic());
			user.setTotalPoint(user.getTotalPoint() + forum.getPointTopic());
		}
		//是否启用威望
		if(forum.getPrestigeAvailable()){
			user.setPrestige(user.getPrestige()+forum.getPrestigeTopic());
		}
		user.setTopicCount(user.getTopicCount() + 1);
		user.setPostToday(user.getPostToday() + 1);
	}

	public List<BbsTopic> getList(Integer forumId,String keywords,Integer userId,
			Short topLevel,Integer first,Integer count) {
		return dao.getList(forumId,keywords,userId,topLevel,first,count);
	}

	public List<BbsTopic> getNewList(Short topLevel,Integer first,Integer count,Integer orderby) {
		return dao.getNewList(topLevel,first,count,orderby);
	}
	
	public List<BbsTopic> getTopList(Short topLevel,Integer count,Integer orderby){
		return dao.getTopList(topLevel, count, orderby);
	}
	
	public List<BbsTopic> getTopicList(){
		return dao.getTopicList();
	}
	public void updateAllTopicCount(BbsTopicCountEnum e){
		dao.updateAllTopicCount(e);
	}
	
	public void updateAllTopTime() {
		dao.updateAllTopTime();
	}
	
	/**
	 *  使用道具
	 */
	public String useMagic(HttpServletRequest request,Integer siteId,Integer tid,
			Integer postId,String magicName,Integer userId,
			String ip,String color,Integer postCreaterId){
		BbsTopic topic=null;
		if(tid!=null){
			topic=findById(tid);
		}
		BbsUser user=bbsUserMng.findById(userId);
		BbsPost post;
		BbsUser postCreater;
		if(StringUtils.isNotBlank(magicName)){
			if(magicName.equals(MagicConstants.MAGIC_CLOSE)){
				/**
				 * 沉默卡--关闭主题
				 */
				if(bbsForumMng.getModerators(siteId).contains(topic.getCreater().getUsername())){
					return MagicConstants.MAGIC_OPEN_ERROR_NOIN_MODERATORS;
				}
				topic.setAllayReply(false);
			}else if(magicName.equals(MagicConstants.MAGIC_OPEN)){
				/**
				 * 喧嚣卡---打开主题
				 */
				topic.setAllayReply(true);
			}else if(magicName.equals(MagicConstants.MAGIC_BUMP)){
				/**
				 * 提升卡
				 */
				topic.setTopLevel(MagicConstants.TOP_LEVEL_BUMP);
				topic.setTopTime(getTopTime(topic));
			}else if(magicName.equals(MagicConstants.MAGIC_JACK)){
				/**
				 * 千斤顶
				 */
				topic.setTopLevel(MagicConstants.TOP_LEVEL_JACK);
				topic.setTopTime(getTopTime(topic));
			}else if(magicName.equals(MagicConstants.MAGIC_STICK)){
				/**
				 * 置顶卡
				 */
				topic.setTopLevel(MagicConstants.TOP_LEVEL_STICK);
				topic.setTopTime(getTopTime(topic));
			}else if(magicName.equals(MagicConstants.MAGIC_SOFA)){
				/**
				 * 抢沙发(沙发的台词以后再设置道具那里再做更改由后台定制而来)
				 */
				if(topic.getHasSofeed()){
					return MagicConstants.MAGIC_SOFEED_ERROR;
				}else{
					String sofalines=magicConfigMng.findById(siteId).getMagicSofaLines();
					String userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();
					post=bbsPostMng.reply(userId, siteId, tid,null, 
							sofalines, ip, null, null,null,CheckMobile.getSource(userAgent),null,null);
					List<BbsPost>postList=bbsPostMng.getByTopicList(topic.getId(),0,Integer.MAX_VALUE);
					//其余各层+1往后排
					postList.remove(0);
					for(BbsPost p:postList){
						p.setIndexCount(p.getIndexCount()+1);
						bbsPostMng.update(p);
					}
					//沙发是2楼，主题帖1楼
					post.setIndexCount(2);
					topic.setHasSofeed(true);
				}
			}else if(magicName.equals(MagicConstants.MAGIC_HIGHTLIGHT)){
				/**
				 * 变色卡--高亮显示
				 */
				highlightWithNoLog(new Integer[]{tid}, color, false, false,DateUtils.afterDate(new Date(), 1), "", user);
			}else if(magicName.equals(MagicConstants.MAGIC_NAMEPOST)){
				/**
				 * 照妖镜---查看匿名身份（tid传入pid）,返回传回帖子创建者的用户名，拼接了常量字符串在上层处理返回问题
				 */
				post=bbsPostMng.findById(postId);
				bbsUserMng.updatePoint(userId, null, null, magicName, 1, 1);
				return MagicConstants.MAGIC_NAMEPOST_SUCCESS+post.getCreater().getUsername();
			}else if(magicName.equals(MagicConstants.MAGIC_ANONYMOUSPOST)){
				/**
				 * 匿名卡---隐藏自己信息
				 */
				post=bbsPostMng.findById(postId);
				post.setAnonymous(true);
			}else if(magicName.equals(MagicConstants.MAGIC_REPENT)){
				/**
				 * 悔悟卡--删除自己帖子,如果帖子是主题第一条帖子则删除主题
				 */
				post=bbsPostMng.findById(postId);
				if(post.getCreater().equals(user)){
					if(post.equals(post.getTopic().getFirstPost())){
						deleteById(tid);
					}else{
						//改变板块对应最后回帖及最后回帖会员
						//1.获取回帖对应的板块
						topic = post.getTopic();
						BbsForum forum = topic.getForum();
						forum.setLastPost(null);
						forum.setLastReply(null);
						bbsForumMng.update(forum);
						bbsPostMng.deleteById(postId);
					}
				}else{
					return MagicConstants.MAGIC_NOT_POST_CREATER_ERROR;
				}
			}else if(magicName.equals(MagicConstants.MAGIC_SHOWIP)){
				/**
				 * 窥视卡---查看用户ip
				 */
				postCreater=bbsUserMng.findById(postCreaterId);
				if(postCreater!=null){
					//更新道具使用
					bbsUserMng.updatePoint(userId, null, null, magicName, 1, 1);
					return postCreater.getUsername()+MagicConstants.MAGIC_SHOWIP_SUCCESS+postCreater.getLastLoginIp();
				}else{
					return MagicConstants.MAGIC_FIND_USER_ERROR;
				}
			}else if(magicName.equals(MagicConstants.MAGIC_CHECKONLINE)){
				/**
				 * 雷达卡---查看用户是否在线
				 */
				postCreater=bbsUserMng.findById(postCreaterId);
				if(postCreater!=null){
					bbsUserMng.updatePoint(userId, null, null, magicName, 1, 1);
					//
					boolean isOnline=false;
					BbsSession userSess=bbsSessionMng.getUserSession(postCreaterId);
					if(userSess!=null){
						isOnline=userSess.isOnline();
					}
					if(isOnline){
						return postCreater.getUsername()+MagicConstants.MAGIC_CHECKONLINE+MagicConstants.MAGIC_CHECKONLINE_ONLINE;
					}else{
						return postCreater.getUsername()+MagicConstants.MAGIC_CHECKONLINE+MagicConstants.MAGIC_CHECKONLINE_OFFLINE;
					}
				}else{
					return MagicConstants.MAGIC_FIND_USER_ERROR;
				}
			}else if(magicName.equals(MagicConstants.MAGIC_MONEY)){
				/**
				 * 金钱卡
				 */
				BbsCommonMagic commomMagic=commomMagicMng.findByIdentifier(magicName);
				int price=commomMagic.getPrice().intValue();
				Integer money=generateRandom(1,price*2);
				String str="";
				MagicMessage magicMessage = MagicMessage.create(request);
				if(commomMagic.getCredit()==1){
					//积分
					str = magicMessage.getMessage("cmsUser.point");
					bbsUserMng.updatePoint(userId, money, null, magicName, 1, 1);
				}else if(commomMagic.getCredit()==2){
					//威望
					str = magicMessage.getMessage("cmsUser.prestige");
					bbsUserMng.updatePoint(userId, null, money, magicName, 1, 1);
				}
				return str+MagicConstants.MAGIC_MONEY_SUCCESS+money;
			}
			//未返回的道具（减少道具数量）公用
			bbsUserMng.updatePoint(userId, null, null, magicName, 1, 1);
		}
		return MagicConstants.MAGIC_OPEN_SUCCESS;
	}

	private BbsTopic createTopic(Integer category,Integer categoryType) {
		if (category != null ) {
			if(category == TOPIC_VOTE){
				if(categoryType==1){
					BbsVoteTopic topic = new BbsVoteTopic();
					return topic;
				}else if(categoryType==2){
					BbsVoteTopicSingle topic=new BbsVoteTopicSingle();
					return topic;
				}
			}
		}
		return new BbsTopic();
	}
	
	private Date getTopTime(BbsTopic topic){
		Date toDay = new Date();
		Date topTime = new Date();
		if (topic.getTopTime()==null) {//判断置顶时间是否存在
			//不存在，则将置顶期限设为当天+1
			topTime.setTime(toDay.getTime()+(1000*60*60*24));
		}else{
			//存在，则将置顶期限+1
			topTime.setTime(topic.getTopTime().getTime()+(1000*60*60*24));
		}
		return topTime;
	}

	private void handleVoteItem(BbsTopic topic, String[] name) {
		if (name != null) {
			if(topic.getCategory() == TOPIC_VOTE||topic.getCategory() ==TOPIC_VOTE_SINGLE){
				for(String s : name){
					BbsVoteItem bean = new BbsVoteItem();
					bean.init();
					bean.setName(s);
					if(topic.getCategory() == TOPIC_VOTE){
						bean.setTopic((BbsVoteTopic)topic);
					}else if(topic.getCategory() == TOPIC_VOTE_SINGLE){
						bean.setTopic((BbsVoteTopicSingle)topic);
					}
					bbsVoteItemMng.save(bean);
				}
			}
		}
	}
	/**
	 *  获取某个范围内的随机数
	 * @param a
	 * @param b
	 * @return
	 */
	private  int generateRandom(int a, int b) {
	        int temp = 0;
	            if (a > b) {
	                temp = new Random().nextInt(a - b);
	                return temp + b;
	            } else {
	                temp = new Random().nextInt(b - a);
	                return temp + a;
	            }
	    }
	
	public List<BbsTopic> getTopicList(Integer userId,Integer bigId,Integer smallId,Integer count){
		return dao.getTopicList(userId, bigId, smallId, count);
		
	}

	@Autowired
	private BbsPostMng bbsPostMng;
	@Autowired
	private BbsForumMng bbsForumMng;
	@Autowired
	private BbsOperationMng bbsOperationMng;
	@Autowired
	private CmsSiteMng siteMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsConfigEhCache bbsConfigEhCache;
	@Autowired
	private BbsVoteItemMng bbsVoteItemMng;
	@Autowired
	private BbsMagicConfigMng magicConfigMng;
	@Autowired
	private BbsCommonMagicMng commomMagicMng;
	@Autowired
	private BbsTopicTypeMng bbsTopicTypeMng;
	@Autowired
	private BbsTopicCountMng topicCountMng;
	@Autowired
	private BbsTopicChargeMng topicChargeMng;
	@Autowired
	private BbsTopicDao dao;
	@Autowired
	private BbsSessionMng bbsSessionMng;
	@Autowired
	private BbsTopicPostOperateMng topicPostOperateMng;
	@Autowired
	private ForumCountCache forumCountCache;

}
