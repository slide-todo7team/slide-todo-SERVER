package com.slide_todo.slide_todoApp.dto.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RefreshTokenDTO {

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonCreator
  public RefreshTokenDTO(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
