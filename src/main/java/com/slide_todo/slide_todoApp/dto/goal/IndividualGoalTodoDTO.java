package com.slide_todo.slide_todoApp.dto.goal;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class IndividualGoalTodoDTO {
    private Long id; //목표 id
    private String title;
    private Long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal progress;
    private List<IndividualTodoDto> todos;

    @Getter
    @Setter
    @Builder
    public static class IndividualTodoDto {
        private Long noteId; //연결된 노트 Id
        private Long id; //할일 id
        private String title;
        private Boolean isDone;

    }

}
