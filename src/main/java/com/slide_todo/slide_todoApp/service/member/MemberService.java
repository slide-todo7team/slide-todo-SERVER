package com.slide_todo.slide_todoApp.service.member;

import com.slide_todo.slide_todoApp.dto.jwt.RefreshTokenDTO;
import com.slide_todo.slide_todoApp.dto.jwt.TokenPairDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberUpdateDTO;
import com.slide_todo.slide_todoApp.dto.member.DuplicationCheckDTO;
import com.slide_todo.slide_todoApp.dto.member.SigninDTO;
import com.slide_todo.slide_todoApp.dto.member.SignupDTO;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;

public interface MemberService {

  ResponseDTO<TokenPairDTO> signup(SignupDTO request);

  ResponseDTO<TokenPairDTO> signin(SigninDTO request);

  ResponseDTO<DuplicationCheckDTO> checkNickname(String nickname);

  ResponseDTO<DuplicationCheckDTO> checkEmail(String email);

  ResponseDTO<TokenPairDTO> refreshToken(RefreshTokenDTO request);

  ResponseDTO<MemberUpdateDTO> updateMember(Long memberId, MemberUpdateDTO request);

  ResponseDTO<?> logout(TokenPairDTO request);

  ResponseDTO<?> deleteMember(Long memberId);


}
