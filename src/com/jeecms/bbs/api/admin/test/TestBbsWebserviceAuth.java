package com.jeecms.bbs.api.admin.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestBbsWebserviceAuth {
	private static String base = "http://192.168.0.150:8080/jeebbs5/api/admin";
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="F82BD7B69D5E1C9E70EF2E1D5F105A62";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";
	
	public static void main(String[] args) {
		System.out.println(testSpaceList());
	}
	
	private static String testSpaceList(){
		String url = base+"/webserviceAuth/list";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("pageNo="+1);
		paramBuff.append("&pageSize="+10);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testSpaceGet(){
		String url = base+"/webserviceAuth/get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+1);
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	
	private static String testSpaceSave(){
		String url = base+"/webserviceAuth/save";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("username="+"ceshi");//名称
		paramBuff.append("&system="+"ceshi");//描述
		paramBuff.append("&password="+"ceshi");//图片地址
		paramBuff.append("&enable="+true);//是否启用 
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testSpaceUpd(){
		String url = base+"/webserviceAuth/update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+2);//名称
		paramBuff.append("&username="+"ceshi2");//名称
		paramBuff.append("&system="+"ceshi");//描述
		paramBuff.append("&password="+"ceshi");//图片地址
		paramBuff.append("&enable="+false);//是否启用 
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
	
	private static String testSpaceDel(){
		String url = base+"/webserviceAuth/delete";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("ids="+"2,3,4");//名称
		paramBuff.append("&appId="+appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		Map<String, String> param = new HashMap<String,String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length==2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
}
