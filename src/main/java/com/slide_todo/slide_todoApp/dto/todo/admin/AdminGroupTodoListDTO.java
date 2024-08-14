package com.slide_todo.slide_todoApp.dto.todo.admin;

import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class AdminGroupTodoListDTO {

  private Long totalCount;
  private Long currentPage;
  private List<GroupTodoInListDTO> todos;

  public AdminGroupTodoListDTO(Long totalCount, Long currentPage, List<GroupTodo> todos) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.todos = todos.stream().map(GroupTodoInListDTO::new).toList();
  }



  @Data
  public static class GroupTodoInListDTO {

    private Long id;
    private String title;
    private Boolean isDone;
    private MemberInGroupTodoDTO writer;
    private MemberInGroupTodoDTO memberInCharge;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long noteId;

    public GroupTodoInListDTO(GroupTodo groupTodo) {
      this.id = groupTodo.getId();
      this.title = groupTodo.getTitle();
      this.isDone = groupTodo.getIsDone();
      if (groupTodo.getWriter() != null) {
        this.writer = new MemberInGroupTodoDTO(groupTodo.getWriter());
      } else {
        this.writer = null;
      }
      if (groupTodo.getMemberInCharge() != null) {
        this.memberInCharge = new MemberInGroupTodoDTO(groupTodo.getMemberInCharge().getMember());
      } else {
        this.memberInCharge = null;
      }
      this.createdAt = groupTodo.getCreatedAt();
      this.updatedAt = groupTodo.getUpdatedAt();
      if (groupTodo.getNote() != null) {
        this.noteId = groupTodo.getNote().getId();
      } else {
        this.noteId = null;
      }
    }
  }

  @Data
  public static class MemberInGroupTodoDTO {

    private Long id;
    private String nickname;

    public MemberInGroupTodoDTO(Member member) {
      this.id = member.getId();
      this.nickname = member.getNickname();
    }
  }
}
