package com.jeecms.bbs.manager.impl;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.jeecms.bbs.dao.BbsAccountPayDao;
import com.jeecms.bbs.entity.BbsAccountDraw;
import com.jeecms.bbs.entity.BbsAccountPay;
import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsUser;
import com.jeecms.bbs.entity.BbsUserAccount;
import com.jeecms.bbs.manager.BbsAccountDrawMng;
import com.jeecms.bbs.manager.BbsAccountPayMng;
import com.jeecms.bbs.manager.BbsConfigChargeMng;
import com.jeecms.bbs.manager.BbsUserAccountMng;
import com.jeecms.common.hibernate4.Updater;
import com.jeecms.common.page.Pagination;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.util.WeixinPay;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.common.web.springmvc.MessageResolver;
import com.jeecms.common.web.springmvc.RealPathResolver;

@Service
@Transactional
public class BbsAccountPayMngImpl implements BbsAccountPayMng {
	public static final String WEIXINPAY_CERT = "WEB-INF/cert/weixinpay_cert.p12";
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
		userAccountMng.payToAuthor(payAmount, drawUser, payTime);
	}
		
	@Transactional(readOnly = true)
	public Pagination getPage(String drawNum,Integer payUserId,Integer drawUserId,
			Date payTimeBegin,Date payTimeEnd,int pageNo, int pageSize) {
		Pagination page = dao.getPage(drawNum,payUserId,drawUserId,payTimeBegin
				,payTimeEnd,pageNo, pageSize);
		return page;
	}
	
	@Override
	public List<BbsAccountPay> getList(String drawNum, Integer payUserId, Integer drawUserId, Date payTimeBegin,
			Date payTimeEnd, Integer first, Integer count) {
		return dao.getList(drawNum,payUserId,drawUserId,payTimeBegin
				,payTimeEnd,first, count);
	}

	@Transactional(readOnly = true)
	public BbsAccountPay findById(Long id) {
		BbsAccountPay entity = dao.findById(id);
		return entity;
	}

	public BbsAccountPay save(BbsAccountPay bean) {
		dao.save(bean);
		return bean;
	}

	public BbsAccountPay update(BbsAccountPay bean) {
		Updater<BbsAccountPay> updater = new Updater<BbsAccountPay>(bean);
		bean = dao.updateByUpdater(updater);
		return bean;
	}

	public BbsAccountPay deleteById(Long id) {
		BbsAccountPay bean = dao.deleteById(id);
		return bean;
	}
	
	public BbsAccountPay[] deleteByIds(Long[] ids) {
		BbsAccountPay[] beans = new BbsAccountPay[ids.length];
		for (int i = 0,len = ids.length; i < len; i++) {
			beans[i] = deleteById(ids[i]);
		}
		return beans;
	}

	private File pkcFile;
	public File getPkcFile() {
		return pkcFile;
	}

	public void setPkcFile(File pkcFile) {
		this.pkcFile = pkcFile;
	}
	private BbsAccountPayDao dao;
	@Autowired
	private BbsAccountDrawMng accountDrawMng;
	@Autowired
	private BbsAccountPayMng accountPayMng;
	@Autowired
	private BbsConfigChargeMng configChargeMng;
	@Autowired
	private RealPathResolver realPathResolver;
	@Autowired
	private BbsUserAccountMng userAccountMng;

	@Autowired
	public void setDao(BbsAccountPayDao dao) {
		this.dao = dao;
	}

}