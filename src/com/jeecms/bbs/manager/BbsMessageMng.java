package com.jeecms.bbs.manager;


import java.util.List;

import com.jeecms.bbs.entity.BbsMessage;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.common.page.Pagination;


public interface BbsMessageMng {
	public BbsMessage findById(Integer id);

	public BbsMessage save(BbsMessage bean);

	public BbsMessage update(BbsMessage bean);

	public BbsMessage deleteById(Integer id);

	public BbsMessage[] deleteByIds(Integer[] ids);
	
	public BbsMessage sendMsg(BbsUser sender, BbsUser receiver, BbsMessage sMsg);
	
	public BbsMessage sendSysMsg(BbsUser sender,BbsUser receiver,
			Integer groupId, Boolean toAll,BbsMessage sMsg);
	
	public Pagination getPageByUserId(Integer userId,Integer typeId, Integer pageNo, Integer pageSize);
	
	public List<BbsMessage> getList(Boolean sys,Integer userId,
			Integer senderId,Integer receiverId,
			Integer typeId, Boolean status,Integer first,Integer count);
	
	public List<BbsMessage> getListByUsername(String username);
	
	public Pagination getPagination(Boolean sys,Integer userId,
			Integer senderId,Integer receiverId,
			Integer typeId, Boolean status,Integer groupId,Integer pageNo,Integer pageSize);
	
	public boolean hasUnReadMessage(Integer userId,Integer typeId);
	
	public BbsMessage getMsg(String username,String sendername,String receivername,Integer typeId);
}