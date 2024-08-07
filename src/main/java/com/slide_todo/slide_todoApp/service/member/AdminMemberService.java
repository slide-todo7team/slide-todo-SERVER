package com.slide_todo.slide_todoApp.service.member;

import com.slide_todo.slide_todoApp.dto.member.MemberDetailDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberIdsDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberListDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberUpdateDTO;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import jakarta.annotation.Nullable;

public interface AdminMemberService {

  /*어드민 페이지에서 유저 리스트 조회*/
  ResponseDTO<MemberListDTO> getAllMembers(long page, long limit, @Nullable String name, @Nullable String nickname,
      @Nullable String email, @Nullable String createdAfter, @Nullable String createdBefore);

  /*어드민 페이지에서 유저 상세정보 조회*/
  ResponseDTO<MemberDetailDTO> getMemberDetail(Long memberId);

  /*복수 유저 삭제*/
  ResponseDTO<MemberListDTO> deleteMembers(MemberIdsDTO request, long page, long limit, String name,
      String nickname, String email, String createdAfter, String createdBefore);

  /*유저 데이터 수정*/
  ResponseDTO<MemberDetailDTO> updateMember(Long memberId, MemberUpdateDTO request);
}
