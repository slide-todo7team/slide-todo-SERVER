package com.slide_todo.slide_todoApp.service.goal;

import com.slide_todo.slide_todoApp.dto.goal.GoalTodosResponseDTO;
import com.slide_todo.slide_todoApp.dto.goal.GroupGoalDTO;
import com.slide_todo.slide_todoApp.dto.goal.GroupGoalTodoDTO;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface GroupGoalService {
    ResponseDTO<GroupGoalDTO> createGroupGoal(Long groupId,String title);

    ResponseDTO<?> getGroupGoals(Long groupId);

    ResponseDTO<GoalTodosResponseDTO<GroupGoalTodoDTO>> getGroupGoalTodos(Long groupId,int page,Integer limit);

    ResponseDTO<GroupGoalDTO> updateGroupGoal(Long groupId, Long goalId, String title);

    ResponseDTO<?> deleteGroupGoal(Long groupId, Long goalId);
}
