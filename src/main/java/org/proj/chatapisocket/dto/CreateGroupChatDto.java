package org.proj.chatapisocket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateGroupChatDto {

    @NotBlank(message = "Название чата не может быть пустым")
    private String name;

    @Size(min = 2, message = "В чате должно быть минимум 2 участника")
    private Set<Long> memberIds;
}
