package com.jeecms.bbs.api.admin.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestBbsForum {

	public static void main(String[] args) {
		//testForums();
		//testSaveForum();
		//testForumGet();
		//testUpdateForum();
		//testGetTpls();
		//testForumGroupByCategorys();
		//testBatchForum();
		testForumDel();
	}
	
	private static String testForums(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/forum/list";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("categoryId="+1);
		paramBuff.append("&first="+"0");
		paramBuff.append("&count="+5);
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
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		System.out.println("encryptSessionKey->"+encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testForumGroupByCategorys(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/forum/listGroupByCategory";
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
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		System.out.println("encryptSessionKey->"+encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testForumGet(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/forum/get";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+1);
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
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testForumDel(){
		String url="http://192.168.0.173:8080/jeebbs5/api/admin/forum/";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+1);
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
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testSaveForum(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/forum/save";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("categoryId="+1);
		paramBuff.append("&title="+"测试板块");
		paramBuff.append("&path="+"testpath");
		paramBuff.append("&description="+"description");
		paramBuff.append("&priority="+"10");
		paramBuff.append("&supportReward="+"true");
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
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		System.out.println("encryptSessionKey->"+encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testUpdateForum(){
		String url="http://192.168.0.173:8080/jeebbs5/api/admin/forum/update";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("id="+22);
		paramBuff.append("&categoryId="+1);
		paramBuff.append("&title="+"测试板块1");
		paramBuff.append("&path="+"testpath1");
		paramBuff.append("&description="+"description1");
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
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		System.out.println("encryptSessionKey->"+encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	
	private static String testBatchForum(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/forum/batchupdate";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("categorys=1,手游3,sy;14,test,teee;15,newc,ppp;16,newc2,ppp33");
		paramBuff.append("&forums="+"1111,p1,1,1;222,p2,1,2;new1,p3,1;new2,p4,14;new3,p5,15;new4,p5,15;new5,p6,16;new7,p7,16");
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
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign=PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		System.out.println("encryptSessionKey->"+encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	private static String testGetTpls(){
		String url="http://192.168.0.140:80/jeebbs5/api/admin/forum/getTpls";
		StringBuffer paramBuff=new StringBuffer();
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
			if(keyValue.length==2){
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->"+res);
		return res;
	}
	
	
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="3D59B3FDB9EF79547A751F1D0EC95EB6";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";
}
