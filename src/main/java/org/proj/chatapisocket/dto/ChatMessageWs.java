package org.proj.chatapisocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.proj.chatapisocket.models.User;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageWs {
    private String sender;
    private String content;
    private String fileUrl;
    private LocalDateTime timestamp;
    private Long chatId;
}

