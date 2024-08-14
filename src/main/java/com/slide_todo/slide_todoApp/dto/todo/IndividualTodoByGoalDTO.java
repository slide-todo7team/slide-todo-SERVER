package com.slide_todo.slide_todoApp.dto.todo;

import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class IndividualTodoByGoalDTO {

  private Long id;
  private String title;
  private Boolean isDone;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long noteId;

  public IndividualTodoByGoalDTO(IndividualTodo todo) {
    this.id = todo.getId();
    this.title = todo.getTitle();
    this.isDone = todo.getIsDone();
    this.createdAt = todo.getCreatedAt();
    this.updatedAt = todo.getUpdatedAt();
    if (todo.getNote() != null) {
      this.noteId = todo.getNote().getId();
    } else {
      this.noteId = null;
    }
  }
}
