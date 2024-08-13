package com.slide_todo.slide_todoApp.dto.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class IndividualTodoByGoalDTO {

  private Long id;
  private String title;
  @JsonProperty("is_done")
  private Boolean isDone;
  @JsonProperty("created_at")
  private LocalDateTime createdAt;
  @JsonProperty("updated_at")
  private LocalDateTime updatedAt;
  @JsonProperty("note_id")
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
