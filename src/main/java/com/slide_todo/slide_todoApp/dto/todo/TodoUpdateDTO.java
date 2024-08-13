package com.slide_todo.slide_todoApp.dto.todo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TodoUpdateDTO {

  @JsonProperty("goal_id")
  private Long goalId;
  private String title;

  @JsonCreator
  public TodoUpdateDTO(Long goalId, String title) {
    this.goalId = goalId;
    this.title = title;
  }
}
