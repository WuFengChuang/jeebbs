package com.jeecms.bbs.manager;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsTopicCountEnum;
import com.jeecms.common.page.Pagination;

public interface BbsTopicMng {
	public void recommend(Integer[] ids ,short recommend);

	public void move(Integer[] ids, Integer forumId, String reason,
			BbsUser operator);

	public void shieldOrOpen(Integer[] ids, boolean shield, String reason,
			BbsUser operator);

	public void lockOrOpen(Integer[] ids, boolean lock, String reason,
			BbsUser operator);

	public void upOrDown(Integer[] ids, Date time, String reason,
			BbsUser operator);

	public void prime(Integer[] ids, short primeLevel, String reason,
			BbsUser operator);

	public void upTop(Integer[] ids, short topLevel, String reason,
			BbsUser operator);

	public void highlight(Integer[] ids, String color, boolean bold,
			boolean italic, Date time, String reason, BbsUser operator);
	
	public void highlightWithNoLog(Integer[] ids, String color, boolean bold,
			boolean italic, Date time, String reason, BbsUser operator);

	public BbsTopic updateTitle(Integer id, String title, BbsUser editor);

	public BbsTopic postTopic(Integer userId, Integer siteId, Integer forumId,
			String title, String content, String ip,
			Integer category, Integer categoryType,
			Integer[] topicTypeIds,String[] name, List<MultipartFile> file,
			Boolean hasAttach,List<String> code,Short equipSource,
			Short charge,Double chargeAmount,
			Float postLatitude,Float postLongitude,
			Boolean rewardPattern,Double rewardRandomMin,
			Double rewardRandomMax,Double[] rewardFix);
	
	public BbsTopic postTopic(Integer userId, Integer siteId, Integer forumId,
	String title,String ip,Short equipSource);

	public Pagination getForTag(Integer siteId, Integer forumId,
			Integer parentPostTypeId, Integer postTypeId, Short status,
			Short primeLevel, String keyWords, String creater,
			Integer createrId, Short topLevel, Integer topicTypeId,Integer excludeId,
			Boolean checkStatus,int pageNo,int pageSize, String jinghua,Integer orderBy,Short recommend);
	
	public List<BbsTopic> getListForTag(Integer siteId, Integer forumId,
			Integer parentPostTypeId, Integer postTypeId, Short status,
			Short primeLevel, String keyWords, String creater,
			Integer createrId, Short topLevel, Integer topicTypeId,Integer excludeId,
			Boolean checkStatus,int first,int count,String jinghua,Integer orderBy,Short recommend);
	
	public Pagination getMemberTopic(Integer webId, Integer memberId,
			int pageNo, int pageSize);

	public Pagination getMemberReply(Integer webId, Integer memberId,
			int pageNo, int pageSize);
	
	public List<BbsTopic> getMemberReply(Integer siteId, Integer userId,
			Integer first,Integer count);

	public Pagination getForSearchDate(Integer siteId, Integer forumId,
			Short primeLevel, Integer day, int pageNo, int pageSize);

	public Pagination getPage(int pageNo, int pageSize);

	public BbsTopic findById(Integer id);

	public BbsTopic save(BbsTopic bean);

	public BbsTopic update(BbsTopic bean);

	public BbsTopic deleteById(Integer id);

	public BbsTopic[] deleteByIds(Integer[] ids);

	public List<BbsTopic> getList(Integer forumId,String keywords,Integer userId,Short topLevel,Integer first,Integer count);

	public List<BbsTopic> getNewList(Short topLevel,Integer first,Integer count,Integer orderby);
	
	public List<BbsTopic> getTopList(Short topLevel,Integer count,Integer orderby);
	
	public List<BbsTopic> getTopicList();
	
	public void updateAllTopicCount(BbsTopicCountEnum e);
	
	public void updateAllTopTime();
	/**
	 * 
	 * @param tid
	 *            主题id
	 * @param magicName
	 *            道具名称
	 */
	public String useMagic(HttpServletRequest request, Integer siteId,
			Integer tid, Integer postId,String magicName, Integer userId, String ip,
			String color, Integer postCreaterId);
	
	public List<BbsTopic> getTopicList(Integer userId,Integer bigId,Integer smallId,Integer count);

}
