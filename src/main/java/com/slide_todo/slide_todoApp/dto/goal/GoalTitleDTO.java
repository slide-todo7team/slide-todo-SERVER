package com.slide_todo.slide_todoApp.dto.goal;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

@Data
public class GoalTitleDTO {
    private String title;

    @JsonCreator
    public GoalTitleDTO(String title) {
        this.title = title;
    }
}
