package com.slide_todo.slide_todoApp.dto.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GroupCodeDTO {
    private String secretCode;

    @JsonCreator
    public GroupCodeDTO(@JsonProperty("secretCode") String secretCode) {
        this.secretCode = secretCode;
    }
}
