package com.slide_todo.slide_todoApp.dto.member.admin;

import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class AdminMemberDetailDTO {

  private Long id;
  private String email;
  private String nickname;
  private String role;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<GroupInMemberDetailDTO> groups;

  public AdminMemberDetailDTO(Member member) {
    this.id = member.getId();
    this.email = member.getEmail();
    this.nickname = member.getNickname();
    this.role = member.getRole().name();
    this.createdAt = member.getCreatedAt();
    this.updatedAt = member.getUpdatedAt();
    this.groups = member.getGroupMembers().stream().map(GroupInMemberDetailDTO::new).toList();
  }


  @Data
  public static class GroupInMemberDetailDTO {

    private Long id;
    private String title;
    private LocalDateTime registerAt;

    public GroupInMemberDetailDTO(GroupMember groupMember) {
      this.title = groupMember.getGroup().getTitle();
      this.id = groupMember.getGroup().getId();
      this.registerAt = groupMember.getRegisteredAt();
    }
  }
}
