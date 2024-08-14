package com.slide_todo.slide_todoApp.repository.todo;

import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo, Long>, BaseTodoRepository {
    List<IndividualTodo> findAllByGoal(IndividualGoal individualGoal);

    @Query("select gt from GroupTodo gt"
        + " left join fetch gt.memberInCharge mc"
        + " left join fetch mc.member"
        + " where gt.goal.id = :groupGoalId")
    List<GroupTodo> findAllByGoal(Long groupGoalId);
}