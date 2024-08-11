package com.slide_todo.slide_todoApp.service.todo;

import com.slide_todo.slide_todoApp.dto.todo.admin.AdminGroupTodoListDTO;
import com.slide_todo.slide_todoApp.dto.todo.admin.AdminIndividualTodoListDTO;
import com.slide_todo.slide_todoApp.dto.todo.admin.TodoIdsDTO;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;

public interface AdminTodoService {

  /*개인 목표에 대한 할 일 리스트 조회*/
  ResponseDTO<AdminIndividualTodoListDTO> getIndividualTodoList(Long goalId, long page, long limit);

  /*그룹 목표에 대한 할 일 리스트 조회*/
  ResponseDTO<AdminGroupTodoListDTO> getGroupTodoList(Long goalId, long page, long limit);

  /*개인 목표 복수 삭제*/
  ResponseDTO<?> deleteIndividualTodos(TodoIdsDTO request);

  /*그룹 목표 복수 삭제*/
  ResponseDTO<?> deleteGroupTodos(TodoIdsDTO request);
}
