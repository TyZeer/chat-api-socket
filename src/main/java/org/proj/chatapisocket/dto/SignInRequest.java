package org.proj.chatapisocket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Schema(description = "Запрос на аутентификацию")
@Validated
public class SignInRequest {

    @Schema(description = "Имя пользователя", example = "Jon")
    @Size(min = 5, max = 50, message = "Ник должен быть от 5 до 50 символов")
    @NotBlank(message = "Ник не может быть пустым")
    @NotNull
    private String username;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = 8, max = 255, message = "Пароль должен быть от 8 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    @NotNull
    private String password;
}
