package org.proj.chatapisocket.services;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.proj.chatapisocket.dto.ChatMessageDto;
import org.proj.chatapisocket.dto.UserDto;
import org.proj.chatapisocket.models.ChatMessage;
import org.proj.chatapisocket.models.ChatRoom;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.repos.ChatMessageRepository;
import org.proj.chatapisocket.repos.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Transactional
    public ChatMessage sendMessage(Long chatRoomId, User sender, String content, String fileUrl) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("Комната не найдена"));
        ChatMessage message = new ChatMessage();
        User managedSender = entityManager.find(User.class, sender.getId());
        if (managedSender == null) {
            throw new RuntimeException("Пользователь не найден");
        }
        message.setSender(managedSender);
        message.setContent(content);
        message.setFileUrl(fileUrl);
        message.setTimestamp(LocalDateTime.now());
        message.setChatRoom(chatRoom);
        entityManager.persist(message);
        return message;
    }

    public List<ChatMessageDto> getMessagesByChatRoomId(Long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ChatMessageDto convertToDto(ChatMessage message) {
        UserDto userDto = new UserDto(
                message.getSender().getId(),
                message.getSender().getUsername()
        );

        return new ChatMessageDto(
                message.getId(),
                userDto,
                message.getContent(),
                message.getFileUrl(),
                message.getTimestamp()
        );
    }
}
