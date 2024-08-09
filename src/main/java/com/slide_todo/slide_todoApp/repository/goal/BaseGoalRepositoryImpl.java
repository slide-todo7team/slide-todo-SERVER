package com.slide_todo.slide_todoApp.repository.goal;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.dto.goal.GroupGoalSearchResultDTO;
import com.slide_todo.slide_todoApp.dto.goal.IndividualGoalSearchResultDTO;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BaseGoalRepositoryImpl implements BaseGoalRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public IndividualGoalSearchResultDTO findIndividualGoalByAdmin(String nickname, String title,
      LocalDateTime createdAfter, LocalDateTime createdBefore, long start, long limit) {
    StringBuilder queryBuilder = new StringBuilder("select ig from IndividualGoal ig where 1=1");
    StringBuilder countQueryBuilder = new StringBuilder(
        "select count(ig) from IndividualGoal ig where 1=1");

    if (nickname != null) {
      queryBuilder.append(" and ig.member.nickname like :nickname");
      countQueryBuilder.append(" and ig.member.nickname like :nickname");
    }
    if (title != null) {
      queryBuilder.append(" and ig.title like :title");
      countQueryBuilder.append(" and ig.title like :title");
    }
    if (createdAfter != null) {
      queryBuilder.append(" and ig.created > :createdAfter");
      countQueryBuilder.append(" and ig.created > :createdAfter");
    }
    if (createdBefore != null) {
      queryBuilder.append(" and ig.created < :createdBefore");
      countQueryBuilder.append(" and ig.created < :createdBefore");
    }

    TypedQuery<IndividualGoal> query = em.createQuery(queryBuilder.toString(),
        IndividualGoal.class);
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

    return new IndividualGoalSearchResultDTO(query.getResultList(), countQuery.getSingleResult());
  }

  @Override
  public GroupGoalSearchResultDTO findGroupGoalByAdmin(String groupName, String title,
      LocalDateTime createdAfter, LocalDateTime createdBefore, long start, long limit) {

    StringBuilder queryBuilder = new StringBuilder("select gg from GroupGoal gg"
        + " left join fetch gg.group g"
        + " where 1=1");
    StringBuilder countQueryBuilder = new StringBuilder(
        "select count(gg) from GroupGoal gg"
            + " left join fetch gg.group g"
            + " where 1=1");

    if (groupName != null) {
      queryBuilder.append(" and g.title like :groupName");
      countQueryBuilder.append(" and g.title like :groupName");
    }
    if (title != null) {
      queryBuilder.append(" and gg.title like :title");
      countQueryBuilder.append(" and gg.title like :title");
    }
    if (createdAfter != null) {
      queryBuilder.append(" and gg.createdAt > :createdAfter");
      countQueryBuilder.append(" and gg.createdAt > :createdAfter");
    }
    if (createdBefore != null) {
      queryBuilder.append(" and gg.createdAt < :createdBefore");
      countQueryBuilder.append(" and gg.createdAt < :createdBefore");
    }

    TypedQuery<GroupGoal> query = em.createQuery(queryBuilder.toString(), GroupGoal.class);
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

    return new GroupGoalSearchResultDTO(query.getResultList(), countQuery.getSingleResult());
  }

  @Override
  public List<IndividualGoal> findIndividualGoalsToDelete(List<Long> ids) {
    List<IndividualGoal> goals = em.createQuery("select ig from IndividualGoal ig"
            + " left join fetch ig.member"
            + " left join fetch ig.todos t"
            + " where ig.id in :ids", IndividualGoal.class)
        .setParameter("ids", ids)
        .getResultList();

    goals.forEach(goal -> {
      goal.updateTodos(em.createQuery("select t from Todo t"
              + " left join fetch t.note"
              + " where t.goal.id = :goalId", Todo.class)
          .setParameter("goalId", goal.getId())
          .getResultList());
    });

    return goals;
  }

  @Override
  public List<GroupGoal> findGroupGoalsToDelete(List<Long> ids) {
    List<GroupGoal> goals = em.createQuery("select gg from GroupGoal gg"
            + " left join fetch gg.group"
            + " left join fetch gg.todos t"
            + " where gg.id in :ids", GroupGoal.class)
        .setParameter("ids", ids)
        .getResultList();

    goals.forEach(goal -> {
      goal.updateTodos(em.createQuery("select t from Todo t"
              + " left join fetch t.note"
              + " where t.goal.id = :goalId", Todo.class)
          .setParameter("goalId", goal.getId())
          .getResultList());
    });

    return goals;
  }

  @Override
  public IndividualGoal findIndividualGoalDetail(Long goalId) {
    try {
      return em.createQuery("select ig from IndividualGoal ig"
              + " left join fetch ig.member"
              + " left join fetch ig.todos t"
              + " where ig.id = :goalId", IndividualGoal.class)
          .setParameter("goalId", goalId)
          .getSingleResult();
    } catch (NoResultException e) {
      throw new CustomException(Exceptions.GOAL_NOT_FOUND);
    }
  }

  @Override
  public GroupGoal findGroupGoalDetail(Long goalId) {
    try {
      GroupGoal goal = em.createQuery("select gg from GroupGoal gg"
          + " left join fetch gg.group"
          + " left join fetch gg.todos t"
          + " where gg.id = :goalId", GroupGoal.class)
          .setParameter("goalId", goalId)
          .getSingleResult();

      List<GroupTodo> todos = goal.getTodos().stream()
          .map(o -> (GroupTodo) o)
          .toList();

      todos.forEach(todo -> {
        todo.updateMemberInCharge(em.createQuery("select gt.memberInCharge from GroupTodo gt"
            + " left join fetch gt.memberInCharge"
            + " where gt.id = :todoId", GroupMember.class)
            .setParameter("todoId", todo.getId())
            .getSingleResult());
      });
      goal.updateTodos(todos.stream().map(o -> (Todo) o).toList());
      return goal;
    } catch (NoResultException e) {
      throw new CustomException(Exceptions.GOAL_NOT_FOUND);
    }
  }
}
