package com.slide_todo.slide_todoApp.repository.group;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class BaseGroupRepositoryImpl implements BaseGroupRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public boolean checkGroupPermission(Long memberId, Long groupId) {
    return em.createQuery("select count(gm) > 0 from Group g"
            + " join g.groupMembers gm"
            + " where g.id = :groupId"
            + " and gm.member.id = :memberId", Boolean.class)
        .setParameter("groupId", groupId)
        .setParameter("memberId", memberId)
        .getSingleResult();
  }
}
