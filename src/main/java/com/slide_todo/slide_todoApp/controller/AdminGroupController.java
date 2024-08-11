package com.slide_todo.slide_todoApp.controller;

import com.slide_todo.slide_todoApp.dto.group.GroupInfoDTO;
import com.slide_todo.slide_todoApp.dto.group.admin.*;
import com.slide_todo.slide_todoApp.service.group.AdminGroupService;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "어드민 그룹 관리 API")
@RequestMapping("/admin/groups")
@RequiredArgsConstructor
public class AdminGroupController {

    private final AdminGroupService adminGroupService;
    private final JwtProvider jwtProvider;

    @GetMapping("")
    @Operation(summary = "그룹 관리 - 그룹 조회")
    public ResponseDTO<GroupListDTO> getAllGroups(
            HttpServletRequest request,
            @Parameter(description = "그룹장 이름") @RequestParam(required = false) String nickname,
            @Parameter(description = "그룹 이름")@RequestParam(required = false) String group,
            @RequestParam int page, @RequestParam int limit){
        Long adminId = jwtProvider.getAdminMemberId(request);
        return adminGroupService.getAllGroups(nickname,group,page,limit);
    }

    @DeleteMapping("")
    @Operation(summary = "그룹 관리 - 그룹 삭제")
    public ResponseDTO<?> deleteGroup(@RequestBody GroupIdDTO groupIdDTO,HttpServletRequest request){
        Long adminId = jwtProvider.getAdminMemberId(request);
        return adminGroupService.deleteGroups(groupIdDTO);
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "그룹 관리 - 그룹 상세 조회")
    public ResponseDTO<GroupInfoListDTO> getGroupInfo(@PathVariable Long groupId,HttpServletRequest request){
        Long adminId = jwtProvider.getAdminMemberId(request);
        return adminGroupService.getGroupInfo(groupId);
    }

    @PatchMapping("/{groupId}")
    @Operation(summary = "그룹 관리 - 제목 or 초대코드 수정")
    public ResponseDTO<GroupInfoListDTO> updateGroupInfo(
            HttpServletRequest request,
            @PathVariable Long groupId,@RequestBody GroupUpdateDTO groupUpdateDTO){
        Long adminId = jwtProvider.getAdminMemberId(request);
        String title = groupUpdateDTO.getTitle();
        String secretCode = groupUpdateDTO.getSecretCode();
        return adminGroupService.updateGroupInfo(groupId,title,secretCode);
    }

    @DeleteMapping("/{groupId}/members/{memberId}")
    @Operation(summary = "그룹 관리 - 멤버 탈퇴")
    public ResponseDTO<?> deleteMember(
            HttpServletRequest request,
            @PathVariable Long groupId,@PathVariable Long memberId){
        Long adminId = jwtProvider.getAdminMemberId(request);
        return adminGroupService.deleteMemeber(groupId,memberId);
    }

    @PatchMapping("/{groupId}/leader/{memberId}")
    @Operation(summary = "그룹 관리 - 방장 변경")
    public ResponseDTO<?> changeLeader(
            HttpServletRequest request,
            @PathVariable Long groupId,@PathVariable Long memberId){
        Long adminId = jwtProvider.getAdminMemberId(request);
        return adminGroupService.changeLeader(groupId,memberId);
    }

}
