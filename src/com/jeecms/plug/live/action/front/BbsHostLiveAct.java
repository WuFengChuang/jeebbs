package com.jeecms.plug.live.action.front;

import static com.jeecms.bbs.Constants.TPLDIR_PLUG;
import static com.jeecms.common.page.SimplePage.cpn;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsAccountDrawMng;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.bbs.web.FrontUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.PropertyUtils;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.core.entity.CmsConfig;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsConfigMng;
import com.jeecms.core.web.WebErrors;
import com.jeecms.core.web.front.URLHelper;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.entity.BbsLiveChapter;
import com.jeecms.plug.live.entity.BbsLiveRate;
import com.jeecms.plug.live.manager.BbsLiveApplyMng;
import com.jeecms.plug.live.manager.BbsLiveChapterMng;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.manager.BbsLiveRateMng;
import com.jeecms.plug.live.manager.BbsLiveUserAccountMng;
import com.jeecms.plug.live.manager.BbsLiveUserMng;
import com.jeecms.plug.live.util.BaiduCloudUtil;
import com.jeecms.plug.live.util.TencentCloudUtil;

/**
 * 活动live管理
 */
@Controller
public class BbsHostLiveAct extends AbstractBbsLive{
	 private static final Logger log = LoggerFactory
	 .getLogger(BbsHostLiveAct.class);

	public static final String TPL_LIVE_INDEX = "tpl.liveHostIndex";
	public static final String TPL_LIVE_LOAD = "tpl.liveLoad";
	public static final String TPL_LIVE_MAIN = "tpl.liveMain";
	public static final String TPL_LIVE_LEFT = "tpl.liveLeft";
	public static final String TPL_LIVE_TREE = "tpl.liveTree";
	public static final String TPL_LIVE_LIST = "tpl.liveList";
	public static final String TPL_LIVE_EDIT = "tpl.liveEdit";
	public static final String TPL_LIVE_VIEWREASON = "tpl.liveViewReason";
	public static final String TPL_LIVE_ADD = "tpl.liveAdd";
	public static final String TPL_LIVE_STATISTICS = "tpl.liveStatistics";
	public static final String TPL_LIVE_TICKET_USER_INDEX = "tpl.liveTicketUserIndex";
	public static final String TPL_LIVE_TICKET_List = "tpl.liveTicketList";
	public static final String TENCENT_PUSH_BASE_URL="tencent.video.cloud.push.url";
	public static final String TENCENT_PLAY_BASE_URL="tencent.video.cloud.play.url";
	public static final String TPL_LIVE_GET_PUSH_URL = "tpl.liveGetPushUrl";

	@RequestMapping("/live/host/index.jspx")
	public String index(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_INDEX);
	}
	
	@RequestMapping("/live/host/load.jspx")
	public String load(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_LOAD);
	}
	
	@RequestMapping("/live/host/main.jspx")
	public String main(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_MAIN);
	}
	
	@RequestMapping("/live/host/v_left.jspx")
	public String left(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_LEFT);
	}

	@RequestMapping(value = "/live/host/v_tree.jspx")
	public String tree(String root, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		log.debug("tree path={}", root);
		CmsSite site=CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		boolean isRoot;
		// jquery treeview的根请求为root=source
		if (StringUtils.isBlank(root) || "source".equals(root)) {
			isRoot = true;
		} else {
			isRoot = false;
		}
		model.addAttribute("isRoot", isRoot);
		WebErrors errors = validateTree(root, request);
		if (errors.hasErrors()) {
			log.error(errors.getErrors().get(0));
			ResponseUtils.renderJson(response, "[]");
			return null;
		}
		List<BbsLiveChapter> list;
		Integer userId = CmsUtils.getUserId(request);
		if (isRoot) {
			list = liveChapterMng.getTopList(userId);
		} else {
			Integer rootId = Integer.valueOf(root);
			list = liveChapterMng.getChildList(userId,rootId);
		}
		model.addAttribute("list", list);
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/json;charset=UTF-8");
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_TREE);
	}
	
	@RequestMapping(value = "/live/host/list.jspx")
	public String list(Integer cid,String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,
			Date qFinishTimeBegin,Date qFinishTimeEnd,Integer qorderBy,
			Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		CmsConfig config=site.getConfig();
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		Pagination pagination=manager.getPage(cid, qtitle, user.getId(),
				qstatus, qtimeBegin, qtimeEnd,
				qFinishTimeBegin,qFinishTimeEnd,qorderBy, 
				cpn(pageNo), CookieUtils.getPageSize(request));
		Map<String,String>pushUrlMap=new HashMap<>();
		List<BbsLive>lives=(List<BbsLive>) pagination.getList();
		//需要判断当前平台选择的视频云平台类型
		initTencentPushBaseUrl();
		for(BbsLive live:lives){
			if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_TENCENT)){
				String url=TencentCloudUtil.getPushUrl(getTencentPushBaseUrl(),
						config.getTencentBizId(), 
						config.getTencentPushFlowKey(), user.getId(), live.getEndTime());
				pushUrlMap.put(live.getId().toString(), url);
			}else if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_BAIDU)){
				String url=BaiduCloudUtil.getPushUrl(config.getBaiduPushDomain(), 
						user.getId(), live.getEndTime(), config.getBaiduStreamSafeKey());
				pushUrlMap.put(live.getId().toString(), url);
			}
		}
		model.addAttribute("pagination", pagination);
		model.addAttribute("pushUrlMap", pushUrlMap);
		appendQuery(model, cid, qtitle, qusername, qstatus,
				qtimeBegin, qtimeEnd, qorderBy);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_LIST);
	}
	
	@RequestMapping(value = "/live/host/add.jspx")
	public String add(Integer cid,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		BbsLiveChapter c;
		if (cid != null) {
			c = liveChapterMng.findById(cid);
		} else {
			c = null;
		}
		List<BbsLiveChapter>chapterList;
		if (c != null) {
			chapterList = c.getListForSelect();
		} else {
			List<BbsLiveChapter>topList=liveChapterMng.getTopList(user.getId());
			chapterList=BbsLiveChapter.getListForSelect(topList, null);
		}
		model.addAttribute("chapterList", chapterList);
		model.addAttribute("chapter", c);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_ADD);
	}
	
	@RequestMapping(value = "/live/host/edit.jspx")
	public String edit(Integer id,Integer cid,String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,Integer qorderBy,
			Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		WebErrors errors = validateEdit(id, user,request);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		List<BbsLiveChapter>topList=liveChapterMng.getTopList(user.getId());
		List<BbsLiveChapter>chapterList=BbsLiveChapter.getListForSelect(topList, null);
		model.addAttribute("live", manager.findById(id));
		model.addAttribute("chapterList", chapterList);
		appendQuery(model, cid, qtitle, qusername, qstatus,
				qtimeBegin, qtimeEnd, qorderBy);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_EDIT);
	}
	
	@RequestMapping(value = "/live/host/save.jspx")
	public String save(
			Integer chapterId,String title,String description,
			String liveLogo,Date beginTime,Date endTime,
			Integer limitUserNum,
			Date qFinishTimeBegin,Date qFinishTimeEnd,Double beginPrice,
			Integer cid,String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,Integer qorderBy,
			Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		WebErrors errors = validateSave(title, beginTime, endTime,chapterId, request);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		CmsConfig config=cmsConfigMng.get();
		Boolean liveNeedCheck=config.getLiveCheck();
		BbsLive live=new BbsLive();
		
		live.setAfterPrice(beginPrice);
		live.setBeginPrice(beginPrice);
		live.setBeginTime(beginTime);
		if(liveNeedCheck){
			live.setCheckStatus(BbsLive.CHECKING);
		}else{
			live.setCheckStatus(BbsLive.CHECKED);
		}
		live.setCreateTime(Calendar.getInstance().getTime());
		live.setDescription(description);
		live.setEndTime(endTime);
		live.setLimitUserNum(limitUserNum);
		live.setLiveLogo(liveLogo);
		live.setTitle(title);
		live.init();
		live.setUser(user);
		String liveUrl = "",demandUrl = "",liveMobileUrl = "", demandImageUrl = "";
		String livePlatStr=config.getLivePlat();
		if(StringUtils.isNotBlank(livePlatStr)){
			if(livePlatStr.equals(BbsLive.LIVE_PLAT_STR_TENCENT)){
				live.setLivePlat(BbsLive.LIVE_PLAT_TENCENT);
			}else if(livePlatStr.equals(BbsLive.LIVE_PLAT_STR_BAIDU)){
				live.setLivePlat(BbsLive.LIVE_PLAT_BAIDU);
			}
		}else{
			live.setLivePlat(BbsLive.LIVE_PLAT_TENCENT);
		}
		//需要判断当前平台选择的视频云平台类型
		if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_TENCENT)){
			initTencentPushBaseUrl();
			initTencentPlayBaseUrl();
			liveUrl=TencentCloudUtil.getRtmpPlayUrl(getTencentPlayBaseUrl(),
					config.getTencentBizId(), user.getId());
			demandUrl=TencentCloudUtil.getPlayUrl(getTencentPlayBaseUrl(),
					config.getTencentBizId(), user.getId(), "flv");
			liveMobileUrl=TencentCloudUtil.getPlayUrl(getTencentPlayBaseUrl(),
					config.getTencentBizId(), user.getId(), "m3u8");
		}else if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_BAIDU)){
			liveUrl=BaiduCloudUtil.getRtmpPlayUrl(config.getBaiduPlayDomain(),
					user.getId(),live.getEndTime(), config.getBaiduStreamSafeKey());
			demandUrl=BaiduCloudUtil.getPlayUrl(config.getBaiduPlayDomain(), 
					user.getId(),live.getEndTime(), config.getBaiduStreamSafeKey(),"flv");
			liveMobileUrl=BaiduCloudUtil.getPlayUrl(config.getBaiduPlayDomain(), 
					user.getId(),live.getEndTime(), config.getBaiduStreamSafeKey(),"m3u8");
		}
		live.setLiveUrl(liveUrl);
		live.setDemandUrl(demandUrl);
		if(StringUtils.isNotBlank(liveMobileUrl)){
			live.setLiveMobileUrl(liveMobileUrl);
		}
		if(StringUtils.isNotBlank(demandImageUrl)){
			live.setDemandImageUrl(demandImageUrl);
		}
		live.setChapter(liveChapterMng.findById(chapterId));
		live.setSite(site);
		if(limitUserNum!=null){
			BbsLiveRate rate=liveRateMng.findByNearLimitNum(limitUserNum);
			if(rate!=null){
				live.setCommissionRate(rate.getRate());
			}
		}
		if(live.getCommissionRate()==null){
			live.setCommissionRate(0d);
		}
		manager.save(live);
		return list(cid, qtitle, qusername, qstatus, qtimeBegin,
				qtimeEnd, qFinishTimeBegin,qFinishTimeEnd,
				qorderBy, pageNo, request, response, model);
	}
	
	@RequestMapping(value = "/live/host/update.jspx")
	public String update(
			Integer id,Integer chapterId,String title,String description,
			String liveLogo,Date beginTime,Date endTime,
			Date qFinishTimeBegin,Date qFinishTimeEnd,Double beginPrice,
			Double afterPrice,Integer limitUserNum,
			String livePlatKey,Short livePlat,
			Integer cid,String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,Integer qorderBy,
			Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		WebErrors errors = validateUpdate(id, title, beginTime,
				endTime, chapterId, user,request);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		updateLive(id, chapterId, title, description,
				liveLogo, beginTime, endTime, beginPrice,
				afterPrice, limitUserNum, livePlatKey, livePlat, null,cid);
		return list(cid, qtitle, qusername, qstatus, qtimeBegin,
				qtimeEnd,qFinishTimeBegin,qFinishTimeEnd,
				qorderBy, pageNo, request, response, model);
	}
	
	@RequestMapping(value = "/live/host/updateToApply.jspx")
	public String updateToApply(
			Integer id,Integer chapterId,String title,String description,
			String liveLogo,Date beginTime,Date endTime,
			Date qFinishTimeBegin,Date qFinishTimeEnd,Double beginPrice,
			Double afterPrice,Integer limitUserNum,
			String livePlatKey,Short livePlat,
			Integer cid,String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,Integer qorderBy,
			Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		WebErrors errors = validateUpdate(id, title, beginTime,
				endTime, chapterId, user,request);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		updateLive(id, chapterId, title, description,
				liveLogo, beginTime, endTime, beginPrice,
				afterPrice, limitUserNum, 
				livePlatKey, livePlat,BbsLive.CHECKING, 
				cid);
		return list(cid, qtitle, qusername, qstatus, qtimeBegin,
				qtimeEnd,qFinishTimeBegin,qFinishTimeEnd,
				qorderBy, pageNo, request, response, model);
	}
	
	@RequestMapping("/live/host/delete.jspx")
	public String delete(Integer[] ids,Integer cid,
			String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,
			Date qFinishTimeBegin,Date qFinishTimeEnd,Integer qorderBy,
			Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		WebErrors errors = validateDelete(ids,user, request);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		BbsLive[] beans = manager.deleteByIds(ids);
		for (BbsLive bean : beans) {
			log.info("delete BbsLiveChapter id={}", bean.getId());
		}
		return list(cid, qtitle, qusername, qstatus,
				qtimeBegin, qtimeEnd,qFinishTimeBegin,qFinishTimeEnd,
				qorderBy, pageNo, request, response, model);
	}
	
	@RequestMapping(value = "/live/host/getLiveRate.jspx")
	public void getRate(Integer userNum,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		JSONObject json=new JSONObject();
		try {
			json.put("rate", 0);
			if(userNum!=null){
				BbsLiveRate rate=liveRateMng.findByNearLimitNum(userNum);
				if(rate!=null){
					json.put("rate", rate.getRate());
				}
			}
			}catch (JSONException e) {
				e.printStackTrace();
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	@RequestMapping(value = "/live/host/statistic.jspx")
	public String statistic(Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		if (pageNo==null) {
			pageNo=1;
		}
		Pagination pagination=accountDrawMng.getPage(user.getId(),null,null,null,
				BbsAccountDraw.APPLY_TYPE_LIVE,cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("liveUserAccount", liveUserAccountMng.findById(user.getId()));
		model.addAttribute("tag_pagination", pagination);
		model.addAttribute("pageNo", pageNo);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_STATISTICS);
	}
	
	@RequestMapping(value = "/live/host/ticklist*.jspx")
	public String liveJoinUserlist(Integer liveId,
			HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		WebErrors errors=WebErrors.create(request);
		if(liveId==null){
			errors.addErrorCode("error.required", liveId);
		}
		if(liveId!=null){
			BbsLive live=manager.findById(liveId);
			if(!live.getUser().equals(user)){
				errors.addErrorCode("live.error.notYourLive");
			}
		}
		if(errors.hasErrors()){
			return FrontUtils.showError(request, response, model, errors);
		}
		Pagination pagination=liveUserMng.getPage(null, null, liveId,
				URLHelper.getPageNo(request), CookieUtils.getPageSize(request));
		model.addAttribute("tag_pagination", pagination);
		model.addAttribute("liveId", liveId);
		FrontUtils.frontPageData(request, model);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_TICKET_List);
	}
	
	@RequestMapping(value = "/live/host/viewReason.jspx")
	public String viewReason(Integer id,Integer cid,String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,Integer qorderBy,
			Integer pageNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		CmsSite site = CmsUtils.getSite(request);
		BbsUser user = CmsUtils.getUser(request);
		FrontUtils.frontData(request, model, site);
		String hostCheck=hostCheck(user, site, request, response, model);
		if(StringUtils.isNotBlank(hostCheck)){
			return hostCheck;
		}
		WebErrors errors = validateEdit(id, user,request);
		if (errors.hasErrors()) {
			return FrontUtils.showError(request, response, model, errors);
		}
		model.addAttribute("live", manager.findById(id));
		appendQuery(model, cid, qtitle, qusername, qstatus,
				qtimeBegin, qtimeEnd, qorderBy);
		return FrontUtils.getTplPath(request, site.getSolutionPath(),
				TPLDIR_PLUG, TPL_LIVE_VIEWREASON);
	}
	
	private BbsLive  updateLive(
			Integer id,Integer chapterId,String title,String description,
			String liveLogo,Date beginTime,Date endTime,
			Double beginPrice,Double afterPrice,Integer limitUserNum,
			String livePlatKey,Short livePlat,Short checkStatus,
			Integer cid){
		BbsLive live=manager.findById(id);
		if(live!=null){
			CmsConfig config=cmsConfigMng.get();
			Integer hostId=live.getUser().getId();
			if(afterPrice!=null){
				live.setAfterPrice(afterPrice);
			}
			if(beginPrice!=null){
				live.setBeginPrice(beginPrice);
			}
			if(beginTime!=null){
				live.setBeginTime(beginTime);
			}
			if(StringUtils.isNotBlank(description)){
				live.setDescription(description);
			}
			if(endTime!=null){
				live.setEndTime(endTime);
			}
			if(limitUserNum!=null){
				live.setLimitUserNum(limitUserNum);
			}
			if(StringUtils.isNotBlank(liveLogo)){
				live.setLiveLogo(liveLogo);
			}
			if(StringUtils.isNotBlank(title)){
				live.setTitle(title);
			}
			if(StringUtils.isNotBlank(livePlatKey)){
				live.setLivePlatKey(livePlatKey);
			}
			if(livePlat!=null){
				live.setLivePlat(livePlat);
			}else{
				live.setLivePlat(BbsLive.LIVE_PLAT_BAIDU);
			}
			live.setChapter(liveChapterMng.findById(chapterId));
			if(limitUserNum!=null){
				BbsLiveRate rate=liveRateMng.findByNearLimitNum(limitUserNum);
				if(rate!=null){
					live.setCommissionRate(rate.getRate());
				}
			}
			if(checkStatus!=null){
				live.setCheckStatus(checkStatus);
			}
			String liveUrl=live.getLiveUrl(), demandUrl=live.getDemandUrl();
			String liveMobileUrl=live.getLiveMobileUrl();
			//需要判断当前平台选择的视频云平台类型
			if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_TENCENT)){
				initTencentPushBaseUrl();
				initTencentPlayBaseUrl();
				liveUrl=TencentCloudUtil.getRtmpPlayUrl(getTencentPlayBaseUrl(),
						config.getTencentBizId(), hostId);
				demandUrl=TencentCloudUtil.getPlayUrl(getTencentPlayBaseUrl(),
						config.getTencentBizId(), hostId, "flv");
				liveMobileUrl=TencentCloudUtil.getPlayUrl(getTencentPlayBaseUrl(),
						config.getTencentBizId(), hostId, "m3u8");
			}else if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_BAIDU)){
				liveUrl=BaiduCloudUtil.getRtmpPlayUrl(config.getBaiduPlayDomain(),
						hostId,live.getEndTime(), config.getBaiduStreamSafeKey());
				demandUrl=BaiduCloudUtil.getPlayUrl(config.getBaiduPlayDomain(), 
						hostId,live.getEndTime(), config.getBaiduStreamSafeKey(),"flv");
				liveMobileUrl=BaiduCloudUtil.getPlayUrl(config.getBaiduPlayDomain(), 
						hostId,live.getEndTime(), config.getBaiduStreamSafeKey(),"m3u8");
			}
			if(StringUtils.isNotBlank(liveUrl)){
				live.setLiveUrl(liveUrl);
			}
			if(StringUtils.isNotBlank(demandUrl)){
				live.setDemandUrl(demandUrl);
			}
			if(StringUtils.isNotBlank(liveMobileUrl)){
				live.setLiveMobileUrl(liveMobileUrl);
			}
			live=manager.update(live);
		}
		return live;
	}
	
	private void initTencentPushBaseUrl(){
		if(getTencentPushBaseUrl()==null){
			setTencentPushBaseUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),TENCENT_PUSH_BASE_URL));
		}
	}
	
	private void initTencentPlayBaseUrl(){
		if(getTencentPlayBaseUrl()==null){
			setTencentPlayBaseUrl(PropertyUtils.getPropertyValue(
					new File(realPathResolver.get(com.jeecms.bbs.Constants.JEEBBS_CONFIG)),TENCENT_PLAY_BASE_URL));
		}
	}
	
	
	private void appendQuery(ModelMap model,Integer cid,String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,Integer qorderBy){
		model.addAttribute("cid", cid);
		model.addAttribute("qtitle", qtitle);
		model.addAttribute("qusername", qusername);
		model.addAttribute("qstatus", qstatus);
		model.addAttribute("qtimeBegin",qtimeBegin);
		model.addAttribute("qtimeEnd", qtimeEnd);
	}
	
	private WebErrors validateTree(String path, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		 //if (errors.ifBlank(path, "path", 255)) {
		// return errors;
		// }
		return errors;
	}
	
	private WebErrors validateEdit(Integer id,BbsUser user, 
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id,user,errors)) {
			return errors;
		}
		return errors;
	}
	
	private WebErrors validateSave(String title,Date begin,Date end,
			Integer chapterId,HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldChapterExist(chapterId,errors)) {
			return errors;
		}
		if(StringUtils.isBlank(title)){
			errors.addErrorCode("error.required", title);
		}
		if(begin==null){
			errors.addErrorCode("error.required", begin);
		}
		if(end==null){
			errors.addErrorCode("error.required", end);
		}
		return errors;
	}
	
	private WebErrors validateUpdate(Integer id,String title,Date begin,
			Date end,Integer chapterId,BbsUser user,HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id,user,errors)) {
			return errors;
		}
		if (vldChapterExist(chapterId,errors)) {
			return errors;
		}
		if(StringUtils.isBlank(title)){
			errors.addErrorCode("error.required", title);
		}
		if(begin==null){
			errors.addErrorCode("error.required", begin);
		}
		if(end==null){
			errors.addErrorCode("error.required", end);
		}
		if(vldUpdate(id)){
			errors.addErrorCode("error.live.checked");
		}
		return errors;
	}
	
	private WebErrors validateDelete(Integer[] ids,BbsUser user, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids");
		for (Integer id : ids) {
			if (vldExist(id,user,  errors)) {
				return errors;
			}
			if(vldUpdate(id)){
				errors.addErrorCode("error.live.checked");
				return errors;
			}
		}
		return errors;
	}
	
	private boolean vldExist(Integer id,BbsUser user,WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsLive entity = manager.findById(id);
		if(errors.ifNotExist(entity, BbsLive.class, id)) {
			return true;
		}
		//非主讲人
		if(!user.equals(entity.getUser())){
			errors.addErrorCode("live.error.notYourLive");
		}
		return false;
	}
	
	private boolean vldUpdate(Integer id) {
		BbsLive entity = manager.findById(id);
		if(entity.getCheckStatus().equals(BbsLive.CHECKED)){
			return true;
		}
		return false;
	}
	
	private boolean vldChapterExist(Integer id,WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsLiveChapter entity = liveChapterMng.findById(id);
		if(errors.ifNotExist(entity, BbsLiveChapter.class, id)) {
			return true;
		}
		return false;
	}

	@Autowired
	private BbsLiveMng manager;
	@Autowired
	private BbsLiveChapterMng liveChapterMng;
	@Autowired
	private CmsConfigMng cmsConfigMng;
	@Autowired
	private BbsLiveRateMng liveRateMng;
	@Autowired
	private BbsLiveUserAccountMng liveUserAccountMng;
	@Autowired
	private BbsLiveUserMng liveUserMng;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private BbsAccountDrawMng accountDrawMng;
	
	private String tencentPushBaseUrl;
	private String tencentPlayBaseUrl;

	public String getTencentPushBaseUrl() {
		return tencentPushBaseUrl;
	}

	public void setTencentPushBaseUrl(String tencentPushBaseUrl) {
		this.tencentPushBaseUrl = tencentPushBaseUrl;
	}

	public String getTencentPlayBaseUrl() {
		return tencentPlayBaseUrl;
	}

	public void setTencentPlayBaseUrl(String tencentPlayBaseUrl) {
		this.tencentPlayBaseUrl = tencentPlayBaseUrl;
	}
	
}
