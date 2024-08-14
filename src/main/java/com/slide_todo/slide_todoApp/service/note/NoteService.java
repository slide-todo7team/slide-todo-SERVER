package com.slide_todo.slide_todoApp.service.note;

import com.slide_todo.slide_todoApp.dto.note.NoteCreateDTO;
import com.slide_todo.slide_todoApp.dto.note.NoteUpdateDTO;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;

public interface NoteService {

  /*노트 작성*/
  ResponseDTO<?> createNote(Long memberId, NoteCreateDTO request);

  /*노트 수정*/
  ResponseDTO<?> updateNote(Long memberId, Long noteId, NoteUpdateDTO request);

  /*노트 삭제*/
  ResponseDTO<?> deleteNote(Long memberId, Long noteId);

  /*단일 노트 조회*/
  ResponseDTO<?> getOneNote(Long memberId, Long noteId);

  /*목표에 따른 노트 리스트 조회*/
  ResponseDTO<?> getNotesByGoal(Long memberId, Long goalId, Long page, Long limit);
}
