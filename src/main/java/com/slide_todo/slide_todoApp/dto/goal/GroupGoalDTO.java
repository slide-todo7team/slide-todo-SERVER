package com.slide_todo.slide_todoApp.dto.goal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GroupGoalDTO {
    private Long id;
    private String title;
    private Long groupId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GroupGoalDTO(GroupGoal goal) {
        this.id = goal.getId();
        this.title = goal.getTitle();
        this.groupId = goal.getGroup().getId();
        this.createdAt = goal.getCreatedAt();
        this.updatedAt = goal.getUpdatedAt();
    }
}