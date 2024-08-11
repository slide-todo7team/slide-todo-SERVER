package com.slide_todo.slide_todoApp.repository.member;

import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.dto.member.MemberSearchResultDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface BaseMemberRepository {

  Member findByMemberId(Long memberId);

  Member findByEmail(String email);

  Boolean existsByEmail(String email);

  Member findByNickname(String nickname);

  Boolean existsByNickname(String nickname);

  MemberSearchResultDTO findByNicknameAndEmailAndCreatedAt(
      String nickname, String email, LocalDateTime createdAfter,
      LocalDateTime createdBefore, long start, long limit
  );

  Member findMemberWithGoalAndGroupMember(Long memberId);

  List<Member> findMembersToDelete(List<Long> ids);
}
