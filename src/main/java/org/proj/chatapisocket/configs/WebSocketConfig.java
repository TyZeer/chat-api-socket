package org.proj.chatapisocket.configs;

import org.proj.chatapisocket.security.jwt.JwtUtil;
import org.proj.chatapisocket.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue", "/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authHeader = accessor.getNativeHeader("Authorization");

                    if (authHeader != null && !authHeader.isEmpty()) {
                        try {
                            String token = authHeader.get(0).replace("Bearer ", "");
                            String username = jwtUtil.extractUserName(token);
                            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

                            if (jwtUtil.isTokenValid(token, userDetails)) {
                                UsernamePasswordAuthenticationToken auth =
                                        new UsernamePasswordAuthenticationToken(
                                                userDetails, null, userDetails.getAuthorities());
                                accessor.setUser(auth);
                                SecurityContextHolder.getContext().setAuthentication(auth);
                            }
                        } catch (Exception e) {
                            throw new MessagingException("Authentication failed: " + e.getMessage());
                        }
                    } else {
                        throw new MessagingException("Authorization header is missing");
                    }
                }
                return message;
            }
        });
    }
}
