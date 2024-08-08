package com.slide_todo.slide_todoApp.dto.todo;

import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import java.util.List;
import lombok.Data;

@Data
public class GroupTodoSearchResultDTO {

  private List<GroupTodo> groupTodos;
  private long totalCount;

  public GroupTodoSearchResultDTO(List<GroupTodo> groupTodos, long totalCount) {
    this.groupTodos = groupTodos;
    this.totalCount = totalCount;
  }
}
