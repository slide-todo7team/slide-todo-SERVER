package com.slide_todo.slide_todoApp.service.member;

import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.dto.member.MemberDetailDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberIdsDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberListDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberSearchResultDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberUpdateDTO;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import com.slide_todo.slide_todoApp.util.response.Responses;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminMemberServiceImpl implements AdminMemberService {

  private final MemberRepository memberRepository;

  @Override
  public ResponseDTO<MemberListDTO> getAllMembers(long page, long limit, String name,
      String nickname, String email, String createdAfter, String createdBefore) {

    MemberListDTO searchResult = searchMembers(page, limit, name, nickname, email, createdAfter, createdBefore);

    return new ResponseDTO<>(searchResult, Responses.OK);
  }

  @Override
  public ResponseDTO<MemberDetailDTO> getMemberDetail(Long memberId) {
    Member member = memberRepository.findMemberWithGoalAndGroupMember(memberId);
    return new ResponseDTO<>(new MemberDetailDTO(member), Responses.OK);
  }

  @Override
  public ResponseDTO<MemberListDTO> deleteMembers(MemberIdsDTO request, long page, long limit, String name,
      String nickname, String email, String createdAfter, String createdBefore) {
    List<Long> ids = request.getMemberIds();

    List<Member> membersToDelete = memberRepository.findMembersToDelete(ids);
    for (Member m : membersToDelete) {
      m.deleteMember();
    }

    MemberListDTO searchResult = searchMembers(page, limit, name, nickname, email, createdAfter, createdBefore);

    return new ResponseDTO<>(searchResult, Responses.OK);
  }

  @Override
  public ResponseDTO<MemberDetailDTO> updateMember(Long memberId, MemberUpdateDTO request) {
    Member member = memberRepository.findByMemberId(memberId);
    member.updateMember(request.getEmail(), request.getName(), request.getNickname());
    return new ResponseDTO<>(new MemberDetailDTO(member), Responses.OK);
  }

  private MemberListDTO searchMembers(long page, long limit, String name,
      String nickname, String email, String createdAfter, String createdBefore) {
    long start;
    if (limit != 0) {
      start = (page - 1) * limit;
    } else {
      start = 0L;
      limit = memberRepository.count();
    }

    LocalDateTime parsedCreatedAfter = LocalDate.parse(createdAfter.replace(" ", ""))
        .atStartOfDay();
    LocalDateTime parsedCreatedBefore = LocalDate.parse(createdBefore.replace(" ", ""))
        .atStartOfDay().plusDays(1);
    MemberSearchResultDTO searchResult = memberRepository.findByNameAndNicknameAndEmailAndCreatedAt(
        name, nickname, email,
        parsedCreatedAfter, parsedCreatedBefore, start, limit);

    return new MemberListDTO(searchResult.getTotalCount(), page, searchResult.getMembers());
  }
}
