package org.proj.chatapisocket.dto;

import java.util.Set;

public class ChatCreationNotification {
    private Long chatId;
    private String chatName;
    private Set<Long> memberIds;

    // Конструкторы, геттеры и сеттеры
    public ChatCreationNotification() {}

    public ChatCreationNotification(Long chatId, String chatName, Set<Long> memberIds) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.memberIds = memberIds;
    }

    // Геттеры и сеттеры
    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }
    public String getChatName() { return chatName; }
    public void setChatName(String chatName) { this.chatName = chatName; }
    public Set<Long> getMemberIds() { return memberIds; }
    public void setMemberIds(Set<Long> memberIds) { this.memberIds = memberIds; }
}
