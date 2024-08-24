package com.slide_todo.slide_todoApp.dto.member;

import com.slide_todo.slide_todoApp.domain.member.Member;
import java.util.List;
import lombok.Data;

@Data
public class MemberSearchResultDTO {

  private List<Member> members;
  private long searchedCount;

  public MemberSearchResultDTO(List<Member> members, long searchedCount) {
    this.members = members;
    this.searchedCount = searchedCount;
  }
}
