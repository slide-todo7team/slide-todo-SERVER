package com.slide_todo.slide_todoApp.dto.member;

import static java.util.Comparator.comparing;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class MemberSidebarDTO {

  private Long id;
  private String nickname;
  private String email;
  private List<GoalInSidebarDTO> individualGoals;
  private List<GroupInSidebarDTO> groups;

  public MemberSidebarDTO(Member member, List<Group> groups
  ) {
    this.id = member.getId();
    this.nickname = member.getNickname();
    this.email = member.getEmail();
    this.individualGoals = member.getIndividualGoals().stream()
        .map(GoalInSidebarDTO::new)
        .sorted(comparing(GoalInSidebarDTO::getCreatedAt).reversed())
        .toList();
    this.groups = groups.stream()
        .map(GroupInSidebarDTO::new)
        .sorted(comparing(GroupInSidebarDTO::getCreatedAt).reversed())
        .toList();
  }


  @Data
  public static class GoalInSidebarDTO {

    private Long id;
    private String title;
    private LocalDateTime createdAt;

    public GoalInSidebarDTO(Goal goal) {
      this.id = goal.getId();
      this.title = goal.getTitle();
      this.createdAt = goal.getCreatedAt();
    }
  }

  @Data
  public static class GroupInSidebarDTO {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private List<GoalInSidebarDTO> groupGoals;

    public GroupInSidebarDTO(Group group) {
      this.id = group.getId();
      this.title = group.getTitle();
      this.createdAt = group.getCreatedAt();
      this.groupGoals = group.getGroupGoals().stream()
          .map(GoalInSidebarDTO::new)
          .sorted(comparing(GoalInSidebarDTO::getCreatedAt).reversed())
          .toList();
    }
  }
}
