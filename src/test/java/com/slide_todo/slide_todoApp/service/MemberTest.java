package com.slide_todo.slide_todoApp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.slide_todo.slide_todoApp.TestGenerator;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.member.MemberRole;
import com.slide_todo.slide_todoApp.dto.jwt.TokenPairDTO;
import com.slide_todo.slide_todoApp.dto.member.SigninDTO;
import com.slide_todo.slide_todoApp.dto.member.SignupDTO;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.service.member.MemberService;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
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
  @Autowired
  private JwtProvider jwtProvider;

  @Test
  @Transactional
  public void 회원가입1() throws Exception {
    /*given*/
    String password = generator.generateRandomString(10);
    Member expected = memberRepository.save(generator.createMember(password));

    /*when*/
    Member result = memberRepository.findByMemberId(expected.getId());

    /*then*/
    assertEquals(expected, result);
  }


  @Test
  @Transactional
  public void 회원가입2() throws Exception {
    /*given*/
    String password = generator.generateRandomString(10);
    SignupDTO request = new SignupDTO(
        generator.generateRandomEmail(),
        password,
        generator.generateRandomString(10),
        generator.generateRandomString(10),
        MemberRole.USER
    );

    Long savedId = jwtProvider.getMemberIdFromToken(
        memberService.signup(request).getData().getAccessToken()
    );

    /*when*/
    Member result = memberRepository.findByMemberId(savedId);

    /*then*/
    assertEquals(request.getEmail(), result.getEmail());
    assertEquals(request.getName(), result.getName());
    assertEquals(request.getNickname(), result.getNickname());
    assertEquals(request.getRole(), result.getRole());
    assertNotEquals(request.getPassword(), result.getPassword());
  }


  @Test
  @Transactional
  public void 로그인() throws Exception {
    /*given*/
    String password = generator.generateRandomString(10);
    Member expected = memberRepository.save(generator.createMember(password));

    /*when*/
    SigninDTO request = new SigninDTO(expected.getEmail(), password);
    TokenPairDTO result = memberService.signin(request).getData();

    /*then*/
    Long memberId = jwtProvider.getMemberIdFromToken(result.getAccessToken());
    Member resultMember = memberRepository.findByMemberId(memberId);
    assertEquals(expected, resultMember);
  }


  @Test
  @Transactional
  public void 닉네임_중복_확인() throws Exception {
    /*given*/
    Member testMember = memberRepository.save(generator.createMember());
    String nickname = testMember.getNickname();
    String newNickname;
    do {
      newNickname = generator.generateRandomString(10);
    } while (testMember.getNickname().equals(newNickname));

    /*when*/
    Boolean result1 = memberService.checkNickname(nickname).getData().isDuplicated();
    Boolean result2 = memberService.checkNickname(newNickname).getData().isDuplicated();

    /*then*/
    assertEquals(Boolean.TRUE, result1);
    assertEquals(Boolean.FALSE, result2);
  }


  @Test
  @Transactional
  public void 로그아웃() throws Exception {
    /*given*/
    String password = generator.generateRandomString(10);
    Member expected = memberRepository.save(generator.createMember(password));

    SigninDTO request = new SigninDTO(expected.getEmail(), password);
    TokenPairDTO tokenPairDTO = memberService.signin(request).getData();

    /*when*/
    memberService.logout(tokenPairDTO);

    Exception exception = assertThrows(
        CustomException.class, () ->
            jwtProvider.getMemberIdFromToken(tokenPairDTO.getAccessToken())
    );

    /*then*/
    assertEquals(
        Exceptions.BLACKLISTED_TOKEN.getMsg(),
        exception.getMessage()
    );
  }

  @Test
  @Transactional
  public void 회원탈퇴() throws Exception {
    /*given*/
    Member member = memberRepository.save(generator.createMember());

    /*when*/
    memberService.deleteMember(member.getId());

    Exception exception = assertThrows(
        CustomException.class, () -> memberRepository.findByMemberId(member.getId())
    );

    /*then*/
    assertEquals(
        Exceptions.MEMBER_NOT_FOUND.getMsg(),
        exception.getMessage()
    );
  }
}
