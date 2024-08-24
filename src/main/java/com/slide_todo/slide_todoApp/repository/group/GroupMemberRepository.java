package com.slide_todo.slide_todoApp.repository.group;

import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GroupMemberRepository extends JpaRepository<GroupMember,Long>, BaseGroupMemberRepository {
    List<GroupMember> findByGroup(Group group);
    List<GroupMember> findByMember(Member member);

    void deleteByGroup(Group group);

    void deleteByGroupAndMemberId(Group group, Long memberId);

    List<GroupMember> findAllByGroupId(Long groupId);

    /*그룹 리더가 계정 삭제 시, 다음 그룹 리더를 찾기 위한 메소드*/
    @Query("SELECT gm FROM GroupMember  gm"
        + " LEFT JOIN FETCH gm.member"
        + " WHERE gm.group.id = :groupId"
        + " ORDER BY gm.id ASC")
    GroupMember findFirstByGroupId(Long groupId);
}
