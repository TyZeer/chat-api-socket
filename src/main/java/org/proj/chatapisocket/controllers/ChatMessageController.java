package org.proj.chatapisocket.controllers;

import org.proj.chatapisocket.dto.ChatMessageDto;
import org.proj.chatapisocket.models.ChatMessage;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
