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

  public GroupTodoDTO(GroupTodo groupTodo, Goal goal) {
    this.id = groupTodo.getId();
    this.title = groupTodo.getTitle();
    this.isDone = groupTodo.getIsDone();
    this.doneGroupMemberId = groupTodo.getGroupMember().getId();
    this.linkUrl = groupTodo.getLinkUrl();
    this.createdAt = groupTodo.getCreatedAt().toString();
    this.updatedAt = groupTodo.getUpdatedAt().toString();
    this.noteId = groupTodo.getNote().getId();
    this.goal = new GoalInTodoDTO(goal);
  }
}
