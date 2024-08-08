package com.slide_todo.slide_todoApp.dto.note;

import com.slide_todo.slide_todoApp.domain.note.Note;
import java.util.List;
import lombok.Data;

@Data
public class NoteSearchResultDTO {

  private List<Note> notes;
  private long totalCount;

  public NoteSearchResultDTO(List<Note> notes, long totalCount) {
    this.notes = notes;
    this.totalCount = totalCount;
  }
}
