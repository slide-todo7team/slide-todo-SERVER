package com.slide_todo.slide_todoApp.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.member.MemberRole;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class MemberListDTO {

  @JsonProperty("total_count")
  private Long totalCount;
  @JsonProperty("current_page")
  private Long currentPage;
  private List<MemberInListDTO> members;

  public MemberListDTO(Long totalCount, Long currentPage, List<Member> members) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.members = members.stream().map(MemberInListDTO::new).toList();
  }

  @Data
  public static class MemberInListDTO {

    private Long id;
    private String email;
    private String name;
    private String nickname;
    private MemberRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MemberInListDTO(Member member) {
      this.id = member.getId();
      this.email = member.getEmail();
      this.name = member.getName();
      this.nickname = member.getNickname();
      this.role = member.getRole();
      this.createdAt = member.getCreatedAt();
      this.updatedAt = member.getUpdatedAt();
    }
  }
}
