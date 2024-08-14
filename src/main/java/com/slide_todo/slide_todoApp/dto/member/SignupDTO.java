package com.slide_todo.slide_todoApp.dto.member;

import lombok.Data;

@Data
public class SignupDTO {

  private String email;
  private String password;
  private String nickname;

  public SignupDTO(String email, String password, String nickname) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
  }
}
