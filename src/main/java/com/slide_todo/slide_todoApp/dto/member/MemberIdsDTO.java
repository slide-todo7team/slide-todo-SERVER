package com.slide_todo.slide_todoApp.dto.member;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import lombok.Data;

@Data
public class MemberIdsDTO {

  private List<Long> memberIds;

  @JsonCreator
  public MemberIdsDTO(List<Long> memberIds) {
    this.memberIds = memberIds;
  }
}
