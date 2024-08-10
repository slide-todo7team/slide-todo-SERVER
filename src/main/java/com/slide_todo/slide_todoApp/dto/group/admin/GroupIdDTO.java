package com.slide_todo.slide_todoApp.dto.group.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GroupIdDTO {
    @JsonProperty("groupIds")
    private List<Long> groupIds;

    @JsonCreator
    public GroupIdDTO(List<Long> groupIds) {
        this.groupIds = groupIds;
    }

}
