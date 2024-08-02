package com.slide_todo.slide_todoApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenDTO {
  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;

  public TokenDTO(String access, String refresh) {
    this.accessToken = access;
    this.refreshToken = refresh;
  }
}

