package com.slide_todo.slide_todoApp.repository.todo;

import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
  public Long countAllByGoalId(Long goalId) {
    return em.createQuery("select count(t) from Todo t"
            + " where t.goal.id = :goalId", Long.class)
        .setParameter("goalId", goalId)
        .getSingleResult();
  }

  @Override
  public List<IndividualTodo> findAllIndividualTodoByMemberId(Long memberId, Long start, Long limit, List<Long> goalIds,
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
        .setFirstResult(start.intValue())
        .setMaxResults(limit.intValue())
         .getResultList();
  }

  @Override
  public Long countAllIndividualTodoByMemberId(Long memberId, List<Long> goalIds, Boolean isDone) {
    if (goalIds == null) {
      goalIds = em.createQuery("select g.id from IndividualGoal g"
              + " left join g.member m"
              + " where g.member.id = :memberId", Long.class)
          .setParameter("memberId", memberId)
          .getResultList().stream().toList();
    }
    return em.createQuery("select count(t) from IndividualTodo t"
            + " where t.isDeleted = false"
            + " and t.goal.id in :goalIds"
            + " and (:isDone is null or t.isDone = :isDone)", Long.class)
        .setParameter("goalIds", goalIds)
        .setParameter("isDone", isDone)
        .getSingleResult();
  }

  @Override
  public Todo findByTodoId(Long todoId) {
    try {
      return em.createQuery("select t from Todo t"
              + " left join fetch t.note"
              + " left join fetch t.goal"
              + " where t.id = :todoId", Todo.class)
          .setParameter("todoId", todoId)
          .getSingleResult();
    } catch (NoResultException e) {
      throw new CustomException(Exceptions.TODO_NOT_FOUND);
    }

  }
}
