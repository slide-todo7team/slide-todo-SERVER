package com.slide_todo.slide_todoApp.dto.goal.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import lombok.Data;

@Data
public class GoalIdsDTO {

  private List<Long> goalIds;

  @JsonCreator
  public GoalIdsDTO(List<Long> goalIds) {
    this.goalIds = goalIds;
  }
}
