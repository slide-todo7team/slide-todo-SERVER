package com.slide_todo.slide_todoApp.repository.note;

import com.slide_todo.slide_todoApp.domain.note.Note;
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
  public List<Note> findAllByGoalId(Long goalId, Long start, Long limit) {
    return em.createQuery("select n from Note n"
            + " left join fetch n.todo t"
            + " where t.goal.id = :goalId", Note.class)
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


}
