package com.slide_todo.slide_todoApp.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NicknameCheckDTO {

  @JsonProperty("is_duplicated")
  private boolean isDuplicated;

  public NicknameCheckDTO(boolean isDuplicated) {
    this.isDuplicated = isDuplicated;
  }
}
