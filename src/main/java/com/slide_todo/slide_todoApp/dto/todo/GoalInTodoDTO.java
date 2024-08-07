package com.slide_todo.slide_todoApp.dto.todo;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import lombok.Data;

@Data
public class GoalInTodoDTO {

  private Long id;
  private String title;

  public GoalInTodoDTO(Goal goal) {
    this.id = goal.getId();
    this.title = goal.getTitle();
  }
}
