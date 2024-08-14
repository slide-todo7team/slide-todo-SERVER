package com.slide_todo.slide_todoApp.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DuplicationCheckDTO {

  private boolean isDuplicated;

  public DuplicationCheckDTO(boolean isDuplicated) {
    this.isDuplicated = isDuplicated;
  }
}
