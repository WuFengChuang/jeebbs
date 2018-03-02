package com.jeecms.bbs.api.front.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestMagic {

	public static void main(String[] args) {
		//testMagicBuy();
		testMagicBuyWithMoney();
		//testMagicSell();
		//testMagicList();
		//testMyMagic();
		//testMagicUse();
	}
	
	private static String testMagicBuy(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/magic/buy";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("mid="+"repent");
		paramBuff.append("&num="+2);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testMagicBuyWithMoney(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/magic/buyUseMoney";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("mid="+"open");
		paramBuff.append("&outOrderNum="+"4007062001201702160192676013");
		// 1微信支付   2支付宝支付 必选
		paramBuff.append("&orderType="+1);
		paramBuff.append("&num="+1);
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testMagicSell(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/magic/sell";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("mid="+"repent");
		paramBuff.append("&num="+1);
		//operator 0出售  2丢弃
		paramBuff.append("&operator="+0);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testMagicUse(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/magic/use";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("num="+1);
		/*
		 * 匿名卡*/
		//paramBuff.append("&mid="+"anonymouspost");
		//paramBuff.append("&ppid="+235);
		/**
		 * 提升卡
		 */
		//paramBuff.append("&mid="+"bump");
		//paramBuff.append("&pid="+90);
		//沉默卡
		//paramBuff.append("&mid="+"close");
		//paramBuff.append("&pid="+90);
		//喧嚣卡
		//paramBuff.append("&mid="+"open");
		//paramBuff.append("&pid="+90);
		//变色卡
		//paramBuff.append("&mid="+"highlight");
		//paramBuff.append("&pid="+90);
		//paramBuff.append("&color="+"FF0000");
		//窥视卡
		//paramBuff.append("&mid="+"showip");
		//paramBuff.append("&username="+"test13");
		//雷达卡 body的status magic.checkonline.offline 离线  magic.checkonline.online 在线
		//paramBuff.append("&mid="+"checkonline");
		//paramBuff.append("&username="+"test13");
		//照妖镜
		//paramBuff.append("&mid="+"namepost");
		//paramBuff.append("&ppid="+235);
		//悔悟卡
		//paramBuff.append("&mid="+"repent");
		//paramBuff.append("&ppid="+135);
		//千斤顶
		//paramBuff.append("&mid="+"jack");
		//paramBuff.append("&pid="+60);
		//金钱卡
		//paramBuff.append("&mid="+"money");
		//置顶卡
		//paramBuff.append("&mid="+"stick");
		//paramBuff.append("&pid="+91);
		//抢沙发
		paramBuff.append("&mid="+"sofa");
		paramBuff.append("&pid="+74);
		
		
		
		paramBuff.append("&appId="+appId);
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	
	private static String testMagicList(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/magic/list";
		Map<String, String>param=new HashMap<String, String>();
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testMyMagic(){
		String url="http://192.168.0.173:8080/jeebbs5/api/front/magic/myMagic";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("appId="+appId);
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String>param=new HashMap<String, String>();
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
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
