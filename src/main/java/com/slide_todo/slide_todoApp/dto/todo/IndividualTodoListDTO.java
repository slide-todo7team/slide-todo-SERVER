package com.slide_todo.slide_todoApp.dto.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class IndividualTodoListDTO {

  @JsonProperty("total_count")
  private Long totalCount;
  @JsonProperty("current_page")
  private Long currentPage;
  private List<IndividualTodoDTO> todos;

  public IndividualTodoListDTO(Long totalCount, Long currentPage, List<IndividualTodoDTO> todos) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.todos = todos;
  }
}
