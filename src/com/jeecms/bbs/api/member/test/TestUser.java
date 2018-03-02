package com.jeecms.bbs.api.member.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestUser {
	private static String base="http://192.168.0.150:8080/jeebbs5/api/member";
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="375F2649FEA8BD78E9B81615ED93CEDD";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";
	
	public static void main(String[] args) {
		testForgotPWD();
	}
	
	private static String testForgotPWD(){
		String url=base+"/user/forbidden";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("forumId="+1);
		paramBuff.append("&userId="+1);
		paramBuff.append("&disabled="+false);
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
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		System.out.println(param);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
}
