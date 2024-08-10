package com.slide_todo.slide_todoApp.service.todo;

import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.dto.todo.GroupTodoSearchResultDTO;
import com.slide_todo.slide_todoApp.dto.todo.IndividualTodoSearchResultDTO;
import com.slide_todo.slide_todoApp.dto.todo.admin.AdminGroupTodoListDTO;
import com.slide_todo.slide_todoApp.dto.todo.admin.AdminIndividualTodoListDTO;
import com.slide_todo.slide_todoApp.dto.todo.admin.TodoIdsDTO;
import com.slide_todo.slide_todoApp.repository.todo.TodoRepository;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminTodoServiceImpl implements AdminTodoService {

  private final TodoRepository todoRepository;

  @Override
  public ResponseDTO<AdminIndividualTodoListDTO> getIndividualTodoList(Long goalId, long page,
      long limit
  ) {
    long start;
    if (limit != 0) {
      start = (page - 1) * limit;
    } else {
      start = 0L;
      limit = todoRepository.count();
    }
    IndividualTodoSearchResultDTO result = todoRepository.findIndividualTodoByAdmin(goalId, start,
        limit);
    AdminIndividualTodoListDTO response = new AdminIndividualTodoListDTO(
        result.getTotalCount(), page, result.getIndividualTodos()
    );
    return new ResponseDTO<>(response, Responses.OK);
  }

  @Override
  public ResponseDTO<AdminGroupTodoListDTO> getGroupTodoList(Long goalId, long page, long limit
  ) {
    long start;
    if (limit != 0) {
      start = (page - 1) * limit;
    } else {
      start = 0L;
      limit = todoRepository.count();
    }
    GroupTodoSearchResultDTO result = todoRepository.findGroupTodoByAdmin(goalId, start, limit);
    AdminGroupTodoListDTO response = new AdminGroupTodoListDTO(
        result.getTotalCount(), page, result.getGroupTodos()
    );
    return new ResponseDTO<>(response, Responses.OK);
  }

  @Override
  @Transactional
  public ResponseDTO<?> deleteIndividualTodos(TodoIdsDTO request) {
    List<IndividualTodo> todos = todoRepository.findIndividualTodosToDelete(request.getTodoIds());

    todos.forEach(Todo::deleteTodo);

    return new ResponseDTO<>(null, Responses.NO_CONTENT);
  }

  @Override
  @Transactional
  public ResponseDTO<?> deleteGroupTodos(TodoIdsDTO request) {
    List<GroupTodo> todos = todoRepository.findGroupTodosToDelete(request.getTodoIds());

    todos.forEach(Todo::deleteTodo);

    return new ResponseDTO<>(null, Responses.NO_CONTENT);
  }
}
