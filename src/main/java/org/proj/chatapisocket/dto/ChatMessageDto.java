package org.proj.chatapisocket.dto;

import java.time.LocalDateTime;

public class ChatMessageDto {
    private Long id;
    private UserDto sender;
    private String content;
    private String fileUrl;
    private LocalDateTime timestamp;

    // Конструкторы
    public ChatMessageDto() {
    }

    public ChatMessageDto(Long id, UserDto sender, String content, String fileUrl, LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.fileUrl = fileUrl;
        this.timestamp = timestamp;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getSender() {
        return sender;
    }

    public void setSender(UserDto sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
