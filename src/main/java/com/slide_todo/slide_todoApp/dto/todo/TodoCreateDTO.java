package com.slide_todo.slide_todoApp.dto.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TodoCreateDTO {

  @JsonProperty("goal_id")
  private Long goalId;
  private String title;

  public TodoCreateDTO(Long goalId, String title) {
    this.goalId = goalId;
    this.title = title;
  }
}
