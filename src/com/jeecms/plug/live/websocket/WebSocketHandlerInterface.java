package com.jeecms.plug.live.websocket;

import org.springframework.web.socket.WebSocketHandler;

public interface WebSocketHandlerInterface  extends WebSocketHandler{
	public void closeLiveWebSocketSession(Integer liveId) ;
}
