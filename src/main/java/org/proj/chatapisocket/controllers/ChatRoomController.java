package org.proj.chatapisocket.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.proj.chatapisocket.dto.ChatRoomDto;
import org.proj.chatapisocket.models.ChatRoom;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.repos.ChatRoomRepository;
import org.proj.chatapisocket.repos.UserRepository;
import org.proj.chatapisocket.security.jwt.JwtUtil;
import org.proj.chatapisocket.services.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @PostMapping("/group")
    public ChatRoom createGroupChat(@RequestParam String name, @RequestParam Set<Long> memberIds) {
        if(!name.isEmpty() && !name.isBlank())
            return chatRoomService.createGroupChat(name, memberIds);
        else
            return null;
    }

    @PostMapping("/private")
    public ChatRoom createPrivateChat(@RequestParam String name, @RequestParam Long user1Id, @RequestParam Long user2Id) {
        if(!name.isEmpty() && !name.isBlank()) {
            if (chatRoomRepository.existsPrivateChatBetweenUsers(user1Id, user2Id)) {
                throw new IllegalStateException("Приватный чат между этими пользователями уже существует");
            }
            return chatRoomService.createPrivateChat(name, user1Id, user2Id);
        }
        else
            return null;
    }

    @PostMapping("/{chatRoomId}/add-user")
    public ChatRoom addUserToGroupChat(@PathVariable Long chatRoomId, @RequestParam Long userId) {
        return chatRoomService.addUserToGroupChat(chatRoomId, userId);
    }
    @GetMapping("/my")
    public List<ChatRoomDto> getChatRooms(HttpServletRequest request) {

        Enumeration<String> headers = request.getHeaders("Authorization");
        if (headers != null) {
            if(headers.hasMoreElements()) {
                String token = headers.nextElement();
                token = token.replace("Bearer ", "");
                String username = jwtUtil.extractUserName(token);
                Optional<User> user = userRepository.findByUsername(username);
                if (user.isPresent()) {
                    return chatRoomService.getMyChatRooms(user.get());
                }
            }
        }
        return null;
    }
}
