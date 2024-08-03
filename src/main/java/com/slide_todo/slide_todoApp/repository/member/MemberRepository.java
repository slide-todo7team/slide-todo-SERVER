package com.slide_todo.slide_todoApp.repository.member;

import com.slide_todo.slide_todoApp.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, BaseMemberRepository {

}
