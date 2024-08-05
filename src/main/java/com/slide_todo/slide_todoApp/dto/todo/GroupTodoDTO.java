package com.slide_todo.slide_todoApp.dto.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import lombok.Data;

@Data
public class GroupTodoDTO {

  private Long id;
  private String title;
  @JsonProperty("is_done")
  private Boolean isDone;
  @JsonProperty("done_group_member_id")
  private Long doneGroupMemberId;
  @JsonProperty("link_url")
  private String linkUrl;
  @JsonProperty("created_at")
  private String createdAt;
  @JsonProperty("updated_at")
  private String updatedAt;
  @JsonProperty("note_id")
  private Long noteId;
  private GoalInTodoDTO goal;

  public GroupTodoDTO(GroupTodo todo, Goal goal) {
    this.id = todo.getId();
    this.title = todo.getTitle();
    this.isDone = todo.getIsDone();
    if (todo.getGroupMember() != null) {
      this.doneGroupMemberId = todo.getGroupMember().getId();
    } else {
      this.doneGroupMemberId = null;
    }
    this.linkUrl = todo.getLinkUrl();
    this.createdAt = todo.getCreatedAt().toString();
    this.updatedAt = todo.getUpdatedAt().toString();
    if (todo.getNote() != null) {
      this.noteId = todo.getNote().getId();
    } else {
      this.noteId = null;
    }
    this.goal = new GoalInTodoDTO(goal);
  }
}
