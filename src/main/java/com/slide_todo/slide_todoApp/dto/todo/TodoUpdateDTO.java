package com.slide_todo.slide_todoApp.dto.todo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TodoUpdateDTO {

  private String title;

  @JsonCreator
  public TodoUpdateDTO(String title) {
    this.title = title;
  }
}
