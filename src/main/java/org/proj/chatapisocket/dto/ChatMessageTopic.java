package org.proj.chatapisocket.dto;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;


@Getter
@Setter
public class ChatMessageTopic extends ChatMessageWs{
    private Long chatMessageId;

    public ChatMessageTopic(String sender, String content, String fileUrl, LocalDateTime timestamp, Long chatId, Long chatMessageId) {
        super(sender, content, fileUrl, timestamp, chatId);
        this.chatMessageId = chatMessageId;
    }

    public ChatMessageTopic() {

    }
}
