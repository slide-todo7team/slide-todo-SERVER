package com.slide_todo.slide_todoApp.service.group;

import com.slide_todo.slide_todoApp.dto.group.*;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;

import java.util.List;

public interface GroupService {
    ResponseDTO<GroupResponseDTO> createGroup(String title, Long memberId);

    ResponseDTO<GroupResponseDTO> joinGroup(String secretCode, Long memberId);

    ResponseDTO<List<GroupDTO>> getGroupList(Long memberId);

    ResponseDTO<GroupInfoDTO> getGroupInfo(Long groupId);

    ResponseDTO<?> deleteGroup(Long groupId);

    ResponseDTO<?> leaveGroup(Long groupId, Long memberId);

    ResponseDTO<GroupCodeDTO> getNewSecretCode(Long groupId);

    ResponseDTO<GroupInfoDTO> saveNewSecretCode(Long groupId, String secretCode);

    ResponseDTO<GroupInfoDTO> updateGroup(Long groupId, String title, String secretCode);

    ResponseDTO<?> deleteMember(Long groupId, Long memberId, Long leaderId);

    ResponseDTO<?> changeLeader(Long groupId, Long memberId, Long leaderId);
}
