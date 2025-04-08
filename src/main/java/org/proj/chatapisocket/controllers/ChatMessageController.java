package org.proj.chatapisocket.controllers;

import org.proj.chatapisocket.dto.ChatMessageDto;
import org.proj.chatapisocket.dto.UpdateMessageRequest;
import org.proj.chatapisocket.models.ChatMessage;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat-messages")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestParam Long chatRoomId, @RequestParam User sender, @RequestParam String content, @RequestParam(required = false) String fileUrl) {
        return chatMessageService.sendMessage(chatRoomId, sender, content, fileUrl);
    }

    @GetMapping("/{chatRoomId}")
    public List<ChatMessageDto> getMessagesByChatRoomId(@PathVariable Long chatRoomId) {
        return chatMessageService.getMessagesByChatRoomId(chatRoomId);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<ChatMessageDto> updateMessage(
            @PathVariable Long messageId,
            @RequestBody UpdateMessageRequest newContent,
            @AuthenticationPrincipal User currentUser) {

        ChatMessage updatedMessage = chatMessageService.updateMessage(messageId, newContent.getContent(), currentUser);
        return ResponseEntity.ok(chatMessageService.convertToDto(updatedMessage));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long messageId,
            @AuthenticationPrincipal User currentUser) {

        chatMessageService.deleteMessage(messageId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
