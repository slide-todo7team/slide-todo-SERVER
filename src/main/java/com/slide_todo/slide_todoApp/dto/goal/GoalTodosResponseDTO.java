package com.slide_todo.slide_todoApp.dto.goal;

import ch.qos.logback.classic.spi.LoggingEventVO;
import lombok.Data;
import java.util.List;

@Data
public class GoalTodosResponseDTO<T> {
    private Long nextCursor;
    private Long totalCount;
    private List<T> goals;

    public GoalTodosResponseDTO(Long nextCursor, Long totalCount,List<T> goals) {
        this.nextCursor = nextCursor;
        this.totalCount = totalCount;
        this.goals = goals;
    }

}
