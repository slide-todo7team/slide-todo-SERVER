package com.slide_todo.slide_todoApp.dto.member;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class MemberUpdateDTO {

  private String email;
  private String name;
  private String nickname;

  public MemberUpdateDTO(@Nullable String email, @Nullable String name,
      @Nullable String nickname) {
    this.email = email;
    this.name = name;
    this.nickname = nickname;
  }
}
