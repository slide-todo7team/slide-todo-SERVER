package com.slide_todo.slide_todoApp.dto.member.admin;

import com.slide_todo.slide_todoApp.domain.member.Member;
import java.util.List;
import lombok.Data;

@Data
public class AdminMemberListDTO {

  private Long totalCount;
  private Long searchedCount;
  private Long currentPage;
  private List<AdminMemberListRowDTO> members;

  public AdminMemberListDTO(Long totalCount, Long searchedCount, Long currentPage, List<Member> members) {
    this.totalCount = totalCount;
    this.searchedCount = searchedCount;
    this.currentPage = currentPage;
    this.members = members.stream().map(AdminMemberListRowDTO::new).toList();
  }
}
