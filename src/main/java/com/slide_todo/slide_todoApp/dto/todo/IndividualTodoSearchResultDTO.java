package com.slide_todo.slide_todoApp.dto.todo;

import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import java.util.List;
import lombok.Data;

@Data
public class IndividualTodoSearchResultDTO {

  private List<IndividualTodo> individualTodos;
  private long totalCount;

  public IndividualTodoSearchResultDTO(List<IndividualTodo> individualTodos, long totalCount) {
    this.individualTodos = individualTodos;
    this.totalCount = totalCount;
  }
}
