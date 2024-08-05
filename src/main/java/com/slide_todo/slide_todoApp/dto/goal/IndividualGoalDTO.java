package com.slide_todo.slide_todoApp.dto.goal;

import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IndividualGoalDTO {
    private Long id;
    private String title;
    private Long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public IndividualGoalDTO(IndividualGoal goal) {
        this.id = goal.getId();
        this.title = goal.getTitle();
        this.memberId = goal.getMember().getId();
        this.createdAt = goal.getCreatedAt();
        this.updatedAt = goal.getUpdatedAt();
    }
}
