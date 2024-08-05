package com.slide_todo.slide_todoApp.repository.note;

import com.slide_todo.slide_todoApp.domain.note.Note;

public interface BaseNoteRepository {

  Note findByNoteId(Long noteId);

}
