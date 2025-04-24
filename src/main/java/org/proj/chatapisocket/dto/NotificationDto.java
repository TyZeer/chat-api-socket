package org.proj.chatapisocket.dto;



import java.time.Instant;

public class NotificationDto {
    private String type;
    private Long chatId;
    private Long messageId;
    private Instant timestamp;

    public NotificationDto(String type, Long chatId, Long messageId) {
        this.type = type;
        this.chatId = chatId;
        this.messageId = messageId;
        this.timestamp = Instant.now();
    }

    public String getType() { return type; }
    public Long getChatId() { return chatId; }
    public Long getMessageId() { return messageId; }
    public Instant getTimestamp() { return timestamp; }
}

