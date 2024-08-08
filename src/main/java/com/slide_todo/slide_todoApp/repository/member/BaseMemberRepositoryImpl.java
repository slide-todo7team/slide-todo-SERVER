package com.slide_todo.slide_todoApp.repository.member;

import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.member.MemberRole;
import com.slide_todo.slide_todoApp.domain.todo.IndividualTodo;
import com.slide_todo.slide_todoApp.dto.member.MemberSearchResultDTO;
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
  public MemberSearchResultDTO findByNicknameAndEmailAndCreatedAt(String nickname,
      String email, LocalDateTime createdAfter, LocalDateTime createdBefore, long start, long limit) {
    StringBuilder queryString = new StringBuilder("select m from Member m where 1=1");
    StringBuilder countQueryString = new StringBuilder("select count(m) from Member m where 1=1");

    if (nickname != null) {
      queryString.append(" and replace(m.nickname, ' ', '') like :nickname");
      countQueryString.append(" and replace(m.nickname, ' ', '') like :nickname");
    }
    if (email != null) {
      queryString.append(" and replace(m.email, ' ', '') like :email");
      countQueryString.append(" and replace(m.email, ' ', '') like :email");
    }
    if (createdAfter != null) {
      queryString.append(" and m.createdAt > :createdAfter");
      countQueryString.append(" and m.createdAt > :createdAfter");
    }
    if (createdBefore != null) {
      queryString.append(" and m.createdAt < :createdBefore");
      countQueryString.append(" and m.createdAt < :createdBefore");
    }

    TypedQuery<Member> query = em.createQuery(queryString.toString(), Member.class);
    TypedQuery<Long> countQuery = em.createQuery(countQueryString.toString(), Long.class);
    if (nickname != null) {
      query.setParameter("nickname", "%" + nickname + "%");
      countQuery.setParameter("nickname", "%" + nickname + "%");
    }
    if (email != null) {
      query.setParameter("email", "%" + email + "%");
      countQuery.setParameter("email", "%" + email + "%");
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

    return new MemberSearchResultDTO(query.getResultList(), countQuery.getSingleResult());
  }

  @Override
  public Member findMemberWithGoalAndGroupMember(Long memberId) {
    try {
      Member member = em.createQuery("select m from Member m"
              + " left join fetch m.individualGoals ig"
              + " where m.id =:memberId", Member.class)
          .setParameter("memberId", memberId)
          .getSingleResult();

      member.updateGroupMembers(em.createQuery("select gm from GroupMember  gm"
          + " where gm.member in :member", GroupMember.class)
          .setParameter("member", member)
          .getResultList());

      return member;

    } catch (NoResultException e) {
      throw new CustomException(Exceptions.MEMBER_NOT_FOUND);
    }
  }

  @Override
  public List<Member> findMembersToDelete(List<Long> ids) {
    List<Member> members = em.createQuery("select m from Member m"
            + " left join fetch m.individualGoals ig"
            + " where m.id in :ids", Member.class)
        .setParameter("ids", ids)
        .getResultList();

    members.forEach(member -> {
      member.updateGroupMembers(em.createQuery("select gm from GroupMember  gm"
              + " left join fetch gm.chargingTodos"
              + " where gm.member in :member", GroupMember.class)
          .setParameter("member", member)
          .getResultList());
    } );

    return members;
  }
}
