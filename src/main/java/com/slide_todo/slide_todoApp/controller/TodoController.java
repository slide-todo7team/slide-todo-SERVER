package com.slide_todo.slide_todoApp.controller;

import com.slide_todo.slide_todoApp.dto.todo.GroupTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.IndividualTodoListDTO;
import com.slide_todo.slide_todoApp.dto.todo.TodoCreateDTO;
import com.slide_todo.slide_todoApp.dto.todo.TodoUpdateDTO;
import com.slide_todo.slide_todoApp.service.todo.TodoService;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "할 일(Todo) API")
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

  private final TodoService todoService;
  private final JwtProvider jwtProvider;

  @PostMapping("/create")
  @Operation(summary = "할 일 생성", description = "입력된 목표에 따라 자동으로 개인/그룹 할 일이 생성됩니다.<br>"
      + "자세한 명세는 노션을 확인해주세요.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "할 일 생성 성공"),
      @ApiResponse(responseCode = "400", description = "할 일 생성 실패")
  })
  public ResponseDTO<?> createTodo(
      HttpServletRequest request,
      @RequestBody TodoCreateDTO todoCreateDTO
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    return todoService.createTodo(memberId, todoCreateDTO);
  }


  @PatchMapping("/update/{todoId}")
  @Operation(summary = "할 일 수정", description = "할 일의 내용을 수정합니다.<br>"
      + "자세한 명세는 노션을 확인해주세요.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "할 일 수정 성공"),
      @ApiResponse(responseCode = "400", description = "할 일 수정 실패")
  })
  public ResponseDTO<?> updateTodo(
      HttpServletRequest request,
      @Parameter(description = "수정할 할 일의 ID") @PathVariable Long todoId,
      @RequestBody TodoUpdateDTO todoUpdateDTO
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    return todoService.updateTodo(memberId, todoId, todoUpdateDTO);
  }


  @PatchMapping("/done/{todoId}")
  @Operation(summary = "할 일 완료 여부 변경", description = "할 일의 완료 여부를 변경합니다.<br>"
      + "자세한 명세는 노션을 확인해주세요.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "할 일 완료 여부 변경 성공"),
      @ApiResponse(responseCode = "400", description = "할 일 완료 여부 변경 실패")
  })
  public ResponseDTO<?> changeTodoDone(
      HttpServletRequest request,
      @Parameter(description = "완료 여부를 변경할 할 일의 ID") @PathVariable Long todoId
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    return todoService.changeTodoDone(memberId, todoId);
  }


  @DeleteMapping("/delete/{todoId}")
  @Operation(summary = "할 일 삭제", description = "할 일을 삭제합니다.<br>"
      + "자세한 명세는 노션을 확인해주세요.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "할 일 삭제 성공"),
      @ApiResponse(responseCode = "400", description = "할 일 삭제 실패")
  })
  public ResponseDTO<?> deleteTodo(
      HttpServletRequest request,
      @Parameter(description = "삭제할 할 일의 ID") @PathVariable Long todoId
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    return todoService.deleteTodo(memberId, todoId);
  }


  @GetMapping("/individual/all")
  @Operation(summary = "개인의 모든 할 일 조회", description = "개인의 모든 할 일을 조회합니다.<br>"
      + "자세한 명세는 노션을 확인해주세요.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "개인의 모든 할 일 조회 성공"),
      @ApiResponse(responseCode = "400", description = "개인의 모든 할 일 조회 실패")
  })
  public ResponseDTO<IndividualTodoListDTO> getIndividualTodoList(
      HttpServletRequest request,
      @Parameter(description = "검색할 페이지 번호") @RequestParam long page,
      @Parameter(description = "한 페이지에 검색할 데이터 수") @RequestParam long limit,
      @Parameter(description = "조회할 목표의 ID 리스트")
      @RequestParam(required = false) List<Long> goalIds,
      @Parameter(description = "완료 여부")
      @RequestParam(required = false) Boolean isDone
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    return todoService.getIndividualTodoList(memberId, page, limit, goalIds, isDone);
  }


  @PatchMapping("/group/charge/{todoId}")
  @Operation(summary = "그룹 할 일의 담당자 변경", description = "개인 할 일의 담당자를 변경합니다.<br>"
      + "자세한 명세는 노션을 확인해주세요.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "할 일의 담당자 변경 성공"),
      @ApiResponse(responseCode = "400", description = "담당자 변경 실패"),
      @ApiResponse(responseCode = "401", description = "담당자 변경 권한 없음")
  })
  public ResponseDTO<GroupTodoDTO> chargeGroupTodo(
      HttpServletRequest request,
      @Parameter(description = "담당자를 변경할 할 일의 ID") @PathVariable Long todoId
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    return todoService.updateChargingGroupMember(memberId, todoId);
  }

  @GetMapping("/list")
  @Operation(summary = "특정 목표에 따른 할 일 조회", description = "특정 목표에 따른 할 일 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo 조회 성공"),
      @ApiResponse(responseCode = "400", description = "Todo 조회 실패")
  })
  public ResponseDTO<?> getTodosByGoal(
      HttpServletRequest request,
      @Parameter(description = "검색할 페이지 번호") @RequestParam Long goalId
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    return todoService.getTodoListByGoal(memberId, goalId);
  }
}
