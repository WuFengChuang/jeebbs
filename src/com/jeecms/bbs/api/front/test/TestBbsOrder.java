package com.jeecms.bbs.api.front.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.web.HttpClientUtil;

public class TestBbsOrder {

	public static void main(String[] args) {
		//testMyOrders();
		testMyTopicCharge();
	}
	
	private static String testMyOrders(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/order/myorders";
		StringBuffer paramBuff=new StringBuffer();
		//我的消费记录
		//paramBuff.append("orderType="+1);
		//他人购买我的内容
		paramBuff.append("orderType="+2);
		paramBuff.append("&first="+"0");
		paramBuff.append("&count="+5);
		paramBuff.append("&appId="+appId);
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testMyTopicCharge(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/order/chargelist";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("orderType="+3);
		paramBuff.append("&first="+"0");
		paramBuff.append("&count="+5);
		paramBuff.append("&appId="+appId);
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		param.put("sessionKey", encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="2B4953BE8E8BBFA63A93D0A1EF7DB7DF";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";
}
