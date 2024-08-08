package com.slide_todo.slide_todoApp.dto.note.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.note.Note;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class IndividualNoteAdminDTO {

  @JsonProperty("total_count")
  private Long totalCount;
  @JsonProperty("current_page")
  private Long currentPage;
  @JsonProperty("notes")
  private List<IndividualNoteInListDTO> notes;

  public IndividualNoteAdminDTO(Long totalCount, Long currentPage, List<Note> notes) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.notes = notes.stream().map(IndividualNoteInListDTO::new).toList();
  }


  @Data
  public static class IndividualNoteInListDTO {

    @JsonProperty("member")
    private MemberInNoteDTO memberInNoteDTO;
    private Long id;
    private String title;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public IndividualNoteInListDTO(Note individualNote) {
      IndividualGoal goal = (IndividualGoal) individualNote.getTodo().getGoal();
      this.memberInNoteDTO = new MemberInNoteDTO(goal.getMember());
      this.id = individualNote.getId();
      this.title = individualNote.getTitle();
      this.createdAt = individualNote.getCreatedAt();
      this.updatedAt = individualNote.getUpdatedAt();
    }
  }


  @Data
  public static class MemberInNoteDTO {

    private Long id;
    private String nickname;

    public MemberInNoteDTO(Member member) {
      this.id = member.getId();
      this.nickname = member.getNickname();
    }
  }
}
