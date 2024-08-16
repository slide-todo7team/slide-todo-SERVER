package com.slide_todo.slide_todoApp.dto.note;

import com.slide_todo.slide_todoApp.domain.note.Note;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.dto.group.GroupMemberDTO;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class GroupNoteDTO {

  private Long id;
  private String title;
  private String content;
  private String linkUrl;
  private LocalDateTime createdAt;
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
    private GroupMemberDTO memberInCharge;
    private String title;
    private Boolean isDone;
    private GoalInGroupTodoDTO goal;

    public GroupTodoInNoteDTO(GroupTodo todo) {
      this.id = todo.getId();
      if (todo.getMemberInCharge() == null) {
        this.memberInCharge = null;
      } else {
        this.memberInCharge = new GroupMemberDTO(todo.getMemberInCharge());
      }
      this.title = todo.getTitle();
      this.isDone = todo.getIsDone();
      this.goal = new GoalInGroupTodoDTO(todo);
    }
  }


  @Data
  public static class GoalInGroupTodoDTO {

    private Long id;
    private String title;

    public GoalInGroupTodoDTO(GroupTodo todo) {
      this.id = todo.getGoal().getId();
      this.title = todo.getGoal().getTitle();
    }
  }
}
