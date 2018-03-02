package com.jeecms.bbs.manager;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.common.page.Pagination;

public interface BbsPostMng {
	
	public BbsPost post(Integer userId, Integer siteId, Integer topicId,
			String content, String ip, List<MultipartFile> file,
			Boolean hasAttach,List<String> code,Short equipSource,Float postLatitude,Float postLongitude);
	
	public BbsPost post(Integer userId, Integer siteId, Integer topicId,
			 String content, String ip,Short equipSource);
			

	public BbsPost updatePost(Integer id, String content,
			BbsUser editor, String ip, List<MultipartFile> file,
			List<String> code,Boolean hasAttach);
	
	public BbsPost updatePost(Integer id, String content,
			BbsUser editor, String ip);

	public BbsPost shield(Integer id, String reason, BbsUser operator,Short status);

	public BbsPost reply(Integer userId, Integer siteId, Integer topicId,Integer parentId,
			String content, String ip, List<MultipartFile> file,
			Boolean hasAttach,List<String> code,Short equipSource,
			Float postLatitude,Float postLongitude);

	public List<BbsPost> getPostByTopic(Integer topicId);

	public Pagination getForTag(Integer siteId, Integer topicId,Integer parentId,
			Integer userId, Boolean checkStatus,Integer option,Integer orderBy,
			int pageNo, int pageSize);
	
	public List<BbsPost> getListForTag(Integer siteId, Integer topicId,
			Integer parentId,Integer userId, Boolean checkStatus, Integer option,
			Integer orderBy,int first, int count);

	public Pagination getMemberReply(Integer webId, Integer memberId,
			int pageNo, int pageSize);

	public int getMemberReplyCount(Integer webId, Integer memberId);

	public BbsPost getLastPost(Integer forumId, Integer topicId);

	public int getIndexCount(Integer topicId);

	public Pagination getPage(int pageNo, int pageSize);

	public BbsPost findById(Integer id);

	public BbsPost save(BbsPost bean);

	public BbsPost update(BbsPost bean);

	public BbsPost deleteById(Integer id);

	public BbsPost[] deleteByIds(Integer[] ids);

	public List<BbsPost> getList(int count, int orderby);
	
	public List<BbsPost> getPostByTopic(Integer topicId,Integer parentId,Integer userId,Boolean checkStatus,Integer first, int count);
	
	public Pagination getPostPageByTopic(Integer topicId,Integer parentId,Integer userId,Boolean checkStatus,Integer pageNo, Integer pageSize);
	
	public List<BbsPost> getByTopicList(Integer topicId,Integer first,Integer count);
}