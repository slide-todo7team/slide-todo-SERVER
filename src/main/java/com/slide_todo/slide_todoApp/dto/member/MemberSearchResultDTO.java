package com.slide_todo.slide_todoApp.dto.member;

import com.slide_todo.slide_todoApp.domain.member.Member;
import java.util.List;
import lombok.Data;

@Data
public class MemberSearchResultDTO {

  private List<Member> members;
  private long totalCount;

  public MemberSearchResultDTO(List<Member> members, long totalCount) {
    this.members = members;
    this.totalCount = totalCount;
  }
}
