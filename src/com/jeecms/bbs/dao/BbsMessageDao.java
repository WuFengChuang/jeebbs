package com.jeecms.bbs.dao;


import java.util.List;

import com.jeecms.bbs.entity.BbsMessage;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

public interface BbsMessageDao {
	public BbsMessage findById(Integer id);

	public BbsMessage save(BbsMessage bean);

	public BbsMessage updateByUpdater(Updater<BbsMessage> updater);

	public BbsMessage deleteById(Integer id);

	public BbsMessage getSendRelation(Integer userId, Integer senderId,
			Integer receiverId,Integer typeId);

	public Pagination getPageByUserId(Integer userId,Integer typeId, Integer pageNo,
			Integer pageSize); 
	
	public List<BbsMessage> getList(Boolean sys,Integer userId,
			Integer senderId,Integer receiverId,
			Integer typeId,Boolean status,Integer first,Integer count);
	
	public List<BbsMessage> getListByUsername(String username);
	
	public Pagination getPagination(Boolean sys,Integer userId,
			Integer senderId,Integer receiverId,
			Integer typeId, Boolean status,Integer groupId,Integer pageNo,Integer pageSize);
	
}