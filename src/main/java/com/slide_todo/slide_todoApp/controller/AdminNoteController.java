package com.slide_todo.slide_todoApp.controller;

import com.slide_todo.slide_todoApp.dto.note.admin.AdminNoteDetailDTO;
import com.slide_todo.slide_todoApp.service.note.AdminNoteService;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "어드민 노트 관리 API")
@RequestMapping("/admin/note")
@RequiredArgsConstructor
public class AdminNoteController {

  private final AdminNoteService adminNoteService;
  private final JwtProvider jwtProvider;

  @GetMapping("/{note_id}")
  @Operation(summary = "어드민 페이지에서 노트 상세정보 조회",
      description = "노트 상세정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "노트 상세정보 조회 성공"),
      @ApiResponse(responseCode = "404", description = "노트를 찾을 수 없음")
  })
  public ResponseDTO<AdminNoteDetailDTO> getNoteDetailByAdmin(
      HttpServletRequest request,
      @PathVariable(name = "note_id") Long noteId
  ) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminNoteService.getNoteDetailByAdmin(noteId);
  }


  @DeleteMapping("/delete/{note_id}")
  @Operation(summary = "노트 삭제", description = "노트를 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "노트 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "노트를 찾을 수 없음")
  })
  public ResponseDTO<?> deleteNoteByAdmin(
      HttpServletRequest request,
      @PathVariable(name = "note_id") Long noteId
  ) {
    Long adminId = jwtProvider.getAdminMemberId(request);
    return adminNoteService.deleteNoteByAdmin(noteId);
  }
}
