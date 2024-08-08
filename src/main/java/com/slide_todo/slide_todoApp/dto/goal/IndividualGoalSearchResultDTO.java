package com.slide_todo.slide_todoApp.dto.goal;

import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import java.util.List;
import lombok.Data;

@Data
public class IndividualGoalSearchResultDTO {

  private List<IndividualGoal> goals;
  private long totalCount;

  public IndividualGoalSearchResultDTO(List<IndividualGoal> goals, long totalCount) {
    this.goals = goals;
    this.totalCount = totalCount;
  }
}
