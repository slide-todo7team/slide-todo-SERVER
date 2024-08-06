package com.slide_todo.slide_todoApp.dto.note;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.note.Note;
import java.util.List;
import lombok.Data;

@Data
public class GroupNoteListDTO {

  @JsonProperty("total_count")
  private Long totalCount;
  @JsonProperty("current_page")
  private Long currentPage;
  private List<GroupNoteDTO> notes;

  public GroupNoteListDTO(Long totalCount, Long currentPage, List<Note> notes) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.notes = notes.stream().map(GroupNoteDTO::new).toList();
  }

}
