package com.slide_todo.slide_todoApp.dto.note;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.note.Note;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class GroupNoteDTO {

  private Long id;
  private String title;
  private String content;
  @JsonProperty("link_url")
  private String linkUrl;
  @JsonProperty("created_at")
  private LocalDateTime createdAt;
  @JsonProperty("updated_at")
  private LocalDateTime updatedAt;
  private GroupTodoInNoteDTO todo;

  public GroupNoteDTO(Note note) {
    this.id = note.getId();
    this.title = note.getTitle();
    this.content = note.getContent();
    this.linkUrl = note.getLinkUrl();
    this.createdAt = note.getCreatedAt();
    this.updatedAt = note.getUpdatedAt();
    this.todo = new GroupTodoInNoteDTO((GroupTodo) note.getTodo());
  }


  @Data
  public static class GroupTodoInNoteDTO {

    private Long id;
    @JsonProperty("charged_group_member_id")
    private Long chargedGroupMemberId;
    private String title;
    private String content;
    @JsonProperty("is_done")
    private Boolean isDone;

    public GroupTodoInNoteDTO(GroupTodo todo) {
      this.id = todo.getId();
      if (todo.getMemberInCharge() == null) {
        this.chargedGroupMemberId = null;
      } else {
        this.chargedGroupMemberId = todo.getMemberInCharge().getId();
      }
      this.title = todo.getTitle();
      this.content = todo.getContent();
      this.isDone = todo.getIsDone();
    }
  }
}
