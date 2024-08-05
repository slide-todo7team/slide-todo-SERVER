package com.slide_todo.slide_todoApp.repository.note;

import com.slide_todo.slide_todoApp.domain.note.Note;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.core.parameters.P;

public class BaseNoteRepositoryImpl implements BaseNoteRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Note findByNoteId(Long noteId) {
    try {
      return em.createQuery("select n from Note n"
              + " where n.id = :noteId", Note.class)
          .setParameter("noteId", noteId)
          .getSingleResult();
    } catch (NoResultException e) {
      throw new CustomException(Exceptions.NOTE_NOT_FOUND);
    }
  }
}
