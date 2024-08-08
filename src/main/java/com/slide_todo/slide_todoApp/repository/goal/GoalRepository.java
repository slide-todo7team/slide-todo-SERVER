package com.slide_todo.slide_todoApp.repository.goal;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GoalRepository extends JpaRepository<Goal, Long>, BaseGoalRepository {

  @Query("SELECT g FROM Goal g WHERE g.id = :goalId")
  Goal findByGoalId(Long goalId);

  @Query("SELECT g FROM Goal g LEFT JOIN FETCH g.todos WHERE g.id = :goalId")
  Goal findWithTodos(Long goalId);
}
