package org.proj.chatapisocket.services;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.proj.chatapisocket.models.ChatMessage;
import org.proj.chatapisocket.models.ChatRoom;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.repos.ChatMessageRepository;
import org.proj.chatapisocket.repos.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        ChatMessage message = new ChatMessage();
        User managedSender = entityManager.find(User.class, sender.getId());
        if (managedSender == null) {
            throw new RuntimeException("User not found");
        }
        message.setSender(managedSender);
        message.setContent(content);
        message.setFileUrl(fileUrl);
        message.setTimestamp(LocalDateTime.now());
       // ChatRoom roomChat = sender.getChatRooms().stream().filter(room -> room.getId().equals(chatRoomId)).findFirst().orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        message.setChatRoom(chatRoom);
        entityManager.persist(message);
        return message;
    }

    public List<ChatMessage> getMessagesByChatRoomId(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }
}
