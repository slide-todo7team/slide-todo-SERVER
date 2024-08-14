package com.slide_todo.slide_todoApp.dto.goal.admin;

import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class IndividualGoalDetailDTO {

  private Long id;
  private String title;
  private BigDecimal progressRate;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private MemberInGoalDTO member;
  private List<IndividualTodoInGoalDTO> individualTodos;

  public IndividualGoalDetailDTO(IndividualGoal goal) {
    this.id = goal.getId();
    this.title = goal.getTitle();
    this.progressRate = goal.getProgressRate();
    this.createdAt = goal.getCreatedAt();
    this.updatedAt = goal.getUpdatedAt();
    this.member = new MemberInGoalDTO(goal.getMember());
    this.individualTodos = goal.getTodos().stream()
        .map(o -> new IndividualTodoInGoalDTO((IndividualTodo) o)).toList();
  }


  @Data
  public static class MemberInGoalDTO {

    private Long id;
    private String nickname;

    public MemberInGoalDTO(Member member) {
      this.id = member.getId();
      this.nickname = member.getNickname();
    }
  }


  @Data
  public static class IndividualTodoInGoalDTO {

    private Long id;
    private String title;
    private Boolean isDone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public IndividualTodoInGoalDTO(IndividualTodo individualTodo) {
      this.id = individualTodo.getId();
      this.title = individualTodo.getTitle();
      this.isDone = individualTodo.getIsDone();
      this.createdAt = individualTodo.getCreatedAt();
      this.updatedAt = individualTodo.getUpdatedAt();
    }
  }
}
