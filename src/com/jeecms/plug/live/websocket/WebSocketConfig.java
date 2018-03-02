package com.jeecms.plug.live.websocket;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
/**
 * WebScoket配置处理器
 * 
 * @author tom
@Configuration  
@EnableWebMvc  
 extends WebMvcConfigurerAdapter
 */

import com.jeecms.core.entity.CmsSite;
import com.jeecms.core.manager.CmsSiteMng;
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(handler, "/ws").addInterceptors(handShake).setAllowedOrigins("http://"+getSite().getDomain());
		registry.addHandler(handler, "/ws/sockjs").addInterceptors(handShake).setAllowedOrigins("http://"+getSite().getDomain()).withSockJS();
		//registry.addHandler(handler, "/ws").addInterceptors(handShake).setAllowedOrigins("*");
		//registry.addHandler(handler, "/ws/sockjs").addInterceptors(handShake).setAllowedOrigins("*").withSockJS();
	}
	
	@PostConstruct
	public void init(){
		setSite(siteMng.findById(1));
	}
	
	private CmsSite site;
	public CmsSite getSite() {
		return site;
	}
	public void setSite(CmsSite site) {
		this.site = site;
	}

	@Autowired
	WebSocketExtHandler handler;
	@Autowired
	HandShake handShake;
	@Autowired
	private CmsSiteMng siteMng;
}