package com.slide_todo.slide_todoApp.repository.note;

import com.slide_todo.slide_todoApp.domain.note.Note;
import com.slide_todo.slide_todoApp.dto.note.NoteSearchResultDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface BaseNoteRepository {

  /*노트 단건 조회*/
  Note findByNoteId(Long noteId);

  /*목표에 따른 노트 조회*/
  List<Note> findAllByGoalId(Long goalId, Long start, Long limit);

  /*목표에 따른 노트 개수 조회*/
  Long countAllByGoalId(Long goalId);

  /*어드민 페이지에서 개인 노트 리스트 조회*/
  NoteSearchResultDTO findIndividualNoteByAdmin(String nickname, String title,
      LocalDateTime createdAfter, LocalDateTime createdBefore,
      long start, long limit);

  /*어드민 페이지에서 그룹 노트 리스트 조회*/
  NoteSearchResultDTO findGroupNoteByAdmin(String groupName, String title,
      LocalDateTime createdAfter, LocalDateTime createdBefore,
      long start, long limit);

  /*삭제할 노트 리스트 조회*/
  List<Note> findNotesToDelete(List<Long> ids);
}
