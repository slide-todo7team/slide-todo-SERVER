package com.slide_todo.slide_todoApp.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class MemberDetailDTO {

  private Long id;
  private String email;
  private String name;
  private String nickname;
  private String role;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  @JsonProperty("goals")
  private List<GroupInMemberDetailDTO> groups;
  @JsonProperty("goals")
  private List<GoalInMemberDetailDTO> goals;

  public MemberDetailDTO(Member member) {
    this.id = member.getId();
    this.email = member.getEmail();
    this.name = member.getName();
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

    public GoalInMemberDetailDTO(Goal goal) {
      this.id = goal.getId();
      this.title = goal.getTitle();
    }
  }
}
