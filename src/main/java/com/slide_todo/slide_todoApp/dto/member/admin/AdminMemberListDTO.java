package com.slide_todo.slide_todoApp.dto.member.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.member.Member;
import java.util.List;
import lombok.Data;

@Data
public class AdminMemberListDTO {

  @JsonProperty("total_count")
  private Long totalCount;
  @JsonProperty("current_page")
  private Long currentPage;
  private List<AdminMemberListRowDTO> members;

  public AdminMemberListDTO(Long totalCount, Long currentPage, List<Member> members) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.members = members.stream().map(AdminMemberListRowDTO::new).toList();
  }
}
