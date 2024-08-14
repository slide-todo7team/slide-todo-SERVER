package com.slide_todo.slide_todoApp.controller;

import com.slide_todo.slide_todoApp.dto.note.NoteCreateDTO;
import com.slide_todo.slide_todoApp.dto.note.NoteUpdateDTO;
import com.slide_todo.slide_todoApp.service.note.NoteService;
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
@Tag(name = "노트 API")
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

  private final NoteService noteService;
  private final JwtProvider jwtProvider;


  @PostMapping("/create")
  @Operation(summary = "노트 생성", description = "노트를 생성합니다.<br>"
      + "자세한 명세는 노션을 확인해주세요.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "노트 생성 성공"),
      @ApiResponse(responseCode = "400", description = "노트 생성 실패"),
      @ApiResponse(responseCode = "401", description = "그룹 권한 없음")
  })
  public ResponseDTO<?> createNote(
      HttpServletRequest request,
      @RequestBody NoteCreateDTO noteCreateDTO
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    return noteService.createNote(memberId, noteCreateDTO);
  }


  @PatchMapping("/update/{noteId}")
  @Operation(summary = "노트 수정", description = "노트의 내용을 수정합니다.<br>"
      + "자세한 명세는 노션을 확인해주세요.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "노트 수정 성공"),
      @ApiResponse(responseCode = "400", description = "노트 수정 실패"),
      @ApiResponse(responseCode = "401", description = "그룹 권한 없음"),
      @ApiResponse(responseCode = "401", description = "할 일 담당 권한 없음"),
  })
  public ResponseDTO<?> updateNote(
      HttpServletRequest request,
      @Parameter(description = "수정할 노트의 ID") @PathVariable Long noteId,
      @RequestBody NoteUpdateDTO noteUpdateDTO
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    return noteService.updateNote(memberId, noteId, noteUpdateDTO);
  }


  @DeleteMapping("/delete/{noteId}")
  @Operation(summary = "노트 삭제", description = "노트를 삭제합니다.<br>"
      + "자세한 명세는 노션을 확인해주세요.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "노트 삭제 성공")
  })
  public ResponseDTO<?> deleteNote(
      HttpServletRequest request,
      @Parameter(description = "삭제할 노트의 ID") @PathVariable Long noteId
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    return noteService.deleteNote(memberId, noteId);
  }


  @GetMapping("/get/{noteId}")
  @Operation(summary = "단일 노트 조회", description = "노트를 조회합니다.<br>"
      + "자세한 명세는 노션을 확인해주세요.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "노트 조회 성공"),
      @ApiResponse(responseCode = "400", description = "노트 조회 실패")
  })
  public ResponseDTO<?> getOneNote(
      HttpServletRequest request,
      @Parameter(description = "조회할 노트의 ID") @PathVariable Long noteId
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    return noteService.getOneNote(memberId, noteId);
  }


  @GetMapping("/list/goal/{goalId}")
  @Operation(summary = "목표에 따른 노트 리스트 조회", description = "목표에 따른 노트 리스트를 조회합니다.<br>"
      + "자세한 명세는 노션을 확인해주세요.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "목표에 따른 노트 리스트 조회 성공"),
      @ApiResponse(responseCode = "400", description = "목표에 따른 노트 리스트 조회 실패")
  })
  public ResponseDTO<?> getNotesByGoal(
      HttpServletRequest request,
      @Parameter(description = "검색할 페이지 번호") @RequestParam long page,
      @Parameter(description = "한 페이지에 검색할 데이터 수") @RequestParam long limit,
      @Parameter(description = "노트를 조회할 목표의 ID") @PathVariable Long goalId
  ) {
    Long memberId = jwtProvider.getMemberId(request);
    return noteService.getNotesByGoal(memberId, goalId, page, limit);
  }

}
