package org.proj.chatapisocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckAuth {
    @JsonProperty("token")
    private String token;
    @JsonProperty("username")
    private String username;
}
