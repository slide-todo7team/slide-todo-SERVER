package com.slide_todo.slide_todoApp.dto.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

@Data
public class RefreshTokenDTO {

  private String refreshToken;

  @JsonCreator
  public RefreshTokenDTO(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
