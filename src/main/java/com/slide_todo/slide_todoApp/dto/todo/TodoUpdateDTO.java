package com.slide_todo.slide_todoApp.dto.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TodoUpdateDTO {

  private String title;
  @JsonProperty("content")
  private String content;

  public TodoUpdateDTO(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
