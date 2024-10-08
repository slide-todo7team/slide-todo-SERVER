package com.slide_todo.slide_todoApp.service.group;

import com.slide_todo.slide_todoApp.dto.group.GroupInfoDTO;
import com.slide_todo.slide_todoApp.dto.group.admin.GroupIdDTO;
import com.slide_todo.slide_todoApp.dto.group.admin.GroupListDTO;
import com.slide_todo.slide_todoApp.util.response.ResponseDTO;

public interface AdminGroupService {

    ResponseDTO<GroupListDTO> getAllGroups(String nickname,String title, int page, int limit);

    ResponseDTO<?> deleteGroups(GroupIdDTO groupIdDTO);

    ResponseDTO<GroupInfoDTO> getGroupInfo(Long groupId);

    ResponseDTO<?> deleteMemeber(Long groupId, Long memberId);

    ResponseDTO<GroupInfoDTO> updateGroupInfo(Long groupId, String title, String secretCode);

    ResponseDTO<?> changeLeader(Long groupId, Long memberId);
}
