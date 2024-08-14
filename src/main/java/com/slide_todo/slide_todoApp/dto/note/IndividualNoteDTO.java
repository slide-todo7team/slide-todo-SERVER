package com.slide_todo.slide_todoApp.dto.note;

import com.slide_todo.slide_todoApp.domain.note.Note;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class IndividualNoteDTO {

  private Long id;
  private String title;
  private String content;
  private String linkUrl;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private IndividualTodoInNoteDTO todo;

  public IndividualNoteDTO(Note note) {
    this.id = note.getId();
    this.title = note.getTitle();
    this.content = note.getContent();
    this.linkUrl = note.getLinkUrl();
    this.createdAt = note.getCreatedAt();
    this.updatedAt = note.getUpdatedAt();
    this.todo = new IndividualTodoInNoteDTO(note.getTodo());
  }

  @Data
  public static class IndividualTodoInNoteDTO {

    private Long id;
    private String title;
    private Boolean isDone;

    public IndividualTodoInNoteDTO(Todo todo) {
      this.id = todo.getId();
      this.title = todo.getTitle();
      this.isDone = todo.getIsDone();
    }
  }
}
