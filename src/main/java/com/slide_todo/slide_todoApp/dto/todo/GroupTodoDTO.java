package com.slide_todo.slide_todoApp.dto.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.dto.group.GroupMemberDTO;
import lombok.Data;

@Data
public class GroupTodoDTO {

  private Long id;
  private String title;
  @JsonProperty("is_done")
  private Boolean isDone;
  @JsonProperty("content")
  private String content;
  @JsonProperty("created_at")
  private String createdAt;
  @JsonProperty("updated_at")
  private String updatedAt;
  @JsonProperty("note_id")
  private Long noteId;
  private GoalInTodoDTO goal;
  @JsonProperty("member_in_charge")
  private GroupMemberDTO memberInCharge;

  public GroupTodoDTO(GroupTodo todo, Goal goal) {
    this.id = todo.getId();
    this.title = todo.getTitle();
    this.isDone = todo.getIsDone();
    if (todo.getMemberInCharge() != null) {
      this.memberInCharge = new GroupMemberDTO(todo.getMemberInCharge());
    } else {
      this.memberInCharge = null;
    }
    this.content = todo.getContent();
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
