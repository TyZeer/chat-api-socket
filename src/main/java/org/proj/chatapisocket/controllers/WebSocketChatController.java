package org.proj.chatapisocket.controllers;

import org.proj.chatapisocket.dto.ChatMessageTopic;
import org.proj.chatapisocket.dto.ChatMessageWs;
import org.proj.chatapisocket.dto.NotificationDto;
import org.proj.chatapisocket.models.ChatMessage;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.services.ChatMessageService;
import org.proj.chatapisocket.services.ChatRoomService;
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
import java.util.Set;

@Controller
public class WebSocketChatController {

    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    public WebSocketChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public ChatMessageWs sendMessage(@Payload ChatMessageWs message, SimpMessageHeaderAccessor headerAccessor, Principal principal) {
        message.setSender(principal.getName());
        String chatRoomId = String.valueOf(message.getChatId());
        if(!chatRoomService.getMembersUsernames(message.getChatId()).contains(message.getSender())){
            return null;
        }
        ChatMessage savedMessage = chatMessageService.sendMessage(message.getChatId(), userService.getByUsername(message.getSender()), message.getContent(), message.getFileUrl());
        ChatMessageTopic redirMessage = new ChatMessageTopic(
                savedMessage.getSender().getUsername(),
                savedMessage.getContent(),
                savedMessage.getFileUrl(),
                savedMessage.getTimestamp(),
                savedMessage.getChatRoom().getId(),
                savedMessage.getId()
                );
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, redirMessage);
        sendNotificationsToChatParticipants(message.getChatId(), redirMessage.getSender(), savedMessage.getId());
        return message;
    }
    private void sendNotificationsToChatParticipants(Long chatId, String excludedUsername, Long messageId) {
        Set<String> participants = chatRoomService.getMembersUsernames(chatId);

        participants.stream()
                .filter(username -> !username.equals(excludedUsername))
                .forEach(username -> {
                    messagingTemplate.convertAndSendToUser(
                            username,
                            "/queue/notifications",
                            new NotificationDto("NEW_MESSAGE", chatId, messageId)
                    );
                });
    }

}
