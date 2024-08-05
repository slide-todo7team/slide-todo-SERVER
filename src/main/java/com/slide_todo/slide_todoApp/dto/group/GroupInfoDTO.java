package com.slide_todo.slide_todoApp.dto.group;

import com.slide_todo.slide_todoApp.domain.group.Group;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GroupInfoDTO {
    private Long id;
    private String title;
    private String createUser;
    private String secretCode;
    private List<GroupMemberDTO> members;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @Setter
    @Builder
    public static class GroupMemberDTO {
        private Long id;
        private Boolean isLeader;
        private String name;
        private String color;
        private Integer contributionRank;
    }

    public GroupInfoDTO(Group group) {
        this.id = group.getId();
        this.title = group.getTitle();
        this.createUser = group.getMember().getNickname();
        this.secretCode = group.getSecretCode();
        this.createdAt = group.getCreatedAt();
        this.updatedAt = group.getUpdatedAt();
    }

}
