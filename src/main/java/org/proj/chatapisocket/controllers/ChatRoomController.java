package org.proj.chatapisocket.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.proj.chatapisocket.dto.ChatRoomDto;
import org.proj.chatapisocket.dto.CreateGroupChatDto;
import org.proj.chatapisocket.dto.CreatePrivateChatDto;
import org.proj.chatapisocket.dto.UserDto;
import org.proj.chatapisocket.models.ChatRoom;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.repos.ChatRoomRepository;
import org.proj.chatapisocket.repos.UserRepository;
import org.proj.chatapisocket.security.jwt.JwtUtil;
import org.proj.chatapisocket.services.ChatRoomService;
import org.simpleframework.xml.core.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.header.Header;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
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


    @PostMapping("/group")
    public ResponseEntity<?> createGroupChat(@RequestBody CreateGroupChatDto dto, @AuthenticationPrincipal User currentUser) {
        try {
            Set<Long> ids = dto.getMemberIds();
            ids.add(currentUser.getId());
            chatRoomService.createGroupChat(dto.getName(), ids);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/private")
    public ResponseEntity<?> createPrivateChat(@RequestBody CreatePrivateChatDto dto ,@AuthenticationPrincipal User currentUser) {
        try {

            if(Objects.equals(currentUser.getId(), dto.getUserId())){
                throw new IllegalArgumentException("Нельзя добавить самого себя");
            }

            chatRoomService.createPrivateChat(
                    dto.getName(),
                    currentUser.getId(),
                    dto.getUserId()
            );
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{chatRoomId}/add-user")
    public ChatRoomDto addUserToGroupChat(@PathVariable Long chatRoomId, @RequestParam Long userId) {
        ChatRoom room = chatRoomService.addUserToGroupChat(chatRoomId, userId);
        String type = room.isGroup() ? "GROUP" : "PRIVATE";
        return new ChatRoomDto(room.getId().toString(), room.getName(), type);
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
//    @GetMapping("/{chatRoom}/users")
//    public List<UserDto> getUsersByChat(@PathVariable Long chatRoomId){
//        return userService.
//    }
}
