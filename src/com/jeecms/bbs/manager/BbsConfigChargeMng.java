package com.jeecms.bbs.manager;

import java.util.Map;

import com.jeecms.bbs.entity.BbsConfigCharge;
import com.jeecms.bbs.entity.BbsIncomeStatistic.BbsIncomeType;


public interface BbsConfigChargeMng {
	
	public BbsConfigCharge findById(Integer id) ;
	
	public BbsConfigCharge getDefault();

	public BbsConfigCharge update(BbsConfigCharge bean
			,String payTransferPassword,Map<String,String> keys,
			Map<String,String>fixVal);
	
	public BbsConfigCharge afterUserPay(Double payAmout,BbsIncomeType incomeType);

}