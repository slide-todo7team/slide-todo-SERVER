package com.slide_todo.slide_todoApp.repository.todo;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long>, BaseTodoRepository {
    List<IndividualTodo> findAllByGoal(IndividualGoal individualGoal);
    List<GroupTodo> findAllByGoal(GroupGoal groupGoal);
}