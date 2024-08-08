package com.slide_todo.slide_todoApp.repository.goal;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.dto.goal.GroupGoalSearchResultDTO;
import com.slide_todo.slide_todoApp.dto.goal.IndividualGoalSearchResultDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface BaseGoalRepository {

  /*어드민 페이지에서 개인 목표 리스트 조회*/
  IndividualGoalSearchResultDTO findIndividualGoalByAdmin(
      String nickname, String title, LocalDateTime createdAfter, LocalDateTime createdBefore,
      long start, long limit
  );


  /*어드민 페이지에서 그룹 목표 리스트 조회*/
  GroupGoalSearchResultDTO findGroupGoalByAdmin(
      String groupName, String title, LocalDateTime createdAfter, LocalDateTime createdBefore,
      long start, long limit
  );

  /*삭제할 개인 목표 리스트 조회*/
  List<IndividualGoal> findIndividualGoalsToDelete(List<Long> ids);

  /*삭제할 그룹 목표 리스트 조회*/
  List<GroupGoal> findGroupGoalsToDelete(List<Long> ids);
}
