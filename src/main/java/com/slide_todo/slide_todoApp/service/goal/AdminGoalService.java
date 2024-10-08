package com.slide_todo.slide_todoApp.service.goal;

import com.slide_todo.slide_todoApp.dto.goal.GoalTitleDTO;
import com.slide_todo.slide_todoApp.dto.goal.admin.GoalIdsDTO;
import com.slide_todo.slide_todoApp.dto.goal.admin.GroupGoalAdminDTO;
import com.slide_todo.slide_todoApp.dto.goal.admin.IndividualGoalAdminDTO;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;

public interface AdminGoalService {

  /*어드민 페이지에서 개인 목표 리스트 조회*/
  ResponseDTO<IndividualGoalAdminDTO> getIndividualGoalsByAdmin(long page, long limit,
      Long memberId, String nickname, String title, String createdAfter, String createdBefore);

  /*어드민 페이지에서 그룹 목표 리스트 조회*/
  ResponseDTO<GroupGoalAdminDTO> getGroupGoalsByAdmin(Long groupId, long page, long limit, String groupName,
      String nickname, String title, String createdAfter, String createdBefore);

  /*개인 목표 복수 삭제*/
  ResponseDTO<?> deleteIndividualGoalsByAdmin(GoalIdsDTO goalIds);

  /*그룹 목표 복수 삭제*/
  ResponseDTO<?> deleteGroupGoalsByAdmin(GoalIdsDTO goalIds);

  /*목표 상세정보 조회*/
  ResponseDTO<?> getGoalDetail(Long goalId);

  /*목표 이름 수정*/
  ResponseDTO<?> updateGoalTitle(Long goalId, GoalTitleDTO request);
}
