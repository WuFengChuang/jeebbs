package com.jeecms.plug.live.action.admin;

import static com.jeecms.common.page.SimplePage.cpn;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.TextMessage;

import com.google.gson.GsonBuilder;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.ApplicationContextUtil;
import com.jeecms.bbs.web.CmsUtils;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.common.web.ResponseUtils;
import com.jeecms.core.entity.CmsConfig; 
import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.web.WebErrors;
import com.jeecms.plug.live.entity.BbsLive;
import com.jeecms.plug.live.entity.BbsLiveChapter;
import com.jeecms.plug.live.manager.BbsLiveChapterMng;
import com.jeecms.plug.live.manager.BbsLiveMng;
import com.jeecms.plug.live.util.BaiduCloudUtil;
import com.jeecms.plug.live.util.TencentCloudUtil;
import com.jeecms.plug.live.websocket.Message;
import com.jeecms.plug.live.websocket.WebSocketExtHandler;

@Controller
public class BbsLiveAct {
	private static final Logger log = LoggerFactory.getLogger(BbsLiveAct.class);

	@RequiresPermissions("live:v_list")
	@RequestMapping("/live/v_list.do")
	public String list(String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,
			Date qFinishTimeBegin,Date qFinishTimeEnd,Integer qorderBy,
			Integer pageNo, HttpServletRequest request, ModelMap model) {
		Integer hostUserId=null;
		if(StringUtils.isNotBlank(qusername)){
			BbsUser user=bbsUserMng.findByUsername(qusername);
			if(user!=null){
				hostUserId=user.getId();
			}else{
				hostUserId=0;
			}
		}
		Date now=Calendar.getInstance().getTime();
		Short queryDbStatus=qstatus;
		if(qstatus!=null){
			if(qstatus==1){
				//未开始
				qtimeBegin=now;
				queryDbStatus=BbsLive.CHECKED;
			}else if(qstatus==2){
				//进行中
				qFinishTimeBegin=now;
				qtimeEnd=now;	
				queryDbStatus=BbsLive.CHECKED;
			}else if(qstatus==3){
				//已结束
				qFinishTimeEnd=now;
				queryDbStatus=BbsLive.CHECKED;
			}else if(qstatus==4){
				//已拒绝
				queryDbStatus=BbsLive.REJECT;
			}else if(qstatus==5){
				//已关闭
				queryDbStatus=BbsLive.STOP;
			}
		}else{
			qstatus=BbsLive.CHECKING;
			queryDbStatus=BbsLive.CHECKING;
		}
		Pagination pagination = manager.getPage(null,qtitle,hostUserId,
				queryDbStatus,qtimeBegin,qtimeEnd,
				qFinishTimeBegin,qFinishTimeEnd,qorderBy,
				cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination",pagination);
		appendQuery(model, qtitle, qusername, qstatus,
				qtimeBegin, qtimeEnd, qorderBy);
		if(qstatus!=null){
			if(qstatus==1){
				//未开始
				return "plugPage/live/live/unbeginlist";
			}else if(qstatus==2){
				//进行中
				return "plugPage/live/live/startlist";
			}else if(qstatus==3){
				//已结束
				return "plugPage/live/live/endlist";
			}else if(qstatus==4){
				//已拒绝
				return "plugPage/live/live/rejectlist";
			}else if(qstatus==5){
				//已关闭
				return "plugPage/live/live/stoplist";
			}
			return "plugPage/live/live/checkinglist";
		}else{
			return "plugPage/live/live/checkinglist";
		}
	}

	@RequiresPermissions("live:v_edit")
	@RequestMapping("/live/v_edit.do")
	public String edit(Integer id,String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,Integer qorderBy,
			Integer pageNo,  HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsLive live=manager.findById(id);
		List<BbsLiveChapter>topList=liveChapterMng.getTopList(live.getUser().getId());
		List<BbsLiveChapter>chapterList=BbsLiveChapter.getListForSelect(topList, null);
		model.addAttribute("chapterList", chapterList);
		model.addAttribute("live", live);
		appendQuery(model, qtitle, qusername, qstatus,
				qtimeBegin, qtimeEnd, qorderBy);
		return "plugPage/live/live/edit";
	}
	
	@RequiresPermissions("live:v_view")
	@RequestMapping("/live/v_view.do")
	public String view(Integer id,HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsLive live=manager.findById(id);
		model.addAttribute("live", live);
		return "plugPage/live/live/view";
	}

	@RequiresPermissions("live:o_update")
	@RequestMapping("/live/o_update.do")
	public String update(BbsLive bean,String qtitle,
			String qusername,Short qstatus,Date qtimeBegin,Date qtimeEnd,
			Date qFinishTimeBegin,Date qFinishTimeEnd,Integer qorderBy,
			Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		//默认修改审核
		bean.setCheckStatus(BbsLive.CHECKED);
		bean = manager.update(bean);
		log.info("update BbsLive id={}.", bean.getId());
		return list(qtitle, qusername, qstatus, qtimeBegin,
				qtimeEnd,qFinishTimeBegin,qFinishTimeEnd,
				qorderBy, pageNo, request, model);
	}
	
	
	
	@RequiresPermissions("live:o_check")
	@RequestMapping("/live/o_check.do")
	public String check(Integer[] ids,String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,
			Date qFinishTimeBegin,Date qFinishTimeEnd,Integer qorderBy,
			Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		for(Integer id:ids){
			BbsLive live=manager.findById(id);
			live.setCheckStatus(BbsLive.CHECKED);
			manager.update(live);
			log.info("check BbsLive id={}.", live.getId());
		}
		return list(qtitle, qusername, qstatus, qtimeBegin,
				qtimeEnd,qFinishTimeBegin,qFinishTimeEnd,
				qorderBy, pageNo, request, model);
	}
	
	@RequiresPermissions("live:o_reject")
	@RequestMapping("/live/o_reject.do")
	public String reject(Integer[] ids,String reason,
			String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,
			Date qFinishTimeBegin,Date qFinishTimeEnd,Integer qorderBy,
			Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		for(Integer id:ids){
			BbsLive live=manager.findById(id);
			live.setReason(reason);
			live.setCheckStatus(BbsLive.REJECT);
			manager.update(live);
			log.info("check BbsLive id={}.", live.getId());
		}
		return list(qtitle, qusername, qstatus, qtimeBegin,
				qtimeEnd,qFinishTimeBegin,qFinishTimeEnd,
				qorderBy, pageNo, request, model);
	}
	
	@RequiresPermissions("live:o_stop")
	@RequestMapping("/live/o_stop.do")
	public String stop(Integer[] ids,String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,
			Date qFinishTimeBegin,Date qFinishTimeEnd,Integer qorderBy,
			Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		CmsSite site=CmsUtils.getSite(request);
		CmsConfig config=site.getConfig();
		Date queryBeginBegin=Calendar.getInstance().getTime();
		for(Integer id:ids){
			BbsLive live=manager.findById(id);
			BbsUser host=live.getUser();
			//关闭主播的未开始直播
			List<BbsLive>unBeginLives=manager.getList(
					null, null, host.getId(),BbsLive.CHECKED,
					queryBeginBegin, null, null, null, 1, 0, 100);
			live.setCheckStatus(BbsLive.STOP);
			manager.update(live);
			for(BbsLive l:unBeginLives){
				l.setCheckStatus(BbsLive.STOP);
				manager.update(l);
			}
			if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_TENCENT)){
				String streamId=config.getTencentBizId()+"_"+live.getUser().getId();
				TencentCloudUtil.disableStram(config.getTencentApiAuthKey()
						,config.getTencentAppId(), streamId);
			}else if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_BAIDU)){
				try {
					BaiduCloudUtil.disableStram(config.getBaiduAccessKeyId(),
							config.getBaiduSecretAccessKey(),
							config.getBaiduPlayDomain(), 
							live.getUser().getId().toString());
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
			webSocketExtHandler.closeLiveWebSocketSession(id);
			log.info("stop BbsLive id={}.", live.getId());
		}
		return list(qtitle, qusername, qstatus, qtimeBegin,
				qtimeEnd, qFinishTimeBegin,qFinishTimeEnd,
				qorderBy, pageNo, request, model);
	}
	
	@RequiresPermissions("live:o_stop")
	@RequestMapping("/live/o_ajax_stop.do")
	public void ajaxStop(Integer  id,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			ResponseUtils.renderJson(response, "false");
		}
		CmsSite site=CmsUtils.getSite(request);
		CmsConfig config=site.getConfig();
		Date queryStartBegin=Calendar.getInstance().getTime();
		BbsLive live=manager.findById(id);
		live.setCheckStatus(BbsLive.STOP);
		manager.update(live);
		BbsUser host=live.getUser();
		//关闭主播的未开始直播
		List<BbsLive>unBeginLives=manager.getList(
				null, null, host.getId(),BbsLive.CHECKED,
				queryStartBegin, null, null, null, 1, 0, 100);
		for(BbsLive l:unBeginLives){
			l.setCheckStatus(BbsLive.STOP);
			manager.update(l);
		}
		if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_TENCENT)){
			String streamId=config.getTencentBizId()+"_"+live.getUser().getId();
			TencentCloudUtil.disableStram(config.getTencentApiAuthKey()
					,config.getTencentAppId(), streamId);
		}else if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_BAIDU)){
			try {
				BaiduCloudUtil.disableStram(config.getBaiduAccessKeyId(),
						config.getBaiduSecretAccessKey(),
						config.getBaiduPlayDomain(), 
						live.getUser().getId().toString());
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		webSocketExtHandler.closeLiveWebSocketSession(id);
		ResponseUtils.renderJson(response, "true");
	}
	
	@RequiresPermissions("live:o_start")
	@RequestMapping("/live/o_start.do")
	public String start(Integer[] ids,String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,
			Date qFinishTimeBegin,Date qFinishTimeEnd,Integer qorderBy,
			Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		CmsSite site=CmsUtils.getSite(request);
		CmsConfig config=site.getConfig();
		Date queryStartBegin=Calendar.getInstance().getTime();
		for(Integer id:ids){
			BbsLive live=manager.findById(id);
			live.setCheckStatus(BbsLive.CHECKED);
			manager.update(live);
			BbsUser host=live.getUser();
			//重新启用未开始直播
			List<BbsLive>unBeginLives=manager.getList(
					null, null, host.getId(),BbsLive.STOP,
					queryStartBegin, null, null, null, 1, 0, 100);
			for(BbsLive l:unBeginLives){
				l.setCheckStatus(BbsLive.CHECKED);
				manager.update(l);
			}
			if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_TENCENT)){
				String streamId=config.getTencentBizId()+"_"+live.getUser().getId();
				TencentCloudUtil.connectStram(config.getTencentApiAuthKey()
						,config.getTencentAppId(), streamId);
			}else if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_BAIDU)){
				try {
					BaiduCloudUtil.connectStram(config.getBaiduAccessKeyId(),
							config.getBaiduSecretAccessKey(),
							config.getBaiduPlayDomain(), 
							live.getUser().getId().toString());
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
			log.info("check BbsLive id={}.", live.getId());
		}
		return list(qtitle, qusername, qstatus, qtimeBegin,
				qtimeEnd, qFinishTimeBegin,qFinishTimeEnd,
				qorderBy, pageNo, request, model);
	}
	
	@RequiresPermissions("live:o_start")
	@RequestMapping("/live/o_ajax_start.do")
	public void ajaxStart(Integer  id,HttpServletRequest request,
			HttpServletResponse response,ModelMap model) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			ResponseUtils.renderJson(response, "false");
		}
		CmsSite site=CmsUtils.getSite(request);
		CmsConfig config=site.getConfig();
		BbsLive live=manager.findById(id);
		live.setCheckStatus(BbsLive.CHECKED);
		manager.update(live);
		BbsUser host=live.getUser();
		Date queryStartBegin=Calendar.getInstance().getTime();
		List<BbsLive>unBeginLives=manager.getList(
				null, null, host.getId(),BbsLive.STOP,
				queryStartBegin, null, null, null, 1, 0, 100);
		for(BbsLive l:unBeginLives){
			l.setCheckStatus(BbsLive.CHECKED);
			manager.update(l);
		}
		if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_TENCENT)){
			String streamId=config.getTencentBizId()+"_"+live.getUser().getId();
			TencentCloudUtil.connectStram(config.getTencentApiAuthKey()
					,config.getTencentAppId(), streamId);
		}else if(live.getLivePlat().equals(BbsLive.LIVE_PLAT_BAIDU)){
			try {
				BaiduCloudUtil.connectStram(config.getBaiduAccessKeyId(),
						config.getBaiduSecretAccessKey(),
						config.getBaiduPlayDomain(), 
						live.getUser().getId().toString());
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		ResponseUtils.renderJson(response, "true");
	}

	@RequiresPermissions("live:o_delete")
	@RequestMapping("/live/o_delete.do")
	public String delete(Integer[] ids, String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,
			Date qFinishTimeBegin,Date qFinishTimeEnd,Integer qorderBy,
			Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		BbsLive[] beans = manager.deleteByIds(ids);
		for (BbsLive bean : beans) {
			log.info("delete BbsLive id={}", bean.getId());
		}
		return list(qtitle, qusername, qstatus, qtimeBegin, 
				qtimeEnd, qFinishTimeBegin,qFinishTimeEnd,qorderBy, pageNo, request, model);
	}
	

	public static void websocketBroadcast(BbsUser user,String txt) throws IOException{
		Message msg = new Message();
		msg.setDate(new Date());
		msg.setFrom(user.getId());
		msg.setFromName(user.getUsername());
		msg.setTo(0);
		msg.setText(txt);
		WebSocketExtHandler socketHandler=(WebSocketExtHandler) ApplicationContextUtil.getBean("webSocketHandler");
		socketHandler.broadcast(new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
	}
	
	private WebErrors validateEdit(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id,errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateUpdate(Integer id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldExist(id, errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateDelete(Integer[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids");
		if(!errors.hasErrors()){
			for (Integer id : ids) {
				vldExist(id, errors);
			}
		}
		return errors;
	}

	private boolean vldExist(Integer id,WebErrors errors) {
		if (errors.ifNull(id, "id")) {
			return true;
		}
		BbsLive entity = manager.findById(id);
		if(errors.ifNotExist(entity, BbsLive.class, id)) {
			return true;
		}
		return false;
	}
	
	private void appendQuery(ModelMap model,String qtitle,String qusername,
			Short qstatus,Date qtimeBegin,Date qtimeEnd,Integer qorderBy){
		model.addAttribute("qtitle", qtitle);
		model.addAttribute("qusername", qusername);
		model.addAttribute("qstatus", qstatus);
		model.addAttribute("qtimeBegin",qtimeBegin);
		model.addAttribute("qtimeEnd", qtimeEnd);
		model.addAttribute("qorderBy", qorderBy);
	}
	
	
	@Autowired
	private BbsLiveMng manager;
	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsLiveChapterMng liveChapterMng;
	@Autowired
	private WebSocketExtHandler webSocketExtHandler;
}