package com.slide_todo.slide_todoApp.dto.note;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NoteCreateDTO {

  @JsonProperty("todo_id")
  private Long todoId;
  private String title;
  private String content;
  @JsonProperty("link_url")
  private String linkUrl;

  public NoteCreateDTO(Long todoId, String title, String content, String linkUrl) {
    this.todoId = todoId;
    this.title = title;
    this.content = content;
    this.linkUrl = linkUrl;
  }
}
