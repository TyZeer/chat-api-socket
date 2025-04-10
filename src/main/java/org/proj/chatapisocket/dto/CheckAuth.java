package org.proj.chatapisocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckAuth {
    @NotNull
    @JsonProperty("token")
    private String token;
    @NotNull
    @JsonProperty("username")
    private String username;
}
