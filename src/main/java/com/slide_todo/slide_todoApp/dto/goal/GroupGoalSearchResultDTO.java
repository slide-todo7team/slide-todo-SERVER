package com.slide_todo.slide_todoApp.dto.goal;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import java.util.List;
import lombok.Data;

@Data
public class GroupGoalSearchResultDTO {

  private List<GroupGoal> goals;
  private long totalCount;

  public GroupGoalSearchResultDTO(List<GroupGoal> goals, long totalCount) {
    this.goals = goals;
    this.totalCount = totalCount;
  }
}
