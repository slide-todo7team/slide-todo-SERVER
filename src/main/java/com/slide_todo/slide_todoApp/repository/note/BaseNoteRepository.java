package com.slide_todo.slide_todoApp.repository.note;

import com.slide_todo.slide_todoApp.domain.note.Note;

public interface BaseNoteRepository {

  /*노트 단건 조회*/
  Note findByNoteId(Long noteId);

}
