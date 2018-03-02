package com.jeecms.bbs.api.admin.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestCmsSensitivity {
	private static String base = "http://192.168.0.150:8080/jeebbs5/api/admin";
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="F82BD7B69D5E1C9E70EF2E1D5F105A62";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";
	
	public static void main(String[] args) {
		System.out.println(testSensitivityList());
	}
	
	private static String testSensitivityList(){
		String url = base+"/sensitivity/list";
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
	
	private static String testSensitivityGet(){
		String url = base+"/sensitivity/get";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("id="+6);
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
	
	private static String testSensitivitySave(){
		String url = base+"/sensitivity/save";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("search="+"111");
		paramBuff.append("&replacement="+"222");
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
	
	private static String testSensitivityBatchSave(){
		String url = base+"/sensitivity/batch_save";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("words="+"ssssa:233\nssssb:111");
		paramBuff.append("&type="+1);
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
	
//	private static String testSensitivityUpd(){
//		String url = base+"/sensitivity/update";
//		StringBuffer paramBuff = new StringBuffer();
//		paramBuff.append("id="+14);
//		paramBuff.append("&search="+"1111");
//		paramBuff.append("&replacement="+"222");
//		paramBuff.append("&appId="+appId);
//		String encryptSessionKey = "";
//		try {
//			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		paramBuff.append("&sessionKey="+encryptSessionKey);
//		Map<String, String> param = new HashMap<String,String>();
//		String[] params = paramBuff.toString().split("&");
//		for (String p : params) {
//			String keyValue[] = p.split("=");
//			if (keyValue.length==2) {
//				param.put(keyValue[0], keyValue[1]);
//			}
//		}
//		param.put("sessionKey", encryptSessionKey);
//		String sign = PayUtil.createSign(param, appKey);
//		param.put("sign", sign);
//		String res = HttpClientUtil.getInstance().postParams(url, param);
//		return res;
//	}
	
	private static String testSensitivityBatchUpd(){
		String url = base+"/sensitivity/batch_update";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("ids="+"14,13,12");
		paramBuff.append("&searchs="+"14,13,12");
		paramBuff.append("&replacements="+"12,13,14");
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
	
	private static String testSensitivityDel(){
		String url = base+"/sensitivity/delete";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("ids="+"14,13,12");
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
