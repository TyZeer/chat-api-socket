package org.proj.chatapisocket.controllers;

import org.proj.chatapisocket.dto.ChatMessageDto;
import org.proj.chatapisocket.dto.UpdateMessageRequest;
import org.proj.chatapisocket.models.ChatMessage;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<Page<ChatMessageDto>> getMessagesByChatRoomId(
            @PathVariable Long chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "timestamp,desc") String[] sort) {

        String[] sortParams = sort[0].split(",");
        Sort orders = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, orders);
        Page<ChatMessageDto> result = chatMessageService.getMessagesByChatRoomId(chatRoomId, pageable);
        return ResponseEntity.ok(result);
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
