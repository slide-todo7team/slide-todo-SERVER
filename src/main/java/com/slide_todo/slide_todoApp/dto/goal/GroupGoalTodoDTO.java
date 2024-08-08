package com.slide_todo.slide_todoApp.dto.goal;

import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import lombok.*;
import com.slide_todo.slide_todoApp.dto.goal.GroupProgressDTO;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupGoalTodoDTO {
    private String title;
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private GroupProgressDTO progress;
    private List<GroupTodoDTO> todos;


    @Getter
    @Setter
    @Builder
    public static class GroupTodoDTO {
        private Long noteId; //연결된 노트 Id
        private Long id; //할일 id
        private String title;
        private Boolean done;
        private String assignee;
    }


}