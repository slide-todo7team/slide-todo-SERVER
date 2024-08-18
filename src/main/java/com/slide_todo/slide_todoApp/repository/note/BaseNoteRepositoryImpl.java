package com.slide_todo.slide_todoApp.repository.note;

import com.slide_todo.slide_todoApp.domain.note.Note;
import com.slide_todo.slide_todoApp.dto.note.NoteSearchResultDTO;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;

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
  public NoteSearchResultDTO findAllByGoalId(Long goalId, long start, long limit) {
    List<Note> notes = em.createQuery("select n from Note n"
            + " left join fetch n.todo t"
            + " left join fetch t.goal g"
            + " where g.id = :goalId"
            + " order by n.createdAt desc", Note.class)
        .setParameter("goalId", goalId)
        .setFirstResult((int) start)
        .setMaxResults((int) limit)
        .getResultList();

    long totalCount = em.createQuery("select count(n) from Note n"
            + " left join n.todo t"
            + " left join t.goal g"
            + " where g.id = :goalId", Long.class)
        .setParameter("goalId", goalId)
        .getSingleResult();

    return new NoteSearchResultDTO(notes, totalCount);
  }

  @Override
  public Long countAllByGoalId(Long goalId) {
    return em.createQuery("select count(n) from Note n"
            + " left join n.todo t"
            + " where n.todo.goal.id = :goalId", Long.class)
        .setParameter("goalId", goalId)
        .getSingleResult();
  }
}
