package com.slide_todo.slide_todoApp.repository.note;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.note.Note;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.dto.note.NoteSearchResultDTO;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BaseNoteRepositoryImpl implements BaseNoteRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Note findByNoteId(Long noteId) {
    try {
      return em.createQuery("select n from Note n"
              + " left join fetch n.todo t"
              + " where n.id = :noteId", Note.class)
          .setParameter("noteId", noteId)
          .getSingleResult();
    } catch (NoResultException e) {
      throw new CustomException(Exceptions.NOTE_NOT_FOUND);
    }
  }

  @Override
  public List<Note> findAllByGoalId(Long goalId, Long start, Long limit) {
    return em.createQuery("select n from Note n"
            + " left join fetch n.todo t"
            + " where t.goal.id = :goalId"
            + " order by n.createdAt desc", Note.class)
        .setParameter("goalId", goalId)
        .setFirstResult(start.intValue())
        .setMaxResults(limit.intValue())
        .getResultList();
  }

  @Override
  public Long countAllByGoalId(Long goalId) {
    return em.createQuery("select count(n) from Note n"
            + " left join n.todo t"
            + " where n.todo.goal.id = :goalId", Long.class)
        .setParameter("goalId", goalId)
        .getSingleResult();
  }

  @Override
  public NoteSearchResultDTO findIndividualNoteByAdmin(String nickname, String title,
      LocalDateTime createdAfter, LocalDateTime createdBefore, long start, long limit) {
    StringBuilder queryBuilder = new StringBuilder("select n from Note n"
        + " left join fetch n.todo t"
        + " left join fetch t.goal g"
        + " left join fetch g.member m"
        + " where 1=1");
    StringBuilder countQueryBuilder = new StringBuilder("select count(n) from Note n"
        + " left join n.todo t"
        + " left join t.goal g"
        + " left join g.member m"
        + " where 1=1");

    if (nickname != null) {
      queryBuilder.append(" and m.nickname = :nickname");
      countQueryBuilder.append(" and m.nickname = :nickname");
    }
    if (title != null) {
      queryBuilder.append(" and n.title like :title");
      countQueryBuilder.append(" and n.title like :title");
    }
    if (createdAfter != null) {
      queryBuilder.append(" and n.created > :createdAfter");
      countQueryBuilder.append(" and n.created > :createdAfter");
    }
    if (createdBefore != null) {
      queryBuilder.append(" and n.created < :createdBefore");
      countQueryBuilder.append(" and n.created < :createdBefore");
    }
    queryBuilder.append((" order by n.createdAt desc"));

    TypedQuery<Note> query = em.createQuery(queryBuilder.toString(), Note.class);
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

    return new NoteSearchResultDTO(query.getResultList(), countQuery.getSingleResult());
  }

  @Override
  public NoteSearchResultDTO findGroupNoteByAdmin(String groupName, String title,
      LocalDateTime createdAfter, LocalDateTime createdBefore, long start, long limit) {

    StringBuilder queryBuilder = new StringBuilder("select gg.id from GroupGoal gg"
        + " left join gg.group g"
        + " where 1=1");

    if (groupName != null) {
      queryBuilder.append(" and g.title like :groupName");
    }

    TypedQuery<Long> goalQuery = em.createQuery(queryBuilder.toString(), Long.class);

    if (groupName != null) {
      goalQuery.setParameter("groupName", "%" + groupName + "%");
    }

    List<Long> goalIds = goalQuery.getResultList();

    StringBuilder noteQueryBuilder = new StringBuilder("select n from Note n"
        + " left join fetch n.todo t"
        + " left join fetch t.goal g"
        + " where g.id in :goalIds"
        + " and 1=1");

    StringBuilder countQueryBuilder = new StringBuilder("select count(n) from Note n"
        + " left join n.todo t"
        + " left join t.goal g"
        + " where g.id in :goalIds"
        + " and 1=1");

    if (title != null) {
      noteQueryBuilder.append(" and n.title like :title");
      countQueryBuilder.append(" and n.title like :title");
    }
    if (createdAfter != null) {
      noteQueryBuilder.append(" and n.createdAt > :createdAfter");
      countQueryBuilder.append(" and n.createdAt > :createdAfter");
    }
    if (createdBefore != null) {
      noteQueryBuilder.append(" and n.createdAt < :createdBefore");
      countQueryBuilder.append(" and n.createdAt < :createdBefore");
    }
    noteQueryBuilder.append((" order by n.createdAt desc"));

    TypedQuery<Note> noteQuery = em.createQuery(noteQueryBuilder.toString(), Note.class);
    TypedQuery<Long> countQuery = em.createQuery(countQueryBuilder.toString(), Long.class);

    noteQuery.setParameter("goalIds", goalIds);
    countQuery.setParameter("goalIds", goalIds);

    if (title != null) {
      noteQuery.setParameter("title", "%" + title + "%");
      countQuery.setParameter("title", "%" + title + "%");
    }
    if (createdAfter != null) {
      noteQuery.setParameter("createdAfter", createdAfter);
      countQuery.setParameter("createdAfter", createdAfter);
    }
    if (createdBefore != null) {
      noteQuery.setParameter("createdBefore", createdBefore);
      countQuery.setParameter("createdBefore", createdBefore);
    }

    noteQuery.setFirstResult((int) start);
    countQuery.setMaxResults((int) limit);

    return new NoteSearchResultDTO(noteQuery.getResultList(), countQuery.getSingleResult());
  }

  @Override
  public List<Note> findNotesToDelete(List<Long> ids) {
    return em.createQuery("select n from Note n"
            + " left join fetch n.todo t"
            + " where n.id in :ids", Note.class)
        .setParameter("ids", ids)
        .getResultList();
  }
}
