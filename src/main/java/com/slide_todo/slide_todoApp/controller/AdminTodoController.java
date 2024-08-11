package com.slide_todo.slide_todoApp.controller;

import com.slide_todo.slide_todoApp.dto.todo.admin.AdminGroupTodoListDTO;
import com.slide_todo.slide_todoApp.dto.todo.admin.AdminIndividualTodoListDTO;
import com.slide_todo.slide_todoApp.dto.todo.admin.TodoIdsDTO;
import com.slide_todo.slide_todoApp.service.todo.AdminTodoService;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "어드민 할 일 관리 API")
@RequestMapping("/admin/todo")
@RequiredArgsConstructor
public class AdminTodoController {

  private final AdminTodoService adminTodoService;
  private final JwtProvider jwtProvider;

  @GetMapping("/individual/{goal_id}")
  @Operation(summary = "개인 목표에 대한 할 일 리스트 조회",
      description = "개인 목표에 대한 할 일 리스트를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "성공")
  public ResponseDTO<AdminIndividualTodoListDTO> getIndividualTodoList(
      HttpServletRequest request,
      @Parameter(description = "검색할 개인 목표 ID") @PathVariable(name = "goal_id") Long goalId,
      @Parameter(description = "검색할 페이지 번호") @RequestParam long page,
      @Parameter(description = "한 페이지에 검색할 데이터 수") @RequestParam long limit) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminTodoService.getIndividualTodoList(goalId, page, limit);
  }


  @GetMapping("/group/{goal_id}")
  @Operation(summary = "그룹 목표에 대한 할 일 리스트 조회",
      description = "그룹 목표에 대한 할 일 리스트를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "성공")
  public ResponseDTO<AdminGroupTodoListDTO> getGroupTodoList(
      HttpServletRequest request,
      @Parameter(description = "검색할 그룹 목표 ID") @PathVariable(name = "goal_id") Long goalId,
      @Parameter(description = "검색할 페이지 번호") @RequestParam long page,
      @Parameter(description = "한 페이지에 검색할 데이터 수") @RequestParam long limit) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminTodoService.getGroupTodoList(goalId, page, limit);
  }


  @DeleteMapping("/individual/delete")
  @Operation(summary = "개인 목표 복수 삭제", description = "개인 목표를 복수 삭제합니다.")
  @ApiResponse(responseCode = "204", description = "삭제 성공")
  public ResponseDTO<?> deleteIndividualTodos(
      HttpServletRequest request,
      @Parameter(description = "삭제할 개인 목표 ID 리스트") @RequestBody TodoIdsDTO todoIds) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminTodoService.deleteIndividualTodos(todoIds);
  }


  @DeleteMapping("/group/delete")
  @Operation(summary = "그룹 목표 복수 삭제", description = "그룹 목표를 복수 삭제합니다.")
  @ApiResponse(responseCode = "204", description = "삭제 성공")
  public ResponseDTO<?> deleteGroupTodos(
      HttpServletRequest request,
      @Parameter(description = "삭제할 그룹 목표 ID 리스트") @RequestBody TodoIdsDTO todoIds) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminTodoService.deleteGroupTodos(todoIds);
  }
}
