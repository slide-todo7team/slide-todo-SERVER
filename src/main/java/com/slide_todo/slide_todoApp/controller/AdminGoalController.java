package com.slide_todo.slide_todoApp.controller;

import com.slide_todo.slide_todoApp.dto.goal.GoalTitleDTO;
import com.slide_todo.slide_todoApp.dto.goal.admin.GoalIdsDTO;
import com.slide_todo.slide_todoApp.dto.goal.admin.IndividualGoalAdminDTO;
import com.slide_todo.slide_todoApp.service.goal.AdminGoalService;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "어드민 목표 관리 API")
@RequestMapping("/admin/goals")
@RequiredArgsConstructor
public class AdminGoalController {

  private final JwtProvider jwtProvider;
  private final AdminGoalService adminGoalService;


  @GetMapping("/individual")
  @Operation(summary = "어드민 페이지에서 개인 목표 리스트 조회",
      description = "어드민 페이지에서 개인 목표 리스트를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "개인 목표 리스트 조회 성공"),
      @ApiResponse(responseCode = "403", description = "어드민만 가능한 요청")
  })
  public ResponseDTO<IndividualGoalAdminDTO> getIndividualGoalsList(
      HttpServletRequest request,
      @Parameter(description = "검색할 페이지 번호") @RequestParam long page,
      @Parameter(description = "한 페이지에 검색할 데이터 수") @RequestParam long limit,
      @Parameter(description = "검색할 닉네임 조건") @RequestParam(required = false) String nickname,
      @Parameter(description = "검색할 목표 제목 조건") @RequestParam(required = false) String title,
      @Parameter(description = "검색할 목표 생성일 조건(~이후) YYYY-MM-DD")
      @RequestParam(name = "created_after", required = false) String createdAfter,
      @Parameter(description = "검색할 목표 생성일 조건(~이전) YYYY-MM-DD")
      @RequestParam(name = "created_before", required = false) String createdBefore
  ) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminGoalService.getIndividualGoalsByAdmin(page, limit, nickname, title, createdAfter,
        createdBefore);
  }


  @GetMapping("/group")
  @Operation(summary = "어드민 페이지에서 그룹 목표 리스트 조회",
      description = "어드민 페이지에서 그룹 목표 리스트를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "그룹 목표 리스트 조회 성공"),
      @ApiResponse(responseCode = "403", description = "어드민만 가능한 요청")
  })
  public ResponseDTO<?> getGroupGoalsList(
      HttpServletRequest request,
      @Parameter(description = "검색할 페이지 번호") @RequestParam long page,
      @Parameter(description = "한 페이지에 검색할 데이터 수") @RequestParam long limit,
      @Parameter(description = "검색할 닉네임 조건") @RequestParam(required = false) String nickname,
      @Parameter(description = "검색할 그룹 이름 조건")
      @RequestParam(name = "group_name", required = false) String groupName,
      @Parameter(description = "검색할 목표 제목 조건") @RequestParam(required = false) String title,
      @Parameter(description = "검색할 목표 생성일 조건(~이후) YYYY-MM-DD")
      @RequestParam(name = "created_after", required = false) String createdAfter,
      @Parameter(description = "검색할 목표 생성일 조건(~이전) YYYY-MM-DD")
      @RequestParam(name = "created_before", required = false) String createdBefore
  ) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminGoalService.getGroupGoalsByAdmin(page, limit, nickname, groupName, title,
        createdAfter, createdBefore);
  }


  @DeleteMapping("/individual/delete")
  @Operation(summary = "어드민 페이지에서 개인 목표 복수 삭제",
      description = "어드민 페이지에서 개인 목표를 복수 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "개인 목표 복수 삭제 성공"),
      @ApiResponse(responseCode = "403", description = "어드민만 가능한 요청")
  })
  public ResponseDTO<?> deleteIndividualGoals(
      HttpServletRequest request, @RequestBody GoalIdsDTO goalIds
  ) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminGoalService.deleteIndividualGoalsByAdmin(goalIds);
  }


  @DeleteMapping("/group/delete")
  @Operation(summary = "어드민 페이지에서 그룹 목표 복수 삭제",
      description = "어드민 페이지에서 그룹 목표를 복수 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "그룹 목표 복수 삭제 성공"),
      @ApiResponse(responseCode = "403", description = "어드민만 가능한 요청")
  })
  public ResponseDTO<?> deleteGroupGoals(
      HttpServletRequest request, @RequestBody GoalIdsDTO goalIds
  ) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminGoalService.deleteGroupGoalsByAdmin(goalIds);
  }


  @GetMapping("/detail/{goal_id}")
  @Operation(summary = "어드민 페이지에서 목표 상세정보 조회",
      description = "어드민 페이지에서 목표 상세정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "목표 상세정보 조회 성공"),
      @ApiResponse(responseCode = "403", description = "어드민만 가능한 요청")
  })
  public ResponseDTO<?> getGoalDetail(
      HttpServletRequest request,
      @Parameter(description = "조회할 목표 ID") @PathVariable(name = "goal_id") Long goalId
  ) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminGoalService.getGoalDetail(goalId);
  }


  @PatchMapping("/update/{goal_id}")
  @Operation(summary = "어드민 페이지에서 목표 이름 수정",
      description = "어드민 페이지에서 목표 이름을 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "목표 이름 수정 성공"),
      @ApiResponse(responseCode = "403", description = "어드민만 가능한 요청")
  })
  public ResponseDTO<?> updateGoalTitle(
      HttpServletRequest request,
      @Parameter(description = "수정할 목표 ID") @PathVariable(name = "goal_id") Long goalId,
      @RequestBody GoalTitleDTO goalTitle
  ) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminGoalService.updateGoalTitle(goalId, goalTitle);
  }
}
