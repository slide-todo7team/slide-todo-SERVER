package com.slide_todo.slide_todoApp.dto.group.admin;

import com.slide_todo.slide_todoApp.dto.group.GroupDTO;
import com.slide_todo.slide_todoApp.dto.group.GroupInfoDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GroupListDTO {
    private int current_page;
    private Long total_count;
    private List<GroupInfoDTO> groups;
}
