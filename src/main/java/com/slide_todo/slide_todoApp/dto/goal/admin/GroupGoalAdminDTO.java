package com.slide_todo.slide_todoApp.dto.goal.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class GroupGoalAdminDTO {

  @JsonProperty("total_count")
  private Long totalCount;
  @JsonProperty("current_page")
  private Long currentPage;
  @JsonProperty("goals")
  private List<GroupGoalInListDTO> goals;

  public GroupGoalAdminDTO(Long totalCount, Long currentPage, List<GroupGoal> goals) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.goals = goals.stream().map(GroupGoalInListDTO::new).toList();
  }


  @Data
  public static class GroupGoalInListDTO {

    @JsonProperty("group")
    private GroupInGoalDTO groupInGoalDTO;
    private Long id;
    private String title;
    @JsonProperty("progress_rate")
    private BigDecimal progressRate;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public GroupGoalInListDTO(GroupGoal goal) {
      this.groupInGoalDTO = new GroupInGoalDTO(goal.getGroup());
      this.id = goal.getId();
      this.title = goal.getTitle();
      this.createdAt = goal.getCreatedAt();
      this.updatedAt = goal.getUpdatedAt();
    }
  }


  @Data
  public static class GroupInGoalDTO {

    private Long id;
    private String title;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public GroupInGoalDTO(Group group) {
      this.id = group.getId();
      this.title = group.getTitle();
      this.createdAt = group.getCreatedAt();
      this.updatedAt = group.getUpdatedAt();
    }
  }
}
