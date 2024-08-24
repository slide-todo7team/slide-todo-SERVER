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
import java.time.LocalDate;
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
  public ResponseDTO<IndividualGoalAdminDTO> getIndividualGoalsByAdmin(long page, long limit,
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
  public ResponseDTO<GroupGoalAdminDTO> getGroupGoalsByAdmin(long page, long limit,
      String nickname, String groupName, String title, String createdAfter, String createdBefore) {

    GroupGoalAdminDTO result = searchGroupGoals(page, limit, nickname, groupName, title,
        createdAfter, createdBefore);

    return new ResponseDTO<>(result, Responses.OK);
  }

  /**
   * 복수 개인 목표 삭제
   * @param goalIds
   * @return
   */
  @Override
  @Transactional
  public ResponseDTO<?> deleteIndividualGoalsByAdmin(GoalIdsDTO goalIds) {

    List<Long> ids = goalIds.getGoalIds();

    List<IndividualGoal> goalsToDelete = goalRepository.findIndividualGoalsToDelete(ids);
    for (IndividualGoal g : goalsToDelete) {
      g.deleteGoal();
    }

    return new ResponseDTO<>(null, Responses.NO_CONTENT);
  }

  /**
   * 복수 그룹 목표 삭제
   * @param goalIds
   * @return
   */
  @Override
  @Transactional
  public ResponseDTO<?> deleteGroupGoalsByAdmin(GoalIdsDTO goalIds) {

    List<Long> ids = goalIds.getGoalIds();

    List<GroupGoal> goalsToDelete = goalRepository.findGroupGoalsToDelete(ids);
    for (GroupGoal g : goalsToDelete) {
      g.deleteGoal();
    }

    return new ResponseDTO<>(null, Responses.NO_CONTENT);
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
  @Transactional
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
      parsedCreatedAfter = LocalDate.parse(createdAfter.replace(" ", ""))
          .atStartOfDay();
    } else {
      parsedCreatedAfter = null;
    }

    if (createdBefore != null) {
      parsedCreatedBefore = LocalDate.parse(createdBefore.replace(" ", ""))
          .atStartOfDay().plusDays(1);
    } else {
      parsedCreatedBefore = null;
    }

    IndividualGoalSearchResultDTO searchResult = goalRepository.findIndividualGoalByAdmin(
        nickname, title, parsedCreatedAfter, parsedCreatedBefore, start, limit
    );

    long totalCount = goalRepository.countIndividualGoal();

    return new IndividualGoalAdminDTO(totalCount, searchResult.getSearchedCount(), page, searchResult.getGoals());
  }

  /*그룹 목표 리스트 조회*/
  private GroupGoalAdminDTO searchGroupGoals(long page, long limit,
      String nickname, String groupName, String title, String createdAfter, String createdBefore) {
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
      parsedCreatedAfter = LocalDate.parse(createdAfter.replace(" ", ""))
          .atStartOfDay();
    } else {
      parsedCreatedAfter = null;
    }

    if (createdBefore != null) {
      parsedCreatedBefore = LocalDate.parse(createdBefore.replace(" ", ""))
          .atStartOfDay().plusDays(1);
    } else {
      parsedCreatedBefore = null;
    }

    GroupGoalSearchResultDTO searchResult = goalRepository.findGroupGoalByAdmin(
        nickname, groupName, title, parsedCreatedAfter, parsedCreatedBefore, start, limit
    );

    long totalCount = goalRepository.countGroupGoal();

    return new GroupGoalAdminDTO(totalCount, searchResult.getSearchCount(), page, searchResult.getGoals());
  }
}
