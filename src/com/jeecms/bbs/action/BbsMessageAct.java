package com.jeecms.bbs.action;

import static com.jeecms.common.page.SimplePage.cpn;

import java.util.LinkedList;
import java.util.List;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecms.common.page.Pagination;
import com.jeecms.common.web.CookieUtils;
import com.jeecms.core.entity.CmsSite;
import com.jeecms.bbs.entity.BbsMessage;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserGroup;
import com.jeecms.bbs.manager.BbsMessageMng;
import com.jeecms.bbs.manager.BbsMessageReplyMng;
import com.jeecms.bbs.manager.BbsUserGroupMng;
import com.jeecms.bbs.manager.BbsUserMng;
import com.jeecms.bbs.web.CmsUtils;

@Controller
public class BbsMessageAct {
	private static final Logger log = LoggerFactory.getLogger(BbsMessageAct.class);
	
	@RequiresPermissions("message:v_sys_list")
	@RequestMapping("/message/v_sys_list.do")
	public String sysMessagelist(Integer pageNo, HttpServletRequest request, ModelMap model) {
//		Pagination pagination=bbsMessageMng.getPagination(true,null,null,cpn(pageNo),
//				CookieUtils.getPageSize(request));
		Pagination pagination=bbsMessageMng.getPagination(true, null, null, null, null, null, null, cpn(pageNo),
				CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		return "message/syslist";
	}
	
	@RequiresPermissions("message:v_list")
	@RequestMapping("/message/v_list.do")
	public String messagelist(String sender,String receiver,String content, 
			Integer pageNo, HttpServletRequest request, ModelMap model) {
		Integer sendUserId=0,receiverId=0;
		BbsUser sendUser=bbsUserMng.findByUsername(sender);
		BbsUser receiverUser=bbsUserMng.findByUsername(receiver);
		if(sendUser!=null){
			sendUserId=sendUser.getId();
		}
		if(receiverUser!=null){
			receiverId=receiverUser.getId();
		}
		Pagination pagination=bbsMessageReplyMng.getPage(
				sendUserId,receiverId,content,
				cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("sender", sender);
		model.addAttribute("receiver", receiver);
		model.addAttribute("content", content);
		model.addAttribute("pageNo", pageNo);
		return "message/list";
	}
	
	@RequiresPermissions("message:v_sendSys")
	@RequestMapping("/message/v_sendSys.do")
	public String sendMessage(Integer pageNo, HttpServletRequest request, ModelMap model) {
		CmsSite site=CmsUtils.getSite(request);
		List<BbsUserGroup> groupList = bbsUserGroupMng.getList(site.getId());
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("groupList",groupList);
		return "message/send";
	}
	
	@RequiresPermissions("message:o_sendSys")
	@RequestMapping("/message/o_sendSys.do")
	public String submitSysMessage(String content,String username,Integer groupId,
			Boolean toAll,Integer pageNo, 
			HttpServletRequest request, ModelMap model) {
		BbsUser user=CmsUtils.getUser(request);
		BbsUser receiverUser=null;
		BbsMessage sMsg=new BbsMessage();
		sMsg.setContent(content);
		if(StringUtils.isNotBlank(username)){
			receiverUser=bbsUserMng.findByUsername(username);
		}
		bbsMessageMng.sendSysMsg(user,receiverUser,groupId,toAll,sMsg);
		return sysMessagelist(pageNo, request, model);
	}
	
	@RequiresPermissions("message:o_delete")
	@RequestMapping("/message/o_delete.do")
	public String delete(Boolean sys,String sender,String receiver,String content, 
			Integer[] ids, Integer pageNo, HttpServletRequest request,ModelMap model) {
		ids=array_unique(ids);
		BbsMessage[] beans = bbsMessageMng.deleteByIds(ids);
		for (BbsMessage bean : beans) {
			log.info("delete BbsMessage id={}", bean.getId());
		}
		if(sys!=null){
			sys=false;
		}
		if(sys){
			return sysMessagelist(pageNo, request, model);
		}else{
			return messagelist(sender,receiver,content,pageNo, request, model);
		}
	}
	
	//去除数组中重复的记录
	public static Integer[] array_unique(Integer[] a) {
	    // array_unique
	    List<Integer> list = new LinkedList<Integer>();
	    for(int i = 0; i < a.length; i++) {
	        if(!list.contains(a[i])) {
	            list.add(a[i]);
	        }
	    }
	    return (Integer[])list.toArray(new Integer[list.size()]);
	}

	@Autowired
	private BbsUserMng bbsUserMng;
	@Autowired
	private BbsMessageMng bbsMessageMng;
	@Autowired
	private BbsMessageReplyMng bbsMessageReplyMng;
	@Autowired
	private BbsUserGroupMng bbsUserGroupMng;
}