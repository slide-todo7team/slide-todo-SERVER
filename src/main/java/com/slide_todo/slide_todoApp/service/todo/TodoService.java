package com.slide_todo.slide_todoApp.service.todo;

import com.slide_todo.slide_todoApp.dto.todo.IndividualTodoDTO;
import com.slide_todo.slide_todoApp.dto.todo.IndividualTodoListDTO;
import com.slide_todo.slide_todoApp.dto.todo.RetrieveIndividualTodoDTO;
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
  ResponseDTO<IndividualTodoListDTO> getIndividualTodoList(Long memberId, Long page, Long limit, RetrieveIndividualTodoDTO request);
}
