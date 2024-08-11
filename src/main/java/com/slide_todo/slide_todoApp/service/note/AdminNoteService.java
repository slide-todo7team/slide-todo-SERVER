package com.slide_todo.slide_todoApp.service.note;

import com.slide_todo.slide_todoApp.domain.note.admin.AdminNoteDetailDTO;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;

public interface AdminNoteService {

  ResponseDTO<AdminNoteDetailDTO> getNoteDetailByAdmin(Long noteId);

  ResponseDTO<?> deleteNoteByAdmin(Long noteId);

}
