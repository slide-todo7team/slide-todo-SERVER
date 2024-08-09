package com.slide_todo.slide_todoApp.service.goal;

import com.slide_todo.slide_todoApp.dto.goal.GoalTodosResponseDTO;
import com.slide_todo.slide_todoApp.dto.goal.IndividualGoalDTO;
import com.slide_todo.slide_todoApp.dto.goal.IndividualGoalTodoDTO;
import com.slide_todo.slide_todoApp.dto.goal.IndividualProgressDTO;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;

public interface IndividualGoalService {
    ResponseDTO<IndividualGoalDTO> createIndividualGoal(String title, Long memberId);

    ResponseDTO<?> getIndividualGoals(Long memberId);

    ResponseDTO<GoalTodosResponseDTO<IndividualGoalTodoDTO>> getIndividualGoalTodos(Long memberId, int page, Integer limit);

    ResponseDTO<IndividualGoalDTO> updateIndividualGoal(Long goalId, String title);

    ResponseDTO<?> deleteIndividualGoal(Long goalId);

    ResponseDTO<IndividualProgressDTO> getIndividualProgress(Long memberId);

    void checkTitleLength(String title);
}
