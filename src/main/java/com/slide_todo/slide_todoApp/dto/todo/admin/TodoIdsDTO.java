package com.slide_todo.slide_todoApp.dto.todo.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class TodoIdsDTO {

  @JsonProperty("todo_ids")
  private List<Long> todoIds;

  @JsonCreator
  public TodoIdsDTO(List<Long> todoIds) {
    this.todoIds = todoIds;
  }
}
