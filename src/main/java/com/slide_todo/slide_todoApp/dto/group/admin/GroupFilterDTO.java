package com.slide_todo.slide_todoApp.dto.group.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupFilterDTO {
    private String name;
    private String group;
}
