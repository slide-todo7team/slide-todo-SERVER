package com.slide_todo.slide_todoApp.repository.group;

import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.Query;
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
}
