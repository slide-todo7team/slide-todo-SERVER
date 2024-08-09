package com.slide_todo.slide_todoApp.dto.member.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.member.Member;
import java.time.LocalDate;
import lombok.Data;

@Data
public class AdminMemberListRowDTO {

  private Long id;
  private String nickname;
  private String email;
  @JsonProperty("created_at")
  private LocalDate createdAt;
  @JsonProperty("updated_at")
  private LocalDate updatedAt;
  @JsonProperty("group_count")
  private long groupCount;

  public AdminMemberListRowDTO(Member member) {
    this.id = member.getId();
    this.nickname = member.getNickname();
    this.email = member.getEmail();
    this.createdAt = member.getCreatedAt().toLocalDate();
    this.updatedAt = member.getUpdatedAt().toLocalDate();
    this.groupCount = member.getGroupMembers().size();
  }
}
