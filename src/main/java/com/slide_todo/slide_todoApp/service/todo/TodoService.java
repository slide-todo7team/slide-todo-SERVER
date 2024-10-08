package com.slide_todo.slide_todoApp.service.todo;

import com.slide_todo.slide_todoApp.dto.todo.GroupTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.IndividualTodoListDTO;
import com.slide_todo.slide_todoApp.dto.todo.TodoCreateDTO;
import com.slide_todo.slide_todoApp.dto.todo.TodoUpdateDTO;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import java.util.List;

public interface TodoService {

  /*할 일 생성*/
  ResponseDTO<?> createTodo(Long memberId, TodoCreateDTO request);

  /*할 일 수정*/
  ResponseDTO<?> updateTodo(Long memberId, Long todoId, TodoUpdateDTO request);

  /*할 일 완료 여부 변경*/
  ResponseDTO<?> changeTodoDone(Long memberId, Long todoId);

  /*할 일 삭제*/
  ResponseDTO<?> deleteTodo(Long memberId, Long todoId);

  /*개인의 모든 할 일 조회*/
  ResponseDTO<IndividualTodoListDTO> getIndividualTodoList(Long memberId, Long page, Long limit, List<Long> goalIds, Boolean isDone);

  /*그룹 할 일의 담당 그룹 멤버 교체*/
  ResponseDTO<GroupTodoDTO> updateChargingGroupMember(Long memberId, Long todoId);

  /*특정 목표의 할 일 목록 조회*/
  ResponseDTO<?> getTodoListByGoal(Long memberId, Long goalId);
}
