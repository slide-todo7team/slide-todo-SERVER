package com.slide_todo.slide_todoApp.controller;

import com.slide_todo.slide_todoApp.dto.group.*;
import com.slide_todo.slide_todoApp.dto.group.admin.GroupUpdateDTO;
import com.slide_todo.slide_todoApp.repository.group.GroupRepository;
import com.slide_todo.slide_todoApp.repository.member.MemberRepository;
import com.slide_todo.slide_todoApp.service.group.GroupService;
import com.slide_todo.slide_todoApp.util.jwt.JwtProvider;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@Tag(name = "그룹 API")
@RequiredArgsConstructor
public class GroupController {

    private final JwtProvider jwtProvider;
    private final GroupService groupService;
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    @PostMapping("")
    @Operation(summary = "그룹 생성")
    public ResponseDTO<GroupResponseDTO> createGroup(@RequestBody GroupTitleDTO groupTitleDTO, HttpServletRequest request) {
        Long memberId = jwtProvider.getMemberId(request);
        String title = groupTitleDTO.getTitle();
        return groupService.createGroup(title, memberId);
    }

    @PostMapping("/members")
    @Operation(summary = "그룹 참여")
    public ResponseDTO<GroupResponseDTO> joinGroup(@RequestBody GroupCodeDTO groupCodeDTO,HttpServletRequest request){
        Long memberId = jwtProvider.getMemberId(request);
        return groupService.joinGroup(groupCodeDTO.getSecretCode(), memberId);
    }

    @GetMapping("")
    @Operation(summary = "그룹 리스트 조회",description = "본인이 속한 그룹 리스트 조회")
    public ResponseDTO<List<GroupDTO>> getGroupList(HttpServletRequest request){
        Long memberId = jwtProvider.getMemberId(request);
        return groupService.getGroupList(memberId);
    }

    @GetMapping("/{groupId}")
    @Operation(summary = "그룹 상세 조회")
    public ResponseDTO<GroupInfoDTO> getGroupInfo(@PathVariable Long groupId){
        return groupService.getGroupInfo(groupId);
    }

    @DeleteMapping("/{groupId}")
    @Operation(summary = "그룹 삭제")
    public ResponseDTO<?> deleteGroup(@PathVariable Long groupId){
        return groupService.deleteGroup(groupId);
    }

    @DeleteMapping("/members/{groupId}")
    @Operation(summary = "그룹 탈퇴")
    public ResponseDTO<?> leaveGroup(@PathVariable Long groupId,HttpServletRequest request){
        Long memberId = jwtProvider.getMemberId(request);
        return groupService.leaveGroup(groupId,memberId);
    }

    @GetMapping("/code/{groupId}")
    @Operation(summary = "초대코드 발급")
    public ResponseDTO<GroupCodeDTO> getNewSecretCode(@PathVariable Long groupId){
        return groupService.getNewSecretCode(groupId);
    }

    @PatchMapping("/code/{groupId}")
    @Operation(summary = "새로운 초대코드 저장")
    public ResponseDTO<GroupInfoDTO> saveNewSecretCode(@PathVariable Long groupId, @RequestBody GroupCodeDTO groupCodeDTO){
        String secretCode = groupCodeDTO.getSecretCode();
        return groupService.saveNewSecretCode(groupId,secretCode);
    }

    @PatchMapping("/{groupId}")
    @Operation(summary = "그룹 이름 / 초대코드 변경")
    public ResponseDTO<GroupInfoDTO> updateGroup(@PathVariable Long groupId, @RequestBody GroupUpdateDTO groupUpdateDTO){
        String title = groupUpdateDTO.getTitle();
        String secretCode = groupUpdateDTO.getSecretCode();
        return groupService.updateGroup(groupId,title,secretCode);
    }

    @DeleteMapping("/members/{groupId}/{memberId}")
    @Operation(summary = "그룹 멤버 탈퇴시키기")
    public ResponseDTO<?> deleteMember(
            HttpServletRequest request,
            @PathVariable Long groupId, @PathVariable Long memberId){
        Long leaderId = jwtProvider.getMemberId(request);
        return groupService.deleteMember(groupId, memberId,leaderId);
    }

    @PatchMapping("/{groupId}/leader/{memberId}")
    @Operation(summary = "그룹 방장 변경")
    public ResponseDTO<?> changeLeader(
            HttpServletRequest request,
            @PathVariable Long groupId, @PathVariable Long memberId){

        Long leaderId = jwtProvider.getMemberId(request);
        return groupService.changeLeader(groupId,memberId,leaderId);
    }
}
