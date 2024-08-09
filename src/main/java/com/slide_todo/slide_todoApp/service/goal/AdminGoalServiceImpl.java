package com.slide_todo.slide_todoApp.service.goal;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.dto.goal.GoalTitleDTO;
import com.slide_todo.slide_todoApp.dto.goal.GroupGoalSearchResultDTO;
import com.slide_todo.slide_todoApp.dto.goal.IndividualGoalSearchResultDTO;
import com.slide_todo.slide_todoApp.dto.goal.admin.GoalIdsDTO;
import com.slide_todo.slide_todoApp.dto.goal.admin.GroupGoalAdminDTO;
import com.slide_todo.slide_todoApp.dto.goal.admin.GroupGoalDetailDTO;
import com.slide_todo.slide_todoApp.dto.goal.admin.IndividualGoalAdminDTO;
import com.slide_todo.slide_todoApp.dto.goal.admin.IndividualGoalDetailDTO;
import com.slide_todo.slide_todoApp.repository.goal.GoalRepository;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminGoalServiceImpl implements AdminGoalService {

  private final GoalRepository goalRepository;

  /**
   * 개인의 목표 리스트 조회
   * @param page
   * @param limit
   * @param nickname
   * @param title
   * @param createdAfter
   * @param createdBefore
   * @return
   */
  @Override
  public ResponseDTO<IndividualGoalAdminDTO> findIndividualAdminGoalsByAdmin(long page, long limit,
      String nickname, String title, String createdAfter, String createdBefore) {

    IndividualGoalAdminDTO result = searchIndividualGoals(page, limit, nickname, title,
        createdAfter, createdBefore);

    return new ResponseDTO<>(result, Responses.OK);
  }

  /**
   * 그룹의 목표 리스트 조회
   * @param page
   * @param limit
   * @param groupName
   * @param title
   * @param createdAfter
   * @param createdBefore
   * @return
   */
  @Override
  public ResponseDTO<GroupGoalAdminDTO> findGroupAdminGoalsByAdmin(long page, long limit,
      String groupName, String title, String createdAfter, String createdBefore) {

    GroupGoalAdminDTO result = searchGroupGoals(page, limit, groupName, title,
        createdAfter, createdBefore);

    return new ResponseDTO<>(result, Responses.OK);
  }

  /**
   * 복수 개인 목표 삭제
   * @param page
   * @param limit
   * @param nickname
   * @param title
   * @param createdAfter
   * @param createdBefore
   * @param goalIds
   * @return
   */
  @Override
  public ResponseDTO<IndividualGoalAdminDTO> deleteIndividualGoalsByAdmin(long page, long limit,
      String nickname, String title, String createdAfter, String createdBefore,
      GoalIdsDTO goalIds) {

    List<Long> ids = goalIds.getGoalIds();

    List<IndividualGoal> goalsToDelete = goalRepository.findIndividualGoalsToDelete(ids);
    for (IndividualGoal g : goalsToDelete) {
      g.deleteGoal();
    }

    IndividualGoalAdminDTO result = searchIndividualGoals(page, limit, nickname, title,
        createdAfter, createdBefore);

    return new ResponseDTO<>(result, Responses.OK);
  }

  /**
   * 복수 그룹 목표 삭제
   * @param page
   * @param limit
   * @param groupName
   * @param title
   * @param createdAfter
   * @param createdBefore
   * @param goalIds
   * @return
   */
  @Override
  public ResponseDTO<GroupGoalAdminDTO> deleteGroupGoalsByAdmin(long page, long limit,
      String groupName, String title, String createdAfter, String createdBefore,
      GoalIdsDTO goalIds) {

    List<Long> ids = goalIds.getGoalIds();

    List<GroupGoal> goalsToDelete = goalRepository.findGroupGoalsToDelete(ids);
    for (GroupGoal g : goalsToDelete) {
      g.deleteGoal();
    }

    GroupGoalAdminDTO result = searchGroupGoals(page, limit, groupName, title,
        createdAfter, createdBefore);

    return new ResponseDTO<>(result, Responses.OK);
  }

  /**
   * 목표 상세정보 조회
   * @param goalId
   * @return
   */
  @Override
  public ResponseDTO<?> getGoalDetail(Long goalId) {
    Goal goal = goalRepository.findByGoalId(goalId);

    if (goal.getDtype().equals("G")) {
      GroupGoal groupGoal = goalRepository.findGroupGoalDetail(goalId);
      return new ResponseDTO<>(
          new GroupGoalDetailDTO(groupGoal), Responses.OK
      );
    }
    IndividualGoal individualGoal = goalRepository.findIndividualGoalDetail(goalId);
    return new ResponseDTO<>(
        new IndividualGoalDetailDTO(individualGoal), Responses.OK
    );
  }

  /**
   * 목표 이름 수정
   * @param goalId
   * @param request
   * @return
   */
  @Override
  public ResponseDTO<?> updateGoalTitle(Long goalId, GoalTitleDTO request) {
    Goal goal = goalRepository.findByGoalId(goalId);
    goal.updateTitle(request.getTitle());

    if (goal.getDtype().equals("G")) {
      GroupGoal groupGoal = goalRepository.findGroupGoalDetail(goalId);
      return new ResponseDTO<>(
          new GroupGoalDetailDTO(groupGoal), Responses.OK
      );
    }
    IndividualGoal individualGoal = goalRepository.findIndividualGoalDetail(goalId);
    return new ResponseDTO<>(
        new IndividualGoalDetailDTO(individualGoal), Responses.OK
    );
  }

  /*개인 목표 리스트 조회*/
  private IndividualGoalAdminDTO searchIndividualGoals(long page, long limit,
      String nickname, String title, String createdAfter, String createdBefore) {
    long start;
    if (limit != 0) {
      start = (page - 1) * limit;
    } else {
      start = 0L;
      limit = goalRepository.count();
    }
    LocalDateTime parsedCreatedAfter;
    LocalDateTime parsedCreatedBefore;
    if (createdAfter != null) {
      parsedCreatedAfter = LocalDateTime.parse(createdAfter);
    } else {
      parsedCreatedAfter = null;
    }

    if (createdBefore != null) {
      parsedCreatedBefore = LocalDateTime.parse(createdBefore);
    } else {
      parsedCreatedBefore = null;
    }

    IndividualGoalSearchResultDTO searchResult = goalRepository.findIndividualGoalByAdmin(
        nickname, title, parsedCreatedAfter, parsedCreatedBefore, start, limit
    );

    return new IndividualGoalAdminDTO(searchResult.getTotalCount(), page, searchResult.getGoals());
  }

  /*그룹 목표 리스트 조회*/
  private GroupGoalAdminDTO searchGroupGoals(long page, long limit,
      String groupName, String title, String createdAfter, String createdBefore) {
    long start;
    if (limit != 0) {
      start = (page - 1) * limit;
    } else {
      start = 0L;
      limit = goalRepository.count();
    }
    LocalDateTime parsedCreatedAfter;
    LocalDateTime parsedCreatedBefore;
    if (createdAfter != null) {
      parsedCreatedAfter = LocalDateTime.parse(createdAfter);
    } else {
      parsedCreatedAfter = null;
    }

    if (createdBefore != null) {
      parsedCreatedBefore = LocalDateTime.parse(createdBefore);
    } else {
      parsedCreatedBefore = null;
    }

    GroupGoalSearchResultDTO searchResult = goalRepository.findGroupGoalByAdmin(
        groupName, title, parsedCreatedAfter, parsedCreatedBefore, start, limit
    );

    return new GroupGoalAdminDTO(searchResult.getTotalCount(), page, searchResult.getGoals());
  }
}
