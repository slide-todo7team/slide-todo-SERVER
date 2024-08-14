package com.slide_todo.slide_todoApp.dto.todo;

import lombok.Data;

@Data
public class TodoCreateDTO {

  private Long goalId;
  private String title;

  public TodoCreateDTO(Long goalId, String title) {
    this.goalId = goalId;
    this.title = title;
  }
}
