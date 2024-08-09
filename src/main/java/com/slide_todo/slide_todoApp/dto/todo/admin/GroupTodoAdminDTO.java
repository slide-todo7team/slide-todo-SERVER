package com.slide_todo.slide_todoApp.dto.todo.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class GroupTodoAdminDTO {

  @JsonProperty("total_count")
  private Long totalCount;
  @JsonProperty("current_page")
  private Long currentPage;
  @JsonProperty("todos")
  private List<GroupTodoInListDTO> todos;

  public GroupTodoAdminDTO(Long totalCount, Long currentPage, List<GroupTodo> todos) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.todos = todos.stream().map(GroupTodoInListDTO::new).toList();
  }



  @Data
  public static class GroupTodoInListDTO {

    @JsonProperty("group")
    private GroupInTodoDTO groupInTodoDTO;
    private Long id;
    private String title;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public GroupTodoInListDTO(GroupTodo groupTodo) {
      GroupGoal goal = (GroupGoal) groupTodo.getGoal();
      this.groupInTodoDTO = new GroupInTodoDTO(goal.getGroup());
      this.id = groupTodo.getId();
      this.title = groupTodo.getTitle();
      this.createdAt = groupTodo.getCreatedAt();
      this.updatedAt = groupTodo.getUpdatedAt();
    }
  }


  @Data
  public static class GroupInTodoDTO {

    private Long id;
    private String title;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public GroupInTodoDTO(Group group) {
      this.id = group.getId();
      this.title = group.getTitle();
      this.createdAt = group.getCreatedAt();
      this.updatedAt = group.getUpdatedAt();
    }
  }
}
