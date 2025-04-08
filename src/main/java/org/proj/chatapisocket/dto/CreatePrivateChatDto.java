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
    private String name;
    @JsonProperty("user1Id")
    private Long user1Id;
    @JsonProperty("user2Id")
    private Long user2Id;
}
