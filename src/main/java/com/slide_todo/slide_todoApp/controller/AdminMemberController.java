package com.slide_todo.slide_todoApp.controller;

import com.slide_todo.slide_todoApp.dto.jwt.TokenPairDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberIdsDTO;
import com.slide_todo.slide_todoApp.dto.member.MemberUpdateDTO;
import com.slide_todo.slide_todoApp.dto.member.SigninDTO;
import com.slide_todo.slide_todoApp.dto.member.admin.AdminMemberDetailDTO;
import com.slide_todo.slide_todoApp.dto.member.admin.AdminMemberListDTO;
import com.slide_todo.slide_todoApp.service.member.AdminMemberService;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "어드민 유저 관리 API")
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class AdminMemberController {

  private final JwtProvider jwtProvider;
  private final AdminMemberService adminMemberService;


  @PostMapping("/signin")
  @Operation(summary = "로그인", description = "로그인 API입니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "로그인 성공"),
      @ApiResponse(responseCode = "403", description = "어드민 권한 없음")
  })
  public ResponseDTO<TokenPairDTO> adminSignin(
      @RequestBody SigninDTO request
  ) {
    return adminMemberService.adminSignin(request);
  }

  @GetMapping("")
  @Operation(summary = "어드민 페이지에서 유저 리스트 조회", description = "유저 리스트를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "유저 리스트 조회 성공")
  public ResponseDTO<AdminMemberListDTO> getAllMembers(
      HttpServletRequest request,
      @Parameter(description = "검색할 페이지 번호") @RequestParam long page,
      @Parameter(description = "한 페이지에 검색할 데이터 수") @RequestParam long limit,
      @Parameter(description = "검색할 닉네임 조건") @RequestParam(required = false) String nickname,
      @Parameter(description = "검색할 이메일 조건") @RequestParam(required = false) String email,
      @Parameter(description = "검색할 계정 생성일 조건(~이후) YYYY-MM-DD")
      @RequestParam(required = false) String createdAfter,
      @Parameter(description = "검색할 계정 생성일 조건(~이전) YYYY-MM-DD")
      @RequestParam(required = false) String createdBefore
  ) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminMemberService.getAllMembers(page, limit, nickname, email, createdAfter, createdBefore);
  }


  @GetMapping("/detail/{memberId}")
  @Operation(summary = "어드민 페이지에서 유저 상세정보 조회", description = "유저 상세정보를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "유저 상세정보 조회 성공")
  public ResponseDTO<AdminMemberDetailDTO> getMemberDetailInformation(
      HttpServletRequest request,
      @PathVariable Long memberId
  ) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminMemberService.getMemberDetail(memberId);
  }


  @DeleteMapping("/delete")
  @Operation(summary = "복수 유저 삭제", description = "복수 유저를 삭제합니다.")
  @ApiResponse(responseCode = "200", description = "복수 유저 삭제 성공")
  public ResponseDTO<?> deleteMembers(
      HttpServletRequest request, @RequestBody MemberIdsDTO memberIdsDTO
  ) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminMemberService.deleteMembers(memberIdsDTO);
  }


  @PatchMapping("/update/{memberId}")
  @Operation(summary = "유저 데이터 수정", description = "유저 데이터를 수정합니다.")
  @ApiResponse(responseCode = "200", description = "유저 데이터 수정 성공")
  public ResponseDTO<AdminMemberDetailDTO> updateMemberInformation(
      HttpServletRequest request,
      @PathVariable Long memberId,
      @RequestBody MemberUpdateDTO memberUpdateDTO
  ) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminMemberService.updateMember(memberId, memberUpdateDTO);
  }
}
