package com.slide_todo.slide_todoApp.repository.group;

import com.slide_todo.slide_todoApp.domain.group.GroupMember;

public interface BaseGroupMemberRepository {

  GroupMember findByMemberIdAndGroupId(Long memberId, Long groupId);
}
