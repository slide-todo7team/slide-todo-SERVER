package com.slide_todo.slide_todoApp.dto.group;

import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import lombok.Data;

@Data
public class GroupMemberDTO {

  private Long id;
  private Long memberId;
  private String nickname;
  private Boolean isLeader;
  private String color;

  public GroupMemberDTO(GroupMember groupMember) {
    this.id = groupMember.getId();
    this.memberId = groupMember.getMember().getId();
    this.nickname = groupMember.getMember().getNickname();
    this.isLeader = groupMember.getIsLeader();
    this.color = groupMember.getColor().getHexCode();
  }
}
