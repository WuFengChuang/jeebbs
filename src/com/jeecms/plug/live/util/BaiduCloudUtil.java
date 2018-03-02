package com.jeecms.plug.live.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidubce.BceClientConfiguration;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.lss.LssClient;
import com.baidubce.services.lss.model.PauseDomainStreamRequest;
import com.baidubce.services.lss.model.PauseDomainStreamResponse;
import com.baidubce.services.lss.model.ResumeDomainStreamRequest;
import com.jeecms.common.util.DateUtils;
import com.jeecms.common.web.HttpClientUtil;

public class BaiduCloudUtil {
	private static final Logger log = LoggerFactory.getLogger(BaiduCloudUtil.class);
	
	private final static String PROTOCOL_RTMP="rtmp://";
	private final static String PROTOCOL_HTTP="http://";
	//0表示禁用； 1表示允许推流；2表示断流
	private final static String LIVE_STATUS_DISABLED="0";
	private final static String LIVE_STATUS_CONNECT="1";
	private final static String LIVE_STATUS_RESET="2";
	private final static String STREAM_APP="/jeebbs/";
	
	//禁用流
	public static void disableStram(String accessKeyId,
			String accessKey,String domain,String streamId){
		 controlStram(accessKeyId, accessKey, domain,
				streamId,  LIVE_STATUS_DISABLED);
	}
	
	//启用流
	public static void connectStram(String accessKeyId,
			String accessKey,
			String domain, String streamId){
		 controlStram(accessKeyId, accessKey, domain,
				streamId, LIVE_STATUS_CONNECT);
	}
	
	private static void controlStram(
			String accessKeyId,
			String accessKey,
			String domain,
			String streamId,String status){
		// 初始化一个LssClient
	    BceClientConfiguration config = new BceClientConfiguration();
	    config.setCredentials(new DefaultBceCredentials(accessKeyId, accessKey));
	    LssClient client = new LssClient(config);
		if(status.equals(LIVE_STATUS_DISABLED)){
			PauseDomainStreamRequest request = new PauseDomainStreamRequest();
			request.withDomain(domain).withApp(STREAM_APP).withStream(streamId);
			client.pauseDomainStream(request);
		}else{
			ResumeDomainStreamRequest request = new ResumeDomainStreamRequest();
			request.withDomain(domain).withApp(STREAM_APP).withStream(streamId);
			client.resumeDomainStream(request);
		}
	}
	
	 public static String getPushUrl(String pushDomain,Integer userId,
				Date liveEndTime,String signingKey){
		 //rtmp://<push.your-domain.com>/<your-app>/<your-stream>?token=计算出来的token值&expire=2016-10-10T00:00:00Z
		 String token=getToken(pushDomain, userId, liveEndTime, signingKey);
		 StringBuffer buff=new StringBuffer();
		 buff.append(PROTOCOL_RTMP);
		 buff.append(pushDomain).append(STREAM_APP).append(userId)
		 .append("?token=").append(token).append("&expire=")
		 .append(getUTCTimeStr(liveEndTime));
		 return buff.toString();
	 }
	 
	 public static String getRtmpPlayUrl(String playDomain,Integer userId,Date liveEndTime,
			 String key){
		 StringBuffer buff=new StringBuffer();
		 buff.append(PROTOCOL_RTMP).append(playDomain)
		 .append(STREAM_APP).append(userId).append(
				 getPlayUrlSuffix(playDomain, userId, liveEndTime, key));
		 return buff.toString();
	 }
	 
	 public static String getPlayUrl(String playDomain,Integer userId,
			 Date liveEndTime,String key,String ext){
		 StringBuffer buff=new StringBuffer();
		 buff.append(PROTOCOL_HTTP).append(playDomain)
		 .append(STREAM_APP).append(userId).append(".").append(ext).append(
				 getPlayUrlSuffix(playDomain, userId, liveEndTime, key));;
		 return buff.toString();
	 }
	 
	 public static String sha256Hex(String stringToSign, String signingKey) {
	    try{
	        Mac mac = Mac.getInstance("HmacSHA256");
	        mac.init(new SecretKeySpec(signingKey.getBytes(Charset.forName("UTF-8")), "HmacSHA256"));
	        return new String(Hex.encodeHex(mac.doFinal(stringToSign.getBytes(Charset.forName("UTF-8")))));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	 
	private static String getUTCTimeStr(Date liveEndTime){
		liveEndTime=DateUtils.getDateMinute(liveEndTime, 10);
		Calendar cal = Calendar.getInstance();
		cal.setTime(liveEndTime);
	    // 取得时间偏移量：
	    int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
	    // 取得夏令时差：
	    int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
	    // 从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = foo.format(cal.getTime());
        return time;
	}
	
	private static String getPushStringToSign(String pushDomain,Integer userId,Date liveEndTime){
		StringBuffer buff=new StringBuffer();
		buff.append(PROTOCOL_RTMP).append(pushDomain).append(STREAM_APP+userId);
		buff.append(";").append(getUTCTimeStr(liveEndTime));
		return buff.toString();
	}
	
	public static String getToken(String pushDomain,Integer userId,
			Date liveEndTime,String signingKey){
		return sha256Hex(getPushStringToSign(pushDomain, userId, liveEndTime), 
				signingKey);
	}
	
	private static String getPlayUrlSuffix(String playDomain,Integer userId,Date liveEndTime,
			String key){
		StringBuffer buff=new StringBuffer();
		//直播结束后10分钟，可能网络延迟
		long txTime=liveEndTime.getTime()/1000+60*10;
		buff.append("?secret=").append(getSecret(playDomain, userId, txTime, key))
		.append("&timestamp="+txTime);
		return buff.toString();
	}
	
	private static String getSecret(String playDomain,Integer userId,long txTime,
			String key) {
        String input = new StringBuilder().
                append(key).append("/").append(playDomain).
                append(STREAM_APP).append(userId).append(txTime).toString();
        String secret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            secret  = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return secret ;
    }
	
	 private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];
        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
	  }
	 

    public static void main(String[] args) {
    	String pushDomain="push.jeecms.com";
    	Integer userId=5;
    	Date liveEndTime=DateUtils.getDateMinute(Calendar.getInstance().getTime(), 60);
    	String signingKey="xccqtn9uoop2scpr591ncbduefouxtxe";
    	System.out.println(getPushUrl(pushDomain, userId, liveEndTime, signingKey));
    	String playDomain="demo26.jeecms.com";
    	System.out.println(getRtmpPlayUrl(playDomain, userId, liveEndTime, signingKey));
    }

	
    
    private static final char[] DIGITS_LOWER =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

}