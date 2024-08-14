package com.slide_todo.slide_todoApp.dto.todo.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import lombok.Data;

@Data
public class TodoIdsDTO {

  private List<Long> todoIds;

  @JsonCreator
  public TodoIdsDTO(List<Long> todoIds) {
    this.todoIds = todoIds;
  }
}
