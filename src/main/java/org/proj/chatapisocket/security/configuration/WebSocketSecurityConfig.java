//package org.proj.chatapisocket.security.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.Message;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authorization.AuthorizationManager;
//import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
//import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
//import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
//import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//
//@Configuration
//@EnableWebSocketSecurity
//public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
//
//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages
//                .simpDestMatchers("/app/**").permitAll() // Secure WebSocket endpoints
//                .simpSubscribeDestMatchers("/topic/**").permitAll(); // Secure subscriptions
//    }
//
//    @Override
//    protected boolean sameOriginDisabled() {
//        return true; // Disable CSRF for WebSocket connections
//    }
//}
