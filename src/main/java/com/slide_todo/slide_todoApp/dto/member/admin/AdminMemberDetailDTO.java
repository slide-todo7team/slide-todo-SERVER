package com.slide_todo.slide_todoApp.dto.member.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.member.Member;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class AdminMemberDetailDTO {

  private Long id;
  private String email;
  private String nickname;
  private String role;
  @JsonProperty("created_at")
  private LocalDateTime createdAt;
  @JsonProperty("updated_at")
  private LocalDateTime updatedAt;
  @JsonProperty("groups")
  private List<GroupInMemberDetailDTO> groups;
  @JsonProperty("individual_goals")
  private List<GoalInMemberDetailDTO> goals;

  public AdminMemberDetailDTO(Member member) {
    this.id = member.getId();
    this.email = member.getEmail();
    this.nickname = member.getNickname();
    this.role = member.getRole().name();
    this.createdAt = member.getCreatedAt();
    this.updatedAt = member.getUpdatedAt();
    this.groups = member.getGroupMembers().stream().map(GroupInMemberDetailDTO::new).toList();
    this.goals = member.getIndividualGoals().stream().map(GoalInMemberDetailDTO::new).toList();
  }


  @Data
  public static class GroupInMemberDetailDTO {

    private Long id;
    private String title;
    @JsonProperty("register_at")
    private LocalDateTime registerAt;

    public GroupInMemberDetailDTO(GroupMember groupMember) {
      this.title = groupMember.getGroup().getTitle();
      this.id = groupMember.getGroup().getId();
      this.registerAt = groupMember.getRegisteredAt();
    }
  }


  @Data
  public static class GoalInMemberDetailDTO {

    private Long id;
    private String title;
    @JsonProperty("progress_rate")
    private BigDecimal progressRate;

    public GoalInMemberDetailDTO(IndividualGoal goal) {
      this.id = goal.getId();
      this.title = goal.getTitle();
      this.progressRate = goal.getProgressRate();
    }
  }
}
