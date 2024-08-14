package com.slide_todo.slide_todoApp.dto.note;

import lombok.Data;

@Data
public class NoteUpdateDTO {

  private String title;
  private String content;
  private String linkUrl;

  public NoteUpdateDTO(String title, String content, String linkUrl) {
    this.title = title;
    this.content = content;
    this.linkUrl = linkUrl;
  }
}
