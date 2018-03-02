package com.jeecms.bbs.api.front.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.web.HttpClientUtil;

public class TestAttent {

	public static void main(String[] args) {
		//testAttentUserList();
		//testAttentTopicList();
		testAttentUser();
	}
	
	private static String testAttentUserList(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/attent/list";
		StringBuffer paramBuff=new StringBuffer();
		//1关注我的   0我关注的
		paramBuff.append("attent="+1);
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
	
	private static String testAttentTopicList(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/attent/topicList";
		StringBuffer paramBuff=new StringBuffer();
		//1收藏   2关注
		paramBuff.append("type="+2);
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
	
	
	private static String testAttentUser(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/attent/user";
		StringBuffer paramBuff=new StringBuffer();
		// 操作参数 0添加关注 1移除关注
		paramBuff.append("operate="+1);
		paramBuff.append("&userId="+6);
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
