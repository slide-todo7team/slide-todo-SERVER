package com.slide_todo.slide_todoApp.dto.goal.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class GroupGoalDetailDTO {

  private Long id;
  private String title;
  @JsonProperty("progress_rate")
  private BigDecimal progressRate;
  @JsonProperty("created_at")
  private LocalDateTime createdAt;
  @JsonProperty("updated_at")
  private LocalDateTime updatedAt;
  @JsonProperty("group")
  private GroupInGoalDTO group;
  @JsonProperty("todos")
  private List<GroupTodoInGoalDTO> groupTodos;

  public GroupGoalDetailDTO(GroupGoal goal) {
    this.id = goal.getId();
    this.title = goal.getTitle();
    this.progressRate = goal.getProgressRate();
    this.createdAt = goal.getCreatedAt();
    this.updatedAt = goal.getUpdatedAt();
    this.group = new GroupInGoalDTO(goal.getGroup());
    this.groupTodos = goal.getTodos().stream().map(o -> new GroupTodoInGoalDTO((GroupTodo) o))
        .toList();
  }


  @Data
  public static class GroupInGoalDTO {

    private Long id;
    @JsonProperty("title")
    private String title;

    public GroupInGoalDTO(Group group) {
      this.id = group.getId();
      this.title = group.getTitle();
    }
  }


  @Data
  public static class GroupTodoInGoalDTO {

    private Long id;
    private String title;
    @JsonProperty("is_done")
    private Boolean isDone;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    @JsonProperty("member_in_charge")
    private GroupMemberInTodoDTO memberInCharge;

    public GroupTodoInGoalDTO(GroupTodo groupTodo) {
      this.id = groupTodo.getId();
      this.title = groupTodo.getTitle();
      this.isDone = groupTodo.getIsDone();
      this.createdAt = groupTodo.getCreatedAt();
      this.updatedAt = groupTodo.getUpdatedAt();
      if (groupTodo.getMemberInCharge() != null) {
        this.memberInCharge = new GroupMemberInTodoDTO(groupTodo.getMemberInCharge());
      } else {
        this.memberInCharge = null;
      }
    }
  }


  @Data
  public static class GroupMemberInTodoDTO {

    private Long id;
    private String nickname;
    private String color;

    public GroupMemberInTodoDTO(GroupMember groupMember) {
      this.id = groupMember.getMember().getId();
      this.nickname = groupMember.getMember().getNickname();
      this.color = groupMember.getColor().getHexCode();
    }
  }
}
