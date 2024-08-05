package com.slide_todo.slide_todoApp.dto.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TodoUpdateDTO {

  private String title;
  @JsonProperty("link_url")
  private String linkUrl;

  public TodoUpdateDTO(String title, String linkUrl) {
    this.title = title;
    this.linkUrl = linkUrl;
  }
}
