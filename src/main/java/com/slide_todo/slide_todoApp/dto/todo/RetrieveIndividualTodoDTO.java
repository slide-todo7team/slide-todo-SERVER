package com.slide_todo.slide_todoApp.dto.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;
import java.util.List;
import lombok.Data;

@Data
public class RetrieveIndividualTodoDTO {

  @JsonProperty("goal_ids")
  private List<Long> goalIds;
  @JsonProperty("is_done")
  private Boolean isDone;
}
