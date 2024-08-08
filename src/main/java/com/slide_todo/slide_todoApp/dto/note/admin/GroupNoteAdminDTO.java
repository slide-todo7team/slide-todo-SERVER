package com.slide_todo.slide_todoApp.dto.note.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.note.Note;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class GroupNoteAdminDTO {

  @JsonProperty("total_count")
  private Long totalCount;
  @JsonProperty("current_page")
  private Long currentPage;
  @JsonProperty("notes")
  private List<GroupNoteInListDTO> notes;

  public GroupNoteAdminDTO(Long totalCount, Long currentPage, List<Note> notes) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.notes = notes.stream().map(GroupNoteInListDTO::new).toList();
  }



  @Data
  public static class GroupNoteInListDTO {

    @JsonProperty("group")
    private GroupInNoteDTO groupInNoteDTO;
    private Long id;
    private String title;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public GroupNoteInListDTO(Note groupNote) {
      this.id = groupNote.getId();
      this.title = groupNote.getTitle();
      this.createdAt = groupNote.getCreatedAt();
      this.updatedAt = groupNote.getUpdatedAt();
    }
  }


  @Data
  public static class GroupInNoteDTO {

    private Long id;
    private String title;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
  }
}
