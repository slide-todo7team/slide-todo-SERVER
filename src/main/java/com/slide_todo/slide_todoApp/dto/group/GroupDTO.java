package com.slide_todo.slide_todoApp.dto.group;

import lombok.Data;

@Data
public class GroupDTO {
    private Long id;
    private String title;

    public GroupDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
