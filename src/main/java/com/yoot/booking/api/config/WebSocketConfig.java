package com.yoot.booking.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig
        implements WebSocketMessageBrokerConfigurer {

    // ================= AUTH =================
    private final WebSocketAuthInterceptor authInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // subscribe
        registry.enableSimpleBroker("/topic", "/queue");

        // send
        registry.setApplicationDestinationPrefixes("/app");

        // private message
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .addInterceptors(authInterceptor)
                .setHandshakeHandler(
                        new UserHandshakeHandler()
                )
                .setAllowedOrigins( "http://localhost:5173" )
                .withSockJS();
    }
}
