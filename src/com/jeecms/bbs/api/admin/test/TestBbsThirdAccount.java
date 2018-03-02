package com.jeecms.bbs.api.admin.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.HttpClientUtil;

public class TestBbsThirdAccount {
	private static String base = "http://192.168.0.150:8080/jeebbs5/api/admin";
	private static String appId="7166912116544627";
	private static String appKey="5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey="9D06EE9416583526DC3A11573E10DC3A";
	private static String aesKey="MnYg7Tm8NR1YiYBJ";
	private static String ivKey="yToM65IuE64VDoEq";
	
	public static void main(String[] args) {
		System.out.println(testAccountList());
	}
	
	
	private static String testAccountList(){
		String url = base+"/account/list";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("pageNo="+1);
		paramBuff.append("&pageSize="+10);
		paramBuff.append("&username=");
		paramBuff.append("&source=");
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
	
	private static String testAccountDel(){
		String url=base+"/account/delete";
		StringBuffer paramBuff=new StringBuffer();
		paramBuff.append("ids="+"1");
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
		String sign = PayUtil.createSign(param, appKey);
		param.put("sign", sign);
		String res=HttpClientUtil.getInstance().postParams(url, param);
		return res;
	}
}
