package com.slide_todo.slide_todoApp.dto.todo;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.dto.group.GroupMemberDTO;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class GroupTodoDTO {

  private Long id;
  private String title;
  private Boolean isDone;
  private String createdAt;
  private String updatedAt;
  private BigDecimal progressRate;
  private Long noteId;
  private GoalInTodoDTO goal;
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
    this.createdAt = todo.getCreatedAt().toString();
    this.updatedAt = todo.getUpdatedAt().toString();
    this.progressRate = goal.getProgressRate();
    if (todo.getNote() != null) {
      this.noteId = todo.getNote().getId();
    } else {
      this.noteId = null;
    }
    this.goal = new GoalInTodoDTO(goal);
  }
}
