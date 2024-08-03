package com.slide_todo.slide_todoApp.dto.member;

import lombok.Data;

@Data
public class SigninDTO {

    private String email;
    private String password;

    public SigninDTO(String email, String password) {
      this.email = email;
      this.password = password;
    }
}
