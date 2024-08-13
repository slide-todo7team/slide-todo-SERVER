package com.slide_todo.slide_todoApp.dto.goal;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SingleGoalDTO {
    private Long id;
    private String title;
    private Long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal progress;

}
