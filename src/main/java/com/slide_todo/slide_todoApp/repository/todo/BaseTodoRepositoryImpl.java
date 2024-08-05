package com.slide_todo.slide_todoApp.repository.todo;

import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class BaseTodoRepositoryImpl implements BaseTodoRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<Todo> findAllByGoalId(Long goalId) {
    return em.createQuery("select t from Todo t"
            + " left join fetch t.note"
            + " where t.goal.id = :goalId", Todo.class)
        .setParameter("goalId", goalId)
        .getResultList();
  }

  @Override
  public List<IndividualTodo> findAllIndividualTodoByMemberId(Long memberId, List<Long> goalIds,
      Boolean isDone) {
    if (goalIds == null) {
      goalIds = em.createQuery("select g.id from IndividualGoal g"
              + " left join g.member m"
              + " where g.member.id = :memberId", Long.class)
          .setParameter("memberId", memberId)
          .getResultList().stream().toList();
    }
    return em.createQuery("select t from IndividualTodo t"
                + " left join fetch t.note n"
                + " where t.isDeleted = false"
                + " and t.goal.id in :goalIds"
                + " and (:isDone is null or t.isDone = :isDone)",
            IndividualTodo.class)
        .setParameter("goalIds", goalIds)
        .setParameter("isDone", isDone)
        .getResultList();
  }

  @Override
  public Todo findByTodoId(Long todoId) {
    return Optional.ofNullable(em.createQuery("select t from Todo t"
            + " left join fetch t.note"
            + " where t.id = :todoId", Todo.class)
        .setParameter("todoId", todoId)
        .getSingleResult())
        .orElseThrow(() -> new CustomException(Exceptions.TODO_NOT_FOUND));
  }
}
