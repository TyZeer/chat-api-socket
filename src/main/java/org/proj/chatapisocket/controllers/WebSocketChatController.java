package org.proj.chatapisocket.controllers;

import org.proj.chatapisocket.dto.ChatMessageTopic;
import org.proj.chatapisocket.dto.ChatMessageWs;
import org.proj.chatapisocket.dto.NotificationDto;
import org.proj.chatapisocket.models.ChatMessage;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.services.ChatMessageService;
import org.proj.chatapisocket.services.ChatRoomService;
import org.proj.chatapisocket.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Set;

@Controller
public class WebSocketChatController {

    private static final Logger log = LoggerFactory.getLogger(WebSocketChatController.class);

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
    public ChatMessageWs sendMessage(@Payload ChatMessageWs message,
                                     SimpMessageHeaderAccessor headerAccessor,
                                     Principal principal) {
        log.info("Получено сообщение от клиента: {}", message);

        try {
            String senderUsername = principal.getName();
            message.setSender(senderUsername);

            log.debug("Установлен отправитель: {}", senderUsername);

            String chatRoomId = String.valueOf(message.getChatId());

            Set<String> members = chatRoomService.getMembersUsernames(message.getChatId());

            if (!members.contains(senderUsername)) {
                log.warn("Пользователь '{}' не является участником чата ID={}", senderUsername, message.getChatId());
                return null;
            }

            log.debug("Пользователь '{}' является участником чата ID={}", senderUsername, message.getChatId());

            User sender = userService.getByUsername(senderUsername);
            if (sender == null) {
                log.error("Пользователь с username={} не найден в системе", senderUsername);
                return null;
            }

            ChatMessage savedMessage = chatMessageService.sendMessage(
                    message.getChatId(),
                    sender,
                    message.getContent(),
                    message.getFileUrl()
            );

            log.info("Сообщение успешно сохранено: id={}, chatId={}, sender={}",
                    savedMessage.getId(), savedMessage.getChatRoom().getId(), savedMessage.getSender().getUsername());

            ChatMessageTopic redirMessage = new ChatMessageTopic(
                    savedMessage.getSender().getUsername(),
                    savedMessage.getContent(),
                    savedMessage.getFileUrl(),
                    savedMessage.getTimestamp(),
                    savedMessage.getChatRoom().getId(),
                    savedMessage.getId()
            );

            messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, redirMessage);
            log.debug("Отправлено сообщение в топик: /topic/chat/{}", chatRoomId);

            sendNotificationsToChatParticipants(message.getChatId(), redirMessage.getSender(), savedMessage.getId());

            return message;
        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения WebSocket", e);
            return null;
        }
    }

    private void sendNotificationsToChatParticipants(Long chatId, String excludedUsername, Long messageId) {
        try {
            Set<String> participants = chatRoomService.getMembersUsernames(chatId);

            log.debug("Отправка уведомлений участникам чата ID={} (исключая '{}')", chatId, excludedUsername);

            participants.stream()
                    .filter(username -> !username.equals(excludedUsername))
                    .forEach(username -> {
                        try {
                            messagingTemplate.convertAndSendToUser(
                                    username,
                                    "/queue/notifications",
                                    new NotificationDto("NEW_MESSAGE", chatId, messageId)
                            );
                            log.debug("Уведомление отправлено пользователю: {}", username);
                        } catch (Exception e) {
                            log.error("Не удалось отправить уведомление пользователю '{}'", username, e);
                        }
                    });
        } catch (Exception e) {
            log.error("Ошибка при отправке уведомлений участникам чата ID={}", chatId, e);
        }
    }
}
