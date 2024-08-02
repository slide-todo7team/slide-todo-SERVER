package com.slide_todo.slide_todoApp.dto.member;

import com.slide_todo.slide_todoApp.domain.member.MemberRole;
import lombok.Data;

@Data
public class SignupDTO {

  private String email;
  private String password;
  private String name;
  private String nickname;
  private MemberRole role;

  public SignupDTO(String email, String password, String name, String nickname, MemberRole role) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.nickname = nickname;
    this.role = role;
  }
}
