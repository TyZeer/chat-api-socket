package org.proj.chatapisocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePrivateChatDto {
    @JsonProperty("name")
    @NotBlank(message = "Название чата не может быть пустым")
    @NotEmpty(message = "Название чата не может быть пустым")
    private String name;
    @JsonProperty("user1Id")
    @NotBlank(message = "ID не может быть пустым")
    private Long user1Id;
    @JsonProperty("user2Id")
    @NotBlank(message = "ID не может быть пустым")
    private Long user2Id;
}
