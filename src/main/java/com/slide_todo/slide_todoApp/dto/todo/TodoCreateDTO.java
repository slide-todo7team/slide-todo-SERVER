package com.slide_todo.slide_todoApp.dto.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TodoCreateDTO {

  @JsonProperty("goal_id")
  private Long goalId;
  private String title;
  @JsonProperty("link_url")
  private String linkUrl;

  public TodoCreateDTO(Long goalId, String title, String linkUrl) {
    this.goalId = goalId;
    this.title = title;
    this.linkUrl = linkUrl;
  }
}
