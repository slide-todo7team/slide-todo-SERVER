package com.slide_todo.slide_todoApp.domain.note.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.note.Note;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AdminNoteDetailDTO {

  private Long id;
  private String title;
  private String content;
  @JsonProperty("created_at")
  private LocalDateTime createdAt;
  @JsonProperty("updated_at")
  private LocalDateTime updatedAt;
  @JsonProperty("link_url")
  private String linkUrl;
  private Writer writer;
  @JsonProperty("todo_title")
  private String todoTitle;

  public AdminNoteDetailDTO(Note note, Member writer) {
    this.id = note.getId();
    this.title = note.getTitle();
    this.content = note.getContent();
    this.createdAt = note.getCreatedAt();
    this.updatedAt = note.getUpdatedAt();
    this.linkUrl = note.getLinkUrl();
    this.writer = new Writer(writer);
    this.todoTitle = note.getTodo().getTitle();
  }


  /**
   * 노트 작성자
   */
  @Data
  public static class Writer {

      private Long id;
      private String nickname;

      public Writer(Member member) {
        if (member != null) {
          this.id = member.getId();
          this.nickname = member.getNickname();
        } else {
          this.id = null;
          this.nickname = null;
      }
    }
  }
}
