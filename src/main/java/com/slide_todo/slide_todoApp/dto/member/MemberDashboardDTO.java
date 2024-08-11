package com.slide_todo.slide_todoApp.dto.member;

import static java.util.Comparator.comparing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class MemberDashboardDTO {

  private Long id;
  private String nickname;
  private String email;
  @JsonProperty("individual_goals")
  private List<GoalInDashboardDTO> individualGoals;
  private List<GroupInDashboardDTO> groups;

  public MemberDashboardDTO(Member member, List<Group> groups
  ) {
    this.id = member.getId();
    this.nickname = member.getNickname();
    this.email = member.getEmail();
    this.individualGoals = member.getIndividualGoals().stream()
        .map(GoalInDashboardDTO::new)
        .sorted(comparing(GoalInDashboardDTO::getCreatedAt).reversed())
        .toList();
    this.groups = groups.stream()
        .map(GroupInDashboardDTO::new)
        .sorted(comparing(GroupInDashboardDTO::getCreatedAt).reversed())
        .toList();
  }


  @Data
  public static class GoalInDashboardDTO {

    private Long id;
    private String title;
    @JsonIgnore
    private LocalDateTime createdAt;

    public GoalInDashboardDTO(Goal goal) {
      this.id = goal.getId();
      this.title = goal.getTitle();
      this.createdAt = goal.getCreatedAt();
    }
  }

  @Data
  public static class GroupInDashboardDTO {

    private Long id;
    private String title;
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonProperty("group_goals")
    private List<GoalInDashboardDTO> groupGoals;

    public GroupInDashboardDTO(Group group) {
      this.id = group.getId();
      this.title = group.getTitle();
      this.createdAt = group.getCreatedAt();
      this.groupGoals = group.getGroupGoals().stream()
          .map(GoalInDashboardDTO::new)
          .sorted(comparing(GoalInDashboardDTO::getCreatedAt).reversed())
          .toList();
    }
  }
}
