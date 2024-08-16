package com.slide_todo.slide_todoApp.repository.group;

import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class BaseGroupMemberRepositoryImpl implements BaseGroupMemberRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public GroupMember findByMemberIdAndGroupId(Long memberId, Long groupId) {
    try {
      return em.createQuery("SELECT gm FROM GroupMember gm"
              + " WHERE gm.member.id = :memberId"
              + " AND gm.group.id = :groupId", GroupMember.class)
          .setParameter("memberId", memberId)
          .setParameter("groupId", groupId)
          .getSingleResult();
    } catch (NoResultException e) {
      throw new CustomException(Exceptions.NO_PERMISSION_FOR_THE_GROUP);
    }

  }

  @Override
  public Map<Long, GroupMember> findAllByGroupTodoIds(List<Long> groupTodoIds) {
    List<GroupTodo> todos = em.createQuery("select gt from GroupTodo gt"
            + " left join fetch gt.memberInCharge gm"
            + " left join fetch gm.member m"
            + " where gt.id in :groupTodoIds"
            + " and gt.memberInCharge is not null", GroupTodo.class)
        .setParameter("groupTodoIds", groupTodoIds)
        .getResultList();

    return todos.stream().collect(Collectors.toMap(GroupTodo::getId, GroupTodo::getMemberInCharge));
  }
}
