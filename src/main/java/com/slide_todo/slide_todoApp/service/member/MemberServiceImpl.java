package com.slide_todo.slide_todoApp.service.member;

import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.dto.jwt.RefreshTokenDTO;
import com.slide_todo.slide_todoApp.dto.jwt.TokenPairDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberUpdateDTO;
import com.slide_todo.slide_todoApp.dto.member.NicknameCheckDTO;
import com.slide_todo.slide_todoApp.dto.member.SigninDTO;
import com.slide_todo.slide_todoApp.dto.member.SignupDTO;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
import com.slide_todo.slide_todoApp.util.jwt.TokenType;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
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
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  private final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
  private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);


  @Override
  @Transactional
  public ResponseDTO<TokenPairDTO> signup(SignupDTO request) {
    if (memberRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(Exceptions.REGISTERED_EMAIL);
    }
    if (memberRepository.existsByNickname(request.getNickname())) {
      throw new CustomException(Exceptions.EXIST_NICKNAME);
    }
    if (!pattern.matcher(request.getEmail()).matches()) {
      throw new CustomException(Exceptions.INVALID_EMAIL);
    }


    Member member = Member.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .name(request.getName())
        .nickname(request.getNickname())
        .role(request.getRole())
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
      throw new CustomException(Exceptions.INVALID_PASSWORD);
    }

    TokenPairDTO tokenPair = new TokenPairDTO(
        jwtProvider.createToken(member.getId(), TokenType.ACCESS),
        jwtProvider.createToken(member.getId(), TokenType.REFRESH)
    );

    return new ResponseDTO<>(tokenPair, Responses.OK);
  }

  @Override
  public ResponseDTO<NicknameCheckDTO> checkNickname(String nickname) {
    Boolean isExist = memberRepository.existsByNickname(nickname);
    return new ResponseDTO<>(new NicknameCheckDTO(isExist), Responses.OK);
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
    if (memberRepository.existsByNickname(request.getNickname())) {
      throw new CustomException(Exceptions.EXIST_NICKNAME);
    }
    if (memberRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(Exceptions.REGISTERED_EMAIL);
    }

    member.updateMember(request.getEmail(), request.getName(), request.getNickname());
    return new ResponseDTO<>(new MemberUpdateDTO(
        member.getEmail(), member.getName(), member.getNickname()
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
    memberRepository.delete(member);
    return new ResponseDTO<>(null, Responses.OK);
  }
}
