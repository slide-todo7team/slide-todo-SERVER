package com.slide_todo.slide_todoApp.dto.goal;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import java.util.List;
import lombok.Data;

@Data
public class GroupGoalSearchResultDTO {

  private List<GroupGoal> goals;
  private long searchCount;

  public GroupGoalSearchResultDTO(List<GroupGoal> goals, long searchCount) {
    this.goals = goals;
    this.searchCount = searchCount;
  }
}
