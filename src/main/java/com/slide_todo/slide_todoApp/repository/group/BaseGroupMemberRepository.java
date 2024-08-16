package com.slide_todo.slide_todoApp.repository.group;

import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import java.util.List;
import java.util.Map;

public interface BaseGroupMemberRepository {

  GroupMember findByMemberIdAndGroupId(Long memberId, Long groupId);

  /*그룹 할 일의 ID 목록으로 담당자 조회*/
  Map<Long, GroupMember> findAllByGroupTodoIds(List<Long> groupTodoIds);
}
