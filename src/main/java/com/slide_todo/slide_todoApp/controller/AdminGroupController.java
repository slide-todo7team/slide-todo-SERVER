package com.slide_todo.slide_todoApp.controller;

import com.slide_todo.slide_todoApp.dto.group.GroupInfoDTO;
import com.slide_todo.slide_todoApp.dto.group.admin.*;
import com.slide_todo.slide_todoApp.service.group.AdminGroupService;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "어드민 그룹 관리 API")
@RequestMapping("/admin/groups")
@RequiredArgsConstructor
public class AdminGroupController {

    private final AdminGroupService adminGroupService;

    @GetMapping("")
    @Operation(summary = "그룹 관리 - 그룹 조회")
    public ResponseDTO<GroupListDTO> getAllGroups(
            @Parameter(description = "그룹장 이름") @RequestParam(required = false) String nickname,
            @Parameter(description = "그룹 이름")@RequestParam(required = false) String group,
            @RequestParam int page, @RequestParam int limit){
        return adminGroupService.getAllGroups(nickname,group,page,limit);
    }

    @DeleteMapping("")
    @Operation(summary = "그룹 관리 - 그룹 삭제")
    public ResponseDTO<?> deleteGroup(@RequestBody GroupIdDTO groupIdDTO){
        return adminGroupService.deleteGroups(groupIdDTO);
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "그룹 관리 - 그룹 상세 조회")
    public ResponseDTO<GroupInfoListDTO> getGroupInfo(@PathVariable Long groupId){
        return adminGroupService.getGroupInfo(groupId);
    }

    @PatchMapping("/{groupId}")
    @Operation(summary = "그룹 관리 - 제목 or 초대코드 수정")
    public ResponseDTO<GroupInfoListDTO> updateGroupInfo(@PathVariable Long groupId,@RequestBody GroupUpdateDTO groupUpdateDTO){
        String title = groupUpdateDTO.getTitle();
        String secretCode = groupUpdateDTO.getSecretCode();
        return adminGroupService.updateGroupInfo(groupId,title,secretCode);
    }

    @DeleteMapping("/{groupId}/members/{memberId}")
    @Operation(summary = "그룹 관리 - 멤버 탈퇴")
    public ResponseDTO<?> deleteMember(@PathVariable Long groupId,@PathVariable Long memberId){
        return adminGroupService.deleteMemeber(groupId,memberId);
    }

    @PatchMapping("/{groupId}/leader/{memberId}")
    @Operation(summary = "그룹 관리 - 방장 변경")
    public ResponseDTO<?> changeLeader(@PathVariable Long groupId,@PathVariable Long memberId){
        return adminGroupService.changeLeader(groupId,memberId);
    }

}
