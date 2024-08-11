package com.slide_todo.slide_todoApp.repository.todo;

import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.dto.todo.GroupTodoSearchResultDTO;
import com.slide_todo.slide_todoApp.dto.todo.IndividualTodoSearchResultDTO;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;

public class BaseTodoRepositoryImpl implements BaseTodoRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<Todo> findAllByGoalId(Long goalId) {
    return em.createQuery("select t from Todo t"
            + " left join fetch t.note"
            + " where t.goal.id = :goalId"
            + " order by t.createdAt desc ", Todo.class)
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
  public List<IndividualTodo> findAllIndividualTodoByMemberId(Long memberId, Long start, Long limit,
      List<Long> goalIds,
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
                + " and (:isDone is null or t.isDone = :isDone)"
                + " order by t.createdAt desc",
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

  @Override
  public List<Todo> findTodosToDelete(List<Long> ids) {
    return em.createQuery("select t from Todo t"
            + " left join fetch t.goal"
            + " left join fetch t.note"
            + " where t.id in :ids", Todo.class)
        .setParameter("ids", ids)
        .getResultList();
  }

  @Override
  public List<IndividualTodo> findIndividualTodosToDelete(List<Long> ids) {
    return em.createQuery("select it from IndividualTodo it"
            + " left join fetch it.goal g"
            + " left join fetch it.note n"
            + " where it.id in :ids", IndividualTodo.class)
        .setParameter("ids", ids)
        .getResultList();
  }

  @Override
  public List<GroupTodo> findGroupTodosToDelete(List<Long> ids) {
    return em.createQuery("select gt from GroupTodo gt"
            + " left join fetch gt.goal g"
            + " left join fetch gt.note n"
            + " where gt.id in :ids", GroupTodo.class)
        .setParameter("ids", ids)
        .getResultList();
  }

  @Override
  public IndividualTodoSearchResultDTO findIndividualTodoByAdmin(Long goalId, long start,
      long limit) {

    List<IndividualTodo> todos = em.createQuery("select it from IndividualTodo it"
            + " where it.goal.id = :goalId"
            + " order by it.createdAt desc", IndividualTodo.class)
        .setParameter("goalId", goalId)
        .setFirstResult((int) start)
        .setMaxResults((int) limit)
        .getResultList();

    Long totalCount = em.createQuery("select count(it) from IndividualTodo it"
            + " where it.goal.id = :goalId", Long.class)
        .setParameter("goalId", goalId)
        .getSingleResult();

    return new IndividualTodoSearchResultDTO(todos, totalCount);
  }

  @Override
  public GroupTodoSearchResultDTO findGroupTodoByAdmin(Long goalId, long start, long limit) {

    List<GroupTodo> todos = em.createQuery("select gt from GroupTodo gt"
            + " left join fetch gt.writer"
            + " left join fetch gt.memberInCharge"
            + " where gt.goal.id = :goalId"
            + " order by gt.createdAt desc", GroupTodo.class)
        .setParameter("goalId", goalId)
        .setFirstResult((int) start)
        .setMaxResults((int) limit)
        .getResultList();

    Long totalCount = em.createQuery("select count(gt) from GroupTodo  gt"
        + " where gt.goal.id = :goalId", Long.class)
        .setParameter("goalId", goalId)
        .getSingleResult();

    return new GroupTodoSearchResultDTO(todos, totalCount);
  }

  @Override
  public GroupTodo findGroupTodoByNoteId(Long noteId) {
    try {
      return em.createQuery("select gt from GroupTodo  gt"
          + " left join fetch gt.memberInCharge mc"
          + " left join fetch mc.member m"
          + " left join gt.note n"
          + "  where n.id = :noteId", GroupTodo.class)
          .setParameter("noteId", noteId)
          .getSingleResult();
    } catch (NoResultException e) {
      throw new CustomException(Exceptions.TODO_NOT_FOUND);
    }
  }
}
