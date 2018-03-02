package com.jeecms.bbs.dao;

import java.util.List;

import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserActiveLevel;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;

/**
 * 用户DAO接口
 * 
 * @author tom
 * 
 */
public interface BbsUserDao {
	public List<BbsUser> getApiList(String username, String email, Integer groupId,
			Boolean disabled,Boolean admin,Boolean official,Integer lastLoginDay,
			Integer rank,Integer orderBy, Integer first, Integer count);
	
	public List<BbsUser> getList(String username, Integer groupId,
			Boolean disabled, Boolean admin,Boolean official,
			Integer first, Integer count);
	
	public Pagination getPage(String username, String email, Integer groupId,
			Boolean disabled, Boolean admin,Boolean official,Integer lastLoginDay,
			Integer rank,Integer orderBy, int pageNo, int pageSize);
	
	public Pagination getPageByAttent(Integer attent, Integer userId, int pageNo, int pageSize);
	
	public List<BbsUser> getListByAttent(Integer attent, Integer userId,
			Integer first, Integer count);
	
	public List<BbsUser> getList(Integer count);
	
	public List<BbsUser> getAdminList(Integer siteId, Boolean allChannel,
			Boolean disabled, Integer rank);

	public BbsUser findById(Integer id);

	public BbsUser findByUsername(String username);

	public int countByUsername(String username);

	public int countByEmail(String email);

	public BbsUser save(BbsUser bean);

	public BbsUser updateByUpdater(Updater<BbsUser> updater);
	
	public BbsUser updateActiveLevel(BbsUser bean,BbsUserActiveLevel level);

	public BbsUser deleteById(Integer id);
	
	public List<BbsUser> getSuggestMember(String username, Integer count);

	
}