package com.slide_todo.slide_todoApp.dto.goal;

import ch.qos.logback.classic.spi.LoggingEventVO;
import lombok.Data;
import java.util.List;

@Data
public class GoalTodosResponseDTO<T> {
    private int currPage;
    private Long totalCount;
    private List<T> goals;

    public GoalTodosResponseDTO(int currPage, Long totalCount,List<T> goals) {
        this.currPage = currPage;
        this.totalCount = totalCount;
        this.goals = goals;
    }

}
