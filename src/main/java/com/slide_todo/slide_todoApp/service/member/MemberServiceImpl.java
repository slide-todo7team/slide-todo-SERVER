package com.slide_todo.slide_todoApp.service.member;

import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.dto.jwt.RefreshTokenDTO;
import com.slide_todo.slide_todoApp.dto.jwt.TokenPairDTO;
import com.slide_todo.slide_todoApp.dto.member.DuplicationCheckDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberSidebarDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberInfoDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberUpdateDTO;
import com.slide_todo.slide_todoApp.dto.member.SigninDTO;
import com.slide_todo.slide_todoApp.dto.member.SignupDTO;
import com.slide_todo.slide_todoApp.repository.group.GroupRepository;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
import com.slide_todo.slide_todoApp.util.jwt.TokenType;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final GroupRepository groupRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  private final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  private final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

  private final String PASSWORD_PATTERN =
      "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=!.])(?=\\S+$).{8,}$";
  private final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);


  @Override
  public ResponseDTO<MemberInfoDTO> getMemberInfo(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    return new ResponseDTO<>(new MemberInfoDTO(member), Responses.OK);
  }

  @Override
  public ResponseDTO<MemberSidebarDTO> getMemberSidebar(Long memberId) {
    Member member = memberRepository.findMemberWithGoalAndGroupMember(memberId);

    List<Long> groupIds = member.getGroupMembers().stream()
        .map(groupMember -> groupMember.getGroup().getId())
        .toList();

    List<Group> groups = groupRepository.findAllByGroupIds(groupIds);

    return new ResponseDTO<>(new MemberSidebarDTO(member, groups), Responses.OK);
  }

  @Override
  @Transactional
  public ResponseDTO<TokenPairDTO> signup(SignupDTO request) {
    validateEmail(request.getEmail());
    validateNickname(request.getNickname());
    validatePassword(request.getPassword());

    Member member = Member.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .nickname(request.getNickname())
        .build();
    memberRepository.save(member);

    TokenPairDTO tokenPair = new TokenPairDTO(
        jwtProvider.createToken(member.getId(), TokenType.ACCESS),
        jwtProvider.createToken(member.getId(), TokenType.REFRESH)
    );

    return new ResponseDTO<>(tokenPair, Responses.CREATED);
  }

  @Override
  public ResponseDTO<TokenPairDTO> signin(SigninDTO request) {
    Member member = memberRepository.findByEmail(request.getEmail());

    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
      throw new CustomException(Exceptions.WRONG_PASSWORD);
    }

    TokenPairDTO tokenPair = new TokenPairDTO(
        jwtProvider.createToken(member.getId(), TokenType.ACCESS),
        jwtProvider.createToken(member.getId(), TokenType.REFRESH)
    );

    return new ResponseDTO<>(tokenPair, Responses.OK);
  }

  @Override
  public ResponseDTO<DuplicationCheckDTO> checkNickname(String nickname) {
    Boolean isExist = memberRepository.existsByNickname(nickname);
    return new ResponseDTO<>(new DuplicationCheckDTO(isExist), Responses.OK);
  }

  @Override
  public ResponseDTO<DuplicationCheckDTO> checkEmail(String email) {
    Boolean isExist = memberRepository.existsByEmail(email);
    return new ResponseDTO<>(new DuplicationCheckDTO(isExist), Responses.OK);
  }

  @Override
  public ResponseDTO<TokenPairDTO> refreshToken(RefreshTokenDTO request) {
    TokenPairDTO response = jwtProvider.refreshAccessToken(request.getRefreshToken());

    return new ResponseDTO<>(response, Responses.OK);
  }

  @Override
  @Transactional
  public ResponseDTO<MemberUpdateDTO> updateMember(Long memberId, MemberUpdateDTO request) {
    Member member = memberRepository.findByMemberId(memberId);
    if (request.getEmail() != null) {
      if (!request.getEmail().equals(member.getEmail())) {
        validateEmail(request.getEmail());
      }
    }
    if (request.getNickname() != null) {
      if (!request.getNickname().equals(member.getNickname())) {
        validateNickname(request.getNickname());
      }
    }

    member.updateMember(request.getEmail(), request.getNickname());
    return new ResponseDTO<>(new MemberUpdateDTO(
        member.getEmail(),member.getNickname()
    ), Responses.OK);
  }

  @Override
  @Transactional
  public ResponseDTO<?> logout(TokenPairDTO request) {
    jwtProvider.logout(request.getAccessToken(), request.getRefreshToken());
    return new ResponseDTO<>(null, Responses.OK);
  }

  @Override
  @Transactional
  public ResponseDTO<?> deleteMember(Long memberId) {
    Member member = memberRepository.findByMemberId(memberId);
    member.deleteMember();
    return new ResponseDTO<>(null, Responses.OK);
  }

  /*이메일 유효성 및 중복 검사*/
  private void validateEmail(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new CustomException(Exceptions.REGISTERED_EMAIL);
    }
    if (!emailPattern.matcher(email).matches()) {
      throw new CustomException(Exceptions.INVALID_EMAIL);
    }
  }

  /*비밀번호 유효성 검사*/
  private void validatePassword(String password) {
    if (!passwordPattern.matcher(password).matches()) {
      throw new CustomException(Exceptions.INVALID_PASSWORD);
    }
  }

  /*닉네임 유효성 검사*/
  private void validateNickname(String nickname) {
    if (memberRepository.existsByNickname(nickname)) {
      throw new CustomException(Exceptions.EXIST_NICKNAME);
    }
    if (nickname.length() > 8) {
      throw new CustomException(Exceptions.NICKNAME_TOO_LONG);
    }
  }
}
