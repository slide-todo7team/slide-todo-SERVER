package com.slide_todo.slide_todoApp.dto.goal.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class GoalIdsDTO {

  @JsonProperty("goal_ids")
  private List<Long> goalIds;

  @JsonCreator
  public GoalIdsDTO(List<Long> goalIds) {
    this.goalIds = goalIds;
  }
}
