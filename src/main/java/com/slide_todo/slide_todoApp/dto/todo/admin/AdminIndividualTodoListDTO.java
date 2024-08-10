package com.slide_todo.slide_todoApp.dto.todo.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 개인 목표 내의 할일 리스트 DTO
 */
@Data
public class AdminIndividualTodoListDTO {

  @JsonProperty("total_count")
  private Long totalCount;
  @JsonProperty("current_page")
  private Long currentPage;
  @JsonProperty("todos")
  private List<IndividualTodoInAdminDTO> todos;

  public AdminIndividualTodoListDTO(Long totalCount, Long currentPage, List<IndividualTodo> todos) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.todos = todos.stream().map(IndividualTodoInAdminDTO::new).toList();
  }


  @Data
  public static class IndividualTodoInAdminDTO {
    private Long id;
    private String title;
    @JsonProperty("is_done")
    private Boolean isDone;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    @JsonProperty("note_id")
    private Long noteId;

    public IndividualTodoInAdminDTO(IndividualTodo individualTodo) {
      this.id = individualTodo.getId();
      this.title = individualTodo.getTitle();
      this.isDone = individualTodo.getIsDone();
      this.createdAt = individualTodo.getCreatedAt();
      this.updatedAt = individualTodo.getUpdatedAt();
      if (individualTodo.getNote() != null) {
        this.noteId = individualTodo.getNote().getId();
      } else {
        this.noteId = null;
      }
    }
  }
}
