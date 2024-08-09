package com.slide_todo.slide_todoApp.dto.goal.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
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
  @JsonProperty("progress_rate")
  private BigDecimal progressRate;
  @JsonProperty("created_at")
  private LocalDateTime createdAt;
  @JsonProperty("updated_at")
  private LocalDateTime updatedAt;
  @JsonProperty("member")
  private MemberInGoalDTO member;
  @JsonProperty("individual_todos")
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
    @JsonProperty("is_done")
    private Boolean isDone;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
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
