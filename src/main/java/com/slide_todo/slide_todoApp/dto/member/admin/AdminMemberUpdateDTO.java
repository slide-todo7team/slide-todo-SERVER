package com.slide_todo.slide_todoApp.dto.member.admin;

import com.slide_todo.slide_todoApp.domain.member.Member;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AdminMemberUpdateDTO {

  private Long id;
  private String email;
  private String nickname;
  private LocalDateTime createdAt;

  public AdminMemberUpdateDTO (Member member) {
    this.id = member.getId();
    this.email = member.getEmail();
    this.nickname = member.getNickname();
    this.createdAt = member.getCreatedAt();
  }
}
