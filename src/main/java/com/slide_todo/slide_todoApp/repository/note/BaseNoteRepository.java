package com.slide_todo.slide_todoApp.repository.note;

import com.slide_todo.slide_todoApp.domain.note.Note;
import java.util.List;

public interface BaseNoteRepository {

  /*노트 단건 조회*/
  Note findByNoteId(Long noteId);

  /*목표에 따른 노트 조회*/
  List<Note> findAllByGoalId(Long goalId, Long start, Long limit);

  /*목표에 따른 노트 개수 조회*/
  Long countAllByGoalId(Long goalId);

}
