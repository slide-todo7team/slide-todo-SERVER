package com.slide_todo.slide_todoApp.service.member;

import com.slide_todo.slide_todoApp.dto.jwt.TokenPairDTO;
import com.slide_todo.slide_todoApp.dto.member.SigninDTO;
import com.slide_todo.slide_todoApp.dto.member.admin.AdminMemberDetailDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberIdsDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberUpdateDTO;
import com.slide_todo.slide_todoApp.dto.member.admin.AdminMemberListDTO;
import com.slide_todo.slide_todoApp.dto.member.admin.AdminMemberUpdateDTO;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import jakarta.annotation.Nullable;

public interface AdminMemberService {

  /*어드민 로그인*/
  ResponseDTO<TokenPairDTO> adminSignin(SigninDTO request);

  /*어드민 페이지에서 유저 리스트 조회*/
  ResponseDTO<AdminMemberListDTO> getAllMembers(long page, long limit, @Nullable String nickname,
      @Nullable String email, @Nullable String createdAfter, @Nullable String createdBefore);

  /*어드민 페이지에서 유저 상세정보 조회*/
  ResponseDTO<AdminMemberDetailDTO> getMemberDetail(Long memberId);

  /*복수 유저 삭제*/
  ResponseDTO<?> deleteMembers(MemberIdsDTO request);

  /*유저 데이터 수정*/
  ResponseDTO<AdminMemberUpdateDTO> updateMember(Long memberId, MemberUpdateDTO request);
}
