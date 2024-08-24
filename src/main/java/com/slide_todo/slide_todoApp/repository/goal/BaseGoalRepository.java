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
      String nickname, String groupName, String title, LocalDateTime createdAfter,
      LocalDateTime createdBefore, long start, long limit
  );

  /*삭제할 개인 목표 리스트 조회*/
  List<IndividualGoal> findIndividualGoalsToDelete(List<Long> ids);

  /*삭제할 그룹 목표 리스트 조회*/
  List<GroupGoal> findGroupGoalsToDelete(List<Long> ids);

  /*개인 목표 상세정보 조회*/
  IndividualGoal findIndividualGoalDetail(Long goalId);

  /*그룹 목표 상세정보 조회*/
  GroupGoal findGroupGoalDetail(Long goalId);

  /*개인 목표 개수 조회*/
  long countIndividualGoal();

  /*그룹 목표 개수 조회*/
  long countGroupGoal();
}
