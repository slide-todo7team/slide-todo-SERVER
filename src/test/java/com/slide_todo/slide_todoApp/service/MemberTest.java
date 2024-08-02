package com.slide_todo.slide_todoApp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.slide_todo.slide_todoApp.TestGenerator;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemberTest {

  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private MemberService memberService;
  @Autowired
  private TestGenerator generator;

  @Test
  @Transactional
  public void 회원가입() throws Exception {
    /*given*/
    String password = generator.generateRandomString(10);
    Member expected = memberRepository.save(generator.createMember(password));

    /*when*/
    Member result = memberRepository.findByMemberId(expected.getId());

    /*then*/
    assertEquals(expected, result);
  }
}
