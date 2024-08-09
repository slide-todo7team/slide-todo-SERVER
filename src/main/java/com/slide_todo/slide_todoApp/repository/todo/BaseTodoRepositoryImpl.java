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
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

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
  public IndividualTodoSearchResultDTO findIndividualTodoByAdmin(String nickname, String title,
      LocalDateTime createdAfter, LocalDateTime createdBefore, long start, long limit) {

    StringBuilder queryBuilder = new StringBuilder("select it from IndividualTodo it"
        + " left join fetch it.goal g"
        + " left join fetch g.member m"
        + " where 1=1");
    StringBuilder countQueryBuilder = new StringBuilder(
        "select count(it) from IndividualTodo it"
            + " left join it.goal g"
            + " left join g.member m"
            + " where 1=1");

    if (nickname != null) {
      queryBuilder.append(" and g.member.nickname like :nickname");
      countQueryBuilder.append(" and g.member.nickname like :nickname");
    }
    if (title != null) {
      queryBuilder.append(" and it.title like :title");
      countQueryBuilder.append(" and it.title like :title");
    }
    if (createdAfter != null) {
      queryBuilder.append(" and it.created > :createdAfter");
      countQueryBuilder.append(" and it.created > :createdAfter");
    }
    if (createdBefore != null) {
      queryBuilder.append(" and it.created < :createdBefore");
      countQueryBuilder.append(" and it.created < :createdBefore");
    }

    TypedQuery<IndividualTodo> query = em.createQuery(queryBuilder.toString(),
        IndividualTodo.class);
    TypedQuery<Long> countQuery = em.createQuery(countQueryBuilder.toString(), Long.class);

    if (nickname != null) {
      query.setParameter("nickname", "%" + nickname + "%");
      countQuery.setParameter("nickname", "%" + nickname + "%");
    }
    if (title != null) {
      query.setParameter("title", "%" + title + "%");
      countQuery.setParameter("title", "%" + title + "%");
    }
    if (createdAfter != null) {
      query.setParameter("createdAfter", createdAfter);
      countQuery.setParameter("createdAfter", createdAfter);
    }
    if (createdBefore != null) {
      query.setParameter("createdBefore", createdBefore);
      countQuery.setParameter("createdBefore", createdBefore);
    }

    query.setFirstResult((int) start);
    query.setMaxResults((int) limit);

    return new IndividualTodoSearchResultDTO(query.getResultList(), countQuery.getSingleResult());
  }

  @Override
  public GroupTodoSearchResultDTO findGroupTodoByAdmin(String groupName, String title,
      LocalDateTime createdAfter, LocalDateTime createdBefore, long start, long limit) {
    StringBuilder queryBuilder = new StringBuilder("select gt from GroupTodo gt"
        + " left join fetch gt.goal g"
        + " left join fetch g.group gg"
        + " where 1=1");
    StringBuilder countQueryBuilder = new StringBuilder(
        "select count(gg) from GroupTodo gt"
            + " left join gt.goal g"
            + " left join g.group gg"
            + " where 1=1");

    if (groupName != null) {
      queryBuilder.append(" and gg.title like :groupName");
      countQueryBuilder.append(" and gg.title like :groupName");
    }
    if (title != null) {
      queryBuilder.append(" and gt.title like :title");
      countQueryBuilder.append(" and gt.title like :title");
    }
    if (createdAfter != null) {
      queryBuilder.append(" and gt.created > :createdAfter");
      countQueryBuilder.append(" and gt.created > :createdAfter");
    }
    if (createdBefore != null) {
      queryBuilder.append(" and gt.created < :createdBefore");
      countQueryBuilder.append(" and gt.created < :createdBefore");
    }

    TypedQuery<GroupTodo> query = em.createQuery(queryBuilder.toString(),
        GroupTodo.class);
    TypedQuery<Long> countQuery = em.createQuery(countQueryBuilder.toString(), Long.class);

    if (groupName != null) {
      query.setParameter("groupName", "%" + groupName + "%");
      countQuery.setParameter("groupName", "%" + groupName + "%");
    }
    if (title != null) {
      query.setParameter("title", "%" + title + "%");
      countQuery.setParameter("title", "%" + title + "%");
    }
    if (createdAfter != null) {
      query.setParameter("createdAfter", createdAfter);
      countQuery.setParameter("createdAfter", createdAfter);
    }
    if (createdBefore != null) {
      query.setParameter("createdBefore", createdBefore);
      countQuery.setParameter("createdBefore", createdBefore);
    }

    query.setFirstResult((int) start);
    query.setMaxResults((int) limit);

    return new GroupTodoSearchResultDTO(query.getResultList(), countQuery.getSingleResult());
  }
}
