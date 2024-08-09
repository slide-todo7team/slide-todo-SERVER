package com.slide_todo.slide_todoApp.repository.todo;

import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.dto.todo.GroupTodoSearchResultDTO;
import com.slide_todo.slide_todoApp.dto.todo.IndividualTodoSearchResultDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface BaseTodoRepository {

  /*특정 목표의 모든 할 일 조회*/
  List<Todo> findAllByGoalId(Long goalId);

  /*특정 목표의 모든 할 일 개수 조회*/
  Long countAllByGoalId(Long goalId);

  /*특정 회원의 모든 할 일 조회<br>
   * goalIds가 null이면 모든 목표의 할 일 조회<br>
   * isDone이 null이면 완료 여부 관계 없이 조회*/
  List<IndividualTodo> findAllIndividualTodoByMemberId(
      Long memberId, Long start, Long limit, List<Long> goalIds, Boolean isDone
  );

  /*특정 회원의 모든 할일 조회<br>
   * goalIds가 null이면 모든 목표의 할 일 조회<br>
   * isDone이 null이면 완료 여부 관계 없이 조회 */
  Long countAllIndividualTodoByMemberId(
      Long memberId, List<Long> goalIds, Boolean isDone
  );

  /*목표의 ID를 통해 단건 조회*/
  Todo findByTodoId(Long todoId);

  /*삭제할 할 일 리스트 조회*/
  List<Todo> findTodosToDelete(List<Long> ids);

  /*삭제할 개인 할 일 리스트 조회*/
  List<IndividualTodo> findIndividualTodosToDelete(List<Long> ids);

  /*삭제할 그룹 할 일 리스트 조회*/
  List<GroupTodo> findGroupTodosToDelete(List<Long> ids);

  /*어드민 페이지 개인 할 일 리스트 조회*/
  IndividualTodoSearchResultDTO findIndividualTodoByAdmin(
      String nickname, String title, LocalDateTime createdAfter, LocalDateTime createdBefore,
      long start, long limit
  );

  /*어드민 페이지 그룹 할 일 리스트 조회*/
  GroupTodoSearchResultDTO findGroupTodoByAdmin(
      String groupName, String title, LocalDateTime createdAfter, LocalDateTime createdBefore,
      long start, long limit
  );
}