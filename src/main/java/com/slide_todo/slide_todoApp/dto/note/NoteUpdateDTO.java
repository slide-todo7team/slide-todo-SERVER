package com.slide_todo.slide_todoApp.dto.note;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NoteUpdateDTO {

  private String title;
  private String content;
  @JsonProperty("link_url")
  private String linkUrl;

  public NoteUpdateDTO(String title, String content, String linkUrl) {
    this.title = title;
    this.content = content;
    this.linkUrl = linkUrl;
  }
}
