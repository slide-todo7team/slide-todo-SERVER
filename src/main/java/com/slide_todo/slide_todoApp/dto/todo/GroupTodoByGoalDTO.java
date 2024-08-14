package com.slide_todo.slide_todoApp.dto.todo;

import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class GroupTodoByGoalDTO {

  private Long id;
  private String title;
  private Boolean isDone;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long noteId;
  private MemberInChargeDTO memberInCharge;

  public GroupTodoByGoalDTO(GroupTodo todo) {
    this.id = todo.getId();
    this.title = todo.getTitle();
    this.isDone = todo.getIsDone();
    this.createdAt = todo.getCreatedAt();
    this.updatedAt = todo.getUpdatedAt();
    if (todo.getNote() != null) {
      this.noteId = todo.getNote().getId();
    } else {
      this.noteId = null;
    }
    if (todo.getMemberInCharge() != null) {
      this.memberInCharge = new MemberInChargeDTO(todo.getMemberInCharge());
    } else {
      this.memberInCharge = null;
    }
  }

  @Data
  public static class MemberInChargeDTO {

    private Long id;
    private String nickname;
    private String color;

    public MemberInChargeDTO(GroupMember member) {
      this.id = member.getMember().getId();
      this.nickname = member.getMember().getNickname();
      this.color = member.getColor().getHexCode();
    }
  }
}
