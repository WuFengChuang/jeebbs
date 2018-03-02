package com.jeecms.plug.live.manager.impl;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.LiveBeansView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.bbs.entity.BbsAccountPay;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserAccount;
import com.jeecms.bbs.manager.BbsAccountDrawMng;
import com.jeecms.bbs.manager.BbsAccountPayMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.util.WeixinPay;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.springmvc.RealPathResolver;
import com.jeecms.plug.live.dao.BbsLiveUserAccountDao;
import com.jeecms.plug.live.entity.BbsLiveUserAccount;
import com.jeecms.plug.live.manager.BbsLiveUserAccountMng;

@Service
@Transactional
public class BbsLiveUserAccountMngImpl implements BbsLiveUserAccountMng {
	
	public static final String WEIXINPAY_CERT = "WEB-INF/cert/weixinpay_cert.p12";
	
	
	public BbsLiveUserAccount userPay(Double payAmout,Integer ticketNum, BbsUser hostUser){
		BbsLiveUserAccount entity = dao.findById(hostUser.getId());
		Calendar curr = Calendar.getInstance();
		Calendar last = Calendar.getInstance();
		if (entity == null) {
			entity=updateAccountInfo(hostUser,ticketNum);
		} 
		if(entity.getLastBuyTime()!=null){
			last.setTime(entity.getLastBuyTime());
			int currDay = curr.get(Calendar.DAY_OF_YEAR);
			int lastDay = last.get(Calendar.DAY_OF_YEAR);
			int currYear=curr.get(Calendar.YEAR);
			int lastYear=last.get(Calendar.YEAR);
			int currMonth = curr.get(Calendar.MONTH);
			int lastMonth = last.get(Calendar.MONTH);
			if(lastYear!=currYear){
				entity.setYearAmount(0d);
				entity.setMonthAmount(0d);
				entity.setDayAmount(0d);
			}else{
				if(currMonth!=lastMonth){
					entity.setMonthAmount(0d);
					entity.setDayAmount(0d);
				}else{
					if (currDay != lastDay) {
						entity.setDayAmount(0d);
					}
				}
			}
		}
		entity.setDayAmount(entity.getDayAmount()+payAmout);
		entity.setMonthAmount(entity.getMonthAmount()+payAmout);
		entity.setYearAmount(entity.getYearAmount()+payAmout);
		entity.setTotalAmount(entity.getTotalAmount()+payAmout);
		entity.setLastBuyTime(curr.getTime());
		if(entity.getBuyCount()!=null){
			entity.setBuyCount(entity.getBuyCount()+1);
		}else{
			entity.setBuyCount(1);
		}
		entity.setNoPayAmount(entity.getNoPayAmount()+payAmout);
		if(ticketNum!=null){
			if(entity.getTicketNum()!=null){
				entity.setTicketNum(entity.getTicketNum()+ticketNum);
			}else{
				entity.setTicketNum(ticketNum);
			}
		}
		return entity;
	}
	
	public BbsLiveUserAccount payToHost(Double drawAmout, BbsUser user,Date payTime){
		BbsLiveUserAccount entity = dao.findById(user.getId());
		if (entity != null&&drawAmout!=null) {
			if(entity.getNoPayAmount()>=drawAmout){
				entity.setDrawCount(entity.getDrawCount()+1);
				entity.setLastDrawTime(payTime);
				entity.setNoPayAmount(entity.getNoPayAmount()-drawAmout);
			}
		} 
		return entity;
	}
	
	public BbsLiveUserAccount updateAccountInfo(BbsUser user,Integer ticketNum) {
		BbsLiveUserAccount entity = dao.findById(user.getId());
		if (entity == null) {
			BbsLiveUserAccount account=new BbsLiveUserAccount();
			account.init();
			account.setTicketNum(ticketNum);
			entity = save(account, user);
			return entity;
		} else {
			return entity;
		}
	}
	
	public BbsLiveUserAccount afterReceiveGift(BbsUser user,Integer giftNum){
		BbsLiveUserAccount entity = dao.findById(user.getId());
		if (entity == null) {
			BbsLiveUserAccount account=new BbsLiveUserAccount();
			account.init();
			entity = save(account, user);
			return entity;
		} else {
			if(entity.getGiftNum()==null){
				entity.setGiftNum(giftNum);
			}else{
				entity.setGiftNum(entity.getGiftNum()+giftNum);
			}
			entity=update(entity);
			return entity;
		}
	}
	
	//微信企业转账
	public String weixinTransferPay(String serverUrl,Integer drawId,
			BbsUser drawUser,BbsUser payUser,Double payAmount,String orderNum,
			HttpServletRequest request,HttpServletResponse response,
			ModelMap model){
		if(getPkcFile()==null){
			setPkcFile(new File(realPathResolver.get(WEIXINPAY_CERT)));
		}
		BbsConfigCharge config=configChargeMng.getDefault();
		BbsUserAccount drawUserAccount=drawUser.getUserAccount();
		if(drawUserAccount==null){
			return MessageResolver.getMessage(request,"transferPay.fail.userAccount.notfound"); 
		}else{
			if(StringUtils.isBlank(drawUser.getUserAccount().getAccountWeixinOpenId())){
				return MessageResolver.getMessage(request,"transferPay.fail.userAccount.notAuth"); 
			}
		}
		if (StringUtils.isNotBlank(config.getWeixinAppId())
				&& StringUtils.isNotBlank(config.getWeixinAccount())) {
			Object result[]=WeixinPay.payToUser(config, getPkcFile(), serverUrl, orderNum,
					drawUser.getUserAccount().getAccountWeixinOpenId(),
					drawUser.getRealname(),  payAmount, 
					MessageResolver.getMessage(request, "cmsAccountDraw.payAccount"),
					RequestUtils.getIpAddr(request));
			String resXml=(String) result[1];
			boolean postError=(Boolean) result[0];
			if(!postError){
				Map<String, String> map=new HashMap<String, String>();
				try {
					map = PayUtil.parseXMLToMap(resXml);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				String returnCode = map.get("return_code");
				if (returnCode.equalsIgnoreCase("FAIL")) {
					//支付失败
					return map.get("return_msg");
				} else if (returnCode.equalsIgnoreCase("SUCCESS")) {
					if (map.get("err_code") != null) {
						//支付失败
						return map.get("err_code_des");
					} else if (map.get("result_code").equalsIgnoreCase(
							"SUCCESS")) {
						//支付成功
						String paymentNo = map.get("payment_no");
						String payment_time = map.get("payment_time");
						try {
							afterPay(drawId, drawUser, payUser, payAmount, orderNum, paymentNo, null, 
									DateUtils.common_format.parse(payment_time),config);
						} catch (ParseException e) {
							//e.printStackTrace();
						}
						return MessageResolver.getMessage(request,"transferPay.success");
					}
				}
			}
			//通信失败
			return MessageResolver.getMessage(request,"error.connect.timeout");
		} else {
			//参数缺失
			return MessageResolver.getMessage(request,"error.contentCharge.need.appid");
		}
	}
	
	private void afterPay(Integer drawId,BbsUser drawUser,BbsUser payUser,
			Double payAmount,String orderNum,
			String weixinNo,String alipyNo,Date payTime,BbsConfigCharge config){
		BbsAccountPay pay=new BbsAccountPay();
		//保存支付记录
		if(drawUser.getDrawAccount()==BbsUserAccount.DRAW_WEIXIN){
			pay.setDrawAccount(drawUser.getAccountWeixin());
			pay.setPayAccount(config.getWeixinAccount());
		}else{
			pay.setDrawAccount(drawUser.getAccountAlipy());
			pay.setPayAccount(config.getAlipayAccount());
		}
		pay.setDrawNum(orderNum);
		pay.setDrawUser(drawUser);
		pay.setPayTime(payTime);
		pay.setPayUser(payUser);
		pay.setWeixinNum(weixinNo);
		pay.setAlipayNum(alipyNo);
		pay=accountPayMng.save(pay);
		//处理申请状态
		BbsAccountDraw draw=accountDrawMng.findById(drawId);
		if(draw!=null){
			draw.setAccountPay(pay);
			draw.setApplyStatus(BbsAccountDraw.DRAW_SUCC);
			accountDrawMng.update(draw);
		}
		//处理提现者账户
		liveUserAccountMng.payToHost(payAmount, drawUser, payTime);
	}
	
	@Transactional(readOnly = true)
	public List<BbsLiveUserAccount> getList(Integer userId,
			Integer orderBy,int count){
		return dao.getList(userId,orderBy, count);
	}
	
	@Transactional(readOnly = true)
	public Pagination getPage(Integer userId,
			Integer orderBy,int pageNo, int pageSize){
		return dao.getPage(userId,orderBy, pageNo, pageSize);
	}

	@Transactional(readOnly = true)
	public BbsLiveUserAccount findById(Integer id) {
		BbsLiveUserAccount entity = dao.findById(id);
		return entity;
	}

	public BbsLiveUserAccount save(BbsLiveUserAccount bean,BbsUser user) {
		bean.setUser(user);
		dao.save(bean);
		return bean;
	}

	public BbsLiveUserAccount update(BbsLiveUserAccount bean) {
		Updater<BbsLiveUserAccount> updater = new Updater<BbsLiveUserAccount>(bean);
		BbsLiveUserAccount entity = dao.updateByUpdater(updater);
		return entity;
	}

	public BbsLiveUserAccount deleteById(Integer id) {
		BbsLiveUserAccount bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsLiveUserAccount[] deleteByIds(Integer[] ids) {
		BbsLiveUserAccount[] beans = new BbsLiveUserAccount[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}
	
	public BbsLiveUserAccount[] updatePriority(Integer[] ids, Integer[] priority){
		int len = ids.length;
		BbsLiveUserAccount[] beans = new BbsLiveUserAccount[len];
		for (int i = 0; i < len; i++) {
			beans[i] = findById(ids[i]);
			beans[i].setTopPriority(priority[i]);
		}
		return beans;
	}

	private BbsLiveUserAccountDao dao;
	
	private File pkcFile;
	public File getPkcFile() {
		return pkcFile;
	}

	public void setPkcFile(File pkcFile) {
		this.pkcFile = pkcFile;
	}
	
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private BbsAccountDrawMng accountDrawMng;
	@Autowired
	private BbsAccountPayMng accountPayMng;
	@Autowired
	private BbsLiveUserAccountMng liveUserAccountMng;

	@Autowired
	public void setDao(BbsLiveUserAccountDao dao) {
		this.dao = dao;
	}
}