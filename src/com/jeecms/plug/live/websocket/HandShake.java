package com.jeecms.plug.live.websocket;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
/**
 * Socket建立连接（握手）和断开
 * 
 * @author tom
 */

import com.jeecms.common.web.session.SessionProvider;
import com.jeecms.plug.live.action.admin.BbsLiveAct;

@Component
public class HandShake implements HandshakeInterceptor {
	private static final Logger log = LoggerFactory.getLogger(BbsLiveAct.class);
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, 
			WebSocketHandler wsHandler,Map<String, Object> attributes) throws Exception {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			//HttpSession session = servletRequest.getServletRequest().getSession(false); 
			// 标记用户
			Integer uid = null;
			Object userId=session.getAttribute(servletRequest.getServletRequest(), "userId");
			if(userId!=null){
				uid=(Integer) userId;
			}
			System.out.println("uid->"+uid);
			// 标记用户
			if (uid != null) {
				attributes.put("uid", uid);
			} else {
				return false;
			}
			log.info("Websocket:用户[ID:" + uid + "]已经建立连接");
		}
		return true;
	}

	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
	}

	@Autowired
	private SessionProvider session;
}