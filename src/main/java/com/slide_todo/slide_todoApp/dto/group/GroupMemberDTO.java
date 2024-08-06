package com.slide_todo.slide_todoApp.dto.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import lombok.Data;

@Data
public class GroupMemberDTO {

  private Long id;
  @JsonProperty("member_id")
  private Long memberId;
  private String nickname;
  @JsonProperty("is_leader")
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
