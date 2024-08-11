package com.slide_todo.slide_todoApp.dto.member;

import com.slide_todo.slide_todoApp.domain.member.Member;
import lombok.Data;

/**
 * 간단한 유저 정보를 조회하는 DTO
 */
@Data
public class MemberInfoDTO {

  private Long id;
  private String nickname;
  private String email;

  public MemberInfoDTO(Member member) {
    this.id = member.getId();
    this.nickname = member.getNickname();
    this.email = member.getEmail();
  }
}
