package com.slide_todo.slide_todoApp.dto.todo.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class IndividualTodoListDTO {

  @JsonProperty("total_count")
  private Long totalCount;
  @JsonProperty("current_page")
  private Long currentPage;
  @JsonProperty("todos")
  private List<IndividualTodoInAdminDTO> todos;

  public IndividualTodoListDTO(Long totalCount, Long currentPage, List<IndividualTodo> todos) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.todos = todos.stream().map(IndividualTodoInAdminDTO::new).toList();
  }


  @Data
  public static class IndividualTodoInAdminDTO {

    @JsonProperty("member")
    private MemberInTodoDTO memberInTodoDTO;
    private Long id;
    private String title;
    private String content;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public IndividualTodoInAdminDTO(IndividualTodo individualTodo) {
      IndividualGoal goal = (IndividualGoal) individualTodo.getGoal();
      this.memberInTodoDTO = new MemberInTodoDTO(goal.getMember());
      this.id = individualTodo.getId();
      this.title = individualTodo.getTitle();
      this.content = individualTodo.getContent();
      this.createdAt = individualTodo.getCreatedAt();
      this.updatedAt = individualTodo.getUpdatedAt();
    }
  }


  @Data
  public static class MemberInTodoDTO {

    private Long id;
    private String nickname;

    public MemberInTodoDTO(Member member) {
      this.id = member.getId();
      this.nickname = member.getNickname();
    }
  }
}
