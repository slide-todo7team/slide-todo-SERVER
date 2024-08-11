package com.slide_todo.slide_todoApp.service.note;

import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.note.Note;
import com.slide_todo.slide_todoApp.domain.note.admin.AdminNoteDetailDTO;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import com.slide_todo.slide_todoApp.repository.note.NoteRepository;
import com.slide_todo.slide_todoApp.repository.todo.TodoRepository;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminNoteServiceImpl implements AdminNoteService {

  private final NoteRepository noteRepository;
  private final TodoRepository todoRepository;

  @Override
  public ResponseDTO<AdminNoteDetailDTO> getNoteDetailByAdmin(Long noteId) {
    Note note = noteRepository.findByNoteId(noteId);
    Todo todo = note.getTodo();

    if (todo.getDtype().equals("G")) {
      GroupTodo groupTodo = todoRepository.findGroupTodoByNoteId(noteId);
      Member writer;
      if (groupTodo.getMemberInCharge() != null) {
        writer = groupTodo.getMemberInCharge().getMember();
      } else {
        writer = null;
      }
      return new ResponseDTO<>(new AdminNoteDetailDTO(note, writer), Responses.OK);
    }
    return new ResponseDTO<>(new AdminNoteDetailDTO(note, todo.getWriter()), Responses.OK);
  }

  @Override
  public ResponseDTO<?> deleteNoteByAdmin(Long noteId) {
    Note note = noteRepository.findByNoteId(noteId);
    note.deleteNote();
    return new ResponseDTO<>(null, Responses.NO_CONTENT);
  }
}
