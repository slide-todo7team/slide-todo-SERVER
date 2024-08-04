package com.slide_todo.slide_todoApp.repository.group;

import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Long> {
    List<GroupMember> findByGroup(Group group);

    List<GroupMember> findByMember(Member member);

    void deleteByGroup(Group group);

    void deleteByGroupAndMemberId(Group group, Long memberId);
}
