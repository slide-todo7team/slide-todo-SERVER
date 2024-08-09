package com.slide_todo.slide_todoApp.controller;

import com.slide_todo.slide_todoApp.dto.goal.*;
import com.slide_todo.slide_todoApp.service.goal.IndividualGoalService;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/individual/goals")
@Tag(name = "개인 목표 API")
@RequiredArgsConstructor
public class IndividualGoalController {
    private final JwtProvider jwtProvider;
    private final IndividualGoalService individualGoalService;

    @PostMapping("")
    @Operation(summary = "개인 목표 생성")
    public ResponseDTO<IndividualGoalDTO> createIndividualGoal(@RequestBody GoalTitleDTO goalTitleDTO, HttpServletRequest request){
        Long memberId = jwtProvider.getMemberId(request);
        String title = goalTitleDTO.getTitle();
        return individualGoalService.createIndividualGoal(title,memberId);
    }

    @GetMapping("")
    @Operation(summary = "개인 목표 리스트 조회")
    public ResponseDTO<?> getIndividualGoals(HttpServletRequest request){
        Long memberId = jwtProvider.getMemberId(request);
        return individualGoalService.getIndividualGoals(memberId);
    }

    @GetMapping("/todos")
    @Operation(summary = "개인 목표 & 할일 리스트 조회")
    public ResponseDTO<GoalTodosResponseDTO<IndividualGoalTodoDTO>> getIndividualGoalTodos(HttpServletRequest request,
                                                                                           @RequestParam(defaultValue =  "0") int page, @RequestParam Integer limit) {
        Long memberId = jwtProvider.getMemberId(request);
        return individualGoalService.getIndividualGoalTodos(memberId,page,limit);
    }

    @PatchMapping("/{goalId}")
    @Operation(summary = "개인 목표 수정")
    public ResponseDTO<IndividualGoalDTO> updateIndividualGoal(@PathVariable Long goalId, @RequestBody GoalTitleDTO goalTitleDTO) {
        String title = goalTitleDTO.getTitle();
        return individualGoalService.updateIndividualGoal(goalId,title);
    }

    @DeleteMapping("/{goalId}")
    @Operation(summary = "개인 목표 삭제")
    public ResponseDTO<?> deleteIndividualGoal(@PathVariable Long goalId) {
        return individualGoalService.deleteIndividualGoal(goalId);
    }

    @GetMapping("/progress")
    @Operation(summary = "개인 할 일 진행률 조회")
    public ResponseDTO<IndividualProgressDTO> getIndividualProgress(HttpServletRequest request) {
        Long memberId = jwtProvider.getMemberId(request);
        return individualGoalService.getIndividualProgress(memberId);
    }

}
