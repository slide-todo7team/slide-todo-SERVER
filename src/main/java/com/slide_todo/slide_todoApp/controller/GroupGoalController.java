package com.slide_todo.slide_todoApp.controller;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.dto.goal.GoalTitleDTO;
import com.slide_todo.slide_todoApp.dto.goal.GoalTodosResponseDTO;
import com.slide_todo.slide_todoApp.dto.goal.GroupGoalDTO;
import com.slide_todo.slide_todoApp.dto.goal.GroupGoalTodoDTO;
import com.slide_todo.slide_todoApp.service.goal.GroupGoalService;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group/goals")
@Tag(name = "그룹 목표 API")
public class GroupGoalController {
    private final GroupGoalService groupGoalService;

    public GroupGoalController(GroupGoalService groupGoalService) {
        this.groupGoalService = groupGoalService;
    }

    @PostMapping("/{groupId}")
    @Operation(summary = "그룹 목표 생성")
    public ResponseDTO<GroupGoalDTO> createGroupGoal(@PathVariable Long groupId, @RequestBody GoalTitleDTO goalTitleDTO){
        String title = goalTitleDTO.getTitle();
        return groupGoalService.createGroupGoal(groupId, title);
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "그룹 목표 리스트 조회")
    public ResponseDTO<?> getGroupGoals(@PathVariable Long groupId){
        return groupGoalService.getGroupGoals(groupId);
    }

    @GetMapping("/todos/{groupId}")
    @Operation(summary = "모든 그룹의 목표 및 할일 리스트 조회")
    public ResponseDTO<GoalTodosResponseDTO<GroupGoalTodoDTO>> getGroupGoalTodos(@PathVariable Long groupId,
                                                                                 @RequestParam(defaultValue =  "0") Long cursor, @RequestParam Integer limit){
        return groupGoalService.getGroupGoalTodos(groupId,cursor,limit);
    }

    @PatchMapping("/{groupId}/{goalId}")
    @Operation(summary = "그룹 목표 수정")
    public ResponseDTO<GroupGoalDTO> updateGroupGoal(@PathVariable Long groupId, @PathVariable Long goalId,@RequestBody GoalTitleDTO goalTitleDTO){
        String title = goalTitleDTO.getTitle();
        return groupGoalService.updateGroupGoal(groupId,goalId,title);
    }

    @DeleteMapping("/{groupId}/{goalId}")
    @Operation(summary = "그룹 목표 삭제")
    public ResponseDTO<?> deleteGroupGoal(@PathVariable Long groupId, @PathVariable Long goalId){
        return groupGoalService.deleteGroupGoal(groupId,goalId);
    }
}
