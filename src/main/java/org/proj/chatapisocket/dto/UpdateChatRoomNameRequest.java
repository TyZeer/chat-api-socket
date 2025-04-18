package org.proj.chatapisocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateChatRoomNameRequest {
    // Геттеры и сеттеры
    @NotBlank
    @JsonProperty("new_name")
    private String newName;

}

