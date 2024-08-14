package com.slide_todo.slide_todoApp.dto.jwt;

import lombok.Data;

@Data
public class TokenPairDTO {

  private String accessToken;
  private String refreshToken;

  public TokenPairDTO(String access, String refresh) {
    this.accessToken = access;
    this.refreshToken = refresh;
  }
}

