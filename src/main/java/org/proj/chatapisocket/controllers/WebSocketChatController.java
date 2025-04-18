package org.proj.chatapisocket.controllers;

import org.proj.chatapisocket.dto.ChatMessageWs;
import org.proj.chatapisocket.models.ChatMessage;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.services.ChatMessageService;
import org.proj.chatapisocket.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketChatController {

    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserService userService;

    @Autowired
    public WebSocketChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public ChatMessageWs sendMessage(@Payload ChatMessageWs message, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
        System.out.println("Received message: " + message.getContent());
        message.setSender(principal.getName());
        String chatRoomId = String.valueOf(message.getChatId());
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, message);

        chatMessageService.sendMessage(message.getChatId(), userService.getByUsername(message.getSender()), message.getContent(), message.getFileUrl());
        return message;
    }

    @MessageMapping("/chat.subscribeToCreations")
    public void subscribeToChatCreations(Principal principal) {
    }

}
