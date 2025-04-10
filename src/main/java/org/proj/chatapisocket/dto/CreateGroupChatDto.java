package org.proj.chatapisocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private String name;
    @JsonProperty("member_ids")
    private Set<Long> memberIds;
}
