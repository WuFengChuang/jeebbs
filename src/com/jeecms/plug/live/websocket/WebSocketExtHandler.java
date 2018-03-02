package com.jeecms.plug.live.websocket;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jeecms.plug.live.action.admin.BbsLiveAct;
import com.jeecms.plug.live.manager.BbsLiveMng;


/**
 * Socket处理器
 * 
 * @author tom
 */
@Component
public class WebSocketExtHandler implements WebSocketHandlerInterface {
	private static final Logger log = LoggerFactory.getLogger(BbsLiveAct.class);
	private  Map<String, WebSocketSession> userSocketSessionMap;
	private  Map<String, WebSocketSession> liveSocketSessionMap;
	
	@PostConstruct
	public void init(){
		userSocketSessionMap = new HashMap<String, WebSocketSession>();
		liveSocketSessionMap = new HashMap<String, WebSocketSession>();
	}
	
	/**
	 关闭live的所有客户连接
	*/
	public void closeLiveWebSocketSession(Integer liveId) {
		Iterator<Entry<String, WebSocketSession>> liveIt = liveSocketSessionMap.entrySet().iterator();
		while (liveIt.hasNext()) {
			Entry<String, WebSocketSession> entry = liveIt.next();
			if (entry.getKey().equals(liveId)) {
				//主动关闭会话
				WebSocketSession socketSession=entry.getValue();
				if(socketSession.isOpen()){
					try {
						socketSession.close();
					} catch (IOException e) {
						//e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	/**
	 * 建立连接后
	 */
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Integer uid = (Integer) session.getAttributes().get("uid");
		Integer liveId =null;
		String queryStr=session.getUri().getQuery();
		if(org.apache.commons.lang.StringUtils.isNotBlank(queryStr)){
			String params[]=queryStr.split("&");
			if(params!=null&&params.length>=2){
				String[]liveParam=params[1].split("=");
				if(liveParam!=null&&liveParam.length>=2&&
						org.apache.commons.lang.StringUtils.isNotBlank(liveParam[1])){
					liveId=Integer.parseInt(liveParam[1]);
				}
			}
		}
		if(liveId!=null){
			//新的连接则换成活动用户数量
			liveMng.sessionConnect(liveId, false);
			//存储新的session socket
			userSocketSessionMap.put(uid.toString(), session);
			liveSocketSessionMap.put(liveId.toString(), session);
		}
	}
	/**
	 * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
	 */
	public void handleMessage(WebSocketSession session,WebSocketMessage<?> message) throws Exception {
		if (message.getPayloadLength() == 0)
			return;
		Message msg = new Gson().fromJson(message.getPayload().toString(),Message.class);
		msg.setDate(new Date());
		sendMessageToUser(msg.getTo(), new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
	}

	/**
	 * 消息传输错误处理
	 */
	public void handleTransportError(WebSocketSession session,Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		Iterator<Entry<String, WebSocketSession>> liveIt = liveSocketSessionMap.entrySet().iterator();
		// 移除Socket会话
		while (it.hasNext()) {
			Entry<String, WebSocketSession> entry = it.next();
			if (entry.getValue().getId().equals(session.getId())) {
				userSocketSessionMap.remove(entry.getKey());
				log.info("Socket会话已经移除:用户ID" + entry.getKey());
				break;
			}
		}
		while (liveIt.hasNext()) {
			Entry<String, WebSocketSession> entry = liveIt.next();
			if (entry.getValue().getId().equals(session.getId())) {
				liveMng.sessionConnect(Integer.parseInt(entry.getKey()), true);
				liveSocketSessionMap.remove(entry.getKey());
				break;
			}
		}
	}

	/**
	 * 关闭连接后
	 */
	public void afterConnectionClosed(WebSocketSession session,CloseStatus closeStatus) throws Exception {
		log.info("Websocket:" + session.getId() + "已经关闭");
		Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		Iterator<Entry<String, WebSocketSession>> liveIt = liveSocketSessionMap.entrySet().iterator();
		// 移除Socket会话
		while (it.hasNext()) {
			Entry<String, WebSocketSession> entry = it.next();
			if (entry.getValue().getId().equals(session.getId())) {
				userSocketSessionMap.remove(entry.getKey());
				log.info("Socket会话已经移除:用户ID" + entry.getKey());
				break;
			}
		}
		while (liveIt.hasNext()) {
			Entry<String, WebSocketSession> entry = liveIt.next();
			if (entry.getValue().getId().equals(session.getId())) {
				liveMng.sessionConnect(Integer.parseInt(entry.getKey()), true);
				liveSocketSessionMap.remove(entry.getKey());
				break;
			}
		}
	}

	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给所有在线用户发送消息
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void broadcast(final TextMessage message) throws IOException {
		Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// 多线程群发
		while (it.hasNext()) {
			final Entry<String, WebSocketSession> entry = it.next();
			if (entry.getValue().isOpen()) {
				// entry.getValue().sendMessage(message);
				new Thread(new Runnable() {
					public void run() {
						try {
							if (entry.getValue().isOpen()) {
								entry.getValue().sendMessage(message);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
	}
	
	public static  void staticBroadcast(final TextMessage message) throws IOException{
		WebSocketExtHandler h=new WebSocketExtHandler();
		h.broadcast(message);
	}

	/**
	 * 给某个用户发送消息
	 * 
	 * @param userName
	 * @param message
	 * @throws IOException
	 */
	public void sendMessageToUser(Integer uid, TextMessage message)throws IOException {
		WebSocketSession session = userSocketSessionMap.get(uid.toString());
		if (session != null && session.isOpen()) {
			session.sendMessage(message);
		}
	}
	@Autowired
	private BbsLiveMng liveMng;
}