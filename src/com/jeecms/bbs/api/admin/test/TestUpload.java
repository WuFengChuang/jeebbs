package com.jeecms.bbs.api.admin.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import com.jeecms.common.upload.FileUpload;
import com.jeecms.common.util.AES128Util;
import com.jeecms.common.util.Num62;
import com.jeecms.common.util.PayUtil;

public class TestUpload {

	public static void main(String[] args) {
		testUpload();
		//testUpload2();
	}
	
	private static String testUpload(){
		String url="http://192.168.0.173:8080/jeebbs5/api/admin/upload/o_upload";
		FileUpload fileUpload = new FileUpload();
		String filePath="D:\\test\\1.jpg";
		String type="image";
		String nonce_str=RandomStringUtils.random(16,Num62.N62_CHARS);
		StringBuffer paramBuff=new StringBuffer();
		Map<String, String>param=new HashMap<String, String>();
		paramBuff.append("type="+type);
		paramBuff.append("&appId="+appId);
		paramBuff.append("&nonce_str="+nonce_str);
		String encryptSessionKey="";
		try {
			encryptSessionKey=AES128Util.encrypt(sessionKey, aesKey,ivKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		paramBuff.append("&sessionKey="+encryptSessionKey);
		String []params=paramBuff.toString().split("&");
		for(String p:params){
			String keyValue[]=p.split("=");
			param.put(keyValue[0], keyValue[1]);
		}
		String sign=PayUtil.createSign(param, appKey);
		paramBuff.append("&sign="+sign);
		String result ="";
		try {
			result= fileUpload.uploadFileWithHttpMime(type,
					url, filePath, appId, encryptSessionKey,nonce_str, sign);
			System.out.println(result);
			JSONObject json=new JSONObject(result);
			String status=(String) json.get("status");
			if(status.equals("true")){
				JSONObject bodyJson= (JSONObject) json.get("body");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static String testUpload2(){
		String url="http://192.168.0.140:80/jeebbs5/api/front/upload/o_upload2";
		FileUpload fileUpload = new FileUpload();
		String filePath="D:\\test\\1.jpg";
		String result ="";
		try {
			// 实例化post提交方式  
		    HttpPost post = new HttpPost(url);  
		    //创建HttpClientBuilder  
		    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
	       //HttpClient  
	        CloseableHttpClient httpClient = httpClientBuilder.build(); 
	        
		    StringBuffer buffer  = new StringBuffer();  ;
		    // 添加json参数  
		    try {  
		        MultipartEntityBuilder builder = MultipartEntityBuilder.create();  
		        //builder.setCharset(Charset.forName("uft-8"));//设置请求的编码格式  
		        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式  
		        File file = new File(filePath);  
		        builder.addBinaryBody("file", file);  
		        HttpEntity entity = builder.build();// 生成 HTTP POST 实体        
		        post.setEntity(entity);//设置请求参数  
		        HttpResponse response = httpClient.execute(post);// 发起请求 并返回请求的响应  
		        HttpEntity resEntity=response.getEntity();
		        InputStream is = resEntity.getContent();  
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is));  
		       
		        String temp;  
		  
		        while ((temp = reader.readLine()) != null) {  
		            buffer.append(temp);  
		        }  
		  
		        System.out.println("buffer->"+buffer);  
		  
		    } catch (UnsupportedEncodingException e) {  
		        e.printStackTrace();  
		    } catch (ClientProtocolException e) {  
		        e.printStackTrace();  
		    } catch (IOException e) {  
		        e.printStackTrace();  
		    } catch (IllegalStateException e) {  
		        e.printStackTrace();  
		    }
			JSONObject json=new JSONObject(buffer.toString());
			String status=(String) json.get("status");
			if(status.equals("true")){
				JSONObject bodyJson= (JSONObject) json.get("body");
				System.out.println(bodyJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static String appId="7166912116544627";
	private static String appKey="vDnwyGf4Ej8eCcqLkhjaHSmav2TAXGVa";
	private static String sessionKey="7C029A3C5EECEDB1F4171D0F21BAB32B";
	private static String aesKey="wKIFyACLEUvHnSIT";
	private static String ivKey="1yTSp6TP47uP12RK";
}
