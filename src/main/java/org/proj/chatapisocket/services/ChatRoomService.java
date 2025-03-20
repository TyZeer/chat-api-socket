package org.proj.chatapisocket.services;

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

    public ChatRoom createPrivateChat(Long user1Id, Long user2Id) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName("Private Chat");
        chatRoom.setGroup(false);

        User user1 = userRepository.findById(user1Id).orElseThrow(() -> new RuntimeException("User not found"));
        User user2 = userRepository.findById(user2Id).orElseThrow(() -> new RuntimeException("User not found"));

        Set<User> members = new HashSet<>();
        members.add(user1);
        members.add(user2);
        chatRoom.setMembers(members);

        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom addUserToGroupChat(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

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
            myChatRoomDtos.add(chatRoomDto);
        }
        return myChatRoomDtos;
    }
}
