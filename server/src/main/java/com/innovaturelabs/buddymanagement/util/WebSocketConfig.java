package com.innovaturelabs.buddymanagement.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/session/");
        config.setApplicationDestinationPrefixes("/attendance");
    }

    @Override
    public  void registerStompEndpoints(StompEndpointRegistry registry){
        RequestUpgradeStrategy upgradeStrategy=new TomcatRequestUpgradeStrategy();
        registry.addEndpoint("/buddy/management").withSockJS();
        registry.addEndpoint("/buddy/management")
                .setHandshakeHandler(new DefaultHandshakeHandler(upgradeStrategy))
                .setAllowedOrigins("*");
    }

}
