package com.jeecms.bbs.manager.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeecms.bbs.cache.BbsConfigEhCache;
import com.jeecms.bbs.cache.ForumCountCache;
import com.jeecms.bbs.dao.BbsPostDao;
import com.jeecms.bbs.entity.Attachment;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsPostCount;
import com.jeecms.bbs.entity.BbsPostText;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicPostOperate;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.AttachmentMng;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsOperationMng;
import com.jeecms.bbs.manager.BbsPostCountMng;
import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.bbs.manager.BbsPostTypeMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsTopicPostOperateMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.StrUtils;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.image.ImageScale;
import com.jeecms.common.image.ImageUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.upload.FileRepository;
import com.jeecms.common.util.CheckMobile;
import com.jeecms.core.manager.CmsSiteMng;

@Service
@Transactional
public class BbsPostMngImpl implements BbsPostMng {

	public BbsPost shield(Integer id, String reason, BbsUser operator,Short status) {
		BbsPost post = dao.findById(id);
		post.setStatus(status);
		if(status==-1){
			bbsOperationMng.saveOpt(post.getSite(), operator, "屏蔽", reason, post);
		}else if(status==0){
			bbsOperationMng.saveOpt(post.getSite(), operator, "取消屏蔽", reason, post);
		}
		
		return post;
	}

	public BbsPost updatePost(Integer id, String content,
			BbsUser editor, String ip) {
		// 修改BbsPost
		BbsPost post = dao.findById(id);
		post.setEditCount(post.getEditCount() + 1);
		post.setEditerIp(ip);
		post.setEditTime(new Timestamp(System.currentTimeMillis()));
		post.setEditer(editor);
		
		if (findHidden(content)) {
			post.setHidden(true);
		} else {
			post.setHidden(false);
		}
		// 修改BbsPostText
		BbsPostText text = post.getPostText();
		text.setContent(content);
		
		// 写入操作日志
		bbsOperationMng.saveOpt(post.getSite(), editor, "编辑", null, post);
		return post;
	}
	
	public BbsPost updatePost(Integer id, String content,
			BbsUser editor, String ip, List<MultipartFile> file,
			List<String> code,Boolean hasAttach) {
		// 修改BbsPost
		BbsPost post = dao.findById(id);
		post.setEditCount(post.getEditCount() + 1);
		post.setEditerIp(ip);
		post.setEditTime(new Timestamp(System.currentTimeMillis()));
		post.setEditer(editor);
		if (file != null && file.size() > 0||(hasAttach != null && hasAttach)) {
			post.setAffix(true);
		}
		if (findHidden(content)) {
			post.setHidden(true);
		} else {
			post.setHidden(false);
		}
		BbsPostText text = post.getPostText();
		
		if (file != null && file.size() > 0) {
			content = uploadImg(content, file, code, 1, post);
		}
		text.setContent(content);
		// 写入操作日志
		bbsOperationMng.saveOpt(post.getSite(), editor, "编辑", null, post);
		return post;
	}

	public BbsPost post(Integer userId, Integer siteId, Integer topicId,
			String content, String ip, List<MultipartFile> file,
			Boolean hasAttach,List<String> code,Short equipSource,Float postLatitude,Float postLongitude) {
		BbsPostText text = new BbsPostText();
		BbsPost post = new BbsPost();
		BbsTopic topic = bbsTopicMng.findById(topicId);
		BbsUser creater=bbsUserMng.findById(userId);
		post.setSite(siteMng.findById(siteId));
		post.setConfig(bbsConfigMng.findById(siteId));
		post.setTopic(topic);
		/*
		if(postTypeId!=null){
			post.setPostType(bbsPostTypeMng.findById(postTypeId));
		}
		*/
		post.setCreater(creater);
		if ((file != null && file.size() > 0)||(hasAttach != null && hasAttach)) {
			post.setAffix(true);
		} else {
			post.setAffix(false);
		}
		if (findHidden(content)) {
			post.setHidden(true);
		} else {
			post.setHidden(false);
		}
		initBbsPost(post, ip);
		if(topic.getForum().getPostNeedCheck()&&!creater.getModerator()){
			post.setCheckStatus(BbsPost.CHECKING);
		}
		if(equipSource==null){
			equipSource=CheckMobile.EQU_PC;
		}
		post.setPostSource(equipSource);
		post.setPostLatitude(postLatitude);
		post.setPostLongitude(postLongitude);
		post = dao.save(post);
		postCountMng.save(new BbsPostCount(), post);
		if (file != null && file.size() > 0) {
			content = uploadImg(content, file, code, siteId, post);
		}
		text.setContent(content);
		post.setPostText(text);
		post.setIndexCount(getIndexCount(topicId));
		if (post.getIndexCount() == 1) {
			topic.setHaveReply(",");
		} else {
			if (topic.getHaveReply() == null) {
				topic.setHaveReply("," + userId + ",");
			} else {
				if (topic.getHaveReply().indexOf("," + userId + ",") == -1) {
					topic.setHaveReply(topic.getHaveReply() + userId + ",");
				}
			}
		}
		forumCountCache.addPost(topic.getForum().getId());
		return post;
	}
	
	public BbsPost post(Integer userId, Integer siteId, Integer topicId,
			String content, String ip,Short equipSource) {
		BbsPostText text = new BbsPostText();
		BbsPost post = new BbsPost();
		BbsTopic topic = bbsTopicMng.findById(topicId);
		post.setSite(siteMng.findById(siteId));
		post.setConfig(bbsConfigMng.findById(siteId));
		post.setTopic(topic);
		/*
		if(postTypeId!=null){
			post.setPostType(bbsPostTypeMng.findById(postTypeId));
		}
		*/
		post.setCreater(bbsUserMng.findById(userId));
		if (findHidden(content)) {
			post.setHidden(true);
		} else {
			post.setHidden(false);
		}
		initBbsPost(post, ip);
		if(equipSource==null){
			equipSource=CheckMobile.EQU_PC;
		}
		post.setPostSource(equipSource);
		post = dao.save(post);
		postCountMng.save(new BbsPostCount(), post);
		text.setContent(content);
		post.setPostText(text);
		post.setIndexCount(getIndexCount(topicId));
		if (post.getIndexCount() == 1) {
			topic.setHaveReply(",");
		} else {
			if (topic.getHaveReply() == null) {
				topic.setHaveReply("," + userId + ",");
			} else {
				if (topic.getHaveReply().indexOf("," + userId + ",") == -1) {
					topic.setHaveReply(topic.getHaveReply() + userId + ",");
				}
			}
		}
		return post;
	}


	public List<BbsPost> getPostByTopic(Integer topicId) {
		return dao.getPostByTopic(topicId);
	}
	
	
	public List<BbsPost> getPostByTopic(Integer topicId,Integer parentId,
			Integer userId,Boolean checkStatus,Integer first, int count){
		return dao.getPostByTopic(topicId,parentId,userId,checkStatus,first, count);
	}
	
	@Override
	public Pagination getPostPageByTopic(Integer topicId, Integer parentId, Integer userId, Boolean checkStatus,
			Integer pageNo, Integer pageSize) {
		return dao.getPostPageByTopic(topicId, parentId, userId, checkStatus, pageNo, pageSize);
	}

	public BbsPost reply(Integer userId, Integer siteId, Integer topicId,Integer parentId,
			String content, String ip, List<MultipartFile> file,
			Boolean hasAttach,List<String> code,
			Short equipSource,Float postLatitude,Float postLongitude) {
		BbsPost post = post(userId, siteId, topicId,content, ip, file,
				hasAttach,code,equipSource,postLatitude,postLongitude);
		if (bbsUserMng.findById(userId).getModerator()) {
			post.getTopic().setModeratorReply(true);
		}
		if(parentId!=null){
			post.setParent(findById(parentId));
			postCountMng.postReply(parentId);
		}
		updatePostCount(post, bbsUserMng.findById(userId));
		bbsConfigEhCache.setBbsConfigCache(1, 0, 1, 0, null, siteId);
		return post;
	}

	public String uploadImg(String content, List<MultipartFile> file,
			List<String> code, Integer siteId, BbsPost post) {
		List<String> list = findImgUrl(content);
		for (int i = 0; i < code.size(); i++) {
			if (list.contains(code.get(i))) {
				String origName = file.get(i).getOriginalFilename();
				String ext = FilenameUtils.getExtension(origName).toLowerCase(
						Locale.ENGLISH);
				//文件格式检查
				String filepath,picZoomPath = null;
				try {
					filepath = fileRepository.storeByExt(siteMng.findById(
							siteId).getUploadPath(), ext, file.get(i));
					//图片生成缩率图
					if(ImageUtils.isValidImageExt(ext)){
						picZoomPath = fileRepository.storeByExt(siteMng.findById(
								siteId).getUploadPath(), ext, fileRepository.retrieve(filepath));
						File picFile = fileRepository.retrieve(picZoomPath);
						BufferedImage bufferedImage = ImageIO.read(picFile);   
						int picWidth = bufferedImage.getWidth();   
						int picHeight = bufferedImage.getHeight();
						int reWidth=bbsConfigMng.get().getPicZoomDefWidth();
						int reHeight = (reWidth * picHeight) / picWidth; //高度等比缩放 
						try {
							imageScale.resizeFix(picFile, picFile, reWidth, reHeight);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					Attachment att = attachmentMng.addAttachment(origName, filepath,
							picZoomPath, post,file.get(i).getSize());
					int j = i + 1;
					content = StrUtils.replace(content, "[localimg]" + j
							+ "[/localimg]", "[attachment]" + att.getId()
							+ "[/attachment]");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}
	
	private String replaceAttachs(String content, List<String> attachs,
			List<String> code) {
		List<String> list = findImgUrl(content);
		for (int i = 0; i < code.size(); i++) {
			if (list.contains(code.get(i))) {
				int j = i + 1;
				content = StrUtils.replace(content, "[localimg]" + j
						+ "[/localimg]", "[attachment]" + attachs.get(i)
						+ "[/attachment]");
			}
		}
		return content;
	}

	private List<String> findImgUrl(String content) {
		String ems = "\\[localimg]([0-9]+)\\[/localimg]";
		Matcher matcher = Pattern.compile(ems).matcher(content);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			String url = matcher.group(1);
			list.add(url);
		}
		return list;
	}

	private boolean findHidden(String content) {
		String ems = "\\[hide\\]([\\s\\S]*)\\[/hide\\]";
		Matcher matcher = Pattern.compile(ems).matcher(content);
		while (matcher.find()) {
			return true;
		}
		return false;
	}

	@Transactional(readOnly = true)
	public Pagination getForTag(Integer siteId, Integer topicId,Integer parentId,
			Integer userId, Boolean checkStatus,Integer option,
			Integer orderBy, int pageNo, int pageSize) {
		return dao.getForTag(siteId, topicId,parentId, userId, 
				checkStatus, option,orderBy,pageNo, pageSize);
	}
	
	@Transactional(readOnly = true)
	public List<BbsPost> getListForTag(Integer siteId, Integer topicId,
			Integer parentId,Integer userId, Boolean checkStatus, 
			Integer option,Integer orderBy,int first, int count){
		return dao.getListForTag(siteId, topicId, parentId, 
				userId, checkStatus,option,orderBy, first, count);
	}

	public BbsPost getLastPost(Integer forumId, Integer topicId) {
		return dao.getLastPost(forumId, topicId);
	}

	@Transactional(readOnly = true)
	public Pagination getMemberReply(Integer webId, Integer memberId,
			int pageNo, int pageSize) {
		int count = dao.getMemberReplyCount(webId, memberId);
		if (pageSize == 0) {
			pageSize = 16;
		}
		if ((count / pageSize + 1) < pageNo) {
			pageNo = count / pageSize + 1;
		}
		if (pageNo < 1) {
			pageNo = 1;
		}
		List<Number> list = dao.getMemberReply(webId, memberId, (pageNo - 1)
				* pageSize, pageSize);
		List<BbsPost> l = new ArrayList<BbsPost>();
		Pagination p = new Pagination();
		for (Number b : list) {
			BbsPost bbspost = dao.findById(b.intValue());
			l.add(bbspost);
		}
		p.setPageNo(pageNo);
		p.setPageSize(pageSize);
		p.setTotalCount(count);
		p.setList(l);
		return p;
	}

	@Transactional(readOnly = true)
	public int getMemberReplyCount(Integer webId, Integer memberId) {
		return dao.getMemberReplyCount(webId, memberId);
	}

	@Transactional(readOnly = true)
	public int getIndexCount(Integer topicId) {
		return dao.getIndexCount(topicId);
	}

	@Transactional(readOnly = true)
	public Pagination getPage(int pageNo, int pageSize) {
		Pagination page = dao.getPage(pageNo, pageSize);
		return page;
	}

	@Transactional(readOnly = true)
	public BbsPost findById(Integer id) {
		BbsPost entity = dao.findById(id);
		return entity;
	}

	public BbsPost save(BbsPost bean) {
		dao.save(bean);
		return bean;
	}

	public BbsPost update(BbsPost bean) {
		Updater<BbsPost> updater = new Updater<BbsPost>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public BbsPost deleteById(Integer id) {
		BbsPost bean=findById(id);
		if(bean!=null){
			BbsTopic topic=bean.getTopic();
			if(topic.getFirstPost()!=null&&topic.getFirstPost().equals(bean)){
				topic.setFirstPost(null);
			}
			if(topic.getLastPost()!=null&&topic.getLastPost().equals(bean)){
				topic.setLastPost(null);
			}
			if (topic.getReplyCount()>0) {//减去话题的回复数量
				topic.setReplyCount(topic.getReplyCount()-1);
			}else{
				topic.setReplyCount(0);
			}
			List<BbsTopicPostOperate>list=topicPostOperateMng.getList(bean.getId(), BbsTopicPostOperate.DATA_TYPE_POST, 
					null, null,0,Integer.MAX_VALUE);
			for(BbsTopicPostOperate p:list){
				topicPostOperateMng.deleteById(p.getId());
			}
			dao.deleteById(id);
			bbsTopicMng.update(topic);
		}
		return bean;
	}

	public BbsPost[] deleteByIds(Integer[] ids) {
		BbsPost[] beans = new BbsPost[ids.length];
		for (int i = 0, len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public List<BbsPost> getList(int count, int orderby) {
		List<Integer>postIds= dao.getList(count,orderby);
		List<BbsPost>results=new ArrayList<BbsPost>();
		if(postIds!=null&&postIds.size()>0){
			for(Integer postId:postIds){
				results.add(findById(postId));
			}
		}
		return results;
	}

	
	private void initBbsPost(BbsPost post, String ip) {
		Date now = new Timestamp(System.currentTimeMillis());
		post.setCreateTime(now);
		post.setPosterIp(ip);
		post.setEditCount(0);
		post.setStatus(BbsPost.NORMAL);
		post.setCheckStatus(BbsPost.CHECKED);
		post.setAnonymous(false);
		if(post.getAffix()==null){
			post.setAffix(false);
		}
		if (post.getIndexCount() == null) {
			post.setIndexCount(0);
		}
	}

	public void updatePostCount(BbsPost post, BbsUser user) {
		BbsTopic topic = post.getTopic();
		BbsForum forum = topic.getForum();
		forum.setLastPost(post);
		forum.setLastReply(post.getCreater());
		forum.setLastTime(post.getCreateTime());
		forum.setPostToday(forum.getPostToday() + 1);
		forum.setPostTotal(forum.getPostTotal() + 1);
		topic.setLastPost(post);
		topic.setLastReply(post.getCreater());
		topic.setLastTime(post.getCreateTime());
		topic.setSortTime(post.getCreateTime());
		topic.setReplyCount(topic.getReplyCount() + 1);
		topic.setReplyCountDay(topic.getReplyCountDay()+1);
		//是否启用积分
		if(forum.getPointAvailable()){
			user.setPoint(user.getPoint() + forum.getPointReply());
			user.setTotalPoint(user.getTotalPoint() + forum.getPointReply());
		}
		//是否启用威望
		if(forum.getPrestigeAvailable()){
			user.setPrestige(user.getPrestige()+forum.getPrestigeReply());
		}
		user.setPostToday(user.getPostToday() + 1);
		user.setReplyCount(user.getReplyCount() + 1);
	}
	
	public List<BbsPost> getByTopicList(Integer topicId,Integer first,Integer count){
		return dao.getByTopicList(topicId,first,count);
		
	}

	private BbsOperationMng bbsOperationMng;
	private BbsConfigMng bbsConfigMng;
	private CmsSiteMng siteMng;
	private BbsTopicMng bbsTopicMng;
	private BbsUserMng bbsUserMng;
	private BbsConfigEhCache bbsConfigEhCache;
	private AttachmentMng attachmentMng;
	private FileRepository fileRepository;
	private BbsPostTypeMng bbsPostTypeMng;
	private BbsPostDao dao;
	@Autowired
	private BbsPostCountMng postCountMng;
	@Autowired
	private ImageScale imageScale;
	@Autowired
	private BbsTopicPostOperateMng topicPostOperateMng;
	@Autowired
	private ForumCountCache forumCountCache;

	@Autowired
	public void setDao(BbsPostDao dao) {
		this.dao = dao;
	}

	@Autowired
	public void setBbsConfigMng(BbsConfigMng bbsConfigMng) {
		this.bbsConfigMng = bbsConfigMng;
	}

	@Autowired
	public void setSiteMng(CmsSiteMng siteMng) {
		this.siteMng = siteMng;
	}

	@Autowired
	public void setBbsUserMng(BbsUserMng bbsUserMng) {
		this.bbsUserMng = bbsUserMng;
	}

	@Autowired
	public void setBbsOperationMng(BbsOperationMng bbsOperationMng) {
		this.bbsOperationMng = bbsOperationMng;
	}

	@Autowired
	public void setBbsTopicMng(BbsTopicMng bbsTopicMng) {
		this.bbsTopicMng = bbsTopicMng;
	}

	@Autowired
	public void setBbsConfigEhCache(BbsConfigEhCache bbsConfigEhCache) {
		this.bbsConfigEhCache = bbsConfigEhCache;
	}

	@Autowired
	public void setAttachmentMng(AttachmentMng attachmentMng) {
		this.attachmentMng = attachmentMng;
	}

	@Autowired
	public void setFileRepository(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}
	@Autowired
	public void setBbsPostTypeMng(BbsPostTypeMng bbsPostTypeMng) {
		this.bbsPostTypeMng = bbsPostTypeMng;
	}

	/* (non-Javadoc)
	 * @see com.jeecms.bbs.manager.BbsPostMng#clearS(com.jeecms.bbs.entity.BbsPost)
	 */
}
