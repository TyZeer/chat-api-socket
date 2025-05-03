package org.proj.chatapisocket.services;

import jakarta.persistence.EntityNotFoundException;
import org.proj.chatapisocket.dto.ChatRoomDto;
import org.proj.chatapisocket.models.ChatRoom;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.repos.ChatRoomRepository;
import org.proj.chatapisocket.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    public ChatRoom createGroupChat(String name, Set<Long> memberIds) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        chatRoom.setGroup(true);

        Set<User> members = new HashSet<>();
        for (Long memberId : memberIds) {
            User user = userRepository.findById(memberId).orElseThrow(() -> new RuntimeException("User not found"));
            members.add(user);
        }
        chatRoom.setMembers(members);

        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom createPrivateChat(String name, Long user1Id, Long user2Id) {
        Long smallerId = Math.min(user1Id, user2Id);
        Long largerId = Math.max(user1Id, user2Id);

        boolean chatExists = chatRoomRepository.existsPrivateChatBetweenUsers(smallerId, largerId);

        if (chatExists) {
            throw new IllegalStateException("Приватный чат между этими пользователями уже существует");
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);

        User user1 = userRepository.findById(smallerId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + smallerId + " не найден"));
        User user2 = userRepository.findById(largerId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + largerId + " не найден"));

        chatRoom.getMembers().add(user1);
        chatRoom.getMembers().add(user2);

        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom addUserToGroupChat(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("Комната не найдена"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        chatRoom.getMembers().add(user);
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoomDto> getMyChatRooms(User user) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByMembers(Set.of(user));
        List<ChatRoomDto> myChatRoomDtos = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            ChatRoomDto chatRoomDto = new ChatRoomDto();
            chatRoomDto.id = String.valueOf(chatRoom.getId());
            chatRoomDto.name = chatRoom.getName();
            if (chatRoom.isGroup()) {
                chatRoomDto.type = "GROUP";
            }
            else
                chatRoomDto.type = "PRIVATE";
            Set<String> membersUsernames = getMembersUsernames(chatRoom.getId());
            chatRoomDto.setUsers(membersUsernames);
            myChatRoomDtos.add(chatRoomDto);
        }
        return myChatRoomDtos;
    }

    public Set<String> getMembersUsernames(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("ChatRoom not found with id: " + chatRoomId))
                .getMembers()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());
    }
}
