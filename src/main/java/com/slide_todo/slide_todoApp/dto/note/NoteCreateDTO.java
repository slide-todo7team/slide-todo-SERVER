package com.slide_todo.slide_todoApp.dto.note;

import lombok.Data;

@Data
public class NoteCreateDTO {

  private Long todoId;
  private String title;
  private String content;
  private String linkUrl;

  public NoteCreateDTO(Long todoId, String title, String content, String linkUrl) {
    this.todoId = todoId;
    this.title = title;
    this.content = content;
    this.linkUrl = linkUrl;
  }
}
