package org.proj.chatapisocket.dto;

public class UserDto {
    private Long id;
    private String username;

    // Конструктор, геттеры и сеттеры
    public UserDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    // Геттеры
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}