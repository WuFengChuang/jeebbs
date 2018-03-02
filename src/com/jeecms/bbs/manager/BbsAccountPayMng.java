package com.jeecms.bbs.manager;

import com.jeecms.bbs.entity.BbsAccountPay;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.common.page.Pagination;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;


public interface BbsAccountPayMng {
	
	public String weixinTransferPay(String serverUrl,Integer drawId,
			BbsUser drawUser,BbsUser payUser,Double payAmount,String orderNum,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model);
	
	public Pagination getPage(String drawNum,Integer payUserId,Integer drawUserId,
			Date payTimeBegin,Date payTimeEnd,int pageNo, int pageSize);
	
	public List<BbsAccountPay> getList(String drawNum,Integer payUserId,Integer drawUserId,
			Date payTimeBegin,Date payTimeEnd,Integer first, Integer count);
	
	public BbsAccountPay findById(Long id);

	public BbsAccountPay save(BbsAccountPay bean);

	public BbsAccountPay update(BbsAccountPay bean);

	public BbsAccountPay deleteById(Long id);
	
	public BbsAccountPay[] deleteByIds(Long[] ids);
}