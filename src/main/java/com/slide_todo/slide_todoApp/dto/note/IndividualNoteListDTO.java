package com.slide_todo.slide_todoApp.dto.note;

import com.slide_todo.slide_todoApp.domain.note.Note;
import java.util.List;
import lombok.Data;

@Data
public class IndividualNoteListDTO {

  private Long totalCount;
  private Long currentPage;
  private List<IndividualNoteDTO> notes;

  public IndividualNoteListDTO(Long totalCount, Long currentPage, List<Note> notes) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.notes = notes.stream().map(IndividualNoteDTO::new).toList();
  }

}
