package com.jeecms.bbs.api.member.test;

import java.util.HashMap;
import java.util.Map;

import com.jeecms.common.util.AES128Util;
import com.jeecms.common.web.HttpClientUtil;

public class TestLiveCustomerOrder {

	private static String base = "http://192.168.0.167:8080/jeebbs5/api/member";
	private static String appId = "7166912116544627";
	private static String appKey = "5atYoyckDzDPetcaQZlF1VsK1o8qCQPE";
	private static String sessionKey = "3C164470B566C9305AC76F7D2382E8CF";
	private static String aesKey = "MnYg7Tm8NR1YiYBJ";
	private static String ivKey = "yToM65IuE64VDoEq";
	
	public static void main(String[] args) {
		testLiveCustomerOrder();

	}
	
	private static String testLiveCustomerOrder() {
		String url = base + "/customerOrder/list";
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("&appId=" + appId);
		String encryptSessionKey = "";
		try {
			encryptSessionKey = AES128Util.encrypt(sessionKey, aesKey, ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey=" + encryptSessionKey);
		Map<String, String> param = new HashMap<String, String>();
		String[] params = paramBuff.toString().split("&");
		for (String p : params) {
			String keyValue[] = p.split("=");
			if (keyValue.length == 2) {
				param.put(keyValue[0], keyValue[1]);
			}
		}
		param.put("sessionKey", encryptSessionKey);
		String res = HttpClientUtil.getInstance().postParams(url, param);
		System.out.println("res->" + res);
		return res;
	}

}
