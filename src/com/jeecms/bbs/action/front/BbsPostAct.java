package com.jeecms.bbs.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_POST;
import static com.jeecms.bbs.Constants.TPLDIR_TOPIC;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jeecms.bbs.api.Constants;
import com.jeecms.bbs.cache.TopicCountEhCache;
import com.jeecms.bbs.entity.BbsForum;
import com.jeecms.bbs.entity.BbsGrade;
import com.jeecms.bbs.entity.BbsPost;
import com.jeecms.bbs.entity.BbsTopic;
import com.jeecms.bbs.entity.BbsTopicPostOperate;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsConfigMng;
import com.jeecms.bbs.manager.BbsGradeMng;
import com.jeecms.bbs.manager.BbsLimitMng;
import com.jeecms.bbs.manager.BbsPostCountMng;
import com.jeecms.bbs.manager.BbsPostMng;
import com.jeecms.bbs.manager.BbsTopicMng;
import com.jeecms.bbs.manager.BbsTopicPostOperateMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.manager.CmsSensitivityMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.bbs.web.WebErrors;
import com.jeecms.common.util.CheckMobile;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.web.MagicMessage;

@Controller
public class BbsPostAct {
	private static final Logger log = LoggerFactory.getLogger(BbsPostAct.class);

	public static final String TPL_POSTADD = "tpl.postadd";
	public static final String TPL_POSTEDIT = "tpl.postedit";
	public static final String TPL_NO_LOGIN = "tpl.nologin";
	public static final String TPL_NO_URL = "tpl.nourl";
	public static final String TPL_POST_QUOTE = "tpl.postquote";
	public static final String TPL_GUANSHUI = "tpl.guanshui";
	public static final String TPL_POST_GRADE = "tpl.postgrade";
	public static final String TPL_NO_VIEW = "tpl.noview";
	public static final String TPL_NO_POSTTYPE = "tpl.noposttype";
	public static final String TPL_POST_CHILD = "tpl.postChild";
	public static final String TPL_POST_CONTENT = "tpl.postContent";

	@RequestMapping("/post/v_add{topicId}.jspx")
	public String add(@PathVariable Integer topicId, Integer tid,Integer parentId,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		if (topicId == null && tid == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_POST, TPL_NO_URL);
		}
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsTopic topic = null;
		if (topicId != null) {
			model.put("topicId", topicId);
			topic = bbsTopicMng.findById(topicId);
		} else if (tid != null) {
			model.put("topicId", tid);
			topic = bbsTopicMng.findById(tid);
		}
		String msg=null;
		//主题是否关闭
		if(!topic.getAllayReply()){
			MagicMessage magicMessage=MagicMessage.create(request);
			 msg=magicMessage.getMessage("magic.open.error");
		}else{
			//检查用户权限
			 msg = checkReply(request,topic.getForum(), user, site);
		}
		if (msg != null) {
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		/*
		if(topic!=null&&topic.getPostType()!=null){
			model.put("postTypeId", topic.getPostType().getId());
		}
		*/
		model.put("parentId", parentId);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_POST, TPL_POSTADD);
	}

	@RequestMapping("/post/v_edit{id}.jspx")
	public String edit(@PathVariable Integer id, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if (id != null) {
			String msg = checkEdit(request,manager.findById(id).getTopic().getForum(),
					manager.findById(id), user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			model.addAttribute("post", manager.findById(id));
		} else {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_POST, TPL_NO_URL);
		}
		return FrontUtils.getTplPath(request, site,
				TPLDIR_POST, TPL_POSTEDIT);
	}

	@RequestMapping("/post/o_save.jspx")
	public String save(BbsPost bean, Integer topicId,Integer parentId,
			String content,
			@RequestParam(value = "code", required = false) List<String> code,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		String msg = checkReply(request,bbsTopicMng.findById(topicId).getForum(), user,
				site);
		if (msg != null) {
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		boolean flag = topicCountEhCache.getLastReply(user.getId(), user
				.getGroup().getPostInterval());
		if (!flag) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_GUANSHUI);
		}
		/*
		if(postTypeId==null){
			postTypeId= bean.getTopic().getPostType().getId();
		}
		if(postTypeId==null){
			postTypeId= ((BbsPostType)postTypeMng.getList(site.getId(), null, null).get(0)).getId();
		}
		if(postTypeId==null){
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_POSTTYPE);
		}
		*/
		//content=filterUserInputContent(content);
		WebErrors errors=WebErrors.create(request);
		if(StringUtils.isBlank(content)){
			errors.addErrorCode("operate.faile");
		}else{
			if(configMng.get().getSensitivityInputOn()){
				if(sensitivityMng.txtHasSensitivity(site.getId(), content)){
					errors.addErrorCode("error.postTxtHasSensitivity");
				}
			}
		}
		//检查文件后缀
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String allowSuffix=site.getConfig().getAllowSuffix();
		List<MultipartFile>files=multipartRequest.getFiles("attachment");
		if(checkFiles(allowSuffix, files)){
			errors.addErrorCode("upload.forbidden");
		}
		if(errors.hasErrors()){
			return FrontUtils.showError(request, response, model, errors);
		}
		String ip = RequestUtils.getIpAddr(request);
		String userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();
		Short equip=CheckMobile.getSource(userAgent);
		bean = manager.reply(user.getId(), site.getId(), topicId,parentId,
				content, ip,
				multipartRequest.getFiles("attachment"),null, code,equip,null,null);
		log.info("save BbsPost id={}", bean.getId());
		bbsUserGroupMng.findNearByPoint(user.getTotalPoint(), user);
		return "redirect:" + bean.getRedirectUrl();
	}
	
	@RequestMapping("/post/o_saveAjax.jspx")
	public void saveAjax(BbsPost bean, Integer topicId,Integer parentId,
			String content,
			@RequestParam(value = "code", required = false) List<String> code,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		JSONObject json=new JSONObject();
		try {
			if (user == null) {
				json.put("status", -1);
			}else{
				String msg = checkReply(request,bbsTopicMng.findById(topicId).getForum(), user,
						site);
				if (msg != null) {
					json.put("status", -2);
				}else{
					boolean flag = topicCountEhCache.getLastReply(user.getId(), user
							.getGroup().getPostInterval());
					if (!flag) {
						json.put("status", -3);
					}else{
						WebErrors errors=WebErrors.create(request);
						if(StringUtils.isBlank(content)){
							errors.addErrorCode("operate.faile");
						}else{
							if(configMng.get().getSensitivityInputOn()){
								if(sensitivityMng.txtHasSensitivity(site.getId(), content)){
									errors.addErrorCode("error.postTxtHasSensitivity");
								}
							}
						}
						//检查文件后缀
						List<MultipartFile> attachments=null;
						if(request instanceof MultipartHttpServletRequest){
							MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
							String allowSuffix=site.getConfig().getAllowSuffix();
							List<MultipartFile>files=multipartRequest.getFiles("attachment");
							if(checkFiles(allowSuffix, files)){
								errors.addErrorCode("upload.forbidden");
							}
							attachments=multipartRequest.getFiles("attachment");
						}
						if(errors.hasErrors()){
							json.put("status", -4);
						}else{
							String ip = RequestUtils.getIpAddr(request);
							String userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();
							Short equip=CheckMobile.getSource(userAgent);
							bean = manager.reply(user.getId(), site.getId(), topicId,parentId,
									content, ip,
									attachments,null, code,equip,null,null);
							log.info("save BbsPost id={}", bean.getId());
							bbsUserGroupMng.findNearByPoint(user.getTotalPoint(), user);
							json.put("status", 1);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	@RequestMapping("/post/o_updateAjax.jspx")
	public void updateAjax(BbsPost bean, Integer postId,
			String content, Integer pageNo, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "code", required = false) List<String> code,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		JSONObject json=new JSONObject();
		try {
			if (user == null) {
				json.put("status", -1);
			}else{
				String msg = checkEdit(request,manager.findById(postId).getTopic().getForum(),
						manager.findById(postId), user, site);
				if (msg != null) {
					json.put("status", -2);
				}else{
					String ip = RequestUtils.getIpAddr(request);
					List<MultipartFile>files=null;
					if(request instanceof MultipartHttpServletRequest){
						MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
						files=multipartRequest.getFiles("attachment");
					}
					//content=filterUserInputContent(content);
					WebErrors errors=WebErrors.create(request);
					if(StringUtils.isBlank(content)){
						errors.addErrorCode("operate.faile");
					}else{
						if(configMng.get().getSensitivityInputOn()){
							if(sensitivityMng.txtHasSensitivity(site.getId(), content)){
								errors.addErrorCode("error.postTxtHasSensitivity");
							}
						}
					}
					String allowSuffix=site.getConfig().getAllowSuffix();
					if(checkFiles(allowSuffix, files)){
						errors.addErrorCode("upload.forbidden");
					}
					if(errors.hasErrors()){
						json.put("status", -4);
					}else{
						bean = manager.updatePost(postId,content, user, ip,
								files, code,null);
						log.info("update BbsPost id={}.", bean.getId());
						json.put("status", 1);
					}
				}
			}
		} catch (Exception e) {
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@RequestMapping("/post/o_update.jspx")
	public String update(BbsPost bean, Integer postId,
			String content, Integer pageNo, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "code", required = false) List<String> code,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		String msg = checkEdit(request,manager.findById(postId).getTopic().getForum(),
				manager.findById(postId), user, site);
		if (msg != null) {
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		String ip = RequestUtils.getIpAddr(request);
	
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		//content=filterUserInputContent(content);
		WebErrors errors=WebErrors.create(request);
		if(StringUtils.isBlank(content)){
			errors.addErrorCode("operate.faile");
		}else{
			if(configMng.get().getSensitivityInputOn()){
				if(sensitivityMng.txtHasSensitivity(site.getId(), content)){
					errors.addErrorCode("error.postTxtHasSensitivity");
				}
			}
		}
		String allowSuffix=site.getConfig().getAllowSuffix();
		List<MultipartFile>files=multipartRequest.getFiles("attachment");
		if(checkFiles(allowSuffix, files)){
			errors.addErrorCode("upload.forbidden");
		}
		if(errors.hasErrors()){
			return FrontUtils.showError(request, response, model, errors);
		}
		bean = manager.updatePost(postId,content, user, ip,
				multipartRequest.getFiles("attachment"), code,null);
		log.info("update BbsPost id={}.", bean.getId());
		return "redirect:" + bean.getRedirectUrl();
	}

	@RequestMapping("/post/v_quote{postId}.jspx")
	public String quote(@PathVariable Integer postId, Integer pid,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsPost post = null;
		if (postId != null) {
			post = manager.findById(postId);
		} else if (pid != null) {
			post = manager.findById(pid);
		}
		model.put("topicId", post.getTopic().getId());
		if (post != null) {
			String msg = checkReply(request,post.getTopic().getForum(), user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			model.put("post", post);
			model.put("topicId", post.getTopic().getId());
			//model.put("postTypeId", post.getPostType().getId());
		}
		model.put("otype", 1);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_POST, TPL_POSTADD);
	}

	@RequestMapping("/post/v_reply{postId}.jspx")
	public String reply(@PathVariable Integer postId, Integer pid,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsPost post = null;
		if (postId != null) {
			post = manager.findById(postId);
		} else if (pid != null) {
			post = manager.findById(pid);
			//model.put("postTypeId", post.getPostType().getId());
		}
		if (post != null) {
			String msg = checkReply(request,post.getTopic().getForum(), user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			model.put("post", post);
			model.put("topicId", post.getTopic().getId());
			model.put("parentId", postId);
			//model.put("postTypeId", post.getPostType().getId());
		}
		model.put("otype", 2);
		
		return FrontUtils.getTplPath(request, site,
				TPLDIR_POST, TPL_POSTADD);
	}

	@RequestMapping("/post/v_grade{postId}.jspx")
	public String grade(@PathVariable Integer postId, Integer pid,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsPost post = null;
		if (postId != null) {
			post = manager.findById(postId);
		} else if (pid != null) {
			post = manager.findById(pid);
		}
		if (post != null) {
			String msg = checkGrade(request,post.getTopic().getForum(), user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			model.put("post", post);
		}
		return FrontUtils.getTplPath(request, site,
				TPLDIR_POST, TPL_POST_GRADE);
	}

	@RequestMapping("/post/o_grade.jspx")
	public String gradeSubmit(BbsGrade bean, Integer postId,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if (postId != null) {
			BbsPost post = manager.findById(postId);
			String msg = checkGrade(request,post.getTopic().getForum(), user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			bbsGradeMng.saveGrade(bean, user, post);
			return "redirect:" + post.getRedirectUrl();
		}
		return FrontUtils.getTplPath(request, site,
				TPLDIR_POST, TPL_POSTADD);
	}

	@RequestMapping("/post/v_shield{postId}_{status}.jspx")
	public String shield(@PathVariable Integer postId, Integer pid,@PathVariable Short status,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsPost post = null;
		if (postId != null) {
			String msg = checkShield(request,manager.findById(postId).getTopic()
					.getForum(), user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			post = manager.shield(postId, null, user,status);
		} else if (pid != null) {
			String msg = checkShield(request,manager.findById(pid).getTopic()
					.getForum(), user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			post = manager.shield(pid, null, user,status);
		}
		return "redirect:" + post.getRedirectUrl();
	}

	@RequestMapping("/post/o_shield.jspx")
	public String shieldSubmit(Integer postId, HttpServletRequest request,
			ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		BbsPost post = null;
		if (postId != null) {
			post = manager.findById(postId);
		}
		if (post != null) {
			String msg = checkShield(request,post.getTopic().getForum(), user, site);
			if (msg != null) {
				model.put("msg", msg);
				return FrontUtils.getTplPath(request, site,
						TPLDIR_TOPIC, TPL_NO_VIEW);
			}
			model.put("post", post);
			model.put("topicId", post.getTopic().getId());
			//model.put("postTypeId", post.getPostType().getId());
		}
		return FrontUtils.getTplPath(request, site,
				TPLDIR_POST, TPL_POSTADD);
	}

	@RequestMapping("/member/o_prohibit.jspx")
	public String prohibit(Integer postId, Integer userId,
			HttpServletRequest request, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		if (user == null) {
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_LOGIN);
		}
		if (!user.getModerator()) {
			String msg=MessageResolver.getMessage(request, "login.groupAccessForbidden",user.getGroup().getName());
			model.put("msg", msg);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_TOPIC, TPL_NO_VIEW);
		}
		BbsUser bbsuser = bbsUserMng.findById(userId);
		BbsPost post = manager.findById(postId);
		bbsuser.setProhibitPost(BbsUser.PROHIBIT_FOREVER);
		return "redirect:" + post.getRedirectUrl();
	}

	@RequestMapping("/post/o_delete.jspx")
	public String delete(Integer[] ids, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		if(ids!=null){
			BbsPost[] beans = manager.deleteByIds(ids);
			for (BbsPost bean : beans) {
				log.info("delete BbsPost id={}", bean.getId());
			}
			if(beans[0]!=null){
				return "redirect:" + beans[0].getRedirectUrl();
			}else{
				return "redirect:/index.jhtml";
			}
		}else{
			return "redirect:/index.jhtml";
		}
		
	}
	
	@RequestMapping("/post/v_list_json.jspx")
	public void getJsonList(Integer topicId,Integer parentId,
			Integer first, Integer count,
			HttpServletRequest request,HttpServletResponse  response) {
		if(count==null){
			count=5;
		}
		if(first==null){
			first=0;
		}
		List<BbsPost>list=null;
		if(topicId!=null){
			list=manager.getPostByTopic(topicId,parentId,null, true,count, first);
		}
		JSONArray array=new JSONArray();
		if(list!=null&&list.size()>0){
			SimpleDateFormat format =new SimpleDateFormat("yy-MM-dd HH:mm");
			try {
				for(int i=0;i<list.size();i++){
					BbsPost post=list.get(i);
					JSONObject object = new JSONObject();
					object.put("username", post.getCreater().getUsername());
					object.put("createTime",format.format(post.getCreateTime()));
					object.put("group", post.getCreater().getGroup().getName());
					object.put("avatar", post.getCreater().getAvatar());
					object.put("content", post.getContentHtml());
					object.put("title", post.getTopic().getTitle());
					object.put("url", post.getUrl());
					array.put(object);
				}
			}catch (JSONException e) {
				//	e.printStackTrace();
			}
		}
		ResponseUtils.renderJson(response, array.toString());
	}
	
	/**
	 * 帖子点赞
	 * @param postId 
	 * @param operate 0点赞 3取消点赞
	 */
	@RequestMapping(value = "/post/up.jspx")
	public void postUp(Integer postId, Integer operate,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws JSONException {
		BbsUser user = CmsUtils.getUser(request);
		JSONObject object = new JSONObject();
		if (user == null) {
			object.put("result", false);
		} else {
			object.put("result", true);
			topicPostOperateMng.postOperate(postId, user.getId(), operate);
			if(operate!=null){
				if(operate.equals(BbsTopicPostOperate.OPT_CANCEL_UP)){
					bbsPostCountMng.postCancelUp(postId);
				}else if(operate.equals(BbsTopicPostOperate.OPT_UP)){
					bbsPostCountMng.postUp(postId);
				}
			}
		}
		ResponseUtils.renderJson(response, object.toString());
	}
	
	@RequestMapping(value = "/post/getPost.jspx")
	public String getPost(Integer postId,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		CmsSite site = CmsUtils.getSite(request);
		if(postId!=null){
			BbsPost post=manager.findById(postId);
			model.put("post", post);
		}
		FrontUtils.frontData(request, model, site);
		return FrontUtils.getTplPath(request, site,
				TPLDIR_POST, TPL_POST_CONTENT);
	}
	
	@RequestMapping(value = "/post/ajaxGetPost.jspx")
	public void ajaxGetPost(Integer postId,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		BbsPost post=null;
		BbsUser user=CmsUtils.getUser(request);
		JSONObject json=new JSONObject();
		String content="";
		if(postId!=null){
			post=manager.findById(postId);
			if(post!=null){
				String haveReplys=post.getTopic().getHaveReply();
				if(post.isShield()){
					if(user!=null&&user.getModerator()){
						content=post.getContentHtml();
					}else{
						content=post.getShieldContent();
					}
				}else if(post.getHidden()){
					if(user!=null){
						if(user.getId().equals(post.getCreater().getId())||user.getModerator()){
							content=post.getContentHtml();
						}else if(StringUtils.isNotBlank(haveReplys)&&haveReplys.contains(","+user.getId()+",")){
							content=post.getContentHtml();
						}else{
							content=post.getHideContent();
						}
					}else{
						content=post.getHideContent();
					}
				}else {
					content=post.getContentHtml();
				}
			}
		}
		json.put("content", content);
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@RequestMapping("/post/listChild.jspx")
	public String getChildList(Integer parentId,
			Integer orderBy,Integer pageNo,HttpServletRequest request, 
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		FrontUtils.frontData(request, model, site);
		FrontUtils.frontPageData(request, model);
		BbsPost post = null;
		BbsUser user=CmsUtils.getUser(request);
		if (parentId != null) {
			post = manager.findById(parentId);
		}
		if(pageNo==null){
			pageNo=1;
		}
		model.put("parentId", parentId);
		model.put("post", post);
		if(post!=null){
			BbsForum forum=post.getTopic().getForum();
			if(orderBy==null){
				orderBy= CookieUtils.getOrderBy(forum.getId(), request);
			}
			model.put("topic", post.getTopic());
			model.put("moderators", checkModerators(forum, user, site));
			model.put("forum", forum);
			model.put("orderBy",orderBy);
			model.addAttribute("pageNo", pageNo);
			return FrontUtils.getTplPath(request, site,
					TPLDIR_POST, TPL_POST_CHILD);
		}else{
			return FrontUtils.pageNotFound(request, response, model);
		}
	}
	

	// public String checkIp(String ip){
	//		
	// return "";
	// }

	private String checkReply(HttpServletRequest request,BbsForum forum, BbsUser user, CmsSite site) {
		String msg="";
		if (forum.getGroupReplies() == null) {
			msg=MessageResolver.getMessage(request, "login.groupAccessForbidden",user.getGroup().getName());
			return msg;
		} else {
			BbsUserGroup group = user.getGroup();
			if (user.getProhibit()) {
				msg=MessageResolver.getMessage(request, "member.gag");
				return msg;
			}
			if (group == null) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.allowReply()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (forum.getGroupReplies().indexOf("," + group.getId() + ",") == -1) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.checkPostToday(user.getPostToday())) {
				msg=MessageResolver.getMessage(request, "member.posttomany");
				return msg;
			}
		}
		String ip=RequestUtils.getIpAddr(request);
		boolean ipLimit=bbsLimitMng.ipIsLimit(ip);
		boolean userLimit=bbsLimitMng.userIsLimit(user.getId());
		if(ipLimit){
			msg=MessageResolver.getMessage(request, "member.ipforbidden");
			return msg;
		}
		if(userLimit){
			msg=MessageResolver.getMessage(request, "member.userforbidden");
			return msg;
		}
		return null;
	}

	private String checkEdit(HttpServletRequest request,BbsForum forum, BbsPost post, BbsUser user,
			CmsSite site) {
		String msg="";
		if (forum.getGroupReplies() == null) {
			msg=MessageResolver.getMessage(request, "member.hasnopermission");
			return msg;
		} else {
			if (user == null) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			BbsUserGroup group = user.getGroup();
			//if (!post.getCreater().equals(user)) {
			//	return "不能编辑别人的帖子";
			//}
			if (forum.getGroupReplies().indexOf("," + group.getId() + ",") == -1) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
		}
		String ip=RequestUtils.getIpAddr(request);
		boolean ipLimit=bbsLimitMng.ipIsLimit(ip);
		boolean userLimit=bbsLimitMng.userIsLimit(user.getId());
		if(ipLimit){
			msg=MessageResolver.getMessage(request, "member.ipforbidden");
			return msg;
		}
		if(userLimit){
			msg=MessageResolver.getMessage(request, "member.userforbidden");
			return msg;
		}
		return null;
	}

	private String checkGrade(HttpServletRequest request,BbsForum forum, BbsUser user, CmsSite site) {
		String msg="";
		if (forum.getGroupReplies() == null) {
			msg=MessageResolver.getMessage(request, "member.hasnopermission");
			return msg;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (forum.getGroupReplies().indexOf("," + group.getId() + ",") == -1) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (user.getGradeToday() != null
					&& user.getGradeToday() >= group.getGradeNum()) {
				msg=MessageResolver.getMessage(request, "member.doesnomark");
				return msg;
			}
		}
		return null;
	}

	private String checkShield(HttpServletRequest request, BbsForum forum, BbsUser user, CmsSite site) {
		String msg;
		if (forum.getGroupTopics() == null) {
			msg=MessageResolver.getMessage(request, "member.hasnopermission");
			return msg;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.allowTopic()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (forum.getGroupTopics().indexOf("," + group.getId() + ",") == -1) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.hasRight(forum, user)) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
			if (!group.topicShield()) {
				msg=MessageResolver.getMessage(request, "member.hasnopermission");
				return msg;
			}
		}
		return null;
	}
	
	private boolean checkFiles(String allowSuffix,List<MultipartFile>files){
		//不为空设置检查
		if(StringUtils.isNotBlank(allowSuffix)){
			String[] exts=allowSuffix.split(",");
			for(MultipartFile file:files){
				String origName = file.getOriginalFilename();
				String ext = FilenameUtils.getExtension(origName).toLowerCase(Locale.ENGLISH);
				//文件格式检查
				if(isNotInArray(exts, ext)){
					return true;
				}
			}
			return false;
		}else{
			return false;
		}
	}
	
	private boolean isNotInArray(String[] exts,String ext){
		if(exts!=null&&exts.length>0){
			for(String e:exts){
				if(e.equals(ext)){
					return false;
				}
			}
			return true;
		}else{
			//exts为空
			return true;
		}
	}

	private final static Whitelist user_content_filter = Whitelist.relaxed();
	static {
		user_content_filter.addTags("embed","object","param","span","div");
		user_content_filter.addAttributes(":all", "style", "class", "id", "name");
		user_content_filter.addAttributes("object", "width", "height","classid","codebase");	
		user_content_filter.addAttributes("param", "name", "value");
		user_content_filter.addAttributes("embed", "src","quality","width","height","allowFullScreen","allowScriptAccess","flashvars","name","type","pluginspage");
	}

	/**
	 * 对用户输入内容进行过滤
	 * @param html
	 * @return
	 */
	public static String filterUserInputContent(String html) {
		if(StringUtils.isBlank(html)) return "";
		return Jsoup.clean(html, user_content_filter);
	}
	
	private boolean checkModerators(BbsForum forum, BbsUser user, CmsSite site) {
		if (forum.getGroupViews() == null) {
			return false;
		} else {
			BbsUserGroup group = null;
			if (user == null) {
				group = bbsConfigMng.findById(site.getId()).getDefaultGroup();
			} else {
				group = user.getGroup();
			}
			if (group == null) {
				return false;
			}
			if (!group.hasRight(forum, user)) {
				return false;
			}
		}
		return true;
	}

	@Autowired
	private BbsPostMng manager;
	@Autowired
	private BbsTopicMng bbsTopicMng;
	@Autowired
	private BbsGradeMng bbsGradeMng;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsConfigMng bbsConfigMng;
	@Autowired
	private TopicCountEhCache topicCountEhCache;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
	@Autowired
	private BbsLimitMng bbsLimitMng;
	@Autowired
	private BbsTopicPostOperateMng topicPostOperateMng;
	@Autowired
	private BbsPostCountMng bbsPostCountMng;
	@Autowired
	private CmsSensitivityMng sensitivityMng;
	@Autowired
	private CmsConfigMng configMng;
}
