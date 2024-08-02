package com.slide_todo.slide_todoApp.repository.member;

import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.member.MemberRole;
import java.util.List;

public interface BaseMemberRepository {

  Member findByMemberId(Long memberId);

  Member findByEmail(String email);

  Member findByNickname(String nickname);

  Boolean existsByNickname(String nickname);

  Long countByRole(MemberRole role);

  List<Member> findByRoll(MemberRole role, int page, int limit);

  Long countAll();

  List<Member> findAll(int page, int limit);
}
