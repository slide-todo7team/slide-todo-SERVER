package com.slide_todo.slide_todoApp.repository.member;

import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.member.MemberRole;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BaseMemberRepositoryImpl implements BaseMemberRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Member findByMemberId(Long memberId) {
    try {
      return em.find(Member.class, memberId);
    } catch (NoResultException e) {
      throw new CustomException(Exceptions.MEMBER_NOT_FOUND);
    }
  }

  @Override
  public Member findByEmail(String email) {
    try {
      return em.createQuery("select m from Member m"
              + " where m.email =:email", Member.class)
          .setParameter("email", email)
          .getSingleResult();
    } catch (NoResultException e) {
      throw new CustomException(Exceptions.MEMBER_WITH_EMAIL_NOT_FOUND);
    }
  }

  @Override
  public Boolean existsByEmail(String email) {
    return !em.createQuery("select m from Member m"
            + " where m.email =:email", Member.class)
        .setParameter("email", email)
        .setMaxResults(1)
        .getResultList().isEmpty();
  }

  @Override
  public Member findByNickname(String nickname) {
    try {
      return em.createQuery("select m from Member m"
              + " where m.nickname =:nickname", Member.class)
          .setParameter("nickname", nickname)
          .getSingleResult();
    } catch (NoResultException e) {
      throw new CustomException(Exceptions.MEMBER_NOT_FOUND);
    }
  }

  @Override
  public Boolean existsByNickname(String nickname) {
    return !em.createQuery("select m from Member m"
            + " where m.nickname =:nickname", Member.class)
        .setParameter("nickname", nickname)
        .setMaxResults(1)
        .getResultList().isEmpty();
  }

  @Override
  public Long countByRole(MemberRole role) {
    return em.createQuery("select count(m) from Member m"
            + " where m.role =:role", Long.class)
        .setParameter("role", role)
        .getSingleResult();
  }

  @Override
  public List<Member> findByRoll(MemberRole role, int page, int limit) {
    int first = (page - 1) * limit;
    return em.createQuery("select m from Member m"
            + " where m.role =:role", Member.class)
        .setParameter("role", role)
        .setFirstResult(first)
        .setMaxResults(first + limit)
        .getResultList();
  }

  @Override
  public Long countAll() {
    return em.createQuery("select count(m) from Member m", Long.class)
        .getSingleResult();
  }

  @Override
  public List<Member> findAll(int page, int limit) {
    int first = (page - 1) * limit;
    return em.createQuery("select m from Member m", Member.class)
        .setFirstResult(first)
        .setMaxResults(first + limit)
        .getResultList();
  }

  @Override
  public List<Member> findByNameAndNicknameAndEmailAndCreatedAt(String name, String nickname,
      String email, String createdAt, long start, long limit) {
    StringBuilder queryString = new StringBuilder("select m from Member m where 1=1");

    if (name !=null) {
      queryString.append(" and replace(m.name, ' ', '') like :name");
    }
    if (nickname != null) {
      queryString.append(" and replace(m.nickname, ' ', '') like :nickname");
    }
    if (email != null) {
      queryString.append(" and replace(m.email, ' ', '') like :email");
    }
    if (createdAt != null) {
      queryString.append(" and m.createdAt > :createdAt");
    }

    TypedQuery<Member> query = em.createQuery(queryString.toString(), Member.class);
    if (name != null) {
      query.setParameter("name", "%" + name + "%");
    }
    if (nickname != null) {
      query.setParameter("nickname", "%" + nickname + "%");
    }
    if (email != null) {
      query.setParameter("email", "%" + email + "%");
    }
    if (createdAt != null) {
      LocalDateTime createdAtDateTime = LocalDate.parse(createdAt.replace(" ", "")).atStartOfDay();
      query.setParameter("createdAt", createdAtDateTime);
    }

    query.setFirstResult((int) start);
    query.setMaxResults((int) limit);

    return query.getResultList();
  }

  @Override
  public Member findMemberWithGoalAndGroupMember(Long memberId) {
    try {
      return em.createQuery("select m from Member m"
          + " left join fetch m.individualGoals ig"
          + " left join fetch m.groupMembers gm"
          + " where m.id =:memberId", Member.class)
          .setParameter("memberId", memberId)
          .getSingleResult();
    } catch (NoResultException e) {
      throw new CustomException(Exceptions.MEMBER_NOT_FOUND);
    }
  }


}
