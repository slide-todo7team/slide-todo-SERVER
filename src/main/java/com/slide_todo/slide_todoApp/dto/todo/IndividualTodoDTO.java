package com.slide_todo.slide_todoApp.dto.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.dto.goal.GoalTitleDTO;
import lombok.Data;

@Data
public class IndividualTodoDTO {

  private Long id;
  private String title;
  @JsonProperty("is_done")
  private Boolean isDone;
  @JsonProperty("link_url")
  private String linkUrl;
  @JsonProperty("created_at")
  private String createdAt;
  @JsonProperty("updated_at")
  private String updatedAt;
  @JsonProperty("note_id")
  private Long noteId;
  private GoalInTodoDTO goal;

  public IndividualTodoDTO(IndividualTodo todo, Goal goal) {
    this.id = todo.getId();
    this.title = todo.getTitle();
    this.isDone = todo.getIsDone();
    this.linkUrl = todo.getLinkUrl();
    this.createdAt = todo.getCreatedAt().toString();
    this.updatedAt = todo.getUpdatedAt().toString();
    this.noteId = todo.getNote().getId();
    this.goal = new GoalInTodoDTO(goal);
  }

}
