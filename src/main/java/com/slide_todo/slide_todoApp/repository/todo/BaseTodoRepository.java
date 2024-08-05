package com.slide_todo.slide_todoApp.repository.todo;

import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import java.util.List;

public interface BaseTodoRepository {

    /*특정 목표의 모든 할 일 조회*/
    List<Todo> findAllByGoalId(Long goalId);

    /*특정 회원의 모든 할 일 조회<br>
     * goalIds가 null이면 모든 목표의 할 일 조회<br>
     * isDone이 null이면 완료 여부 관계 없이 조회*/
    List<IndividualTodo> findAllIndividualTodoByMemberId(
            Long memberId, List<Long> goalIds, Boolean isDone
    );


}