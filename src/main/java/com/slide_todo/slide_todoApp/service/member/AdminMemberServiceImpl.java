package com.slide_todo.slide_todoApp.service.member;

import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.dto.jwt.TokenPairDTO;
import com.slide_todo.slide_todoApp.dto.member.SigninDTO;
import com.slide_todo.slide_todoApp.dto.member.admin.AdminMemberDetailDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberIdsDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberSearchResultDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberUpdateDTO;
import com.slide_todo.slide_todoApp.dto.member.admin.AdminMemberListDTO;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
import com.slide_todo.slide_todoApp.util.jwt.TokenType;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminMemberServiceImpl implements AdminMemberService {

  private final MemberRepository memberRepository;
  private final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  private final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  @Override
  public ResponseDTO<TokenPairDTO> adminSignin(SigninDTO request) {
    Member member = memberRepository.findByEmail(request.getEmail());

    if (member.getRole().name().equals("USER")) {
      throw new CustomException(Exceptions.ADMIN_ONLY);
    }

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
  public ResponseDTO<AdminMemberListDTO> getAllMembers(long page, long limit,
      String nickname, String email, String createdAfter, String createdBefore) {

    AdminMemberListDTO searchResult = searchMembers(page, limit, nickname, email, createdAfter,
        createdBefore);

    return new ResponseDTO<>(searchResult, Responses.OK);
  }

  @Override
  public ResponseDTO<AdminMemberDetailDTO> getMemberDetail(Long memberId) {
    Member member = memberRepository.findMemberWithGoalAndGroupMember(memberId);
    return new ResponseDTO<>(new AdminMemberDetailDTO(member), Responses.OK);

  }

  @Override
  @Transactional
  public ResponseDTO<AdminMemberListDTO> deleteMembers(MemberIdsDTO request) {
    List<Long> ids = request.getMemberIds();

    List<Member> membersToDelete = memberRepository.findMembersToDelete(ids);
    for (Member m : membersToDelete) {
      m.deleteMember();
    }

    return new ResponseDTO<>(null, Responses.NO_CONTENT);
  }

  @Override
  @Transactional
  public ResponseDTO<AdminMemberDetailDTO> updateMember(Long memberId, MemberUpdateDTO request) {
    Member member = memberRepository.findByMemberId(memberId);

    if (request.getEmail() != null) {
      validateEmail(request.getEmail());
    }
    if (request.getNickname() != null) {
      if (!request.getNickname().equals(member.getNickname())) {
        validateNickname(request.getNickname());
      }
    }

    member.updateMember(request.getEmail(), request.getNickname());
    return new ResponseDTO<>(new AdminMemberDetailDTO(member), Responses.OK);
  }

  private AdminMemberListDTO searchMembers(long page, long limit,
      String nickname, String email, String createdAfter, String createdBefore) {
    long start;
    if (limit != 0) {
      start = (page - 1) * limit;
    } else {
      start = 0L;
      limit = memberRepository.count();
    }
    LocalDateTime parsedCreatedAfter;
    LocalDateTime parsedCreatedBefore;
    if (createdAfter != null) {
      parsedCreatedAfter = LocalDate.parse(createdAfter.replace(" ", ""))
          .atStartOfDay();
    } else {
      parsedCreatedAfter = null;
    }

    if (createdBefore != null) {
      parsedCreatedBefore = LocalDate.parse(createdBefore.replace(" ", ""))
          .atStartOfDay().plusDays(1);
    } else {
      parsedCreatedBefore = null;
    }

    MemberSearchResultDTO searchResult = memberRepository.findByNicknameAndEmailAndCreatedAt(
        nickname, email,
        parsedCreatedAfter, parsedCreatedBefore, start, limit);

    return new AdminMemberListDTO(searchResult.getTotalCount(), page, searchResult.getMembers());

//    return new MemberListDTO(searchResult.getTotalCount(), page, searchResult.getMembers());
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

  /*닉네임 중복 검사*/
  private void validateNickname(String nickname) {
    if (memberRepository.existsByNickname(nickname)) {
      throw new CustomException(Exceptions.EXIST_NICKNAME);
    }
  }
}
