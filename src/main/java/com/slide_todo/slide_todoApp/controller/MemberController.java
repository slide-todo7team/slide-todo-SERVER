package com.slide_todo.slide_todoApp.controller;

import com.slide_todo.slide_todoApp.dto.jwt.RefreshTokenDTO;
import com.slide_todo.slide_todoApp.dto.jwt.TokenPairDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberDashboardDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberInfoDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberUpdateDTO;
import com.slide_todo.slide_todoApp.dto.member.DuplicationCheckDTO;
import com.slide_todo.slide_todoApp.dto.member.SigninDTO;
import com.slide_todo.slide_todoApp.dto.member.SignupDTO;
import com.slide_todo.slide_todoApp.service.member.MemberService;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "유저 API")
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final JwtProvider jwtProvider;

  @PostMapping("/signup")
  @Operation(summary = "회원가입", description = "회원가입 API입니다.")
  @ApiResponse(responseCode = "201", description = "회원가입 성공")
  public ResponseDTO<TokenPairDTO> signup(
      @RequestBody SignupDTO request
  ) {
    return memberService.signup(request);
  }


  @GetMapping("/nickname/duplicate")
  @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복 확인 API입니다.")
  @ApiResponse(responseCode = "200", description = "닉네임 중복 확인 성공")
  public ResponseDTO<DuplicationCheckDTO> checkNickname(
      @RequestParam String nickname) {
    return memberService.checkNickname(nickname);
  }


  @GetMapping("/email/duplicate")
  @Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인 API입니다.")
  @ApiResponse(responseCode = "200", description = "이메일 중복 확인 성공")
  public ResponseDTO<DuplicationCheckDTO> checkEmail(
      @RequestParam String email) {
    return memberService.checkEmail(email);
  }


  @PostMapping("/signin")
  @Operation(summary = "로그인", description = "로그인 API입니다.")
  @ApiResponse(responseCode = "200", description = "로그인 성공")
  public ResponseDTO<TokenPairDTO> signin(
      @RequestBody SigninDTO request) {
    return memberService.signin(request);
  }


  @PostMapping("/token/refresh")
  @Operation(summary = "토큰 갱신", description = "토큰 갱신 API입니다.")
  @ApiResponse(responseCode = "200", description = "토큰 갱신 성공")
  public ResponseDTO<TokenPairDTO> refreshToken(
      @RequestBody RefreshTokenDTO refreshToken) {
    return memberService.refreshToken(refreshToken);
  }


  @PostMapping("/logout")
  @Operation(summary = "로그아웃", description = "로그아웃 API입니다.")
  @ApiResponse(responseCode = "200", description = "로그아웃 성공")
  public ResponseDTO<?> logout(@RequestBody TokenPairDTO request) {
    return memberService.logout(request);
  }


  @PatchMapping("/info/update")
  @Operation(summary = "회원 정보 수정", description = "회원 정보 수정 API입니다.")
  @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공")
  public ResponseDTO<MemberUpdateDTO> updateMember(
      HttpServletRequest request, @RequestBody MemberUpdateDTO updateDTO) {
    Long memberId = jwtProvider.getMemberId(request);
    return memberService.updateMember(memberId, updateDTO);
  }


  @GetMapping("/withdraw")
  @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 API입니다.")
  @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공")
  public ResponseDTO<?> deleteMember(HttpServletRequest request) {
    Long memberId = jwtProvider.getMemberId(request);
    return memberService.deleteMember(memberId);
  }


  @GetMapping("/info")
  @Operation(summary = "회원 정보 조회", description = "회원 정보 조회 API입니다.")
  @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공")
  public ResponseDTO<MemberInfoDTO> getMemberInfo(HttpServletRequest request) {
    Long memberId = jwtProvider.getMemberId(request);
    return memberService.getMemberInfo(memberId);
  }


  @GetMapping("/dashboard")
  @Operation(summary = "회원 대시보드 조회", description = "회원 대시보드 조회 API입니다.")
  @ApiResponse(responseCode = "200", description = "회원 대시보드 조회 성공")
  public ResponseDTO<MemberDashboardDTO> getMemberDashboard(HttpServletRequest request) {
    Long memberId = jwtProvider.getMemberId(request);
    return memberService.getMemberDashboard(memberId);
  }
}
